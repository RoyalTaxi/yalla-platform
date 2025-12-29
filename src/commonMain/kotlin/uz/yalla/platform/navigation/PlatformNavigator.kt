package uz.yalla.platform.navigation

expect class PlatformNavigator<S : Screen>(
    controller: PlatformNavController,
    registry: ScreenRegistry<S>,
    onNavigate: ((S) -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onSetRoot: ((S) -> Unit)? = null,
    onPopTo: ((S, Boolean) -> Unit)? = null,
    onReplaceFrom: ((S, S) -> Unit)? = null
) : Navigator<S> {
    override fun navigate(screen: S)
    override fun back()
    override fun setRoot(screen: S)
    override fun popTo(screen: S, inclusive: Boolean)
    override fun replaceFrom(fromScreen: S, toScreen: S)
}
