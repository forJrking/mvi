package com.example.mvi.utils

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object ImageLoader {

    fun Uri.decodeBitmap(context: Context) = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media.getBitmap(context.contentResolver, this)

    } else {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }
}