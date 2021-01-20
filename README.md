# Primitive-Collections (To be Renamed)

This is a Simple Primitive Collections Library i started as a hobby Project.
It's basis is of FastUtil and Java's Collection Library
But both lib's have a lot of problems, and in FastUtils case foundation seem changes to be no longer possible.
So this project was started.
With a Rule-Sheet, and also hopefully more Test-Coverage.

Currently Cons against FastUtil (will/might change over time).
- No Maps, this is next on the todolist.
- No Singletons,
- No BigLists/Sets/Maps
- No Custom Spliterator support.
- No Reference Collections.
- Some implementations are slower due to not including all Micro-optimizations (this might not change)
- Not as Specific Case Testing compared to FastUtil
- No Java Serialization implemented
- Code Generator is right now not finalized and no automatic setup exists as of this moment.

Pros against FastUtil
- A Cleaner implementation of Collections that do exists: NavigableSet (Exists), PriorityQueues are Save-able outside of java Serialization and a lot of others.
- More Consistency with features, not leaving a lot of holes in implementations. (Anything outside of HashMaps/Lists have massive holes)
- Abstract Tests to allow more coverage on tests and allow quicker implementation on tests on new Implementations.
- A lot better packaging where each types have their own packages to also make it easier to use the library. (lists/sets/collections/queues/utils)
- Due to package name choice: This Library imports do not overshadow java imports. (Anything after s is overshadowed)
- No Linux Environment necessary to generate the sourcecode if you want to work on it. All done with Java and or Gradle.
- A lot of extra functions that become useful in some cases, like: ITrimmable, IArray, extractElements(from, to), moveToFirst/Last, PriorityQueue iterator. Better Synchronization wrappers that expose useful interfaces.
- More Control how internals work thanks to SanityChecks
- Support to some extend of a Java Specific Feature: Non-TypeSepcific Collection Non Delete functions. (any method that used Object as Parameter instead of Generic)

This Library also includes my own MergeSort Algorithm that does not need to duplicate the input array.
It is a bit slower depending on the size, but is a stable sort that does not require to duplicate an array