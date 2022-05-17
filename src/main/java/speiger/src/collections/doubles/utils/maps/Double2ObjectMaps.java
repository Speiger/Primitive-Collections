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
import speiger.src.collections.doubles.functions.consumer.DoubleObjectConsumer;
import speiger.src.collections.doubles.functions.function.Double2ObjectFunction;
import speiger.src.collections.doubles.functions.function.DoubleObjectUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectSortedMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectOrderedMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Double2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Double2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Double2ObjectMap<V> empty() { 
		return (Double2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Double2ObjectMap.Entry<V>> fastIterator(Double2ObjectMap<V> map) {
		ObjectSet<Double2ObjectMap.Entry<V>> entries = map.double2ObjectEntrySet();
		return entries instanceof Double2ObjectMap.FastEntrySet ? ((Double2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Double2ObjectMap.Entry<V>> fastIterable(Double2ObjectMap<V> map) {
		ObjectSet<Double2ObjectMap.Entry<V>> entries = map.double2ObjectEntrySet();
		return map instanceof Double2ObjectMap.FastEntrySet ? new ObjectIterable<Double2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Double2ObjectMap.Entry<V>> iterator() { return ((Double2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Double2ObjectMap.Entry<V>> action) { ((Double2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Double2ObjectMap<V> map, Consumer<Double2ObjectMap.Entry<V>> action) {
		ObjectSet<Double2ObjectMap.Entry<V>> entries = map.double2ObjectEntrySet();
		if(entries instanceof Double2ObjectMap.FastEntrySet) ((Double2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectMap<V> synchronize(Double2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectMap<V> synchronize(Double2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectSortedMap<V> synchronize(Double2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectSortedMap<V> synchronize(Double2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectOrderedMap<V> synchronize(Double2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectOrderedMap<V> synchronize(Double2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectNavigableMap<V> synchronize(Double2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Double2ObjectNavigableMap<V> synchronize(Double2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Double2ObjectMap<V> unmodifiable(Double2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Double2ObjectOrderedMap<V> unmodifiable(Double2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Double2ObjectSortedMap<V> unmodifiable(Double2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Double2ObjectNavigableMap<V> unmodifiable(Double2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Double2ObjectMap.Entry<V> unmodifiable(Double2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Double2ObjectMap.Entry<V> unmodifiable(Map.Entry<Double, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Double2ObjectMap<V> singleton(double key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractDouble2ObjectMap<V> {
		final double key;
		final V value;
		DoubleSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Double2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(double key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(double key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(double key) { return Objects.equals(this.key, Double.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(double key, V defaultValue) { return Objects.equals(this.key, Double.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public DoubleSet keySet() { 
			if(keySet == null) keySet = DoubleSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractDouble2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractDouble2ObjectMap<V> {
		@Override
		public V put(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(double key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(double key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(double key, V defaultValue) { return null; }
		@Override
		public DoubleSet keySet() { return DoubleSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractDouble2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Double, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Double2ObjectMap.Entry<V> entry) {
			super(entry.getDoubleKey(), entry.getValue());
		}
		
		@Override
		public void set(double key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Double2ObjectNavigableMap<V> {
		Double2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Double2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Double2ObjectNavigableMap<V> descendingMap() { return Double2ObjectMaps.synchronize(map.descendingMap()); }
		@Override
		public DoubleNavigableSet navigableKeySet() { return DoubleSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public DoubleNavigableSet descendingKeySet() { return DoubleSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Double2ObjectMap.Entry<V> firstEntry() { return Double2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Double2ObjectMap.Entry<V> lastEntry() { return Double2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Double2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Double2ObjectNavigableMap<V> subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { return Double2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Double2ObjectNavigableMap<V> headMap(double toKey, boolean inclusive) { return Double2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Double2ObjectNavigableMap<V> tailMap(double fromKey, boolean inclusive) { return Double2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Double2ObjectNavigableMap<V> subMap(double fromKey, double toKey) { return Double2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ObjectNavigableMap<V> headMap(double toKey) { return Double2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ObjectNavigableMap<V> tailMap(double fromKey) { return Double2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Double2ObjectMap.Entry<V> lowerEntry(double key) { return Double2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Double2ObjectMap.Entry<V> higherEntry(double key) { return Double2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Double2ObjectMap.Entry<V> floorEntry(double key) { return Double2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Double2ObjectMap.Entry<V> ceilingEntry(double key) { return Double2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Double2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Double2ObjectOrderedMap<V> {
		Double2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Double2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Double2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Double2ObjectSortedMap<V> {
		Double2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Double2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public Double2ObjectSortedMap<V> subMap(double fromKey, double toKey) { return Double2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Double2ObjectSortedMap<V> headMap(double toKey) { return Double2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Double2ObjectSortedMap<V> tailMap(double fromKey) { return Double2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public double firstDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Double2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractDouble2ObjectMap<V> implements Double2ObjectMap<V> {
		Double2ObjectMap<V> map;
		ObjectCollection<V> values;
		DoubleSet keys;
		ObjectSet<Double2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Double2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(double key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(double key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(double key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(double key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(double key) { return map.get(key); }
		@Override
		public V getOrDefault(double key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Double2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleSet keySet() { 
			if(keys == null) keys = DoubleSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.double2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Double2ObjectMap.Entry<V>>
	{
		ObjectSet<Double2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Double2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Double2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Double2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Double2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Double2ObjectMap.Entry<V>>() {
				ObjectIterator<Double2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Double2ObjectMap.Entry<V> next() { return Double2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Double2ObjectNavigableMap<V> {
		Double2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Double2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Double2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Double2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public DoubleNavigableSet navigableKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public DoubleNavigableSet descendingKeySet() { synchronized(mutex) { return DoubleSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Double2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Double2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Double2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Double2ObjectNavigableMap<V> subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Double2ObjectNavigableMap<V> headMap(double toKey, boolean inclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Double2ObjectNavigableMap<V> tailMap(double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Double2ObjectNavigableMap<V> subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ObjectNavigableMap<V> headMap(double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ObjectNavigableMap<V> tailMap(double fromKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double lowerKey(double key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public double higherKey(double key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public double floorKey(double key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public double ceilingKey(double key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Double2ObjectMap.Entry<V> lowerEntry(double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Double2ObjectMap.Entry<V> higherEntry(double key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Double2ObjectMap.Entry<V> floorEntry(double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Double2ObjectMap.Entry<V> ceilingEntry(double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Double2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> headMap(Double toKey, boolean inclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> tailMap(Double fromKey, boolean inclusive) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> headMap(Double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectNavigableMap<V> tailMap(Double fromKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Double2ObjectMap.Entry<V> lowerEntry(Double key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Double2ObjectMap.Entry<V> floorEntry(Double key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Double2ObjectMap.Entry<V> ceilingEntry(Double key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Double2ObjectMap.Entry<V> higherEntry(Double key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Double2ObjectOrderedMap<V> {
		Double2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Double2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Double2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(double key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(double key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(double key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(double key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(double key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(double key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Double2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Double2ObjectSortedMap<V> {
		Double2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Double2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Double2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Double2ObjectSortedMap<V> subMap(double fromKey, double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Double2ObjectSortedMap<V> headMap(double toKey)  { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Double2ObjectSortedMap<V> tailMap(double fromKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public double firstDoubleKey() { synchronized(mutex) { return map.firstDoubleKey(); } }
		@Override
		public double pollFirstDoubleKey() { synchronized(mutex) { return map.pollFirstDoubleKey(); } }
		@Override
		public double lastDoubleKey() { synchronized(mutex) { return map.lastDoubleKey(); } }
		@Override
		public double pollLastDoubleKey() { synchronized(mutex) { return map.pollLastDoubleKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Double2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Double firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Double lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Double2ObjectSortedMap<V> subMap(Double fromKey, Double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectSortedMap<V> headMap(Double toKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Double2ObjectSortedMap<V> tailMap(Double fromKey) { synchronized(mutex) { return Double2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractDouble2ObjectMap<V> implements Double2ObjectMap<V> {
		Double2ObjectMap<V> map;
		ObjectCollection<V> values;
		DoubleSet keys;
		ObjectSet<Double2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Double2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Double2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractDouble2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(double key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(double key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Double2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Double2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Double, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(double[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(double key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(double key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(double key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(double key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(double key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(double key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(double key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Double2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(DoubleObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(double key, DoubleObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(double key, Double2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(double key, DoubleObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V merge(double key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Double2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(double key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(DoubleObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Double2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public DoubleSet keySet() {
			if(keys == null) keys = DoubleSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.double2ObjectEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public V get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public V getOrDefault(Object key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public V put(Double key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Double key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Double key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Double key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Double, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Double key, Function<? super Double, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Double key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Double, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}