package uz.yalla.platform.navigation

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

/**
 * UINavigationController subclass with interactive pop gesture (swipe-to-back) support.
 * Automatically enables the edge swipe gesture when there's more than one view controller in the stack.
 */
@OptIn(ExperimentalForeignApi::class)
class InteractiveNavigationController : UINavigationController(nibName = null, bundle = null),
    UIGestureRecognizerDelegateProtocol {

    override fun viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.apply {
            delegate = this@InteractiveNavigationController
            setEnabled(true)
        }
    }

    override fun gestureRecognizerShouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
        return viewControllers.size > 1
    }
}

/**
 * Result of creating an iOS navigation host.
 * Contains the navigation controller and utilities for advanced operations.
 */
class IosNavigationHostResult<S : Screen>(
    /** The UINavigationController to use as root view controller */
    val navigationController: UINavigationController,
    /** Rebuilds all screens in the backstack except the current one. Useful for locale changes. */
    val rebuildBackstack: () -> Unit,
    /** Returns a copy of the current screen stack */
    val getScreenStack: () -> List<S>
)

/**
 * Creates an iOS navigation host that manages screen navigation using UINavigationController.
 *
 * This function encapsulates all the iOS-specific navigation logic:
 * - InteractiveNavigationController with swipe-to-back gesture
 * - Screen stack management synchronized with UINavigationController
 * - Navigator implementation with setRoot, popTo, replaceFrom support
 * - Automatic stack sync on interactive pop gestures
 *
 * @param registry The screen registry with all screen definitions
 * @param startScreen The initial screen to display
 * @param viewControllerFactory Factory function to create view controllers for screens
 * @return IosNavigationHostResult containing the navigation controller and utilities
 *
 * Example usage:
 * ```kotlin
 * fun AppRootController(): UIViewController {
 *     val registry = createAppScreenRegistry(...)
 *     val result = IosNavigationHost(
 *         registry = registry,
 *         startScreen = AppScreen.Home,
 *         viewControllerFactory = { screen, navigator ->
 *             baseViewController { registry.entryForScreen(screen).content.Content(screen, navigator) }
 *         }
 *     )
 *
 *     // Handle locale changes
 *     scope.launch {
 *         localeFlow.collect { result.rebuildBackstack() }
 *     }
 *
 *     return result.navigationController
 * }
 * ```
 */
@OptIn(ExperimentalForeignApi::class)
fun <S : Screen> IosNavigationHost(
    registry: ScreenRegistry<S>,
    startScreen: S,
    viewControllerFactory: (screen: S, navigator: Navigator<S>) -> UIViewController
): IosNavigationHostResult<S> {
    val nav = InteractiveNavigationController().apply {
        setNavigationBarHidden(true, animated = false)
    }

    val screenStack = mutableListOf<S>()

    // Forward declaration for makeViewController
    lateinit var makeViewController: (S) -> UIViewController

    // Create navigator with full iOS-specific operations
    val navigator = object : Navigator<S> {
        override fun navigate(screen: S) {
            screenStack.add(screen)
            nav.pushViewController(makeViewController(screen), animated = true)
        }

        override fun back() {
            if (screenStack.size > 1) {
                screenStack.removeLast()
            }
            nav.popViewControllerAnimated(true)
        }

        override fun setRoot(screen: S) {
            screenStack.clear()
            screenStack.add(screen)
            nav.setViewControllers(listOf(makeViewController(screen)), animated = false)
        }

        override fun popTo(screen: S, inclusive: Boolean) {
            val index = screenStack.indexOfLast { it == screen }
            if (index != -1) {
                val targetIndex = if (inclusive) index else index + 1
                if (targetIndex < screenStack.size) {
                    screenStack.subList(targetIndex, screenStack.size).clear()
                }
                val viewControllers = nav.viewControllers.take(targetIndex + 1)
                nav.setViewControllers(viewControllers, animated = true)
            }
        }

        override fun replaceFrom(fromScreen: S, toScreen: S) {
            val fromIndex = screenStack.indexOfLast { it == fromScreen }
            if (fromIndex != -1) {
                screenStack.subList(fromIndex, screenStack.size).clear()
            }
            navigate(toScreen)
        }
    }

    // Create view controller factory
    makeViewController = { screen ->
        viewControllerFactory(screen, navigator)
    }

    // Navigation delegate to sync screen stack with UINavigationController on swipe gestures
    val navDelegate = object : NSObject(), UINavigationControllerDelegateProtocol {
        override fun navigationController(
            navigationController: UINavigationController,
            didShowViewController: UIViewController,
            animated: Boolean
        ) {
            val count = navigationController.viewControllers.size
            if (screenStack.size > count) {
                screenStack.subList(count, screenStack.size).clear()
            }
        }
    }

    // Rebuild backstack function for locale changes
    val rebuildBackstack: () -> Unit = {
        if (screenStack.size > 1) {
            val current = nav.viewControllers.lastOrNull()
            val rebuilt = screenStack.dropLast(1).map { makeViewController(it) } +
                    listOfNotNull(current)
            nav.setViewControllers(rebuilt, animated = false)
        }
    }

    // Initialize with start screen
    screenStack.add(startScreen)
    nav.setViewControllers(listOf(makeViewController(startScreen)), animated = false)
    nav.delegate = navDelegate

    return IosNavigationHostResult(
        navigationController = nav,
        rebuildBackstack = rebuildBackstack,
        getScreenStack = { screenStack.toList() }
    )
}
