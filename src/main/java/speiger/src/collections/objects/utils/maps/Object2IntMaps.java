package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.objects.functions.function.Object2IntFunction;
import speiger.src.collections.objects.functions.function.ObjectIntUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.ints.utils.IntSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Object2IntMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2IntMap.Entry<T>> fastIterator(Object2IntMap<T> map) {
		ObjectSet<Object2IntMap.Entry<T>> entries = map.object2IntEntrySet();
		return entries instanceof Object2IntMap.FastEntrySet ? ((Object2IntMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2IntMap.Entry<T>> fastIterable(Object2IntMap<T> map) {
		ObjectSet<Object2IntMap.Entry<T>> entries = map.object2IntEntrySet();
		return map instanceof Object2IntMap.FastEntrySet ? new ObjectIterable<Object2IntMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2IntMap.Entry<T>> iterator() { return ((Object2IntMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2IntMap.Entry<T>> action) { ((Object2IntMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2IntMap<T> map, Consumer<Object2IntMap.Entry<T>> action) {
		ObjectSet<Object2IntMap.Entry<T>> entries = map.object2IntEntrySet();
		if(entries instanceof Object2IntMap.FastEntrySet) ((Object2IntMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2IntMap<T> empty() { 
		return (Object2IntMap<T>)EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntMap<T> synchronize(Object2IntMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntMap<T> synchronize(Object2IntMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntSortedMap<T> synchronize(Object2IntSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntSortedMap<T> synchronize(Object2IntSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntOrderedMap<T> synchronize(Object2IntOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntOrderedMap<T> synchronize(Object2IntOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntNavigableMap<T> synchronize(Object2IntNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2IntNavigableMap<T> synchronize(Object2IntNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2IntMap<T> unmodifiable(Object2IntMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2IntOrderedMap<T> unmodifiable(Object2IntOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2IntSortedMap<T> unmodifiable(Object2IntSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2IntNavigableMap<T> unmodifiable(Object2IntNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2IntMap.Entry<T> unmodifiable(Object2IntMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2IntMap.Entry<T> unmodifiable(Map.Entry<T, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2IntMap<T> singleton(T key, int value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2IntMap<T> {
		final T key;
		final int value;
		ObjectSet<T> keySet;
		IntCollection values;
		ObjectSet<Object2IntMap.Entry<T>> entrySet;
		
		SingletonMap(T key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public int remOrDefault(T key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int getInt(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(T key, int defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2IntMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2IntMap<T> {
		@Override
		public int put(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public int remOrDefault(T key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int getInt(T key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(T key, int defaultValue) { return 0; }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2IntMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2IntMap.Entry<T> entry) {
			super(entry.getKey(), entry.getIntValue());
		}
		
		@Override
		public void set(T key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2IntNavigableMap<T> {
		Object2IntNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2IntNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2IntNavigableMap<T> descendingMap() { return Object2IntMaps.synchronize(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2IntMap.Entry<T> firstEntry() { return Object2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2IntMap.Entry<T> lastEntry() { return Object2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2IntMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2IntMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2IntNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2IntNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2IntNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2IntNavigableMap<T> subMap(T fromKey, T toKey) { return Object2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2IntNavigableMap<T> headMap(T toKey) { return Object2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2IntNavigableMap<T> tailMap(T fromKey) { return Object2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2IntMap.Entry<T> lowerEntry(T key) { return Object2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2IntMap.Entry<T> higherEntry(T key) { return Object2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2IntMap.Entry<T> floorEntry(T key) { return Object2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2IntMap.Entry<T> ceilingEntry(T key) { return Object2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2IntNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2IntOrderedMap<T> {
		Object2IntOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2IntOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Object2IntOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2IntSortedMap<T> {
		Object2IntSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2IntSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2IntSortedMap<T> subMap(T fromKey, T toKey) { return Object2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2IntSortedMap<T> headMap(T toKey) { return Object2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2IntSortedMap<T> tailMap(T fromKey) { return Object2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Object2IntSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2IntMap<T> implements Object2IntMap<T> {
		Object2IntMap<T> map;
		IntCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2IntMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2IntMap<T> map) {
			this.map = map;
		}
		
		@Override
		public int put(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(T key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public int remOrDefault(T key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int getInt(T key) { return map.getInt(key); }
		@Override
		public int getOrDefault(T key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Object2IntMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2IntMap.Entry<T>>
	{
		ObjectSet<Object2IntMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2IntMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2IntMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2IntMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2IntMap.Entry<T>>() {
				ObjectIterator<Object2IntMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2IntMap.Entry<T> next() { return Object2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2IntNavigableMap<T> {
		Object2IntNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2IntNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2IntNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2IntNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Object2IntMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2IntMap.Entry<T> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2IntMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2IntMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2IntNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2IntNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2IntNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2IntNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2IntNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2IntNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2IntMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2IntMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2IntMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2IntMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2IntNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2IntOrderedMap<T> {
		Object2IntOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2IntOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2IntOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(T key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(T key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Object2IntOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2IntSortedMap<T> {
		Object2IntSortedMap<T> map;
		
		SynchronizedSortedMap(Object2IntSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2IntSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2IntSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2IntSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2IntSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Object2IntSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2IntMap<T> implements Object2IntMap<T> {
		Object2IntMap<T> map;
		IntCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2IntMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2IntMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2IntMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2IntMap<T> setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(T key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(T key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2IntMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(T key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(T key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2IntMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2IntMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int getInt(T key) { synchronized(mutex) { return map.getInt(key); } }
		@Override
		public int rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public int remOrDefault(T key, int defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(T key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Object2IntMap<T> m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(ObjectIntUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(T key, ObjectIntUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(T key, Object2IntFunction<T> mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(T key, ObjectIntUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int mergeInt(T key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Object2IntMap<T> m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(T key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectIntConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Object2IntMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2IntEntrySet(), mutex);
			return entrySet;
		}
		
	}
}