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

import androidx.compose.ui.input.key.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.os.Environment.isMac
import com.teamdev.jxbrowser.ui.KeyLocation
import com.teamdev.jxbrowser.ui.KeyModifiers
import com.teamdev.jxbrowser.ui.event.KeyPressed
import com.teamdev.jxbrowser.ui.event.KeyReleased
import com.teamdev.jxbrowser.ui.event.KeyTyped
import com.teamdev.jxbrowser.ui.internal.KeyCodes
import com.teamdev.jxbrowser.ui.internal.KeyEvents.isMacAccessKey
import java.awt.event.KeyEvent.*

/**
 * Dispatches Compose key events to Chromium.
 */
internal class KeyEventDispatcher(private val widget: BrowserWidget) {

    /**
     * Dispatches the given key `event` to Chromium.
     *
     * @param event a Compose key event
     */
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
        val awtKeyEvent = event.nativeKeyEvent as java.awt.event.KeyEvent
        val keyCode = ComposeKey.from(event).chromiumCode()
        val modifiers = keyModifiers(event)
        val builder = KeyPressed.newBuilder(keyCode)
            .keyLocation(keyLocation(event))
            .keyModifiers(modifiers)

        var keyChar = awtKeyEvent.keyChar
        if (isMac() && isMacAccessKey(keyCode, modifiers)) {
            keyChar = KeyCodes.toAlphabeticChar(keyCode)
        }
        if (keyChar != CHAR_UNDEFINED) {
            builder.keyChar(keyChar)
        }
        val keyPressed = builder.build()
        widget.dispatch(keyPressed)

        // Compose does not fire the key typed event, but Chromium requires it.
        // So, we fire the key typed event manually in between the key pressed and
        // key released events.
        if (keyChar != CHAR_UNDEFINED) {
            notifyKeyTyped(keyPressed)
        }
    }

    private fun notifyKeyTyped(keyPressed: KeyPressed) {
        widget.dispatch(
            KeyTyped.newBuilder(keyPressed.keyCode())
                .keyLocation(keyPressed.keyLocation())
                .keyModifiers(keyPressed.keyModifiers())
                .keyChar(keyPressed.keyChar())
                .build()
        )
    }

    private fun keyReleased(event: KeyEvent) {
        val keyCode = ComposeKey.from(event).chromiumCode()
        widget.dispatch(
            KeyReleased.newBuilder(keyCode)
                .keyLocation(keyLocation(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun keyLocation(event: KeyEvent): KeyLocation {
        return when (event.key.nativeKeyLocation) {
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
