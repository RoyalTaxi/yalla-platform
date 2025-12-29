@file:OptIn(ExperimentalComposeUiApi::class)

package uz.yalla.platform.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIModalPresentationPageSheet
import platform.UIKit.UIViewController
import uz.yalla.platform.LocalCircleIconButtonFactory
import uz.yalla.platform.LocalSquircleIconButtonFactory
import uz.yalla.platform.SheetPresenterFactory
import kotlin.math.abs

internal typealias CircleButtonFactory = (String, () -> Unit, Double, Long) -> UIViewController
internal typealias SquircleButtonFactory = (String, () -> Unit, Double, Long) -> UIViewController
internal typealias ThemeProvider = @Composable (@Composable () -> Unit) -> Unit

internal class SheetPresenter(
    private val parent: UIViewController?,
    private val factory: SheetPresenterFactory?,
    private val onDismissedByUser: () -> Unit
) {
    private var controller: UIViewController? = null
    private var isProgrammaticDismiss = false
    private var lastMeasuredHeight = 0.0

    fun present(
        circleButtonFactory: CircleButtonFactory?,
        squircleButtonFactory: SquircleButtonFactory?,
        themeProvider: ThemeProvider?,
        content: @Composable () -> Unit
    ) {
        controller?.let { dismiss(animated = false) }
        val parentController = parent?.topPresentedController() ?: return

        val host = createComposeController(
            circleButtonFactory = circleButtonFactory,
            squircleButtonFactory = squircleButtonFactory,
            themeProvider = themeProvider,
            content = content
        )

        controller = host

        if (factory != null) {
            factory.present(parentController, host, CORNER_RADIUS) {
                handleDismissCallback()
            }
        } else {
            parentController.presentViewController(host, animated = true, completion = null)
        }
    }

    fun dismiss(animated: Boolean = true) {
        val ctrl = controller ?: return
        isProgrammaticDismiss = true
        controller = null

        if (factory != null) {
            factory.dismiss(ctrl, animated)
        } else {
            ctrl.dismissViewControllerAnimated(animated, null)
        }

        isProgrammaticDismiss = false
    }

    private fun createComposeController(
        circleButtonFactory: CircleButtonFactory?,
        squircleButtonFactory: SquircleButtonFactory?,
        themeProvider: ThemeProvider?,
        content: @Composable () -> Unit
    ): UIViewController = ComposeUIViewController(
        configure = { opaque = false }
    ) {
        val themedContent: @Composable () -> Unit = {
            CompositionLocalProvider(
                LocalCircleIconButtonFactory provides circleButtonFactory,
                LocalSquircleIconButtonFactory provides squircleButtonFactory
            ) {
                MeasuredContent(content)
            }
        }

        themeProvider?.invoke(themedContent) ?: themedContent()
    }.apply {
        modalPresentationStyle = UIModalPresentationPageSheet
    }

    @Composable
    private fun MeasuredContent(content: @Composable () -> Unit) {
        val density = LocalDensity.current

        Box(
            modifier = Modifier.onSizeChanged { size ->
                val heightPt = size.height / density.density.toDouble()
                if (abs(lastMeasuredHeight - heightPt) > HEIGHT_CHANGE_THRESHOLD) {
                    lastMeasuredHeight = heightPt
                    controller?.let { factory?.updateHeight(it, heightPt) }
                }
            }
        ) {
            content()
        }
    }

    private fun handleDismissCallback() {
        if (!isProgrammaticDismiss) {
            controller = null
            onDismissedByUser()
        }
    }

    private companion object {
        const val CORNER_RADIUS = 24.0
        const val HEIGHT_CHANGE_THRESHOLD = 20.0
    }
}

private fun UIViewController.topPresentedController(): UIViewController {
    var current = this
    while (current.presentedViewController != null) {
        current = current.presentedViewController!!
    }
    return current
}
