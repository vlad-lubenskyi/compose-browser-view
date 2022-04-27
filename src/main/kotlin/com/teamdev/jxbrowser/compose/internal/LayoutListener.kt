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

class LayoutListener(private val widget: BrowserWidget) {
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
        widget.bounds(
            Rect.of(location.x, location.y, size.width / 2, size.height / 2),
            Rect.of(location.x, location.y, size.width / 2, size.height / 2)
        )
    }

    companion object {
        const val SCALE_FACTOR = 2
    }
}