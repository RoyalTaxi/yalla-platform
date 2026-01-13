package uz.yalla.platform.indicator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.infiniteretry.snizzors.SnizzorsUIView
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleMedium
import platform.UIKit.UIColor

@Composable
actual fun NativeLoadingIndicator(
    modifier: Modifier,
    color: Color
) {
    SnizzorsUIView(
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
