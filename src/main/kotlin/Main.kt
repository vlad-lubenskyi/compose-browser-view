// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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

    val startUrl = "https://www.google.com"
    val mutableUrl = remember { mutableStateOf(startUrl) }

    Window(onCloseRequest = ::exitApplication, title = "JxBrowser in Compose Desktop") {
        Column {
            AddressBar(browser, mutableUrl)
            BrowserView(browser)
        }
    }

    browser.navigation().loadUrl(startUrl)
}

@Composable
private fun AddressBar(browser: Browser, url: MutableState<String>) {
    Row(
        modifier = Modifier.height(40.dp)
    ) {
        BasicTextField(
            value = url.value,
            onValueChange = {
                url.value = it
            },
            modifier = Modifier.weight(1f).padding(start = 10.dp, top = 10.dp),
            singleLine = true
        )
        Button(
            modifier = Modifier.padding(3.dp),
            onClick = { browser.navigation().loadUrl(url.value) }
        ) {
            Text(text = "Go")
        }
    }
}
