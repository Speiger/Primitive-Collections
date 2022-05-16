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
import speiger.src.collections.longs.functions.consumer.LongLongConsumer;
import speiger.src.collections.longs.functions.function.Long2LongFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.utils.LongCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Long2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2LongMap.Entry> fastIterator(Long2LongMap map) {
		ObjectSet<Long2LongMap.Entry> entries = map.long2LongEntrySet();
		return entries instanceof Long2LongMap.FastEntrySet ? ((Long2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2LongMap.Entry> fastIterable(Long2LongMap map) {
		ObjectSet<Long2LongMap.Entry> entries = map.long2LongEntrySet();
		return map instanceof Long2LongMap.FastEntrySet ? new ObjectIterable<Long2LongMap.Entry>(){
			@Override
			public ObjectIterator<Long2LongMap.Entry> iterator() { return ((Long2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2LongMap.Entry> action) { ((Long2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2LongMap map, Consumer<Long2LongMap.Entry> action) {
		ObjectSet<Long2LongMap.Entry> entries = map.long2LongEntrySet();
		if(entries instanceof Long2LongMap.FastEntrySet) ((Long2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongMap synchronize(Long2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongMap synchronize(Long2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongSortedMap synchronize(Long2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongSortedMap synchronize(Long2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongOrderedMap synchronize(Long2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongOrderedMap synchronize(Long2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongNavigableMap synchronize(Long2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2LongNavigableMap synchronize(Long2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2LongMap unmodifiable(Long2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2LongOrderedMap unmodifiable(Long2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2LongSortedMap unmodifiable(Long2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2LongNavigableMap unmodifiable(Long2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2LongMap.Entry unmodifiable(Long2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2LongMap.Entry unmodifiable(Map.Entry<Long, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2LongMap singleton(long key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2LongMap {
		final long key;
		final long value;
		LongSet keySet;
		LongCollection values;
		ObjectSet<Long2LongMap.Entry> entrySet;
		
		SingletonMap(long key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(long key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(long key, long defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2LongMap.Entry> long2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2LongMap {
		@Override
		public long put(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(long key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(long key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(long key, long defaultValue) { return 0L; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Long2LongMap.Entry> long2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2LongMap.Entry entry) {
			super(entry.getLongKey(), entry.getLongValue());
		}
		
		@Override
		public void set(long key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2LongNavigableMap {
		Long2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Long2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2LongNavigableMap descendingMap() { return Long2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2LongMap.Entry firstEntry() { return Long2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2LongMap.Entry lastEntry() { return Long2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2LongNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2LongNavigableMap headMap(long toKey, boolean inclusive) { return Long2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2LongNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2LongNavigableMap subMap(long fromKey, long toKey) { return Long2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2LongNavigableMap headMap(long toKey) { return Long2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2LongNavigableMap tailMap(long fromKey) { return Long2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2LongMap.Entry lowerEntry(long key) { return Long2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2LongMap.Entry higherEntry(long key) { return Long2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2LongMap.Entry floorEntry(long key) { return Long2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2LongMap.Entry ceilingEntry(long key) { return Long2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2LongOrderedMap {
		Long2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Long2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Long2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2LongSortedMap {
		Long2LongSortedMap map;
		
		UnmodifyableSortedMap(Long2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2LongSortedMap subMap(long fromKey, long toKey) { return Long2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2LongSortedMap headMap(long toKey) { return Long2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2LongSortedMap tailMap(long fromKey) { return Long2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Long2LongSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2LongMap implements Long2LongMap {
		Long2LongMap map;
		LongCollection values;
		LongSet keys;
		ObjectSet<Long2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Long2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(long key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(long key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(long key) { return map.get(key); }
		@Override
		public long getOrDefault(long key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2LongMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2LongMap.Entry> long2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2LongMap.Entry>
	{
		ObjectSet<Long2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Long2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2LongMap.Entry> iterator() {
			return new ObjectIterator<Long2LongMap.Entry>() {
				ObjectIterator<Long2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2LongMap.Entry next() { return Long2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2LongNavigableMap {
		Long2LongNavigableMap map;
		
		SynchronizedNavigableMap(Long2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2LongNavigableMap descendingMap() { synchronized(mutex) { return Long2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2LongMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2LongNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2LongNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2LongNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2LongNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2LongNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2LongNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2LongMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2LongMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2LongMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2LongMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2LongNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2LongNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2LongNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2LongNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2LongNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2LongNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2LongMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2LongMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2LongMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2LongMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2LongOrderedMap {
		Long2LongOrderedMap map;
		
		SynchronizedOrderedMap(Long2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(long key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(long key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Long2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2LongSortedMap {
		Long2LongSortedMap map;
		
		SynchronizedSortedMap(Long2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2LongSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2LongSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2LongSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Long2LongSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2LongSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2LongSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2LongSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2LongMap implements Long2LongMap {
		Long2LongMap map;
		LongCollection values;
		LongSet keys;
		ObjectSet<Long2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(long key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(long key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(long key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(long key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(long key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(long key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Long2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(long key, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(long key, Long2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(long key, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(long key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Long2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(long key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2LongMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2LongMap.Entry> long2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2LongEntrySet(), mutex);
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
		public Long put(Long key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Long key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Long key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Long key, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Long key, Function<? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Long key, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Long key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}