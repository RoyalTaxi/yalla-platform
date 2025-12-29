package uz.yalla.platform.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

interface Screen {
    val route: String
}

/**
 * Functional interface for screen content to avoid composable lambda casting issues.
 */
fun interface ScreenContent<S : Screen> {
    @Composable
    fun Content(screen: S, navigator: Navigator<S>)
}

data class ScreenEntry<S : Screen>(
    val screenClass: KClass<out S>,
    val route: String,
    val arguments: List<NavArgSpec> = emptyList(),
    val parse: (NavArgs) -> S,
    val content: ScreenContent<S>
)

interface ScreenRegistry<S : Screen> {
    val start: S
    val entries: List<ScreenEntry<S>>
}

class ScreenRegistryBuilder<S : Screen> @PublishedApi internal constructor() {
    @PublishedApi
    internal val _entries = mutableListOf<ScreenEntry<S>>()
    val entries: List<ScreenEntry<S>> get() = _entries.toList()

    inline fun <reified T : S> screen(
        route: String,
        arguments: List<NavArgSpec> = emptyList(),
        noinline parse: (NavArgs) -> T,
        crossinline content: @Composable (T, Navigator<S>) -> Unit
    ) {
        _entries += ScreenEntry(
            screenClass = T::class,
            route = route,
            arguments = arguments,
            parse = { args ->
                @Suppress("UNCHECKED_CAST")
                parse(args) as S
            },
            content = ScreenContent { screen, navigator ->
                @Suppress("UNCHECKED_CAST")
                content(screen as T, navigator)
            }
        )
    }

    inline fun <reified T : S> screen(
        instance: T,
        crossinline content: @Composable (T, Navigator<S>) -> Unit
    ) {
        screen(
            route = instance.route,
            arguments = emptyList(),
            parse = { instance },
            content = content
        )
    }
}

inline fun <S : Screen> screenRegistry(
    start: S,
    builder: ScreenRegistryBuilder<S>.() -> Unit
): ScreenRegistry<S> {
    val registryBuilder = ScreenRegistryBuilder<S>().apply(builder)
    return object : ScreenRegistry<S> {
        override val start: S = start
        override val entries: List<ScreenEntry<S>> = registryBuilder.entries
    }
}

fun <S : Screen> ScreenRegistry<S>.entryForScreen(screen: S): ScreenEntry<S> {
    return entries.first { it.screenClass.isInstance(screen) }
}

fun <S : Screen> ScreenRegistry<S>.entryForRoute(route: String): ScreenEntry<S>? {
    return entries.firstOrNull { it.route == route.substringBefore('/') }
}

fun <S : Screen> ScreenRegistry<S>.routeFor(screen: S): String = screen.route
