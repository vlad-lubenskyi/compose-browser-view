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

import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.teamdev.jxbrowser.browser.internal.BrowserWidget
import com.teamdev.jxbrowser.ui.Rect

/**
 * A layout change listener that adjusts the size and location of the browser.
 */
internal class LayoutListener(private val widget: BrowserWidget) {
    private var size: IntSize = IntSize.Zero
    private var location: IntOffset = IntOffset.Zero

    /**
     * Called when bounds of the browser view change.
     *
     * It defines how the browser view is measured and laid out.
     */
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

    /**
     * Called when the position of the browser view change.
     *
     * It defines the location of the browser view.
     */
    fun onPositioned(coordinates: LayoutCoordinates) {
        val positionInRoot = coordinates.positionInRoot()
        location = IntOffset(
            positionInRoot.x.toInt(),
            positionInRoot.y.toInt()
        )
    }

    private fun updateBounds() {
        val boundsInWindow =
            Rect.of(location.x, location.y, size.width / ScaleFactor.value, size.height / ScaleFactor.value)
        val boundsInScreen =
            Rect.of(location.x, location.y, size.width / ScaleFactor.value, size.height / ScaleFactor.value)
        widget.bounds(boundsInWindow, boundsInScreen)
    }
}
