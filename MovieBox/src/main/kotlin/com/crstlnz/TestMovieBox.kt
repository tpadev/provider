package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(MovieBox())
//    providerTester.testMainPage()
//    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3089349649006742360&se=1&ep=1")
//    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=5589585095314260816&se=1&ep=2")

//    providerTester.testAll()
//    providerTester.testSearch("demon slayer")
    providerTester.testLoad("https://moviebox.ng/movies/moving-KfytK8gmUE6?id=5589585095314260816&scene=&type=/movie/detail")
}
