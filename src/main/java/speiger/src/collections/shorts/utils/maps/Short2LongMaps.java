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
import speiger.src.collections.shorts.functions.consumer.ShortLongConsumer;
import speiger.src.collections.shorts.functions.function.Short2LongFunction;
import speiger.src.collections.shorts.functions.function.ShortLongUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.utils.LongCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Short2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2LongMap.Entry> fastIterator(Short2LongMap map) {
		ObjectSet<Short2LongMap.Entry> entries = map.short2LongEntrySet();
		return entries instanceof Short2LongMap.FastEntrySet ? ((Short2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2LongMap.Entry> fastIterable(Short2LongMap map) {
		ObjectSet<Short2LongMap.Entry> entries = map.short2LongEntrySet();
		return map instanceof Short2LongMap.FastEntrySet ? new ObjectIterable<Short2LongMap.Entry>(){
			@Override
			public ObjectIterator<Short2LongMap.Entry> iterator() { return ((Short2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2LongMap.Entry> action) { ((Short2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2LongMap map, Consumer<Short2LongMap.Entry> action) {
		ObjectSet<Short2LongMap.Entry> entries = map.short2LongEntrySet();
		if(entries instanceof Short2LongMap.FastEntrySet) ((Short2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongMap synchronize(Short2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongMap synchronize(Short2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongSortedMap synchronize(Short2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongSortedMap synchronize(Short2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongOrderedMap synchronize(Short2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongOrderedMap synchronize(Short2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongNavigableMap synchronize(Short2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2LongNavigableMap synchronize(Short2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2LongMap unmodifiable(Short2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2LongOrderedMap unmodifiable(Short2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2LongSortedMap unmodifiable(Short2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2LongNavigableMap unmodifiable(Short2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2LongMap.Entry unmodifiable(Short2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2LongMap.Entry unmodifiable(Map.Entry<Short, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2LongMap singleton(short key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2LongMap {
		final short key;
		final long value;
		ShortSet keySet;
		LongCollection values;
		ObjectSet<Short2LongMap.Entry> entrySet;
		
		SingletonMap(short key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(short key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(short key, long defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public long computeLong(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Short2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2LongMap.Entry> short2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2LongMap {
		@Override
		public long put(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(short key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(short key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(short key, long defaultValue) { return defaultValue; }
		@Override
		public long computeLong(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Short2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Short2LongMap.Entry> short2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2LongMap.Entry entry) {
			super(entry.getShortKey(), entry.getLongValue());
		}
		
		@Override
		public void set(short key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2LongNavigableMap {
		Short2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Short2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2LongNavigableMap descendingMap() { return Short2LongMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet keySet() { return ShortSets.unmodifiable(map.keySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2LongMap.Entry firstEntry() { return Short2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2LongMap.Entry lastEntry() { return Short2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2LongNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2LongNavigableMap headMap(short toKey, boolean inclusive) { return Short2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2LongNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2LongNavigableMap subMap(short fromKey, short toKey) { return Short2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2LongNavigableMap headMap(short toKey) { return Short2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2LongNavigableMap tailMap(short fromKey) { return Short2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2LongMap.Entry lowerEntry(short key) { return Short2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2LongMap.Entry higherEntry(short key) { return Short2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2LongMap.Entry floorEntry(short key) { return Short2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2LongMap.Entry ceilingEntry(short key) { return Short2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2LongNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2LongOrderedMap {
		Short2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Short2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Short2LongOrderedMap copy() { return map.copy(); }
		@Override
		public ShortOrderedSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return (ShortOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Short2LongMap.Entry> short2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.short2LongEntrySet());
			return (ObjectOrderedSet<Short2LongMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2LongSortedMap {
		Short2LongSortedMap map;
		
		UnmodifyableSortedMap(Short2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2LongSortedMap subMap(short fromKey, short toKey) { return Short2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2LongSortedMap headMap(short toKey) { return Short2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2LongSortedMap tailMap(short fromKey) { return Short2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Short2LongSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2LongMap implements Short2LongMap {
		Short2LongMap map;
		LongCollection values;
		ShortSet keys;
		ObjectSet<Short2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Short2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(short key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(short key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(short key) {
			long type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public long getOrDefault(short key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long computeLong(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsent(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllLong(Short2LongMap m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceLongs(ShortLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceLongs(Short2LongMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Short2LongMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2LongMap.Entry> short2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Short2LongMap.Entry>
	{
		ObjectOrderedSet<Short2LongMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Short2LongMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Short2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Short2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Short2LongMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Short2LongMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Short2LongMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Short2LongMap.Entry> iterator(Short2LongMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Short2LongMap.Entry first() { return set.first(); }
		@Override
		public Short2LongMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Short2LongMap.Entry last() { return set.last(); }
		@Override
		public Short2LongMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2LongMap.Entry>
	{
		ObjectSet<Short2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Short2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2LongMap.Entry> iterator() {
			return new ObjectIterator<Short2LongMap.Entry>() {
				ObjectIterator<Short2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2LongMap.Entry next() { return Short2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2LongNavigableMap {
		Short2LongNavigableMap map;
		
		SynchronizedNavigableMap(Short2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2LongNavigableMap descendingMap() { synchronized(mutex) { return Short2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ShortNavigableSet keySet() { synchronized(mutex) { return ShortSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Short2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2LongMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Short2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2LongNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2LongNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2LongNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2LongNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2LongNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2LongNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2LongMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2LongMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2LongMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2LongMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2LongNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2LongNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2LongMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2LongMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2LongMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2LongMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2LongOrderedMap {
		Short2LongOrderedMap map;
		
		SynchronizedOrderedMap(Short2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(short key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(short key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Short2LongOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortOrderedSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return (ShortOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Short2LongMap.Entry> short2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2LongEntrySet(), mutex);
			return (ObjectOrderedSet<Short2LongMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2LongSortedMap {
		Short2LongSortedMap map;
		
		SynchronizedSortedMap(Short2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2LongSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2LongSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2LongSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Short2LongSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2LongSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2LongSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2LongSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2LongMap implements Short2LongMap {
		Short2LongMap map;
		LongCollection values;
		ShortSet keys;
		ObjectSet<Short2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(short key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(short key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(short key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(short key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(short key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(short key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Short2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(ShortLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(short key, ShortLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongNonDefault(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public long supplyLongIfAbsent(short key, LongSupplier valueProvider) { synchronized(mutex) { return map.supplyLongIfAbsent(key, valueProvider); } }
		@Override
		public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) { synchronized(mutex) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Short2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(short key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Short2LongMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2LongMap.Entry> short2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2LongEntrySet(), mutex);
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
		public Long put(Short key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Short key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Short key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Short key, BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Short key, Function<? super Short, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Short key, BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Short key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}