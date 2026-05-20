package com.southside.lyricget.lyrics;

import com.google.gson.JsonObject;
import com.southside.lyricget.LyricSearchConfig;

public class MusixmatchProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "musixmatch";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        if (config.musixmatchApiKey == null || config.musixmatchApiKey.isBlank()) return null;
        String url = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track="
                + com.southside.lyricget.LyricsFetcher.encode(title)
                + "&q_artist="
                + com.southside.lyricget.LyricsFetcher.encode(artist)
                + "&apikey="
                + com.southside.lyricget.LyricsFetcher.encode(config.musixmatchApiKey);
        JsonObject json = getJson(url, config.timeoutMs);
        if (json == null) return null;
        if (!json.has("message") || !json.get("message").isJsonObject()) return null;
        JsonObject body = json.getAsJsonObject("message").getAsJsonObject("body");
        if (body != null && body.has("lyrics")) {
            JsonObject lyrics = body.getAsJsonObject("lyrics");
            if (lyrics.has("lyrics_body")) return lyrics.get("lyrics_body").getAsString();
        }
        return null;
    }
}
