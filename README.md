# BrowserView for Compose Desktop

An experimental [JxBrowser](https://www.teamdev.com/jxbrowser) component for Compose Desktop.

![app-screenshot](/img/app-screenshot.png?raw=true "JxBrowser in Compose Desktop")

## JxBrowser License

To run the example, you will need a valid JxBrowser license.
For more information please see the [Licensing](https://jxbrowser-support.teamdev.com/docs/guides/introduction/licensing.html#licensing) guide.

## Usage

Use `BrowserView(browser)` function to add a component:

```kotlin
fun main() = application {

    // Start the Chromium process.
    val engine = Engine.newInstance(OFF_SCREEN)

    // Create a browser.
    val browser = engine.newBrowser()

    Window(onCloseRequest = ::exitApplication) {
        BrowserView(browser)
    }
    browser.navigation().loadUrl("https://google.com")
}
```
---

The information in this repository is provided on the following terms: https://www.teamdev.com/terms-and-privacy
