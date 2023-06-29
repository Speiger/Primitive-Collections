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
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.objects.functions.function.ToDoubleFunction;
import speiger.src.collections.objects.functions.function.ObjectDoubleUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.utils.DoubleCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2DoubleMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2DoubleMap<T> empty() { 
		return (Object2DoubleMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2DoubleMap.Entry<T>> fastIterator(Object2DoubleMap<T> map) {
		ObjectSet<Object2DoubleMap.Entry<T>> entries = map.object2DoubleEntrySet();
		return entries instanceof Object2DoubleMap.FastEntrySet ? ((Object2DoubleMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2DoubleMap.Entry<T>> fastIterable(Object2DoubleMap<T> map) {
		ObjectSet<Object2DoubleMap.Entry<T>> entries = map.object2DoubleEntrySet();
		return map instanceof Object2DoubleMap.FastEntrySet ? new ObjectIterable<Object2DoubleMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2DoubleMap.Entry<T>> iterator() { return ((Object2DoubleMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2DoubleMap.Entry<T>> action) { ((Object2DoubleMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2DoubleMap<T> map, Consumer<Object2DoubleMap.Entry<T>> action) {
		ObjectSet<Object2DoubleMap.Entry<T>> entries = map.object2DoubleEntrySet();
		if(entries instanceof Object2DoubleMap.FastEntrySet) ((Object2DoubleMap.FastEntrySet<T>)entries).fastForEach(action); 
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
	public static <T> Object2DoubleMap<T> synchronize(Object2DoubleMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleMap<T> synchronize(Object2DoubleMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleSortedMap<T> synchronize(Object2DoubleSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleSortedMap<T> synchronize(Object2DoubleSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleOrderedMap<T> synchronize(Object2DoubleOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleOrderedMap<T> synchronize(Object2DoubleOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleNavigableMap<T> synchronize(Object2DoubleNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2DoubleNavigableMap<T> synchronize(Object2DoubleNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2DoubleMap<T> unmodifiable(Object2DoubleMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2DoubleOrderedMap<T> unmodifiable(Object2DoubleOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2DoubleSortedMap<T> unmodifiable(Object2DoubleSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2DoubleNavigableMap<T> unmodifiable(Object2DoubleNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2DoubleMap.Entry<T> unmodifiable(Object2DoubleMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2DoubleMap.Entry<T> unmodifiable(Map.Entry<T, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2DoubleMap<T> singleton(T key, double value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2DoubleMap<T> {
		final T key;
		final double value;
		ObjectSet<T> keySet;
		DoubleCollection values;
		ObjectSet<Object2DoubleMap.Entry<T>> entrySet;
		
		SingletonMap(T key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public double remOrDefault(T key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double getDouble(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(T key, double defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2DoubleMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2DoubleMap<T> {
		@Override
		public double put(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public double remOrDefault(T key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double getDouble(T key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(T key, double defaultValue) { return defaultValue; }
		@Override
		public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2DoubleMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2DoubleMap.Entry<T> entry) {
			super(entry.getKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(T key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2DoubleNavigableMap<T> {
		Object2DoubleNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2DoubleNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2DoubleNavigableMap<T> descendingMap() { return Object2DoubleMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2DoubleMap.Entry<T> firstEntry() { return Object2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2DoubleMap.Entry<T> lastEntry() { return Object2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2DoubleMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2DoubleMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2DoubleNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2DoubleNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2DoubleNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2DoubleNavigableMap<T> subMap(T fromKey, T toKey) { return Object2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2DoubleNavigableMap<T> headMap(T toKey) { return Object2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2DoubleNavigableMap<T> tailMap(T fromKey) { return Object2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2DoubleMap.Entry<T> lowerEntry(T key) { return Object2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2DoubleMap.Entry<T> higherEntry(T key) { return Object2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2DoubleMap.Entry<T> floorEntry(T key) { return Object2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2DoubleMap.Entry<T> ceilingEntry(T key) { return Object2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2DoubleNavigableMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2DoubleOrderedMap<T> {
		Object2DoubleOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2DoubleOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Object2DoubleOrderedMap<T> copy() { return map.copy(); }
		@Override
		public ObjectOrderedSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return (ObjectOrderedSet<T>)keys;
		}
				
		@Override
		public ObjectOrderedSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.object2DoubleEntrySet());
			return (ObjectOrderedSet<Object2DoubleMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2DoubleSortedMap<T> {
		Object2DoubleSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2DoubleSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2DoubleSortedMap<T> subMap(T fromKey, T toKey) { return Object2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2DoubleSortedMap<T> headMap(T toKey) { return Object2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2DoubleSortedMap<T> tailMap(T fromKey) { return Object2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Object2DoubleSortedMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2DoubleMap<T> implements Object2DoubleMap<T> {
		Object2DoubleMap<T> map;
		DoubleCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2DoubleMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2DoubleMap<T> map) {
			this.map = map;
		}
		
		@Override
		public double put(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(T key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public double remOrDefault(T key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double getDouble(T key) {
			double type = map.getDouble(key);
			return Double.doubleToLongBits(type) == Double.doubleToLongBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public double getOrDefault(T key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(T key, ToDoubleFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(T key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(ObjectDoubleUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(Object2DoubleMap<T> m) { throw new UnsupportedOperationException(); }
		@Override
		public Object2DoubleMap<T> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<T> extends UnmodifyableEntrySet<T> implements ObjectOrderedSet<Object2DoubleMap.Entry<T>>
	{
		ObjectOrderedSet<Object2DoubleMap.Entry<T>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Object2DoubleMap.Entry<T>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Object2DoubleMap.Entry<T>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator(Object2DoubleMap.Entry<T> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Object2DoubleMap.Entry<T> first() { return set.first(); }
		@Override
		public Object2DoubleMap.Entry<T> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Object2DoubleMap.Entry<T> last() { return set.last(); }
		@Override
		public Object2DoubleMap.Entry<T> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2DoubleMap.Entry<T>>
	{
		ObjectSet<Object2DoubleMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2DoubleMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2DoubleMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2DoubleMap.Entry<T>>() {
				ObjectIterator<Object2DoubleMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2DoubleMap.Entry<T> next() { return Object2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2DoubleNavigableMap<T> {
		Object2DoubleNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2DoubleNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2DoubleNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2DoubleNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Object2DoubleMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2DoubleMap.Entry<T> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Object2DoubleMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2DoubleMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2DoubleNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2DoubleNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2DoubleNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2DoubleNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2DoubleNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2DoubleNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2DoubleMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2DoubleMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2DoubleMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2DoubleMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2DoubleNavigableMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2DoubleOrderedMap<T> {
		Object2DoubleOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2DoubleOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2DoubleOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(T key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(T key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Object2DoubleOrderedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectOrderedSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return (ObjectOrderedSet<T>)keys;
		}
		
		@Override
		public ObjectOrderedSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2DoubleEntrySet(), mutex);
			return (ObjectOrderedSet<Object2DoubleMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2DoubleSortedMap<T> {
		Object2DoubleSortedMap<T> map;
		
		SynchronizedSortedMap(Object2DoubleSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2DoubleSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2DoubleSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2DoubleSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2DoubleSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Object2DoubleSortedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2DoubleMap<T> implements Object2DoubleMap<T> {
		Object2DoubleMap<T> map;
		DoubleCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2DoubleMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2DoubleMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2DoubleMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2DoubleMap<T> setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(T key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(T key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2DoubleMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(T key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(T key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2DoubleMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2DoubleMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double getDouble(T key) { synchronized(mutex) { return map.getDouble(key); } }
		@Override
		public double rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public double remOrDefault(T key, double defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(T key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Object2DoubleMap<T> m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(ObjectDoubleUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(T key, ToDoubleFunction<T> mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsent(key, valueProvider); } }
		@Override
		public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeDoubleNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsentNonDefault(T key, ToDoubleFunction<T> mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresentNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsentNonDefault(T key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(T key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectDoubleConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Object2DoubleMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2DoubleEntrySet(), mutex);
			return entrySet;
		}
		
	}
}