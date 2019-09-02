package com.slfpro.application.utils.netUtils

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 指定的网络请求接口
 *
 * 爱客当前的接口需要标注 @FormUrlEncoded
 *
 * 按照进销存的接口:
 * (POST)
 * @FormUrlEncoded
 * @POST("api/{version}/users/login.json")
 * fun getTokenLogin(@Path("version") version: String,
 *                   @FieldMap map: Map<String, String>)
 *              : Observable<BaseResult<UserInfo>>
 * 用 @FieldMap 传递 Map 参数
 * 或
 * @FormUrlEncoded
 * @POST("api/{version}/users/verify_code.json")
 * fun verifyMessageCode(@Path("version") version: String,
 *                       @Field("mobile") mobile: String,
 *                       @Field("code") msgCode: String)
 *                  : Observable<BaseResult<String>>
 * 用 @Field 传递单个参数
 *
 * (GET)
 * @GET("api/{version}/users/get_code.json")
 * fun getMessageCode(@Path("version") version: String,
 *                    @Query("mobile") mobile: String)
 *                : Observable<BaseResult<String>>
 * 用 @Query 拼接参数
 *
 * (特殊请求，比如delete需要传递body)
 * @HTTP(method = "DELETE", path = "api/{version}/users/delete_client_id.json", hasBody = true)
 * fun signOutApp(@Path("version") version: String,
 *                @Body signOutAppBody: SignOutAppBody)
 *          : Observable<BaseResult<String>>
 */
/*
    */
/**
 * 登录
 * 不需要corp_id
 * @param loginBean RequestLogin
 * @return Observable<BaseResult<ResponseLogin>>
 *//*
    @POST("auth/login_app")
    fun login(@Body loginBean: RequestLogin): Observable<ResponseBody>

    */
/**
 * 获取悬浮窗信息
 * @param phoneNum String
 *//*
    @GET("dial_logs/caller_id")
    fun getFloatInfo(@Query("phone") phoneNum: String): Observable<BaseResult<ResponseFloatInfo>>

    */
/**
 * 获取上传七牛的token
 * @param fileType String
 *//*
    @GET("qiniu/auth/upload_token")
    fun getUploadQiNiuToken(@Query("policy") fileType: String): Observable<ResponseBody>

    */
/**
 * APP自动拨号后挂断时需要调用的接口
 * @return Observable<ResponseBody>
 *//*
    @POST("dial_centers/hang_up_nofify?")
    fun dialHangUp(): Observable<ResponseBody>

    */
/**
 * 生成通话记录
 * @return Observable<ResponseBody>
 *//*
    @POST("dial_logs")
    fun createDialLogs(@Body request: RequestCreateDialog): Observable<BaseResult<ResponseCreateDialLog>>

    */
/**
 * 更新通话记录为销售动态
 * @return Observable<ResponseBody>
 *//*
    @PUT("dial_logs/{itemId}")
    fun upDataDialLogs(@Path("itemId") itemId: Long, @Body request: RequestUpDataDialLog): Observable<BaseResult<ResponseUpDataDialLog>>

    */
/**
 * 获取用户信息
 * @return Observable<BaseResult<ResponseUserInfo>>
 *//*
    @GET("user/info")
    fun getUserInfo(): Observable<BaseResult<ResponseUserInfo>>

    */
/**
 * 下载
 * @param url String
 * @return Observable<ResponseBody>
 *//*
    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>

    *//**
 * 获取阿里云token
 * @param url String
 * @return Observable<ResponseBody>
 *//*
    @GET
    fun getAliyunToken(@Url url: String): Observable<ResponseBody>
*/

interface ApiService {

}