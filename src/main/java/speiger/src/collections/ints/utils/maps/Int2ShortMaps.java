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
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.ints.functions.function.Int2ShortFunction;
import speiger.src.collections.ints.functions.function.IntShortUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortCollections;
import speiger.src.collections.shorts.utils.ShortSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2ShortMap.Entry> fastIterator(Int2ShortMap map) {
		ObjectSet<Int2ShortMap.Entry> entries = map.int2ShortEntrySet();
		return entries instanceof Int2ShortMap.FastEntrySet ? ((Int2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2ShortMap.Entry> fastIterable(Int2ShortMap map) {
		ObjectSet<Int2ShortMap.Entry> entries = map.int2ShortEntrySet();
		return map instanceof Int2ShortMap.FastEntrySet ? new ObjectIterable<Int2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Int2ShortMap.Entry> iterator() { return ((Int2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2ShortMap.Entry> action) { ((Int2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2ShortMap map, Consumer<Int2ShortMap.Entry> action) {
		ObjectSet<Int2ShortMap.Entry> entries = map.int2ShortEntrySet();
		if(entries instanceof Int2ShortMap.FastEntrySet) ((Int2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortMap synchronize(Int2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortMap synchronize(Int2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortSortedMap synchronize(Int2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortSortedMap synchronize(Int2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortOrderedMap synchronize(Int2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortOrderedMap synchronize(Int2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortNavigableMap synchronize(Int2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ShortNavigableMap synchronize(Int2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2ShortMap unmodifiable(Int2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ShortOrderedMap unmodifiable(Int2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ShortSortedMap unmodifiable(Int2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ShortNavigableMap unmodifiable(Int2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2ShortMap.Entry unmodifiable(Int2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2ShortMap.Entry unmodifiable(Map.Entry<Integer, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2ShortMap singleton(int key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2ShortMap {
		final int key;
		final short value;
		IntSet keySet;
		ShortCollection values;
		ObjectSet<Int2ShortMap.Entry> entrySet;
		
		SingletonMap(int key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(int key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(int key, short defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2ShortMap {
		@Override
		public short put(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(int key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(int key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(int key, short defaultValue) { return (short)0; }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2ShortMap.Entry entry) {
			super(entry.getIntKey(), entry.getShortValue());
		}
		
		@Override
		public void set(int key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2ShortNavigableMap {
		Int2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Int2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2ShortNavigableMap descendingMap() { return Int2ShortMaps.synchronize(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2ShortMap.Entry firstEntry() { return Int2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2ShortMap.Entry lastEntry() { return Int2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2ShortNavigableMap headMap(int toKey, boolean inclusive) { return Int2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, int toKey) { return Int2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ShortNavigableMap headMap(int toKey) { return Int2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey) { return Int2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Int2ShortMap.Entry lowerEntry(int key) { return Int2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2ShortMap.Entry higherEntry(int key) { return Int2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2ShortMap.Entry floorEntry(int key) { return Int2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2ShortMap.Entry ceilingEntry(int key) { return Int2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2ShortOrderedMap {
		Int2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Int2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Int2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2ShortSortedMap {
		Int2ShortSortedMap map;
		
		UnmodifyableSortedMap(Int2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2ShortSortedMap subMap(int fromKey, int toKey) { return Int2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ShortSortedMap headMap(int toKey) { return Int2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ShortSortedMap tailMap(int fromKey) { return Int2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Int2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2ShortMap implements Int2ShortMap {
		Int2ShortMap map;
		ShortCollection values;
		IntSet keys;
		ObjectSet<Int2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Int2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(int key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(int key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(int key) { return map.get(key); }
		@Override
		public short getOrDefault(int key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Int2ShortMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2ShortMap.Entry>
	{
		ObjectSet<Int2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Int2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2ShortMap.Entry> iterator() {
			return new ObjectIterator<Int2ShortMap.Entry>() {
				ObjectIterator<Int2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2ShortMap.Entry next() { return Int2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2ShortNavigableMap {
		Int2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Int2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2ShortNavigableMap descendingMap() { synchronized(mutex) { return Int2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Int2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2ShortNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ShortNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2ShortMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2ShortMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2ShortMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2ShortMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Int2ShortNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ShortNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ShortNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ShortNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ShortNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ShortNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Int2ShortMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2ShortMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2ShortMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2ShortMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2ShortOrderedMap {
		Int2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Int2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(int key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(int key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Int2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2ShortSortedMap {
		Int2ShortSortedMap map;
		
		SynchronizedSortedMap(Int2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2ShortSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ShortSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ShortSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Int2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2ShortSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ShortSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ShortSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2ShortMap implements Int2ShortMap {
		Int2ShortMap map;
		ShortCollection values;
		IntSet keys;
		ObjectSet<Int2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(int key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(int key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(int key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(int key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(int key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(int key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Int2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(IntShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(int key, IntShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(int key, Int2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(int key, IntShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short mergeShort(int key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Int2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(int key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Int2ShortMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2ShortEntrySet(), mutex);
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
		public Short put(Integer key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Integer key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Integer key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Integer key, BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Integer key, Function<? super Integer, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Integer key, BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Integer key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}