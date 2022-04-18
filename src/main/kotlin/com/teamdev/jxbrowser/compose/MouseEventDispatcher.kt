package com.teamdev.jxbrowser.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.compose.LayoutListener.Companion.SCALE_FACTOR
import com.teamdev.jxbrowser.os.Environment
import com.teamdev.jxbrowser.ui.*
import com.teamdev.jxbrowser.ui.event.*
import java.awt.event.InputEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import javax.swing.SwingUtilities.*

class MouseEventDispatcher(private val widget: BrowserWidget) {

    @OptIn(ExperimentalComposeUiApi::class)
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
            PointerEventType.Scroll -> {
                mouseScrolled(event, position)
            }
        }
    }

    private fun mousePressed(event: PointerEvent, position: Offset) {
        val awtEvent = event.awtEventOrNull!!
        widget.dispatch(
            MousePressed.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(mouseButton(event))
                .clickCount(awtEvent.clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseReleased(event: PointerEvent, position: Offset) {
        val awtEvent = event.awtEventOrNull!!
        widget.dispatch(
            MouseReleased.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(releasedMouseButton(awtEvent))
                .clickCount(awtEvent.clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseEntered(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseEntered.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .build()
        )
    }

    private fun mouseExited(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseExited.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .build()
        )
    }

    private fun mouseMoved(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseMoved.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .build()
        )
    }

    private fun mouseScrolled(pointerEvent: PointerEvent, position: Offset) {
        val e = pointerEvent.awtEventOrNull as MouseWheelEvent
        val directionFix = -1
        val scrollType = ScrollType.forNumber(e.scrollType)
        if (scrollType == null) {
            ScrollType.SCROLL_TYPE_UNSPECIFIED
        }
        val pointsPerUnit: Float = if (Environment.isMac()) 10F else 100f / 3
        val delta: Float = e.unitsToScroll * pointsPerUnit * directionFix
        val deltaX: Float = if (e.isShiftDown) delta else 0F
        val deltaY: Float = if (!e.isShiftDown) delta else 0F
        widget.dispatch(
            MouseWheel.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(pointerEvent, position))
                .deltaX(deltaX)
                .deltaY(deltaY)
                .scrollType(scrollType)
                .keyModifiers(keyModifiers(pointerEvent))
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

    private fun localPoint(offset: Offset) =
        Point.of(offset.x.toInt() / SCALE_FACTOR, offset.y.toInt() / SCALE_FACTOR)

    private fun screenPoint(event: PointerEvent, offset: Offset): Point {
        val awtEvent = event.awtEventOrNull ?: return localPoint(offset)
        val location = awtEvent.locationOnScreen
        val globalX = location.x + offset.x.toInt()
        val globalY = location.y + offset.y.toInt()
        return Point.of(globalX / SCALE_FACTOR, globalY / SCALE_FACTOR)
    }
}