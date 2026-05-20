package com.southside.lyricget.lyrics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.southside.lyricget.LyricSearchConfig;

public class KugouProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "kugou";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String keyword = title + " " + artist;
        String searchUrl = "https://lyrics.kugou.com/search?ver=1&man=yes&client=pc&keyword=" + com.southside.lyricget.LyricsFetcher.encode(keyword);
        JsonObject search = getJson(searchUrl, config.timeoutMs);
        if (search == null || !search.has("candidates") || !search.get("candidates").isJsonArray()) return null;
        JsonArray candidates = search.getAsJsonArray("candidates");
        if (candidates.isEmpty()) return null;
        JsonObject first = candidates.get(0).getAsJsonObject();
        if (!first.has("id") || !first.has("accesskey")) return null;
        String id = first.get("id").getAsString();
        String accessKey = first.get("accesskey").getAsString();
        String url = "https://lyrics.kugou.com/download?ver=1&client=pc&id=" + id + "&accesskey=" + accessKey + "&fmt=lrc&charset=utf8";
        JsonObject lyric = getJson(url, config.timeoutMs);
        if (lyric != null && lyric.has("content")) {
            try {
                byte[] decoded = java.util.Base64.getDecoder().decode(lyric.get("content").getAsString());
                return new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
