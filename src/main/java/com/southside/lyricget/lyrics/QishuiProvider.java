package com.southside.lyricget.lyrics;

import com.google.gson.JsonObject;
import com.southside.lyricget.LyricSearchConfig;

public class QishuiProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "qishui";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String url = "https://api.vience.cn/api/music/qishui/lrc?name="
                + com.southside.lyricget.LyricsFetcher.encode(title)
                + "&artist="
                + com.southside.lyricget.LyricsFetcher.encode(artist);
        JsonObject json = getJson(url, config.timeoutMs);
        if (json == null) return null;
        if (json.has("data") && json.get("data").isJsonObject()) {
            JsonObject data = json.getAsJsonObject("data");
            if (data.has("lrc")) return data.get("lrc").getAsString();
        }
        return null;
    }
}
