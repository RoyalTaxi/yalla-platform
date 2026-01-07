package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import uz.yalla.platform.LocalSquircleIconButtonFactory
import uz.yalla.platform.model.IconType
import uz.yalla.platform.toAssetName

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeSquircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier,
    border: BorderStroke?
) {
    val factory = LocalSquircleIconButtonFactory.current ?: return
    val borderWidth = border?.width?.value?.toDouble() ?: 0.0
    val borderColor = (border?.brush as? SolidColor)?.value?.toArgb()?.toLong() ?: 0L
    val iconName = iconType.toAssetName()

    UIKitViewController(
        factory = { factory(iconName, onClick, borderWidth, borderColor) },
        modifier = modifier.size(48.dp)
    )
}
