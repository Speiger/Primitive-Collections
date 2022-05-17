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
import speiger.src.collections.longs.functions.consumer.LongByteConsumer;
import speiger.src.collections.longs.functions.function.Long2ByteFunction;
import speiger.src.collections.longs.functions.function.LongByteUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.bytes.utils.ByteSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Long2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2ByteMap.Entry> fastIterator(Long2ByteMap map) {
		ObjectSet<Long2ByteMap.Entry> entries = map.long2ByteEntrySet();
		return entries instanceof Long2ByteMap.FastEntrySet ? ((Long2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2ByteMap.Entry> fastIterable(Long2ByteMap map) {
		ObjectSet<Long2ByteMap.Entry> entries = map.long2ByteEntrySet();
		return map instanceof Long2ByteMap.FastEntrySet ? new ObjectIterable<Long2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Long2ByteMap.Entry> iterator() { return ((Long2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2ByteMap.Entry> action) { ((Long2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2ByteMap map, Consumer<Long2ByteMap.Entry> action) {
		ObjectSet<Long2ByteMap.Entry> entries = map.long2ByteEntrySet();
		if(entries instanceof Long2ByteMap.FastEntrySet) ((Long2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteMap synchronize(Long2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteMap synchronize(Long2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteSortedMap synchronize(Long2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteSortedMap synchronize(Long2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteOrderedMap synchronize(Long2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteOrderedMap synchronize(Long2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteNavigableMap synchronize(Long2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2ByteNavigableMap synchronize(Long2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2ByteMap unmodifiable(Long2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ByteOrderedMap unmodifiable(Long2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ByteSortedMap unmodifiable(Long2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2ByteNavigableMap unmodifiable(Long2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2ByteMap.Entry unmodifiable(Long2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2ByteMap.Entry unmodifiable(Map.Entry<Long, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2ByteMap singleton(long key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2ByteMap {
		final long key;
		final byte value;
		LongSet keySet;
		ByteCollection values;
		ObjectSet<Long2ByteMap.Entry> entrySet;
		
		SingletonMap(long key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(long key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(long key, byte defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2ByteMap.Entry> long2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2ByteMap {
		@Override
		public byte put(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(long key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(long key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(long key, byte defaultValue) { return (byte)0; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Long2ByteMap.Entry> long2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2ByteMap.Entry entry) {
			super(entry.getLongKey(), entry.getByteValue());
		}
		
		@Override
		public void set(long key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2ByteNavigableMap {
		Long2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Long2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2ByteNavigableMap descendingMap() { return Long2ByteMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2ByteMap.Entry firstEntry() { return Long2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2ByteMap.Entry lastEntry() { return Long2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ByteNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2ByteNavigableMap headMap(long toKey, boolean inclusive) { return Long2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2ByteNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2ByteNavigableMap subMap(long fromKey, long toKey) { return Long2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ByteNavigableMap headMap(long toKey) { return Long2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ByteNavigableMap tailMap(long fromKey) { return Long2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2ByteMap.Entry lowerEntry(long key) { return Long2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2ByteMap.Entry higherEntry(long key) { return Long2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2ByteMap.Entry floorEntry(long key) { return Long2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2ByteMap.Entry ceilingEntry(long key) { return Long2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2ByteOrderedMap {
		Long2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Long2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Long2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2ByteSortedMap {
		Long2ByteSortedMap map;
		
		UnmodifyableSortedMap(Long2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2ByteSortedMap subMap(long fromKey, long toKey) { return Long2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ByteSortedMap headMap(long toKey) { return Long2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ByteSortedMap tailMap(long fromKey) { return Long2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Long2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2ByteMap implements Long2ByteMap {
		Long2ByteMap map;
		ByteCollection values;
		LongSet keys;
		ObjectSet<Long2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Long2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(long key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(long key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(long key) { return map.get(key); }
		@Override
		public byte getOrDefault(long key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2ByteMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2ByteMap.Entry> long2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2ByteMap.Entry>
	{
		ObjectSet<Long2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Long2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2ByteMap.Entry> iterator() {
			return new ObjectIterator<Long2ByteMap.Entry>() {
				ObjectIterator<Long2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2ByteMap.Entry next() { return Long2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2ByteNavigableMap {
		Long2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Long2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2ByteNavigableMap descendingMap() { synchronized(mutex) { return Long2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2ByteNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2ByteNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2ByteNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2ByteNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ByteNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ByteNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2ByteMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2ByteMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2ByteMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2ByteMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2ByteNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ByteNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ByteNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ByteNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ByteNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ByteNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2ByteMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2ByteMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2ByteMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2ByteMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2ByteOrderedMap {
		Long2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Long2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(long key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(long key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Long2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2ByteSortedMap {
		Long2ByteSortedMap map;
		
		SynchronizedSortedMap(Long2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2ByteSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ByteSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ByteSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Long2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2ByteSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ByteSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ByteSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2ByteMap implements Long2ByteMap {
		Long2ByteMap map;
		ByteCollection values;
		LongSet keys;
		ObjectSet<Long2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(long key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(long key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(long key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(long key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(long key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(long key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Long2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(LongByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(long key, LongByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(long key, Long2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(long key, LongByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte mergeByte(long key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Long2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(long key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2ByteMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2ByteMap.Entry> long2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2ByteEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Byte get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Byte getOrDefault(Object key, Byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Byte put(Long key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Long key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Long key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Long key, Function<? super Long, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Long key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}