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
import speiger.src.collections.shorts.functions.consumer.ShortShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2ShortFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.utils.ShortCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Short2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2ShortMap.Entry> fastIterator(Short2ShortMap map) {
		ObjectSet<Short2ShortMap.Entry> entries = map.short2ShortEntrySet();
		return entries instanceof Short2ShortMap.FastEntrySet ? ((Short2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2ShortMap.Entry> fastIterable(Short2ShortMap map) {
		ObjectSet<Short2ShortMap.Entry> entries = map.short2ShortEntrySet();
		return map instanceof Short2ShortMap.FastEntrySet ? new ObjectIterable<Short2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Short2ShortMap.Entry> iterator() { return ((Short2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2ShortMap.Entry> action) { ((Short2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2ShortMap map, Consumer<Short2ShortMap.Entry> action) {
		ObjectSet<Short2ShortMap.Entry> entries = map.short2ShortEntrySet();
		if(entries instanceof Short2ShortMap.FastEntrySet) ((Short2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortMap synchronize(Short2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortMap synchronize(Short2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortSortedMap synchronize(Short2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortSortedMap synchronize(Short2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortOrderedMap synchronize(Short2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortOrderedMap synchronize(Short2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortNavigableMap synchronize(Short2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ShortNavigableMap synchronize(Short2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2ShortMap unmodifiable(Short2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ShortOrderedMap unmodifiable(Short2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ShortSortedMap unmodifiable(Short2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ShortNavigableMap unmodifiable(Short2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2ShortMap.Entry unmodifiable(Short2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2ShortMap.Entry unmodifiable(Map.Entry<Short, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2ShortMap singleton(short key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2ShortMap {
		final short key;
		final short value;
		ShortSet keySet;
		ShortCollection values;
		ObjectSet<Short2ShortMap.Entry> entrySet;
		
		SingletonMap(short key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(short key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(short key, short defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2ShortMap.Entry> short2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2ShortMap {
		@Override
		public short put(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(short key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(short key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(short key, short defaultValue) { return (short)0; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Short2ShortMap.Entry> short2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2ShortMap.Entry entry) {
			super(entry.getShortKey(), entry.getShortValue());
		}
		
		@Override
		public void set(short key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2ShortNavigableMap {
		Short2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Short2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2ShortNavigableMap descendingMap() { return Short2ShortMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2ShortMap.Entry firstEntry() { return Short2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2ShortMap.Entry lastEntry() { return Short2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ShortNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2ShortNavigableMap headMap(short toKey, boolean inclusive) { return Short2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2ShortNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2ShortNavigableMap subMap(short fromKey, short toKey) { return Short2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ShortNavigableMap headMap(short toKey) { return Short2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ShortNavigableMap tailMap(short fromKey) { return Short2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2ShortMap.Entry lowerEntry(short key) { return Short2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2ShortMap.Entry higherEntry(short key) { return Short2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2ShortMap.Entry floorEntry(short key) { return Short2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2ShortMap.Entry ceilingEntry(short key) { return Short2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2ShortOrderedMap {
		Short2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Short2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Short2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2ShortSortedMap {
		Short2ShortSortedMap map;
		
		UnmodifyableSortedMap(Short2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2ShortSortedMap subMap(short fromKey, short toKey) { return Short2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ShortSortedMap headMap(short toKey) { return Short2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ShortSortedMap tailMap(short fromKey) { return Short2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Short2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2ShortMap implements Short2ShortMap {
		Short2ShortMap map;
		ShortCollection values;
		ShortSet keys;
		ObjectSet<Short2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Short2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(short key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(short key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(short key) { return map.get(key); }
		@Override
		public short getOrDefault(short key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2ShortMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2ShortMap.Entry> short2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2ShortMap.Entry>
	{
		ObjectSet<Short2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Short2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2ShortMap.Entry> iterator() {
			return new ObjectIterator<Short2ShortMap.Entry>() {
				ObjectIterator<Short2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2ShortMap.Entry next() { return Short2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2ShortNavigableMap {
		Short2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Short2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2ShortNavigableMap descendingMap() { synchronized(mutex) { return Short2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2ShortNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2ShortNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2ShortNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2ShortNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ShortNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ShortNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2ShortMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2ShortMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2ShortMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2ShortMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2ShortNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ShortNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ShortNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ShortNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ShortNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ShortNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2ShortMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2ShortMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2ShortMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2ShortMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2ShortOrderedMap {
		Short2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Short2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(short key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(short key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Short2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2ShortSortedMap {
		Short2ShortSortedMap map;
		
		SynchronizedSortedMap(Short2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2ShortSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ShortSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ShortSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Short2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2ShortSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ShortSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ShortSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2ShortMap implements Short2ShortMap {
		Short2ShortMap map;
		ShortCollection values;
		ShortSet keys;
		ObjectSet<Short2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(short key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(short key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(short key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(short key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(short key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(short key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Short2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(short key, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(short key, Short2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(short key, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short mergeShort(short key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Short2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(short key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2ShortMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2ShortMap.Entry> short2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2ShortEntrySet(), mutex);
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
		public Short put(Short key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Short key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Short key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Short key, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Short key, Function<? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Short key, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Short key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}