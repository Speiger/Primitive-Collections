package speiger.src.collections.ints.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.function.Int2ObjectFunction;
import speiger.src.collections.ints.functions.function.IntObjectUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Int2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator(Int2ObjectMap<V> map) {
		ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
		return entries instanceof Int2ObjectMap.FastEntrySet ? ((Int2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Int2ObjectMap.Entry<V>> fastIterable(Int2ObjectMap<V> map) {
		ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
		return map instanceof Int2ObjectMap.FastEntrySet ? new ObjectIterable<Int2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() { return ((Int2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) { ((Int2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Int2ObjectMap<V> map, Consumer<Int2ObjectMap.Entry<V>> action) {
		ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
		if(entries instanceof Int2ObjectMap.FastEntrySet) ((Int2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Int2ObjectMap<V> empty() { 
		return (Int2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectSortedMap<V> synchronize(Int2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectSortedMap<V> synchronize(Int2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectOrderedMap<V> synchronize(Int2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectOrderedMap<V> synchronize(Int2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectNavigableMap<V> synchronize(Int2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Int2ObjectNavigableMap<V> synchronize(Int2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Int2ObjectMap<V> unmodifiable(Int2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Int2ObjectOrderedMap<V> unmodifiable(Int2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Int2ObjectSortedMap<V> unmodifiable(Int2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Int2ObjectNavigableMap<V> unmodifiable(Int2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Int2ObjectMap.Entry<V> unmodifiable(Int2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Int2ObjectMap.Entry<V> unmodifiable(Map.Entry<Integer, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Int2ObjectMap<V> singleton(int key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractInt2ObjectMap<V> {
		final int key;
		final V value;
		IntSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Int2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(int key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(int key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(int key, V defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractInt2ObjectMap<V> {
		@Override
		public V put(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(int key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(int key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(int key, V defaultValue) { return null; }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractInt2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Integer, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2ObjectMap.Entry<V> entry) {
			super(entry.getIntKey(), entry.getValue());
		}
		
		@Override
		public void set(int key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Int2ObjectNavigableMap<V> {
		Int2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Int2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2ObjectNavigableMap<V> descendingMap() { return Int2ObjectMaps.synchronize(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2ObjectMap.Entry<V> firstEntry() { return Int2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2ObjectMap.Entry<V> lastEntry() { return Int2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey, boolean inclusive) { return Int2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey, boolean inclusive) { return Int2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, int toKey) { return Int2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey) { return Int2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey) { return Int2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lowerKey(int key) { return map.lowerKey(key); }
		@Override
		public int higherKey(int key) { return map.higherKey(key); }
		@Override
		public int floorKey(int key) { return map.floorKey(key); }
		@Override
		public int ceilingKey(int key) { return map.ceilingKey(key); }
		@Override
		public Int2ObjectMap.Entry<V> lowerEntry(int key) { return Int2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2ObjectMap.Entry<V> higherEntry(int key) { return Int2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2ObjectMap.Entry<V> floorEntry(int key) { return Int2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2ObjectMap.Entry<V> ceilingEntry(int key) { return Int2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Int2ObjectOrderedMap<V> {
		Int2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Int2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Int2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Int2ObjectSortedMap<V> {
		Int2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Int2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2ObjectSortedMap<V> subMap(int fromKey, int toKey) { return Int2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ObjectSortedMap<V> headMap(int toKey) { return Int2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ObjectSortedMap<V> tailMap(int fromKey) { return Int2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Int2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectMap<V> {
		Int2ObjectMap<V> map;
		ObjectCollection<V> values;
		IntSet keys;
		ObjectSet<Int2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Int2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(int key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(int key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(int key) { return map.get(key); }
		@Override
		public V getOrDefault(int key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Int2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.int2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Int2ObjectMap.Entry<V>>
	{
		ObjectSet<Int2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Int2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Int2ObjectMap.Entry<V>>() {
				ObjectIterator<Int2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2ObjectMap.Entry<V> next() { return Int2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Int2ObjectNavigableMap<V> {
		Int2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Int2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Int2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2ObjectMap.Entry<V> lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2ObjectMap.Entry<V> higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2ObjectMap.Entry<V> floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2ObjectMap.Entry<V> ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> headMap(Integer toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectNavigableMap<V> tailMap(Integer fromKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Integer lowerKey(Integer key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Integer floorKey(Integer key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Integer ceilingKey(Integer key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Integer higherKey(Integer key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Int2ObjectMap.Entry<V> lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2ObjectMap.Entry<V> floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2ObjectMap.Entry<V> ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2ObjectMap.Entry<V> higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Int2ObjectOrderedMap<V> {
		Int2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Int2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(int key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(int key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Int2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Int2ObjectSortedMap<V> {
		Int2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Int2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2ObjectSortedMap<V> subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ObjectSortedMap<V> headMap(int toKey)  { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ObjectSortedMap<V> tailMap(int fromKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Int2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2ObjectSortedMap<V> subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectSortedMap<V> headMap(Integer toKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ObjectSortedMap<V> tailMap(Integer fromKey) { synchronized(mutex) { return Int2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectMap<V> {
		Int2ObjectMap<V> map;
		ObjectCollection<V> values;
		IntSet keys;
		ObjectSet<Int2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(int key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(int key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Int2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(int key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(int key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Int2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(IntObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(int key, IntObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(int key, Int2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(int key, IntObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V merge(int key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Int2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(int key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Int2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2ObjectEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public V get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public V getOrDefault(Object key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public V put(Integer key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Integer key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Integer key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}