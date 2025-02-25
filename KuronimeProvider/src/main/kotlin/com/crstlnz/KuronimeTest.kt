package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(KuronimeProvider())
    providerTester.testLoadLinks("https://kuronime.biz/nonton-salaryman-ga-isekai-ni-ittara-shitennou-ni-natta-hanashi-episode-9/")
}
