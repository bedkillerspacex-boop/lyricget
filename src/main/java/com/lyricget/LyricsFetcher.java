package com.lyricget;

import com.lyricget.lyrics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public final class LyricsFetcher {
    private LyricsFetcher() {}

    public static CompletableFuture<LyricResult> searchParallelDetailed(String title, String artist, LyricSearchConfig config) {
        String cleanTitle = normalize(title);
        String cleanArtist = normalize(artist);
        List<LyricProvider> providers = providers(config);
        if (providers.isEmpty() || cleanTitle.isBlank()) {
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<LyricResult>> futures = new ArrayList<>();
        for (LyricProvider provider : providers) {
            futures.add(provider.fetch(cleanTitle, cleanArtist, config)
                    .thenApply(lyrics -> lyrics == null || lyrics.isBlank() ? null : new LyricResult(lyrics, provider.getName())));
        }

        CompletableFuture<LyricResult> out = new CompletableFuture<>();
        AtomicInteger remaining = new AtomicInteger(futures.size());
        for (CompletableFuture<LyricResult> future : futures) {
            future.handle((result, ex) -> {
                if (result != null && !out.isDone()) {
                    out.complete(result);
                }
                if (remaining.decrementAndGet() == 0 && !out.isDone()) {
                    out.complete(null);
                }
                return null;
            });
        }
        return out;
    }

    private static List<LyricProvider> providers(LyricSearchConfig config) {
        List<LyricProvider> out = new ArrayList<>();
        if (config.enableNetease) out.add(new NeteaseProvider());
        if (config.enableQQ) out.add(new QqProvider());
        if (config.enableLrclib) out.add(new LrclibProvider());
        if (config.enableKugou) out.add(new KugouProvider());
        if (config.enableQishui) out.add(new QishuiProvider());
        if (config.enableMusixmatch) out.add(new MusixmatchProvider());
        if (config.enableCustom) out.add(new CustomProvider());
        return out;
    }

    public static String normalize(String text) {
        if (text == null) return "";
        return text.replaceAll("\\s+", " ").trim();
    }

    public static boolean acceptable(String lyrics, LyricSearchConfig config) {
        if (lyrics == null || lyrics.isBlank()) return false;
        return lyrics.contains("[");
    }

    public static String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s == null ? "" : s, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s == null ? "" : s;
        }
    }

    public static String stripMarkup(String lyrics) {
        if (lyrics == null) return "";
        return lyrics.replace("\uFEFF", "").trim();
    }

    public static boolean containsChinese(String text) {
        if (text == null) return false;
        for (char c : text.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) return true;
        }
        return false;
    }
}
