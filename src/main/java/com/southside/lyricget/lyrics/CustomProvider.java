package com.southside.lyricget.lyrics;

import com.southside.lyricget.LyricSearchConfig;

public class CustomProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "custom";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String url = config.customUrl
                .replace("%title%", com.southside.lyricget.LyricsFetcher.encode(title))
                .replace("%artist%", com.southside.lyricget.LyricsFetcher.encode(artist));
        String body = get(url, config.timeoutMs);
        return com.southside.lyricget.LyricsFetcher.stripMarkup(body);
    }
}
