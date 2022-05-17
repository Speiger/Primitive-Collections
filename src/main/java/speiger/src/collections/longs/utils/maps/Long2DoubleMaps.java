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
import speiger.src.collections.longs.functions.consumer.LongDoubleConsumer;
import speiger.src.collections.longs.functions.function.Long2DoubleFunction;
import speiger.src.collections.longs.functions.function.LongDoubleUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleCollections;
import speiger.src.collections.doubles.utils.DoubleSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Long2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Long2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Long2DoubleMap.Entry> fastIterator(Long2DoubleMap map) {
		ObjectSet<Long2DoubleMap.Entry> entries = map.long2DoubleEntrySet();
		return entries instanceof Long2DoubleMap.FastEntrySet ? ((Long2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Long2DoubleMap.Entry> fastIterable(Long2DoubleMap map) {
		ObjectSet<Long2DoubleMap.Entry> entries = map.long2DoubleEntrySet();
		return map instanceof Long2DoubleMap.FastEntrySet ? new ObjectIterable<Long2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Long2DoubleMap.Entry> iterator() { return ((Long2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2DoubleMap.Entry> action) { ((Long2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Long2DoubleMap map, Consumer<Long2DoubleMap.Entry> action) {
		ObjectSet<Long2DoubleMap.Entry> entries = map.long2DoubleEntrySet();
		if(entries instanceof Long2DoubleMap.FastEntrySet) ((Long2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleMap synchronize(Long2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleMap synchronize(Long2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleSortedMap synchronize(Long2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleSortedMap synchronize(Long2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleOrderedMap synchronize(Long2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleOrderedMap synchronize(Long2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleNavigableMap synchronize(Long2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Long2DoubleNavigableMap synchronize(Long2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Long2DoubleMap unmodifiable(Long2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2DoubleOrderedMap unmodifiable(Long2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Long2DoubleSortedMap unmodifiable(Long2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Long2DoubleNavigableMap unmodifiable(Long2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2DoubleMap.Entry unmodifiable(Long2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Long2DoubleMap.Entry unmodifiable(Map.Entry<Long, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Long2DoubleMap singleton(long key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractLong2DoubleMap {
		final long key;
		final double value;
		LongSet keySet;
		DoubleCollection values;
		ObjectSet<Long2DoubleMap.Entry> entrySet;
		
		SingletonMap(long key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(long key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(long key, double defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2DoubleMap.Entry> long2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractLong2DoubleMap {
		@Override
		public double put(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(long key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(long key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(long key, double defaultValue) { return 0D; }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Long2DoubleMap.Entry> long2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractLong2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Long, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2DoubleMap.Entry entry) {
			super(entry.getLongKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(long key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Long2DoubleNavigableMap {
		Long2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Long2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2DoubleNavigableMap descendingMap() { return Long2DoubleMaps.synchronize(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2DoubleMap.Entry firstEntry() { return Long2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2DoubleMap.Entry lastEntry() { return Long2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2DoubleNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2DoubleNavigableMap headMap(long toKey, boolean inclusive) { return Long2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2DoubleNavigableMap tailMap(long fromKey, boolean inclusive) { return Long2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2DoubleNavigableMap subMap(long fromKey, long toKey) { return Long2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2DoubleNavigableMap headMap(long toKey) { return Long2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2DoubleNavigableMap tailMap(long fromKey) { return Long2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Long2DoubleMap.Entry lowerEntry(long key) { return Long2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2DoubleMap.Entry higherEntry(long key) { return Long2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2DoubleMap.Entry floorEntry(long key) { return Long2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2DoubleMap.Entry ceilingEntry(long key) { return Long2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Long2DoubleOrderedMap {
		Long2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Long2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Long2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Long2DoubleSortedMap {
		Long2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Long2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2DoubleSortedMap subMap(long fromKey, long toKey) { return Long2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2DoubleSortedMap headMap(long toKey) { return Long2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2DoubleSortedMap tailMap(long fromKey) { return Long2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollLastLongKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Long2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractLong2DoubleMap implements Long2DoubleMap {
		Long2DoubleMap map;
		DoubleCollection values;
		LongSet keys;
		ObjectSet<Long2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Long2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(long key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(long key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(long key) { return map.get(key); }
		@Override
		public double getOrDefault(long key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Long2DoubleMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2DoubleMap.Entry> long2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.long2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Long2DoubleMap.Entry>
	{
		ObjectSet<Long2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Long2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Long2DoubleMap.Entry>() {
				ObjectIterator<Long2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2DoubleMap.Entry next() { return Long2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Long2DoubleNavigableMap {
		Long2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Long2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Long2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2DoubleNavigableMap subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2DoubleNavigableMap headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2DoubleNavigableMap tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2DoubleNavigableMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2DoubleNavigableMap headMap(long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2DoubleNavigableMap tailMap(long fromKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2DoubleMap.Entry lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2DoubleMap.Entry higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2DoubleMap.Entry floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2DoubleMap.Entry ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap headMap(Long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleNavigableMap tailMap(Long fromKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Long2DoubleMap.Entry lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2DoubleMap.Entry floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2DoubleMap.Entry ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2DoubleMap.Entry higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Long2DoubleOrderedMap {
		Long2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Long2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(long key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(long key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Long2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Long2DoubleSortedMap {
		Long2DoubleSortedMap map;
		
		SynchronizedSortedMap(Long2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2DoubleSortedMap subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2DoubleSortedMap headMap(long toKey)  { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2DoubleSortedMap tailMap(long fromKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Long2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2DoubleSortedMap subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleSortedMap headMap(Long toKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2DoubleSortedMap tailMap(Long fromKey) { synchronized(mutex) { return Long2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractLong2DoubleMap implements Long2DoubleMap {
		Long2DoubleMap map;
		DoubleCollection values;
		LongSet keys;
		ObjectSet<Long2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(long key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(long key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(long key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(long key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Long2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Long2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(long key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(long key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Long2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(LongDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(long key, LongDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(long key, Long2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(long key, LongDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double mergeDouble(long key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Long2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(long key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Long2DoubleMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2DoubleMap.Entry> long2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2DoubleEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Double get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Double getOrDefault(Object key, Double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Double put(Long key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Long key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Long key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Long key, BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Long key, Function<? super Long, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Long key, BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Long key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}