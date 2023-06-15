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
import speiger.src.collections.doubles.functions.consumer.DoubleShortConsumer;
import speiger.src.collections.doubles.functions.function.Double2ShortFunction;
import speiger.src.collections.doubles.functions.function.DoubleShortUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.utils.ShortCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2ShortMap.Entry> fastIterator(Double2ShortMap map) {
		ObjectSet<Double2ShortMap.Entry> entries = map.double2ShortEntrySet();
		return entries instanceof Double2ShortMap.FastEntrySet ? ((Double2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2ShortMap.Entry> fastIterable(Double2ShortMap map) {
		ObjectSet<Double2ShortMap.Entry> entries = map.double2ShortEntrySet();
		return map instanceof Double2ShortMap.FastEntrySet ? new ObjectIterable<Double2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Double2ShortMap.Entry> iterator() { return ((Double2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2ShortMap.Entry> action) { ((Double2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2ShortMap map, Consumer<Double2ShortMap.Entry> action) {
		ObjectSet<Double2ShortMap.Entry> entries = map.double2ShortEntrySet();
		if(entries instanceof Double2ShortMap.FastEntrySet) ((Double2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortMap synchronize(Double2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortMap synchronize(Double2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortSortedMap synchronize(Double2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortSortedMap synchronize(Double2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortOrderedMap synchronize(Double2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortOrderedMap synchronize(Double2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortNavigableMap synchronize(Double2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ShortNavigableMap synchronize(Double2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2ShortMap unmodifiable(Double2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ShortOrderedMap unmodifiable(Double2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ShortSortedMap unmodifiable(Double2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ShortNavigableMap unmodifiable(Double2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2ShortMap.Entry unmodifiable(Double2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2ShortMap.Entry unmodifiable(Map.Entry<Double, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2ShortMap singleton(double key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2ShortMap {
		final double key;
		final short value;
		DoubleSet keySet;
		ShortCollection values;
		ObjectSet<Double2ShortMap.Entry> entrySet;
		
		SingletonMap(double key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(double key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(double key, short defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Double2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2ShortMap.Entry> double2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2ShortMap {
		@Override
		public short put(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(double key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(double key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(double key, short defaultValue) { return defaultValue; }
		@Override
		public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Double2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Double2ShortMap.Entry> double2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2ShortMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getShortValue());
		}
		
		@Override
		public void set(double key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2ShortNavigableMap {
		Double2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Double2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2ShortNavigableMap descendingMap() { return Double2ShortMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2ShortMap.Entry firstEntry() { return Double2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2ShortMap.Entry lastEntry() { return Double2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ShortNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2ShortNavigableMap headMap(double toKey, boolean inclusive) { return Double2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2ShortNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2ShortNavigableMap subMap(double fromKey, double toKey) { return Double2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ShortNavigableMap headMap(double toKey) { return Double2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ShortNavigableMap tailMap(double fromKey) { return Double2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2ShortMap.Entry lowerEntry(double key) { return Double2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2ShortMap.Entry higherEntry(double key) { return Double2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2ShortMap.Entry floorEntry(double key) { return Double2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2ShortMap.Entry ceilingEntry(double key) { return Double2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2ShortNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2ShortOrderedMap {
		Double2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Double2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Double2ShortOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2ShortMap.Entry> double2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2ShortEntrySet());
			return (ObjectOrderedSet<Double2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2ShortSortedMap {
		Double2ShortSortedMap map;
		
		UnmodifyableSortedMap(Double2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2ShortSortedMap subMap(double fromKey, double toKey) { return Double2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ShortSortedMap headMap(double toKey) { return Double2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ShortSortedMap tailMap(double fromKey) { return Double2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Double2ShortSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2ShortMap implements Double2ShortMap {
		Double2ShortMap map;
		ShortCollection values;
		DoubleSet keys;
		ObjectSet<Double2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Double2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(double key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(double key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(double key) {
			short type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public short getOrDefault(double key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Double2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(DoubleShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(Double2ShortMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2ShortMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2ShortMap.Entry> double2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2ShortMap.Entry>
	{
		ObjectOrderedSet<Double2ShortMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2ShortMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2ShortMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2ShortMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2ShortMap.Entry> iterator(Double2ShortMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2ShortMap.Entry first() { return set.first(); }
		@Override
		public Double2ShortMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ShortMap.Entry last() { return set.last(); }
		@Override
		public Double2ShortMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2ShortMap.Entry>
	{
		ObjectSet<Double2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Double2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2ShortMap.Entry> iterator() {
			return new ObjectIterator<Double2ShortMap.Entry>() {
				ObjectIterator<Double2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2ShortMap.Entry next() { return Double2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2ShortNavigableMap {
		Double2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Double2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2ShortNavigableMap descendingMap() { synchronized(mutex) { return Double2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2ShortNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2ShortNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2ShortNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2ShortNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ShortNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ShortNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2ShortMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2ShortMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2ShortMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2ShortMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2ShortNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ShortNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2ShortMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2ShortMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2ShortMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2ShortMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2ShortOrderedMap {
		Double2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Double2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(double key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(double key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Double2ShortOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2ShortMap.Entry> double2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2ShortEntrySet(), mutex);
			return (ObjectOrderedSet<Double2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2ShortSortedMap {
		Double2ShortSortedMap map;
		
		SynchronizedSortedMap(Double2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2ShortSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ShortSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ShortSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Double2ShortSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2ShortSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ShortSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ShortSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2ShortMap implements Double2ShortMap {
		Double2ShortMap map;
		ShortCollection values;
		DoubleSet keys;
		ObjectSet<Double2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(double key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(double key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(double key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(double key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(double key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(double key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Double2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(DoubleShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsent(key, valueProvider); } }
		@Override
		public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Double2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(double key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2ShortMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2ShortMap.Entry> double2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2ShortEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Short get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Short getOrDefault(Object key, Short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Short put(Double key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Double key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Double key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Double key, BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Double key, Function<? super Double, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Double key, BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Double key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}