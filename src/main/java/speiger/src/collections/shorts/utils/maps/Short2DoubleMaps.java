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
import speiger.src.collections.shorts.functions.consumer.ShortDoubleConsumer;
import speiger.src.collections.shorts.functions.function.Short2DoubleFunction;
import speiger.src.collections.shorts.functions.function.ShortDoubleUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleSortedMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleOrderedMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleCollections;
import speiger.src.collections.doubles.utils.DoubleSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Short2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Short2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Short2DoubleMap.Entry> fastIterator(Short2DoubleMap map) {
		ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
		return entries instanceof Short2DoubleMap.FastEntrySet ? ((Short2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Short2DoubleMap.Entry> fastIterable(Short2DoubleMap map) {
		ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
		return map instanceof Short2DoubleMap.FastEntrySet ? new ObjectIterable<Short2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Short2DoubleMap.Entry> iterator() { return ((Short2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Short2DoubleMap.Entry> action) { ((Short2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Short2DoubleMap map, Consumer<Short2DoubleMap.Entry> action) {
		ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
		if(entries instanceof Short2DoubleMap.FastEntrySet) ((Short2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Short2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleMap synchronize(Short2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleMap synchronize(Short2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleSortedMap synchronize(Short2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleSortedMap synchronize(Short2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleOrderedMap synchronize(Short2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleOrderedMap synchronize(Short2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleNavigableMap synchronize(Short2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Short2DoubleNavigableMap synchronize(Short2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Short2DoubleMap unmodifiable(Short2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2DoubleOrderedMap unmodifiable(Short2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Short2DoubleSortedMap unmodifiable(Short2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Short2DoubleNavigableMap unmodifiable(Short2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2DoubleMap.Entry unmodifiable(Short2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Short2DoubleMap.Entry unmodifiable(Map.Entry<Short, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Short2DoubleMap singleton(short key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractShort2DoubleMap {
		final short key;
		final double value;
		ShortSet keySet;
		DoubleCollection values;
		ObjectSet<Short2DoubleMap.Entry> entrySet;
		
		SingletonMap(short key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(short key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(short key) { return Objects.equals(this.key, Short.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(short key, double defaultValue) { return Objects.equals(this.key, Short.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ShortSet keySet() { 
			if(keySet == null) keySet = ShortSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Short2DoubleMap.Entry> short2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractShort2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractShort2DoubleMap {
		@Override
		public double put(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(short key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(short key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(short key, double defaultValue) { return 0D; }
		@Override
		public ShortSet keySet() { return ShortSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Short2DoubleMap.Entry> short2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractShort2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Short, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Short2DoubleMap.Entry entry) {
			super(entry.getShortKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(short key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Short2DoubleNavigableMap {
		Short2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Short2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Short2DoubleNavigableMap descendingMap() { return Short2DoubleMaps.synchronize(map.descendingMap()); }
		@Override
		public ShortNavigableSet navigableKeySet() { return ShortSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ShortNavigableSet descendingKeySet() { return ShortSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Short2DoubleMap.Entry firstEntry() { return Short2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Short2DoubleMap.Entry lastEntry() { return Short2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Short2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Short2DoubleNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { return Short2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Short2DoubleNavigableMap headMap(short toKey, boolean inclusive) { return Short2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Short2DoubleNavigableMap tailMap(short fromKey, boolean inclusive) { return Short2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Short2DoubleNavigableMap subMap(short fromKey, short toKey) { return Short2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2DoubleNavigableMap headMap(short toKey) { return Short2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2DoubleNavigableMap tailMap(short fromKey) { return Short2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Short2DoubleMap.Entry lowerEntry(short key) { return Short2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Short2DoubleMap.Entry higherEntry(short key) { return Short2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Short2DoubleMap.Entry floorEntry(short key) { return Short2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Short2DoubleMap.Entry ceilingEntry(short key) { return Short2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Short2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Short2DoubleOrderedMap {
		Short2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Short2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Short2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Short2DoubleSortedMap {
		Short2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Short2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public Short2DoubleSortedMap subMap(short fromKey, short toKey) { return Short2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Short2DoubleSortedMap headMap(short toKey) { return Short2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Short2DoubleSortedMap tailMap(short fromKey) { return Short2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public short firstShortKey() { return map.firstShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short lastShortKey() { return map.lastShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollLastShortKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Short2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractShort2DoubleMap implements Short2DoubleMap {
		Short2DoubleMap map;
		DoubleCollection values;
		ShortSet keys;
		ObjectSet<Short2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Short2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(short key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(short key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(short key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(short key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(short key) { return map.get(key); }
		@Override
		public double getOrDefault(short key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Short2DoubleMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortSet keySet() { 
			if(keys == null) keys = ShortSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Short2DoubleMap.Entry> short2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.short2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Short2DoubleMap.Entry>
	{
		ObjectSet<Short2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Short2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Short2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Short2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Short2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Short2DoubleMap.Entry>() {
				ObjectIterator<Short2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Short2DoubleMap.Entry next() { return Short2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Short2DoubleNavigableMap {
		Short2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Short2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Short2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Short2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ShortNavigableSet navigableKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ShortNavigableSet descendingKeySet() { synchronized(mutex) { return ShortSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Short2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Short2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Short2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Short2DoubleNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Short2DoubleNavigableMap headMap(short toKey, boolean inclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Short2DoubleNavigableMap tailMap(short fromKey, boolean inclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Short2DoubleNavigableMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2DoubleNavigableMap headMap(short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2DoubleNavigableMap tailMap(short fromKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short lowerKey(short key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public short higherKey(short key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public short floorKey(short key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public short ceilingKey(short key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Short2DoubleMap.Entry lowerEntry(short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Short2DoubleMap.Entry higherEntry(short key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Short2DoubleMap.Entry floorEntry(short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Short2DoubleMap.Entry ceilingEntry(short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Short2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap headMap(Short toKey, boolean inclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap tailMap(Short fromKey, boolean inclusive) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap headMap(Short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleNavigableMap tailMap(Short fromKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Short2DoubleMap.Entry lowerEntry(Short key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Short2DoubleMap.Entry floorEntry(Short key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Short2DoubleMap.Entry ceilingEntry(Short key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Short2DoubleMap.Entry higherEntry(Short key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Short2DoubleOrderedMap {
		Short2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Short2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Short2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(short key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(short key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(short key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(short key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(short key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(short key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Short2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Short2DoubleSortedMap {
		Short2DoubleSortedMap map;
		
		SynchronizedSortedMap(Short2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Short2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Short2DoubleSortedMap subMap(short fromKey, short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Short2DoubleSortedMap headMap(short toKey)  { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Short2DoubleSortedMap tailMap(short fromKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public short firstShortKey() { synchronized(mutex) { return map.firstShortKey(); } }
		@Override
		public short pollFirstShortKey() { synchronized(mutex) { return map.pollFirstShortKey(); } }
		@Override
		public short lastShortKey() { synchronized(mutex) { return map.lastShortKey(); } }
		@Override
		public short pollLastShortKey() { synchronized(mutex) { return map.pollLastShortKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Short2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Short firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Short lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Short2DoubleSortedMap subMap(Short fromKey, Short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleSortedMap headMap(Short toKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Short2DoubleSortedMap tailMap(Short fromKey) { synchronized(mutex) { return Short2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractShort2DoubleMap implements Short2DoubleMap {
		Short2DoubleMap map;
		DoubleCollection values;
		ShortSet keys;
		ObjectSet<Short2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Short2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Short2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractShort2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(short key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(short key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Short2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(short key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(short key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Short2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Short2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Short, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(short[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(short key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(short key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(short key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(short key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(short key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(short key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(short key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Short2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(ShortDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(short key, ShortDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(short key, Short2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(short key, ShortDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double mergeDouble(short key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Short2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(short key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ShortDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Short2DoubleMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortSet keySet() {
			if(keys == null) keys = ShortSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Short2DoubleMap.Entry> short2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.short2DoubleEntrySet(), mutex);
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
		public Double put(Short key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Short key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Short key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Short key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Short key, Function<? super Short, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Short key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Short, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}