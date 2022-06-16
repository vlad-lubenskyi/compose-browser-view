/*
 * Copyright 2000-2022 TeamDev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamdev.jxbrowser.compose.internal

import com.teamdev.jxbrowser.internal.ToolkitLibrary
import com.teamdev.jxbrowser.internal.rpc.MemoryId
import com.teamdev.jxbrowser.ui.Rect
import com.teamdev.jxbrowser.ui.Size
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import java.nio.ByteBuffer

/**
 * A pixel buffer to be rendered by Compose.
 */
internal class PixelBuffer {
    private var width = 0
    private var height = 0
    private var buffer: ByteBuffer? = null

    private fun initialize(clientSize: Size) {
        if (width != clientSize.width() || height != clientSize.height()) {
            width = clientSize.width()
            height = clientSize.height()

            val pixels = ByteArray(width * height * 4)
            buffer = ByteBuffer.wrap(pixels)
        }
    }

    /**
     * Updates the pixels in the buffer.
     */
    fun updatePixels(viewSize: Size, dirtyRect: Rect, memoryId: MemoryId, repaintCallback: (Image) -> Unit) {
        initialize(viewSize)
        if (!validateDirtyRect(dirtyRect, viewSize)) {
            return
        }
        val asIntBuffer = buffer!!
        ToolkitLibrary.instance().updatePixelsBytes(
            memoryId.value, dirtyRect, viewSize, asIntBuffer.array()
        )
        val skiaImage = createSkijaImage(asIntBuffer.array(), width, height)
        repaintCallback.invoke(skiaImage)
    }

    private fun validateDirtyRect(dirtyRect: Rect, viewSize: Size): Boolean {
        val viewWidth = viewSize.width()
        val viewHeight = viewSize.height()
        val dirtyRectOrigin = dirtyRect.origin()
        val dirtyRectSize = dirtyRect.size()
        return dirtyRectOrigin.x() <= viewWidth
                && dirtyRectOrigin.y() <= viewHeight
                && dirtyRectOrigin.x() + dirtyRectSize.width() <= viewWidth
                && dirtyRectOrigin.y() + dirtyRectSize.height() <= viewHeight
    }

    private fun createSkijaImage(bytes: ByteArray, width: Int, height: Int): Image {
        val imageInfo = ImageInfo(width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL)
        return Image.makeRaster(imageInfo, bytes, width * 4)
    }
}
