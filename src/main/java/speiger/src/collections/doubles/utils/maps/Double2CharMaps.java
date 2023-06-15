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
import speiger.src.collections.doubles.functions.consumer.DoubleCharConsumer;
import speiger.src.collections.doubles.functions.function.Double2CharFunction;
import speiger.src.collections.doubles.functions.function.DoubleCharUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2CharMap.Entry> fastIterator(Double2CharMap map) {
		ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
		return entries instanceof Double2CharMap.FastEntrySet ? ((Double2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2CharMap.Entry> fastIterable(Double2CharMap map) {
		ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
		return map instanceof Double2CharMap.FastEntrySet ? new ObjectIterable<Double2CharMap.Entry>(){
			@Override
			public ObjectIterator<Double2CharMap.Entry> iterator() { return ((Double2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2CharMap.Entry> action) { ((Double2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2CharMap map, Consumer<Double2CharMap.Entry> action) {
		ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
		if(entries instanceof Double2CharMap.FastEntrySet) ((Double2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharMap synchronize(Double2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharMap synchronize(Double2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharSortedMap synchronize(Double2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharSortedMap synchronize(Double2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharOrderedMap synchronize(Double2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharOrderedMap synchronize(Double2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharNavigableMap synchronize(Double2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2CharNavigableMap synchronize(Double2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2CharMap unmodifiable(Double2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2CharOrderedMap unmodifiable(Double2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2CharSortedMap unmodifiable(Double2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2CharNavigableMap unmodifiable(Double2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2CharMap.Entry unmodifiable(Double2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2CharMap.Entry unmodifiable(Map.Entry<Double, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2CharMap singleton(double key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2CharMap {
		final double key;
		final char value;
		DoubleSet keySet;
		CharCollection values;
		ObjectSet<Double2CharMap.Entry> entrySet;
		
		SingletonMap(double key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(double key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(double key, char defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2CharMap.Entry> double2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2CharMap {
		@Override
		public char put(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(double key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(double key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(double key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Double2CharMap.Entry> double2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2CharMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getCharValue());
		}
		
		@Override
		public void set(double key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2CharNavigableMap {
		Double2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Double2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2CharNavigableMap descendingMap() { return Double2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet keySet() { return DoubleSets.unmodifiable(map.keySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2CharMap.Entry firstEntry() { return Double2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2CharMap.Entry lastEntry() { return Double2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2CharNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2CharNavigableMap headMap(double toKey, boolean inclusive) { return Double2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2CharNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2CharNavigableMap subMap(double fromKey, double toKey) { return Double2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2CharNavigableMap headMap(double toKey) { return Double2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2CharNavigableMap tailMap(double fromKey) { return Double2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2CharMap.Entry lowerEntry(double key) { return Double2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2CharMap.Entry higherEntry(double key) { return Double2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2CharMap.Entry floorEntry(double key) { return Double2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2CharMap.Entry ceilingEntry(double key) { return Double2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2CharNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2CharOrderedMap {
		Double2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Double2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Double2CharOrderedMap copy() { return map.copy(); }
		@Override
		public DoubleOrderedSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return (DoubleOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Double2CharMap.Entry> double2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.double2CharEntrySet());
			return (ObjectOrderedSet<Double2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2CharSortedMap {
		Double2CharSortedMap map;
		
		UnmodifyableSortedMap(Double2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2CharSortedMap subMap(double fromKey, double toKey) { return Double2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2CharSortedMap headMap(double toKey) { return Double2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2CharSortedMap tailMap(double fromKey) { return Double2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Double2CharSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2CharMap implements Double2CharMap {
		Double2CharMap map;
		CharCollection values;
		DoubleSet keys;
		ObjectSet<Double2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Double2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(double key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(double key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(double key) {
			char type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(double key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(double key, Double2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(double key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(DoubleCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Double2CharMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Double2CharMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2CharMap.Entry> double2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Double2CharMap.Entry>
	{
		ObjectOrderedSet<Double2CharMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Double2CharMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Double2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Double2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Double2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Double2CharMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Double2CharMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Double2CharMap.Entry> iterator(Double2CharMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Double2CharMap.Entry first() { return set.first(); }
		@Override
		public Double2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Double2CharMap.Entry last() { return set.last(); }
		@Override
		public Double2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2CharMap.Entry>
	{
		ObjectSet<Double2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Double2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2CharMap.Entry> iterator() {
			return new ObjectIterator<Double2CharMap.Entry>() {
				ObjectIterator<Double2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2CharMap.Entry next() { return Double2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2CharNavigableMap {
		Double2CharNavigableMap map;
		
		SynchronizedNavigableMap(Double2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2CharNavigableMap descendingMap() { synchronized(mutex) { return Double2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet keySet() { synchronized(mutex) { return DoubleSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Double2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2CharMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Double2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2CharNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2CharNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2CharNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2CharNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2CharNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2CharNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2CharMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2CharMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2CharMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2CharMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2CharNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2CharNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2CharMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2CharMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2CharMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2CharMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2CharOrderedMap {
		Double2CharOrderedMap map;
		
		SynchronizedOrderedMap(Double2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(double key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(double key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Double2CharOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleOrderedSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return (DoubleOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Double2CharMap.Entry> double2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Double2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2CharSortedMap {
		Double2CharSortedMap map;
		
		SynchronizedSortedMap(Double2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2CharSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2CharSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2CharSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Double2CharSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2CharSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2CharSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2CharSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2CharMap implements Double2CharMap {
		Double2CharMap map;
		CharCollection values;
		DoubleSet keys;
		ObjectSet<Double2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(double key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(double key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(double key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(double key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(double key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(double key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Double2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(DoubleCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(double key, Double2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(double key, DoubleCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(double key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char supplyCharIfAbsentNonDefault(double key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(double key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Double2CharMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2CharMap.Entry> double2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2CharEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Character get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Character getOrDefault(Object key, Character defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Character put(Double key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Double key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Double key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Double key, Function<? super Double, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Double key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}