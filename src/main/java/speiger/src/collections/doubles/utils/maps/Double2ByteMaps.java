package speiger.src.collections.doubles.utils.maps;

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
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.consumer.DoubleByteConsumer;
import speiger.src.collections.doubles.functions.function.Double2ByteFunction;
import speiger.src.collections.doubles.functions.function.DoubleByteUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.bytes.utils.ByteSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Double2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2ByteMap.Entry> fastIterator(Double2ByteMap map) {
		ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
		return entries instanceof Double2ByteMap.FastEntrySet ? ((Double2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2ByteMap.Entry> fastIterable(Double2ByteMap map) {
		ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
		return map instanceof Double2ByteMap.FastEntrySet ? new ObjectIterable<Double2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Double2ByteMap.Entry> iterator() { return ((Double2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2ByteMap.Entry> action) { ((Double2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2ByteMap map, Consumer<Double2ByteMap.Entry> action) {
		ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
		if(entries instanceof Double2ByteMap.FastEntrySet) ((Double2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteMap synchronize(Double2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteMap synchronize(Double2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteSortedMap synchronize(Double2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteSortedMap synchronize(Double2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteOrderedMap synchronize(Double2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteOrderedMap synchronize(Double2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteNavigableMap synchronize(Double2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2ByteNavigableMap synchronize(Double2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2ByteMap unmodifiable(Double2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ByteOrderedMap unmodifiable(Double2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ByteSortedMap unmodifiable(Double2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2ByteNavigableMap unmodifiable(Double2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2ByteMap.Entry unmodifiable(Double2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2ByteMap.Entry unmodifiable(Map.Entry<Double, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2ByteMap singleton(double key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2ByteMap {
		final double key;
		final byte value;
		DoubleSet keySet;
		ByteCollection values;
		ObjectSet<Double2ByteMap.Entry> entrySet;
		
		SingletonMap(double key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(double key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(double key, byte defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2ByteMap.Entry> double2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2ByteMap {
		@Override
		public byte put(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(double key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(double key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(double key, byte defaultValue) { return (byte)0; }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Double2ByteMap.Entry> double2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2ByteMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getByteValue());
		}
		
		@Override
		public void set(double key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2ByteNavigableMap {
		Double2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Double2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2ByteNavigableMap descendingMap() { return Double2ByteMaps.synchronize(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2ByteMap.Entry firstEntry() { return Double2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2ByteMap.Entry lastEntry() { return Double2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ByteNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2ByteNavigableMap headMap(double toKey, boolean inclusive) { return Double2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2ByteNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2ByteNavigableMap subMap(double fromKey, double toKey) { return Double2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ByteNavigableMap headMap(double toKey) { return Double2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ByteNavigableMap tailMap(double fromKey) { return Double2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(double e) { throw new UnsupportedOperationException(); }
		@Override
		public double getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(double e) { throw new UnsupportedOperationException(); }
		@Override
		public double getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public double lowerKey(double key) { return map.lowerKey(key); }
		@Override
		public double higherKey(double key) { return map.higherKey(key); }
		@Override
		public double floorKey(double key) { return map.floorKey(key); }
		@Override
		public double ceilingKey(double key) { return map.ceilingKey(key); }
		@Override
		public Double2ByteMap.Entry lowerEntry(double key) { return Double2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2ByteMap.Entry higherEntry(double key) { return Double2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2ByteMap.Entry floorEntry(double key) { return Double2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2ByteMap.Entry ceilingEntry(double key) { return Double2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2ByteOrderedMap {
		Double2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Double2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Double2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2ByteSortedMap {
		Double2ByteSortedMap map;
		
		UnmodifyableSortedMap(Double2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2ByteSortedMap subMap(double fromKey, double toKey) { return Double2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ByteSortedMap headMap(double toKey) { return Double2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ByteSortedMap tailMap(double fromKey) { return Double2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Double2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2ByteMap implements Double2ByteMap {
		Double2ByteMap map;
		ByteCollection values;
		DoubleSet keys;
		ObjectSet<Double2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Double2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(double key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(double key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(double key) { return map.get(key); }
		@Override
		public byte getOrDefault(double key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Double2ByteMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2ByteMap.Entry> double2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2ByteMap.Entry>
	{
		ObjectSet<Double2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Double2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2ByteMap.Entry> iterator() {
			return new ObjectIterator<Double2ByteMap.Entry>() {
				ObjectIterator<Double2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2ByteMap.Entry next() { return Double2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2ByteNavigableMap {
		Double2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Double2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2ByteNavigableMap descendingMap() { synchronized(mutex) { return Double2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Double2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2ByteNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2ByteNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2ByteNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2ByteNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ByteNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ByteNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2ByteMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2ByteMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2ByteMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2ByteMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double2ByteNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ByteNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ByteNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ByteNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ByteNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ByteNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(double e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public double getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(double e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public double getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Double lowerKey(Double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Double floorKey(Double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Double ceilingKey(Double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Double higherKey(Double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Double2ByteMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2ByteMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2ByteMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2ByteMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2ByteOrderedMap {
		Double2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Double2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(double key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(double key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Double2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2ByteSortedMap {
		Double2ByteSortedMap map;
		
		SynchronizedSortedMap(Double2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2ByteSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ByteSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ByteSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Double2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2ByteSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ByteSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ByteSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2ByteMap implements Double2ByteMap {
		Double2ByteMap map;
		ByteCollection values;
		DoubleSet keys;
		ObjectSet<Double2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(double key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(double key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(double key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(double key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Double2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Double2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(double key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(double key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Double2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(DoubleByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(double key, DoubleByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(double key, Double2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(double key, DoubleByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte mergeByte(double key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Double2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(double key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Double2ByteMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2ByteMap.Entry> double2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2ByteEntrySet(), mutex);
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
		public Byte put(Double key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Double key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Double key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Double key, BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Double key, Function<? super Double, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Double key, BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Double key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}