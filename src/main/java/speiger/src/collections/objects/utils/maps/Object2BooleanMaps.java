package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.objects.functions.function.ObjectBooleanUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.utils.BooleanCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2BooleanMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2BooleanMap<T> empty() { 
		return (Object2BooleanMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2BooleanMap.Entry<T>> fastIterator(Object2BooleanMap<T> map) {
		ObjectSet<Object2BooleanMap.Entry<T>> entries = map.object2BooleanEntrySet();
		return entries instanceof Object2BooleanMap.FastEntrySet ? ((Object2BooleanMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2BooleanMap.Entry<T>> fastIterable(Object2BooleanMap<T> map) {
		ObjectSet<Object2BooleanMap.Entry<T>> entries = map.object2BooleanEntrySet();
		return map instanceof Object2BooleanMap.FastEntrySet ? new ObjectIterable<Object2BooleanMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2BooleanMap.Entry<T>> iterator() { return ((Object2BooleanMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2BooleanMap.Entry<T>> action) { ((Object2BooleanMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2BooleanMap<T> map, Consumer<Object2BooleanMap.Entry<T>> action) {
		ObjectSet<Object2BooleanMap.Entry<T>> entries = map.object2BooleanEntrySet();
		if(entries instanceof Object2BooleanMap.FastEntrySet) ((Object2BooleanMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanMap<T> synchronize(Object2BooleanMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanMap<T> synchronize(Object2BooleanMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanSortedMap<T> synchronize(Object2BooleanSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanSortedMap<T> synchronize(Object2BooleanSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanOrderedMap<T> synchronize(Object2BooleanOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanOrderedMap<T> synchronize(Object2BooleanOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanNavigableMap<T> synchronize(Object2BooleanNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2BooleanNavigableMap<T> synchronize(Object2BooleanNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2BooleanMap<T> unmodifiable(Object2BooleanMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2BooleanOrderedMap<T> unmodifiable(Object2BooleanOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2BooleanSortedMap<T> unmodifiable(Object2BooleanSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2BooleanNavigableMap<T> unmodifiable(Object2BooleanNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2BooleanMap.Entry<T> unmodifiable(Object2BooleanMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2BooleanMap.Entry<T> unmodifiable(Map.Entry<T, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2BooleanMap<T> singleton(T key, boolean value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2BooleanMap<T> {
		final T key;
		final boolean value;
		ObjectSet<T> keySet;
		BooleanCollection values;
		ObjectSet<Object2BooleanMap.Entry<T>> entrySet;
		
		SingletonMap(T key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remOrDefault(T key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getBoolean(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(T key, boolean defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2BooleanMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2BooleanMap<T> {
		@Override
		public boolean put(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remOrDefault(T key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getBoolean(T key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(T key, boolean defaultValue) { return defaultValue; }
		@Override
		public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2BooleanMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2BooleanMap.Entry<T> entry) {
			super(entry.getKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(T key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2BooleanNavigableMap<T> {
		Object2BooleanNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2BooleanNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2BooleanNavigableMap<T> descendingMap() { return Object2BooleanMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2BooleanMap.Entry<T> firstEntry() { return Object2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2BooleanMap.Entry<T> lastEntry() { return Object2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2BooleanMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2BooleanMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2BooleanNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2BooleanNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2BooleanNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2BooleanNavigableMap<T> subMap(T fromKey, T toKey) { return Object2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2BooleanNavigableMap<T> headMap(T toKey) { return Object2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2BooleanNavigableMap<T> tailMap(T fromKey) { return Object2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2BooleanMap.Entry<T> lowerEntry(T key) { return Object2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2BooleanMap.Entry<T> higherEntry(T key) { return Object2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2BooleanMap.Entry<T> floorEntry(T key) { return Object2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2BooleanMap.Entry<T> ceilingEntry(T key) { return Object2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2BooleanNavigableMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2BooleanOrderedMap<T> {
		Object2BooleanOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2BooleanOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Object2BooleanOrderedMap<T> copy() { return map.copy(); }
		@Override
		public ObjectOrderedSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return (ObjectOrderedSet<T>)keys;
		}
				
		@Override
		public ObjectOrderedSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.object2BooleanEntrySet());
			return (ObjectOrderedSet<Object2BooleanMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2BooleanSortedMap<T> {
		Object2BooleanSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2BooleanSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2BooleanSortedMap<T> subMap(T fromKey, T toKey) { return Object2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2BooleanSortedMap<T> headMap(T toKey) { return Object2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2BooleanSortedMap<T> tailMap(T fromKey) { return Object2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Object2BooleanSortedMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2BooleanMap<T> implements Object2BooleanMap<T> {
		Object2BooleanMap<T> map;
		BooleanCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2BooleanMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2BooleanMap<T> map) {
			this.map = map;
		}
		
		@Override
		public boolean put(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(T key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remOrDefault(T key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getBoolean(T key) {
			boolean type = map.getBoolean(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public boolean getOrDefault(T key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(ObjectBooleanUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(Object2BooleanMap<T> m) { throw new UnsupportedOperationException(); }
		@Override
		public Object2BooleanMap<T> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<T> extends UnmodifyableEntrySet<T> implements ObjectOrderedSet<Object2BooleanMap.Entry<T>>
	{
		ObjectOrderedSet<Object2BooleanMap.Entry<T>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Object2BooleanMap.Entry<T>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Object2BooleanMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2BooleanMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Object2BooleanMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Object2BooleanMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Object2BooleanMap.Entry<T>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Object2BooleanMap.Entry<T>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Object2BooleanMap.Entry<T>> iterator(Object2BooleanMap.Entry<T> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Object2BooleanMap.Entry<T> first() { return set.first(); }
		@Override
		public Object2BooleanMap.Entry<T> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Object2BooleanMap.Entry<T> last() { return set.last(); }
		@Override
		public Object2BooleanMap.Entry<T> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2BooleanMap.Entry<T>>
	{
		ObjectSet<Object2BooleanMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2BooleanMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2BooleanMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2BooleanMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2BooleanMap.Entry<T>>() {
				ObjectIterator<Object2BooleanMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2BooleanMap.Entry<T> next() { return Object2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2BooleanNavigableMap<T> {
		Object2BooleanNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2BooleanNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2BooleanNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2BooleanNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Object2BooleanMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2BooleanMap.Entry<T> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Object2BooleanMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2BooleanMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2BooleanNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2BooleanNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2BooleanNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2BooleanNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2BooleanNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2BooleanNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2BooleanMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2BooleanMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2BooleanMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2BooleanMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2BooleanNavigableMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2BooleanOrderedMap<T> {
		Object2BooleanOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2BooleanOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2BooleanOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(T key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(T key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Object2BooleanOrderedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectOrderedSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return (ObjectOrderedSet<T>)keys;
		}
		
		@Override
		public ObjectOrderedSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2BooleanEntrySet(), mutex);
			return (ObjectOrderedSet<Object2BooleanMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2BooleanSortedMap<T> {
		Object2BooleanSortedMap<T> map;
		
		SynchronizedSortedMap(Object2BooleanSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2BooleanSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2BooleanSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2BooleanSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2BooleanSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Object2BooleanSortedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2BooleanMap<T> implements Object2BooleanMap<T> {
		Object2BooleanMap<T> map;
		BooleanCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2BooleanMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2BooleanMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2BooleanMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2BooleanMap<T> setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(T key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(T key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2BooleanMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Object2BooleanMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean getBoolean(T key) { synchronized(mutex) { return map.getBoolean(key); } }
		@Override
		public boolean rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public boolean remOrDefault(T key, boolean defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(T key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Object2BooleanMap<T> m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(ObjectBooleanUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeBooleanNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsent(key, valueProvider); } }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(T key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectBooleanConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Object2BooleanMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2BooleanEntrySet(), mutex);
			return entrySet;
		}
		
	}
}