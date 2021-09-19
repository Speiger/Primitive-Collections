# Changelog of versions


### Version 0.3.7/0.4.0
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