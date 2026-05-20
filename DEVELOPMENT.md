# Mod 闆嗘垚鏂囨。

`lyricget` 涓嶆槸涓€涓?Minecraft mod锛屼篃涓嶅鐞?SMTC銆丠UD銆佹挱鏀惧櫒鐘舵€佹垨 UI銆?

瀹冩槸缁欏叾浠?mod 闆嗘垚浣跨敤鐨勬瓕璇嶈幏鍙栨ā鍧楋細涓婂眰 mod 鎻愪緵姝屽悕鍜屾瓕鎵嬪悕锛宍lyricget` 骞惰璇锋眰澶氫釜姝岃瘝婧愶紝鐒跺悗杩斿洖绗竴涓彲鐢ㄦ瓕璇嶇粨鏋溿€?

## 闆嗘垚杈圭晫

闆嗘垚鏂硅礋璐ｏ細

- 鐩戝惉鎴栬鍙栧綋鍓嶆挱鏀炬瓕鏇?
- 鍐冲畾浠€涔堟椂鍊欏彂璧锋瓕璇嶆悳绱?
- 缂撳瓨鎼滅储缁撴灉
- 鍦?HUD銆佽亰澶╂爮銆佸睆骞曠粍浠舵垨鍏朵粬 UI 涓樉绀烘瓕璇?
- 澶勭悊 Minecraft 瀹㈡埛绔嚎绋嬪拰娓叉煋绾跨▼

`lyricget` 璐熻矗锛?

- 鎺ユ敹 `title` 鍜屽彲閫?`artist`
- 璋冪敤宸插惎鐢ㄧ殑姝岃瘝婧?
- 杩斿洖 `LyricResult`
- 鍦ㄦ病鏈夊彲鐢ㄦ瓕璇嶆椂杩斿洖 `null`

## 寮曞叆鏂瑰紡

褰撳墠椤圭洰鏄櫘閫?Gradle Java 椤圭洰锛屽皻鏈彂甯冨埌 Maven 浠撳簱銆傚叾浠?mod 椤圭洰鍙互鐢ㄤ笅闈㈠嚑绉嶆柟寮忔帴鍏ャ€?

### 鏂瑰紡涓€锛氫綔涓烘簮鐮佹ā鍧楀紩鍏?

閫傚悎鍚屼竴涓粨搴撴垨鏈湴寮€鍙戙€?

鍦ㄤ笂灞?mod 鐨?`settings.gradle` 涓姞鍏ワ細

```gradle
includeBuild("../lyricget")
```

鐒跺悗鍦ㄤ笂灞?mod 鐨?`build.gradle` 涓緷璧栵細

```gradle
dependencies {
    implementation "com.lyricget:lyricget:1.0.0"
}
```

璺緞鎸夊疄闄呯洰褰曡皟鏁淬€?

### 鏂瑰紡浜岋細澶嶅埗婧愮爜鍖?

閫傚悎涓嶆兂缁存姢澶氭ā鍧楁瀯寤虹殑灏忓瀷 mod銆?

澶嶅埗浠ヤ笅鍖呭埌涓婂眰 mod 鐨勬簮鐮佺洰褰曪細

```text
com/lyricget/
```

鍚屾椂纭繚涓婂眰 mod 涔熷寘鍚?Gson 渚濊禆锛?

```gradle
dependencies {
    implementation "com.google.code.gson:gson:2.11.0"
}
```

濡傛灉浣跨敤 Shadow銆丣ar-in-Jar 鎴?loader 鑷甫鐨勪緷璧栨墦鍖呮柟寮忥紝鎸夌洰鏍?mod loader 鐨勮鍒欏鐞嗕緷璧栥€?

## Skid 鍒板叾浠?mod 鍚庡浣曢€氫俊

濡傛灉鎶?`lyricget` 婧愮爜澶嶅埗杩涘叾浠?mod锛屾湰椤圭洰灏变笉鍐嶆槸涓€涓嫭绔?mod銆傛鏃朵笉瑕佽璁♀€滀袱涓?mod 浜掑彂娑堟伅鈥濓紝鑰屾槸鎶婂畠褰撴垚涓婂眰 mod 鍐呴儴鐨勪竴涓?Java 鏈嶅姟銆?

鎺ㄨ崘杈圭晫锛?

```text
鎾斁鐘舵€佹ā鍧?-> 姝岃瘝鏈嶅姟閫傞厤灞?-> lyricget -> 姝岃瘝鏈嶅姟閫傞厤灞?-> UI/缂撳瓨妯″潡
```

涔熷氨鏄細

- 涓婂眰 mod 璐熻矗鎷垮埌褰撳墠鎾斁淇℃伅
- 涓婂眰 mod 璋冪敤 `LyricsFetcher.searchParallelDetailed(...)`
- `lyricget` 杩斿洖 `CompletableFuture<LyricResult>`
- 涓婂眰 mod 鍦ㄥ洖璋冮噷鎺ユ敹缁撴灉
- 涓婂眰 mod 鍐嶆妸缁撴灉浜ょ粰鑷繁鐨勭紦瀛樸€丠UD銆佽亰澶╂爮鎴栧叾浠?UI

绀轰緥閫傞厤灞傦細

```java
import com.lyricget.LyricResult;
import com.lyricget.LyricSearchConfig;
import com.lyricget.LyricsFetcher;

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

涓婂眰 mod 鑷繁瀹氫箟娑堟伅瀵硅薄锛?

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

璋冪敤鏂瑰紡锛?

```java
LyricGetBridge bridge = new LyricGetBridge();

bridge.requestLyrics(currentTitle, currentArtist, message -> {
    if (!message.found()) {
        // 鍐欏叆鏈懡涓紦瀛橈紝鎴栨竻绌哄綋鍓嶆瓕璇嶆樉绀恒€?
        return;
    }

    // 鍐欏叆浣犵殑 mod 缂撳瓨鎴栫姸鎬佸鍣ㄣ€?
    // 濡傛灉瑕佹洿鏂?Minecraft UI锛岃鍒囧洖瀹㈡埛绔嚎绋嬪悗鍐嶆敼 UI 鐘舵€併€?
    String lyrics = message.lyrics();
    String provider = message.provider();
});
```

杩欏氨鏄€滃彂閫佲€濆拰鈥滄帴鏀垛€濓細

- 鍙戦€侊細涓婂眰 mod 璋冪敤 `requestLyrics(title, artist, receiver)`
- 鎺ユ敹锛歚receiver.accept(message)` 鏀跺埌姝岃瘝缁撴灉

杩欓噷鐨勬秷鎭槸杩涚▼鍐?Java 瀵硅薄锛屼笉鏄?Minecraft 缃戠粶鍖咃紝涔熶笉鏄?mod channel銆?

鍙湁鍦ㄤ綘鎶?`lyricget` 鏀归€犳垚鐙珛 mod锛屽苟涓斿笇鏈涘叾浠?mod 鍦ㄨ繍琛屾椂閫氳繃 mod loader 璋冪敤瀹冩椂锛屾墠闇€瑕侀澶栬璁?Fabric API銆丗orge capability銆佷簨浠舵€荤嚎鎴栫綉缁?channel銆傚綋鍓嶉」鐩笉鎻愪緵杩欎簺灞傘€?

## 璋冪敤鍏ュ彛

涓诲叆鍙ｆ槸锛?

```java
LyricsFetcher.searchParallelDetailed(title, artist, config)
```

绀轰緥锛?

```java
import com.lyricget.LyricResult;
import com.lyricget.LyricSearchConfig;
import com.lyricget.LyricsFetcher;

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

杩斿洖鍊肩害瀹氾細

- `CompletableFuture<LyricResult>` 瀹屾垚涓旂粨鏋滀笉涓?`null`锛氭壘鍒版瓕璇?
- `CompletableFuture<LyricResult>` 瀹屾垚涓旂粨鏋滀负 `null`锛氭病鏈夊彲鐢ㄧ粨鏋?
- `LyricResult.lyrics()`锛氭瓕璇嶆枃鏈紝鍙兘鏄?LRC锛屼篃鍙兘鏄櫘閫氭瓕璇?
- `LyricResult.providerName()`锛氬懡涓殑姝岃瘝婧愬悕绉?

## 绾跨▼瑕佹眰

姝岃瘝鎼滅储浼氳闂綉缁溿€傞泦鎴愬埌 Minecraft mod 鏃讹紝涓嶈鍦ㄥ鎴风涓荤嚎绋嬫垨娓叉煋绾跨▼閲岄樆濉炵瓑寰呫€?

鎺ㄨ崘锛?

```java
LyricsFetcher.searchParallelDetailed(title, artist, config)
        .thenAccept(result -> {
            if (result == null) {
                return;
            }

            String lyrics = result.lyrics();
            String provider = result.providerName();

            // 鍦ㄨ繖閲屾妸缁撴灉浜ょ粰浣犵殑缂撳瓨銆佺姸鎬佸鍣ㄦ垨 UI 鏇存柊璋冨害鍣ㄣ€?
            // 濡傛灉闇€瑕佸洖鍒?Minecraft 瀹㈡埛绔嚎绋嬶紝璇蜂娇鐢ㄤ綘鐨?loader/client 鎻愪緵鐨勮皟搴?API銆?
        });
```

涓嶆帹鑽愶細

```java
LyricResult result = LyricsFetcher.searchParallelDetailed(title, artist, config).join();
```

闄ら潪浣犲凡缁忕‘璁よ繖娈典唬鐮佽繍琛屽湪鍚庡彴绾跨▼銆?

## 閰嶇疆椤?

浣跨敤榛樿閰嶇疆锛?

```java
LyricSearchConfig config = LyricSearchConfig.defaults();
```

鍙皟鏁村瓧娈碉細

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

鑷畾涔夋瓕璇嶆簮锛?

```java
config.enableCustom = true;
config.customUrl = "https://api.example.com/lyrics?title=%title%&artist=%artist%";
```

`%title%` 鍜?`%artist%` 浼氳鏇挎崲涓?URL 缂栫爜鍚庣殑鎼滅储璇嶃€?

Musixmatch锛?

```java
config.enableMusixmatch = true;
config.musixmatchApiKey = "your-api-key";
```

涓嶈鎶婄鏈?API key 鍐欐鍦ㄥ叕寮€浠撳簱閲屻€?

## 鎼滅储璇嶅缓璁?

涓婂眰 mod 搴旇灏介噺浼犲叆骞插噣鐨勬瓕鏇蹭俊鎭細

- `title` 鍙斁姝屽悕锛屼笉瑕佹贩鍏ユ挱鏀惧櫒鐘舵€併€佹枃浠舵墿灞曞悕鎴栨瓕璇嶈
- `artist` 鍙负绌猴紝浣嗘湁姝屾墜鍚嶆椂鍛戒腑鐜囨洿楂?
- 鏈湴鏂囦欢鍚嶅缓璁厛鍦ㄤ笂灞?mod 涓竻娲楀悗鍐嶄紶鍏?

`lyricget` 鍙細鍋氬熀纭€绌虹櫧瀛楃褰掍竴鍖栵紝涓嶈礋璐ｅ鏉傜殑鏇茬洰淇℃伅瑙ｆ瀽銆?

## 缂撳瓨寤鸿

`lyricget` 涓嶅唴缃紦瀛樸€俶od 闆嗘垚鏃跺缓璁敤涓婂眰缂撳瓨閬垮厤閲嶅璇锋眰銆?

鎺ㄨ崘缂撳瓨閿細

```text
normalizedTitle + "\n" + normalizedArtist
```

寤鸿缂撳瓨鍐呭锛?

- 姝岃瘝鏂囨湰
- provider 鍚嶇О
- 鏌ヨ鏃堕棿
- 鏄惁涓烘湭鍛戒腑缁撴灉

鏈懡涓粨鏋滀篃寤鸿鐭椂闂寸紦瀛橈紝閬垮厤鍚屼竴棣栨瓕鍙嶅璇锋眰鎵€鏈夋瓕璇嶆簮銆?

## 璁稿彲璇佽姹?

姝ら」鐩娇鐢ㄨ嚜瀹氫箟闄愬埗鎬ц鍙瘉锛屼笉鏄?MIT銆?

闆嗘垚鍒?mod 鎴栨淳鐢熼」鐩椂蹇呴』閬靛畧 `LICENSE`锛屽挨鍏舵槸锛?

- 涓嶅厑璁镐簩娆″敭鍗栨垨鍖呭惈鍦ㄤ粯璐逛骇鍝併€佷粯璐规湇鍔′腑
- 鍒嗗彂婧愮爜鎴栨淳鐢熶唬鐮佹椂蹇呴』鍖呭惈 LICENSE 鏂囦欢
- 浣跨敤姝ら」鐩紑鍙?mod 鏃讹紝蹇呴』鏄庣‘璇存槑姝岃瘝鑾峰彇鏉ユ簮

寤鸿鍦ㄤ笂灞?mod 鐨?README 鎴栭福璋㈤〉闈㈠啓鏄庯細

```text
Lyrics are fetched through lyricget.
```

## lyricget 鍐呴儴缁存姢

濡傛灉浣犳槸鍦ㄧ淮鎶?`lyricget` 鏈韩锛岃€屼笉鏄湪鍏朵粬 mod 涓泦鎴愬畠锛?

1. 鍦?`src/main/java/com/lyricget/lyrics/` 涓嬫柊澧?`XXXProvider`
2. 缁ф壙 `AbstractLyricProvider` 鎴栧疄鐜?`LyricProvider`
3. 鍦?`LyricsFetcher.providers()` 涓敞鍐?
4. 鎴愬姛鏃惰繑鍥?LRC 鎴栨櫘閫氭瓕璇嶆枃鏈?
5. 澶辫触銆佹棤缁撴灉鎴栦笉鍙帴鍙楃粨鏋滆繑鍥?`null`

缁存姢瑙勫垯锛?

- 涓嶈鎶?Minecraft銆丼MTC銆丠UD 鎴栨挱鏀惧櫒鎺у埗浠ｇ爜鏀捐繘鏉?
- 涓嶈鍦?provider 涓姞鍏ヤ笂灞?UI 閫昏緫
- provider 搴斾繚鎸佸彲鐙珛鏇挎崲
- 鍏叡鍏ュ彛浼樺厛淇濇寔鍚戝悗鍏煎锛岄伩鍏嶇牬鍧忓凡鏈?mod 闆嗘垚

## 鏋勫缓楠岃瘉

鍦?`lyricget` 浠撳簱涓繍琛岋細

```bash
./gradlew.bat build
```

鍛戒护琛岃皟璇曪細

```bash
./gradlew.bat run --args="鏅村ぉ 鍛ㄦ澃浼?
```
