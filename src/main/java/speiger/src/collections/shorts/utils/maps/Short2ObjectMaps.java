package speiger.src.collections.shorts.utils.maps;

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
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.consumer.ShortObjectConsumer;
import speiger.src.collections.shorts.functions.function.Short2ObjectFunction;
import speiger.src.collections.shorts.functions.function.ShortObjectUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Short2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Short2ObjectMap.Entry<V>> fastIterator(Short2ObjectMap<V> map) {
		ObjectSet<Short2ObjectMap.Entry<V>> entries = map.short2ObjectEntrySet();
		return entries instanceof Short2ObjectMap.FastEntrySet ? ((Short2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Short2ObjectMap.Entry<V>> fastIterable(Short2ObjectMap<V> map) {
		ObjectSet<Short2ObjectMap.Entry<V>> entries = map.short2ObjectEntrySet();
		return map instanceof Short2ObjectMap.FastEntrySet ? new ObjectIterable<Short2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Short2ObjectMap.Entry<V>> iterator() { return ((Short2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2ObjectMap.Entry<V>> action) { ((Short2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Short2ObjectMap<V> map, Consumer<Short2ObjectMap.Entry<V>> action) {
		ObjectSet<Short2ObjectMap.Entry<V>> entries = map.short2ObjectEntrySet();
		if(entries instanceof Short2ObjectMap.FastEntrySet) ((Short2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Short2ObjectMap<V> empty() { 
		return (Short2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectMap<V> synchronize(Short2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectMap<V> synchronize(Short2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectSortedMap<V> synchronize(Short2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectSortedMap<V> synchronize(Short2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectOrderedMap<V> synchronize(Short2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectOrderedMap<V> synchronize(Short2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectNavigableMap<V> synchronize(Short2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Short2ObjectNavigableMap<V> synchronize(Short2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Short2ObjectMap<V> unmodifiable(Short2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Short2ObjectOrderedMap<V> unmodifiable(Short2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Short2ObjectSortedMap<V> unmodifiable(Short2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Short2ObjectNavigableMap<V> unmodifiable(Short2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Short2ObjectMap.Entry<V> unmodifiable(Short2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Short2ObjectMap.Entry<V> unmodifiable(Map.Entry<Short, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Short2ObjectMap<V> singleton(short key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractShort2ObjectMap<V> {
		final short key;
		final V value;
		ShortSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Short2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(short key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(short key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(short key, V defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractShort2ObjectMap<V> {
		@Override
		public V put(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(short key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(short key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(short key, V defaultValue) { return null; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractShort2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Short, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2ObjectMap.Entry<V> entry) {
			super(entry.getShortKey(), entry.getValue());
		}
		
		@Override
		public void set(short key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Short2ObjectNavigableMap<V> {
		Short2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Short2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2ObjectNavigableMap<V> descendingMap() { return Short2ObjectMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2ObjectMap.Entry<V> firstEntry() { return Short2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2ObjectMap.Entry<V> lastEntry() { return Short2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ObjectNavigableMap<V> subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2ObjectNavigableMap<V> headMap(short toKey, boolean inclusive) { return Short2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2ObjectNavigableMap<V> tailMap(short fromKey, boolean inclusive) { return Short2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2ObjectNavigableMap<V> subMap(short fromKey, short toKey) { return Short2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ObjectNavigableMap<V> headMap(short toKey) { return Short2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ObjectNavigableMap<V> tailMap(short fromKey) { return Short2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public short lowerKey(short key) { return map.lowerKey(key); }
		@Override
		public short higherKey(short key) { return map.higherKey(key); }
		@Override
		public short floorKey(short key) { return map.floorKey(key); }
		@Override
		public short ceilingKey(short key) { return map.ceilingKey(key); }
		@Override
		public Short2ObjectMap.Entry<V> lowerEntry(short key) { return Short2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2ObjectMap.Entry<V> higherEntry(short key) { return Short2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2ObjectMap.Entry<V> floorEntry(short key) { return Short2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2ObjectMap.Entry<V> ceilingEntry(short key) { return Short2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Short2ObjectOrderedMap<V> {
		Short2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Short2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Short2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Short2ObjectSortedMap<V> {
		Short2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Short2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2ObjectSortedMap<V> subMap(short fromKey, short toKey) { return Short2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ObjectSortedMap<V> headMap(short toKey) { return Short2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ObjectSortedMap<V> tailMap(short fromKey) { return Short2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Short2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractShort2ObjectMap<V> implements Short2ObjectMap<V> {
		Short2ObjectMap<V> map;
		ObjectCollection<V> values;
		ShortSet keys;
		ObjectSet<Short2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Short2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(short key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(short key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(short key) { return map.get(key); }
		@Override
		public V getOrDefault(short key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.short2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Short2ObjectMap.Entry<V>>
	{
		ObjectSet<Short2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Short2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Short2ObjectMap.Entry<V>>() {
				ObjectIterator<Short2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2ObjectMap.Entry<V> next() { return Short2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Short2ObjectNavigableMap<V> {
		Short2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Short2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2ObjectNavigableMap<V> subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2ObjectNavigableMap<V> headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2ObjectNavigableMap<V> tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2ObjectNavigableMap<V> subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ObjectNavigableMap<V> headMap(short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ObjectNavigableMap<V> tailMap(short fromKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2ObjectMap.Entry<V> lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2ObjectMap.Entry<V> higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2ObjectMap.Entry<V> floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2ObjectMap.Entry<V> ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> headMap(Short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectNavigableMap<V> tailMap(Short fromKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(short e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public short getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(short e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public short getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Short lowerKey(Short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Short floorKey(Short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Short ceilingKey(Short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Short higherKey(Short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Short2ObjectMap.Entry<V> lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2ObjectMap.Entry<V> floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2ObjectMap.Entry<V> ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2ObjectMap.Entry<V> higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Short2ObjectOrderedMap<V> {
		Short2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Short2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(short key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(short key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Short2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Short2ObjectSortedMap<V> {
		Short2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Short2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2ObjectSortedMap<V> subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ObjectSortedMap<V> headMap(short toKey)  { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ObjectSortedMap<V> tailMap(short fromKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Short2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2ObjectSortedMap<V> subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectSortedMap<V> headMap(Short toKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ObjectSortedMap<V> tailMap(Short fromKey) { synchronized(mutex) { return Short2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractShort2ObjectMap<V> implements Short2ObjectMap<V> {
		Short2ObjectMap<V> map;
		ObjectCollection<V> values;
		ShortSet keys;
		ObjectSet<Short2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(short key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(short key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Short2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(short key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(short key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Short2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(ShortObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(short key, ShortObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(short key, Short2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(short key, ShortObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V merge(short key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Short2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(short key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2ObjectEntrySet(), mutex);
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
		public V put(Short key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Short key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Short key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Short key, Function<? super Short, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Short key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}