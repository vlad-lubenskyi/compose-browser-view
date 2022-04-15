package com.teamdev.jxbrowser.compose

import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.ui.KeyModifiers
import com.teamdev.jxbrowser.ui.MouseButton
import com.teamdev.jxbrowser.ui.MouseModifiers
import com.teamdev.jxbrowser.ui.Point
import com.teamdev.jxbrowser.ui.event.*
import java.awt.event.InputEvent
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities.*

class MouseEventDispatcher(private val widget: BrowserWidget) {

    fun dispatch(event: PointerEvent) {
        val position = event.changes.first().position
        when (event.type) {
            PointerEventType.Press -> {
                mousePressed(event, position)
            }
            PointerEventType.Release -> {
                mouseReleased(event, position)
            }
            PointerEventType.Move -> {
                mouseMoved(event, position)
            }
            PointerEventType.Enter -> {
                mouseEntered(event, position)
            }
            PointerEventType.Exit -> {
                mouseExited(event, position)
            }
        }
    }

    private fun mousePressed(event: PointerEvent, position: Offset) {
        val awtEvent = event.awtEventOrNull!!
        val locationOnScreen = Point.of(awtEvent.locationOnScreen.x, awtEvent.locationOnScreen.y)
        widget.dispatch(
            MousePressed.newBuilder(point(position))
                .button(mouseButton(event))
                .locationOnScreen(locationOnScreen)
                .clickCount(awtEvent.clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseReleased(event: PointerEvent, position: Offset) {
        val awtEvent = event.awtEventOrNull!!
        val locationOnScreen = Point.of(awtEvent.locationOnScreen.x, awtEvent.locationOnScreen.y)
        widget.dispatch(
            MouseReleased.newBuilder(point(position))
                .button(releasedMouseButton(awtEvent))
                .locationOnScreen(locationOnScreen)
                .clickCount(awtEvent.clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseEntered(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseEntered.newBuilder(point(position))
                .build()
        )
    }

    private fun mouseExited(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseExited.newBuilder(point(position))
                .build()
        )
    }

    private fun mouseMoved(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseMoved.newBuilder(point(position))
                .build()
        )
    }

    private fun mouseButton(event: PointerEvent): MouseButton {
        val buttons = event.buttons
        return if (buttons.isPrimaryPressed) {
            MouseButton.PRIMARY
        } else if (buttons.isBackPressed) {
            MouseButton.BACK
        } else if (buttons.isForwardPressed) {
            MouseButton.FORWARD
        } else if (buttons.isSecondaryPressed) {
            MouseButton.SECONDARY
        } else if (buttons.isTertiaryPressed) {
            MouseButton.MIDDLE
        } else {
            MouseButton.NO_BUTTON
        }
    }

    private fun releasedMouseButton(event: MouseEvent): MouseButton {
        return if (isLeftMouseButton(event)) {
            MouseButton.PRIMARY
        } else if (isMiddleMouseButton(event)) {
            MouseButton.MIDDLE
        } else if (isRightMouseButton(event)) {
            MouseButton.SECONDARY
        } else if (event.button == MouseEvent.NOBUTTON) {
            MouseButton.NO_BUTTON
        } else {
            MouseButton.MOUSE_BUTTON_UNSPECIFIED
        }
    }

    private fun mouseModifiers(event: PointerEvent): MouseModifiers {
        val awtEvent = event.awtEventOrNull!!
        val modifiersEx = awtEvent.modifiersEx
        return MouseModifiers.newBuilder()
            .primaryButtonDown(
                modifiersEx and InputEvent.BUTTON1_DOWN_MASK == InputEvent.BUTTON1_DOWN_MASK
            )
            .middleButtonDown(
                modifiersEx and InputEvent.BUTTON2_DOWN_MASK == InputEvent.BUTTON2_DOWN_MASK
            )
            .secondaryButtonDown(
                modifiersEx and InputEvent.BUTTON3_DOWN_MASK == InputEvent.BUTTON3_DOWN_MASK
            )
            .build()
    }

    private fun keyModifiers(event: PointerEvent): KeyModifiers {
        val keyboardModifiers = event.keyboardModifiers
        return KeyModifiers.newBuilder()
            .altDown(keyboardModifiers.isAltPressed)
            .altGraphDown(keyboardModifiers.isAltGraphPressed)
            .controlDown(keyboardModifiers.isCtrlPressed)
            .shiftDown(keyboardModifiers.isShiftPressed)
            .metaDown(keyboardModifiers.isMetaPressed)
            .build()
    }

    private fun point(offset: Offset) =
        Point.of(offset.x.toInt() / LayoutListener.SCALE_FACTOR, offset.y.toInt() / LayoutListener.SCALE_FACTOR)
}