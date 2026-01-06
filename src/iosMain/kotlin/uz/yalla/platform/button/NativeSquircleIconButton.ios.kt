package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.compose.resources.painterResource
import platform.Foundation.NSSelectorFromString
import uz.yalla.design.theme.System
import uz.yalla.platform.LocalSquircleIconButtonFactory
import uz.yalla.platform.model.IconType
import uz.yalla.platform.toAssetName
import uz.yalla.platform.toDrawableResource

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeSquircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier,
    border: BorderStroke?
) {
    val factory = LocalSquircleIconButtonFactory.current

    if (factory != null) {
        val borderWidth = border?.width?.value?.toDouble() ?: 0.0
        val borderColor = (border?.brush as? SolidColor)?.value?.toArgb()?.toLong() ?: 0L
        val iconName = iconType.toAssetName()

        UIKitViewController(
            factory = { factory(iconName, onClick, borderWidth, borderColor) },
            update = { viewController ->
                val selector = NSSelectorFromString("updateIcon:")
                if (viewController.respondsToSelector(selector)) {
                    viewController.performSelector(selector, withObject = iconName)
                }
            },
            modifier = modifier.size(48.dp)
        )
    } else {
        // Fallback to Compose implementation
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(8.dp),
            border = border,
            colors = ButtonDefaults.buttonColors(
                containerColor = System.color.backgroundSecondary,
                contentColor = System.color.iconBase
            ),
            modifier = modifier
                .size(48.dp)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(iconType.toDrawableResource()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(System.color.iconBase)
            )
        }
    }
}
