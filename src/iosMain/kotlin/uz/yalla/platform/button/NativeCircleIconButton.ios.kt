package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.compose.resources.painterResource
import platform.Foundation.NSSelectorFromString
import uz.yalla.design.theme.System
import uz.yalla.platform.LocalCircleIconButtonFactory
import uz.yalla.platform.model.IconType
import uz.yalla.platform.toAssetName
import uz.yalla.platform.toDrawableResource

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeCircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier,
    alpha: Float,
    border: BorderStroke?
) {
    val factory = LocalCircleIconButtonFactory.current

    if (factory != null) {
        val borderWidth = border?.width?.value?.toDouble() ?: 0.0
        val borderColor = (border?.brush as? SolidColor)?.value?.toArgb()?.toLong() ?: 0L
        val iconName = iconType.toAssetName()

        UIKitViewController(
            factory = { factory(iconName, onClick, borderWidth, borderColor) },
            update = { viewController ->
                viewController.view.alpha = alpha.toDouble()
                val selector = NSSelectorFromString("updateIcon:")
                if (viewController.respondsToSelector(selector)) {
                    viewController.performSelector(selector, withObject = iconName)
                }
            },
            modifier = modifier.size(48.dp)
        )
    } else {
        // Fallback to Compose implementation
        IconButton(
            onClick = onClick,
            modifier = modifier
                .size(48.dp)
                .graphicsLayer { this.alpha = alpha }
                .then(if (border != null) Modifier.border(border, CircleShape) else Modifier),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = System.color.iconBase,
                containerColor = System.color.backgroundBase
            )
        ) {
            Image(
                painter = painterResource(iconType.toDrawableResource()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(System.color.iconBase)
            )
        }
    }
}
