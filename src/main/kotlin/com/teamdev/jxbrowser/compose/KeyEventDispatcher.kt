package com.teamdev.jxbrowser.compose

import androidx.compose.ui.input.key.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.os.Environment.isMac
import com.teamdev.jxbrowser.ui.KeyCode
import com.teamdev.jxbrowser.ui.KeyLocation
import com.teamdev.jxbrowser.ui.KeyModifiers
import com.teamdev.jxbrowser.ui.event.KeyPressed
import com.teamdev.jxbrowser.ui.event.KeyReleased
import com.teamdev.jxbrowser.ui.event.KeyTyped
import com.teamdev.jxbrowser.ui.internal.KeyCodes
import com.teamdev.jxbrowser.ui.internal.KeyEvents.isMacAccessKey
import java.awt.event.KeyEvent.CHAR_UNDEFINED
import java.awt.event.KeyEvent.VK_ENTER

class KeyEventDispatcher(private val widget: BrowserWidget) {

    fun dispatch(event: KeyEvent) {
        when (event.type) {
            KeyEventType.Unknown -> {
                keyTyped(event)
            }
            KeyEventType.KeyDown -> {
                keyPressed(event)
            }
            KeyEventType.KeyUp -> {
                keyReleased(event)
            }
        }
    }

    private fun keyTyped(event: KeyEvent) {
        val awtKeyEvent = event.nativeKeyEvent as java.awt.event.KeyEvent
        val keyCode = keyCode(awtKeyEvent)
        val keyLocation = keyLocation(awtKeyEvent)
        val modifiers = keyModifiers(event)
        var keyChar = awtKeyEvent.keyChar
        if (isMac() && isMacAccessKey(keyCode, modifiers)) {
            keyChar = KeyCodes.toAlphabeticChar(keyCode)
        }
        widget.dispatch(
            KeyTyped.newBuilder(keyCode)
                .keyLocation(keyLocation)
                .keyModifiers(keyModifiers(event))
                .keyChar(keyChar)
                .build()
        )
    }

    private fun keyPressed(event: KeyEvent) {
        val awtKeyEvent = event.nativeKeyEvent as java.awt.event.KeyEvent
        var keyCode = keyCode(awtKeyEvent)
        if (isSystemKey(awtKeyEvent)) {
            keyCode = KeyCode.KEY_CODE_RETURN
        }
        val keyLocation = keyLocation(awtKeyEvent)
        val builder = KeyPressed.newBuilder(keyCode)
            .keyLocation(keyLocation)
            .keyModifiers(keyModifiers(event))

        val keyChar = awtKeyEvent.keyChar
        if (keyChar != CHAR_UNDEFINED && !isSystemKey(awtKeyEvent)) {
            builder.keyChar(keyChar)
        }
        widget.dispatch(builder.build())
    }

    private fun keyReleased(event: KeyEvent) {
        val awtKeyEvent = event.nativeKeyEvent as java.awt.event.KeyEvent
        var keyCode = keyCode(awtKeyEvent)
        if (isSystemKey(awtKeyEvent)) {
            keyCode = KeyCode.KEY_CODE_RETURN
        }
        val keyLocation = keyLocation(awtKeyEvent)
        widget.dispatch(
            KeyReleased.newBuilder(keyCode)
                .keyLocation(keyLocation)
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun keyCode(e: java.awt.event.KeyEvent): KeyCode {
        return KeyCode.forNumber(e.keyCode) ?: KeyCode.KEY_CODE_UNSPECIFIED
    }

    private fun keyLocation(e: java.awt.event.KeyEvent): KeyLocation {
        return KeyLocation.forNumber(e.keyLocation) ?: KeyLocation.KEY_LOCATION_UNSPECIFIED
    }

    private fun isSystemKey(e: java.awt.event.KeyEvent): Boolean {
        return e.keyCode == VK_ENTER
    }

    private fun keyModifiers(event: KeyEvent): KeyModifiers {
        return KeyModifiers.newBuilder()
            .altDown(event.isAltPressed)
            .controlDown(event.isCtrlPressed)
            .shiftDown(event.isShiftPressed)
            .metaDown(event.isMetaPressed)
            .build()
    }
}