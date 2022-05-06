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
        /**
         * Creates a key with a code and [standard][KeyEvent.KEY_LOCATION_STANDARD] location.
         *
         * @param keyCode a Compose key code as in [Key]
         */
        fun of(keyCode: Key): ComposeKey {
            return ComposeKey(keyCode, KeyEvent.KEY_LOCATION_STANDARD)
        }

        /**
         * Creates a key with a code and location.
         *
         * @param keyCode     a Compose key code as in [Key]
         * @param keyLocation a native key location as in [KeyEvent]
         */
        fun of(keyCode: Key, keyLocation: Int): ComposeKey {
            return ComposeKey(keyCode, keyLocation)
        }

        /**
         * Creates a key from the [androidx.compose.ui.input.key.KeyEvent].
         *
         * @param event a Compose key event
         */
        fun from(event: androidx.compose.ui.input.key.KeyEvent): ComposeKey {
            return of(event.key, event.key.nativeKeyLocation)
        }

        /**
         * Creates a key with a code and [left][KeyEvent.KEY_LOCATION_LEFT] location.
         *
         * @param keyCode a Compose key code as in [Key]
         */
        fun left(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_LEFT)
        }

        /**
         * Creates a key with a code and [right][KeyEvent.KEY_LOCATION_RIGHT] location.
         *
         * @param keyCode a Compose key code as in [Key]
         */
        fun right(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_RIGHT)
        }

        /**
         * Creates a key with a code and [numpad][KeyEvent.KEY_LOCATION_NUMPAD] location.
         *
         * @param keyCode a Compose key code as in [Key]
         */
        fun numpad(keyCode: Key): ComposeKey {
            return of(keyCode, KeyEvent.KEY_LOCATION_NUMPAD)
        }
    }
}
