# Changelog of versions

### Version 0.8.0 (Unreleased)
- Added: ISizeProvider interface (Optimization Helper)
- Added: ISizeProvider into most Iterable implementations (Distinct/Filter/FlatMap/ArrayFlatMap don't support it, for obvious reasons)
- Added: ToArray function into Iterable which uses ISizeProvider to reduce overhead of duplicating arrays.
- Added: Functions that have the same type, Int2IntFunction as example, have now a identity function.
- Added: Functions of a BooleanValue have now alwaysTrue/False function.
- Fixed: putIfAbsent now replaces defaultValues
- Fixed: OpenHashSet/Map and their Custom Variants no longer rely on List implementations.
- Fixed: ObjectCopyOnWriteList.of did create a ObjectArrayList instead of the CopyOnWrite variant.
- Removed: BooleanSet and Maps that start with a Boolean classes since they can not be used anyways.
- Breaking Change: Function classes now use the "apply/applyAs/test" format from Java itself, instead of the "get" format. This cleans up a lot of things. But will break existing function class implementations
- Breaking Change: Classes that used PrimitiveCollection functions now default to java functions where applicable, this is to increase compat.

### Version 0.7.0
- Added: Over 11 Million Unit Tests to this library to ensure quality.
- Added: ArrayList size constructor now throws IllegalStateException if the size parameter is negative
- Added: EnumMap specialized forEach implementation.
- Added: AbstractMap.remove now delegates to its primitive counterpart.
- Added: ConcurrentHashMap now implements ITrimmable
- Refactor: Removed a lot of disabled code from ArraySet.
- Removed: LinkedList.addAll(index, List) now delegates to LinkedList.addAll(index, Collection) due to no special optimization required.
- Fixed: AbstractList.SubList.get/set/swapRemove didn't calculate their List index Properly
- Fixed: AbstractList.SubList chains now properly if you create SubLists within SubLists.
- Fixed: AbstractList.Iterator.add now respects Immutable/UnmodifiableLists.
- Fixed: AbstractList.Iterator.skip/back now keep track of the last returned value for remove function to work properly.
- Fixed: CopyOnWriteArrayList.extract/removeElements(int, int) does now proper range checks and remove elements properly.
- Fixed: CopyOnWriteArrayList.SubList now works properly. (Reimplemented entirely)
- Fixed: CopyOnWriteArrayList.Iterator.previous() was returning the wrong values.
- Fixed: CopyOnWriteArrayList.Iterator.skip now skips the right amount of elements and stops where it should.
- Fixed: LinkedList.first/last/dequeue/dequeueLast now throws NoSuchElementException when empty instead of IllegalStateException.
- Fixed: LinkedList had an edge case where the entire reverse iterator would break if the wrong element was removed.
- Fixed: LinkedList.extractElement now returns the correct values.
- Fixed: AbstractMap.entrySet().remove(Object) now returns true if defaultReturnValue elements were removed.
- Fixed: ConcurrentHashMap.remove(Object, Object) checks if the type matches before comparing against null Values.
- Fixed: LinkedHashMap.clearAndTrim() was checking the wrong value for determining the full reset or clearing of a Map.
- Fixed: HashMap.trim/clearToTrim() was using the wrong value to determin if something should be done.
- Fixed: HashMap now compares empty values (0) against nullKeys when Object Variants of the type are used.
- Fixed: ImmutableMap now compares empty values (0) against nullKeys when Object Variants of the type are used.
- Fixed: ArrayMap.iterator(key) now throws NoSuchElementException when the element wasn't found.
- Fixed: Linked/EnumMap array constructor was creating the wrong size values array.
- Fixed: LinkedEnumMap.getAndMoveToFirst/Last was moving elements even if the element wasn't present.
- Fixed: AVL/RBTreeMap.getFirst/LastKey was not throwing a NoSuchElementException if the map was empty.
- Fixed: Map.Builder wasn't throwing a IllegalStateException when creating a negative size builder.
- Fixed: AVL/RBTreeSet.DecendingSet.subSet(from, fromInclusive, to, toInclusive) was creating a corrupt asending subset.
- Fixed: ArraySet throws now a IllegalStateException when trying to create it with a negative size.
- Fixed: ArraySet.addMoveToLast(key) was crashing when a key was already present.
- Fixed: Immutable/LinkedHashSet now keep track of their iteration index properly.
- Fixed: LinkedHashSet.moveToFirst/Last(key) would crash if the Set was empty.
- Fixed: LinkedHashSet.clearAndTrim() was checking the wrong value for determining the full reset or clearing of a Map.
- Fixed: HashSet.trim/clearToTrim() was using the wrong value to determin if something should be done.


### Version 0.6.2
- Added: Array only sorting function return the inputed array. This was done to allow for static final references to use the method in one go without having to make lambda wrappers. Cleaner code.
- Added: Iterator Wrappers are now a bit more in Compliance with Java Standards.
- Added: AsyncBuilders now Support Array Inputs to create cleaner code.
- Changed: LinkedList.addBulk variable definition was triggering a false positive.
- Fixed: TreeMap.subMap().entrySet().remove() wouldn't check primitives properly.
- Fixed: SortedMap.sub/tail/headMap were looping into themselves.
- Fixed: AbstractCollection.retainAll didn't push removed values through the consumer.
- Fixed: AbstractCollection.toArray wouldn't reset the last entry if the input array was larger then the elements in the collection.
- Fixed: SubList didn't check for ranges properly or didn't use parent list to validate changes.
- Fixed: ArrayList.addElements didn't check input array fully and used the wrong variable to move the elements around.
- Fixed: LinkedList.addElements(EmptyInput) would crash.
- Fixed: LinkedList.swapRemove didn't account for if the removed element was the prelast one.
- Fixed: LinkedList.removeElements would break the implementation if the list was almost empty and the middle element was removed.
- Fixed: LinkedHashSet.addAndMoveToFirst wouldn't move elements to the first place.
- Fixed: ArrayList/LinkedList extractElements crashing when 0 or less elements are desired.
- Fixed: TreeMap pollFirst/LastKey should return the defaultMin/max value instead of a Empty value.
- Fixed: TreeMap keySet implementation was missing the class type implementations to pass keySet tests.
- Fixed: TreeMap.SubMap Iterator (primitive Keys) was crashing because a Null was set on to a primitive.


### Version 0.6.1
- Fixed: FIFO queue crashing when the last index is before the first index when peek is called.
- Fixed: FIFO queue only clears the array if it was in use.
- Added: Sorted Method for the stream replacing functions.

### Version 0.6.0
- Added: addOrGet for sets.
- Added: Async API which allows to easily execute Iterables/Collections offthread without the complexity.
- Added: CopyOnWriteArrayList and tests for it
- Added: Support up to Java17.
- Added: Build System now adds module-info if the Running JVM is 9 or higher
- Added: ArrayList.of(Class, size) that allows you to allocate a size right at the creation of the List without having to create a wrapper array.
- Added: A ConcurrentHashMap implementation.
- Fixed: containsValue in the HashMap wouldn't check the nullKey
- Removed: Deprecated functions from SortedMaps/Sets

### Version 0.5.3
- Added: OrderedMap/Set
- Added: Deprecation to Functions that are specific to Ordered interfaces in the SortedMap/Set
- Added: subFrom to Maps which is the counterpart of the addTo method
- Added: pourAsList and pourAsSet (booleans excluded for sets) to Iterable
- Fixed: ArrayList.grow had a small bug where it would trigger to early causing performance problems with exact sized collections.
- Fixed: FIFOQueue size constructor had a small bug where it would trigger a array enlargement when all elements were inserted.

### Version 0.5.2
- Fixed: Bugs with Queues starting with the wrong size
- Fixed: ArrayGrowth for Queues was +1 instead of +50%
- Added: Benchmarks with java and FastUtil

### Version 0.5.1
- Fixed: Reworked the NavigableSet/Map implementations of RBTree/AVLTree/Array Sets/Maps so they are now deemed stable.
- Added: Another 150k Unit tests.
- Added: List and Set Unit tests for Integer (or Primitives in this case) to ensure basic stability there. (Now covering all sets and lists)
- Fixed: Bugs with null values for primitive collections.
- Removed: ArraySet/Map subSet/subMap implementation was removed.

### Version 0.5.0
- Added: 2 Helper functions to find out how many bits are required to store a Number.
- Added: pour function directly into Iterable which allows to collect all elements in the Iterable directly.
- Added: The new ToArray method from Java9 and newer into the library. Using a functional interface. (Just a backport)
- Changed: Reworked how the Map Builder functions are created. They are now in a SubClass that moves them out of the way. Less Clutter. (This might break things if that was used before)
- Added: Map Builder that allows now to Build Maps like Guava ImmutableMaps can be build. Note: This has a slight performance overhead.
- Added: Unmodifiable and Synchronize wrapper functions direclty into Collection Interfaces. This is mostly a quality of life thing.
- Added: Unmodifiable and Synchronized Wrapper Collections can now be cloned. They clone the underlying map which doesn't break functionality. (Had a usecase for it)
- Added: A boxed putAll array variant.
- Fixed: EnumMaps didn't keep track of their size and now got proper care and implementations as needed. There might be more work required but at least the core functionality is now up to date.
- Added: Tests for the new Stream replace functions to ensure no bugs are left.
- Fixed: Custom HashSet reduce function with a default value was checking incorrectly for present keys.
- Added: Guava TestSuit
- Fixed: HashCode and toString method would crash if the Object Key/Value was null
- Added: AbstractTypeCollection now delegates the contains check to type-specific Collections if it detects it.
- Fixed: Map.Entry toString wasn't writing values not like it should do.
- Fixed: Set.hashCode now is the sum of the elements instead of a Unique HashCode based on the elements.
- Fixed: Added missing NonNull Checks.
- Fixed: Custom/OpenHashMap.containsValue implementation was wrong.
- Fixed: Custom/OpenHashMap.compute/present/absent now works how it is specified in the Java Documentation
- Fixed: Custom/OpenHashMap.merge/BulkMerge now works how it is specified in the Java Documentation
- Fixed: Custom/Linked/OpenHashMap.keySet.remove was causing a infinite loop.
- Fixed: Custom/Linked/OpenHashMap.entrySet.contains was not correctly comparing the entry.
- Fixed: Custom/OpenHashMap.mapIterator now no longer crashes in certain cases.
- Added: Custom/LinkedOpenHashMap now takes use of the improved Iterator it has for containsValue
- Fixed: CustomOpenHashMap.keySet.forEach was basically putting out keys even if they were present
- Fixed: ImmutableMaps issues thanks to the tests. Roughly the same as the rest of the maps
- Fixed: RB/AVLTreeMaps issues. Roughly the same as the rest of the maps
- Fixed: SubLists are now properly implemented.
- Fixed: HashSet Iterator bugs now fixed... That was Painful.
- Added: Tests for Lists and Sets

### Version 0.4.5
- Added: removeAll/retainAll(Collection c, Consumer r) which receives all the elements that got deleted from the collection
- Fixed: Supplier get function wasn't referencing original function.
- Added: addIfPresent/Absent to lists
- Added: distinct, limit and peek iterators
- Added: Iterable's can now reduce its contents
- Added: Better ForEach support for IterableWrappers so a Iterator chain is not created
- Added: SwapRemove to Lists which moves the last element into the desired space to be deleted
- Added: More Test cases

### Version 0.4.4
- Fixed: ObjectArrayList.of was causing crashes because of a Poor implementation.
- Added: Unsorted HashMaps/Sets now throw Concurrent exceptions if they were modified during a rehash.
- Added: Array/Collection version of enqueue and enqueueFirst to PriorityQueues.
- Added: fillBuffer function into PrimitiveLists which allow to optimize JavaNio buffers if needed.

### Version 0.4.3
- Added: Wrapper now support the Optimized Lambda replacer functions to improve performance.
- Added: FIFO Queue has now a minimum capacity and that is now checked more consistently.

### Version 0.4.2
- Added: Lists/Sets/Maps/PriorityQueues are now copy-able. with the new copy() function.
	Note: subLists/subMaps/subSets or synchronize/unmodifyable wrappers do not support that function.
- Fixed: PriorityQueues didn't implement: hashCode/equals/toString

### Version 0.4.1
- Changed: ForEach with input now provides input, value instead of value, input, this improves the usage of method references greatly
- Added: addAll with Array-types in collections.
- Added: Java Iterator/Iterable support for Stream replacing methods
- Added: Suppliers.
- Added: SupplyIfAbsent. It is ComputeIfAbsent but using suppliers
- Added: Count feature into Iterable
- Fixed: A couple bugs with the new StreamReplacing functions in LinkedCollections Iterating to Infinity

### Version 0.4.0
- Changed: Iterable specific helper functions were moved out of Iterators and moved into Iterables
- Added: New Stream replacing functions: findFirst, matchesAny/All/None
- Fixed: Compute/ComputeIfAbsent/ComputeIfPresent/Merge/BulkMerge in maps now behave like they should.
- Added: Implementations for New Stream replacing functions.
- Changed: Removed a lot of duplicated forEach implementations
- Added: Flat/Mapping functions (to object) are now accessible to primitive maps.
- Added: Filter function to Iterators/Iterables (Iterable implements it by default)
- Changed: Cleanup of some variables/mappers
- Added/Fixed: AVL/RBTreeMap got reworked and SubMaps work more properly now. Also forEach support got improved a lot
- Added/Fixed: TreeSubSets (RB/AVL) got their functional implementations improved too.
- Added: Pairs are now a thing. In Mutable/Immutable Form

### Version 0.3.6
- Fixed: addAll non Type Specific Lists was causing crashes.
- Fixed/Changed: clearAndTrim's implementation was all over the place. In some cases causing crash scenarios.
- Fixed: Wrappers didn't implement toString/equals/hashCode
- Added: Tests for addAll Bug
- Changed: Cleaned up CodeStyle as bugs were fixed.

### Version 0.3.5
- Fixed: Simple Code Generator dependency was declared wrong. Its only needed for runtime. Not for Compilation.
- Fixed: ObjectLists Crashed when a null was provided as a Comparator. (Unless the List was Initialized with the ClassType)
- Fixed: LinkedLists didn't implement add(Object)
- Fixed: Object Collections did have the JavaCollections deprecated as the Constructor. This should only be deprecated for Primitives
- Added: Tests with 5k Random names for Object sorting.
- Changed: Object Arrays no longer require a Comparable[] it just assumes now that the elements in the Array are Comparable
- Fixed: Dependency to SimpleCodeGenerator should be no longer a thing. Because the resulting library doesn't need it only the builder does.

### Version 0.3.4
- Fixed: ArrayLists didn't resize properly if they were empty.


### Version 0.3.3
- Added: Flat/Mapping function for Iterables/Iterators to help avoid streams for cleaner looking code
- Fixed: AVLTrees pollFirst/Last is now keeping orders and is fixed
- Fixed: AbstractCollection bulk adding methods now link to the specialized implementations.
- Fixed: A bug with getElements in ArrayList.
- Fixed: PriorityQueue remove/toArray function were renamed so they fit better with other interfaces. (remove => removeFirst and toArray uses a different genericType)
- Added: LinkedList which is a List/PriorityDequeue/Stack which allows for more optimized use-cases and reduced boxing/unboxing.
- Added: Tests for LinkedList

### Version 0.3.2
- Fixed: Map.put wasn't referring to primitive variants.
- Added: ImmutableList.
- Added: Iterator pour function into a List or Array
- Changed: Arrays Wrap is now accessible to Objects and now is ? extends TYPE instead of TYPE.
- Added: OpenHashSets now implement foreach and have less overhead.
- Added: ImmutableOpenHashSet that is not editable (is linked by default for fast iteration)
- Added: CustomOpenHashSets now implement foreach and have less overhead.
- Added: ImmutableOpenHashMap that is not editable (is linked by default for fast iteration)
- Added: Maps can now be created through the interface.
- Fixed: Lists.addElements(T...elements) was adding elements at the beginning of a list instead of the end.
- Fixed: Bugs with the AVLTreeSet. And marked bugs with AVLTreeX that are still present.

### Version 0.3.1
- Fixed: containsKey & containsValue in HashMaps were deprecated for Object Variants.
- Fixed: HashMap wasn't deleting Keys & Values references when removing a Object
- Fixed: AVLTreeMap didn't balance properly.
- Changed: EnumMap no longer tries to access SharedSecrets since its gone in java11
- Added: HashMaps now implement ITrimmable
- Added: AVLTreeSet didn't balance properly
- Fixed: HashMaps & LinkedMaps weren't clearing references properly.

### Version 0.3.0 (Breaking 0.2.0)
- Added: Stack.isEmpty was missing
- Changed: remove/removeLast/enqueue/enqueueFirst no longer use Type Suffixes
- Removed: Suffixes for unmodifiable & synchronize functions.
- Changed: Primitive Stacks no longer depend on the base Stack class. Because seriously not needed.
- Changed: PriorityQueues no longer extends Object Variant.
- Changed: Maps.get function is no longer using Suffixes unless its absolutely necessary.
- Changed: Maps.remove function is no longer using Suffixes unless its absolutely necessary.
- Changed: ObjectList methods are no longer marked Deprecated even so it was for primitive ones.
- Added: Shuffle & Reverse Methods.
- Added: Concat Iterators.
- Added: PriorityQueues