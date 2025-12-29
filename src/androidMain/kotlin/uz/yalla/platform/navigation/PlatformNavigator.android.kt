package uz.yalla.platform.navigation

@Suppress("UNUSED_PARAMETER")
actual class PlatformNavigator<S : Screen> actual constructor(
    private val controller: PlatformNavController,
    private val registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)?,
    onBack: (() -> Unit)?
) : Navigator<S> {
    actual override fun navigate(screen: S) {
        controller.navigate(registry.routeFor(screen))
    }

    actual override fun back() {
        controller.popBackStack()
    }
}
