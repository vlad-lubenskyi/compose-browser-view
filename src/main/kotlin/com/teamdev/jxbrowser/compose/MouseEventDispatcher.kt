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
        val clickCount = event.awtEventOrNull?.clickCount ?: 1
        widget.dispatch(
            MousePressed.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(mouseButton(event))
                .clickCount(clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseReleased(event: PointerEvent, position: Offset) {
        val clickCount = event.awtEventOrNull?.clickCount ?: 1
        widget.dispatch(
            MouseReleased.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(releasedMouseButton(event))
                .clickCount(clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseEntered(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseEntered.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(mouseButton(event))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseExited(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseExited.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .button(mouseButton(event))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseMoved(event: PointerEvent, position: Offset) {
        widget.dispatch(
            MouseMoved.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseScrolled(event: PointerEvent, position: Offset) {
        val awtEvent = event.awtEventOrNull as MouseWheelEvent
        val directionFix = -1
        val scrollType = ScrollType.forNumber(awtEvent.scrollType)
        if (scrollType == null) {
            ScrollType.SCROLL_TYPE_UNSPECIFIED
        }
        val pointsPerUnit: Float = if (Environment.isMac()) 10F else 100f / 3
        val delta: Float = awtEvent.unitsToScroll * pointsPerUnit * directionFix
        val deltaX: Float = if (awtEvent.isShiftDown) delta else 0F
        val deltaY: Float = if (!awtEvent.isShiftDown) delta else 0F
        widget.dispatch(
            MouseWheel.newBuilder(localPoint(position))
                .locationOnScreen(screenPoint(event, position))
                .deltaX(deltaX)
                .deltaY(deltaY)
                .scrollType(scrollType)
                .keyModifiers(keyModifiers(event))
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

    private fun releasedMouseButton(event: PointerEvent): MouseButton {
        val awtEvent = event.awtEventOrNull
        return if (isLeftMouseButton(awtEvent)) {
            MouseButton.PRIMARY
        } else if (isMiddleMouseButton(awtEvent)) {
            MouseButton.MIDDLE
        } else if (isRightMouseButton(awtEvent)) {
            MouseButton.SECONDARY
        } else if (awtEvent?.button == MouseEvent.NOBUTTON) {
            MouseButton.NO_BUTTON
        } else {
            MouseButton.MOUSE_BUTTON_UNSPECIFIED
        }
    }

    private fun mouseModifiers(event: PointerEvent): MouseModifiers {
        val buttons = event.buttons
        return MouseModifiers.newBuilder()
            .primaryButtonDown(buttons.isPrimaryPressed)
            .middleButtonDown(buttons.isTertiaryPressed)
            .secondaryButtonDown(buttons.isSecondaryPressed)
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