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
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.objects.functions.function.ToFloatFunction;
import speiger.src.collections.objects.functions.function.ObjectFloatUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.utils.FloatCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2FloatMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2FloatMap<T> empty() { 
		return (Object2FloatMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2FloatMap.Entry<T>> fastIterator(Object2FloatMap<T> map) {
		ObjectSet<Object2FloatMap.Entry<T>> entries = map.object2FloatEntrySet();
		return entries instanceof Object2FloatMap.FastEntrySet ? ((Object2FloatMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2FloatMap.Entry<T>> fastIterable(Object2FloatMap<T> map) {
		ObjectSet<Object2FloatMap.Entry<T>> entries = map.object2FloatEntrySet();
		return map instanceof Object2FloatMap.FastEntrySet ? new ObjectIterable<Object2FloatMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2FloatMap.Entry<T>> iterator() { return ((Object2FloatMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2FloatMap.Entry<T>> action) { ((Object2FloatMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2FloatMap<T> map, Consumer<Object2FloatMap.Entry<T>> action) {
		ObjectSet<Object2FloatMap.Entry<T>> entries = map.object2FloatEntrySet();
		if(entries instanceof Object2FloatMap.FastEntrySet) ((Object2FloatMap.FastEntrySet<T>)entries).fastForEach(action); 
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
	public static <T> Object2FloatMap<T> synchronize(Object2FloatMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatMap<T> synchronize(Object2FloatMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatSortedMap<T> synchronize(Object2FloatSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatSortedMap<T> synchronize(Object2FloatSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatOrderedMap<T> synchronize(Object2FloatOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatOrderedMap<T> synchronize(Object2FloatOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatNavigableMap<T> synchronize(Object2FloatNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2FloatNavigableMap<T> synchronize(Object2FloatNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2FloatMap<T> unmodifiable(Object2FloatMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2FloatOrderedMap<T> unmodifiable(Object2FloatOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2FloatSortedMap<T> unmodifiable(Object2FloatSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2FloatNavigableMap<T> unmodifiable(Object2FloatNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2FloatMap.Entry<T> unmodifiable(Object2FloatMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2FloatMap.Entry<T> unmodifiable(Map.Entry<T, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2FloatMap<T> singleton(T key, float value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2FloatMap<T> {
		final T key;
		final float value;
		ObjectSet<T> keySet;
		FloatCollection values;
		ObjectSet<Object2FloatMap.Entry<T>> entrySet;
		
		SingletonMap(T key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public float remOrDefault(T key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float getFloat(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(T key, float defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2FloatMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2FloatMap<T> {
		@Override
		public float put(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public float remOrDefault(T key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float getFloat(T key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(T key, float defaultValue) { return defaultValue; }
		@Override
		public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2FloatMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2FloatMap.Entry<T> entry) {
			super(entry.getKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(T key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2FloatNavigableMap<T> {
		Object2FloatNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2FloatNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2FloatNavigableMap<T> descendingMap() { return Object2FloatMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2FloatMap.Entry<T> firstEntry() { return Object2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2FloatMap.Entry<T> lastEntry() { return Object2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2FloatMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2FloatMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2FloatNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2FloatNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2FloatNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2FloatNavigableMap<T> subMap(T fromKey, T toKey) { return Object2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2FloatNavigableMap<T> headMap(T toKey) { return Object2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2FloatNavigableMap<T> tailMap(T fromKey) { return Object2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2FloatMap.Entry<T> lowerEntry(T key) { return Object2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2FloatMap.Entry<T> higherEntry(T key) { return Object2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2FloatMap.Entry<T> floorEntry(T key) { return Object2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2FloatMap.Entry<T> ceilingEntry(T key) { return Object2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2FloatNavigableMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2FloatOrderedMap<T> {
		Object2FloatOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2FloatOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Object2FloatOrderedMap<T> copy() { return map.copy(); }
		@Override
		public ObjectOrderedSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return (ObjectOrderedSet<T>)keys;
		}
				
		@Override
		public ObjectOrderedSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.object2FloatEntrySet());
			return (ObjectOrderedSet<Object2FloatMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2FloatSortedMap<T> {
		Object2FloatSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2FloatSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2FloatSortedMap<T> subMap(T fromKey, T toKey) { return Object2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2FloatSortedMap<T> headMap(T toKey) { return Object2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2FloatSortedMap<T> tailMap(T fromKey) { return Object2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Object2FloatSortedMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2FloatMap<T> implements Object2FloatMap<T> {
		Object2FloatMap<T> map;
		FloatCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2FloatMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2FloatMap<T> map) {
			this.map = map;
		}
		
		@Override
		public float put(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(T key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public float remOrDefault(T key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float getFloat(T key) {
			float type = map.getFloat(key);
			return Float.floatToIntBits(type) == Float.floatToIntBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public float getOrDefault(T key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(T key, ToFloatFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(T key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(ObjectFloatUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(Object2FloatMap<T> m) { throw new UnsupportedOperationException(); }
		@Override
		public Object2FloatMap<T> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<T> extends UnmodifyableEntrySet<T> implements ObjectOrderedSet<Object2FloatMap.Entry<T>>
	{
		ObjectOrderedSet<Object2FloatMap.Entry<T>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Object2FloatMap.Entry<T>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Object2FloatMap.Entry<T>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> iterator(Object2FloatMap.Entry<T> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Object2FloatMap.Entry<T> first() { return set.first(); }
		@Override
		public Object2FloatMap.Entry<T> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Object2FloatMap.Entry<T> last() { return set.last(); }
		@Override
		public Object2FloatMap.Entry<T> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2FloatMap.Entry<T>>
	{
		ObjectSet<Object2FloatMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2FloatMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2FloatMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2FloatMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2FloatMap.Entry<T>>() {
				ObjectIterator<Object2FloatMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2FloatMap.Entry<T> next() { return Object2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2FloatNavigableMap<T> {
		Object2FloatNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2FloatNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2FloatNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2FloatNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Object2FloatMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2FloatMap.Entry<T> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Object2FloatMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2FloatMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2FloatNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2FloatNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2FloatNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2FloatNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2FloatNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2FloatNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2FloatMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2FloatMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2FloatMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2FloatMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2FloatNavigableMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2FloatOrderedMap<T> {
		Object2FloatOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2FloatOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2FloatOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(T key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(T key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Object2FloatOrderedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectOrderedSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return (ObjectOrderedSet<T>)keys;
		}
		
		@Override
		public ObjectOrderedSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2FloatEntrySet(), mutex);
			return (ObjectOrderedSet<Object2FloatMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2FloatSortedMap<T> {
		Object2FloatSortedMap<T> map;
		
		SynchronizedSortedMap(Object2FloatSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2FloatSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2FloatSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2FloatSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2FloatSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Object2FloatSortedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2FloatMap<T> implements Object2FloatMap<T> {
		Object2FloatMap<T> map;
		FloatCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2FloatMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2FloatMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2FloatMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2FloatMap<T> setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(T key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(T key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2FloatMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(T key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(T key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2FloatMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2FloatMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float getFloat(T key) { synchronized(mutex) { return map.getFloat(key); } }
		@Override
		public float rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public float remOrDefault(T key, float defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(T key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Object2FloatMap<T> m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(ObjectFloatUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(T key, ToFloatFunction<T> mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsent(key, valueProvider); } }
		@Override
		public float computeFloatNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeFloatNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsentNonDefault(T key, ToFloatFunction<T> mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresentNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsentNonDefault(T key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(T key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectFloatConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Object2FloatMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2FloatEntrySet(), mutex);
			return entrySet;
		}
		
	}
}