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
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.consumer.LongShortConsumer;
import speiger.src.collections.longs.functions.function.Long2ShortFunction;
import speiger.src.collections.longs.functions.function.LongShortUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortCollections;
import speiger.src.collections.shorts.utils.ShortSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Long2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2ShortMap.Entry> fastIterator(Long2ShortMap map) {
		ObjectSet<Long2ShortMap.Entry> entries = map.long2ShortEntrySet();
		return entries instanceof Long2ShortMap.FastEntrySet ? ((Long2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2ShortMap.Entry> fastIterable(Long2ShortMap map) {
		ObjectSet<Long2ShortMap.Entry> entries = map.long2ShortEntrySet();
		return map instanceof Long2ShortMap.FastEntrySet ? new ObjectIterable<Long2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Long2ShortMap.Entry> iterator() { return ((Long2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2ShortMap.Entry> action) { ((Long2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2ShortMap map, Consumer<Long2ShortMap.Entry> action) {
		ObjectSet<Long2ShortMap.Entry> entries = map.long2ShortEntrySet();
		if(entries instanceof Long2ShortMap.FastEntrySet) ((Long2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortMap synchronize(Long2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortMap synchronize(Long2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortSortedMap synchronize(Long2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortSortedMap synchronize(Long2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortOrderedMap synchronize(Long2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortOrderedMap synchronize(Long2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortNavigableMap synchronize(Long2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ShortNavigableMap synchronize(Long2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2ShortMap unmodifiable(Long2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ShortOrderedMap unmodifiable(Long2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ShortSortedMap unmodifiable(Long2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ShortNavigableMap unmodifiable(Long2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2ShortMap.Entry unmodifiable(Long2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2ShortMap.Entry unmodifiable(Map.Entry<Long, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2ShortMap singleton(long key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2ShortMap {
		final long key;
		final short value;
		LongSet keySet;
		ShortCollection values;
		ObjectSet<Long2ShortMap.Entry> entrySet;
		
		SingletonMap(long key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(long key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(long key, short defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2ShortMap.Entry> long2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2ShortMap {
		@Override
		public short put(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(long key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(long key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(long key, short defaultValue) { return (short)0; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Long2ShortMap.Entry> long2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2ShortMap.Entry entry) {
			super(entry.getLongKey(), entry.getShortValue());
		}
		
		@Override
		public void set(long key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2ShortNavigableMap {
		Long2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Long2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2ShortNavigableMap descendingMap() { return Long2ShortMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2ShortMap.Entry firstEntry() { return Long2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2ShortMap.Entry lastEntry() { return Long2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ShortNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2ShortNavigableMap headMap(long toKey, boolean inclusive) { return Long2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2ShortNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2ShortNavigableMap subMap(long fromKey, long toKey) { return Long2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ShortNavigableMap headMap(long toKey) { return Long2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ShortNavigableMap tailMap(long fromKey) { return Long2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2ShortMap.Entry lowerEntry(long key) { return Long2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2ShortMap.Entry higherEntry(long key) { return Long2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2ShortMap.Entry floorEntry(long key) { return Long2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2ShortMap.Entry ceilingEntry(long key) { return Long2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2ShortOrderedMap {
		Long2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Long2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Long2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2ShortSortedMap {
		Long2ShortSortedMap map;
		
		UnmodifyableSortedMap(Long2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2ShortSortedMap subMap(long fromKey, long toKey) { return Long2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ShortSortedMap headMap(long toKey) { return Long2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ShortSortedMap tailMap(long fromKey) { return Long2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Long2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2ShortMap implements Long2ShortMap {
		Long2ShortMap map;
		ShortCollection values;
		LongSet keys;
		ObjectSet<Long2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Long2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(long key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(long key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(long key) { return map.get(key); }
		@Override
		public short getOrDefault(long key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2ShortMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2ShortMap.Entry> long2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2ShortMap.Entry>
	{
		ObjectSet<Long2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Long2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2ShortMap.Entry> iterator() {
			return new ObjectIterator<Long2ShortMap.Entry>() {
				ObjectIterator<Long2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2ShortMap.Entry next() { return Long2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2ShortNavigableMap {
		Long2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Long2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2ShortNavigableMap descendingMap() { synchronized(mutex) { return Long2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2ShortNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2ShortNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2ShortNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2ShortNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ShortNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ShortNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2ShortMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2ShortMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2ShortMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2ShortMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2ShortNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ShortNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ShortNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ShortNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ShortNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ShortNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2ShortMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2ShortMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2ShortMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2ShortMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2ShortOrderedMap {
		Long2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Long2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(long key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(long key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Long2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2ShortSortedMap {
		Long2ShortSortedMap map;
		
		SynchronizedSortedMap(Long2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2ShortSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ShortSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ShortSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Long2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2ShortSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ShortSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ShortSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2ShortMap implements Long2ShortMap {
		Long2ShortMap map;
		ShortCollection values;
		LongSet keys;
		ObjectSet<Long2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(long key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(long key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(long key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(long key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(long key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(long key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Long2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(LongShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(long key, LongShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(long key, Long2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(long key, LongShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short mergeShort(long key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Long2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(long key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2ShortMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2ShortMap.Entry> long2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2ShortEntrySet(), mutex);
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
		public Short put(Long key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Long key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Long key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Long key, Function<? super Long, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Long key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}