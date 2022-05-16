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
import speiger.src.collections.longs.functions.consumer.LongCharConsumer;
import speiger.src.collections.longs.functions.function.Long2CharFunction;
import speiger.src.collections.longs.functions.function.LongCharUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.utils.CharCollections;
import speiger.src.collections.chars.utils.CharSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Long2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2CharMap.Entry> fastIterator(Long2CharMap map) {
		ObjectSet<Long2CharMap.Entry> entries = map.long2CharEntrySet();
		return entries instanceof Long2CharMap.FastEntrySet ? ((Long2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2CharMap.Entry> fastIterable(Long2CharMap map) {
		ObjectSet<Long2CharMap.Entry> entries = map.long2CharEntrySet();
		return map instanceof Long2CharMap.FastEntrySet ? new ObjectIterable<Long2CharMap.Entry>(){
			@Override
			public ObjectIterator<Long2CharMap.Entry> iterator() { return ((Long2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2CharMap.Entry> action) { ((Long2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2CharMap map, Consumer<Long2CharMap.Entry> action) {
		ObjectSet<Long2CharMap.Entry> entries = map.long2CharEntrySet();
		if(entries instanceof Long2CharMap.FastEntrySet) ((Long2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharMap synchronize(Long2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharMap synchronize(Long2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharSortedMap synchronize(Long2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharSortedMap synchronize(Long2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharOrderedMap synchronize(Long2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharOrderedMap synchronize(Long2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharNavigableMap synchronize(Long2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2CharNavigableMap synchronize(Long2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2CharMap unmodifiable(Long2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2CharOrderedMap unmodifiable(Long2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2CharSortedMap unmodifiable(Long2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2CharNavigableMap unmodifiable(Long2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2CharMap.Entry unmodifiable(Long2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2CharMap.Entry unmodifiable(Map.Entry<Long, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2CharMap singleton(long key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2CharMap {
		final long key;
		final char value;
		LongSet keySet;
		CharCollection values;
		ObjectSet<Long2CharMap.Entry> entrySet;
		
		SingletonMap(long key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(long key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(long key, char defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2CharMap.Entry> long2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2CharMap {
		@Override
		public char put(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(long key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(long key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(long key, char defaultValue) { return (char)0; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Long2CharMap.Entry> long2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2CharMap.Entry entry) {
			super(entry.getLongKey(), entry.getCharValue());
		}
		
		@Override
		public void set(long key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2CharNavigableMap {
		Long2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Long2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2CharNavigableMap descendingMap() { return Long2CharMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2CharMap.Entry firstEntry() { return Long2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2CharMap.Entry lastEntry() { return Long2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2CharNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2CharNavigableMap headMap(long toKey, boolean inclusive) { return Long2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2CharNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2CharNavigableMap subMap(long fromKey, long toKey) { return Long2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2CharNavigableMap headMap(long toKey) { return Long2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2CharNavigableMap tailMap(long fromKey) { return Long2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2CharMap.Entry lowerEntry(long key) { return Long2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2CharMap.Entry higherEntry(long key) { return Long2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2CharMap.Entry floorEntry(long key) { return Long2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2CharMap.Entry ceilingEntry(long key) { return Long2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2CharNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2CharOrderedMap {
		Long2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Long2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Long2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2CharSortedMap {
		Long2CharSortedMap map;
		
		UnmodifyableSortedMap(Long2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2CharSortedMap subMap(long fromKey, long toKey) { return Long2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2CharSortedMap headMap(long toKey) { return Long2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2CharSortedMap tailMap(long fromKey) { return Long2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Long2CharSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2CharMap implements Long2CharMap {
		Long2CharMap map;
		CharCollection values;
		LongSet keys;
		ObjectSet<Long2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Long2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(long key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(long key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(long key) { return map.get(key); }
		@Override
		public char getOrDefault(long key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2CharMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2CharMap.Entry> long2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2CharMap.Entry>
	{
		ObjectSet<Long2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Long2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2CharMap.Entry> iterator() {
			return new ObjectIterator<Long2CharMap.Entry>() {
				ObjectIterator<Long2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2CharMap.Entry next() { return Long2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2CharNavigableMap {
		Long2CharNavigableMap map;
		
		SynchronizedNavigableMap(Long2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2CharNavigableMap descendingMap() { synchronized(mutex) { return Long2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2CharMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2CharNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2CharNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2CharNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2CharNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2CharNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2CharNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2CharMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2CharMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2CharMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2CharMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2CharNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2CharNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2CharNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2CharNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2CharNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2CharNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2CharNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2CharMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2CharMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2CharMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2CharMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2CharOrderedMap {
		Long2CharOrderedMap map;
		
		SynchronizedOrderedMap(Long2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(long key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(long key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Long2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2CharSortedMap {
		Long2CharSortedMap map;
		
		SynchronizedSortedMap(Long2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2CharSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2CharSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2CharSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Long2CharSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2CharSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2CharSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2CharSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2CharMap implements Long2CharMap {
		Long2CharMap map;
		CharCollection values;
		LongSet keys;
		ObjectSet<Long2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(long key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(long key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(long key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(long key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(long key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(long key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Long2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(LongCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(long key, LongCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(long key, Long2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(long key, LongCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char mergeChar(long key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Long2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(long key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2CharMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2CharMap.Entry> long2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2CharEntrySet(), mutex);
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
		public Character put(Long key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Long key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Long key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Long key, BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Long key, Function<? super Long, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Long key, BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Long key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}