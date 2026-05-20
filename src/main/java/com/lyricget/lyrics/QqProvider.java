package com.lyricget.lyrics;

import com.google.gson.JsonObject;
import com.lyricget.LyricSearchConfig;

public class QqProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "qq";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String keyword = title + " " + artist;
        String searchUrl = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=1&n=10&w=" + com.lyricget.LyricsFetcher.encode(keyword) + "&format=json";
        JsonObject search = getJson(searchUrl, config.timeoutMs);
        if (search == null) return null;
        JsonObject data = search.has("data") && search.get("data").isJsonObject() ? search.getAsJsonObject("data") : null;
        if (data == null || !data.has("song") || !data.get("song").isJsonObject()) return null;
        JsonObject song = data.getAsJsonObject("song");
        if (!song.has("list") || !song.get("list").isJsonArray()) return null;
        if (song.getAsJsonArray("list").isEmpty()) return null;
        JsonObject first = song.getAsJsonArray("list").get(0).getAsJsonObject();
        if (!first.has("mid")) return null;
        String mid = first.get("mid").getAsString();
        String lrcUrl = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?songmid=" + mid + "&format=json&nobase64=1";
        JsonObject lyric = getJson(lrcUrl, config.timeoutMs);
        if (lyric == null || !lyric.has("lyric")) return null;
        return lyric.get("lyric").getAsString();
    }
}
