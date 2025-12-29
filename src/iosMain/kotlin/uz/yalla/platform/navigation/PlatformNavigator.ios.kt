package uz.yalla.platform.navigation

/**
 * iOS platform navigator that delegates to provided handlers.
 *
 * Note: For most iOS use cases, prefer using [IosNavigationHost] which provides
 * a complete navigation solution with UINavigationController management.
 * This class is primarily for advanced customization needs.
 */
@Suppress("UNUSED_PARAMETER")
actual class PlatformNavigator<S : Screen> actual constructor(
    controller: PlatformNavController,
    registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)?,
    onBack: (() -> Unit)?,
    onSetRoot: ((S) -> Unit)?,
    onPopTo: ((S, Boolean) -> Unit)?,
    onReplaceFrom: ((S, S) -> Unit)?
) : Navigator<S> {
    private val navigateHandler = onNavigate ?: error("onNavigate required on iOS")
    private val backHandler = onBack ?: error("onBack required on iOS")
    private val setRootHandler = onSetRoot ?: error("onSetRoot required on iOS")
    private val popToHandler = onPopTo ?: error("onPopTo required on iOS")
    private val replaceFromHandler = onReplaceFrom ?: error("onReplaceFrom required on iOS")

    actual override fun navigate(screen: S) = navigateHandler(screen)
    actual override fun back() = backHandler()
    actual override fun setRoot(screen: S) = setRootHandler(screen)
    actual override fun popTo(screen: S, inclusive: Boolean) = popToHandler(screen, inclusive)
    actual override fun replaceFrom(fromScreen: S, toScreen: S) = replaceFromHandler(fromScreen, toScreen)
}
