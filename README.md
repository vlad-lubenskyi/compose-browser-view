# BrowserView for Compose Desktop

A Compose component that displays content loaded
in the Chromium-based browser.

Uses [JxBrowser](https://www.teamdev.com/jxbrowser)
to integrate a web browser and communicate with it.

## Setup & Run

## Usage

Use `BrowserView.composable()` function to add a component:

```kotlin
@Composable
fun App(browser: Browser) {
    BrowserView(browser).composable()
    browser.navigation().loadUrl("https://google.com")
}
```

You need to create [`Engine`](https://jxbrowser-support.teamdev.com/javadoc/7.24.4/com/teamdev/jxbrowser/engine/Engine.html)
and [`Browser`](https://jxbrowser-support.teamdev.com/javadoc/7.24.4/com/teamdev/jxbrowser/browser/Browser.html)
to set up the web browser control.

```kotlin
fun main() = application {
    val engine = Engine.newInstance(RenderingMode.OFF_SCREEN)
    val browser = engine.newBrowser()
    Window(onCloseRequest = ::exitApplication) {
        App(browser)
    }
}
```