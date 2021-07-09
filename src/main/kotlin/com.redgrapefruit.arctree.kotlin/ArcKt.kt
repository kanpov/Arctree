package com.redgrapefruit.arctree.kotlin

import net.minecraft.world.gen.feature.ConfiguredFeature

/**
 * Utilities for the Kotlin extensions of this library.
 *
 * Needed to separate the Kotlin part with the Java.
 *
 * **Do NOT use any of the methods provided in this object in your Java code despite the interoperability.**
 */
object ArcKt {
    /**
     * A [DslMarker] for the tree DSL
     */
    @DslMarker
    annotation class TreeDslMarker

    /**
     * Initializes the tree DSL.
     *
     * Example:
     * ```kotlin
     * tree {
     *     // Configure your tree here using the TreeScope methods
     * }
     * ```
     */
    @TreeDslMarker
    fun tree(init: TreeScope.() -> Unit): ConfiguredFeature<*, *> {
        val scope = TreeScope()
        scope.init()
        return scope.build()
    }
}
