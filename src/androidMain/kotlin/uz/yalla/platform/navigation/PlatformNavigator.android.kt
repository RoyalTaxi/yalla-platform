package uz.yalla.platform.navigation

@Suppress("UNUSED_PARAMETER")
actual class PlatformNavigator<S : Screen> actual constructor(
    private val controller: PlatformNavController,
    private val registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)?,
    onBack: (() -> Unit)?,
    onSetRoot: ((S) -> Unit)?,
    onPopTo: ((S, Boolean) -> Unit)?,
    onReplaceFrom: ((S, S) -> Unit)?
) : Navigator<S> {
    actual override fun navigate(screen: S) {
        controller.navigate(registry.routeFor(screen))
    }

    actual override fun back() {
        controller.popBackStack()
    }

    actual override fun setRoot(screen: S) {
        controller.navigate(registry.routeFor(screen)) {
            popUpTo(controller.graph.startDestinationId) { inclusive = true }
        }
    }

    actual override fun popTo(screen: S, inclusive: Boolean) {
        controller.popBackStack(registry.routeFor(screen), inclusive)
    }

    actual override fun replaceFrom(fromScreen: S, toScreen: S) {
        controller.navigate(registry.routeFor(toScreen)) {
            popUpTo(registry.routeFor(fromScreen)) { inclusive = true }
        }
    }
}
