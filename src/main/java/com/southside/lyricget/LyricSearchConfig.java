package com.southside.lyricget;

public final class LyricSearchConfig {
    public boolean enableNetease = true;
    public boolean enableQQ = true;
    public boolean enableLrclib = true;
    public boolean enableKugou = true;
    public boolean enableQishui = true;
    public boolean enableMusixmatch = false;
    public boolean enableCustom = false;
    public String customUrl = "https://api.lrc.cx/lyrics?title=%title%&artist=%artist%";
    public String musixmatchApiKey = "";
    public int timeoutMs = 4000;
    public boolean preferChinese = true;

    public static LyricSearchConfig defaults() {
        return new LyricSearchConfig();
    }
}
