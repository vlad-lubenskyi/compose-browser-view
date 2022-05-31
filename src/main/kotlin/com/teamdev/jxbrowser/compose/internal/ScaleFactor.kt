/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.compose.internal

/**
 * A scale factor used to render JxBrowser.
 *
 * This is a simplification. In a real-life scenario, we would need to use the means provided by AWT and
 * the operating system to get the scale factor of a particular display.
 */
internal object ScaleFactor {
    const val value = 2
}
