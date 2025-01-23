package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(Anoboy())
    providerTester.testLoad("https://tv1.anoboy.app/2024/10/ranma-%c2%bd-2024/")
}
