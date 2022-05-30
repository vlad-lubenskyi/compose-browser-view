/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
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
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.browser.internal.callback.PaintCallback
import com.teamdev.jxbrowser.browser.internal.rpc.Paint
import com.teamdev.jxbrowser.browser.internal.rpc.PaintRequest
import com.teamdev.jxbrowser.compose.internal.KeyEventDispatcher
import com.teamdev.jxbrowser.compose.internal.LayoutListener
import com.teamdev.jxbrowser.compose.internal.MemoryImage
import com.teamdev.jxbrowser.compose.internal.MouseEventDispatcher
import com.teamdev.jxbrowser.internal.Display
import com.teamdev.jxbrowser.ui.Size
import org.jetbrains.skia.Image
import com.teamdev.jxbrowser.ui.Rect as JxRect

private const val STOP_PROPAGATION = true
private val image: MutableState<Image?> = mutableStateOf(null)

/**
 * Adds a widget that displays the content of the [Browser].
 */
@Composable
fun BrowserView(browser: Browser) {
    val widget = createBrowserWidget(browser)
    val keyDispatcher = KeyEventDispatcher(widget)
    val layoutListener = LayoutListener(widget)
    val mouseDispatcher = MouseEventDispatcher(widget)

    widget.show()

    val focusRequester = FocusRequester()
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

private fun createBrowserWidget(browser: Browser): BrowserWidget {
    val widget = (browser as BrowserImpl).widget()
    widget.set(PaintCallback::class.java, OnPaint(image))
    widget.displayId(Display.primaryDisplay().id())
    return widget
}

private class OnPaint(private val image: MutableState<Image?>) : PaintCallback {
    private val memoryImage = MemoryImage()

    override fun on(params: Paint.Request): Paint.Response? {
        val request: PaintRequest = params.paintRequest
        val viewSize: Size = request.viewSize
        val dirtyRect: JxRect = request.dirtyRect
        if (!memoryImage.validateDirtyRect(dirtyRect, viewSize)) {
            return Paint.Response.newBuilder().build()
        }
        memoryImage.updatePixels(
            viewSize,
            dirtyRect,
            request.memoryId
        ) { updatedImage ->
            image.value = updatedImage
        }
        return Paint.Response.newBuilder().build()
    }
}
