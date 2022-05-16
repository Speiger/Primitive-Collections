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
import speiger.src.collections.doubles.functions.consumer.DoubleBooleanConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.function.DoubleBooleanUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.utils.BooleanCollections;
import speiger.src.collections.booleans.utils.BooleanSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Double2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Double2BooleanMap.Entry> fastIterator(Double2BooleanMap map) {
		ObjectSet<Double2BooleanMap.Entry> entries = map.double2BooleanEntrySet();
		return entries instanceof Double2BooleanMap.FastEntrySet ? ((Double2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Double2BooleanMap.Entry> fastIterable(Double2BooleanMap map) {
		ObjectSet<Double2BooleanMap.Entry> entries = map.double2BooleanEntrySet();
		return map instanceof Double2BooleanMap.FastEntrySet ? new ObjectIterable<Double2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Double2BooleanMap.Entry> iterator() { return ((Double2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2BooleanMap.Entry> action) { ((Double2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Double2BooleanMap map, Consumer<Double2BooleanMap.Entry> action) {
		ObjectSet<Double2BooleanMap.Entry> entries = map.double2BooleanEntrySet();
		if(entries instanceof Double2BooleanMap.FastEntrySet) ((Double2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Double2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanMap synchronize(Double2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanMap synchronize(Double2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanSortedMap synchronize(Double2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanSortedMap synchronize(Double2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanOrderedMap synchronize(Double2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanOrderedMap synchronize(Double2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanNavigableMap synchronize(Double2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Double2BooleanNavigableMap synchronize(Double2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Double2BooleanMap unmodifiable(Double2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2BooleanOrderedMap unmodifiable(Double2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Double2BooleanSortedMap unmodifiable(Double2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Double2BooleanNavigableMap unmodifiable(Double2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2BooleanMap.Entry unmodifiable(Double2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Double2BooleanMap.Entry unmodifiable(Map.Entry<Double, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Double2BooleanMap singleton(double key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractDouble2BooleanMap {
		final double key;
		final boolean value;
		DoubleSet keySet;
		BooleanCollection values;
		ObjectSet<Double2BooleanMap.Entry> entrySet;
		
		SingletonMap(double key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(double key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(double key, boolean defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2BooleanMap.Entry> double2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractDouble2BooleanMap {
		@Override
		public boolean put(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(double key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(double key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(double key, boolean defaultValue) { return false; }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Double2BooleanMap.Entry> double2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractDouble2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Double, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2BooleanMap.Entry entry) {
			super(entry.getDoubleKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(double key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Double2BooleanNavigableMap {
		Double2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Double2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2BooleanNavigableMap descendingMap() { return Double2BooleanMaps.synchronize(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2BooleanMap.Entry firstEntry() { return Double2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2BooleanMap.Entry lastEntry() { return Double2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2BooleanNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2BooleanNavigableMap headMap(double toKey, boolean inclusive) { return Double2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2BooleanNavigableMap tailMap(double fromKey, boolean inclusive) { return Double2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2BooleanNavigableMap subMap(double fromKey, double toKey) { return Double2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2BooleanNavigableMap headMap(double toKey) { return Double2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2BooleanNavigableMap tailMap(double fromKey) { return Double2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2BooleanMap.Entry lowerEntry(double key) { return Double2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2BooleanMap.Entry higherEntry(double key) { return Double2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2BooleanMap.Entry floorEntry(double key) { return Double2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2BooleanMap.Entry ceilingEntry(double key) { return Double2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Double2BooleanOrderedMap {
		Double2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Double2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Double2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Double2BooleanSortedMap {
		Double2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Double2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2BooleanSortedMap subMap(double fromKey, double toKey) { return Double2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2BooleanSortedMap headMap(double toKey) { return Double2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2BooleanSortedMap tailMap(double fromKey) { return Double2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Double2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractDouble2BooleanMap implements Double2BooleanMap {
		Double2BooleanMap map;
		BooleanCollection values;
		DoubleSet keys;
		ObjectSet<Double2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Double2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(double key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(double key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(double key) { return map.get(key); }
		@Override
		public boolean getOrDefault(double key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Double2BooleanMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2BooleanMap.Entry> double2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.double2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Double2BooleanMap.Entry>
	{
		ObjectSet<Double2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Double2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Double2BooleanMap.Entry>() {
				ObjectIterator<Double2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2BooleanMap.Entry next() { return Double2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Double2BooleanNavigableMap {
		Double2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Double2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Double2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2BooleanNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2BooleanNavigableMap headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2BooleanNavigableMap tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2BooleanNavigableMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2BooleanNavigableMap headMap(double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2BooleanNavigableMap tailMap(double fromKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2BooleanMap.Entry lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2BooleanMap.Entry higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2BooleanMap.Entry floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2BooleanMap.Entry ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2BooleanNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap headMap(Double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanNavigableMap tailMap(Double fromKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2BooleanMap.Entry lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2BooleanMap.Entry floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2BooleanMap.Entry ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2BooleanMap.Entry higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Double2BooleanOrderedMap {
		Double2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Double2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(double key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(double key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Double2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Double2BooleanSortedMap {
		Double2BooleanSortedMap map;
		
		SynchronizedSortedMap(Double2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2BooleanSortedMap subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2BooleanSortedMap headMap(double toKey)  { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2BooleanSortedMap tailMap(double fromKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Double2BooleanSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2BooleanSortedMap subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanSortedMap headMap(Double toKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2BooleanSortedMap tailMap(Double fromKey) { synchronized(mutex) { return Double2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractDouble2BooleanMap implements Double2BooleanMap {
		Double2BooleanMap map;
		BooleanCollection values;
		DoubleSet keys;
		ObjectSet<Double2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(double key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(double key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Double2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(double key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(double key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Double2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(DoubleBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(double key, DoubleBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(double key, Double2BooleanFunction mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(double key, DoubleBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean mergeBoolean(double key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Double2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(double key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Double2BooleanMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2BooleanMap.Entry> double2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2BooleanEntrySet(), mutex);
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
		public Boolean put(Double key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Double key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Double key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Double key, Function<? super Double, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Double key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}