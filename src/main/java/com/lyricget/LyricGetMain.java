package com.lyricget;

public final class LyricGetMain {
    private LyricGetMain() {}

    public static void main(String[] args) {
        String title = args.length > 0 ? args[0] : "";
        String artist = args.length > 1 ? args[1] : "";

        LyricSearchConfig config = LyricSearchConfig.defaults();
        LyricsFetcher.searchParallelDetailed(title, artist, config)
                .thenAccept(result -> {
                    if (result == null) {
                        System.out.println("NO_RESULT");
                        return;
                    }
                    System.out.println("PROVIDER=" + result.providerName());
                    System.out.println(result.lyrics());
                })
                .join();
    }
}
