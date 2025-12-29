package uz.yalla.platform.navigation

expect class PlatformNavigator<S : Screen>(
    controller: PlatformNavController,
    registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)?,
    onBack: (() -> Unit)?
) : Navigator<S> {
    override fun navigate(screen: S)
    override fun back()
}
