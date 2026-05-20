package com.southside.lyricget.lyrics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.southside.lyricget.LyricSearchConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NeteaseProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "netease";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String query = title + " " + artist;
        String searchUrl = "https://music.163.com/api/search/get/web?type=1&s=" + com.southside.lyricget.LyricsFetcher.encode(query);
        JsonObject search = getJson(searchUrl, config.timeoutMs);
        if (search == null) return null;
        JsonObject result = search.has("result") && search.get("result").isJsonObject() ? search.getAsJsonObject("result") : null;
        if (result == null || !result.has("songs") || !result.get("songs").isJsonArray()) return null;
        JsonArray songs = result.getAsJsonArray("songs");
        if (songs.isEmpty()) return null;
        JsonObject song = songs.get(0).getAsJsonObject();
        if (!song.has("id")) return null;
        String id = song.get("id").getAsString();
        String lyricUrl = "https://interface3.music.163.com/api/song/lyric?os=pc&id=" + id + "&lv=-1&tv=-1";
        JsonObject lyric = getJson(lyricUrl, config.timeoutMs);
        if (lyric == null) return null;
        if (lyric.has("lrc") && lyric.get("lrc").isJsonObject()) {
            JsonObject lrc = lyric.getAsJsonObject("lrc");
            if (lrc.has("lyric")) {
                String payload = lrc.get("lyric").getAsString();
                return payload == null ? null : payload.trim();
            }
        }
        if (lyric.has("klyric") && lyric.get("klyric").isJsonObject()) {
            JsonObject k = lyric.getAsJsonObject("klyric");
            if (k.has("lyric")) return k.get("lyric").getAsString();
        }
        return null;
    }
}
