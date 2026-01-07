package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIButton
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIImage
import uz.yalla.platform.LocalCircleIconButtonFactory
import uz.yalla.platform.model.IconType
import uz.yalla.platform.toAssetName

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeCircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier,
    alpha: Float,
    border: BorderStroke?
) {
    val factory = LocalCircleIconButtonFactory.current ?: return
    val borderWidth = border?.width?.value?.toDouble() ?: 0.0
    val borderColor = (border?.brush as? SolidColor)?.value?.toArgb()?.toLong() ?: 0L
    val iconName = iconType.toAssetName()

    UIKitViewController(
        factory = { factory(iconName, onClick, borderWidth, borderColor) },
        update = { controller ->
            controller.view.alpha = alpha.toDouble()
            val button = controller.view as? UIButton
            val image = UIImage.imageNamed(iconName)
            button?.setImage(image, forState = UIControlStateNormal)
        },
        modifier = modifier.size(48.dp)
    )
}
