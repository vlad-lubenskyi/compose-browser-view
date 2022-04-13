package com.teamdev.jxbrowser.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.ui.MouseButton
import com.teamdev.jxbrowser.ui.Point
import com.teamdev.jxbrowser.ui.event.*

class MouseEventDispatcher(private val widget: BrowserWidget) {

    fun dispatch(event: PointerEvent) {
        val position = event.changes.first().position
        when (event.type) {
            PointerEventType.Press -> {
                mousePressed(position, event)
            }
            PointerEventType.Move -> {
                mouseMoved(position)
            }
            PointerEventType.Enter -> {
                mouseEntered(event, position)
            }
            PointerEventType.Exit -> {
                mouseExited(event, position)
            }
            PointerEventType.Release -> {
                mouseReleased(event, position)
            }
        }
    }

    private fun mouseReleased(event: PointerEvent, position: Offset) {
        val build = MouseReleased.newBuilder(
            Point.of(
                position.x.toInt() / LayoutListener.SCALE_FACTOR,
                position.y.toInt() / LayoutListener.SCALE_FACTOR
            )
        )
            .button(mouseButton(event))
            .clickCount(1)
            .build()
        widget.dispatch(build)
    }

    private fun mouseEntered(event: PointerEvent, position: Offset) {
        val point = point(position)
        val jxEvent = MouseEntered.newBuilder(point)
            .button(mouseButton(event))
            .locationOnScreen(point)
            .build()
        widget.dispatch(jxEvent)
    }

    private fun mouseExited(event: PointerEvent, position: Offset) {
        val jxEvent = MouseExited.newBuilder(Point.of(position.x.toInt() / 2, position.y.toInt() / 2))
            .build()
        widget.dispatch(jxEvent)
    }

    private fun mouseMoved(position: Offset) {
        val jxEvent = MouseMoved.newBuilder(Point.of(position.x.toInt() / 2, position.y.toInt() / 2))
            .build()
        widget.dispatch(jxEvent)
    }

    private fun mousePressed(
        position: Offset,
        event: PointerEvent
    ) {
        val build = MousePressed.newBuilder(Point.of(position.x.toInt() / 2, position.y.toInt() / 2))
            .button(mouseButton(event))
            .clickCount(1)
            .build()
        widget.dispatch(build)
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

    private fun point(offset: Offset) =
        Point.of(offset.x.toInt() / LayoutListener.SCALE_FACTOR, offset.y.toInt() / LayoutListener.SCALE_FACTOR)


}