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
import speiger.src.collections.shorts.functions.consumer.ShortBooleanConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.function.ShortBooleanUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.utils.BooleanCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Short2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2BooleanMap.Entry> fastIterator(Short2BooleanMap map) {
		ObjectSet<Short2BooleanMap.Entry> entries = map.short2BooleanEntrySet();
		return entries instanceof Short2BooleanMap.FastEntrySet ? ((Short2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2BooleanMap.Entry> fastIterable(Short2BooleanMap map) {
		ObjectSet<Short2BooleanMap.Entry> entries = map.short2BooleanEntrySet();
		return map instanceof Short2BooleanMap.FastEntrySet ? new ObjectIterable<Short2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Short2BooleanMap.Entry> iterator() { return ((Short2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2BooleanMap.Entry> action) { ((Short2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2BooleanMap map, Consumer<Short2BooleanMap.Entry> action) {
		ObjectSet<Short2BooleanMap.Entry> entries = map.short2BooleanEntrySet();
		if(entries instanceof Short2BooleanMap.FastEntrySet) ((Short2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanMap synchronize(Short2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanMap synchronize(Short2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanSortedMap synchronize(Short2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanSortedMap synchronize(Short2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanOrderedMap synchronize(Short2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanOrderedMap synchronize(Short2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanNavigableMap synchronize(Short2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2BooleanNavigableMap synchronize(Short2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2BooleanMap unmodifiable(Short2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2BooleanOrderedMap unmodifiable(Short2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2BooleanSortedMap unmodifiable(Short2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2BooleanNavigableMap unmodifiable(Short2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2BooleanMap.Entry unmodifiable(Short2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2BooleanMap.Entry unmodifiable(Map.Entry<Short, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2BooleanMap singleton(short key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2BooleanMap {
		final short key;
		final boolean value;
		ShortSet keySet;
		BooleanCollection values;
		ObjectSet<Short2BooleanMap.Entry> entrySet;
		
		SingletonMap(short key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(short key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(short key, boolean defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public boolean computeBoolean(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(short key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Short2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2BooleanMap.Entry> short2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2BooleanMap {
		@Override
		public boolean put(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(short key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(short key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(short key, boolean defaultValue) { return defaultValue; }
		@Override
		public boolean computeBoolean(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(short key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Short2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Short2BooleanMap.Entry> short2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2BooleanMap.Entry entry) {
			super(entry.getShortKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(short key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2BooleanNavigableMap {
		Short2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Short2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2BooleanNavigableMap descendingMap() { return Short2BooleanMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet keySet() { return ShortSets.unmodifiable(map.keySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2BooleanMap.Entry firstEntry() { return Short2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2BooleanMap.Entry lastEntry() { return Short2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2BooleanNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2BooleanNavigableMap headMap(short toKey, boolean inclusive) { return Short2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2BooleanNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2BooleanNavigableMap subMap(short fromKey, short toKey) { return Short2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2BooleanNavigableMap headMap(short toKey) { return Short2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2BooleanNavigableMap tailMap(short fromKey) { return Short2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2BooleanMap.Entry lowerEntry(short key) { return Short2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2BooleanMap.Entry higherEntry(short key) { return Short2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2BooleanMap.Entry floorEntry(short key) { return Short2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2BooleanMap.Entry ceilingEntry(short key) { return Short2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2BooleanNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2BooleanOrderedMap {
		Short2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Short2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Short2BooleanOrderedMap copy() { return map.copy(); }
		@Override
		public ShortOrderedSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return (ShortOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Short2BooleanMap.Entry> short2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.short2BooleanEntrySet());
			return (ObjectOrderedSet<Short2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2BooleanSortedMap {
		Short2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Short2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2BooleanSortedMap subMap(short fromKey, short toKey) { return Short2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2BooleanSortedMap headMap(short toKey) { return Short2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2BooleanSortedMap tailMap(short fromKey) { return Short2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Short2BooleanSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2BooleanMap implements Short2BooleanMap {
		Short2BooleanMap map;
		BooleanCollection values;
		ShortSet keys;
		ObjectSet<Short2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Short2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(short key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(short key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(short key) {
			boolean type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public boolean getOrDefault(short key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean computeBoolean(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(short key, ShortPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(short key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(short key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Short2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(ShortBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(Short2BooleanMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Short2BooleanMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2BooleanMap.Entry> short2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Short2BooleanMap.Entry>
	{
		ObjectOrderedSet<Short2BooleanMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Short2BooleanMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Short2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Short2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Short2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Short2BooleanMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Short2BooleanMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Short2BooleanMap.Entry> iterator(Short2BooleanMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Short2BooleanMap.Entry first() { return set.first(); }
		@Override
		public Short2BooleanMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Short2BooleanMap.Entry last() { return set.last(); }
		@Override
		public Short2BooleanMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2BooleanMap.Entry>
	{
		ObjectSet<Short2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Short2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Short2BooleanMap.Entry>() {
				ObjectIterator<Short2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2BooleanMap.Entry next() { return Short2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2BooleanNavigableMap {
		Short2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Short2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ShortNavigableSet keySet() { synchronized(mutex) { return ShortSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Short2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Short2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2BooleanNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2BooleanNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2BooleanNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2BooleanNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2BooleanNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2BooleanNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2BooleanMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2BooleanMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2BooleanMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2BooleanMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2BooleanNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2BooleanMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2BooleanMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2BooleanMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2BooleanMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2BooleanOrderedMap {
		Short2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Short2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(short key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(short key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Short2BooleanOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortOrderedSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return (ShortOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Short2BooleanMap.Entry> short2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2BooleanEntrySet(), mutex);
			return (ObjectOrderedSet<Short2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2BooleanSortedMap {
		Short2BooleanSortedMap map;
		
		SynchronizedSortedMap(Short2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2BooleanSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2BooleanSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2BooleanSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Short2BooleanSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2BooleanSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2BooleanSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2BooleanMap implements Short2BooleanMap {
		Short2BooleanMap map;
		BooleanCollection values;
		ShortSet keys;
		ObjectSet<Short2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(short key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(short key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Short2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(short key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(short key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Short2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(ShortBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(short key, ShortBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(short key, ShortPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(short key, ShortBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsent(short key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsent(key, valueProvider); } }
		@Override
		public boolean computeBooleanNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(short key, ShortPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresentNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(short key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public boolean mergeBoolean(short key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Short2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(short key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Short2BooleanMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2BooleanMap.Entry> short2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2BooleanEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Boolean get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Boolean getOrDefault(Object key, Boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Boolean put(Short key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Short key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Short key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Short key, Function<? super Short, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Short key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}