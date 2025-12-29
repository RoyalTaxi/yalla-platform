package uz.yalla.platform

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.ui.graphics.vector.ImageVector
import uz.yalla.platform.model.IconType

internal fun IconType.toImageVector(): ImageVector = when (this) {
    IconType.MENU -> Icons.Default.Menu
    IconType.LOCATION -> Icons.Default.MyLocation
    IconType.CLOSE -> Icons.Default.Close
    IconType.DONE -> Icons.Default.Done
    IconType.Back -> Icons.AutoMirrored.Default.ArrowBack
}
