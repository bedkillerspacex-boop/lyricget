package com.lyricget.lyrics;

import com.lyricget.LyricSearchConfig;

public class CustomProvider extends AbstractLyricProvider {
    @Override
    public String getName() {
        return "custom";
    }

    @Override
    protected String fetchSync(String title, String artist, LyricSearchConfig config) {
        String url = config.customUrl
                .replace("%title%", com.lyricget.LyricsFetcher.encode(title))
                .replace("%artist%", com.lyricget.LyricsFetcher.encode(artist));
        String body = get(url, config.timeoutMs);
        return com.lyricget.LyricsFetcher.stripMarkup(body);
    }
}
