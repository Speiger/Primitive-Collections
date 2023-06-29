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
import speiger.src.collections.floats.functions.consumer.FloatCharConsumer;
import speiger.src.collections.floats.functions.function.Float2CharFunction;
import speiger.src.collections.floats.functions.function.FloatCharUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2CharMap.Entry> fastIterator(Float2CharMap map) {
		ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
		return entries instanceof Float2CharMap.FastEntrySet ? ((Float2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2CharMap.Entry> fastIterable(Float2CharMap map) {
		ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
		return map instanceof Float2CharMap.FastEntrySet ? new ObjectIterable<Float2CharMap.Entry>(){
			@Override
			public ObjectIterator<Float2CharMap.Entry> iterator() { return ((Float2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2CharMap.Entry> action) { ((Float2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2CharMap map, Consumer<Float2CharMap.Entry> action) {
		ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
		if(entries instanceof Float2CharMap.FastEntrySet) ((Float2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharMap synchronize(Float2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharMap synchronize(Float2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharSortedMap synchronize(Float2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharSortedMap synchronize(Float2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharOrderedMap synchronize(Float2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharOrderedMap synchronize(Float2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharNavigableMap synchronize(Float2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2CharNavigableMap synchronize(Float2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2CharMap unmodifiable(Float2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2CharOrderedMap unmodifiable(Float2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2CharSortedMap unmodifiable(Float2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2CharNavigableMap unmodifiable(Float2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2CharMap.Entry unmodifiable(Float2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2CharMap.Entry unmodifiable(Map.Entry<Float, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2CharMap singleton(float key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2CharMap {
		final float key;
		final char value;
		FloatSet keySet;
		CharCollection values;
		ObjectSet<Float2CharMap.Entry> entrySet;
		
		SingletonMap(float key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(float key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(float key, char defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public char computeChar(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(float key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Float2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2CharMap.Entry> float2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2CharMap {
		@Override
		public char put(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(float key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(float key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(float key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(float key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Float2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Float2CharMap.Entry> float2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2CharMap.Entry entry) {
			super(entry.getFloatKey(), entry.getCharValue());
		}
		
		@Override
		public void set(float key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2CharNavigableMap {
		Float2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Float2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2CharNavigableMap descendingMap() { return Float2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet keySet() { return FloatSets.unmodifiable(map.keySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2CharMap.Entry firstEntry() { return Float2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2CharMap.Entry lastEntry() { return Float2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2CharNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2CharNavigableMap headMap(float toKey, boolean inclusive) { return Float2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2CharNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2CharNavigableMap subMap(float fromKey, float toKey) { return Float2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2CharNavigableMap headMap(float toKey) { return Float2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2CharNavigableMap tailMap(float fromKey) { return Float2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2CharMap.Entry lowerEntry(float key) { return Float2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2CharMap.Entry higherEntry(float key) { return Float2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2CharMap.Entry floorEntry(float key) { return Float2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2CharMap.Entry ceilingEntry(float key) { return Float2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2CharNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2CharOrderedMap {
		Float2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Float2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Float2CharOrderedMap copy() { return map.copy(); }
		@Override
		public FloatOrderedSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return (FloatOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Float2CharMap.Entry> float2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.float2CharEntrySet());
			return (ObjectOrderedSet<Float2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2CharSortedMap {
		Float2CharSortedMap map;
		
		UnmodifyableSortedMap(Float2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2CharSortedMap subMap(float fromKey, float toKey) { return Float2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2CharSortedMap headMap(float toKey) { return Float2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2CharSortedMap tailMap(float fromKey) { return Float2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Float2CharSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2CharMap implements Float2CharMap {
		Float2CharMap map;
		CharCollection values;
		FloatSet keys;
		ObjectSet<Float2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Float2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(float key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(float key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(float key) {
			char type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(float key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(float key, Float2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(float key, FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(float key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(float key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Float2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(FloatCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Float2CharMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Float2CharMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2CharMap.Entry> float2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Float2CharMap.Entry>
	{
		ObjectOrderedSet<Float2CharMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Float2CharMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Float2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Float2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Float2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Float2CharMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Float2CharMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Float2CharMap.Entry> iterator(Float2CharMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Float2CharMap.Entry first() { return set.first(); }
		@Override
		public Float2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Float2CharMap.Entry last() { return set.last(); }
		@Override
		public Float2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2CharMap.Entry>
	{
		ObjectSet<Float2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Float2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2CharMap.Entry> iterator() {
			return new ObjectIterator<Float2CharMap.Entry>() {
				ObjectIterator<Float2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2CharMap.Entry next() { return Float2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2CharNavigableMap {
		Float2CharNavigableMap map;
		
		SynchronizedNavigableMap(Float2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2CharNavigableMap descendingMap() { synchronized(mutex) { return Float2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public FloatNavigableSet keySet() { synchronized(mutex) { return FloatSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Float2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2CharMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Float2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2CharNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2CharNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2CharNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2CharNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2CharNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2CharNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2CharMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2CharMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2CharMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2CharMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2CharNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2CharNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2CharMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2CharMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2CharMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2CharMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2CharOrderedMap {
		Float2CharOrderedMap map;
		
		SynchronizedOrderedMap(Float2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(float key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(float key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Float2CharOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatOrderedSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return (FloatOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Float2CharMap.Entry> float2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Float2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2CharSortedMap {
		Float2CharSortedMap map;
		
		SynchronizedSortedMap(Float2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2CharSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2CharSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2CharSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Float2CharSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2CharSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2CharSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2CharSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2CharMap implements Float2CharMap {
		Float2CharMap map;
		CharCollection values;
		FloatSet keys;
		ObjectSet<Float2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(float key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(float key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(float key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(float key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(float key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(float key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Float2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(FloatCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(float key, FloatCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(float key, Float2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(float key, FloatCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(float key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char computeCharNonDefault(float key, FloatCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(float key, Float2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(float key, FloatCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsentNonDefault(float key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(float key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Float2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(float key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Float2CharMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2CharMap.Entry> float2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2CharEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Character get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Character getOrDefault(Object key, Character defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Character put(Float key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Float key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Float key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Float key, BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Float key, Function<? super Float, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Float key, BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Float key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}