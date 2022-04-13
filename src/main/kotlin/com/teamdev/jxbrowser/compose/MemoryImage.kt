package com.teamdev.jxbrowser.compose/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.internal.ToolkitLibrary
import com.teamdev.jxbrowser.internal.rpc.MemoryId
import com.teamdev.jxbrowser.ui.Rect
import com.teamdev.jxbrowser.ui.Size
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.nio.ByteBuffer
import java.nio.IntBuffer


class MemoryImage {
    private var width = 0
    private var height = 0
    private var buffer: ByteBuffer? = null

    fun initialize(clientSize: Size) {
        if (width != clientSize.width() || height != clientSize.height()) {

            width = clientSize.width()
            height = clientSize.height()
            val pixels = ByteArray(width * height * 4)
            buffer = ByteBuffer.wrap(pixels)
        }
    }

    fun updatePixels(
        viewSize: Size, dirtyRect: Rect, memoryId: MemoryId, repaintCallback: (Image) -> Unit
    ) {
        initialize(viewSize)
        if (!validateDirtyRect(dirtyRect, viewSize)) {
            return
        }
        val dirtyX = dirtyRect.origin().x()
        val dirtyY = dirtyRect.origin().y()
        val dirtyWidth = dirtyRect.size().width()
        val dirtyHeight = dirtyRect.size().height()

        val asIntBuffer = buffer!!
        ToolkitLibrary.instance().updatePixelsBytes(
            memoryId.value, dirtyRect, viewSize, asIntBuffer.array()
        )
        val skiaImage =
            convertToSkijaImage(asIntBuffer.array(), width, height)//.getSubimage(dirtyRect.x(), dirtyRect.y(), dirtyRect.width(), dirtyRect.height()))
        val firstPixel = asIntBuffer.array()[0]
//        val bytes = byteArrayOf(
//            ((firstPixel shr (8*0)) and 0xff).toByte(),
//            ((firstPixel shr (8*1)) and 0xff).toByte(),
//            ((firstPixel shr (8*2)) and 0xff).toByte(),
//            ((firstPixel shr (8*3)) and 0xff).toByte(),
//        )
//        print("before:  ${bytes[0]} ${bytes[1]} ${bytes[2]} ${bytes[3]}")
        repaintCallback.invoke(skiaImage)
    }

    private fun validateDirtyRect(dirtyRect: Rect, viewSize: Size): Boolean {
        val viewWidth = viewSize.width()
        val viewHeight = viewSize.height()
        val dirtyRectOrigin = dirtyRect.origin()
        val dirtyRectSize = dirtyRect.size()
        return dirtyRectOrigin.x() <= viewWidth && dirtyRectOrigin.y() <= viewHeight
                && dirtyRectOrigin.x() + dirtyRectSize.width() <= viewWidth
                && dirtyRectOrigin.y() + dirtyRectSize.height() <= viewHeight
    }

    private fun convertToSkijaImage(image: BufferedImage): Image {
        val db = image.raster.dataBuffer as DataBufferInt
        val pixels = db.data
        return convertToSkijaImage(pixels, image.width, image.height)
    }

    private fun convertToSkijaImage(image: IntArray, width: Int, height: Int): Image {
        val pixels = image
        val bytes = ByteArray(pixels.size * 4)
        for (i in pixels.indices) {
            val p = pixels[i]
            bytes[i * 4 + 3] = (p and -0x1000000 shr 24).toByte()
            bytes[i * 4 + 2] = (p and 0xFF0000 shr 16).toByte()
            bytes[i * 4 + 1] = (p and 0xFF00 shr 8).toByte()
            bytes[i * 4] = (p and 0xFF).toByte()
        }
        println("; after:  ${bytes[0]} ${bytes[1]} ${bytes[2]} ${bytes[3]}")
        return convertToSkijaImage(bytes, width, height)
    }

    private fun convertToSkijaImage(bytes: ByteArray, width: Int, height: Int): Image {
        val imageInfo = ImageInfo(width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL)
        return Image.makeRaster(imageInfo, bytes, width * 4)
    }
}