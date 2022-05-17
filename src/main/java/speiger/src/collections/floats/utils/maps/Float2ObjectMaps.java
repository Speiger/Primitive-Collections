package speiger.src.collections.floats.utils.maps;

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
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.consumer.FloatObjectConsumer;
import speiger.src.collections.floats.functions.function.Float2ObjectFunction;
import speiger.src.collections.floats.functions.function.FloatObjectUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Float2ObjectMap<V> empty() { 
		return (Float2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Float2ObjectMap.Entry<V>> fastIterator(Float2ObjectMap<V> map) {
		ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
		return entries instanceof Float2ObjectMap.FastEntrySet ? ((Float2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Float2ObjectMap.Entry<V>> fastIterable(Float2ObjectMap<V> map) {
		ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
		return map instanceof Float2ObjectMap.FastEntrySet ? new ObjectIterable<Float2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() { return ((Float2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2ObjectMap.Entry<V>> action) { ((Float2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Float2ObjectMap<V> map, Consumer<Float2ObjectMap.Entry<V>> action) {
		ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
		if(entries instanceof Float2ObjectMap.FastEntrySet) ((Float2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
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
	public static <V> Float2ObjectMap<V> synchronize(Float2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectMap<V> synchronize(Float2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectSortedMap<V> synchronize(Float2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectSortedMap<V> synchronize(Float2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectOrderedMap<V> synchronize(Float2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectOrderedMap<V> synchronize(Float2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectNavigableMap<V> synchronize(Float2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Float2ObjectNavigableMap<V> synchronize(Float2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Float2ObjectMap<V> unmodifiable(Float2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Float2ObjectOrderedMap<V> unmodifiable(Float2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Float2ObjectSortedMap<V> unmodifiable(Float2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Float2ObjectNavigableMap<V> unmodifiable(Float2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Float2ObjectMap.Entry<V> unmodifiable(Float2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Float2ObjectMap.Entry<V> unmodifiable(Map.Entry<Float, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Float2ObjectMap<V> singleton(float key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractFloat2ObjectMap<V> {
		final float key;
		final V value;
		FloatSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Float2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(float key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(float key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(float key, V defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractFloat2ObjectMap<V> {
		@Override
		public V put(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(float key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(float key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(float key, V defaultValue) { return null; }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractFloat2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Float, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2ObjectMap.Entry<V> entry) {
			super(entry.getFloatKey(), entry.getValue());
		}
		
		@Override
		public void set(float key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Float2ObjectNavigableMap<V> {
		Float2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Float2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2ObjectNavigableMap<V> descendingMap() { return Float2ObjectMaps.synchronize(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2ObjectMap.Entry<V> firstEntry() { return Float2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2ObjectMap.Entry<V> lastEntry() { return Float2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey, boolean inclusive) { return Float2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey, boolean inclusive) { return Float2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, float toKey) { return Float2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey) { return Float2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey) { return Float2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(float e) { throw new UnsupportedOperationException(); }
		@Override
		public float getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(float e) { throw new UnsupportedOperationException(); }
		@Override
		public float getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public float lowerKey(float key) { return map.lowerKey(key); }
		@Override
		public float higherKey(float key) { return map.higherKey(key); }
		@Override
		public float floorKey(float key) { return map.floorKey(key); }
		@Override
		public float ceilingKey(float key) { return map.ceilingKey(key); }
		@Override
		public Float2ObjectMap.Entry<V> lowerEntry(float key) { return Float2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2ObjectMap.Entry<V> higherEntry(float key) { return Float2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2ObjectMap.Entry<V> floorEntry(float key) { return Float2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2ObjectMap.Entry<V> ceilingEntry(float key) { return Float2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Float2ObjectOrderedMap<V> {
		Float2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Float2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Float2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Float2ObjectSortedMap<V> {
		Float2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Float2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2ObjectSortedMap<V> subMap(float fromKey, float toKey) { return Float2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ObjectSortedMap<V> headMap(float toKey) { return Float2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ObjectSortedMap<V> tailMap(float fromKey) { return Float2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Float2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractFloat2ObjectMap<V> implements Float2ObjectMap<V> {
		Float2ObjectMap<V> map;
		ObjectCollection<V> values;
		FloatSet keys;
		ObjectSet<Float2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Float2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(float key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(float key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(float key) { return map.get(key); }
		@Override
		public V getOrDefault(float key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Float2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.float2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Float2ObjectMap.Entry<V>>
	{
		ObjectSet<Float2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Float2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Float2ObjectMap.Entry<V>>() {
				ObjectIterator<Float2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2ObjectMap.Entry<V> next() { return Float2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Float2ObjectNavigableMap<V> {
		Float2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Float2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Float2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2ObjectMap.Entry<V> lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2ObjectMap.Entry<V> higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2ObjectMap.Entry<V> floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2ObjectMap.Entry<V> ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> headMap(Float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectNavigableMap<V> tailMap(Float fromKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(float e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public float getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(float e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public float getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Float lowerKey(Float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Float floorKey(Float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Float ceilingKey(Float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Float higherKey(Float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Float2ObjectMap.Entry<V> lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2ObjectMap.Entry<V> floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2ObjectMap.Entry<V> ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2ObjectMap.Entry<V> higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Float2ObjectOrderedMap<V> {
		Float2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Float2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(float key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(float key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Float2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Float2ObjectSortedMap<V> {
		Float2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Float2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2ObjectSortedMap<V> subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ObjectSortedMap<V> headMap(float toKey)  { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ObjectSortedMap<V> tailMap(float fromKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Float2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2ObjectSortedMap<V> subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectSortedMap<V> headMap(Float toKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ObjectSortedMap<V> tailMap(Float fromKey) { synchronized(mutex) { return Float2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractFloat2ObjectMap<V> implements Float2ObjectMap<V> {
		Float2ObjectMap<V> map;
		ObjectCollection<V> values;
		FloatSet keys;
		ObjectSet<Float2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(float key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(float key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Float2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(float key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(float key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Float2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(FloatObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(float key, FloatObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(float key, Float2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V merge(float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Float2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(float key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Float2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2ObjectEntrySet(), mutex);
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
		public V put(Float key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Float key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Float key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Float key, Function<? super Float, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Float key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}