package me.jalxp.easylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import kotlin.math.min

class ImageUtility {

    companion object {
        fun setPic(view: View, path: String?) {
            if (path.isNullOrEmpty())
                return
            Log.e("<SetPic>", "width: ${view.width}, height:${view.height}")
            val targetW = view.width
            val targetH = view.height
            if (targetH < 1 || targetW < 1) {
                return
            }
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path)
            val photoW = bmpOptions.outWidth
            val photoH = bmpOptions.outHeight
            val scale = min(photoW / targetW, photoH / targetH)
            bmpOptions.inSampleSize = scale
            bmpOptions.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(path, bmpOptions)
            when (view) {
                is ImageView -> (view as ImageView).setImageBitmap(bitmap)
                //else -> view.background = bitmap.toDrawable(view.resources)
                else -> view.background = BitmapDrawable(view.resources, bitmap)
            }
        }

        fun setPic(view: View, path: String?, width: Int, height: Int) {
            if (path.isNullOrEmpty())
                return
            if (height < 1 || width < 1) {
                return
            }
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inPreferredConfig = Bitmap.Config.RGB_565
            bmpOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path)
            val photoW = bmpOptions.outWidth
            val photoH = bmpOptions.outHeight
            val scale = min(photoW / width, photoH / height)
            bmpOptions.inSampleSize = scale
            bmpOptions.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(path, bmpOptions)
            when (view) {
                is ImageView -> (view as ImageView).setImageBitmap(bitmap)
                //else -> view.background = bitmap.toDrawable(view.resources)
                else -> view.background = BitmapDrawable(view.resources, bitmap)
            }
        }
    }
}
