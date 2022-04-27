/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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

class BrowserView(browser: Browser) {
    private var image: MutableState<Image?> = mutableStateOf(null)
    private val widget: BrowserWidget
    private val layoutListener: LayoutListener
    private val mouseDispatcher: MouseEventDispatcher
    private val keyDispatcher: KeyEventDispatcher

    init {
        widget = createBrowserWidget(browser)
        layoutListener = LayoutListener(widget)
        mouseDispatcher = MouseEventDispatcher(widget)
        keyDispatcher = KeyEventDispatcher(widget)
    }

    @Composable
    fun composable() {
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
                    true
                }
                .onFocusEvent {
                    if (it.hasFocus) widget.focus() else widget.unfocus()
                }
                .focusable()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas { canvas ->
                    image.value?.let {
                        canvas.nativeCanvas.drawImage(it, 0f, 0f)
                    }
                }
                focusRequester.requestFocus()
            }
        }
    }

    private fun createBrowserWidget(browser: Browser): BrowserWidget {
        val widget = (browser as BrowserImpl).widget()
        widget.set(PaintCallback::class.java, OnPaint(image))
        widget.displayId(Display.primaryDisplay().id())
        return widget
    }
}

class OnPaint(private val image: MutableState<Image?>) : PaintCallback {
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