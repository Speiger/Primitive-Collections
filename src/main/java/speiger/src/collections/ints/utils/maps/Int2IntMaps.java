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
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.function.Int2IntFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.utils.IntCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2IntMap.Entry> fastIterator(Int2IntMap map) {
		ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
		return entries instanceof Int2IntMap.FastEntrySet ? ((Int2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2IntMap.Entry> fastIterable(Int2IntMap map) {
		ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
		return map instanceof Int2IntMap.FastEntrySet ? new ObjectIterable<Int2IntMap.Entry>(){
			@Override
			public ObjectIterator<Int2IntMap.Entry> iterator() { return ((Int2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2IntMap.Entry> action) { ((Int2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2IntMap map, Consumer<Int2IntMap.Entry> action) {
		ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
		if(entries instanceof Int2IntMap.FastEntrySet) ((Int2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntMap synchronize(Int2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntMap synchronize(Int2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntSortedMap synchronize(Int2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntSortedMap synchronize(Int2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntOrderedMap synchronize(Int2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntOrderedMap synchronize(Int2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntNavigableMap synchronize(Int2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2IntNavigableMap synchronize(Int2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2IntMap unmodifiable(Int2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2IntOrderedMap unmodifiable(Int2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2IntSortedMap unmodifiable(Int2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2IntNavigableMap unmodifiable(Int2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2IntMap.Entry unmodifiable(Int2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2IntMap.Entry unmodifiable(Map.Entry<Integer, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2IntMap singleton(int key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2IntMap {
		final int key;
		final int value;
		IntSet keySet;
		IntCollection values;
		ObjectSet<Int2IntMap.Entry> entrySet;
		
		SingletonMap(int key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(int key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(int key, int defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2IntMap {
		@Override
		public int put(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(int key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(int key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(int key, int defaultValue) { return 0; }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2IntMap.Entry entry) {
			super(entry.getIntKey(), entry.getIntValue());
		}
		
		@Override
		public void set(int key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2IntNavigableMap {
		Int2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Int2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2IntNavigableMap descendingMap() { return Int2IntMaps.synchronize(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2IntMap.Entry firstEntry() { return Int2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2IntMap.Entry lastEntry() { return Int2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2IntNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2IntNavigableMap headMap(int toKey, boolean inclusive) { return Int2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2IntNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2IntNavigableMap subMap(int fromKey, int toKey) { return Int2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2IntNavigableMap headMap(int toKey) { return Int2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2IntNavigableMap tailMap(int fromKey) { return Int2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Int2IntMap.Entry lowerEntry(int key) { return Int2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2IntMap.Entry higherEntry(int key) { return Int2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2IntMap.Entry floorEntry(int key) { return Int2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2IntMap.Entry ceilingEntry(int key) { return Int2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2IntOrderedMap {
		Int2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Int2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Int2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2IntSortedMap {
		Int2IntSortedMap map;
		
		UnmodifyableSortedMap(Int2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2IntSortedMap subMap(int fromKey, int toKey) { return Int2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2IntSortedMap headMap(int toKey) { return Int2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2IntSortedMap tailMap(int fromKey) { return Int2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Int2IntSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2IntMap implements Int2IntMap {
		Int2IntMap map;
		IntCollection values;
		IntSet keys;
		ObjectSet<Int2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Int2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(int key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(int key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(int key) { return map.get(key); }
		@Override
		public int getOrDefault(int key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Int2IntMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2IntMap.Entry>
	{
		ObjectSet<Int2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Int2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2IntMap.Entry> iterator() {
			return new ObjectIterator<Int2IntMap.Entry>() {
				ObjectIterator<Int2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2IntMap.Entry next() { return Int2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2IntNavigableMap {
		Int2IntNavigableMap map;
		
		SynchronizedNavigableMap(Int2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2IntNavigableMap descendingMap() { synchronized(mutex) { return Int2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Int2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2IntMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2IntNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2IntNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2IntNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2IntNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2IntNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2IntNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2IntMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2IntMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2IntMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2IntMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Int2IntNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2IntNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2IntNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2IntNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2IntNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2IntNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Int2IntMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2IntMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2IntMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2IntMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2IntOrderedMap {
		Int2IntOrderedMap map;
		
		SynchronizedOrderedMap(Int2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(int key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(int key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Int2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2IntSortedMap {
		Int2IntSortedMap map;
		
		SynchronizedSortedMap(Int2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2IntSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2IntSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2IntSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Int2IntSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2IntSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2IntSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2IntSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2IntMap implements Int2IntMap {
		Int2IntMap map;
		IntCollection values;
		IntSet keys;
		ObjectSet<Int2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(int key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(int key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(int key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(int key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(int key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(int key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Int2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(int key, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(int key, Int2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(int key, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int mergeInt(int key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Int2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(int key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Int2IntMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2IntEntrySet(), mutex);
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
		public Integer put(Integer key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Integer key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Integer key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Integer key, Function<? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Integer key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}