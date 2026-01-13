package uz.yalla.platform.indicator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleMedium
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeLoadingIndicator(
    modifier: Modifier,
    color: Color
) {
    UIKitView(
        modifier = modifier,
        factory = {
            UIActivityIndicatorView(activityIndicatorStyle = UIActivityIndicatorViewStyleMedium).apply {
                if (color != Color.Unspecified) {
                    this.color = UIColor(
                        red = color.red.toDouble(),
                        green = color.green.toDouble(),
                        blue = color.blue.toDouble(),
                        alpha = color.alpha.toDouble()
                    )
                }
                backgroundColor = UIColor.clearColor
                setOpaque(false)
                startAnimating()
            }
        }
    )
}
