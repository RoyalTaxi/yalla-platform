package uz.yalla.platform.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uz.yalla.platform.model.IconType

@Composable
expect fun NativeCircleIconButton(
    iconType: IconType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    alpha: Float = 1f,
    border: BorderStroke? = null
)