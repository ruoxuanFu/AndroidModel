package com.slfpro.application

import android.annotation.SuppressLint
import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.slfpro.application.constant.AppConfig
import com.slfpro.application.utils.MediaLoader
import com.slfpro.application.utils.netUtils.NetStateMonitor
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.properties.Delegates

/**
 * @data on ${Data}
 */
class AKApplication : Application() {

    //单例
    companion object {
        var instance: AKApplication by Delegates.notNull()
    }

    private lateinit var mNetStateMonitorDis: Disposable
    lateinit var mNetState: NetStateMonitor.NetState

    override fun onCreate() {
        super.onCreate()
        instance = this
        //9.0弹窗
        disableAPIDialog()

        //初始化图片选择器
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader()).setLocale(Locale.getDefault()).build())

        //设置网络状态监听
        setNetStateListener()
    }

    /**
     * 设置网络状态监听
     */
    private fun setNetStateListener() {
        mNetStateMonitorDis = NetStateMonitor.instance.observe()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //EventBus.getDefault().post(NetWorkStateEvent(it))
                    mNetState = it
                    Logger.e("NetState is ${it.name}")
                    if (mNetState != NetStateMonitor.NetState.NETWORK_NOT_FIND) {

                    }
                }
    }

    override fun onTerminate() {
        mNetStateMonitorDis.isDisposed
        super.onTerminate()
    }

    /**
     * android 9.0 调用私有api弹框的解决方案
     */
    @SuppressLint("PrivateApi")
    private fun disableAPIDialog() {
        try {
            val clazz = Class.forName("android.app.ActivityThread")
            val currentActivityThread = clazz.getDeclaredMethod("currentActivityThread")
            currentActivityThread.isAccessible = true
            val activityThread = currentActivityThread.invoke(null)
            val mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}