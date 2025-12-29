package uz.yalla.platform.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

interface Screen {
    val route: String
}

data class ScreenEntry<S : Screen>(
    val screenClass: KClass<out S>,
    val route: String,
    val arguments: List<NavArgSpec> = emptyList(),
    val parse: (NavArgs) -> S,
    val content: @Composable (S, Navigator<S>) -> Unit
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
        noinline content: @Composable (T, Navigator<S>) -> Unit
    ) {
        @Suppress("UNCHECKED_CAST")
        _entries += ScreenEntry(
            screenClass = T::class,
            route = route,
            arguments = arguments,
            parse = parse,
            content = content as @Composable (S, Navigator<S>) -> Unit
        )
    }

    inline fun <reified T : S> screen(
        instance: T,
        noinline content: @Composable (T, Navigator<S>) -> Unit
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
