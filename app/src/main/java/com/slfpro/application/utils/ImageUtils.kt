package com.slfpro.application.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.View
import com.orhanobut.logger.Logger
import com.slfpro.application.constant.AppConfig
import com.slfpro.application.widget.WatermarkLayout
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

/**
 * 图片相关
 * 创建水印图片
 * 图片压缩
 */
class ImageUtils private constructor() {
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ImageUtils()
    }

    /**
     * 创建水印图片
     * 耗时操作，需要放在子线程中操作
     *
     * @param context Context
     * @param name String 名字
     * @param coordinate String 坐标描述
     * @param photoPath String 图片路径
     * @return File?
     */
    fun createWatermarkPhoto(
        context: Context,
        name: String,
        coordinate: String,
        photoPath: String
    ): File? {
        val photo = File(photoPath)

        if (!photo.exists()) {
            Logger.e("照片未找到")
            return null
        }

        //水印bitmap
        var watermarkBitmap = createWatermark(context, name, coordinate)
        //照片bitmap
        var photoBitmap = BitmapFactory.decodeFile(photoPath)

        //获取图片旋转角度
        val photoDegree = getPhotoDegree(photoPath)
        if (photoDegree != 0) {
            //矫正角度
            photoBitmap = rotateImageView(photoDegree, photoBitmap)
        }

        //添加水印到照片
        val photoWidth = photoBitmap.width
        val photoHeight = photoBitmap.height
        //按比例缩放水印
        watermarkBitmap = zoomImg(watermarkBitmap, photoWidth.toFloat() * 2 / 3)
        //创建一个bitmap
        val wmBitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        //将该图片作为画布
        val canvas = Canvas(wmBitmap)
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(photoBitmap, 0f, 0f, null)
        //在画布上绘制水印图片
        canvas.drawBitmap(
            watermarkBitmap,
            SizeUtils.instance.dp2px(20f).toFloat(),
            (photoHeight - watermarkBitmap.height - SizeUtils.instance.dp2px(20f)).toFloat(),
            null
        )
        // 保存
        canvas.save()
        // 存储
        canvas.restore()
        //保存到本地
        val outFileDir = AppConfig.instance.PHOTO_PATH.getInternalStorageFile()
        val outFile = File(
            outFileDir,
            "${DateUtils.instance.getNowString(DateUtils.instance.FORMAT_UNDERLINED)}${AppConfig.instance.PHOTO_SUFFIX}.jpg"
        )
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(outFile)
            wmBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outFile
    }

    /**
     * 按比例缩放水印
     *
     * @param watermark Bitmap 水印
     * @param newWidth Float 缩放比例
     * @return Bitmap
     */
    private fun zoomImg(watermark: Bitmap, newWidth: Float): Bitmap {
        val width = watermark.width
        val height = watermark.height
        // 计算缩放比例
        val newHeight = height * newWidth / width
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(watermark, 0, 0, width, height, matrix, true)
    }

    /**
     * 创建水印bitmap
     *
     * @param context Context
     * @param name String 名字
     * @param coordinate String 坐标描述
     * @return Bitmap
     */
    private fun createWatermark(context: Context, name: String, coordinate: String): Bitmap {
        val watermarkLayout = WatermarkLayout(context)
        watermarkLayout.getTvName().text = name
        watermarkLayout.getTvTime().text =
            StringBuffer(DateUtils.instance.getNowString(DateUtils.instance.FORMAT_CN_NO_SECONDS))
                .insert(
                    11,
                    " " + DateUtils.instance.getChineseWeek(DateUtils.instance.getNowMills())
                ).toString()
        watermarkLayout.getTvCoordinate().text = coordinate
        watermarkLayout.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        watermarkLayout.layout(0, 0, watermarkLayout.measuredWidth, watermarkLayout.measuredHeight)
        watermarkLayout.buildDrawingCache()
        return watermarkLayout.drawingCache
    }

    /**
     * 获取图片旋转角度
     *
     * @param path String
     * @return Int
     */
    private fun getPhotoDegree(path: String): Int {
        var degree = 0
        val exifInterface = ExifInterface(path)
        when (exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
        return degree
    }

    /**
     * 旋转图片
     *
     * @param angle Int 角度
     * @param bitmap Bitmap 图片
     * @return Bitmap
     */
    private fun rotateImageView(angle: Int, bitmap: Bitmap): Bitmap {
        //旋转图片
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        //返回新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 图片压缩
     * @param context Context
     * @param image File
     * @param zipPath String
     * @param callback (imageFile: File) -> Unit
     */
    @SuppressLint("CheckResult")
    fun imageCompression(
        context: Context,
        image: File,
        imageIndex: Int = 0,
        zipPath: String = AppConfig.instance.ZIP_PHOTO_FILE.getInternalStorageFile().absolutePath,
        callback: (success: Boolean, compressionFile: File?, imageIndex: Int) -> Unit
    ) {
        Compressor(context)
            .setDestinationDirectoryPath(zipPath)
            .compressToFileAsFlowable(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback(true, it, imageIndex)
                },
                {
                    callback(false, null, imageIndex)
                    "图片上传错误，请检查图片后重试".toast()
                    Logger.d("压缩图片出错: ${it.message}")
                }
            )
    }
}