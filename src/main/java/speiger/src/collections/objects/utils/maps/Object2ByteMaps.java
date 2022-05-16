package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.Object2ByteFunction;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.bytes.utils.ByteSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Object2ByteMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2ByteMap.Entry<T>> fastIterator(Object2ByteMap<T> map) {
		ObjectSet<Object2ByteMap.Entry<T>> entries = map.object2ByteEntrySet();
		return entries instanceof Object2ByteMap.FastEntrySet ? ((Object2ByteMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2ByteMap.Entry<T>> fastIterable(Object2ByteMap<T> map) {
		ObjectSet<Object2ByteMap.Entry<T>> entries = map.object2ByteEntrySet();
		return map instanceof Object2ByteMap.FastEntrySet ? new ObjectIterable<Object2ByteMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2ByteMap.Entry<T>> iterator() { return ((Object2ByteMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2ByteMap.Entry<T>> action) { ((Object2ByteMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2ByteMap<T> map, Consumer<Object2ByteMap.Entry<T>> action) {
		ObjectSet<Object2ByteMap.Entry<T>> entries = map.object2ByteEntrySet();
		if(entries instanceof Object2ByteMap.FastEntrySet) ((Object2ByteMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2ByteMap<T> empty() { 
		return (Object2ByteMap<T>)EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteMap<T> synchronize(Object2ByteMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteMap<T> synchronize(Object2ByteMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteSortedMap<T> synchronize(Object2ByteSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteSortedMap<T> synchronize(Object2ByteSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteOrderedMap<T> synchronize(Object2ByteOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteOrderedMap<T> synchronize(Object2ByteOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteNavigableMap<T> synchronize(Object2ByteNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2ByteNavigableMap<T> synchronize(Object2ByteNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ByteMap<T> unmodifiable(Object2ByteMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ByteOrderedMap<T> unmodifiable(Object2ByteOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ByteSortedMap<T> unmodifiable(Object2ByteSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2ByteNavigableMap<T> unmodifiable(Object2ByteNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2ByteMap.Entry<T> unmodifiable(Object2ByteMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2ByteMap.Entry<T> unmodifiable(Map.Entry<T, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2ByteMap<T> singleton(T key, byte value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2ByteMap<T> {
		final T key;
		final byte value;
		ObjectSet<T> keySet;
		ByteCollection values;
		ObjectSet<Object2ByteMap.Entry<T>> entrySet;
		
		SingletonMap(T key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public byte remOrDefault(T key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte getByte(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(T key, byte defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2ByteMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2ByteMap<T> {
		@Override
		public byte put(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public byte remOrDefault(T key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte getByte(T key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(T key, byte defaultValue) { return (byte)0; }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2ByteMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2ByteMap.Entry<T> entry) {
			super(entry.getKey(), entry.getByteValue());
		}
		
		@Override
		public void set(T key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2ByteNavigableMap<T> {
		Object2ByteNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2ByteNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2ByteNavigableMap<T> descendingMap() { return Object2ByteMaps.synchronize(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2ByteMap.Entry<T> firstEntry() { return Object2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2ByteMap.Entry<T> lastEntry() { return Object2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2ByteMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ByteMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ByteNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2ByteNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2ByteNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2ByteNavigableMap<T> subMap(T fromKey, T toKey) { return Object2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ByteNavigableMap<T> headMap(T toKey) { return Object2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ByteNavigableMap<T> tailMap(T fromKey) { return Object2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2ByteMap.Entry<T> lowerEntry(T key) { return Object2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2ByteMap.Entry<T> higherEntry(T key) { return Object2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2ByteMap.Entry<T> floorEntry(T key) { return Object2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2ByteMap.Entry<T> ceilingEntry(T key) { return Object2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2ByteNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2ByteOrderedMap<T> {
		Object2ByteOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2ByteOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Object2ByteOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2ByteSortedMap<T> {
		Object2ByteSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2ByteSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2ByteSortedMap<T> subMap(T fromKey, T toKey) { return Object2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ByteSortedMap<T> headMap(T toKey) { return Object2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ByteSortedMap<T> tailMap(T fromKey) { return Object2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Object2ByteSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2ByteMap<T> implements Object2ByteMap<T> {
		Object2ByteMap<T> map;
		ByteCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2ByteMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2ByteMap<T> map) {
			this.map = map;
		}
		
		@Override
		public byte put(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(T key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public byte remOrDefault(T key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte getByte(T key) { return map.getByte(key); }
		@Override
		public byte getOrDefault(T key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Object2ByteMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2ByteMap.Entry<T>>
	{
		ObjectSet<Object2ByteMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2ByteMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2ByteMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2ByteMap.Entry<T>>() {
				ObjectIterator<Object2ByteMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2ByteMap.Entry<T> next() { return Object2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2ByteNavigableMap<T> {
		Object2ByteNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2ByteNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2ByteNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2ByteNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Object2ByteMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2ByteMap.Entry<T> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2ByteMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2ByteMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2ByteNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2ByteNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2ByteNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2ByteNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ByteNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ByteNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2ByteMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2ByteMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2ByteMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2ByteMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2ByteNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2ByteOrderedMap<T> {
		Object2ByteOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2ByteOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2ByteOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(T key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(T key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Object2ByteOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2ByteSortedMap<T> {
		Object2ByteSortedMap<T> map;
		
		SynchronizedSortedMap(Object2ByteSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2ByteSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2ByteSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ByteSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ByteSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Object2ByteSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2ByteMap<T> implements Object2ByteMap<T> {
		Object2ByteMap<T> map;
		ByteCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2ByteMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2ByteMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2ByteMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2ByteMap<T> setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(T key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(T key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2ByteMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(T key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(T key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2ByteMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2ByteMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte getByte(T key) { synchronized(mutex) { return map.getByte(key); } }
		@Override
		public byte rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public byte remOrDefault(T key, byte defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(T key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Object2ByteMap<T> m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(ObjectByteUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(T key, Object2ByteFunction<T> mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Object2ByteMap<T> m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(T key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectByteConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Object2ByteMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2ByteEntrySet(), mutex);
			return entrySet;
		}
		
	}
}