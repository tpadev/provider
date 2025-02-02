package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(MovieBox())
    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3619725132714887968&se=4&ep=2")
//    providerTester.testAll()
//    providerTester.testSearch("demon slayer")
//    providerTester.testLoad("https://moviebox.ng/movies/demon-slayer-kimetsu-no-yaiba-OpOlWPwnoj4?id=3619725132714887968&scene=&type=/movie/detail&utm_source=h5seo_www.google.com")
}
