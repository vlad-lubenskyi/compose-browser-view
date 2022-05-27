# BrowserView for Compose Desktop

A Compose component that displays content loaded in the Chromium-based browser.

Uses [JxBrowser](https://www.teamdev.com/jxbrowser) to integrate a web browser and communicate with it.

![app-screenshot](/img/app-screenshot.png?raw=true)

## Usage

Use `BrowserView.composable()` function to add a component:

```kotlin
fun main() = application {

    // Start the Chromium process.
    val engine = Engine.newInstance(OFF_SCREEN)

    // Create a browser to display its content.
    val browser = engine.newBrowser()

    Window(onCloseRequest = ::exitApplication) {
        BrowserView(browser).composable()
    }
    browser.navigation().loadUrl("https://google.com")
}
```
