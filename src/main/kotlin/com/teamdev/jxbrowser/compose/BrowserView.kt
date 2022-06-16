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

package com.teamdev.jxbrowser.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import com.teamdev.jxbrowser.browser.Browser
import com.teamdev.jxbrowser.browser.internal.BrowserImpl
import com.teamdev.jxbrowser.browser.internal.callback.PaintCallback
import com.teamdev.jxbrowser.browser.internal.rpc.Paint
import com.teamdev.jxbrowser.browser.internal.rpc.PaintRequest
import com.teamdev.jxbrowser.compose.internal.KeyEventDispatcher
import com.teamdev.jxbrowser.compose.internal.LayoutListener
import com.teamdev.jxbrowser.compose.internal.PixelBuffer
import com.teamdev.jxbrowser.compose.internal.MouseEventDispatcher
import com.teamdev.jxbrowser.internal.Display
import org.jetbrains.skia.Image

private const val STOP_PROPAGATION = true

/**
 * Adds a widget that displays the content of the [Browser].
 */
@Composable
fun BrowserView(browser: Browser) {
    val image: MutableState<Image?> = mutableStateOf(null)
    val widget = (browser as BrowserImpl).widget()
    widget.displayId(Display.primaryDisplay().id())
    widget.set(PaintCallback::class.java, OnPaint(image))

    val keyDispatcher = KeyEventDispatcher(widget)
    val focusRequester = FocusRequester()
    val layoutListener = LayoutListener(widget)
    val mouseDispatcher = MouseEventDispatcher(widget)

    widget.show()

    Box(
        modifier = Modifier.background(color = Color.White)
            .fillMaxSize()
            .layout(layoutListener.onLayout())
            .focusRequester(focusRequester)
            .onGloballyPositioned(layoutListener::onPositioned)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        mouseDispatcher.dispatch(awaitPointerEvent())
                    }
                }
            }
            .onKeyEvent {
                keyDispatcher.dispatch(it)
                STOP_PROPAGATION
            }
            .onFocusEvent {
                if (it.hasFocus) widget.focus() else widget.unfocus()
            }
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                // We need to focus the component when capturing key events,
                // so we programmatically request focus on click.
                focusRequester.requestFocus()
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas { canvas ->
                image.value?.let {
                    canvas.nativeCanvas.drawImage(it, 0f, 0f)
                }
            }
        }
    }
}

private class OnPaint(private val image: MutableState<Image?>) : PaintCallback {
    private val pixelBuffer = PixelBuffer()

    override fun on(params: Paint.Request): Paint.Response? {
        val request: PaintRequest = params.paintRequest
        pixelBuffer.updatePixels(
            request.viewSize,
            request.dirtyRect,
            request.memoryId
        ) { updatedImage ->
            image.value = updatedImage
        }
        return Paint.Response.newBuilder().build()
    }
}
