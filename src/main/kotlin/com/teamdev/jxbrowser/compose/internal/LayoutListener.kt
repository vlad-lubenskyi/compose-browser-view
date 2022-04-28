/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.compose.internal

import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.ui.Rect

internal class LayoutListener(private val widget: BrowserWidget) {
    private var size: IntSize = IntSize.Zero
    private var location: IntOffset = IntOffset.Zero

    fun onLayout(): MeasureScope.(Measurable, Constraints) -> MeasureResult {
        return { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            this@LayoutListener.size = IntSize(width = placeable.width, height = placeable.height)
            updateBounds()
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
    }

    fun onPositioned(coordinates: LayoutCoordinates) {
        val positionInRoot = coordinates.positionInRoot()
        location = IntOffset(
            positionInRoot.x.toInt(),
            positionInRoot.y.toInt()
        )
    }

    private fun updateBounds() {
        val boundsInWindow = Rect.of(location.x, location.y, size.width / SCALE_FACTOR, size.height / SCALE_FACTOR)
        val boundsInScreen = Rect.of(location.x, location.y, size.width / SCALE_FACTOR, size.height / SCALE_FACTOR)
        widget.bounds(boundsInWindow, boundsInScreen)
    }

    companion object {
        const val SCALE_FACTOR = 2
    }
}