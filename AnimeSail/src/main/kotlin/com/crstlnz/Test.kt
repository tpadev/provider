package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(AnimeSail())
    providerTester.testLoad("https://154.26.137.28/anime/kusuriya-no-hitorigoto-2nd-season/")
}
