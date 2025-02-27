package com.crstlnz

import com.crstlnz.utils.ArmoBiz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(DramaSerial())
//    providerTester.testMainPage()
//    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=3089349649006742360&se=1&ep=1")
//    providerTester.testLoadLinks("https://moviebox.ng/wefeed-h5-bff/web/subject/play?subjectId=5589585095314260816&se=1&ep=2")

//    providerTester.testAll()
//    providerTester.testSearch("demon slayer")
//    providerTester.testLoad("https://tv4.dramaserial.id/film-seri/nonton-hook-bait-2025-sub-indo-2/")
    providerTester.testLoadLinks("https://tv4.dramaserial.id/film-seri/nonton-hook-bait-2025-sub-indo-2/")
}
