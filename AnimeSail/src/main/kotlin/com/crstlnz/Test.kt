package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(AnimeSail())
    providerTester.testLoadLinks("https://154.26.137.28/ranma-%c2%bd-2024-episode-2/")
}
