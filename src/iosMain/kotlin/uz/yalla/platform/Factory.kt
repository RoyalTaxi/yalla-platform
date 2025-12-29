package uz.yalla.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import platform.UIKit.UIViewController

val LocalCircleIconButtonFactory =
    staticCompositionLocalOf<((icon: String, onClick: () -> Unit, borderWidth: Double, borderColor: Long) -> UIViewController)?> { null }

val LocalSquircleIconButtonFactory =
    staticCompositionLocalOf<((icon: String, onClick: () -> Unit, borderWidth: Double, borderColor: Long) -> UIViewController)?> { null }

val LocalSheetPresenterFactory =
    staticCompositionLocalOf<SheetPresenterFactory?> { null }

val LocalThemeProvider =
    staticCompositionLocalOf<(@Composable (@Composable () -> Unit) -> Unit)?> { null }

data class SheetPresenterFactory(
    val present: (
        parent: UIViewController,
        controller: UIViewController,
        cornerRadius: Double,
        onDismiss: () -> Unit
    ) -> Unit,
    val updateHeight: (controller: UIViewController, height: Double) -> Unit,
    val dismiss: (controller: UIViewController, animated: Boolean) -> Unit
)

class NativeSheetPresenterFactory(
    private val present: (UIViewController, UIViewController, Double, () -> Unit) -> Unit,
    private val updateHeight: (UIViewController, Double) -> Unit,
    private val dismiss: (UIViewController, Boolean) -> Unit
) {
    fun toSheetPresenterFactory() = SheetPresenterFactory(
        present = present,
        updateHeight = updateHeight,
        dismiss = dismiss
    )
}
