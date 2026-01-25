package uz.yalla.platform.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NativeSheet(
    isVisible: Boolean,
    shape: Shape,
    containerColor: Color,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    var shouldShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(true)

    LaunchedEffect(isVisible) {
        if (isVisible) {
            shouldShow = true
        } else if (shouldShow) {
            sheetState.hide()
            shouldShow = false
        }
    }

    if (shouldShow) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = shape,
            containerColor = containerColor,
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            onDismissRequest = {
                shouldShow = false
                onDismissRequest()
            }
        ) {
            Box(Modifier.systemBarsPadding()) {
                content()
            }
        }
    }
}
