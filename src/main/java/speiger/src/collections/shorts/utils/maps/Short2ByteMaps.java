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
import speiger.src.collections.shorts.functions.consumer.ShortByteConsumer;
import speiger.src.collections.shorts.functions.function.Short2ByteFunction;
import speiger.src.collections.shorts.functions.function.ShortByteUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.bytes.utils.ByteSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Short2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2ByteMap.Entry> fastIterator(Short2ByteMap map) {
		ObjectSet<Short2ByteMap.Entry> entries = map.short2ByteEntrySet();
		return entries instanceof Short2ByteMap.FastEntrySet ? ((Short2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2ByteMap.Entry> fastIterable(Short2ByteMap map) {
		ObjectSet<Short2ByteMap.Entry> entries = map.short2ByteEntrySet();
		return map instanceof Short2ByteMap.FastEntrySet ? new ObjectIterable<Short2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Short2ByteMap.Entry> iterator() { return ((Short2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2ByteMap.Entry> action) { ((Short2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2ByteMap map, Consumer<Short2ByteMap.Entry> action) {
		ObjectSet<Short2ByteMap.Entry> entries = map.short2ByteEntrySet();
		if(entries instanceof Short2ByteMap.FastEntrySet) ((Short2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteMap synchronize(Short2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteMap synchronize(Short2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteSortedMap synchronize(Short2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteSortedMap synchronize(Short2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteOrderedMap synchronize(Short2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteOrderedMap synchronize(Short2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteNavigableMap synchronize(Short2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2ByteNavigableMap synchronize(Short2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2ByteMap unmodifiable(Short2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ByteOrderedMap unmodifiable(Short2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ByteSortedMap unmodifiable(Short2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2ByteNavigableMap unmodifiable(Short2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2ByteMap.Entry unmodifiable(Short2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2ByteMap.Entry unmodifiable(Map.Entry<Short, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2ByteMap singleton(short key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2ByteMap {
		final short key;
		final byte value;
		ShortSet keySet;
		ByteCollection values;
		ObjectSet<Short2ByteMap.Entry> entrySet;
		
		SingletonMap(short key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(short key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(short key, byte defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2ByteMap.Entry> short2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2ByteMap {
		@Override
		public byte put(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(short key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(short key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(short key, byte defaultValue) { return (byte)0; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Short2ByteMap.Entry> short2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2ByteMap.Entry entry) {
			super(entry.getShortKey(), entry.getByteValue());
		}
		
		@Override
		public void set(short key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2ByteNavigableMap {
		Short2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Short2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2ByteNavigableMap descendingMap() { return Short2ByteMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2ByteMap.Entry firstEntry() { return Short2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2ByteMap.Entry lastEntry() { return Short2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2ByteNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2ByteNavigableMap headMap(short toKey, boolean inclusive) { return Short2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2ByteNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2ByteNavigableMap subMap(short fromKey, short toKey) { return Short2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ByteNavigableMap headMap(short toKey) { return Short2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ByteNavigableMap tailMap(short fromKey) { return Short2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2ByteMap.Entry lowerEntry(short key) { return Short2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2ByteMap.Entry higherEntry(short key) { return Short2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2ByteMap.Entry floorEntry(short key) { return Short2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2ByteMap.Entry ceilingEntry(short key) { return Short2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2ByteOrderedMap {
		Short2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Short2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Short2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2ByteSortedMap {
		Short2ByteSortedMap map;
		
		UnmodifyableSortedMap(Short2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2ByteSortedMap subMap(short fromKey, short toKey) { return Short2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2ByteSortedMap headMap(short toKey) { return Short2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2ByteSortedMap tailMap(short fromKey) { return Short2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Short2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2ByteMap implements Short2ByteMap {
		Short2ByteMap map;
		ByteCollection values;
		ShortSet keys;
		ObjectSet<Short2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Short2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(short key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(short key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(short key) { return map.get(key); }
		@Override
		public byte getOrDefault(short key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2ByteMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2ByteMap.Entry> short2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2ByteMap.Entry>
	{
		ObjectSet<Short2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Short2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2ByteMap.Entry> iterator() {
			return new ObjectIterator<Short2ByteMap.Entry>() {
				ObjectIterator<Short2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2ByteMap.Entry next() { return Short2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2ByteNavigableMap {
		Short2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Short2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2ByteNavigableMap descendingMap() { synchronized(mutex) { return Short2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2ByteNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2ByteNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2ByteNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2ByteNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ByteNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ByteNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2ByteMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2ByteMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2ByteMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2ByteMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2ByteNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ByteNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ByteNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2ByteNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ByteNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ByteNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2ByteMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2ByteMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2ByteMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2ByteMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2ByteOrderedMap {
		Short2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Short2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(short key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(short key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Short2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2ByteSortedMap {
		Short2ByteSortedMap map;
		
		SynchronizedSortedMap(Short2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2ByteSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2ByteSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2ByteSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Short2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2ByteSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ByteSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2ByteSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2ByteMap implements Short2ByteMap {
		Short2ByteMap map;
		ByteCollection values;
		ShortSet keys;
		ObjectSet<Short2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(short key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(short key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(short key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(short key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(short key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(short key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Short2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(ShortByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(short key, ShortByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(short key, Short2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(short key, ShortByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte mergeByte(short key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Short2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(short key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2ByteMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2ByteMap.Entry> short2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2ByteEntrySet(), mutex);
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
		public Byte put(Short key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Short key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Short key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Short key, Function<? super Short, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Short key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}