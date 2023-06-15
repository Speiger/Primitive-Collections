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
import speiger.src.collections.doubles.functions.consumer.DoubleDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleUnaryOperator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.utils.DoubleCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2DoubleMap.Entry> fastIterator(Double2DoubleMap map) {
		ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
		return entries instanceof Double2DoubleMap.FastEntrySet ? ((Double2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2DoubleMap.Entry> fastIterable(Double2DoubleMap map) {
		ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
		return map instanceof Double2DoubleMap.FastEntrySet ? new ObjectIterable<Double2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Double2DoubleMap.Entry> iterator() { return ((Double2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2DoubleMap.Entry> action) { ((Double2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2DoubleMap map, Consumer<Double2DoubleMap.Entry> action) {
		ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
		if(entries instanceof Double2DoubleMap.FastEntrySet) ((Double2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleMap synchronize(Double2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleMap synchronize(Double2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleSortedMap synchronize(Double2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleSortedMap synchronize(Double2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleOrderedMap synchronize(Double2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleOrderedMap synchronize(Double2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleNavigableMap synchronize(Double2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2DoubleNavigableMap synchronize(Double2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2DoubleMap unmodifiable(Double2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2DoubleOrderedMap unmodifiable(Double2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2DoubleSortedMap unmodifiable(Double2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2DoubleNavigableMap unmodifiable(Double2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2DoubleMap.Entry unmodifiable(Double2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2DoubleMap.Entry unmodifiable(Map.Entry<Double, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2DoubleMap singleton(double key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2DoubleMap {
		final double key;
		final double value;
		DoubleSet keySet;
		DoubleCollection values;
		ObjectSet<Double2DoubleMap.Entry> entrySet;
		
		SingletonMap(double key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(double key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(double key, double defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Double2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2DoubleMap.Entry> double2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2DoubleMap {
		@Override
		public double put(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(double key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(double key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(double key, double defaultValue) { return defaultValue; }
		@Override
		public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Double2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Double2DoubleMap.Entry> double2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2DoubleMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(double key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2DoubleNavigableMap {
		Double2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Double2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2DoubleNavigableMap descendingMap() { return Double2DoubleMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2DoubleMap.Entry firstEntry() { return Double2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2DoubleMap.Entry lastEntry() { return Double2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2DoubleNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2DoubleNavigableMap headMap(double toKey, boolean inclusive) { return Double2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2DoubleNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2DoubleNavigableMap subMap(double fromKey, double toKey) { return Double2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2DoubleNavigableMap headMap(double toKey) { return Double2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2DoubleNavigableMap tailMap(double fromKey) { return Double2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2DoubleMap.Entry lowerEntry(double key) { return Double2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2DoubleMap.Entry higherEntry(double key) { return Double2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2DoubleMap.Entry floorEntry(double key) { return Double2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2DoubleMap.Entry ceilingEntry(double key) { return Double2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2DoubleNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2DoubleOrderedMap {
		Double2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Double2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Double2DoubleOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2DoubleEntrySet());
			return (ObjectOrderedSet<Double2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2DoubleSortedMap {
		Double2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Double2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2DoubleSortedMap subMap(double fromKey, double toKey) { return Double2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2DoubleSortedMap headMap(double toKey) { return Double2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2DoubleSortedMap tailMap(double fromKey) { return Double2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Double2DoubleSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2DoubleMap implements Double2DoubleMap {
		Double2DoubleMap map;
		DoubleCollection values;
		DoubleSet keys;
		ObjectSet<Double2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Double2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(double key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(double key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(double key) {
			double type = map.get(key);
			return Double.doubleToLongBits(type) == Double.doubleToLongBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public double getOrDefault(double key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Double2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(Double2DoubleMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2DoubleMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2DoubleMap.Entry>
	{
		ObjectOrderedSet<Double2DoubleMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2DoubleMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2DoubleMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2DoubleMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2DoubleMap.Entry> iterator(Double2DoubleMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2DoubleMap.Entry first() { return set.first(); }
		@Override
		public Double2DoubleMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2DoubleMap.Entry last() { return set.last(); }
		@Override
		public Double2DoubleMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2DoubleMap.Entry>
	{
		ObjectSet<Double2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Double2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Double2DoubleMap.Entry>() {
				ObjectIterator<Double2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2DoubleMap.Entry next() { return Double2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2DoubleNavigableMap {
		Double2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Double2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2DoubleNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2DoubleNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2DoubleNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2DoubleNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2DoubleNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2DoubleNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2DoubleMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2DoubleMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2DoubleMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2DoubleMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2DoubleNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2DoubleMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2DoubleMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2DoubleMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2DoubleMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2DoubleOrderedMap {
		Double2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Double2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(double key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(double key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Double2DoubleOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2DoubleEntrySet(), mutex);
			return (ObjectOrderedSet<Double2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2DoubleSortedMap {
		Double2DoubleSortedMap map;
		
		SynchronizedSortedMap(Double2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2DoubleSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2DoubleSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2DoubleSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Double2DoubleSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2DoubleSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2DoubleSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2DoubleMap implements Double2DoubleMap {
		Double2DoubleMap map;
		DoubleCollection values;
		DoubleSet keys;
		ObjectSet<Double2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(double key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(double key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(double key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(double key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(double key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(double key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Double2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsent(key, valueProvider); } }
		@Override
		public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Double2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(double key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2DoubleMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2DoubleEntrySet(), mutex);
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
		public Double put(Double key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Double key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Double key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Double key, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Double key, Function<? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Double key, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Double key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}