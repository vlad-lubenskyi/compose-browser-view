package com.teamdev.jxbrowser.compose.internal

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.pointer.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.compose.internal.LayoutListener.Companion.SCALE_FACTOR
import com.teamdev.jxbrowser.os.Environment.isMac
import com.teamdev.jxbrowser.ui.*
import com.teamdev.jxbrowser.ui.event.*
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import javax.swing.SwingUtilities.*

class MouseEventDispatcher(private val widget: BrowserWidget) {

    @OptIn(ExperimentalComposeUiApi::class)
    fun dispatch(event: PointerEvent) {
        when (event.type) {
            PointerEventType.Press -> {
                mousePressed(event)
            }
            PointerEventType.Release -> {
                mouseReleased(event)
            }
            PointerEventType.Move -> {
                mouseMoved(event)
            }
            PointerEventType.Enter -> {
                mouseEntered(event)
            }
            PointerEventType.Exit -> {
                mouseExited(event)
            }
            PointerEventType.Scroll -> {
                mouseScrolled(event)
            }
        }
    }

    private fun mousePressed(event: PointerEvent) {
        val clickCount = event.awtEventOrNull?.clickCount ?: 1
        widget.dispatch(
            MousePressed.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
                .button(mouseButton(event))
                .clickCount(clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseReleased(event: PointerEvent) {
        val clickCount = event.awtEventOrNull?.clickCount ?: 1
        widget.dispatch(
            MouseReleased.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
                .button(releasedMouseButton(event))
                .clickCount(clickCount)
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseEntered(event: PointerEvent) {
        widget.dispatch(
            MouseEntered.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
                .button(mouseButton(event))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseExited(event: PointerEvent) {
        widget.dispatch(
            MouseExited.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
                .button(mouseButton(event))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseMoved(event: PointerEvent) {
        widget.dispatch(
            MouseMoved.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
                .mouseModifiers(mouseModifiers(event))
                .keyModifiers(keyModifiers(event))
                .build()
        )
    }

    private fun mouseScrolled(event: PointerEvent) {
        val awtEvent = event.awtEventOrNull as MouseWheelEvent
        val scrollType = ScrollType.forNumber(awtEvent.scrollType) ?: ScrollType.SCROLL_TYPE_UNSPECIFIED
        val pointsPerUnit = if (isMac()) 10F else 100f / 3
        val directionFix = -1
        val delta = awtEvent.unitsToScroll * pointsPerUnit * directionFix
        val deltaX = if (awtEvent.isShiftDown) delta else 0F
        val deltaY = if (!awtEvent.isShiftDown) delta else 0F
        widget.dispatch(
            MouseWheel.newBuilder(localPoint(event))
                .locationOnScreen(screenPoint(event))
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

    private fun localPoint(event: PointerEvent): Point {
        val position = event.changes.first().position
        val x = position.x.toInt()
        val y = position.y.toInt()
        return Point.of(x / SCALE_FACTOR, y / SCALE_FACTOR)
    }

    private fun screenPoint(event: PointerEvent): Point {
        val awtEvent = event.awtEventOrNull ?: return localPoint(event)
        val location = awtEvent.locationOnScreen
        return Point.of(location.x, location.y)
    }
}