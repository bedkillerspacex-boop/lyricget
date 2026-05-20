package com.southside.lyricget.lyrics;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractLyricProvider implements LyricProvider {
    protected HttpURLConnection openConn(String urlStr, int timeout) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        return conn;
    }

    protected String get(String urlStr, int timeout) {
        try {
            HttpURLConnection conn = openConn(urlStr, timeout);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) sb.append(line).append('\n');
                    return sb.toString().trim();
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    protected JsonObject getJson(String urlStr, int timeout) {
        String body = get(urlStr, timeout);
        if (body == null || !body.trim().startsWith("{")) return null;
        try {
            return JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public CompletableFuture<String> fetch(String title, String artist, com.southside.lyricget.LyricSearchConfig config) {
        return CompletableFuture.supplyAsync(() -> fetchSync(title, artist, config));
    }

    protected abstract String fetchSync(String title, String artist, com.southside.lyricget.LyricSearchConfig config);
}
