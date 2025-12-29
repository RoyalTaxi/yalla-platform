package uz.yalla.platform.navigation

@Suppress("UNUSED_PARAMETER")
actual class PlatformNavigator<S : Screen> actual constructor(
    controller: PlatformNavController,
    registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)?,
    onBack: (() -> Unit)?
) : Navigator<S> {
    private val navigateHandler = onNavigate ?: error("onNavigate required on iOS")
    private val backHandler = onBack ?: error("onBack required on iOS")

    actual override fun navigate(screen: S) = navigateHandler(screen)
    actual override fun back() = backHandler()
}
