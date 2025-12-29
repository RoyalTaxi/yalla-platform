package uz.yalla.platform.navigation

interface Navigator<S : Screen> {
    fun navigate(screen: S)
    fun back()
}
