package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.UnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2ObjectMap<?, ?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T, V> Object2ObjectMap<T, V> empty() { 
		return (Object2ObjectMap<T, V>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T, V> ObjectIterator<Object2ObjectMap.Entry<T, V>> fastIterator(Object2ObjectMap<T, V> map) {
		ObjectSet<Object2ObjectMap.Entry<T, V>> entries = map.object2ObjectEntrySet();
		return entries instanceof Object2ObjectMap.FastEntrySet ? ((Object2ObjectMap.FastEntrySet<T, V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T, V> ObjectIterable<Object2ObjectMap.Entry<T, V>> fastIterable(Object2ObjectMap<T, V> map) {
		ObjectSet<Object2ObjectMap.Entry<T, V>> entries = map.object2ObjectEntrySet();
		return map instanceof Object2ObjectMap.FastEntrySet ? new ObjectIterable<Object2ObjectMap.Entry<T, V>>(){
			@Override
			public ObjectIterator<Object2ObjectMap.Entry<T, V>> iterator() { return ((Object2ObjectMap.FastEntrySet<T, V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2ObjectMap.Entry<T, V>> action) { ((Object2ObjectMap.FastEntrySet<T, V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T, V> void fastForEach(Object2ObjectMap<T, V> map, Consumer<Object2ObjectMap.Entry<T, V>> action) {
		ObjectSet<Object2ObjectMap.Entry<T, V>> entries = map.object2ObjectEntrySet();
		if(entries instanceof Object2ObjectMap.FastEntrySet) ((Object2ObjectMap.FastEntrySet<T, V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectMap<T, V> synchronize(Object2ObjectMap<T, V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectMap<T, V> synchronize(Object2ObjectMap<T, V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectSortedMap<T, V> synchronize(Object2ObjectSortedMap<T, V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectSortedMap<T, V> synchronize(Object2ObjectSortedMap<T, V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectOrderedMap<T, V> synchronize(Object2ObjectOrderedMap<T, V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectOrderedMap<T, V> synchronize(Object2ObjectOrderedMap<T, V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectNavigableMap<T, V> synchronize(Object2ObjectNavigableMap<T, V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T, V> Object2ObjectNavigableMap<T, V> synchronize(Object2ObjectNavigableMap<T, V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T, V> Object2ObjectMap<T, V> unmodifiable(Object2ObjectMap<T, V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T, V> Object2ObjectOrderedMap<T, V> unmodifiable(Object2ObjectOrderedMap<T, V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T, V> Object2ObjectSortedMap<T, V> unmodifiable(Object2ObjectSortedMap<T, V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T, V> Object2ObjectNavigableMap<T, V> unmodifiable(Object2ObjectNavigableMap<T, V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T, V> Object2ObjectMap.Entry<T, V> unmodifiable(Object2ObjectMap.Entry<T, V> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T, V> Object2ObjectMap.Entry<T, V> unmodifiable(Map.Entry<T, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T, V>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T, V> Object2ObjectMap<T, V> singleton(T key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<T, V> extends AbstractObject2ObjectMap<T, V> {
		final T key;
		final V value;
		ObjectSet<T> keySet;
		ObjectCollection<V> values;
		ObjectSet<Object2ObjectMap.Entry<T, V>> entrySet;
		
		SingletonMap(T key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public V remOrDefault(T key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public V getObject(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(T key, UnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<T, V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<T, V> extends AbstractObject2ObjectMap<T, V> {
		@Override
		public V put(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public V remOrDefault(T key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public V getObject(T key) { return getDefaultReturnValue(); }
		@Override
		public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(T key, UnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T, V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T, V> extends AbstractObject2ObjectMap.BasicEntry<T, V> {
		
		UnmodifyableEntry(Map.Entry<T, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2ObjectMap.Entry<T, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		@Override
		public void set(T key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T, V> extends UnmodifyableSortedMap<T, V> implements Object2ObjectNavigableMap<T, V> {
		Object2ObjectNavigableMap<T, V> map;
		
		UnmodifyableNavigableMap(Object2ObjectNavigableMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2ObjectNavigableMap<T, V> descendingMap() { return Object2ObjectMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2ObjectMap.Entry<T, V> firstEntry() { return Object2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2ObjectMap.Entry<T, V> lastEntry() { return Object2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2ObjectMap.Entry<T, V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ObjectMap.Entry<T, V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ObjectNavigableMap<T, V> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2ObjectNavigableMap<T, V> headMap(T toKey, boolean inclusive) { return Object2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2ObjectNavigableMap<T, V> tailMap(T fromKey, boolean inclusive) { return Object2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2ObjectNavigableMap<T, V> subMap(T fromKey, T toKey) { return Object2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ObjectNavigableMap<T, V> headMap(T toKey) { return Object2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ObjectNavigableMap<T, V> tailMap(T fromKey) { return Object2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2ObjectMap.Entry<T, V> lowerEntry(T key) { return Object2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2ObjectMap.Entry<T, V> higherEntry(T key) { return Object2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2ObjectMap.Entry<T, V> floorEntry(T key) { return Object2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2ObjectMap.Entry<T, V> ceilingEntry(T key) { return Object2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2ObjectNavigableMap<T, V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T, V> extends UnmodifyableMap<T, V> implements Object2ObjectOrderedMap<T, V> {
		Object2ObjectOrderedMap<T, V> map;
		
		UnmodifyableOrderedMap(Object2ObjectOrderedMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Object2ObjectOrderedMap<T, V> copy() { return map.copy(); }
		@Override
		public ObjectOrderedSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return (ObjectOrderedSet<T>)keys;
		}
				
		@Override
		public ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.object2ObjectEntrySet());
			return (ObjectOrderedSet<Object2ObjectMap.Entry<T, V>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T, V> extends UnmodifyableMap<T, V> implements Object2ObjectSortedMap<T, V> {
		Object2ObjectSortedMap<T, V> map;
		
		UnmodifyableSortedMap(Object2ObjectSortedMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2ObjectSortedMap<T, V> subMap(T fromKey, T toKey) { return Object2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2ObjectSortedMap<T, V> headMap(T toKey) { return Object2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2ObjectSortedMap<T, V> tailMap(T fromKey) { return Object2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public ObjectSortedSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Object2ObjectSortedMap<T, V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T, V> extends AbstractObject2ObjectMap<T, V> implements Object2ObjectMap<T, V> {
		Object2ObjectMap<T, V> map;
		ObjectCollection<V> values;
		ObjectSet<T> keys;
		ObjectSet<Object2ObjectMap.Entry<T, V>> entrySet;
		
		UnmodifyableMap(Object2ObjectMap<T, V> map) {
			this.map = map;
		}
		
		@Override
		public V put(T key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(T key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public V remOrDefault(T key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public V getObject(T key) {
			V type = map.getObject(key);
			return Objects.equals(type, map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(T key, UnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(ObjectObjectUnaryOperator<T, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(Object2ObjectMap<T, V> m) { throw new UnsupportedOperationException(); }
		@Override
		public Object2ObjectMap<T, V> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<T, V> extends UnmodifyableEntrySet<T, V> implements ObjectOrderedSet<Object2ObjectMap.Entry<T, V>>
	{
		ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> iterator(Object2ObjectMap.Entry<T, V> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Object2ObjectMap.Entry<T, V> first() { return set.first(); }
		@Override
		public Object2ObjectMap.Entry<T, V> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Object2ObjectMap.Entry<T, V> last() { return set.last(); }
		@Override
		public Object2ObjectMap.Entry<T, V> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T, V> extends ObjectSets.UnmodifiableSet<Object2ObjectMap.Entry<T, V>>
	{
		ObjectSet<Object2ObjectMap.Entry<T, V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2ObjectMap.Entry<T, V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2ObjectMap.Entry<T, V>> action) {
			s.forEach(T -> action.accept(Object2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2ObjectMap.Entry<T, V>> iterator() {
			return new ObjectIterator<Object2ObjectMap.Entry<T, V>>() {
				ObjectIterator<Object2ObjectMap.Entry<T, V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2ObjectMap.Entry<T, V> next() { return Object2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T, V> extends SynchronizedSortedMap<T, V> implements Object2ObjectNavigableMap<T, V> {
		Object2ObjectNavigableMap<T, V> map;
		
		SynchronizedNavigableMap(Object2ObjectNavigableMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2ObjectNavigableMap<T, V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2ObjectNavigableMap<T, V> descendingMap() { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Object2ObjectMap.Entry<T, V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2ObjectMap.Entry<T, V> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Object2ObjectMap.Entry<T, V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2ObjectMap.Entry<T, V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2ObjectNavigableMap<T, V> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2ObjectNavigableMap<T, V> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2ObjectNavigableMap<T, V> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2ObjectNavigableMap<T, V> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ObjectNavigableMap<T, V> headMap(T toKey) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ObjectNavigableMap<T, V> tailMap(T fromKey) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2ObjectMap.Entry<T, V> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2ObjectMap.Entry<T, V> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2ObjectMap.Entry<T, V> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2ObjectMap.Entry<T, V> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2ObjectNavigableMap<T, V> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T, V> extends SynchronizedMap<T, V> implements Object2ObjectOrderedMap<T, V> {
		Object2ObjectOrderedMap<T, V> map;
		
		SynchronizedOrderedMap(Object2ObjectOrderedMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2ObjectOrderedMap<T, V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(T key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(T key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Object2ObjectOrderedMap<T, V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectOrderedSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return (ObjectOrderedSet<T>)keys;
		}
		
		@Override
		public ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2ObjectEntrySet(), mutex);
			return (ObjectOrderedSet<Object2ObjectMap.Entry<T, V>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T, V> extends SynchronizedMap<T, V> implements Object2ObjectSortedMap<T, V> {
		Object2ObjectSortedMap<T, V> map;
		
		SynchronizedSortedMap(Object2ObjectSortedMap<T, V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2ObjectSortedMap<T, V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2ObjectSortedMap<T, V> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2ObjectSortedMap<T, V> headMap(T toKey)  { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2ObjectSortedMap<T, V> tailMap(T fromKey) { synchronized(mutex) { return Object2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public ObjectSortedSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Object2ObjectSortedMap<T, V> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T, V> extends AbstractObject2ObjectMap<T, V> implements Object2ObjectMap<T, V> {
		Object2ObjectMap<T, V> map;
		ObjectCollection<V> values;
		ObjectSet<T> keys;
		ObjectSet<Object2ObjectMap.Entry<T, V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2ObjectMap<T, V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2ObjectMap<T, V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2ObjectMap<T, V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(T key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(T key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2ObjectMap<T, V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Object2ObjectMap<T, V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public V getObject(T key) { synchronized(mutex) { return map.getObject(key); } }
		@Override
		public V rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public V remOrDefault(T key, V defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean replace(T key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(T key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Object2ObjectMap<T, V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(ObjectObjectUnaryOperator<T, V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(T key, UnaryOperator<T, V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) { synchronized(mutex) { return map.supplyIfAbsent(key, valueProvider); } }
		@Override
		public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public void forEach(ObjectObjectConsumer<T, V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Object2ObjectMap<T, V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2ObjectEntrySet(), mutex);
			return entrySet;
		}
		
	}
}