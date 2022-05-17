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
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntBooleanUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.utils.BooleanCollections;
import speiger.src.collections.booleans.utils.BooleanSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2BooleanMap.Entry> fastIterator(Int2BooleanMap map) {
		ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
		return entries instanceof Int2BooleanMap.FastEntrySet ? ((Int2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2BooleanMap.Entry> fastIterable(Int2BooleanMap map) {
		ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
		return map instanceof Int2BooleanMap.FastEntrySet ? new ObjectIterable<Int2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Int2BooleanMap.Entry> iterator() { return ((Int2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2BooleanMap.Entry> action) { ((Int2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2BooleanMap map, Consumer<Int2BooleanMap.Entry> action) {
		ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
		if(entries instanceof Int2BooleanMap.FastEntrySet) ((Int2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanMap synchronize(Int2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanMap synchronize(Int2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanSortedMap synchronize(Int2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanSortedMap synchronize(Int2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanOrderedMap synchronize(Int2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanOrderedMap synchronize(Int2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanNavigableMap synchronize(Int2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2BooleanNavigableMap synchronize(Int2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2BooleanMap unmodifiable(Int2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2BooleanOrderedMap unmodifiable(Int2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2BooleanSortedMap unmodifiable(Int2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2BooleanNavigableMap unmodifiable(Int2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2BooleanMap.Entry unmodifiable(Int2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2BooleanMap.Entry unmodifiable(Map.Entry<Integer, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2BooleanMap singleton(int key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2BooleanMap {
		final int key;
		final boolean value;
		IntSet keySet;
		BooleanCollection values;
		ObjectSet<Int2BooleanMap.Entry> entrySet;
		
		SingletonMap(int key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(int key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(int key, boolean defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2BooleanMap.Entry> int2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2BooleanMap {
		@Override
		public boolean put(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(int key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(int key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(int key, boolean defaultValue) { return false; }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Int2BooleanMap.Entry> int2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2BooleanMap.Entry entry) {
			super(entry.getIntKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(int key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2BooleanNavigableMap {
		Int2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Int2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2BooleanNavigableMap descendingMap() { return Int2BooleanMaps.synchronize(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2BooleanMap.Entry firstEntry() { return Int2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2BooleanMap.Entry lastEntry() { return Int2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2BooleanNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2BooleanNavigableMap headMap(int toKey, boolean inclusive) { return Int2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2BooleanNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2BooleanNavigableMap subMap(int fromKey, int toKey) { return Int2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2BooleanNavigableMap headMap(int toKey) { return Int2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2BooleanNavigableMap tailMap(int fromKey) { return Int2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Int2BooleanMap.Entry lowerEntry(int key) { return Int2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2BooleanMap.Entry higherEntry(int key) { return Int2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2BooleanMap.Entry floorEntry(int key) { return Int2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2BooleanMap.Entry ceilingEntry(int key) { return Int2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2BooleanOrderedMap {
		Int2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Int2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Int2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2BooleanSortedMap {
		Int2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Int2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2BooleanSortedMap subMap(int fromKey, int toKey) { return Int2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2BooleanSortedMap headMap(int toKey) { return Int2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2BooleanSortedMap tailMap(int fromKey) { return Int2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollFirstIntKey(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollLastIntKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Int2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2BooleanMap implements Int2BooleanMap {
		Int2BooleanMap map;
		BooleanCollection values;
		IntSet keys;
		ObjectSet<Int2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Int2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(int key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(int key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(int key) { return map.get(key); }
		@Override
		public boolean getOrDefault(int key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Int2BooleanMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2BooleanMap.Entry>
	{
		ObjectSet<Int2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Int2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Int2BooleanMap.Entry>() {
				ObjectIterator<Int2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2BooleanMap.Entry next() { return Int2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2BooleanNavigableMap {
		Int2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Int2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Int2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2BooleanNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2BooleanNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2BooleanNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2BooleanNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2BooleanNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2BooleanNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2BooleanMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2BooleanMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2BooleanMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2BooleanMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Int2BooleanMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2BooleanMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2BooleanMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2BooleanMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2BooleanOrderedMap {
		Int2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Int2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(int key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(int key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Int2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2BooleanSortedMap {
		Int2BooleanSortedMap map;
		
		SynchronizedSortedMap(Int2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2BooleanSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2BooleanSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2BooleanSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Int2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2BooleanSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2BooleanSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2BooleanMap implements Int2BooleanMap {
		Int2BooleanMap map;
		BooleanCollection values;
		IntSet keys;
		ObjectSet<Int2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(int key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(int key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Int2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(int key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(int key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Int2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(IntBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(int key, IntBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(int key, Int2BooleanFunction mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(int key, IntBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean mergeBoolean(int key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Int2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(int key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Int2BooleanMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2BooleanEntrySet(), mutex);
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
		public Boolean put(Integer key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Integer key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Integer key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Integer key, Function<? super Integer, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Integer key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}