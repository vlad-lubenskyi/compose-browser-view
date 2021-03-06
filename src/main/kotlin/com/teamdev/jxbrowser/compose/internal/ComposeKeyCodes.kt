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

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.teamdev.jxbrowser.internal.ui.ToolkitKeyCodes
import com.teamdev.jxbrowser.ui.KeyCode

/**
 * A mapping of Compose key codes to [KeyCode]'s.
 *
 * The implementation of [Key] in Compose Desktop is part of an experimental API,
 * so we add the [OptIn] annotation to allow this API to be used.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal object ComposeKeyCodes {

    internal val codes: ToolkitKeyCodes<ComposeKey> = ToolkitKeyCodes.newBuilder<ComposeKey>()
        .add(ComposeKey.of(Key.ShiftLeft), KeyCode.KEY_CODE_SHIFT)
        .add(ComposeKey.left(Key.ShiftLeft), KeyCode.KEY_CODE_LSHIFT)
        .add(ComposeKey.right(Key.ShiftRight), KeyCode.KEY_CODE_RSHIFT)

        .add(ComposeKey.of(Key.CtrlLeft), KeyCode.KEY_CODE_CONTROL)
        .add(ComposeKey.left(Key.CtrlLeft), KeyCode.KEY_CODE_LCONTROL)
        .add(ComposeKey.right(Key.CtrlRight), KeyCode.KEY_CODE_RCONTROL)

        .add(ComposeKey.of(Key.AltLeft), KeyCode.KEY_CODE_MENU)
        .add(ComposeKey.left(Key.AltLeft), KeyCode.KEY_CODE_LMENU)
        .add(ComposeKey.right(Key.AltRight), KeyCode.KEY_CODE_RMENU)

        .add(ComposeKey.of(Key.MetaLeft), KeyCode.KEY_CODE_LCMD)
        .add(ComposeKey.left(Key.MetaLeft), KeyCode.KEY_CODE_LCMD)
        .add(ComposeKey.right(Key.MetaRight), KeyCode.KEY_CODE_RCMD)

        .add(ComposeKey.of(Key.Enter), KeyCode.KEY_CODE_RETURN)
        .add(ComposeKey.of(Key.Backspace), KeyCode.KEY_CODE_BACK)
        .add(ComposeKey.of(Key.Tab), KeyCode.KEY_CODE_TAB)
        .add(ComposeKey.of(Key.Clear), KeyCode.KEY_CODE_CLEAR)
        .add(ComposeKey.of(Key.CapsLock), KeyCode.KEY_CODE_CAPITAL)
        .add(ComposeKey.of(Key.Escape), KeyCode.KEY_CODE_ESCAPE)
        .add(ComposeKey.of(Key.Spacebar), KeyCode.KEY_CODE_SPACE)
        .add(ComposeKey.of(Key.PageUp), KeyCode.KEY_CODE_PRIOR)
        .add(ComposeKey.of(Key.PageDown), KeyCode.KEY_CODE_NEXT)
        .add(ComposeKey.of(Key.MoveEnd), KeyCode.KEY_CODE_END)
        .add(ComposeKey.of(Key.Home), KeyCode.KEY_CODE_HOME)
        .add(ComposeKey.of(Key.Delete), KeyCode.KEY_CODE_DELETE)
        .add(ComposeKey.of(Key.ScrollLock), KeyCode.KEY_CODE_SCROLL)
        .add(ComposeKey.of(Key.F1), KeyCode.KEY_CODE_F1)
        .add(ComposeKey.of(Key.F2), KeyCode.KEY_CODE_F2)
        .add(ComposeKey.of(Key.F3), KeyCode.KEY_CODE_F3)
        .add(ComposeKey.of(Key.F4), KeyCode.KEY_CODE_F4)
        .add(ComposeKey.of(Key.F5), KeyCode.KEY_CODE_F5)
        .add(ComposeKey.of(Key.F6), KeyCode.KEY_CODE_F6)
        .add(ComposeKey.of(Key.F7), KeyCode.KEY_CODE_F7)
        .add(ComposeKey.of(Key.F8), KeyCode.KEY_CODE_F8)
        .add(ComposeKey.of(Key.F9), KeyCode.KEY_CODE_F9)
        .add(ComposeKey.of(Key.F10), KeyCode.KEY_CODE_F10)
        .add(ComposeKey.of(Key.F11), KeyCode.KEY_CODE_F11)
        .add(ComposeKey.of(Key.F12), KeyCode.KEY_CODE_F12)
        .add(ComposeKey.of(Key.PrintScreen), KeyCode.KEY_CODE_SNAPSHOT)
        .add(ComposeKey.of(Key.Insert), KeyCode.KEY_CODE_INSERT)
        .add(ComposeKey.of(Key.Help), KeyCode.KEY_CODE_HELP)
        .add(ComposeKey.of(Key.Grave), KeyCode.KEY_CODE_OEM_3)
        .add(ComposeKey.of(Key.Apostrophe), KeyCode.KEY_CODE_OEM_7)
        .add(ComposeKey.of(Key.At), KeyCode.KEY_CODE_ATTN)
        .add(ComposeKey.of(Key.Semicolon), KeyCode.KEY_CODE_OEM_1)
        .add(ComposeKey.of(Key.LeftBracket), KeyCode.KEY_CODE_OEM_4)
        .add(ComposeKey.of(Key.Backslash), KeyCode.KEY_CODE_OEM_5)
        .add(ComposeKey.of(Key.RightBracket), KeyCode.KEY_CODE_OEM_6)
        .add(ComposeKey.of(Key.Equals), KeyCode.KEY_CODE_OEM_PLUS)
        .add(ComposeKey.of(Key.Plus), KeyCode.KEY_CODE_OEM_PLUS)
        .add(ComposeKey.of(Key.DirectionUp), KeyCode.KEY_CODE_UP)
        .add(ComposeKey.of(Key.DirectionLeft), KeyCode.KEY_CODE_LEFT)
        .add(ComposeKey.of(Key.DirectionRight), KeyCode.KEY_CODE_RIGHT)
        .add(ComposeKey.of(Key.DirectionDown), KeyCode.KEY_CODE_DOWN)
        .add(ComposeKey.of(Key.Comma), KeyCode.KEY_CODE_OEM_COMMA)
        .add(ComposeKey.of(Key.Minus), KeyCode.KEY_CODE_OEM_MINUS)
        .add(ComposeKey.of(Key.Period), KeyCode.KEY_CODE_OEM_PERIOD)
        .add(ComposeKey.of(Key.Slash), KeyCode.KEY_CODE_OEM_2)
        .add(ComposeKey.of(Key.Zero), KeyCode.KEY_CODE_O)
        .add(ComposeKey.of(Key.One), KeyCode.KEY_CODE_1)
        .add(ComposeKey.of(Key.Two), KeyCode.KEY_CODE_2)
        .add(ComposeKey.of(Key.Three), KeyCode.KEY_CODE_3)
        .add(ComposeKey.of(Key.Four), KeyCode.KEY_CODE_4)
        .add(ComposeKey.of(Key.Five), KeyCode.KEY_CODE_5)
        .add(ComposeKey.of(Key.Six), KeyCode.KEY_CODE_6)
        .add(ComposeKey.of(Key.Seven), KeyCode.KEY_CODE_7)
        .add(ComposeKey.of(Key.Eight), KeyCode.KEY_CODE_8)
        .add(ComposeKey.of(Key.Nine), KeyCode.KEY_CODE_9)
        .add(ComposeKey.of(Key.A), KeyCode.KEY_CODE_A)
        .add(ComposeKey.of(Key.B), KeyCode.KEY_CODE_B)
        .add(ComposeKey.of(Key.C), KeyCode.KEY_CODE_C)
        .add(ComposeKey.of(Key.D), KeyCode.KEY_CODE_D)
        .add(ComposeKey.of(Key.E), KeyCode.KEY_CODE_E)
        .add(ComposeKey.of(Key.F), KeyCode.KEY_CODE_F)
        .add(ComposeKey.of(Key.G), KeyCode.KEY_CODE_G)
        .add(ComposeKey.of(Key.H), KeyCode.KEY_CODE_H)
        .add(ComposeKey.of(Key.I), KeyCode.KEY_CODE_I)
        .add(ComposeKey.of(Key.J), KeyCode.KEY_CODE_J)
        .add(ComposeKey.of(Key.K), KeyCode.KEY_CODE_K)
        .add(ComposeKey.of(Key.L), KeyCode.KEY_CODE_L)
        .add(ComposeKey.of(Key.M), KeyCode.KEY_CODE_M)
        .add(ComposeKey.of(Key.N), KeyCode.KEY_CODE_N)
        .add(ComposeKey.of(Key.O), KeyCode.KEY_CODE_O)
        .add(ComposeKey.of(Key.P), KeyCode.KEY_CODE_P)
        .add(ComposeKey.of(Key.Q), KeyCode.KEY_CODE_Q)
        .add(ComposeKey.of(Key.R), KeyCode.KEY_CODE_R)
        .add(ComposeKey.of(Key.S), KeyCode.KEY_CODE_S)
        .add(ComposeKey.of(Key.T), KeyCode.KEY_CODE_T)
        .add(ComposeKey.of(Key.U), KeyCode.KEY_CODE_U)
        .add(ComposeKey.of(Key.V), KeyCode.KEY_CODE_V)
        .add(ComposeKey.of(Key.W), KeyCode.KEY_CODE_W)
        .add(ComposeKey.of(Key.X), KeyCode.KEY_CODE_X)
        .add(ComposeKey.of(Key.Y), KeyCode.KEY_CODE_Y)
        .add(ComposeKey.of(Key.Z), KeyCode.KEY_CODE_Z)

        .add(ComposeKey.numpad(Key.NumPadEnter), KeyCode.KEY_CODE_RETURN)
        .add(ComposeKey.numpad(Key.NumPad0), KeyCode.KEY_CODE_NUMPAD0)
        .add(ComposeKey.numpad(Key.NumPad1), KeyCode.KEY_CODE_NUMPAD1)
        .add(ComposeKey.numpad(Key.NumPad2), KeyCode.KEY_CODE_NUMPAD2)
        .add(ComposeKey.numpad(Key.NumPad3), KeyCode.KEY_CODE_NUMPAD3)
        .add(ComposeKey.numpad(Key.NumPad4), KeyCode.KEY_CODE_NUMPAD4)
        .add(ComposeKey.numpad(Key.NumPad5), KeyCode.KEY_CODE_NUMPAD5)
        .add(ComposeKey.numpad(Key.NumPad6), KeyCode.KEY_CODE_NUMPAD6)
        .add(ComposeKey.numpad(Key.NumPad7), KeyCode.KEY_CODE_NUMPAD7)
        .add(ComposeKey.numpad(Key.NumPad8), KeyCode.KEY_CODE_NUMPAD8)
        .add(ComposeKey.numpad(Key.NumPad9), KeyCode.KEY_CODE_NUMPAD9)
        .add(ComposeKey.numpad(Key.NumPadMultiply), KeyCode.KEY_CODE_MULTIPLY)
        .add(ComposeKey.numpad(Key.NumPadAdd), KeyCode.KEY_CODE_ADD)
        .add(ComposeKey.numpad(Key.NumPadSubtract), KeyCode.KEY_CODE_SUBTRACT)
        .add(ComposeKey.numpad(Key.NumPadDot), KeyCode.KEY_CODE_DECIMAL)
        .add(ComposeKey.numpad(Key.NumLock), KeyCode.KEY_CODE_NUMLOCK)
        .add(ComposeKey.numpad(Key.DirectionUp), KeyCode.KEY_CODE_UP)
        .add(ComposeKey.numpad(Key.DirectionDown), KeyCode.KEY_CODE_DOWN)
        .add(ComposeKey.numpad(Key.DirectionLeft), KeyCode.KEY_CODE_LEFT)
        .add(ComposeKey.numpad(Key.DirectionRight), KeyCode.KEY_CODE_RIGHT)
        .add(ComposeKey.numpad(Key.Clear), KeyCode.KEY_CODE_CLEAR)
        .add(ComposeKey.numpad(Key.PageUp), KeyCode.KEY_CODE_PRIOR)
        .add(ComposeKey.numpad(Key.PageDown), KeyCode.KEY_CODE_DOWN)
        .add(ComposeKey.numpad(Key.MoveEnd), KeyCode.KEY_CODE_END)
        .add(ComposeKey.numpad(Key.MoveHome), KeyCode.KEY_CODE_HOME)
        .build()
}
