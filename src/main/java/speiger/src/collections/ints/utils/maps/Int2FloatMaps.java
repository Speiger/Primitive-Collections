package speiger.src.collections.ints.utils.maps;

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
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.function.Int2FloatFunction;
import speiger.src.collections.ints.functions.function.IntFloatUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.utils.FloatCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2FloatMap.Entry> fastIterator(Int2FloatMap map) {
		ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
		return entries instanceof Int2FloatMap.FastEntrySet ? ((Int2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2FloatMap.Entry> fastIterable(Int2FloatMap map) {
		ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
		return map instanceof Int2FloatMap.FastEntrySet ? new ObjectIterable<Int2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Int2FloatMap.Entry> iterator() { return ((Int2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2FloatMap.Entry> action) { ((Int2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2FloatMap map, Consumer<Int2FloatMap.Entry> action) {
		ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
		if(entries instanceof Int2FloatMap.FastEntrySet) ((Int2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatMap synchronize(Int2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatMap synchronize(Int2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatSortedMap synchronize(Int2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatSortedMap synchronize(Int2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatOrderedMap synchronize(Int2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatOrderedMap synchronize(Int2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatNavigableMap synchronize(Int2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2FloatNavigableMap synchronize(Int2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2FloatMap unmodifiable(Int2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2FloatOrderedMap unmodifiable(Int2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2FloatSortedMap unmodifiable(Int2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2FloatNavigableMap unmodifiable(Int2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2FloatMap.Entry unmodifiable(Int2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2FloatMap.Entry unmodifiable(Map.Entry<Integer, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2FloatMap singleton(int key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2FloatMap {
		final int key;
		final float value;
		IntSet keySet;
		FloatCollection values;
		ObjectSet<Int2FloatMap.Entry> entrySet;
		
		SingletonMap(int key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(int key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(int key, float defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2FloatMap.Entry> int2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2FloatMap {
		@Override
		public float put(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(int key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(int key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(int key, float defaultValue) { return defaultValue; }
		@Override
		public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Int2FloatMap.Entry> int2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2FloatMap.Entry entry) {
			super(entry.getIntKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(int key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2FloatNavigableMap {
		Int2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Int2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2FloatNavigableMap descendingMap() { return Int2FloatMaps.unmodifiable(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2FloatMap.Entry firstEntry() { return Int2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2FloatMap.Entry lastEntry() { return Int2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2FloatNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2FloatNavigableMap headMap(int toKey, boolean inclusive) { return Int2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2FloatNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2FloatNavigableMap subMap(int fromKey, int toKey) { return Int2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2FloatNavigableMap headMap(int toKey) { return Int2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2FloatNavigableMap tailMap(int fromKey) { return Int2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lowerKey(int key) { return map.lowerKey(key); }
		@Override
		public int higherKey(int key) { return map.higherKey(key); }
		@Override
		public int floorKey(int key) { return map.floorKey(key); }
		@Override
		public int ceilingKey(int key) { return map.ceilingKey(key); }
		@Override
		public Int2FloatMap.Entry lowerEntry(int key) { return Int2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2FloatMap.Entry higherEntry(int key) { return Int2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2FloatMap.Entry floorEntry(int key) { return Int2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2FloatMap.Entry ceilingEntry(int key) { return Int2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2FloatNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2FloatOrderedMap {
		Int2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Int2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Int2FloatOrderedMap copy() { return map.copy(); }
		@Override
		public IntOrderedSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return (IntOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Int2FloatMap.Entry> int2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.int2FloatEntrySet());
			return (ObjectOrderedSet<Int2FloatMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2FloatSortedMap {
		Int2FloatSortedMap map;
		
		UnmodifyableSortedMap(Int2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2FloatSortedMap subMap(int fromKey, int toKey) { return Int2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2FloatSortedMap headMap(int toKey) { return Int2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2FloatSortedMap tailMap(int fromKey) { return Int2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public IntSortedSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Int2FloatSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2FloatMap implements Int2FloatMap {
		Int2FloatMap map;
		FloatCollection values;
		IntSet keys;
		ObjectSet<Int2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Int2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(int key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(int key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(int key) {
			float type = map.get(key);
			return Float.floatToIntBits(type) == Float.floatToIntBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public float getOrDefault(int key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(IntFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceFloats(Int2FloatMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Int2FloatMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2FloatMap.Entry> int2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Int2FloatMap.Entry>
	{
		ObjectOrderedSet<Int2FloatMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Int2FloatMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Int2FloatMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> iterator(Int2FloatMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Int2FloatMap.Entry first() { return set.first(); }
		@Override
		public Int2FloatMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Int2FloatMap.Entry last() { return set.last(); }
		@Override
		public Int2FloatMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2FloatMap.Entry>
	{
		ObjectSet<Int2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Int2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2FloatMap.Entry> iterator() {
			return new ObjectIterator<Int2FloatMap.Entry>() {
				ObjectIterator<Int2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2FloatMap.Entry next() { return Int2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2FloatNavigableMap {
		Int2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Int2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2FloatNavigableMap descendingMap() { synchronized(mutex) { return Int2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public IntNavigableSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Int2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Int2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2FloatNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2FloatNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2FloatNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2FloatNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2FloatNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2FloatNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2FloatMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2FloatMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2FloatMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2FloatMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2FloatNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2FloatNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Integer lowerKey(Integer key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Integer floorKey(Integer key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Integer ceilingKey(Integer key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Integer higherKey(Integer key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Int2FloatMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2FloatMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2FloatMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2FloatMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2FloatOrderedMap {
		Int2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Int2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(int key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(int key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Int2FloatOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntOrderedSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return (IntOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Int2FloatMap.Entry> int2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2FloatEntrySet(), mutex);
			return (ObjectOrderedSet<Int2FloatMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2FloatSortedMap {
		Int2FloatSortedMap map;
		
		SynchronizedSortedMap(Int2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2FloatSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2FloatSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2FloatSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public IntSortedSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Int2FloatSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2FloatSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2FloatSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2FloatSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2FloatMap implements Int2FloatMap {
		Int2FloatMap map;
		FloatCollection values;
		IntSet keys;
		ObjectSet<Int2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(int key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(int key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(int key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(int key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(int key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(int key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Int2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(IntFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsent(key, valueProvider); } }
		@Override
		public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(int key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Int2FloatMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2FloatMap.Entry> int2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2FloatEntrySet(), mutex);
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
		public Float put(Integer key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Integer key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Integer key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Integer key, Function<? super Integer, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Integer key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}