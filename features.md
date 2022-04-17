## Quality Of Life Features
New Specialized functions/classes that were added to increase performance and/or reduce allocations and/or for Quality of life.  


# Functions

Functions that increase performance or are quality of life in their nature.   

<details>
<summary>Iterable</summary>
<p>

## Functional Functions

Java adds themselves a lot of functional functions like,  
- Stream:
	- Map/FlatMap
	- Filter/Distinct/Limit
	- Count/FindFirst/Collect
	- Peek/ForEach/Reduce
	- anyMatch/allMatch/NoneMatch   

that allows to process a collection in a functional way.   
But these require streams which have a lot of Overhead in their nature.    

Luckly Primitive Collections adds replacement functions that provide the same functionality but with minimal overhead.   
Here are some examples:
```java
public ObjectIterable<Path> toPath(ObjectIterable<String> iterable) {
	return iterable.map(Paths::get).filter(Files::exist);
}

public Iterable<Path> toPath(Iterable<String> iterable) {
	return ObjectIterables.map(iterable, Paths::get).filter(Files::exist);
}

public int sum(IntIterable iterable) {
	return iterable.reduce(Integer::sum);
}
```

## AsyncAPI</summary>

The AsyncAPI is a Feature that simplifies the processing of Collections on a separate thread.   
It uses the same concept as Javas Stream API but uses the light weight Functions from Primitive Collections to achieve the same thing.   
Unlike Javas StreamAPI the AsyncAPI is always singleThreaded and more like Javas CompletableFuture, which you can await or let run Asynchronous.    

The Goal is it to simplify the processing of Collections asynchronous.   
Especially on tasks which don't have to be finished instantly but can be processed on the side.   

Here is a example of how the API works.
```java
public void processFiles(ObjectCollection<String> potentialFiles) {
     potentialFiles.asAsync()
         .map(Paths::get).filter(Files::exists) //Modifies the collection (Optional)
         .forEach(Files::delete) //Creates the action (Required)
         .callback(T -> {}} //Callback on completion, still offthread (Optional)
         .execute() //Starts the task. (Required)
}
```
</p>
</details>
<details>
<summary>Collection</summary>
<p>

These are functions specific to the Collections interface, stuff that everyone wished it was present to be in the first place.

## AddAll (Array)
Adding Elements to a Collection usually requires either a for loop or a Arrays.wrap().   
This isn't an issue with Primitive Collections.
```java
public void addMonths(ObjectCollection<String> months) {
	months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September, "October", November", "December");
}

public void addElements(ObjectCollection<String> result, String[] elements) {
	result.addAll(elements, 0, 5); //elements, offset, length
}
```

## containsAny
Everyone hatest comparing if 2 collections have part of each other included.   
The solution usually requires for loops and keeping track if things were found or not.   
And every Java Developer had this issue at least once and wished for a clean solution.
```java
public boolean hasMonths(ObjectCollection<Month> target, Collection<Month> toFind) {
	return target.containsAny(toFind);
}
```

## Copy
Collections get copied every now and then. There is only 2 ways that this happens.  
Javas Clone API or using Constructor that supports collections.  
Javas Clone API is kinda in a Zombie state, where it is supported or not. Its not really clear if you should use it or not.  
The Clone CloneNotSupportedException isn't helping either, causing more janky code.  
While a Constructor can only support so much and testing for every case isn't really viable.  

So the decision was made to straight out not support clone and instead add a copy function which doesn't use a checked exception.  
It works exactly like the clone function. In a sense where it creates a shallow copy. (SubCollections do not work for obvious reasons)
```java
public IntCollection copy(IntCollection original) {
	return original.copy();
}
```

## Primitive Streams
Since Javas Stream API is still really useful, even at its shortcomings, Primitive Collections provides easy access to it.  
Generic Streams and the closest Primitive Stream will be provided. So a FloatCollection goes to a DoubleStream.
```java
public IntStream createStream(IntCollection source) {
	return source.primitiveStream();
}
```

## RemoveAll/RetainAll with listener
Ever wanted use removeAll or retainAll and wanted to know what elements actually got deleted?  
The usual solution is to create a copy and then apply it to the original and cross reference them.  
Which leads to really messy code and just hasn't a clean solution.  
Luckly Primitive Collections got you covered.
```java
public void removeInvalidFiles(ObjectCollections<Path> files, ObjectCollection<Path> toRemove) {
	files.removeAll(toRemove, T -> System.out.println(T));
}

public void removeInvalidFiles(ObjectCollections<Path> files, ObjectCollection<Path> toKeep) {
	files.retainFiles(toKeep, T -> System.out.println(T));
}
```

## ToArray 
Primitive Collections supports primitive/generic toArray functions for its Primitive Collections.  
On top of that the Object side gets a Java9 function ported back to java8, which uses a functional Interface to create the backing array.
```java
public Integer[] toArray(IntCollection c) {
	return c.toArray(new Integer[c.size]);
}

public int[] toArray(IntCollection c) {
	return c.toIntArray();
}

public String[] toArray(ObjectCollection<String> c) {
	return c.toArray(String::new);
}
```
</p>
</details>

<details>
<summary>List</summary>
<p>

These functions are List specific functions, a couple of these are from FastUtil.

## add/get/remove/extractElements
These functions really useful helper functions. 3 of which are copied from FastUtil. (extract is from Primitive Collections)  
They are basically array forms of addAll, getAll, removeRange and removeAndGetRange. This is the simplest way to describe it.  

Here some example:
```java
public void addAll(DoubleList list) {
	list.addElements(0D, 12.2D, 3.5D, 4.2D);
}

public double[] getAll(DoubleList list, int amount) {
	double[] result = new double[amount];
	list.getElements(0, result);
	return result;
}

public void removeRange(FloatList list) {
	list.removeElements(5, 14);
}

public float[] extractRange(FloatList list) {
	return list.extractElements(5, 14); //Returns the removed elements
}
```

## addIfPresent/addIfAbsent
These two functions are simple helper functions that check internally if a element is present or absent before adding them to the List.   
Removing the need for a contains or indexOf check every time you want to add a element.
While it is of course better to use a set, there is cases where this is still useful.

```java
public void addElements(IntList list, int... numbersToAdd) {
	for(int e : numbersToAdd) {
		list.addIfAbsent(e);
	}
}

public void addExisting(ObjectList<String> list, String... textToAdd) {
	for(String s : textToAdd) {
		list.addIfPresent(s);
	}
}
```

## SwapRemove
Lists when removing a Element shift usually the backing array to the left based to shrink the elements.  
While that isn't computational expensive with LinkedLists, it is with ArrayLists.  
Here comes swapRemove into play, which just removes the desired elements and instead of shifting left puts the last element in its place.
This reduces the data copying required down to 1 element instead of an array.

```java
public int remove(IntList elements, int indexToRemove) {
	return elements.swapRemove(indexToRemove);
}
```

## Unstable Sort (From FastUtil)
Unstable Sort uses a Faster but not as stable sorting algorithm to sort the Collection.  
Stable doesn't mean crashing, but more like that the result isn't exactly perfectly sorted.  
```java
public void sort(List<Month> list, Comparator<Month> sorter) {
	list.unstableSort(sorter);
}
```
</p>
</details>

<details>
<summary>Map</summary>
<p>

These functions are based on the Map interface. Useful functions you really would want.  

## addTo/subFrom
addTo (from FastUtil) and subFrom are mathematically functions that either add or subtract from the value of a given key.   
And if the key isn't present or would result in the default value it will either add or remove the entry from the Map. Given the circumstance.   
This is a really useful function and I wish FastUtil made it accessible by default but sadly it isn't.   
To simplify the explanation:    
- addTo if no element is present puts in the desired number, otherwise it sums up the two values.   
- subFrom if a element is present subtracts from it, if the element reaches the default value it removes the element from the map. If not present it will be ignored.   
```java
public void addTo(Object2DoubleMap<Month> map, Month key, double averageTrainsRepaired) {
	map.addTo(key, averageTrainsRepaired);
}

public void subFrom(Long2IntMap map, long key, double amount) {
	map.subFrom(key, amount);
}
```

## addToAll
Simple bulk version of the addTo function since sometimes you want to merge 2 maps for summing.  
Especially if your work is multi-threaded this can become useful.

```java
public void addTo(Object2DoubleMap<Month> map, Object2DoubleMap<Month> trainsRepaired) {
	map.addToAll(trainsRepaired);
}
```

## mergeAll
This is a simple bulk version of merge since merging 2 maps is more frequent then people might think and leads to cleaner code too.
```java
public void merge(Long2ByteMap result, Long2ByteMap toMerge) {
	result.mergeAll(toMerge);
}
```

## putAll (Array)
This allows to put keys and values as arrays instead of requiring a WrapperMap to insert the elements.  
Not as useful as the Collections.addAll variant but still really useful.
```java
public void putAll(Int2DoubleMap map, int[] keys, double[] values) {
	map.put(keys, values, 2, 15);
}
```

## putAllIfAbsent
putAll has this usual quirk where if a element is present it will replace the value, and sometimes this is not wanted.  
While putIfAbsent exists it has no real mass form and makes iterative solutions really uneasy to use.  
Here comes the helper function that gets rid of that problem.
```java
public void merge(Long2ObjectMap<String> regionFiles, Long2ObjectMap<String> toAdd) {
	regionFiles.putAllIfAbsent(toAdd);
}
```

## removeOrDefault
getOrDefault is a really useful function that find use cases all the time.   
Sadly by default there is no variant of removeOrDefault, while it has less cases still could be used every now and then.  
This function basically tries to remove a element, if it is not present it will just return your desired default.
```java
public Path removeCache(Long2ObjectMap<Path> caches, long key) {
	return caches.removeOrDefault(key, Paths.get("nuclearFun"));
}
```

## supplyIfAbsent
This one is one of my favorites. computeIfAbsent is a really useful function.   
But in 90% of the cases I use it the value is a collection.  
This becomes really annoying since methodReferences are faster/cleaner then Lambdas in my opinion.  
supplyIfAbsent is basically computeIfAbsent but without a key, perfect for the default constructor of a collection.  
This is the whole reason it exists.  
```java
public void example(Int2ObjectMap<List<String>> map, Int2ObjectMap<String> toAdd) {
	for(Entry<String> entry : toAdd.entrySet()) {
		map.supplyIfAbsent(entry.getKey(), ObjectArrayList::new).add(entry.getValue());
	}
}
```
</p>
</details>

# Interfaces

Interfaces that provide essential or quality of life features.

<details>
<summary>ITrimmable</summary>
<p>

The ITrimmable is Accessor interface that allows you to access a couple helper functions to control the size of your collections.  
This was created for the constant casting requirement to implementations just to shrink collections which get annoying over time.  

## trim
This function basically trims down the backing implementation to use as little memory as required to store the elements in the collection.  
Optionally a desired minimum size can be provided as of how low it should go at worst.  

## clearAndTrim
when you want to reset a Collection completely you have 2 options. Clear it and then call trim, or recreate the collection.  
clearAndTrim solves this problem by clearing the collection and trimming it in one go, reducing overhead to achieve such a thing.  

</p>
</details>

<details>
<summary>IArray</summary>
<p>

IArray is a Accessor interface that provides more access to collections by providing tools to grow your collection as needed.  
While putAll/addAll try to ensure that you have enough room for your elements, this is not really a solution for all cases.  
Sometimes you need to ensure the Collection is pre-initialized.  
IArray grants you that control.  

There is also a type specific that provides you access to the backing array implementation of Lists for faster Iteration but that is a really specific case.  

## ensureCapacity
Ensures that your collection has enough storage for the elements you want to insert.  

## elements (ITypeSpecificArray)
Allows you access to the backing array of a List which is for people who know what they are doing.  
There is a lambda version of this function too which makes sure for synchronizedLists that you are the only one accessing the array.  

</p>
</details>

<details>
<summary>OrderedMap</summary>
<p>

The OrderedMap is a real edge case interface that was born for a need.    
FastUtil added functions that were like moveToFirst which were hardcoded to the implementation.  
They didn't fit into something like a SortedMap because the Set wasn't sorted.  
So OrderedMap was born, which isn't random but ordered in a specific way that can be changed.  

## getAndMoveToFirst/getAndMoveToLast
Returns a desired element and removing it to the first/last spot in the Map. Moving the element that was at its spot after/before it.  

## moveToFirst/moveToLast
Moves the element if present to the first/last spot in the Map. Moving the element that was at its spot after/before it.  
Returns true if the element was actually moved.  

## putAndMoveToFirst/putAndMoveToLast
Adds the desired element and moves it to first/last spot in the Map. Moving the element that was at its spot after/before it.  

## firstKey/lastKey (Optional poll)
Provides access to the current first/last key of the Map.  
Optionally can be polled if desired.  

## firstValue/lastValue
Provides access to the current first/last value of the Map.  

</p>
</details>

<details>
<summary>OrderedSet</summary>
<p>

The OrderedSet is a real edge case interface that was born for a need.    
FastUtil added functions that were like moveToFirst which were hardcoded to the implementation.  
They didn't fit into something like a SortedSet because the Set wasn't sorted.  
So OrderedSet was born, which isn't random but ordered in a specific way that can be changed.  

## addAndMoveToFirst/addAndMoveToLast
Adds the desired element and moves it to first/last spot in the Collection. Moving the element that was at its spot after/before it.  

## moveToFirst/moveToLast
Moves the element if present to the first/last spot in the Collection. Moving the element that was at its spot after/before it.  
Returns true if the element was actually moved.  

## first/last (Optional poll)
Provides access to the current first/last element of the set.  
Optionally can be polled if desired.

</p>
</details>