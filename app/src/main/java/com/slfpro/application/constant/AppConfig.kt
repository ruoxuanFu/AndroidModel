package com.slfpro.application.constant

import com.slfpro.application.BuildConfig
import com.slfpro.application.utils.SPUtils
import com.slfpro.application.utils.cacheUtils.CacheDiskStaticUtils

/**
 * 配置与程序相关的常量
 */
class AppConfig private constructor() {
    companion object {
        val instance: AppConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppConfig()
        }
    }

    val DEBUG = BuildConfig.DEBUG
    //SharedPreferences文件名称
    val DEF_SP_NAME = "AppInfo"
    //数据缓存地址
    val DATA_CACHE_PATH = "/SlfPro/Cache/"
    //aike照片地址
    val PHOTO_PATH = "/SlfPro/Photo_Cache"
    //aike图片压缩地址
    val ZIP_PHOTO_FILE = "/SlfPro/Photo_Cache_zip-p"
    //aike照片后缀
    val PHOTO_SUFFIX = "_sfl_photo"

    /**
     * 用户登录Url类型
     */
    fun setUrlType(type: Int) {
        SPUtils.instance.saveValue(KeySet.KEY_URL_TYPE, type)
    }

    fun getUrlType(): Int {
        return SPUtils.instance.getIntSp(KeySet.KEY_URL_TYPE) ?: 0
    }

    /**
     * 用户Token
     * @param token String
     */
    fun setUserToken(token: String) {
        SPUtils.instance.saveValue(KeySet.KEY_USER_TOKEN, token)
    }

    fun getUserToken(): String {
        return SPUtils.instance.getStringSp(KeySet.KEY_USER_TOKEN) ?: ""
    }

    /**
     * 用户Id
     * @param id Int
     */
    fun setUserId(id: Int) {
        SPUtils.instance.saveValue(KeySet.KEY_USER_ID, id)
    }

    fun getUserId(): Int {
        return SPUtils.instance.getIntSp(KeySet.KEY_USER_ID) ?: 0
    }

    /**
     * 用户登录名
     * @param login String
     */
    fun setUserLogin(login: String) {
        SPUtils.instance.saveValue(KeySet.KEY_USER_LOGIN, login)
    }

    fun getUserLogin(): String {
        return SPUtils.instance.getStringSp(KeySet.KEY_USER_LOGIN) ?: ""
    }

    /**
     * 用户密码
     * @param token String
     */
    fun setUserPsw(token: String) {
        SPUtils.instance.saveValue(KeySet.KEY_USER_PSW, token)
    }

    fun getUserPsw(): String {
        return SPUtils.instance.getStringSp(KeySet.KEY_USER_PSW) ?: ""
    }

    /**
     * 用户信息
     * @param userInfo ResponseUserInfo
     */
/*
        fun setUserInfo(userInfo: ResponseUserInfo?) {
            CacheDiskStaticUtils.put(KeySet.CACHE_KEY_USER_INFO, userInfo)
        }

        fun getUserInfo(): ResponseUserInfo? {
            return CacheDiskStaticUtils.getParcelable<ResponseUserInfo>(KeySet.CACHE_KEY_USER_INFO, ResponseUserInfo.CREATOR)
        }
*/

    /**
     * 清除缓存信息
     * @return Boolean
     */
    fun clearCache(): Boolean {
        return CacheDiskStaticUtils.clear()
    }

    /**
     * 清除缓存信息
     * @param key String
     * @return Boolean
     */
    fun clearCache(key: String): Boolean {
        return CacheDiskStaticUtils.remove(key)
    }

    /**
     * 清除用户信息
     */
    fun clearUserInfo() {
        //清除user_token
        SPUtils.instance.remove(KeySet.KEY_USER_TOKEN)
        //清除user_id
        SPUtils.instance.remove(KeySet.KEY_USER_ID)
        //用户login
        SPUtils.instance.remove(KeySet.KEY_USER_LOGIN)
        //用户psw
        SPUtils.instance.remove(KeySet.KEY_USER_PSW)
        //清除缓存
        clearCache()
    }
}