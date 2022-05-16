package speiger.src.collections.ints.utils.maps;

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
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.consumer.IntLongConsumer;
import speiger.src.collections.ints.functions.function.Int2LongFunction;
import speiger.src.collections.ints.functions.function.IntLongUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.utils.LongCollections;
import speiger.src.collections.longs.utils.LongSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Int2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2LongMap.Entry> fastIterator(Int2LongMap map) {
		ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
		return entries instanceof Int2LongMap.FastEntrySet ? ((Int2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2LongMap.Entry> fastIterable(Int2LongMap map) {
		ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
		return map instanceof Int2LongMap.FastEntrySet ? new ObjectIterable<Int2LongMap.Entry>(){
			@Override
			public ObjectIterator<Int2LongMap.Entry> iterator() { return ((Int2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2LongMap.Entry> action) { ((Int2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2LongMap map, Consumer<Int2LongMap.Entry> action) {
		ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
		if(entries instanceof Int2LongMap.FastEntrySet) ((Int2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongMap synchronize(Int2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongMap synchronize(Int2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongSortedMap synchronize(Int2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongSortedMap synchronize(Int2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongOrderedMap synchronize(Int2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongOrderedMap synchronize(Int2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongNavigableMap synchronize(Int2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2LongNavigableMap synchronize(Int2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2LongMap unmodifiable(Int2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2LongOrderedMap unmodifiable(Int2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2LongSortedMap unmodifiable(Int2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2LongNavigableMap unmodifiable(Int2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2LongMap.Entry unmodifiable(Int2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2LongMap.Entry unmodifiable(Map.Entry<Integer, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2LongMap singleton(int key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2LongMap {
		final int key;
		final long value;
		IntSet keySet;
		LongCollection values;
		ObjectSet<Int2LongMap.Entry> entrySet;
		
		SingletonMap(int key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(int key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(int key, long defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2LongMap.Entry> int2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2LongMap {
		@Override
		public long put(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(int key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(int key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(int key, long defaultValue) { return 0L; }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Int2LongMap.Entry> int2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2LongMap.Entry entry) {
			super(entry.getIntKey(), entry.getLongValue());
		}
		
		@Override
		public void set(int key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2LongNavigableMap {
		Int2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Int2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2LongNavigableMap descendingMap() { return Int2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2LongMap.Entry firstEntry() { return Int2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2LongMap.Entry lastEntry() { return Int2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2LongNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2LongNavigableMap headMap(int toKey, boolean inclusive) { return Int2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2LongNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2LongNavigableMap subMap(int fromKey, int toKey) { return Int2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2LongNavigableMap headMap(int toKey) { return Int2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2LongNavigableMap tailMap(int fromKey) { return Int2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lowerKey(int key) { return map.lowerKey(key); }
		@Override
		public int higherKey(int key) { return map.higherKey(key); }
		@Override
		public int floorKey(int key) { return map.floorKey(key); }
		@Override
		public int ceilingKey(int key) { return map.ceilingKey(key); }
		@Override
		public Int2LongMap.Entry lowerEntry(int key) { return Int2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2LongMap.Entry higherEntry(int key) { return Int2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2LongMap.Entry floorEntry(int key) { return Int2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2LongMap.Entry ceilingEntry(int key) { return Int2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2LongOrderedMap {
		Int2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Int2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Int2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2LongSortedMap {
		Int2LongSortedMap map;
		
		UnmodifyableSortedMap(Int2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2LongSortedMap subMap(int fromKey, int toKey) { return Int2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2LongSortedMap headMap(int toKey) { return Int2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2LongSortedMap tailMap(int fromKey) { return Int2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Int2LongSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2LongMap implements Int2LongMap {
		Int2LongMap map;
		LongCollection values;
		IntSet keys;
		ObjectSet<Int2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Int2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(int key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(int key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(int key) { return map.get(key); }
		@Override
		public long getOrDefault(int key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Int2LongMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2LongMap.Entry> int2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2LongMap.Entry>
	{
		ObjectSet<Int2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Int2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2LongMap.Entry> iterator() {
			return new ObjectIterator<Int2LongMap.Entry>() {
				ObjectIterator<Int2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2LongMap.Entry next() { return Int2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2LongNavigableMap {
		Int2LongNavigableMap map;
		
		SynchronizedNavigableMap(Int2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2LongNavigableMap descendingMap() { synchronized(mutex) { return Int2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Int2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2LongMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2LongNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2LongNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2LongNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2LongNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2LongNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2LongNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2LongMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2LongMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2LongMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2LongMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Int2LongNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2LongNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2LongNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2LongNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2LongNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2LongNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Integer lowerKey(Integer key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Integer floorKey(Integer key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Integer ceilingKey(Integer key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Integer higherKey(Integer key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Int2LongMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2LongMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2LongMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2LongMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2LongOrderedMap {
		Int2LongOrderedMap map;
		
		SynchronizedOrderedMap(Int2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(int key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(int key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Int2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2LongSortedMap {
		Int2LongSortedMap map;
		
		SynchronizedSortedMap(Int2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2LongSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2LongSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2LongSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Int2LongSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2LongSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2LongSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2LongSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2LongMap implements Int2LongMap {
		Int2LongMap map;
		LongCollection values;
		IntSet keys;
		ObjectSet<Int2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(int key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(int key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(int key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(int key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(int key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(int key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Int2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(IntLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(int key, IntLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(int key, Int2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(int key, IntLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(int key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Int2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(int key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Int2LongMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2LongMap.Entry> int2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2LongEntrySet(), mutex);
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
		public Long put(Integer key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Integer key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Integer key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Integer key, BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Integer key, Function<? super Integer, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Integer key, BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Integer key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}