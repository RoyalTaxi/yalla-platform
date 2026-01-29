package uz.yalla.platform.sheet

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NativeSheet(
    isVisible: Boolean,
    shape: Shape,
    containerColor: Color,
    onDismissRequest: () -> Unit,
    dismissEnabled: Boolean,
    onDismissAttempt: () -> Unit,
    isDark: Boolean?,
    content: @Composable () -> Unit
) {
    val darkMode = isDark ?: isSystemInDarkTheme()
    val properties = ModalBottomSheetProperties(
        isAppearanceLightStatusBars = !darkMode,
        isAppearanceLightNavigationBars = !darkMode,
    )

    var shouldShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { value ->
            val isHiding = value == SheetValue.Hidden
            if (!dismissEnabled && isVisible && isHiding) {
                onDismissAttempt()
                false
            } else {
                true
            }
        }
    )

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
            properties = properties,
            onDismissRequest = {
                if (dismissEnabled) {
                    shouldShow = false
                    onDismissRequest()
                } else {
                    onDismissAttempt()
                }
            },
            content = { content() }
        )
    }
}
