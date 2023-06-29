package speiger.src.collections.shorts.utils.maps;

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
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.consumer.ShortCharConsumer;
import speiger.src.collections.shorts.functions.function.Short2CharFunction;
import speiger.src.collections.shorts.functions.function.ShortCharUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Short2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2CharMap.Entry> fastIterator(Short2CharMap map) {
		ObjectSet<Short2CharMap.Entry> entries = map.short2CharEntrySet();
		return entries instanceof Short2CharMap.FastEntrySet ? ((Short2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2CharMap.Entry> fastIterable(Short2CharMap map) {
		ObjectSet<Short2CharMap.Entry> entries = map.short2CharEntrySet();
		return map instanceof Short2CharMap.FastEntrySet ? new ObjectIterable<Short2CharMap.Entry>(){
			@Override
			public ObjectIterator<Short2CharMap.Entry> iterator() { return ((Short2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2CharMap.Entry> action) { ((Short2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2CharMap map, Consumer<Short2CharMap.Entry> action) {
		ObjectSet<Short2CharMap.Entry> entries = map.short2CharEntrySet();
		if(entries instanceof Short2CharMap.FastEntrySet) ((Short2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharMap synchronize(Short2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharMap synchronize(Short2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharSortedMap synchronize(Short2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharSortedMap synchronize(Short2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharOrderedMap synchronize(Short2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharOrderedMap synchronize(Short2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharNavigableMap synchronize(Short2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2CharNavigableMap synchronize(Short2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2CharMap unmodifiable(Short2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2CharOrderedMap unmodifiable(Short2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2CharSortedMap unmodifiable(Short2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2CharNavigableMap unmodifiable(Short2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2CharMap.Entry unmodifiable(Short2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2CharMap.Entry unmodifiable(Map.Entry<Short, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2CharMap singleton(short key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2CharMap {
		final short key;
		final char value;
		ShortSet keySet;
		CharCollection values;
		ObjectSet<Short2CharMap.Entry> entrySet;
		
		SingletonMap(short key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(short key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(short key, char defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public char computeChar(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2CharMap.Entry> short2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2CharMap {
		@Override
		public char put(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(short key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(short key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(short key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Short2CharMap.Entry> short2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2CharMap.Entry entry) {
			super(entry.getShortKey(), entry.getCharValue());
		}
		
		@Override
		public void set(short key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2CharNavigableMap {
		Short2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Short2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2CharNavigableMap descendingMap() { return Short2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet keySet() { return ShortSets.unmodifiable(map.keySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2CharMap.Entry firstEntry() { return Short2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2CharMap.Entry lastEntry() { return Short2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2CharNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2CharNavigableMap headMap(short toKey, boolean inclusive) { return Short2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2CharNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2CharNavigableMap subMap(short fromKey, short toKey) { return Short2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2CharNavigableMap headMap(short toKey) { return Short2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2CharNavigableMap tailMap(short fromKey) { return Short2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public short lowerKey(short key) { return map.lowerKey(key); }
		@Override
		public short higherKey(short key) { return map.higherKey(key); }
		@Override
		public short floorKey(short key) { return map.floorKey(key); }
		@Override
		public short ceilingKey(short key) { return map.ceilingKey(key); }
		@Override
		public Short2CharMap.Entry lowerEntry(short key) { return Short2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2CharMap.Entry higherEntry(short key) { return Short2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2CharMap.Entry floorEntry(short key) { return Short2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2CharMap.Entry ceilingEntry(short key) { return Short2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2CharNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2CharOrderedMap {
		Short2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Short2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Short2CharOrderedMap copy() { return map.copy(); }
		@Override
		public ShortOrderedSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return (ShortOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Short2CharMap.Entry> short2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.short2CharEntrySet());
			return (ObjectOrderedSet<Short2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2CharSortedMap {
		Short2CharSortedMap map;
		
		UnmodifyableSortedMap(Short2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2CharSortedMap subMap(short fromKey, short toKey) { return Short2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2CharSortedMap headMap(short toKey) { return Short2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2CharSortedMap tailMap(short fromKey) { return Short2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public ShortSortedSet keySet() { return ShortSets.unmodifiable(map.keySet()); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Short2CharSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2CharMap implements Short2CharMap {
		Short2CharMap map;
		CharCollection values;
		ShortSet keys;
		ObjectSet<Short2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Short2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(short key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(short key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(short key) {
			char type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(short key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Short2CharMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Short2CharMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2CharMap.Entry> short2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Short2CharMap.Entry>
	{
		ObjectOrderedSet<Short2CharMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Short2CharMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Short2CharMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> iterator(Short2CharMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Short2CharMap.Entry first() { return set.first(); }
		@Override
		public Short2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Short2CharMap.Entry last() { return set.last(); }
		@Override
		public Short2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2CharMap.Entry>
	{
		ObjectSet<Short2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Short2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2CharMap.Entry> iterator() {
			return new ObjectIterator<Short2CharMap.Entry>() {
				ObjectIterator<Short2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2CharMap.Entry next() { return Short2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2CharNavigableMap {
		Short2CharNavigableMap map;
		
		SynchronizedNavigableMap(Short2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2CharNavigableMap descendingMap() { synchronized(mutex) { return Short2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ShortNavigableSet keySet() { synchronized(mutex) { return ShortSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Short2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2CharMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Short2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2CharNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2CharNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2CharNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2CharNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2CharNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2CharNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2CharMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2CharMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2CharMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2CharMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2CharNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2CharNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(short e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public short getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(short e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public short getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Short lowerKey(Short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Short floorKey(Short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Short ceilingKey(Short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Short higherKey(Short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Short2CharMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2CharMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2CharMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2CharMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2CharOrderedMap {
		Short2CharOrderedMap map;
		
		SynchronizedOrderedMap(Short2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(short key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(short key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Short2CharOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortOrderedSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return (ShortOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Short2CharMap.Entry> short2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Short2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2CharSortedMap {
		Short2CharSortedMap map;
		
		SynchronizedSortedMap(Short2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2CharSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2CharSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2CharSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public ShortSortedSet keySet() { synchronized(mutex) { return ShortSets.synchronize(map.keySet(), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Short2CharSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2CharSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2CharSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2CharSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2CharMap implements Short2CharMap {
		Short2CharMap map;
		CharCollection values;
		ShortSet keys;
		ObjectSet<Short2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(short key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(short key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(short key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(short key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(short key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(short key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Short2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(ShortCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(short key, ShortCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(short key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char computeCharNonDefault(short key, ShortCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(short key, Short2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(short key, ShortCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsentNonDefault(short key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(short key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Short2CharMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2CharMap.Entry> short2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2CharEntrySet(), mutex);
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
		public Character put(Short key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Short key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Short key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Short key, Function<? super Short, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Short key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}