# Changelog of versions


## Version 0.3.0 (Breaking 0.2.0)
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