package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(AnimeSail())
    providerTester.testLoadLinks("https://154.26.137.28/douse-koishite-shimaunda-episode-3/")
}
