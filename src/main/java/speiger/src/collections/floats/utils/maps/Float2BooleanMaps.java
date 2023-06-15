package speiger.src.collections.floats.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.consumer.FloatBooleanConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatBooleanUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.utils.BooleanCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2BooleanMap.Entry> fastIterator(Float2BooleanMap map) {
		ObjectSet<Float2BooleanMap.Entry> entries = map.float2BooleanEntrySet();
		return entries instanceof Float2BooleanMap.FastEntrySet ? ((Float2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2BooleanMap.Entry> fastIterable(Float2BooleanMap map) {
		ObjectSet<Float2BooleanMap.Entry> entries = map.float2BooleanEntrySet();
		return map instanceof Float2BooleanMap.FastEntrySet ? new ObjectIterable<Float2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Float2BooleanMap.Entry> iterator() { return ((Float2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2BooleanMap.Entry> action) { ((Float2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2BooleanMap map, Consumer<Float2BooleanMap.Entry> action) {
		ObjectSet<Float2BooleanMap.Entry> entries = map.float2BooleanEntrySet();
		if(entries instanceof Float2BooleanMap.FastEntrySet) ((Float2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanMap synchronize(Float2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanMap synchronize(Float2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanSortedMap synchronize(Float2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanSortedMap synchronize(Float2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanOrderedMap synchronize(Float2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanOrderedMap synchronize(Float2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanNavigableMap synchronize(Float2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2BooleanNavigableMap synchronize(Float2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2BooleanMap unmodifiable(Float2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2BooleanOrderedMap unmodifiable(Float2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2BooleanSortedMap unmodifiable(Float2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2BooleanNavigableMap unmodifiable(Float2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2BooleanMap.Entry unmodifiable(Float2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2BooleanMap.Entry unmodifiable(Map.Entry<Float, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2BooleanMap singleton(float key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2BooleanMap {
		final float key;
		final boolean value;
		FloatSet keySet;
		BooleanCollection values;
		ObjectSet<Float2BooleanMap.Entry> entrySet;
		
		SingletonMap(float key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(float key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(float key, boolean defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2BooleanMap.Entry> float2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2BooleanMap {
		@Override
		public boolean put(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(float key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(float key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(float key, boolean defaultValue) { return defaultValue; }
		@Override
		public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Float2BooleanMap.Entry> float2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2BooleanMap.Entry entry) {
			super(entry.getFloatKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(float key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2BooleanNavigableMap {
		Float2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Float2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2BooleanNavigableMap descendingMap() { return Float2BooleanMaps.unmodifiable(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2BooleanMap.Entry firstEntry() { return Float2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2BooleanMap.Entry lastEntry() { return Float2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2BooleanNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2BooleanNavigableMap headMap(float toKey, boolean inclusive) { return Float2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2BooleanNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2BooleanNavigableMap subMap(float fromKey, float toKey) { return Float2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2BooleanNavigableMap headMap(float toKey) { return Float2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2BooleanNavigableMap tailMap(float fromKey) { return Float2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(float e) { throw new UnsupportedOperationException(); }
		@Override
		public float getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(float e) { throw new UnsupportedOperationException(); }
		@Override
		public float getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public float lowerKey(float key) { return map.lowerKey(key); }
		@Override
		public float higherKey(float key) { return map.higherKey(key); }
		@Override
		public float floorKey(float key) { return map.floorKey(key); }
		@Override
		public float ceilingKey(float key) { return map.ceilingKey(key); }
		@Override
		public Float2BooleanMap.Entry lowerEntry(float key) { return Float2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2BooleanMap.Entry higherEntry(float key) { return Float2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2BooleanMap.Entry floorEntry(float key) { return Float2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2BooleanMap.Entry ceilingEntry(float key) { return Float2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2BooleanNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2BooleanOrderedMap {
		Float2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Float2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Float2BooleanOrderedMap copy() { return map.copy(); }
		@Override
		public FloatOrderedSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return (FloatOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.float2BooleanEntrySet());
			return (ObjectOrderedSet<Float2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2BooleanSortedMap {
		Float2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Float2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2BooleanSortedMap subMap(float fromKey, float toKey) { return Float2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2BooleanSortedMap headMap(float toKey) { return Float2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2BooleanSortedMap tailMap(float fromKey) { return Float2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public FloatSortedSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Float2BooleanSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2BooleanMap implements Float2BooleanMap {
		Float2BooleanMap map;
		BooleanCollection values;
		FloatSet keys;
		ObjectSet<Float2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Float2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(float key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(float key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(float key) {
			boolean type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public boolean getOrDefault(float key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(FloatBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(Float2BooleanMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Float2BooleanMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Float2BooleanMap.Entry>
	{
		ObjectOrderedSet<Float2BooleanMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Float2BooleanMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Float2BooleanMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> iterator(Float2BooleanMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Float2BooleanMap.Entry first() { return set.first(); }
		@Override
		public Float2BooleanMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Float2BooleanMap.Entry last() { return set.last(); }
		@Override
		public Float2BooleanMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2BooleanMap.Entry>
	{
		ObjectSet<Float2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Float2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Float2BooleanMap.Entry>() {
				ObjectIterator<Float2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2BooleanMap.Entry next() { return Float2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2BooleanNavigableMap {
		Float2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Float2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public FloatNavigableSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Float2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Float2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2BooleanNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2BooleanNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2BooleanNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2BooleanNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2BooleanNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2BooleanNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2BooleanMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2BooleanMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2BooleanMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2BooleanMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2BooleanNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(float e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public float getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(float e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public float getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Float lowerKey(Float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Float floorKey(Float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Float ceilingKey(Float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Float higherKey(Float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Float2BooleanMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2BooleanMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2BooleanMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2BooleanMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2BooleanOrderedMap {
		Float2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Float2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(float key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(float key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Float2BooleanOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatOrderedSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return (FloatOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2BooleanEntrySet(), mutex);
			return (ObjectOrderedSet<Float2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2BooleanSortedMap {
		Float2BooleanSortedMap map;
		
		SynchronizedSortedMap(Float2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2BooleanSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2BooleanSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2BooleanSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public FloatSortedSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Float2BooleanSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2BooleanSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2BooleanSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2BooleanMap implements Float2BooleanMap {
		Float2BooleanMap map;
		BooleanCollection values;
		FloatSet keys;
		ObjectSet<Float2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(float key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(float key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Float2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(float key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(float key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Float2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(FloatBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsent(key, valueProvider); } }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(float key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Float2BooleanMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2BooleanEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Boolean get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Boolean getOrDefault(Object key, Boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Boolean put(Float key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Float key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Float key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Float key, Function<? super Float, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Float key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}