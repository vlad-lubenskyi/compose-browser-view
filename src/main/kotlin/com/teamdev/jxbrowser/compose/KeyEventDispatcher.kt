package com.teamdev.jxbrowser.compose

import androidx.compose.ui.input.key.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.compose.internal.ComposeKey
import com.teamdev.jxbrowser.compose.internal.ComposeKeyCodes
import com.teamdev.jxbrowser.internal.ui.ToolkitKeyCodes
import com.teamdev.jxbrowser.os.Environment.isMac
import com.teamdev.jxbrowser.ui.KeyLocation
import com.teamdev.jxbrowser.ui.KeyModifiers
import com.teamdev.jxbrowser.ui.event.KeyPressed
import com.teamdev.jxbrowser.ui.event.KeyReleased
import com.teamdev.jxbrowser.ui.event.KeyTyped
import com.teamdev.jxbrowser.ui.internal.KeyCodes
import com.teamdev.jxbrowser.ui.internal.KeyEvents.isMacAccessKey
import java.awt.event.KeyEvent.*

class KeyEventDispatcher(private val widget: BrowserWidget) {

    private val keyCodes: ToolkitKeyCodes<ComposeKey> = ComposeKeyCodes.instance()

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
        val keyCode = keyCodes.toKeyCode(ComposeKey.from(event))
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
        val keyCode = keyCodes.toKeyCode(ComposeKey.from(event))
        val keyLocation = keyLocation(awtKeyEvent)
        val builder = KeyPressed.newBuilder(keyCode)
            .keyLocation(keyLocation)
            .keyModifiers(keyModifiers(event))

        val keyChar = awtKeyEvent.keyChar
        if (keyChar != CHAR_UNDEFINED) {
            builder.keyChar(keyChar)
        }
        widget.dispatch(builder.build())
    }

    private fun keyReleased(event: KeyEvent) {
        val awtKeyEvent = event.nativeKeyEvent as java.awt.event.KeyEvent
        val keyCode = keyCodes.toKeyCode(ComposeKey.from(event))
        val keyLocation = keyLocation(awtKeyEvent)
        widget.dispatch(
            KeyReleased.newBuilder(keyCode)
                .keyLocation(keyLocation)
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun keyLocation(e: java.awt.event.KeyEvent): KeyLocation {
        return when (e.keyLocation) {
            KEY_LOCATION_NUMPAD -> {
                KeyLocation.NUMERIC_KEYPAD
            }
            KEY_LOCATION_LEFT -> {
                KeyLocation.LEFT
            }
            KEY_LOCATION_RIGHT -> {
                KeyLocation.RIGHT
            }
            else -> {
                KeyLocation.STANDARD
            }
        }
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