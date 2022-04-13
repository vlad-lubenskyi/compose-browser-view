// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.teamdev.jxbrowser.browser.Browser
import com.teamdev.jxbrowser.compose.BrowserView
import com.teamdev.jxbrowser.engine.Engine
import com.teamdev.jxbrowser.engine.EngineOptions
import com.teamdev.jxbrowser.engine.RenderingMode.OFF_SCREEN
import com.teamdev.jxbrowser.logging.Level
import com.teamdev.jxbrowser.logging.Logger
import java.nio.file.Paths

@Composable
@Preview
fun App(browser: Browser) {
    Logger.level(Level.DEBUG)
    BrowserView(browser).composable()
    browser.navigation().loadUrl("https://google.com")
}

fun main() = application {
    val engine = Engine.newInstance(
        EngineOptions.newBuilder(OFF_SCREEN)
            .chromiumDir(Paths.get("/Users/vladyslav.lubenskyi/dev/BrowserCore/chromium/src/out/Release"))
            .build()
    )
    Window(onCloseRequest = ::exitApplication) {
        App(engine.newBrowser())
    }
}
