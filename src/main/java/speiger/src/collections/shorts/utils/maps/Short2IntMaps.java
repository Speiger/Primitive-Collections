package speiger.src.collections.shorts.utils.maps;

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
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.consumer.ShortIntConsumer;
import speiger.src.collections.shorts.functions.function.Short2IntFunction;
import speiger.src.collections.shorts.functions.function.ShortIntUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.ints.utils.IntSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Short2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2IntMap.Entry> fastIterator(Short2IntMap map) {
		ObjectSet<Short2IntMap.Entry> entries = map.short2IntEntrySet();
		return entries instanceof Short2IntMap.FastEntrySet ? ((Short2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2IntMap.Entry> fastIterable(Short2IntMap map) {
		ObjectSet<Short2IntMap.Entry> entries = map.short2IntEntrySet();
		return map instanceof Short2IntMap.FastEntrySet ? new ObjectIterable<Short2IntMap.Entry>(){
			@Override
			public ObjectIterator<Short2IntMap.Entry> iterator() { return ((Short2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2IntMap.Entry> action) { ((Short2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2IntMap map, Consumer<Short2IntMap.Entry> action) {
		ObjectSet<Short2IntMap.Entry> entries = map.short2IntEntrySet();
		if(entries instanceof Short2IntMap.FastEntrySet) ((Short2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntMap synchronize(Short2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntMap synchronize(Short2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntSortedMap synchronize(Short2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntSortedMap synchronize(Short2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntOrderedMap synchronize(Short2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntOrderedMap synchronize(Short2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntNavigableMap synchronize(Short2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2IntNavigableMap synchronize(Short2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2IntMap unmodifiable(Short2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2IntOrderedMap unmodifiable(Short2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2IntSortedMap unmodifiable(Short2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2IntNavigableMap unmodifiable(Short2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2IntMap.Entry unmodifiable(Short2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2IntMap.Entry unmodifiable(Map.Entry<Short, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2IntMap singleton(short key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2IntMap {
		final short key;
		final int value;
		ShortSet keySet;
		IntCollection values;
		ObjectSet<Short2IntMap.Entry> entrySet;
		
		SingletonMap(short key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(short key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(short key, int defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2IntMap {
		@Override
		public int put(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(short key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(short key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(short key, int defaultValue) { return 0; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2IntMap.Entry entry) {
			super(entry.getShortKey(), entry.getIntValue());
		}
		
		@Override
		public void set(short key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2IntNavigableMap {
		Short2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Short2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2IntNavigableMap descendingMap() { return Short2IntMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2IntMap.Entry firstEntry() { return Short2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2IntMap.Entry lastEntry() { return Short2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2IntNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2IntNavigableMap headMap(short toKey, boolean inclusive) { return Short2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2IntNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2IntNavigableMap subMap(short fromKey, short toKey) { return Short2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2IntNavigableMap headMap(short toKey) { return Short2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2IntNavigableMap tailMap(short fromKey) { return Short2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(short e) { throw new UnsupportedOperationException(); }
		@Override
		public short getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public short lowerKey(short key) { return map.lowerKey(key); }
		@Override
		public short higherKey(short key) { return map.higherKey(key); }
		@Override
		public short floorKey(short key) { return map.floorKey(key); }
		@Override
		public short ceilingKey(short key) { return map.ceilingKey(key); }
		@Override
		public Short2IntMap.Entry lowerEntry(short key) { return Short2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2IntMap.Entry higherEntry(short key) { return Short2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2IntMap.Entry floorEntry(short key) { return Short2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2IntMap.Entry ceilingEntry(short key) { return Short2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2IntOrderedMap {
		Short2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Short2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Short2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2IntSortedMap {
		Short2IntSortedMap map;
		
		UnmodifyableSortedMap(Short2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2IntSortedMap subMap(short fromKey, short toKey) { return Short2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2IntSortedMap headMap(short toKey) { return Short2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2IntSortedMap tailMap(short fromKey) { return Short2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Short2IntSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2IntMap implements Short2IntMap {
		Short2IntMap map;
		IntCollection values;
		ShortSet keys;
		ObjectSet<Short2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Short2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(short key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(short key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(short key) { return map.get(key); }
		@Override
		public int getOrDefault(short key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2IntMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2IntMap.Entry>
	{
		ObjectSet<Short2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Short2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2IntMap.Entry> iterator() {
			return new ObjectIterator<Short2IntMap.Entry>() {
				ObjectIterator<Short2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2IntMap.Entry next() { return Short2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2IntNavigableMap {
		Short2IntNavigableMap map;
		
		SynchronizedNavigableMap(Short2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2IntNavigableMap descendingMap() { synchronized(mutex) { return Short2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2IntMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2IntNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2IntNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2IntNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2IntNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2IntNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2IntNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2IntMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2IntMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2IntMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2IntMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2IntNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2IntNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2IntNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2IntNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2IntNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2IntNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(short e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public short getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(short e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public short getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Short lowerKey(Short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Short floorKey(Short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Short ceilingKey(Short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Short higherKey(Short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Short2IntMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2IntMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2IntMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2IntMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2IntOrderedMap {
		Short2IntOrderedMap map;
		
		SynchronizedOrderedMap(Short2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(short key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(short key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Short2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2IntSortedMap {
		Short2IntSortedMap map;
		
		SynchronizedSortedMap(Short2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2IntSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2IntSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2IntSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Short2IntSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2IntSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2IntSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2IntSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2IntMap implements Short2IntMap {
		Short2IntMap map;
		IntCollection values;
		ShortSet keys;
		ObjectSet<Short2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(short key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(short key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(short key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(short key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(short key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(short key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Short2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(ShortIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(short key, ShortIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(short key, Short2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int mergeInt(short key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Short2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(short key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2IntMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2IntEntrySet(), mutex);
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
		public Integer put(Short key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Short key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Short key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Short key, BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Short key, Function<? super Short, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Short key, BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Short key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}