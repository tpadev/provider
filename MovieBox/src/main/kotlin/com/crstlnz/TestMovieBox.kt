package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(MovieBox())
//    providerTester.testLoad("https://moviebox.ng/movies/niyala-QEfogF0boQa?id=9102201097187460288&scene=&type=/movie/detail")
//    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3089349649006742360&se=1&ep=1")
    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3989995742919283480&se=4&ep=1|https://moviebox.ng/movies/jack-ryan-qzDHqzOdKK4?id=3989995742919283480&scene=&page_from=suggestion&type=/movie/detail&utm_source=Hola")
//    data.first.forEach {
//        println(it.type)
//        println(it.url)
//        println(it.quality)
//    }
//    providerTester.testAll()
//    providerTester.testSearch("demon slayer")
//    providerTester.testLoad("https://moviebox.ng/movies/moving-KfytK8gmUE6?id=5589585095314260816&scene=&page_from=search_detail&type=/movie/detail&utm_source=h5seo_www.google.com")
}
