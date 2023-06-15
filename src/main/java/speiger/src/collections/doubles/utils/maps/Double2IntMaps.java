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
import speiger.src.collections.doubles.functions.consumer.DoubleIntConsumer;
import speiger.src.collections.doubles.functions.function.Double2IntFunction;
import speiger.src.collections.doubles.functions.function.DoubleIntUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.utils.IntCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2IntMap.Entry> fastIterator(Double2IntMap map) {
		ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
		return entries instanceof Double2IntMap.FastEntrySet ? ((Double2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2IntMap.Entry> fastIterable(Double2IntMap map) {
		ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
		return map instanceof Double2IntMap.FastEntrySet ? new ObjectIterable<Double2IntMap.Entry>(){
			@Override
			public ObjectIterator<Double2IntMap.Entry> iterator() { return ((Double2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2IntMap.Entry> action) { ((Double2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2IntMap map, Consumer<Double2IntMap.Entry> action) {
		ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
		if(entries instanceof Double2IntMap.FastEntrySet) ((Double2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntMap synchronize(Double2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntMap synchronize(Double2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntSortedMap synchronize(Double2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntSortedMap synchronize(Double2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntOrderedMap synchronize(Double2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntOrderedMap synchronize(Double2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntNavigableMap synchronize(Double2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2IntNavigableMap synchronize(Double2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2IntMap unmodifiable(Double2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2IntOrderedMap unmodifiable(Double2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2IntSortedMap unmodifiable(Double2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2IntNavigableMap unmodifiable(Double2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2IntMap.Entry unmodifiable(Double2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2IntMap.Entry unmodifiable(Map.Entry<Double, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2IntMap singleton(double key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2IntMap {
		final double key;
		final int value;
		DoubleSet keySet;
		IntCollection values;
		ObjectSet<Double2IntMap.Entry> entrySet;
		
		SingletonMap(double key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(double key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(double key, int defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public int computeInt(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(double key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Double2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2IntMap.Entry> double2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2IntMap {
		@Override
		public int put(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(double key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(double key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(double key, int defaultValue) { return defaultValue; }
		@Override
		public int computeInt(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(double key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Double2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Double2IntMap.Entry> double2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2IntMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getIntValue());
		}
		
		@Override
		public void set(double key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2IntNavigableMap {
		Double2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Double2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2IntNavigableMap descendingMap() { return Double2IntMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2IntMap.Entry firstEntry() { return Double2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2IntMap.Entry lastEntry() { return Double2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2IntNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2IntNavigableMap headMap(double toKey, boolean inclusive) { return Double2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2IntNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2IntNavigableMap subMap(double fromKey, double toKey) { return Double2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2IntNavigableMap headMap(double toKey) { return Double2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2IntNavigableMap tailMap(double fromKey) { return Double2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2IntMap.Entry lowerEntry(double key) { return Double2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2IntMap.Entry higherEntry(double key) { return Double2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2IntMap.Entry floorEntry(double key) { return Double2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2IntMap.Entry ceilingEntry(double key) { return Double2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2IntNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2IntOrderedMap {
		Double2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Double2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Double2IntOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2IntMap.Entry> double2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2IntEntrySet());
			return (ObjectOrderedSet<Double2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2IntSortedMap {
		Double2IntSortedMap map;
		
		UnmodifyableSortedMap(Double2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2IntSortedMap subMap(double fromKey, double toKey) { return Double2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2IntSortedMap headMap(double toKey) { return Double2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2IntSortedMap tailMap(double fromKey) { return Double2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Double2IntSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2IntMap implements Double2IntMap {
		Double2IntMap map;
		IntCollection values;
		DoubleSet keys;
		ObjectSet<Double2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Double2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(double key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(double key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(double key) {
			int type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public int getOrDefault(double key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public int computeInt(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(double key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Double2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(Double2IntMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2IntMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2IntMap.Entry> double2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2IntMap.Entry>
	{
		ObjectOrderedSet<Double2IntMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2IntMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2IntMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator(Double2IntMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2IntMap.Entry first() { return set.first(); }
		@Override
		public Double2IntMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2IntMap.Entry last() { return set.last(); }
		@Override
		public Double2IntMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2IntMap.Entry>
	{
		ObjectSet<Double2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Double2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2IntMap.Entry> iterator() {
			return new ObjectIterator<Double2IntMap.Entry>() {
				ObjectIterator<Double2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2IntMap.Entry next() { return Double2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2IntNavigableMap {
		Double2IntNavigableMap map;
		
		SynchronizedNavigableMap(Double2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2IntNavigableMap descendingMap() { synchronized(mutex) { return Double2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2IntMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2IntNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2IntNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2IntNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2IntNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2IntNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2IntNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2IntMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2IntMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2IntMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2IntMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2IntNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2IntNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2IntMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2IntMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2IntMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2IntMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2IntOrderedMap {
		Double2IntOrderedMap map;
		
		SynchronizedOrderedMap(Double2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(double key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(double key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Double2IntOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2IntMap.Entry> double2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2IntEntrySet(), mutex);
			return (ObjectOrderedSet<Double2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2IntSortedMap {
		Double2IntSortedMap map;
		
		SynchronizedSortedMap(Double2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2IntSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2IntSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2IntSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Double2IntSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2IntSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2IntSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2IntSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2IntMap implements Double2IntMap {
		Double2IntMap map;
		IntCollection values;
		DoubleSet keys;
		ObjectSet<Double2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(double key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(double key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(double key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(double key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(double key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(double key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Double2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(DoubleIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(double key, DoubleIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(double key, Double2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsentNonDefault(double key, Double2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(double key, DoubleIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresentNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public int supplyIntIfAbsent(double key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsent(key, valueProvider); } }
		@Override
		public int supplyIntIfAbsentNonDefault(double key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public int mergeInt(double key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Double2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(double key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2IntMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2IntMap.Entry> double2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2IntEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Integer get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Integer getOrDefault(Object key, Integer defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Integer put(Double key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Double key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Double key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Double key, BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Double key, Function<? super Double, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Double key, BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Double key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}