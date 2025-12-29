package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import uz.yalla.platform.LocalSquircleIconButtonFactory
import uz.yalla.platform.model.IconType
import uz.yalla.platform.toSFSymbol

@Composable
actual fun NativeSquircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier,
    border: BorderStroke?
) {
    val factory = LocalSquircleIconButtonFactory.current ?: return

    val borderWidth = border?.width?.value?.toDouble() ?: 0.0
    val borderColor = border?.brush?.let { brush ->
        (brush as? SolidColor)?.value?.value?.toLong() ?: 0L
    } ?: 0L

    UIKitViewController(
        factory = { factory(iconType.toSFSymbol(), onClick, borderWidth, borderColor) },
        modifier = modifier.size(48.dp),
        update = { viewController ->
            viewController.view.backgroundColor = platform.UIKit.UIColor.clearColor
            viewController.view.setOpaque(false)
        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}
