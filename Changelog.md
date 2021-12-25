# Changelog of versions

### Version 0.5.3
- Added: OrderedMap/Set
- Added: Deprecation to Functions that are specific to Ordered interfaces in the SortedMap/Set
- Added: subFrom to Maps which is the counterpart of the addTo method

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