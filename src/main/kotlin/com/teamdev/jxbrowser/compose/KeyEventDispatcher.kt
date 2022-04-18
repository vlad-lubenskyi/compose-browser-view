package com.teamdev.jxbrowser.compose

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.ui.KeyCode
import com.teamdev.jxbrowser.ui.event.KeyPressed
import com.teamdev.jxbrowser.ui.event.KeyReleased

class KeyEventDispatcher(private val widget: BrowserWidget) {

    fun dispatch(event: KeyEvent) {
        when (event.type) {
            KeyEventType.KeyDown -> {
                keyPressed(event)
            }
            KeyEventType.KeyUp -> {
                keyReleased(event)
            }
        }
    }

    private fun keyPressed(event: KeyEvent) {
        widget.dispatch(
            KeyPressed.newBuilder(KeyCode.KEY_CODE_L)
                .build()
        )
    }

    private fun keyReleased(event: KeyEvent) {
        widget.dispatch(
            KeyReleased.newBuilder(KeyCode.KEY_CODE_L)
                .build()
        )
    }
}