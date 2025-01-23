// use an integer for version numbers
version = 1

android {
    namespace = "AnimeSail"
}
dependencies {
    implementation(project(":"))
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
        "AnimeMovie",
        "Anime",
        "OVA",
    )

    iconUrl = "https://anoboy.pro/assets/img/favicon.png"
}