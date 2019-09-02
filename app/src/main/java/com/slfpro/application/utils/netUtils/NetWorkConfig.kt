package com.slfpro.application.utils.netUtils

import com.slfpro.application.AKApplication
import com.slfpro.application.constant.AppConfig


class NetWorkConfig private constructor() {
    companion object {
        val instance: NetWorkConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkConfig()
        }
    }

    //设备
    val DEVICE = "android"

    var ACCESS_TOKEN = "access_token"

    var BASE_URL = arrayOf(
        "url dev",         //dev
        "url test",        //test
        "url staging",     //staging
        "url production"            //production
    )

    fun getBaseUrl(): String {
        var baseUrl: String = BASE_URL[3]
        if (AppConfig.instance.DEBUG) {
            when (AppConfig.instance.getUrlType()) {
                0 -> {
                    baseUrl = BASE_URL[0]
                }
                1 -> {
                    baseUrl = BASE_URL[1]
                }
                2 -> {
                    baseUrl = BASE_URL[2]
                }
                3 -> {
                    baseUrl = BASE_URL[3]
                }
            }
        }
        return baseUrl
    }

}
