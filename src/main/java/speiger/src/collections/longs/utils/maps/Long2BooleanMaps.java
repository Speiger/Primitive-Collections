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
import speiger.src.collections.longs.functions.consumer.LongBooleanConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongBooleanUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.utils.BooleanCollections;
import speiger.src.collections.booleans.utils.BooleanSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Long2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2BooleanMap.Entry> fastIterator(Long2BooleanMap map) {
		ObjectSet<Long2BooleanMap.Entry> entries = map.long2BooleanEntrySet();
		return entries instanceof Long2BooleanMap.FastEntrySet ? ((Long2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2BooleanMap.Entry> fastIterable(Long2BooleanMap map) {
		ObjectSet<Long2BooleanMap.Entry> entries = map.long2BooleanEntrySet();
		return map instanceof Long2BooleanMap.FastEntrySet ? new ObjectIterable<Long2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Long2BooleanMap.Entry> iterator() { return ((Long2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2BooleanMap.Entry> action) { ((Long2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2BooleanMap map, Consumer<Long2BooleanMap.Entry> action) {
		ObjectSet<Long2BooleanMap.Entry> entries = map.long2BooleanEntrySet();
		if(entries instanceof Long2BooleanMap.FastEntrySet) ((Long2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanMap synchronize(Long2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanMap synchronize(Long2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanSortedMap synchronize(Long2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanSortedMap synchronize(Long2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanOrderedMap synchronize(Long2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanOrderedMap synchronize(Long2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanNavigableMap synchronize(Long2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2BooleanNavigableMap synchronize(Long2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2BooleanMap unmodifiable(Long2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2BooleanOrderedMap unmodifiable(Long2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2BooleanSortedMap unmodifiable(Long2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2BooleanNavigableMap unmodifiable(Long2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2BooleanMap.Entry unmodifiable(Long2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2BooleanMap.Entry unmodifiable(Map.Entry<Long, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2BooleanMap singleton(long key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2BooleanMap {
		final long key;
		final boolean value;
		LongSet keySet;
		BooleanCollection values;
		ObjectSet<Long2BooleanMap.Entry> entrySet;
		
		SingletonMap(long key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(long key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(long key, boolean defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2BooleanMap.Entry> long2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2BooleanMap {
		@Override
		public boolean put(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(long key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(long key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(long key, boolean defaultValue) { return false; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Long2BooleanMap.Entry> long2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2BooleanMap.Entry entry) {
			super(entry.getLongKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(long key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2BooleanNavigableMap {
		Long2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Long2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2BooleanNavigableMap descendingMap() { return Long2BooleanMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2BooleanMap.Entry firstEntry() { return Long2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2BooleanMap.Entry lastEntry() { return Long2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2BooleanNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2BooleanNavigableMap headMap(long toKey, boolean inclusive) { return Long2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2BooleanNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2BooleanNavigableMap subMap(long fromKey, long toKey) { return Long2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2BooleanNavigableMap headMap(long toKey) { return Long2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2BooleanNavigableMap tailMap(long fromKey) { return Long2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2BooleanMap.Entry lowerEntry(long key) { return Long2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2BooleanMap.Entry higherEntry(long key) { return Long2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2BooleanMap.Entry floorEntry(long key) { return Long2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2BooleanMap.Entry ceilingEntry(long key) { return Long2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2BooleanOrderedMap {
		Long2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Long2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Long2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2BooleanSortedMap {
		Long2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Long2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2BooleanSortedMap subMap(long fromKey, long toKey) { return Long2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2BooleanSortedMap headMap(long toKey) { return Long2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2BooleanSortedMap tailMap(long fromKey) { return Long2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Long2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2BooleanMap implements Long2BooleanMap {
		Long2BooleanMap map;
		BooleanCollection values;
		LongSet keys;
		ObjectSet<Long2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Long2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(long key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(long key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(long key) { return map.get(key); }
		@Override
		public boolean getOrDefault(long key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2BooleanMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2BooleanMap.Entry> long2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2BooleanMap.Entry>
	{
		ObjectSet<Long2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Long2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Long2BooleanMap.Entry>() {
				ObjectIterator<Long2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2BooleanMap.Entry next() { return Long2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2BooleanNavigableMap {
		Long2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Long2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2BooleanNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2BooleanNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2BooleanNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2BooleanNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2BooleanNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2BooleanNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2BooleanMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2BooleanMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2BooleanMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2BooleanMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2BooleanMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2BooleanMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2BooleanMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2BooleanMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2BooleanOrderedMap {
		Long2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Long2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(long key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(long key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Long2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2BooleanSortedMap {
		Long2BooleanSortedMap map;
		
		SynchronizedSortedMap(Long2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2BooleanSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2BooleanSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2BooleanSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Long2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2BooleanSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2BooleanSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2BooleanMap implements Long2BooleanMap {
		Long2BooleanMap map;
		BooleanCollection values;
		LongSet keys;
		ObjectSet<Long2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(long key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(long key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Long2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(long key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(long key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Long2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(LongBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(long key, LongBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(long key, Long2BooleanFunction mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(long key, LongBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean mergeBoolean(long key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Long2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(long key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2BooleanMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2BooleanMap.Entry> long2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2BooleanEntrySet(), mutex);
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
		public Boolean put(Long key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Long key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Long key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Long key, Function<? super Long, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Long key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}