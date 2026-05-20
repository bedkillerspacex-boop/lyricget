# Mod 集成文档

`lyricget` 不是一个 Minecraft mod，也不处理 SMTC、HUD、播放器状态或 UI。

它是给其他 mod 集成使用的歌词获取模块：上层 mod 提供歌名和歌手名，`lyricget` 并行请求多个歌词源，然后返回第一个可用歌词结果。

## 集成边界

集成方负责：

- 监听或读取当前播放歌曲
- 决定什么时候发起歌词搜索
- 缓存搜索结果
- 在 HUD、聊天栏、屏幕组件或其他 UI 中显示歌词
- 处理 Minecraft 客户端线程和渲染线程

`lyricget` 负责：

- 接收 `title` 和可选 `artist`
- 调用已启用的歌词源
- 返回 `LyricResult`
- 在没有可用歌词时返回 `null`

## 引入方式

当前项目是普通 Gradle Java 项目，尚未发布到 Maven 仓库。其他 mod 项目可以用下面几种方式接入。

### 方式一：作为源码模块引入

适合同一个仓库或本地开发。

在上层 mod 的 `settings.gradle` 中加入：

```gradle
includeBuild("../lyricget")
```

然后在上层 mod 的 `build.gradle` 中依赖：

```gradle
dependencies {
    implementation "com.southside:lyricget:1.0.0"
}
```

路径按实际目录调整。

### 方式二：复制源码包

适合不想维护多模块构建的小型 mod。

复制以下包到上层 mod 的源码目录：

```text
com/southside/lyricget/
```

同时确保上层 mod 也包含 Gson 依赖：

```gradle
dependencies {
    implementation "com.google.code.gson:gson:2.11.0"
}
```

如果使用 Shadow、Jar-in-Jar 或 loader 自带的依赖打包方式，按目标 mod loader 的规则处理依赖。

## Skid 到其他 mod 后如何通信

如果把 `lyricget` 源码复制进其他 mod，本项目就不再是一个独立 mod。此时不要设计“两个 mod 互发消息”，而是把它当成上层 mod 内部的一个 Java 服务。

推荐边界：

```text
播放状态模块 -> 歌词服务适配层 -> lyricget -> 歌词服务适配层 -> UI/缓存模块
```

也就是：

- 上层 mod 负责拿到当前播放信息
- 上层 mod 调用 `LyricsFetcher.searchParallelDetailed(...)`
- `lyricget` 返回 `CompletableFuture<LyricResult>`
- 上层 mod 在回调里接收结果
- 上层 mod 再把结果交给自己的缓存、HUD、聊天栏或其他 UI

示例适配层：

```java
import com.southside.lyricget.LyricResult;
import com.southside.lyricget.LyricSearchConfig;
import com.southside.lyricget.LyricsFetcher;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class LyricGetBridge {
    private final LyricSearchConfig config = LyricSearchConfig.defaults();

    public CompletableFuture<Void> requestLyrics(
            String title,
            String artist,
            Consumer<LyricMessage> receiver
    ) {
        return LyricsFetcher.searchParallelDetailed(title, artist, config)
                .thenAccept(result -> {
                    if (result == null) {
                        receiver.accept(LyricMessage.noResult(title, artist));
                        return;
                    }

                    receiver.accept(LyricMessage.found(
                            title,
                            artist,
                            result.providerName(),
                            result.lyrics()
                    ));
                });
    }
}
```

上层 mod 自己定义消息对象：

```java
public record LyricMessage(
        String title,
        String artist,
        String provider,
        String lyrics,
        boolean found
) {
    public static LyricMessage found(String title, String artist, String provider, String lyrics) {
        return new LyricMessage(title, artist, provider, lyrics, true);
    }

    public static LyricMessage noResult(String title, String artist) {
        return new LyricMessage(title, artist, "", "", false);
    }
}
```

调用方式：

```java
LyricGetBridge bridge = new LyricGetBridge();

bridge.requestLyrics(currentTitle, currentArtist, message -> {
    if (!message.found()) {
        // 写入未命中缓存，或清空当前歌词显示。
        return;
    }

    // 写入你的 mod 缓存或状态容器。
    // 如果要更新 Minecraft UI，请切回客户端线程后再改 UI 状态。
    String lyrics = message.lyrics();
    String provider = message.provider();
});
```

这就是“发送”和“接收”：

- 发送：上层 mod 调用 `requestLyrics(title, artist, receiver)`
- 接收：`receiver.accept(message)` 收到歌词结果

这里的消息是进程内 Java 对象，不是 Minecraft 网络包，也不是 mod channel。

只有在你把 `lyricget` 改造成独立 mod，并且希望其他 mod 在运行时通过 mod loader 调用它时，才需要额外设计 Fabric API、Forge capability、事件总线或网络 channel。当前项目不提供这些层。

## 调用入口

主入口是：

```java
LyricsFetcher.searchParallelDetailed(title, artist, config)
```

示例：

```java
import com.southside.lyricget.LyricResult;
import com.southside.lyricget.LyricSearchConfig;
import com.southside.lyricget.LyricsFetcher;

import java.util.concurrent.CompletableFuture;

public final class ModLyricService {
    public CompletableFuture<LyricResult> search(String title, String artist) {
        LyricSearchConfig config = LyricSearchConfig.defaults();
        config.timeoutMs = 4000;
        config.enableMusixmatch = false;
        config.enableCustom = false;

        return LyricsFetcher.searchParallelDetailed(title, artist, config);
    }
}
```

返回值约定：

- `CompletableFuture<LyricResult>` 完成且结果不为 `null`：找到歌词
- `CompletableFuture<LyricResult>` 完成且结果为 `null`：没有可用结果
- `LyricResult.lyrics()`：歌词文本，可能是 LRC，也可能是普通歌词
- `LyricResult.providerName()`：命中的歌词源名称

## 线程要求

歌词搜索会访问网络。集成到 Minecraft mod 时，不要在客户端主线程或渲染线程里阻塞等待。

推荐：

```java
LyricsFetcher.searchParallelDetailed(title, artist, config)
        .thenAccept(result -> {
            if (result == null) {
                return;
            }

            String lyrics = result.lyrics();
            String provider = result.providerName();

            // 在这里把结果交给你的缓存、状态容器或 UI 更新调度器。
            // 如果需要回到 Minecraft 客户端线程，请使用你的 loader/client 提供的调度 API。
        });
```

不推荐：

```java
LyricResult result = LyricsFetcher.searchParallelDetailed(title, artist, config).join();
```

除非你已经确认这段代码运行在后台线程。

## 配置项

使用默认配置：

```java
LyricSearchConfig config = LyricSearchConfig.defaults();
```

可调整字段：

```java
config.enableNetease = true;
config.enableQQ = true;
config.enableLrclib = true;
config.enableKugou = true;
config.enableQishui = true;
config.enableMusixmatch = false;
config.enableCustom = false;
config.timeoutMs = 4000;
config.preferChinese = true;
```

自定义歌词源：

```java
config.enableCustom = true;
config.customUrl = "https://api.example.com/lyrics?title=%title%&artist=%artist%";
```

`%title%` 和 `%artist%` 会被替换为 URL 编码后的搜索词。

Musixmatch：

```java
config.enableMusixmatch = true;
config.musixmatchApiKey = "your-api-key";
```

不要把私有 API key 写死在公开仓库里。

## 搜索词建议

上层 mod 应该尽量传入干净的歌曲信息：

- `title` 只放歌名，不要混入播放器状态、文件扩展名或歌词行
- `artist` 可为空，但有歌手名时命中率更高
- 本地文件名建议先在上层 mod 中清洗后再传入

`lyricget` 只会做基础空白字符归一化，不负责复杂的曲目信息解析。

## 缓存建议

`lyricget` 不内置缓存。mod 集成时建议用上层缓存避免重复请求。

推荐缓存键：

```text
normalizedTitle + "\n" + normalizedArtist
```

建议缓存内容：

- 歌词文本
- provider 名称
- 查询时间
- 是否为未命中结果

未命中结果也建议短时间缓存，避免同一首歌反复请求所有歌词源。

## 许可证要求

此项目使用自定义限制性许可证，不是 MIT。

集成到 mod 或派生项目时必须遵守 `LICENSE`，尤其是：

- 不允许二次售卖或包含在付费产品、付费服务中
- 分发源码或派生代码时必须包含 LICENSE 文件
- 使用此项目开发 mod 时，必须明确说明歌词获取来源

建议在上层 mod 的 README 或鸣谢页面写明：

```text
Lyrics are fetched through lyricget.
```

## lyricget 内部维护

如果你是在维护 `lyricget` 本身，而不是在其他 mod 中集成它：

1. 在 `src/main/java/com/southside/lyricget/lyrics/` 下新增 `XXXProvider`
2. 继承 `AbstractLyricProvider` 或实现 `LyricProvider`
3. 在 `LyricsFetcher.providers()` 中注册
4. 成功时返回 LRC 或普通歌词文本
5. 失败、无结果或不可接受结果返回 `null`

维护规则：

- 不要把 Minecraft、SMTC、HUD 或播放器控制代码放进来
- 不要在 provider 中加入上层 UI 逻辑
- provider 应保持可独立替换
- 公共入口优先保持向后兼容，避免破坏已有 mod 集成

## 构建验证

在 `lyricget` 仓库中运行：

```bash
./gradlew.bat build
```

命令行调试：

```bash
./gradlew.bat run --args="晴天 周杰伦"
```
