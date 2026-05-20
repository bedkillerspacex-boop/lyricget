package com.southside.lyricget.lyrics;

import com.southside.lyricget.LyricSearchConfig;

import java.util.concurrent.CompletableFuture;

public interface LyricProvider {
    String getName();
    CompletableFuture<String> fetch(String title, String artist, LyricSearchConfig config);
}
