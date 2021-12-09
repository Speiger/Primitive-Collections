# Primitive-Collections

This is a Simple Primitive Collections Library i started as a hobby Project.     
It is based on Java's Collection Library and FastUtil.     
But its focus is a different one.     

## Main Features:      
- ArrayLists / LinkedLists
- HashSet/Map (Linked & HashControl)
- TreeSet/Map (RB & AVL)
- EnumMap
- Immutable Maps/Lists/Sets
- Priority Queue
- Streams
- SplitIterators
- Iterators
- Pairs
- Unary/Functions
- Suppliers
- Bi/Consumers

## Specialized Functions
New Specialized functions that were added to increase performance or reduce allocations or Quality Of life.
To highlight things that may be wanted.
- Iterable:
	- map/flatMap/arrayFlatMap: A Light weight version of Stream.map().
	- findFirst: Allows to find the first element of a Predicated Iterable.
	- filter: Allows to filter unwanted elements for wrapped Iterable
	- matchAny/matchNone/matchAll: Allows to find elements in a collection.
	- count: counts all valid elements in a collection.
	- forEach: Allows to input a second element into a forEach move allowing for more flexibility for Method References
	- reduce/limit/peek/distinct: Light Versions of the Stream variant, to reduce Stream usage.
	- pour: a function that allows to collect all elements within the Collection
- Collection:
	- containsAny: Allows to test if another collection contains an of the elements of the tested collection.
	- primitiveStream: Provides access to the closest Java Stream Type.
	- copy: shallowCopies the collection, used instead of clone because this is better to use.
		(subCollections/synchronized/unmodifiable not supported for obvious reasons)
	- toArray: the ToArray function from Java9 and newer that uses a Functional interface and can use a method reference
- List:
	- add/get/removeElements (From FastUtil): Allows to add/get/remove an Array into/from a list. Just with less overhead
	- extractElements: Allows to remove a Range of elements from the List and get what was removed.
	- Unstable Sort(From FastUtil): Uses a faster but not stable sort (Quick-Sort as example) to sort the list.
	- addIfAbsent/Present: adds a element only if absent/present in the list
	- swapRemove: deletes a desired element and inserts the last element in its place instead of leftshifting elements.
- SortedSet:
	- addAndMoveToFirst/Last (From FastUtil but moved to Interface): Allows to add a element to the first/last position of a sorted set.
	- moveToFirst/Last: Moves the desired element at the first/last position of the SortedSet.
	- pollFirst/Last: Allows to poll the first/last element of the set.
- Map:
	- putAll: putAll but in Array form.
	- putAllIfAbsent: Puts only the elements that are absent.
	- addTo (Only Primitives Values) (From FastUtil but moved to Interface): allows to add to the value of a given key. If not present it will be added. (Accumulator)
	- addToAll: Same as addTo but bulkVersion.
	- removeOrDefault: removes a Element and if not present returns the default value instead of the present value.
	- mergeAll: BulkVersion of Merge function.
	- supplyIfAbsent: A Supplier based computeIfAbsent
- Sorted Map:
	- addAndMoveToFirst/Last (From FastUtil but moved to Interface): Allows to add a element to the first/last position of a sorted Map.
	- moveToFirst/Last: Moves the desired element at the first/last position of the Map.
	- getAndMoveToFirst/Last: gets the element and moves it to the first/last position. Replicating a Optional LinkedHashMap feature.
	- pollFirst/LastKey: Allows to poll the first/last element.
	- first/LastValue: Allows to get the first/last value from the Map.
	
	
# Notes about Versions
Any 0.x.0 version (Minor) can be reason for massive changes including API.     
To ensure that problems can be dealt with even if it is breaking the current API.     
Any breaking changes will be Documented (once 1.0 is released)     

Also to save space every 0.0.x (Patch) that is 2 Minor Versions behind will be removed. 
So if 0.5.0 is released every 0.3.x patch will be deleted, except for the last patch for that minor version.

# How to install
Using Gradle:
```gradle
repositories {
    maven {
        url = "https://maven.speiger.com/repository/main"
    }
}
dependencies {
	compile 'de.speiger:Primitive-Collections:0.4.5'
}
```
Direct:

| Version 	| Jar                                                                                                                          	| Sources                                                                                                                              	| Java Doc                                                                                                                             	|
|---------	|------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|
| 0.4.5   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5-javadoc.jar) 	|
| 0.4.4   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4-javadoc.jar) 	|
| 0.4.3   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3-javadoc.jar) 	|
| 0.4.2   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2-javadoc.jar) 	|
| 0.4.1   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1-javadoc.jar) 	|
| 0.4.0   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0-javadoc.jar) 	|
| 0.3.6   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-javadoc.jar) 	|
| 0.3.5   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5-javadoc.jar) 	|
| 0.3.4   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4-javadoc.jar) 	|
| 0.3.3   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3-javadoc.jar) 	|

# Contributing
If you want to contribute.      
This project is created using gradle and java and my Template Library only. Nothing extra.      
If you setup gradle the library will be downloaded automatically.      

Where is everything stored?
- Variables and ClassNames are define [here](src/builder/java/speiger/src/builder/GlobalVariables.java)
- Templates are stored [here](src/builder/resources/speiger/assets/collections/templates)
- Tests can be found [here](src/test/java/speiger/src/collections)

Please if you want to contribute follow the [Rule-Sheet](RuleSheet.md). It keeps everything in line.


# How to Build

The SourceCode can be generated via:     
/gradlew.bat generateSource      

to build the jar:           
/gradlew.bat build      
do not combine the commands because they can not be executed at the same time.      

## Current Down Sides (Random order)
- Testing for Sub Maps/Sets/Lists are only in a very basic way tested
- Documentation is only present at the lowest level for most cases and needs a typo fixing.