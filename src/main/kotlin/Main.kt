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
