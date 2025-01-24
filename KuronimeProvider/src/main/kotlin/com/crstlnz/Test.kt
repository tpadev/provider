package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(KuronimeProvider())
    providerTester.testLoad("https://kuronime.biz/anime/sousei-no-aquarion-myth-of-emotions/")
}
