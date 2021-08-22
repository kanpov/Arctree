
# Arctree

Arctree is an easy-to-use, lightweight library for FabricMC modding.

It gives you all the tools to create trees in Minecraft, as complex as you wish.

## Install

The library is available on my [custom Maven](https://github.com/RedGrapefruit09/Maven) for anyone to include.

Put this into your `gradle.properties`, at the end of the file:
```properties
arctree_version=1.0.2
```

Put this into your `build.gradle`:
```groovy
repositories {
   maven {
     name = "RedGrapefruit09"
     url = "https://raw.githubusercontent.com/RedGrapefruit09/Maven/master"
     content {
         includeGroup("com.redgrapefruit09.arctree")
     }
  }
}

dependencies {
   modImplementation("com.redgrapefruit09.arctree:arctree:${project.arctree_version}")
   // This bundles the library inside of your mod so it doesn't need to be installed separately
   include("com.redgrapefruit09.arctree:arctree:${project.arctree_version}")
}
```

If these blocks are already there, just copy-paste the code inside them.

## Main guide

### Registering tree types

Some things in tree generation have registry wrappers, or simply `Type`s.

These `Type`s are necessary to register these things, but, unfortunately, most of them aren't
directly exposed by the Minecraft's code.

Arctree fixes this issue by using mixins under the hood and providing you with a class called `ArctreeTypes`.

It's very small and provides static methods that create, register and return your `Type`.

1. `TrunkPlacerType`s:
    ```java
   public static final TrunkPlacerType<MyTrunkPlacer> MY_TRUNK_PLACER_TYPE = ArctreeTypes.trunkPlacerType(
        new Identifier("my_mod", "my_trunk_placer"),
        MyTrunkPlacer.CODEC);
   ```

2. `FoliagePlacerType`s:
   ```java
   public static final FoliagePlacerType<MyFoliagePlacer> MY_FOLIAGE_PLACER_TYPE = ArctreeTypes.foliagePlacerType(
        new Identifier("my_mod", "my_foliage_placer"),
        MyFoliagePlacer.CODEC);
   ```
   
3. `TreeDecoratorType`s:
   ```java
   public static final TreeDecoratorType<MyTreeDecorator> MY_TREE_DECORATOR_TYPE = ArctreeTypes.treeDecoratorType(
        new Identifier("my_mod", "my_tree_decorator"),
        MyTreeDecorator.CODEC);
   ```

4. `BlockStateProviderType`s:
   ```java
   public static final BlockStateProviderType<MyBSP> MY_BSP_TYPE = ArctreeTypes.blockStateProviderType(
        new Identifier("my_mod", "my_bsp"),
        MyBSP.CODEC);
   ```

Some `Type`s can be registered directly, but the registry methods for them take\
strings like `my_mod:my_bsp` instead of a standard `Identifier`.

The `ArctreeTypes` class has methods that handle the conversion automatically for you:

5. `IntProviderType`s:
   ```java
   public static final IntProviderType<MyIntProvider> MY_INT_PROVIDER_TYPE = ArctreeTypes.intProviderType(
        new Identifier("my_mod", "my_int_provider"),
        MyIntProvider.CODEC);
   ```

6. `FloatProviderType`s:
   ```java
   public static final FloatProviderType<MyFloatProvider> MY_FLOAT_PROVIDER_TYPE = ArctreeTypes.floatProviderType(
        new Identifier("my_mod", "my_float_provider"),
        MyFloatProvider.CODEC);
   ```

7. `HeightProviderType`s:
   ```java
   public static final HeightProviderType<MyHeightProvider> MY_HEIGHT_PROVIDER_TYPE = ArctreeTypes.heightProvider(
        new Identifier("my_mod", "my_height_provider"),
        MyHeightProvider.CODEC);
   ```

### Making ConfiguredFeatures

As you might know, for trees, instead of creating a new `Feature`, all you have to do is\
reconfigure `Feature.TREE` with settings of your preference.

This process is a bit tedious, and has some quirks to it because of multiple builders used,\
so Arctree provides a unified easy builder for trees - `ArctreeCreator`.

Some parameters are optional, some - mandatory. You can determine that by looking at the\
annotation used - `@Optional` or `@Mandatory`.

Here's how it looks with all parameters filled. (put your values instead of `...`):

```java
public static final ConfiguredFeature<?, ?> MY_TREE = ArctreeCreator.create()
     // The TrunkPlacer for the tree. Mandatory
    .trunkPlacer(...)
     // The FoliagePlacer for the tree. Mandatory
    .foliagePlacer(...)
     // The BlockStateProvider for the tree's trunk. Mandatory
    .trunkProvider(...)
     // The BlockStateProvider for the tree's foliage. Mandatory
    .foliageProvider(...)
     // The BlockStateProvider for the tree's sapling to plant with. Mandatory
    .saplingProvider(...)
     // The minimum FeatureSize of the tree. Optional.
     // Highly not recommended to configure on your own if you don't know what you're doing.
    .minimumSize(...)
     // An ImmutableList of TreeDecorators for this tree. Optional (empty by default).
     // Recommended to create using the ArctreeCreator.decoratorList helper method
    .decorators(ArctreeCreator.decoratorList(...))
     // The BlockStateProvider for the dirt generated underneath the tree. Optional.
    .dirtProvider(...)

     // Optional. Uncomment for the worldgen to ignore vines while generating the tree
     //.ignoreVines()

     // Optional. Uncomment for the worldgen to forcefully place dirt underneath the tree
     //.forceDirt()
    
    // The weirdly made spawn chance for the tree. 3 -> 33%, 5 -> 50% etc. Optional.
    .spawnChance(...)
    
    .build()
```

If you did not pass all mandatory parameters, you would get a `NullPointerException` for each missing one.

Be careful!

### Defaults

Aside from what has already been mentioned, there are two other things required to make a tree:\
a `SaplingBlock` and a `SaplingGenerator`.

`SaplingBlock` has a `protected` constructor, so you cannot directly use it.\
`SaplingGenerator` does not have a default implementation.

To solve this, Arctree introduces `SimpleSaplingBlock` and `SimpleSaplingGenerator`:

1. `SimpleSaplingBlock`:
   ```java
   public static final SaplingBlock MY_SAPLING = new SimpleSaplingBlock(MY_GENERATOR, FabricBlockSettings...)
   ```

2. `SimpleSaplingGenerator`:
   ```java
   public static final SaplingGenerator MY_GENERATOR = new SimpleSaplingGenerator(MY_TREE);
   ```

## Usage with Kotlin

Even though this library mainly focuses on Java devs, there are still lots of Kotlin devs who need\
this to have proper interoperability with Kotlin.

### Default interoperability

Your standard interoperability will work well out-of-the-box.

Everything is annotated with either `@NotNull` or `@Nullable`, so there shouldn't be nullability issues.

### Tree DSL

This is the main feature of the Kotlin part of the library.

Since `ArctreeCreator` is a Java builder, it wouldn't work as well with Kotlin since\
Kotlin has type-safe builders (DSLs).

For that reason, I've created a simple DSL that does the same thing as `ArctreeCreator`.

Here's how it should be used:

```kotlin
val MY_TREE: ConfiguredFeature<*, *> = ArcKt.tree {
    // Replace ... with actual values
    // For more detail on the fields, see the comments on the ArctreeCreator tutorial
    trunkProvider = ...
    foliageProvider = ...
    saplingProvider = ...
    trunkPlacer = ...
    foliagePlacer = ...
    minimumSize = ...
    // The decoratorList method can be safely reused here
    decorators = ArctreeCreator.decoratorList(...)
    dirtProvider = ...
    ignoreVines = ...
    forceDirt = ...
    spawnChance = ...
}
```
