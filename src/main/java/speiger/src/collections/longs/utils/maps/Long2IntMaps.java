package speiger.src.collections.longs.utils.maps;

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
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.consumer.LongIntConsumer;
import speiger.src.collections.longs.functions.function.Long2IntFunction;
import speiger.src.collections.longs.functions.function.LongIntUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.utils.IntCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Long2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2IntMap.Entry> fastIterator(Long2IntMap map) {
		ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
		return entries instanceof Long2IntMap.FastEntrySet ? ((Long2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2IntMap.Entry> fastIterable(Long2IntMap map) {
		ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
		return map instanceof Long2IntMap.FastEntrySet ? new ObjectIterable<Long2IntMap.Entry>(){
			@Override
			public ObjectIterator<Long2IntMap.Entry> iterator() { return ((Long2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2IntMap.Entry> action) { ((Long2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2IntMap map, Consumer<Long2IntMap.Entry> action) {
		ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
		if(entries instanceof Long2IntMap.FastEntrySet) ((Long2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntMap synchronize(Long2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntMap synchronize(Long2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntSortedMap synchronize(Long2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntSortedMap synchronize(Long2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntOrderedMap synchronize(Long2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntOrderedMap synchronize(Long2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntNavigableMap synchronize(Long2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2IntNavigableMap synchronize(Long2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2IntMap unmodifiable(Long2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2IntOrderedMap unmodifiable(Long2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2IntSortedMap unmodifiable(Long2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2IntNavigableMap unmodifiable(Long2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2IntMap.Entry unmodifiable(Long2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2IntMap.Entry unmodifiable(Map.Entry<Long, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2IntMap singleton(long key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2IntMap {
		final long key;
		final int value;
		LongSet keySet;
		IntCollection values;
		ObjectSet<Long2IntMap.Entry> entrySet;
		
		SingletonMap(long key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(long key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(long key, int defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public int computeInt(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(long key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Long2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2IntMap {
		@Override
		public int put(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(long key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(long key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(long key, int defaultValue) { return defaultValue; }
		@Override
		public int computeInt(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(long key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Long2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2IntMap.Entry entry) {
			super(entry.getLongKey(), entry.getIntValue());
		}
		
		@Override
		public void set(long key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2IntNavigableMap {
		Long2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Long2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2IntNavigableMap descendingMap() { return Long2IntMaps.unmodifiable(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet keySet() { return LongSets.unmodifiable(map.keySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2IntMap.Entry firstEntry() { return Long2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2IntMap.Entry lastEntry() { return Long2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2IntNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2IntNavigableMap headMap(long toKey, boolean inclusive) { return Long2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2IntNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2IntNavigableMap subMap(long fromKey, long toKey) { return Long2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2IntNavigableMap headMap(long toKey) { return Long2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2IntNavigableMap tailMap(long fromKey) { return Long2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(long e) { throw new UnsupportedOperationException(); }
		@Override
		public long getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(long e) { throw new UnsupportedOperationException(); }
		@Override
		public long getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public long lowerKey(long key) { return map.lowerKey(key); }
		@Override
		public long higherKey(long key) { return map.higherKey(key); }
		@Override
		public long floorKey(long key) { return map.floorKey(key); }
		@Override
		public long ceilingKey(long key) { return map.ceilingKey(key); }
		@Override
		public Long2IntMap.Entry lowerEntry(long key) { return Long2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2IntMap.Entry higherEntry(long key) { return Long2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2IntMap.Entry floorEntry(long key) { return Long2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2IntMap.Entry ceilingEntry(long key) { return Long2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2IntNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2IntOrderedMap {
		Long2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Long2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Long2IntOrderedMap copy() { return map.copy(); }
		@Override
		public LongOrderedSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return (LongOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Long2IntMap.Entry> long2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.long2IntEntrySet());
			return (ObjectOrderedSet<Long2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2IntSortedMap {
		Long2IntSortedMap map;
		
		UnmodifyableSortedMap(Long2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2IntSortedMap subMap(long fromKey, long toKey) { return Long2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2IntSortedMap headMap(long toKey) { return Long2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2IntSortedMap tailMap(long fromKey) { return Long2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public LongSortedSet keySet() { return LongSets.unmodifiable(map.keySet()); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Long2IntSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2IntMap implements Long2IntMap {
		Long2IntMap map;
		IntCollection values;
		LongSet keys;
		ObjectSet<Long2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Long2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(long key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(long key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(long key) {
			int type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public int getOrDefault(long key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public int computeInt(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(long key, Long2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(long key, LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(long key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(long key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Long2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(LongIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(Long2IntMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Long2IntMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Long2IntMap.Entry>
	{
		ObjectOrderedSet<Long2IntMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Long2IntMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Long2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Long2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Long2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Long2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Long2IntMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Long2IntMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Long2IntMap.Entry> iterator(Long2IntMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Long2IntMap.Entry first() { return set.first(); }
		@Override
		public Long2IntMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Long2IntMap.Entry last() { return set.last(); }
		@Override
		public Long2IntMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2IntMap.Entry>
	{
		ObjectSet<Long2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Long2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2IntMap.Entry> iterator() {
			return new ObjectIterator<Long2IntMap.Entry>() {
				ObjectIterator<Long2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2IntMap.Entry next() { return Long2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2IntNavigableMap {
		Long2IntNavigableMap map;
		
		SynchronizedNavigableMap(Long2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2IntNavigableMap descendingMap() { synchronized(mutex) { return Long2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public LongNavigableSet keySet() { synchronized(mutex) { return LongSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Long2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2IntMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Long2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2IntNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2IntNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2IntNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2IntNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2IntNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2IntNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2IntMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2IntMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2IntMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2IntMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2IntNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2IntNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(long e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public long getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(long e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public long getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Long lowerKey(Long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Long floorKey(Long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Long ceilingKey(Long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Long higherKey(Long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Long2IntMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2IntMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2IntMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2IntMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2IntOrderedMap {
		Long2IntOrderedMap map;
		
		SynchronizedOrderedMap(Long2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(long key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(long key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Long2IntOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public LongOrderedSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return (LongOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Long2IntMap.Entry> long2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2IntEntrySet(), mutex);
			return (ObjectOrderedSet<Long2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2IntSortedMap {
		Long2IntSortedMap map;
		
		SynchronizedSortedMap(Long2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2IntSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2IntSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2IntSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public LongSortedSet keySet() { synchronized(mutex) { return LongSets.synchronize(map.keySet(), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Long2IntSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2IntSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2IntSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2IntSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2IntMap implements Long2IntMap {
		Long2IntMap map;
		IntCollection values;
		LongSet keys;
		ObjectSet<Long2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(long key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(long key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(long key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(long key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(long key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(long key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Long2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(LongIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(long key, LongIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(long key, Long2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(long key, LongIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int supplyIntIfAbsent(long key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsent(key, valueProvider); } }
		@Override
		public int computeIntNonDefault(long key, LongIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsentNonDefault(long key, Long2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfPresentNonDefault(long key, LongIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public int supplyIntIfAbsentNonDefault(long key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public int mergeInt(long key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Long2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(long key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Long2IntMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2IntEntrySet(), mutex);
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
		public Integer put(Long key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Long key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Long key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Long key, BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Long key, Function<? super Long, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Long key, BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Long key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}