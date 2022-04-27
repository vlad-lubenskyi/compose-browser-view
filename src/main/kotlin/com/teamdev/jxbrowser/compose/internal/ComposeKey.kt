/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.compose.internal

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyLocation
import com.teamdev.jxbrowser.internal.ui.ToolkitKey
import java.awt.event.KeyEvent

/**
 * A Compose-specific representation of a keyboard key.
 */
internal class ComposeKey private constructor(keyCode: Key, keyLocation: Int) :
    ToolkitKey<Key?, Int?>(keyCode, keyLocation) {

    companion object {
        fun of(keyCode: Key): ComposeKey {
            return ComposeKey(keyCode, KeyEvent.KEY_LOCATION_STANDARD)
        }

        fun of(keyCode: Key, keyLocation: Int): ComposeKey {
            return ComposeKey(keyCode, keyLocation)
        }

        fun from(event: androidx.compose.ui.input.key.KeyEvent): ComposeKey {
            return of(event.key, event.key.nativeKeyLocation)
        }

        fun left(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_LEFT)
        }

        fun right(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_RIGHT)
        }

        fun numpad(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_NUMPAD)
        }
    }
}