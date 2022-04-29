# BrowserView for Compose Desktop

A Composable UI component that displays content loaded
in the Chromium-based browser.

Uses [JxBrowser](https://www.teamdev.com/jxbrowser)
to integrate a web browser and communicate with it.

## Setup & Run

## Usage

To display component in Composable context use `BrowserView.composable()`
function.

```kotlin
@Composable
fun App(browser: Browser) {
    BrowserView(browser).composable()
    browser.navigation().loadUrl("https://google.com")
}
```

Create [`Engine`](https://jxbrowser-support.teamdev.com/javadoc/7.24.4/com/teamdev/jxbrowser/engine/Engine.html)
and [`Browser`](https://jxbrowser-support.teamdev.com/javadoc/7.24.4/com/teamdev/jxbrowser/browser/Browser.html) instances.

```kotlin
fun main() = application {
    val engine = Engine.newInstance(RenderingMode.OFF_SCREEN)
    val browser = engine.newBrowser()
    Window(onCloseRequest = ::exitApplication) {
        App(browser)
    }
}
```