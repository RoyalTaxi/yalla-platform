package uz.yalla.platform.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import uz.yalla.platform.LocalCircleIconButtonFactory
import uz.yalla.platform.LocalSheetPresenterFactory
import uz.yalla.platform.LocalSquircleIconButtonFactory
import uz.yalla.platform.LocalThemeProvider

@Suppress("UNUSED_PARAMETER")
@Composable
actual fun NativeSheet(
    isVisible: Boolean,
    shape: Shape,
    containerColor: Color,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val parentController = remember(isVisible) { findKeyWindowRootController() } ?: return
    val currentOnDismiss by rememberUpdatedState(onDismissRequest)
    val currentContent by rememberUpdatedState(content)

    val circleButtonFactory = LocalCircleIconButtonFactory.current
    val squircleButtonFactory = LocalSquircleIconButtonFactory.current
    val sheetFactory = LocalSheetPresenterFactory.current
    val themeProvider = LocalThemeProvider.current

    DisposableEffect(isVisible, parentController) {
        if (!isVisible) return@DisposableEffect onDispose {}

        val presenter = SheetPresenter(
            parent = parentController,
            factory = sheetFactory,
            onDismissedByUser = { currentOnDismiss() }
        )

        presenter.present(
            circleButtonFactory = circleButtonFactory,
            squircleButtonFactory = squircleButtonFactory,
            themeProvider = themeProvider,
            content = { currentContent() }
        )

        onDispose { presenter.dismiss(animated = true) }
    }
}

@Suppress("UNCHECKED_CAST")
private fun findKeyWindowRootController(): UIViewController? {
    val scenes = UIApplication.sharedApplication.connectedScenes as? Set<*>

    val activeScene = scenes?.firstOrNull { scene ->
        (scene as? UIWindowScene)?.activationState == UISceneActivationStateForegroundActive
    } as? UIWindowScene

    val keyWindow = activeScene?.windows?.firstOrNull { window ->
        (window as? UIWindow)?.isKeyWindow() == true
    } as? UIWindow ?: UIApplication.sharedApplication.keyWindow

    return keyWindow?.rootViewController?.topValidController()
}

private fun UIViewController.topValidController(): UIViewController {
    var current = this
    while (current.presentedViewController != null) {
        val presented = current.presentedViewController!!
        if (presented.isBeingDismissed() || presented.view.window == null) { break }
        current = presented
    }
    return current
}
