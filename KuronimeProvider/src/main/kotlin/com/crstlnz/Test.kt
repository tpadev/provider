package com.crstlnz

suspend fun main() {
    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(KuronimeProvider())
    providerTester.testAll()
}
