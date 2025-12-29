package uz.yalla.platform.navigation

/**
 * Core navigation interface for screen-based navigation.
 *
 * Provides both basic navigation operations (navigate, back) and advanced
 * operations (setRoot, popTo, replaceFrom) commonly needed in mobile apps.
 */
interface Navigator<S : Screen> {
    /**
     * Navigates to the specified screen, pushing it onto the navigation stack.
     */
    fun navigate(screen: S)

    /**
     * Navigates back to the previous screen, popping the current screen from the stack.
     */
    fun back()

    /**
     * Clears the navigation stack and sets the given screen as the new root.
     * Used after successful login/logout to reset the navigation state.
     */
    fun setRoot(screen: S)

    /**
     * Pops the backstack to the specified screen.
     * @param screen The target screen to pop to
     * @param inclusive If true, also removes the target screen from the stack
     */
    fun popTo(screen: S, inclusive: Boolean = false)

    /**
     * Replaces all screens from the specified screen onwards with a new screen.
     * Useful for flows where you want to navigate to a new screen while removing
     * intermediate screens from the backstack.
     */
    fun replaceFrom(fromScreen: S, toScreen: S)
}
