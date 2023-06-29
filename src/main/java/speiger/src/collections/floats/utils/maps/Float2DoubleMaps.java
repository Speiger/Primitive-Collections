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
import speiger.src.collections.floats.functions.consumer.FloatDoubleConsumer;
import speiger.src.collections.floats.functions.function.Float2DoubleFunction;
import speiger.src.collections.floats.functions.function.FloatDoubleUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.utils.DoubleCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2DoubleMap.Entry> fastIterator(Float2DoubleMap map) {
		ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
		return entries instanceof Float2DoubleMap.FastEntrySet ? ((Float2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2DoubleMap.Entry> fastIterable(Float2DoubleMap map) {
		ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
		return map instanceof Float2DoubleMap.FastEntrySet ? new ObjectIterable<Float2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Float2DoubleMap.Entry> iterator() { return ((Float2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2DoubleMap.Entry> action) { ((Float2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2DoubleMap map, Consumer<Float2DoubleMap.Entry> action) {
		ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
		if(entries instanceof Float2DoubleMap.FastEntrySet) ((Float2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleMap synchronize(Float2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleMap synchronize(Float2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleSortedMap synchronize(Float2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleSortedMap synchronize(Float2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleOrderedMap synchronize(Float2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleOrderedMap synchronize(Float2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleNavigableMap synchronize(Float2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2DoubleNavigableMap synchronize(Float2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2DoubleMap unmodifiable(Float2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2DoubleOrderedMap unmodifiable(Float2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2DoubleSortedMap unmodifiable(Float2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2DoubleNavigableMap unmodifiable(Float2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2DoubleMap.Entry unmodifiable(Float2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2DoubleMap.Entry unmodifiable(Map.Entry<Float, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2DoubleMap singleton(float key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2DoubleMap {
		final float key;
		final double value;
		FloatSet keySet;
		DoubleCollection values;
		ObjectSet<Float2DoubleMap.Entry> entrySet;
		
		SingletonMap(float key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(float key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(float key, double defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public double computeDouble(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(float key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Float2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2DoubleMap.Entry> float2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2DoubleMap {
		@Override
		public double put(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(float key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(float key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(float key, double defaultValue) { return defaultValue; }
		@Override
		public double computeDouble(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(float key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Float2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Float2DoubleMap.Entry> float2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2DoubleMap.Entry entry) {
			super(entry.getFloatKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(float key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2DoubleNavigableMap {
		Float2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Float2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2DoubleNavigableMap descendingMap() { return Float2DoubleMaps.unmodifiable(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2DoubleMap.Entry firstEntry() { return Float2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2DoubleMap.Entry lastEntry() { return Float2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2DoubleNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2DoubleNavigableMap headMap(float toKey, boolean inclusive) { return Float2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2DoubleNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2DoubleNavigableMap subMap(float fromKey, float toKey) { return Float2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2DoubleNavigableMap headMap(float toKey) { return Float2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2DoubleNavigableMap tailMap(float fromKey) { return Float2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2DoubleMap.Entry lowerEntry(float key) { return Float2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2DoubleMap.Entry higherEntry(float key) { return Float2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2DoubleMap.Entry floorEntry(float key) { return Float2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2DoubleMap.Entry ceilingEntry(float key) { return Float2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2DoubleNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2DoubleOrderedMap {
		Float2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Float2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Float2DoubleOrderedMap copy() { return map.copy(); }
		@Override
		public FloatOrderedSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return (FloatOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Float2DoubleMap.Entry> float2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.float2DoubleEntrySet());
			return (ObjectOrderedSet<Float2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2DoubleSortedMap {
		Float2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Float2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2DoubleSortedMap subMap(float fromKey, float toKey) { return Float2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2DoubleSortedMap headMap(float toKey) { return Float2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2DoubleSortedMap tailMap(float fromKey) { return Float2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Float2DoubleSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2DoubleMap implements Float2DoubleMap {
		Float2DoubleMap map;
		DoubleCollection values;
		FloatSet keys;
		ObjectSet<Float2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Float2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(float key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(float key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(float key) {
			double type = map.get(key);
			return Double.doubleToLongBits(type) == Double.doubleToLongBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public double getOrDefault(float key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double computeDouble(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(float key, Float2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(float key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(float key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Float2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(FloatDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(Float2DoubleMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Float2DoubleMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2DoubleMap.Entry> float2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Float2DoubleMap.Entry>
	{
		ObjectOrderedSet<Float2DoubleMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Float2DoubleMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Float2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Float2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Float2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Float2DoubleMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Float2DoubleMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Float2DoubleMap.Entry> iterator(Float2DoubleMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Float2DoubleMap.Entry first() { return set.first(); }
		@Override
		public Float2DoubleMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Float2DoubleMap.Entry last() { return set.last(); }
		@Override
		public Float2DoubleMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2DoubleMap.Entry>
	{
		ObjectSet<Float2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Float2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Float2DoubleMap.Entry>() {
				ObjectIterator<Float2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2DoubleMap.Entry next() { return Float2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2DoubleNavigableMap {
		Float2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Float2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public FloatNavigableSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Float2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Float2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2DoubleNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2DoubleNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2DoubleNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2DoubleNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2DoubleNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2DoubleNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2DoubleMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2DoubleMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2DoubleMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2DoubleMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2DoubleNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2DoubleMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2DoubleMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2DoubleMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2DoubleMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2DoubleOrderedMap {
		Float2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Float2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(float key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(float key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Float2DoubleOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatOrderedSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return (FloatOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Float2DoubleMap.Entry> float2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2DoubleEntrySet(), mutex);
			return (ObjectOrderedSet<Float2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2DoubleSortedMap {
		Float2DoubleSortedMap map;
		
		SynchronizedSortedMap(Float2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2DoubleSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2DoubleSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2DoubleSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Float2DoubleSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2DoubleSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2DoubleSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2DoubleMap implements Float2DoubleMap {
		Float2DoubleMap map;
		DoubleCollection values;
		FloatSet keys;
		ObjectSet<Float2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(float key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(float key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(float key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(float key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(float key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(float key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Float2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(FloatDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(float key, FloatDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(float key, Float2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(float key, FloatDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsent(float key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsent(key, valueProvider); } }
		@Override
		public double computeDoubleNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsentNonDefault(float key, Float2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresentNonDefault(float key, FloatDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsentNonDefault(float key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public double mergeDouble(float key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Float2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(float key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Float2DoubleMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2DoubleMap.Entry> float2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2DoubleEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Double get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Double getOrDefault(Object key, Double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Double put(Float key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Float key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Float key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Float key, Function<? super Float, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Float key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}