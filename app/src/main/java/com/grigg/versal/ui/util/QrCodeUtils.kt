package com.grigg.versal.ui.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.StaticLayout
import android.text.TextPaint
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.OutputStream
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.core.graphics.withTranslation

object QrCodeUtils {
    fun generateQrCodeWithText(
        title: String,
        url: String,
        footer: String? = null,
        size: Int = 512
    ): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, size, size)
        val qrBitmap = createBitmap(size, size)

        for (x in 0 until size) {
            for (y in 0 until size) {
                qrBitmap[x, y] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }

        val padding = size / 10
        val maxWidth = size.toFloat()

        // Title Paint
        val titlePaint = TextPaint().apply {
            color = Color.BLACK
            textSize = size / 12f
            isAntiAlias = true
        }

        // Title Layout
        val titleLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(title, 0, title.length, titlePaint, maxWidth.toInt())
                .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                title, titlePaint, maxWidth.toInt(),
                android.text.Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false
            )
        }

        // Footer Layout
        val footerLayout = if (!footer.isNullOrBlank()) {
            val footerPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = size / 18f
                isAntiAlias = true
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(footer, 0, footer.length, footerPaint, maxWidth.toInt())
                    .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(
                    footer, footerPaint, maxWidth.toInt(),
                    android.text.Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false
                )
            }
        } else {
            null
        }

        val titleHeight = titleLayout.height
        val footerHeight = footerLayout?.height ?: 0
        val totalHeight = padding + titleHeight + padding + size + padding + footerHeight + padding

        val finalBitmap = createBitmap(size + padding * 2, totalHeight)
        val canvas = Canvas(finalBitmap)
        canvas.drawColor(Color.WHITE)

        // Draw Title
        canvas.withTranslation(padding.toFloat(), padding.toFloat()) {
            titleLayout.draw(this)
        }

        // Draw QR
        val qrTop = padding + titleHeight + padding
        canvas.drawBitmap(qrBitmap, padding.toFloat(), qrTop.toFloat(), null)

        // Draw Footer
        if (footerLayout != null) {
            val footerTop = qrTop + size + padding
            canvas.withTranslation(padding.toFloat(), footerTop.toFloat()) {
                footerLayout.draw(this)
            }
        }

        return finalBitmap
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Versal")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)
            }
        }
        return uri
    }
}
