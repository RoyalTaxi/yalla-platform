package uz.yalla.platform

import uz.yalla.platform.model.IconType

internal fun IconType.toSFSymbol(): String = when (this) {
    IconType.MENU -> "line.3.horizontal"
    IconType.LOCATION -> "location.fill"
    IconType.CLOSE -> "xmark"
    IconType.DONE -> "checkmark"
    IconType.Back -> "chevron.backward"
}
