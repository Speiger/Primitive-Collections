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
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.function.Int2CharFunction;
import speiger.src.collections.ints.functions.function.IntCharUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2CharMap.Entry> fastIterator(Int2CharMap map) {
		ObjectSet<Int2CharMap.Entry> entries = map.int2CharEntrySet();
		return entries instanceof Int2CharMap.FastEntrySet ? ((Int2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2CharMap.Entry> fastIterable(Int2CharMap map) {
		ObjectSet<Int2CharMap.Entry> entries = map.int2CharEntrySet();
		return map instanceof Int2CharMap.FastEntrySet ? new ObjectIterable<Int2CharMap.Entry>(){
			@Override
			public ObjectIterator<Int2CharMap.Entry> iterator() { return ((Int2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2CharMap.Entry> action) { ((Int2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2CharMap map, Consumer<Int2CharMap.Entry> action) {
		ObjectSet<Int2CharMap.Entry> entries = map.int2CharEntrySet();
		if(entries instanceof Int2CharMap.FastEntrySet) ((Int2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharMap synchronize(Int2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharMap synchronize(Int2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharSortedMap synchronize(Int2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharSortedMap synchronize(Int2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharOrderedMap synchronize(Int2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharOrderedMap synchronize(Int2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharNavigableMap synchronize(Int2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2CharNavigableMap synchronize(Int2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2CharMap unmodifiable(Int2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2CharOrderedMap unmodifiable(Int2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2CharSortedMap unmodifiable(Int2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2CharNavigableMap unmodifiable(Int2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2CharMap.Entry unmodifiable(Int2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2CharMap.Entry unmodifiable(Map.Entry<Integer, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2CharMap singleton(int key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2CharMap {
		final int key;
		final char value;
		IntSet keySet;
		CharCollection values;
		ObjectSet<Int2CharMap.Entry> entrySet;
		
		SingletonMap(int key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(int key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(int key, char defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public char computeChar(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Int2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2CharMap.Entry> int2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2CharMap {
		@Override
		public char put(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(int key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(int key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(int key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Int2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Int2CharMap.Entry> int2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2CharMap.Entry entry) {
			super(entry.getIntKey(), entry.getCharValue());
		}
		
		@Override
		public void set(int key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2CharNavigableMap {
		Int2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Int2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2CharNavigableMap descendingMap() { return Int2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2CharMap.Entry firstEntry() { return Int2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2CharMap.Entry lastEntry() { return Int2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2CharNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2CharNavigableMap headMap(int toKey, boolean inclusive) { return Int2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2CharNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2CharNavigableMap subMap(int fromKey, int toKey) { return Int2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2CharNavigableMap headMap(int toKey) { return Int2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2CharNavigableMap tailMap(int fromKey) { return Int2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Int2CharMap.Entry lowerEntry(int key) { return Int2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2CharMap.Entry higherEntry(int key) { return Int2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2CharMap.Entry floorEntry(int key) { return Int2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2CharMap.Entry ceilingEntry(int key) { return Int2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2CharNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2CharOrderedMap {
		Int2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Int2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Int2CharOrderedMap copy() { return map.copy(); }
		@Override
		public IntOrderedSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return (IntOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Int2CharMap.Entry> int2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.int2CharEntrySet());
			return (ObjectOrderedSet<Int2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2CharSortedMap {
		Int2CharSortedMap map;
		
		UnmodifyableSortedMap(Int2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2CharSortedMap subMap(int fromKey, int toKey) { return Int2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2CharSortedMap headMap(int toKey) { return Int2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2CharSortedMap tailMap(int fromKey) { return Int2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Int2CharSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2CharMap implements Int2CharMap {
		Int2CharMap map;
		CharCollection values;
		IntSet keys;
		ObjectSet<Int2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Int2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(int key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(int key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(int key) {
			char type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(int key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Int2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(IntCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Int2CharMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Int2CharMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2CharMap.Entry> int2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Int2CharMap.Entry>
	{
		ObjectOrderedSet<Int2CharMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Int2CharMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Int2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Int2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Int2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Int2CharMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Int2CharMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Int2CharMap.Entry> iterator(Int2CharMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Int2CharMap.Entry first() { return set.first(); }
		@Override
		public Int2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Int2CharMap.Entry last() { return set.last(); }
		@Override
		public Int2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2CharMap.Entry>
	{
		ObjectSet<Int2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Int2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2CharMap.Entry> iterator() {
			return new ObjectIterator<Int2CharMap.Entry>() {
				ObjectIterator<Int2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2CharMap.Entry next() { return Int2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2CharNavigableMap {
		Int2CharNavigableMap map;
		
		SynchronizedNavigableMap(Int2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2CharNavigableMap descendingMap() { synchronized(mutex) { return Int2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public IntNavigableSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Int2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2CharMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Int2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2CharNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2CharNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2CharNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2CharNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2CharNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2CharNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2CharMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2CharMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2CharMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2CharMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2CharNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2CharNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Int2CharMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2CharMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2CharMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2CharMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2CharOrderedMap {
		Int2CharOrderedMap map;
		
		SynchronizedOrderedMap(Int2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(int key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(int key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Int2CharOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntOrderedSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return (IntOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Int2CharMap.Entry> int2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Int2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2CharSortedMap {
		Int2CharSortedMap map;
		
		SynchronizedSortedMap(Int2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2CharSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2CharSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2CharSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Int2CharSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2CharSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2CharSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2CharSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2CharMap implements Int2CharMap {
		Int2CharMap map;
		CharCollection values;
		IntSet keys;
		ObjectSet<Int2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(int key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(int key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(int key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(int key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(int key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(int key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Int2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(IntCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(int key, IntCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(int key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Int2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(int key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Int2CharMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2CharMap.Entry> int2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2CharEntrySet(), mutex);
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
		public Character put(Integer key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Integer key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Integer key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Integer key, BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Integer key, Function<? super Integer, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Integer key, BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Integer key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}