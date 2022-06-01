/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.teamdev.jxbrowser.browser.Browser
import com.teamdev.jxbrowser.compose.BrowserView
import com.teamdev.jxbrowser.engine.Engine
import com.teamdev.jxbrowser.engine.RenderingMode.OFF_SCREEN

fun main() = application {
    val engine = Engine.newInstance(OFF_SCREEN)
    val browser = engine.newBrowser()
    val url = "https://www.google.com"

    Window(onCloseRequest = ::exitApplication, title = "JxBrowser in Compose Desktop") {
        Column {
            AddressBar(browser, url)
            BrowserView(browser)
        }
    }

    browser.navigation().loadUrl(url)
}

@Composable
private fun AddressBar(browser: Browser, url: String) {
    val mutableUrl = remember { mutableStateOf(url) }

    Row(
        modifier = Modifier.height(40.dp)
    ) {
        BasicTextField(
            value = mutableUrl.value,
            onValueChange = {
                mutableUrl.value = it
            },
            modifier = Modifier.weight(1f).padding(start = 10.dp, top = 10.dp),
            singleLine = true
        )
        Button(
            modifier = Modifier.padding(3.dp),
            onClick = { browser.navigation().loadUrl(mutableUrl.value) }
        ) {
            Text(text = "Go")
        }
    }
}
