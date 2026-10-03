package com.example.petcare.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import java.io.File

object ImageLoaderUtils {
    fun loadPetImage(imageView: ImageView, imageUriStr: String?) {
        if (imageUriStr.isNullOrBlank()) return

        val trimmed = imageUriStr.trim()
        imageView.visibility = View.VISIBLE

        try {
            when {
                trimmed.startsWith("android.resource://") ||
                trimmed.startsWith("content://") ||
                trimmed.startsWith("file://") -> {
                    imageView.setImageURI(Uri.parse(trimmed))
                }
                trimmed.startsWith("/") -> {
                    val file = File(trimmed)
                    if (file.exists()) {
                        imageView.setImageURI(Uri.fromFile(file))
                    } else {
                        imageView.setImageURI(Uri.parse(trimmed))
                    }
                }
                else -> {
                    val context = imageView.context
                    val resId = context.resources.getIdentifier(trimmed, "drawable", context.packageName)
                    if (resId != 0) {
                        imageView.setImageResource(resId)
                    } else {
                        imageView.setImageURI(Uri.parse(trimmed))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
