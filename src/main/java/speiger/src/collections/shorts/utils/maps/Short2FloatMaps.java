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
import speiger.src.collections.shorts.functions.consumer.ShortFloatConsumer;
import speiger.src.collections.shorts.functions.function.Short2FloatFunction;
import speiger.src.collections.shorts.functions.function.ShortFloatUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.utils.FloatCollections;
import speiger.src.collections.floats.utils.FloatSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Short2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2FloatMap.Entry> fastIterator(Short2FloatMap map) {
		ObjectSet<Short2FloatMap.Entry> entries = map.short2FloatEntrySet();
		return entries instanceof Short2FloatMap.FastEntrySet ? ((Short2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2FloatMap.Entry> fastIterable(Short2FloatMap map) {
		ObjectSet<Short2FloatMap.Entry> entries = map.short2FloatEntrySet();
		return map instanceof Short2FloatMap.FastEntrySet ? new ObjectIterable<Short2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Short2FloatMap.Entry> iterator() { return ((Short2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2FloatMap.Entry> action) { ((Short2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2FloatMap map, Consumer<Short2FloatMap.Entry> action) {
		ObjectSet<Short2FloatMap.Entry> entries = map.short2FloatEntrySet();
		if(entries instanceof Short2FloatMap.FastEntrySet) ((Short2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatMap synchronize(Short2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatMap synchronize(Short2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatSortedMap synchronize(Short2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatSortedMap synchronize(Short2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatOrderedMap synchronize(Short2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatOrderedMap synchronize(Short2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatNavigableMap synchronize(Short2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2FloatNavigableMap synchronize(Short2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2FloatMap unmodifiable(Short2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2FloatOrderedMap unmodifiable(Short2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2FloatSortedMap unmodifiable(Short2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2FloatNavigableMap unmodifiable(Short2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2FloatMap.Entry unmodifiable(Short2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2FloatMap.Entry unmodifiable(Map.Entry<Short, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2FloatMap singleton(short key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2FloatMap {
		final short key;
		final float value;
		ShortSet keySet;
		FloatCollection values;
		ObjectSet<Short2FloatMap.Entry> entrySet;
		
		SingletonMap(short key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(short key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(short key, float defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2FloatMap.Entry> short2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2FloatMap {
		@Override
		public float put(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(short key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(short key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(short key, float defaultValue) { return 0F; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Short2FloatMap.Entry> short2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2FloatMap.Entry entry) {
			super(entry.getShortKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(short key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2FloatNavigableMap {
		Short2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Short2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2FloatNavigableMap descendingMap() { return Short2FloatMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2FloatMap.Entry firstEntry() { return Short2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2FloatMap.Entry lastEntry() { return Short2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2FloatNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2FloatNavigableMap headMap(short toKey, boolean inclusive) { return Short2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2FloatNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2FloatNavigableMap subMap(short fromKey, short toKey) { return Short2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2FloatNavigableMap headMap(short toKey) { return Short2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2FloatNavigableMap tailMap(short fromKey) { return Short2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2FloatMap.Entry lowerEntry(short key) { return Short2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2FloatMap.Entry higherEntry(short key) { return Short2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2FloatMap.Entry floorEntry(short key) { return Short2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2FloatMap.Entry ceilingEntry(short key) { return Short2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2FloatOrderedMap {
		Short2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Short2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Short2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2FloatSortedMap {
		Short2FloatSortedMap map;
		
		UnmodifyableSortedMap(Short2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2FloatSortedMap subMap(short fromKey, short toKey) { return Short2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2FloatSortedMap headMap(short toKey) { return Short2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2FloatSortedMap tailMap(short fromKey) { return Short2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Short2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2FloatMap implements Short2FloatMap {
		Short2FloatMap map;
		FloatCollection values;
		ShortSet keys;
		ObjectSet<Short2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Short2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(short key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(short key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(short key) { return map.get(key); }
		@Override
		public float getOrDefault(short key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2FloatMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2FloatMap.Entry> short2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2FloatMap.Entry>
	{
		ObjectSet<Short2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Short2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2FloatMap.Entry> iterator() {
			return new ObjectIterator<Short2FloatMap.Entry>() {
				ObjectIterator<Short2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2FloatMap.Entry next() { return Short2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2FloatNavigableMap {
		Short2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Short2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2FloatNavigableMap descendingMap() { synchronized(mutex) { return Short2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2FloatNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2FloatNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2FloatNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2FloatNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2FloatNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2FloatNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2FloatMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2FloatMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2FloatMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2FloatMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2FloatNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2FloatNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2FloatNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2FloatNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2FloatNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2FloatNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2FloatMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2FloatMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2FloatMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2FloatMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2FloatOrderedMap {
		Short2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Short2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(short key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(short key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Short2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2FloatSortedMap {
		Short2FloatSortedMap map;
		
		SynchronizedSortedMap(Short2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2FloatSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2FloatSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2FloatSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Short2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2FloatSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2FloatSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2FloatSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2FloatMap implements Short2FloatMap {
		Short2FloatMap map;
		FloatCollection values;
		ShortSet keys;
		ObjectSet<Short2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(short key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(short key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(short key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(short key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(short key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(short key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Short2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(ShortFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(short key, ShortFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(short key, Short2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(short key, ShortFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float mergeFloat(short key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Short2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(short key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2FloatMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2FloatMap.Entry> short2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2FloatEntrySet(), mutex);
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
		public Float put(Short key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Short key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Short key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Short key, BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Short key, Function<? super Short, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Short key, BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Short key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}