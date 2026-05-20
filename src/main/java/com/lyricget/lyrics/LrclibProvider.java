package com.lyricget.lyrics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lyricget.LyricSearchConfig;

public class LrclibProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "lrclib";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String url = "https://lrclib.net/api/get?track_name=" + com.lyricget.LyricsFetcher.encode(title)
                + "&artist_name=" + com.lyricget.LyricsFetcher.encode(artist);
        JsonObject json = getJson(url, config.timeoutMs);
        if (json == null) return null;
        if (json.has("syncedLyrics") && !json.get("syncedLyrics").isJsonNull()) return json.get("syncedLyrics").getAsString();
        if (json.has("plainLyrics") && !json.get("plainLyrics").isJsonNull()) return json.get("plainLyrics").getAsString();
        if (json.has("lyrics") && json.get("lyrics").isJsonPrimitive()) return json.get("lyrics").getAsString();
        if (json.has("data") && json.get("data").isJsonArray()) {
            JsonArray arr = json.getAsJsonArray("data");
            if (!arr.isEmpty()) {
                JsonObject item = arr.get(0).getAsJsonObject();
                if (item.has("syncedLyrics")) return item.get("syncedLyrics").getAsString();
                if (item.has("plainLyrics")) return item.get("plainLyrics").getAsString();
            }
        }
        return null;
    }
}
