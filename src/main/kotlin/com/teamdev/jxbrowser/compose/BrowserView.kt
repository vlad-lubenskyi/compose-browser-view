package com.teamdev.jxbrowser.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import com.teamdev.jxbrowser.internal.Display
import com.teamdev.jxbrowser.ui.Size
import org.jetbrains.skia.Image
import com.teamdev.jxbrowser.ui.Rect as JxRect

class BrowserView(browser: Browser) {
    private var image: MutableState<Image?> = mutableStateOf(null)
    private val layoutListener: LayoutListener
    private val mouseDispatcher: MouseEventDispatcher
    private val keyDispatcher: KeyEventDispatcher
    private val widget: BrowserWidget

    init {
        widget = (browser as BrowserImpl).widget()
        widget.set(PaintCallback::class.java, OnPaint(image))
        widget.displayId(Display.primaryDisplay().id())
        layoutListener = LayoutListener(widget)
        mouseDispatcher = MouseEventDispatcher(widget)
        keyDispatcher = KeyEventDispatcher(widget)
    }

    @Composable
    fun composable() {
        widget.show()

        val focusRequester = remember { FocusRequester() }
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
                .onFocusEvent {
                    if (it.hasFocus) {
                        widget.focus()
                    } else {
                        widget.unfocus()
                    }
                }
                .onKeyEvent {
                    keyDispatcher.dispatch(it)
                    false
                }
                .focusable()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas { canvas ->
                    image.value?.let {
                        canvas.nativeCanvas.drawImage(it, 0f, 0f)
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}


class OnPaint(private val image: MutableState<Image?>) : PaintCallback {
    private val memoryImage = MemoryImage()
    override fun on(params: Paint.Request): Paint.Response? {
        val request: PaintRequest = params.paintRequest
        val viewSize: Size = request.viewSize
        val dirtyRect: JxRect = request.dirtyRect
        if (!validateDirtyRect(dirtyRect, viewSize)) {
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

    private fun validateDirtyRect(dirtyRect: JxRect, viewSize: Size): Boolean {
        val viewWidth = viewSize.width()
        val viewHeight = viewSize.height()
        val dirtyRectOrigin = dirtyRect.origin()
        val dirtyRectSize = dirtyRect.size()
        return dirtyRectOrigin.x() <= viewWidth && dirtyRectOrigin.y() <= viewHeight && dirtyRectOrigin.x() + dirtyRectSize.width() <= viewWidth && dirtyRectOrigin.y() + dirtyRectSize.height() <= viewHeight
    }
}