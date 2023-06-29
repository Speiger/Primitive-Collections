package speiger.src.collections.ints.utils.maps;

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
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.ints.functions.function.Int2DoubleFunction;
import speiger.src.collections.ints.functions.function.IntDoubleUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.utils.DoubleCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2DoubleMap.Entry> fastIterator(Int2DoubleMap map) {
		ObjectSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
		return entries instanceof Int2DoubleMap.FastEntrySet ? ((Int2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2DoubleMap.Entry> fastIterable(Int2DoubleMap map) {
		ObjectSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
		return map instanceof Int2DoubleMap.FastEntrySet ? new ObjectIterable<Int2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Int2DoubleMap.Entry> iterator() { return ((Int2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2DoubleMap.Entry> action) { ((Int2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2DoubleMap map, Consumer<Int2DoubleMap.Entry> action) {
		ObjectSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
		if(entries instanceof Int2DoubleMap.FastEntrySet) ((Int2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleMap synchronize(Int2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleMap synchronize(Int2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleSortedMap synchronize(Int2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleSortedMap synchronize(Int2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleOrderedMap synchronize(Int2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleOrderedMap synchronize(Int2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleNavigableMap synchronize(Int2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2DoubleNavigableMap synchronize(Int2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2DoubleMap unmodifiable(Int2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2DoubleOrderedMap unmodifiable(Int2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2DoubleSortedMap unmodifiable(Int2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2DoubleNavigableMap unmodifiable(Int2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2DoubleMap.Entry unmodifiable(Int2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2DoubleMap.Entry unmodifiable(Map.Entry<Integer, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2DoubleMap singleton(int key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2DoubleMap {
		final int key;
		final double value;
		IntSet keySet;
		DoubleCollection values;
		ObjectSet<Int2DoubleMap.Entry> entrySet;
		
		SingletonMap(int key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(int key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(int key, double defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public double computeDouble(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(int key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Int2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2DoubleMap.Entry> int2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2DoubleMap {
		@Override
		public double put(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(int key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(int key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(int key, double defaultValue) { return defaultValue; }
		@Override
		public double computeDouble(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(int key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Int2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Int2DoubleMap.Entry> int2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2DoubleMap.Entry entry) {
			super(entry.getIntKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(int key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2DoubleNavigableMap {
		Int2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Int2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2DoubleNavigableMap descendingMap() { return Int2DoubleMaps.unmodifiable(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2DoubleMap.Entry firstEntry() { return Int2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2DoubleMap.Entry lastEntry() { return Int2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2DoubleNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2DoubleNavigableMap headMap(int toKey, boolean inclusive) { return Int2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2DoubleNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2DoubleNavigableMap subMap(int fromKey, int toKey) { return Int2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2DoubleNavigableMap headMap(int toKey) { return Int2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2DoubleNavigableMap tailMap(int fromKey) { return Int2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lowerKey(int key) { return map.lowerKey(key); }
		@Override
		public int higherKey(int key) { return map.higherKey(key); }
		@Override
		public int floorKey(int key) { return map.floorKey(key); }
		@Override
		public int ceilingKey(int key) { return map.ceilingKey(key); }
		@Override
		public Int2DoubleMap.Entry lowerEntry(int key) { return Int2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2DoubleMap.Entry higherEntry(int key) { return Int2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2DoubleMap.Entry floorEntry(int key) { return Int2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2DoubleMap.Entry ceilingEntry(int key) { return Int2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2DoubleNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2DoubleOrderedMap {
		Int2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Int2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Int2DoubleOrderedMap copy() { return map.copy(); }
		@Override
		public IntOrderedSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return (IntOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.int2DoubleEntrySet());
			return (ObjectOrderedSet<Int2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2DoubleSortedMap {
		Int2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Int2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2DoubleSortedMap subMap(int fromKey, int toKey) { return Int2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2DoubleSortedMap headMap(int toKey) { return Int2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2DoubleSortedMap tailMap(int fromKey) { return Int2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public IntSortedSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Int2DoubleSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2DoubleMap implements Int2DoubleMap {
		Int2DoubleMap map;
		DoubleCollection values;
		IntSet keys;
		ObjectSet<Int2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Int2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(int key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(int key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(int key) {
			double type = map.get(key);
			return Double.doubleToLongBits(type) == Double.doubleToLongBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public double getOrDefault(int key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double computeDouble(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsent(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresent(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsent(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfAbsentNonDefault(int key, Int2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double computeDoubleIfPresentNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(int key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public double mergeDouble(int key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllDouble(Int2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(IntDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceDoubles(Int2DoubleMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Int2DoubleMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Int2DoubleMap.Entry>
	{
		ObjectOrderedSet<Int2DoubleMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Int2DoubleMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Int2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Int2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Int2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Int2DoubleMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Int2DoubleMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Int2DoubleMap.Entry> iterator(Int2DoubleMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Int2DoubleMap.Entry first() { return set.first(); }
		@Override
		public Int2DoubleMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Int2DoubleMap.Entry last() { return set.last(); }
		@Override
		public Int2DoubleMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2DoubleMap.Entry>
	{
		ObjectSet<Int2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Int2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Int2DoubleMap.Entry>() {
				ObjectIterator<Int2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2DoubleMap.Entry next() { return Int2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2DoubleNavigableMap {
		Int2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Int2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public IntNavigableSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Int2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Int2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2DoubleNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2DoubleNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2DoubleNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2DoubleNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2DoubleNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2DoubleNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2DoubleMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2DoubleMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2DoubleMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2DoubleMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2DoubleNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Integer lowerKey(Integer key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Integer floorKey(Integer key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Integer ceilingKey(Integer key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Integer higherKey(Integer key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Int2DoubleMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2DoubleMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2DoubleMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2DoubleMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2DoubleOrderedMap {
		Int2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Int2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(int key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(int key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Int2DoubleOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntOrderedSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return (IntOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2DoubleEntrySet(), mutex);
			return (ObjectOrderedSet<Int2DoubleMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2DoubleSortedMap {
		Int2DoubleSortedMap map;
		
		SynchronizedSortedMap(Int2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2DoubleSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2DoubleSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2DoubleSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public IntSortedSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Int2DoubleSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2DoubleSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2DoubleSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2DoubleMap implements Int2DoubleMap {
		Int2DoubleMap map;
		DoubleCollection values;
		IntSet keys;
		ObjectSet<Int2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(int key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(int key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(int key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(int key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(int key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(int key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Int2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(IntDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(int key, IntDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(int key, Int2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(int key, IntDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsent(int key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsent(key, valueProvider); } }
		@Override
		public double computeDoubleNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsentNonDefault(int key, Int2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresentNonDefault(int key, IntDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public double supplyDoubleIfAbsentNonDefault(int key, DoubleSupplier valueProvider) { synchronized(mutex) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public double mergeDouble(int key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Int2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(int key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Int2DoubleMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2DoubleEntrySet(), mutex);
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
		public Double put(Integer key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Integer key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Integer key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Integer key, BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Integer key, Function<? super Integer, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Integer key, BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Integer key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}