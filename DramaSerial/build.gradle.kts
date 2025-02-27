// use an integer for version numbers
version = 2

android {
    namespace = "DramaSerial"
}

cloudstream {
    language = "id"
    // All of these properties are optional, you can safely remove them

    // description = "Lorem Ipsum"
    authors = listOf("Crstlnz")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "AsianDrama",
        "Movie",
        "TvSeries",
        "AnimeMovie",
        "Anime",
        "OVA",
    )

    iconUrl =
        "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://tv4.dramaserial.id/&size=64"
}