# lyricget

`lyricget` 是一个单一功能的小项目：

- 接受搜索词
- 返回歌词

不提供：

- SMTC
- HUD
- Minecraft 集成
- 播放器控制

## 文档

- [开发指南](DEVELOPMENT.md)
- [许可证](LICENSE)

## 功能边界

输入：

- 歌名
- 可选歌手名

输出：

- 命中的歌词文本
- 命中的提供者名称

如果没有结果，返回 `NO_RESULT`。

## 运行方式

### 构建

```bash
./gradlew.bat build
```

### 运行

```bash
./gradlew.bat run --args="歌名 歌手"
```

示例：

```bash
./gradlew.bat run --args="晴天 周杰伦"
```

## 项目结构

```text
src/main/java/com/lyricget/
  LyricGetMain.java
  LyricsFetcher.java
  LyricResult.java
  LyricSearchConfig.java
  lyrics/
    AbstractLyricProvider.java
    LyricProvider.java
    NeteaseProvider.java
    QqProvider.java
    LrclibProvider.java
    KugouProvider.java
    QishuiProvider.java
    MusixmatchProvider.java
    CustomProvider.java
```

## 设计原则

- 只保留“歌词搜索”这一件事
- provider 独立放在 `lyrics/`
- 不耦合任何上层 UI 或客户端环境
- 构建后可直接作为独立 Java 项目使用

## 输出约定

成功时输出：

```text
PROVIDER=<provider>
<lyrics>
```

失败时输出：

```text
NO_RESULT
```

## 许可证说明

此项目不是 MIT。

当前使用自定义限制性许可证，明确包含：

- 不允许二次售卖
- 许可证会随时更改，但新协议不追溯到更早 fork 出去的仓库
- 如果使用此项目开发 mod，必须注明歌词获取来源
- 项目文件内必须包含 LICENSE
