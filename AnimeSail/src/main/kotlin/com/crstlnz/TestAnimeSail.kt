package com.crstlnz

import com.crstlnz.utils.AnimeSailEmbed

suspend fun main() {
//    registerExtractorAPI(AnimeSailEmbed())
//    val providerTester = com.lagradost.cloudstreamtest.ProviderTester(AnimeSail())
//    providerTester.testLoadLinks("https://154.26.137.28/city-the-animation-episode-2/")

    AnimeSailEmbed().getUrl("https://154.26.137.28/utils/player/pixel/?id=YxvLYsE1&token=650dbfb3af4ae16cc7365c4fbe9e0cfd", null, {

    }, {
        println(it)
    })
}
