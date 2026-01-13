package uz.yalla.platform.indicator

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun NativeLoadingIndicator(
    modifier: Modifier,
    color: Color
) {
    if (color != Color.Unspecified) {
        CircularProgressIndicator(
            modifier = modifier,
            color = color
        )
    } else {
        CircularProgressIndicator(modifier = modifier)
    }
}
