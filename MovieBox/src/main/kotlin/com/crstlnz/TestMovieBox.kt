package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(MovieBox())
//    providerTester.testMainPage()
    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3089349649006742360&se=1&ep=1")
//    providerTester.testAll()
//    providerTester.testSearch("demon slayer")
//    providerTester.testLoad("https://moviebox.ng/movies/squid-game-4CyZm1LfdG3?id=3089349649006742360&scene=&type=/movie/detail&utm_source=h5seo_www.google.com")
}
