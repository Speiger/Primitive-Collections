package speiger.src.collections.doubles.utils.maps;

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
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.consumer.DoubleFloatConsumer;
import speiger.src.collections.doubles.functions.function.Double2FloatFunction;
import speiger.src.collections.doubles.functions.function.DoubleFloatUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.utils.FloatCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2FloatMap.Entry> fastIterator(Double2FloatMap map) {
		ObjectSet<Double2FloatMap.Entry> entries = map.double2FloatEntrySet();
		return entries instanceof Double2FloatMap.FastEntrySet ? ((Double2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2FloatMap.Entry> fastIterable(Double2FloatMap map) {
		ObjectSet<Double2FloatMap.Entry> entries = map.double2FloatEntrySet();
		return map instanceof Double2FloatMap.FastEntrySet ? new ObjectIterable<Double2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Double2FloatMap.Entry> iterator() { return ((Double2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2FloatMap.Entry> action) { ((Double2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2FloatMap map, Consumer<Double2FloatMap.Entry> action) {
		ObjectSet<Double2FloatMap.Entry> entries = map.double2FloatEntrySet();
		if(entries instanceof Double2FloatMap.FastEntrySet) ((Double2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatMap synchronize(Double2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatMap synchronize(Double2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatSortedMap synchronize(Double2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatSortedMap synchronize(Double2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatOrderedMap synchronize(Double2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatOrderedMap synchronize(Double2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatNavigableMap synchronize(Double2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2FloatNavigableMap synchronize(Double2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2FloatMap unmodifiable(Double2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2FloatOrderedMap unmodifiable(Double2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2FloatSortedMap unmodifiable(Double2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2FloatNavigableMap unmodifiable(Double2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2FloatMap.Entry unmodifiable(Double2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2FloatMap.Entry unmodifiable(Map.Entry<Double, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2FloatMap singleton(double key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2FloatMap {
		final double key;
		final float value;
		DoubleSet keySet;
		FloatCollection values;
		ObjectSet<Double2FloatMap.Entry> entrySet;
		
		SingletonMap(double key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(double key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(double key, float defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public float computeFloat(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(double key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Double2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2FloatMap.Entry> double2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2FloatMap {
		@Override
		public float put(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(double key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(double key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(double key, float defaultValue) { return defaultValue; }
		@Override
		public float computeFloat(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(double key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Double2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Double2FloatMap.Entry> double2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2FloatMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(double key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2FloatNavigableMap {
		Double2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Double2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2FloatNavigableMap descendingMap() { return Double2FloatMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2FloatMap.Entry firstEntry() { return Double2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2FloatMap.Entry lastEntry() { return Double2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2FloatNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2FloatNavigableMap headMap(double toKey, boolean inclusive) { return Double2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2FloatNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2FloatNavigableMap subMap(double fromKey, double toKey) { return Double2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2FloatNavigableMap headMap(double toKey) { return Double2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2FloatNavigableMap tailMap(double fromKey) { return Double2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(double e) { throw new UnsupportedOperationException(); }
		@Override
		public double getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(double e) { throw new UnsupportedOperationException(); }
		@Override
		public double getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public double lowerKey(double key) { return map.lowerKey(key); }
		@Override
		public double higherKey(double key) { return map.higherKey(key); }
		@Override
		public double floorKey(double key) { return map.floorKey(key); }
		@Override
		public double ceilingKey(double key) { return map.ceilingKey(key); }
		@Override
		public Double2FloatMap.Entry lowerEntry(double key) { return Double2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2FloatMap.Entry higherEntry(double key) { return Double2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2FloatMap.Entry floorEntry(double key) { return Double2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2FloatMap.Entry ceilingEntry(double key) { return Double2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2FloatNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2FloatOrderedMap {
		Double2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Double2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Double2FloatOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2FloatMap.Entry> double2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2FloatEntrySet());
			return (ObjectOrderedSet<Double2FloatMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2FloatSortedMap {
		Double2FloatSortedMap map;
		
		UnmodifyableSortedMap(Double2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2FloatSortedMap subMap(double fromKey, double toKey) { return Double2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2FloatSortedMap headMap(double toKey) { return Double2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2FloatSortedMap tailMap(double fromKey) { return Double2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public DoubleSortedSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Double2FloatSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2FloatMap implements Double2FloatMap {
		Double2FloatMap map;
		FloatCollection values;
		DoubleSet keys;
		ObjectSet<Double2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Double2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(double key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(double key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(double key) {
			float type = map.get(key);
			return Float.floatToIntBits(type) == Float.floatToIntBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public float getOrDefault(double key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float computeFloat(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(double key, Double2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(double key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(double key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Double2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(DoubleFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(Double2FloatMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2FloatMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2FloatMap.Entry> double2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2FloatMap.Entry>
	{
		ObjectOrderedSet<Double2FloatMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2FloatMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2FloatMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2FloatMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2FloatMap.Entry> iterator(Double2FloatMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2FloatMap.Entry first() { return set.first(); }
		@Override
		public Double2FloatMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2FloatMap.Entry last() { return set.last(); }
		@Override
		public Double2FloatMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2FloatMap.Entry>
	{
		ObjectSet<Double2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Double2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2FloatMap.Entry> iterator() {
			return new ObjectIterator<Double2FloatMap.Entry>() {
				ObjectIterator<Double2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2FloatMap.Entry next() { return Double2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2FloatNavigableMap {
		Double2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Double2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2FloatNavigableMap descendingMap() { synchronized(mutex) { return Double2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2FloatNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2FloatNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2FloatNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2FloatNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2FloatNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2FloatNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2FloatMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2FloatMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2FloatMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2FloatMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2FloatNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2FloatNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(double e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public double getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(double e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public double getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Double lowerKey(Double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Double floorKey(Double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Double ceilingKey(Double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Double higherKey(Double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Double2FloatMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2FloatMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2FloatMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2FloatMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2FloatOrderedMap {
		Double2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Double2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(double key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(double key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Double2FloatOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2FloatMap.Entry> double2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2FloatEntrySet(), mutex);
			return (ObjectOrderedSet<Double2FloatMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2FloatSortedMap {
		Double2FloatSortedMap map;
		
		SynchronizedSortedMap(Double2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2FloatSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2FloatSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2FloatSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public DoubleSortedSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Double2FloatSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2FloatSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2FloatSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2FloatSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2FloatMap implements Double2FloatMap {
		Double2FloatMap map;
		FloatCollection values;
		DoubleSet keys;
		ObjectSet<Double2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(double key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(double key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(double key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(double key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(double key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(double key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Double2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(DoubleFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(double key, DoubleFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(double key, Double2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(double key, DoubleFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsent(double key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsent(key, valueProvider); } }
		@Override
		public float computeFloatNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsentNonDefault(double key, Double2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresentNonDefault(double key, DoubleFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsentNonDefault(double key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public float mergeFloat(double key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Double2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(double key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2FloatMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2FloatMap.Entry> double2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2FloatEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Float get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Float getOrDefault(Object key, Float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Float put(Double key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Double key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Double key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Double key, BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Double key, Function<? super Double, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Double key, BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Double key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}