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
import speiger.src.collections.floats.functions.consumer.FloatByteConsumer;
import speiger.src.collections.floats.functions.function.Float2ByteFunction;
import speiger.src.collections.floats.functions.function.FloatByteUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.utils.ByteCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2ByteMap.Entry> fastIterator(Float2ByteMap map) {
		ObjectSet<Float2ByteMap.Entry> entries = map.float2ByteEntrySet();
		return entries instanceof Float2ByteMap.FastEntrySet ? ((Float2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2ByteMap.Entry> fastIterable(Float2ByteMap map) {
		ObjectSet<Float2ByteMap.Entry> entries = map.float2ByteEntrySet();
		return map instanceof Float2ByteMap.FastEntrySet ? new ObjectIterable<Float2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Float2ByteMap.Entry> iterator() { return ((Float2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2ByteMap.Entry> action) { ((Float2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2ByteMap map, Consumer<Float2ByteMap.Entry> action) {
		ObjectSet<Float2ByteMap.Entry> entries = map.float2ByteEntrySet();
		if(entries instanceof Float2ByteMap.FastEntrySet) ((Float2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteMap synchronize(Float2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteMap synchronize(Float2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteSortedMap synchronize(Float2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteSortedMap synchronize(Float2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteOrderedMap synchronize(Float2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteOrderedMap synchronize(Float2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteNavigableMap synchronize(Float2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ByteNavigableMap synchronize(Float2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2ByteMap unmodifiable(Float2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ByteOrderedMap unmodifiable(Float2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ByteSortedMap unmodifiable(Float2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ByteNavigableMap unmodifiable(Float2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2ByteMap.Entry unmodifiable(Float2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2ByteMap.Entry unmodifiable(Map.Entry<Float, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2ByteMap singleton(float key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2ByteMap {
		final float key;
		final byte value;
		FloatSet keySet;
		ByteCollection values;
		ObjectSet<Float2ByteMap.Entry> entrySet;
		
		SingletonMap(float key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(float key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(float key, byte defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public byte computeByte(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(float key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Float2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2ByteMap.Entry> float2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2ByteMap {
		@Override
		public byte put(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(float key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(float key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(float key, byte defaultValue) { return defaultValue; }
		@Override
		public byte computeByte(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(float key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Float2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Float2ByteMap.Entry> float2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2ByteMap.Entry entry) {
			super(entry.getFloatKey(), entry.getByteValue());
		}
		
		@Override
		public void set(float key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2ByteNavigableMap {
		Float2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Float2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2ByteNavigableMap descendingMap() { return Float2ByteMaps.unmodifiable(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2ByteMap.Entry firstEntry() { return Float2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2ByteMap.Entry lastEntry() { return Float2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ByteNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2ByteNavigableMap headMap(float toKey, boolean inclusive) { return Float2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2ByteNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2ByteNavigableMap subMap(float fromKey, float toKey) { return Float2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ByteNavigableMap headMap(float toKey) { return Float2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ByteNavigableMap tailMap(float fromKey) { return Float2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2ByteMap.Entry lowerEntry(float key) { return Float2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2ByteMap.Entry higherEntry(float key) { return Float2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2ByteMap.Entry floorEntry(float key) { return Float2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2ByteMap.Entry ceilingEntry(float key) { return Float2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2ByteNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2ByteOrderedMap {
		Float2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Float2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Float2ByteOrderedMap copy() { return map.copy(); }
		@Override
		public FloatOrderedSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return (FloatOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Float2ByteMap.Entry> float2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.float2ByteEntrySet());
			return (ObjectOrderedSet<Float2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2ByteSortedMap {
		Float2ByteSortedMap map;
		
		UnmodifyableSortedMap(Float2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2ByteSortedMap subMap(float fromKey, float toKey) { return Float2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ByteSortedMap headMap(float toKey) { return Float2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ByteSortedMap tailMap(float fromKey) { return Float2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Float2ByteSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2ByteMap implements Float2ByteMap {
		Float2ByteMap map;
		ByteCollection values;
		FloatSet keys;
		ObjectSet<Float2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Float2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(float key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(float key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(float key) {
			byte type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public byte getOrDefault(float key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public byte computeByte(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(float key, Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(float key, FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(float key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(float key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Float2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(FloatByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(Float2ByteMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Float2ByteMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2ByteMap.Entry> float2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Float2ByteMap.Entry>
	{
		ObjectOrderedSet<Float2ByteMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Float2ByteMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Float2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Float2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Float2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Float2ByteMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Float2ByteMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Float2ByteMap.Entry> iterator(Float2ByteMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Float2ByteMap.Entry first() { return set.first(); }
		@Override
		public Float2ByteMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ByteMap.Entry last() { return set.last(); }
		@Override
		public Float2ByteMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2ByteMap.Entry>
	{
		ObjectSet<Float2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Float2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2ByteMap.Entry> iterator() {
			return new ObjectIterator<Float2ByteMap.Entry>() {
				ObjectIterator<Float2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2ByteMap.Entry next() { return Float2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2ByteNavigableMap {
		Float2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Float2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2ByteNavigableMap descendingMap() { synchronized(mutex) { return Float2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public FloatNavigableSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Float2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Float2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2ByteNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2ByteNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2ByteNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2ByteNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ByteNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ByteNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2ByteMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2ByteMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2ByteMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2ByteMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2ByteNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ByteNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2ByteMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2ByteMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2ByteMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2ByteMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2ByteOrderedMap {
		Float2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Float2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(float key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(float key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Float2ByteOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatOrderedSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return (FloatOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Float2ByteMap.Entry> float2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2ByteEntrySet(), mutex);
			return (ObjectOrderedSet<Float2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2ByteSortedMap {
		Float2ByteSortedMap map;
		
		SynchronizedSortedMap(Float2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2ByteSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ByteSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ByteSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Float2ByteSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2ByteSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ByteSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ByteSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2ByteMap implements Float2ByteMap {
		Float2ByteMap map;
		ByteCollection values;
		FloatSet keys;
		ObjectSet<Float2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(float key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(float key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(float key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(float key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(float key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(float key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Float2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(FloatByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(float key, FloatByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(float key, Float2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(float key, FloatByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte supplyByteIfAbsent(float key, ByteSupplier valueProvider) { synchronized(mutex) { return map.supplyByteIfAbsent(key, valueProvider); } }
		@Override
		public byte computeByteNonDefault(float key, FloatByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteNonDefault(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsentNonDefault(float key, Float2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresentNonDefault(float key, FloatByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public byte supplyByteIfAbsentNonDefault(float key, ByteSupplier valueProvider) { synchronized(mutex) { return map.supplyByteIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public byte mergeByte(float key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Float2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(float key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Float2ByteMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2ByteMap.Entry> float2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2ByteEntrySet(), mutex);
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
		public Byte put(Float key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Float key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Float key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Float key, BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Float key, Function<? super Float, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Float key, BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Float key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}