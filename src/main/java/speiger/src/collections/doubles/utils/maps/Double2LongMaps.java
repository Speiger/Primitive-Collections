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
import speiger.src.collections.doubles.functions.consumer.DoubleLongConsumer;
import speiger.src.collections.doubles.functions.function.Double2LongFunction;
import speiger.src.collections.doubles.functions.function.DoubleLongUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.utils.LongCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2LongMap.Entry> fastIterator(Double2LongMap map) {
		ObjectSet<Double2LongMap.Entry> entries = map.double2LongEntrySet();
		return entries instanceof Double2LongMap.FastEntrySet ? ((Double2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2LongMap.Entry> fastIterable(Double2LongMap map) {
		ObjectSet<Double2LongMap.Entry> entries = map.double2LongEntrySet();
		return map instanceof Double2LongMap.FastEntrySet ? new ObjectIterable<Double2LongMap.Entry>(){
			@Override
			public ObjectIterator<Double2LongMap.Entry> iterator() { return ((Double2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2LongMap.Entry> action) { ((Double2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2LongMap map, Consumer<Double2LongMap.Entry> action) {
		ObjectSet<Double2LongMap.Entry> entries = map.double2LongEntrySet();
		if(entries instanceof Double2LongMap.FastEntrySet) ((Double2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongMap synchronize(Double2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongMap synchronize(Double2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongSortedMap synchronize(Double2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongSortedMap synchronize(Double2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongOrderedMap synchronize(Double2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongOrderedMap synchronize(Double2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongNavigableMap synchronize(Double2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2LongNavigableMap synchronize(Double2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2LongMap unmodifiable(Double2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2LongOrderedMap unmodifiable(Double2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2LongSortedMap unmodifiable(Double2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2LongNavigableMap unmodifiable(Double2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2LongMap.Entry unmodifiable(Double2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2LongMap.Entry unmodifiable(Map.Entry<Double, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2LongMap singleton(double key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2LongMap {
		final double key;
		final long value;
		DoubleSet keySet;
		LongCollection values;
		ObjectSet<Double2LongMap.Entry> entrySet;
		
		SingletonMap(double key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(double key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(double key, long defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public long computeLong(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2LongMap {
		@Override
		public long put(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(double key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(double key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(double key, long defaultValue) { return defaultValue; }
		@Override
		public long computeLong(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2LongMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getLongValue());
		}
		
		@Override
		public void set(double key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2LongNavigableMap {
		Double2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Double2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2LongNavigableMap descendingMap() { return Double2LongMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2LongMap.Entry firstEntry() { return Double2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2LongMap.Entry lastEntry() { return Double2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2LongNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2LongNavigableMap headMap(double toKey, boolean inclusive) { return Double2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2LongNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2LongNavigableMap subMap(double fromKey, double toKey) { return Double2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2LongNavigableMap headMap(double toKey) { return Double2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2LongNavigableMap tailMap(double fromKey) { return Double2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2LongMap.Entry lowerEntry(double key) { return Double2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2LongMap.Entry higherEntry(double key) { return Double2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2LongMap.Entry floorEntry(double key) { return Double2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2LongMap.Entry ceilingEntry(double key) { return Double2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2LongNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2LongOrderedMap {
		Double2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Double2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Double2LongOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2LongEntrySet());
			return (ObjectOrderedSet<Double2LongMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2LongSortedMap {
		Double2LongSortedMap map;
		
		UnmodifyableSortedMap(Double2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2LongSortedMap subMap(double fromKey, double toKey) { return Double2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2LongSortedMap headMap(double toKey) { return Double2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2LongSortedMap tailMap(double fromKey) { return Double2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Double2LongSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2LongMap implements Double2LongMap {
		Double2LongMap map;
		LongCollection values;
		DoubleSet keys;
		ObjectSet<Double2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Double2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(double key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(double key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(double key) {
			long type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public long getOrDefault(double key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long computeLong(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(double key, Double2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(double key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceLongs(DoubleLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceLongs(Double2LongMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2LongMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2LongMap.Entry>
	{
		ObjectOrderedSet<Double2LongMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2LongMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2LongMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2LongMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2LongMap.Entry> iterator(Double2LongMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2LongMap.Entry first() { return set.first(); }
		@Override
		public Double2LongMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2LongMap.Entry last() { return set.last(); }
		@Override
		public Double2LongMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2LongMap.Entry>
	{
		ObjectSet<Double2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Double2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2LongMap.Entry> iterator() {
			return new ObjectIterator<Double2LongMap.Entry>() {
				ObjectIterator<Double2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2LongMap.Entry next() { return Double2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2LongNavigableMap {
		Double2LongNavigableMap map;
		
		SynchronizedNavigableMap(Double2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2LongNavigableMap descendingMap() { synchronized(mutex) { return Double2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2LongMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2LongNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2LongNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2LongNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2LongNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2LongNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2LongNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2LongMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2LongMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2LongMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2LongMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2LongNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2LongNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2LongMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2LongMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2LongMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2LongMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2LongOrderedMap {
		Double2LongOrderedMap map;
		
		SynchronizedOrderedMap(Double2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(double key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(double key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Double2LongOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2LongEntrySet(), mutex);
			return (ObjectOrderedSet<Double2LongMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2LongSortedMap {
		Double2LongSortedMap map;
		
		SynchronizedSortedMap(Double2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2LongSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2LongSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2LongSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Double2LongSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2LongSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2LongSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2LongSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2LongMap implements Double2LongMap {
		Double2LongMap map;
		LongCollection values;
		DoubleSet keys;
		ObjectSet<Double2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(double key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(double key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(double key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(double key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(double key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(double key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Double2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(DoubleLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(double key, DoubleLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongNonDefault(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsentNonDefault(double key, Double2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresentNonDefault(double key, DoubleLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public long supplyLongIfAbsent(double key, LongSupplier valueProvider) { synchronized(mutex) { return map.supplyLongIfAbsent(key, valueProvider); } }
		@Override
		public long supplyLongIfAbsentNonDefault(double key, LongSupplier valueProvider) { synchronized(mutex) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(double key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2LongMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2LongEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Long get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Long getOrDefault(Object key, Long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Long put(Double key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Double key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Double key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Double key, Function<? super Double, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Double key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}