# 开发指南

## 目标

此项目只负责：

- 接收搜索词
- 向多个歌词源请求
- 返回最先拿到的可用歌词

此项目不负责：

- SMTC
- Minecraft
- UI
- 缓存系统
- 播放状态同步

## 核心入口

- `LyricGetMain`：命令行入口
- `LyricsFetcher`：统一搜索入口
- `LyricSearchConfig`：提供者开关与超时配置

## 新增歌词源

1. 在 `lyrics/` 下新增一个 `XXXProvider`
2. 继承 `AbstractLyricProvider` 或直接实现 `LyricProvider`
3. 在 `LyricsFetcher.providers()` 中注册
4. 返回 LRC 或普通歌词文本；失败返回 `null`

## 维护规则

- 不要把 Minecraft 或 SMTC 相关代码放进来
- 不要把歌词显示逻辑放进来
- 不要把播放器状态逻辑放进来
- 所有 provider 都应该是可独立替换的

## 编译

```bash
./gradlew.bat build
```

## 运行

```bash
./gradlew.bat run --args="歌名 歌手"
```
