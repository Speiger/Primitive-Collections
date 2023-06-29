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
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.consumer.FloatIntConsumer;
import speiger.src.collections.floats.functions.function.Float2IntFunction;
import speiger.src.collections.floats.functions.function.FloatIntUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.utils.IntCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2IntMap.Entry> fastIterator(Float2IntMap map) {
		ObjectSet<Float2IntMap.Entry> entries = map.float2IntEntrySet();
		return entries instanceof Float2IntMap.FastEntrySet ? ((Float2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2IntMap.Entry> fastIterable(Float2IntMap map) {
		ObjectSet<Float2IntMap.Entry> entries = map.float2IntEntrySet();
		return map instanceof Float2IntMap.FastEntrySet ? new ObjectIterable<Float2IntMap.Entry>(){
			@Override
			public ObjectIterator<Float2IntMap.Entry> iterator() { return ((Float2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2IntMap.Entry> action) { ((Float2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2IntMap map, Consumer<Float2IntMap.Entry> action) {
		ObjectSet<Float2IntMap.Entry> entries = map.float2IntEntrySet();
		if(entries instanceof Float2IntMap.FastEntrySet) ((Float2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntMap synchronize(Float2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntMap synchronize(Float2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntSortedMap synchronize(Float2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntSortedMap synchronize(Float2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntOrderedMap synchronize(Float2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntOrderedMap synchronize(Float2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntNavigableMap synchronize(Float2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2IntNavigableMap synchronize(Float2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2IntMap unmodifiable(Float2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2IntOrderedMap unmodifiable(Float2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2IntSortedMap unmodifiable(Float2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2IntNavigableMap unmodifiable(Float2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2IntMap.Entry unmodifiable(Float2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2IntMap.Entry unmodifiable(Map.Entry<Float, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2IntMap singleton(float key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2IntMap {
		final float key;
		final int value;
		FloatSet keySet;
		IntCollection values;
		ObjectSet<Float2IntMap.Entry> entrySet;
		
		SingletonMap(float key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(float key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(float key, int defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public int computeInt(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2IntMap {
		@Override
		public int put(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(float key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(float key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(float key, int defaultValue) { return defaultValue; }
		@Override
		public int computeInt(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2IntMap.Entry entry) {
			super(entry.getFloatKey(), entry.getIntValue());
		}
		
		@Override
		public void set(float key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2IntNavigableMap {
		Float2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Float2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2IntNavigableMap descendingMap() { return Float2IntMaps.unmodifiable(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2IntMap.Entry firstEntry() { return Float2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2IntMap.Entry lastEntry() { return Float2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2IntNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2IntNavigableMap headMap(float toKey, boolean inclusive) { return Float2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2IntNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2IntNavigableMap subMap(float fromKey, float toKey) { return Float2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2IntNavigableMap headMap(float toKey) { return Float2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2IntNavigableMap tailMap(float fromKey) { return Float2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2IntMap.Entry lowerEntry(float key) { return Float2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2IntMap.Entry higherEntry(float key) { return Float2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2IntMap.Entry floorEntry(float key) { return Float2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2IntMap.Entry ceilingEntry(float key) { return Float2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2IntNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2IntOrderedMap {
		Float2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Float2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Float2IntOrderedMap copy() { return map.copy(); }
		@Override
		public FloatOrderedSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return (FloatOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Float2IntMap.Entry> float2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.float2IntEntrySet());
			return (ObjectOrderedSet<Float2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2IntSortedMap {
		Float2IntSortedMap map;
		
		UnmodifyableSortedMap(Float2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2IntSortedMap subMap(float fromKey, float toKey) { return Float2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2IntSortedMap headMap(float toKey) { return Float2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2IntSortedMap tailMap(float fromKey) { return Float2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public FloatSortedSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Float2IntSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2IntMap implements Float2IntMap {
		Float2IntMap map;
		IntCollection values;
		FloatSet keys;
		ObjectSet<Float2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Float2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(float key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(float key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(float key) {
			int type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public int getOrDefault(float key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public int computeInt(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsent(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(FloatIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceInts(Float2IntMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Float2IntMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Float2IntMap.Entry>
	{
		ObjectOrderedSet<Float2IntMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Float2IntMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Float2IntMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> iterator(Float2IntMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Float2IntMap.Entry first() { return set.first(); }
		@Override
		public Float2IntMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Float2IntMap.Entry last() { return set.last(); }
		@Override
		public Float2IntMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2IntMap.Entry>
	{
		ObjectSet<Float2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Float2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2IntMap.Entry> iterator() {
			return new ObjectIterator<Float2IntMap.Entry>() {
				ObjectIterator<Float2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2IntMap.Entry next() { return Float2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2IntNavigableMap {
		Float2IntNavigableMap map;
		
		SynchronizedNavigableMap(Float2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2IntNavigableMap descendingMap() { synchronized(mutex) { return Float2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public FloatNavigableSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Float2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2IntMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Float2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2IntNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2IntNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2IntNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2IntNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2IntNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2IntNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2IntMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2IntMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2IntMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2IntMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2IntNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2IntNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2IntMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2IntMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2IntMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2IntMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2IntOrderedMap {
		Float2IntOrderedMap map;
		
		SynchronizedOrderedMap(Float2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(float key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(float key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Float2IntOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatOrderedSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return (FloatOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Float2IntMap.Entry> float2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2IntEntrySet(), mutex);
			return (ObjectOrderedSet<Float2IntMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2IntSortedMap {
		Float2IntSortedMap map;
		
		SynchronizedSortedMap(Float2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2IntSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2IntSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2IntSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public FloatSortedSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Float2IntSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2IntSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2IntSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2IntSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2IntMap implements Float2IntMap {
		Float2IntMap map;
		IntCollection values;
		FloatSet keys;
		ObjectSet<Float2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(float key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(float key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(float key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(float key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(float key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(float key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Float2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(FloatIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(float key, FloatIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int supplyIntIfAbsent(float key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsent(key, valueProvider); } }
		@Override
		public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider) { synchronized(mutex) { return map.supplyIntIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(float key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Float2IntMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2IntEntrySet(), mutex);
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
		public Integer put(Float key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Float key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Float key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Float key, Function<? super Float, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Float key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}