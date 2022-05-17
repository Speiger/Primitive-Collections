package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.Object2ShortFunction;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortCollections;
import speiger.src.collections.shorts.utils.ShortSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2ShortMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2ShortMap<T> empty() { 
		return (Object2ShortMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2ShortMap.Entry<T>> fastIterator(Object2ShortMap<T> map) {
		ObjectSet<Object2ShortMap.Entry<T>> entries = map.object2ShortEntrySet();
		return entries instanceof Object2ShortMap.FastEntrySet ? ((Object2ShortMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2ShortMap.Entry<T>> fastIterable(Object2ShortMap<T> map) {
		ObjectSet<Object2ShortMap.Entry<T>> entries = map.object2ShortEntrySet();
		return map instanceof Object2ShortMap.FastEntrySet ? new ObjectIterable<Object2ShortMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2ShortMap.Entry<T>> iterator() { return ((Object2ShortMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) { ((Object2ShortMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2ShortMap<T> map, Consumer<Object2ShortMap.Entry<T>> action) {
		ObjectSet<Object2ShortMap.Entry<T>> entries = map.object2ShortEntrySet();
		if(entries instanceof Object2ShortMap.FastEntrySet) ((Object2ShortMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortMap<T> synchronize(Object2ShortMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortMap<T> synchronize(Object2ShortMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortSortedMap<T> synchronize(Object2ShortSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortSortedMap<T> synchronize(Object2ShortSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortOrderedMap<T> synchronize(Object2ShortOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortOrderedMap<T> synchronize(Object2ShortOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortNavigableMap<T> synchronize(Object2ShortNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ShortNavigableMap<T> synchronize(Object2ShortNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ShortMap<T> unmodifiable(Object2ShortMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ShortOrderedMap<T> unmodifiable(Object2ShortOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ShortSortedMap<T> unmodifiable(Object2ShortSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ShortNavigableMap<T> unmodifiable(Object2ShortNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2ShortMap.Entry<T> unmodifiable(Object2ShortMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2ShortMap.Entry<T> unmodifiable(Map.Entry<T, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2ShortMap<T> singleton(T key, short value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2ShortMap<T> {
		final T key;
		final short value;
		ObjectSet<T> keySet;
		ShortCollection values;
		ObjectSet<Object2ShortMap.Entry<T>> entrySet;
		
		SingletonMap(T key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public short remOrDefault(T key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short getShort(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(T key, short defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2ShortMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2ShortMap<T> {
		@Override
		public short put(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public short remOrDefault(T key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short getShort(T key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(T key, short defaultValue) { return (short)0; }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2ShortMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2ShortMap.Entry<T> entry) {
			super(entry.getKey(), entry.getShortValue());
		}
		
		@Override
		public void set(T key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2ShortNavigableMap<T> {
		Object2ShortNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2ShortNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2ShortNavigableMap<T> descendingMap() { return Object2ShortMaps.synchronize(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2ShortMap.Entry<T> firstEntry() { return Object2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2ShortMap.Entry<T> lastEntry() { return Object2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2ShortMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ShortMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, T toKey) { return Object2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey) { return Object2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey) { return Object2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2ShortMap.Entry<T> lowerEntry(T key) { return Object2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2ShortMap.Entry<T> higherEntry(T key) { return Object2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2ShortMap.Entry<T> floorEntry(T key) { return Object2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2ShortMap.Entry<T> ceilingEntry(T key) { return Object2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2ShortNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2ShortOrderedMap<T> {
		Object2ShortOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2ShortOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Object2ShortOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2ShortSortedMap<T> {
		Object2ShortSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2ShortSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2ShortSortedMap<T> subMap(T fromKey, T toKey) { return Object2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ShortSortedMap<T> headMap(T toKey) { return Object2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ShortSortedMap<T> tailMap(T fromKey) { return Object2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Object2ShortSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortMap<T> {
		Object2ShortMap<T> map;
		ShortCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2ShortMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2ShortMap<T> map) {
			this.map = map;
		}
		
		@Override
		public short put(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(T key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public short remOrDefault(T key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short getShort(T key) { return map.getShort(key); }
		@Override
		public short getOrDefault(T key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Object2ShortMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2ShortMap.Entry<T>>
	{
		ObjectSet<Object2ShortMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2ShortMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2ShortMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2ShortMap.Entry<T>>() {
				ObjectIterator<Object2ShortMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2ShortMap.Entry<T> next() { return Object2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2ShortNavigableMap<T> {
		Object2ShortNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2ShortNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2ShortNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2ShortNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Object2ShortMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2ShortMap.Entry<T> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2ShortMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2ShortMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2ShortMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2ShortMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2ShortMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2ShortMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2ShortNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2ShortOrderedMap<T> {
		Object2ShortOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2ShortOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2ShortOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(T key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(T key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Object2ShortOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2ShortSortedMap<T> {
		Object2ShortSortedMap<T> map;
		
		SynchronizedSortedMap(Object2ShortSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2ShortSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2ShortSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ShortSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ShortSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Object2ShortSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortMap<T> {
		Object2ShortMap<T> map;
		ShortCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2ShortMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2ShortMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2ShortMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2ShortMap<T> setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(T key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(T key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2ShortMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(T key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(T key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2ShortMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2ShortMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short getShort(T key) { synchronized(mutex) { return map.getShort(key); } }
		@Override
		public short rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public short remOrDefault(T key, short defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(T key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Object2ShortMap<T> m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(ObjectShortUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(T key, Object2ShortFunction<T> mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(T key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectShortConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Object2ShortMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2ShortEntrySet(), mutex);
			return entrySet;
		}
		
	}
}