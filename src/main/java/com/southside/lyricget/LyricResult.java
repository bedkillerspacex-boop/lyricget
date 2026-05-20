package com.southside.lyricget;

public record LyricResult(String lyrics, String providerName) {
    public LyricResult {
        providerName = providerName == null ? "" : providerName.trim();
    }
}
