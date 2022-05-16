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
import speiger.src.collections.longs.functions.consumer.LongFloatConsumer;
import speiger.src.collections.longs.functions.function.Long2FloatFunction;
import speiger.src.collections.longs.functions.function.LongFloatUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.utils.FloatCollections;
import speiger.src.collections.floats.utils.FloatSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Long2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2FloatMap.Entry> fastIterator(Long2FloatMap map) {
		ObjectSet<Long2FloatMap.Entry> entries = map.long2FloatEntrySet();
		return entries instanceof Long2FloatMap.FastEntrySet ? ((Long2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2FloatMap.Entry> fastIterable(Long2FloatMap map) {
		ObjectSet<Long2FloatMap.Entry> entries = map.long2FloatEntrySet();
		return map instanceof Long2FloatMap.FastEntrySet ? new ObjectIterable<Long2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Long2FloatMap.Entry> iterator() { return ((Long2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2FloatMap.Entry> action) { ((Long2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2FloatMap map, Consumer<Long2FloatMap.Entry> action) {
		ObjectSet<Long2FloatMap.Entry> entries = map.long2FloatEntrySet();
		if(entries instanceof Long2FloatMap.FastEntrySet) ((Long2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatMap synchronize(Long2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatMap synchronize(Long2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatSortedMap synchronize(Long2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatSortedMap synchronize(Long2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatOrderedMap synchronize(Long2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatOrderedMap synchronize(Long2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatNavigableMap synchronize(Long2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2FloatNavigableMap synchronize(Long2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2FloatMap unmodifiable(Long2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2FloatOrderedMap unmodifiable(Long2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2FloatSortedMap unmodifiable(Long2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2FloatNavigableMap unmodifiable(Long2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2FloatMap.Entry unmodifiable(Long2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2FloatMap.Entry unmodifiable(Map.Entry<Long, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2FloatMap singleton(long key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2FloatMap {
		final long key;
		final float value;
		LongSet keySet;
		FloatCollection values;
		ObjectSet<Long2FloatMap.Entry> entrySet;
		
		SingletonMap(long key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(long key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(long key, float defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2FloatMap.Entry> long2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2FloatMap {
		@Override
		public float put(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(long key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(long key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(long key, float defaultValue) { return 0F; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Long2FloatMap.Entry> long2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2FloatMap.Entry entry) {
			super(entry.getLongKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(long key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2FloatNavigableMap {
		Long2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Long2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2FloatNavigableMap descendingMap() { return Long2FloatMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2FloatMap.Entry firstEntry() { return Long2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2FloatMap.Entry lastEntry() { return Long2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2FloatNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2FloatNavigableMap headMap(long toKey, boolean inclusive) { return Long2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2FloatNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2FloatNavigableMap subMap(long fromKey, long toKey) { return Long2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2FloatNavigableMap headMap(long toKey) { return Long2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2FloatNavigableMap tailMap(long fromKey) { return Long2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2FloatMap.Entry lowerEntry(long key) { return Long2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2FloatMap.Entry higherEntry(long key) { return Long2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2FloatMap.Entry floorEntry(long key) { return Long2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2FloatMap.Entry ceilingEntry(long key) { return Long2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2FloatOrderedMap {
		Long2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Long2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Long2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2FloatSortedMap {
		Long2FloatSortedMap map;
		
		UnmodifyableSortedMap(Long2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2FloatSortedMap subMap(long fromKey, long toKey) { return Long2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2FloatSortedMap headMap(long toKey) { return Long2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2FloatSortedMap tailMap(long fromKey) { return Long2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Long2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2FloatMap implements Long2FloatMap {
		Long2FloatMap map;
		FloatCollection values;
		LongSet keys;
		ObjectSet<Long2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Long2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(long key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(long key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(long key) { return map.get(key); }
		@Override
		public float getOrDefault(long key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2FloatMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2FloatMap.Entry> long2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2FloatMap.Entry>
	{
		ObjectSet<Long2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Long2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2FloatMap.Entry> iterator() {
			return new ObjectIterator<Long2FloatMap.Entry>() {
				ObjectIterator<Long2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2FloatMap.Entry next() { return Long2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2FloatNavigableMap {
		Long2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Long2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2FloatNavigableMap descendingMap() { synchronized(mutex) { return Long2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2FloatNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2FloatNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2FloatNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2FloatNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2FloatNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2FloatNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2FloatMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2FloatMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2FloatMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2FloatMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2FloatNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2FloatNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2FloatNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2FloatNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2FloatNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2FloatNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2FloatMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2FloatMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2FloatMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2FloatMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2FloatOrderedMap {
		Long2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Long2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(long key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(long key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Long2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2FloatSortedMap {
		Long2FloatSortedMap map;
		
		SynchronizedSortedMap(Long2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2FloatSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2FloatSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2FloatSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Long2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2FloatSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2FloatSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2FloatSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2FloatMap implements Long2FloatMap {
		Long2FloatMap map;
		FloatCollection values;
		LongSet keys;
		ObjectSet<Long2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(long key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(long key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(long key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(long key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(long key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(long key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Long2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(LongFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(long key, LongFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(long key, Long2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(long key, LongFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float mergeFloat(long key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Long2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(long key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2FloatMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2FloatMap.Entry> long2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2FloatEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Float get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Float getOrDefault(Object key, Float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Float put(Long key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Long key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Long key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Long key, BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Long key, Function<? super Long, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Long key, BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Long key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}