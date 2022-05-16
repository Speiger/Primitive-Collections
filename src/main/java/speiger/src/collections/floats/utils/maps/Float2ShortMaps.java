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
import speiger.src.collections.floats.functions.consumer.FloatShortConsumer;
import speiger.src.collections.floats.functions.function.Float2ShortFunction;
import speiger.src.collections.floats.functions.function.FloatShortUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortCollections;
import speiger.src.collections.shorts.utils.ShortSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Float2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2ShortMap.Entry> fastIterator(Float2ShortMap map) {
		ObjectSet<Float2ShortMap.Entry> entries = map.float2ShortEntrySet();
		return entries instanceof Float2ShortMap.FastEntrySet ? ((Float2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2ShortMap.Entry> fastIterable(Float2ShortMap map) {
		ObjectSet<Float2ShortMap.Entry> entries = map.float2ShortEntrySet();
		return map instanceof Float2ShortMap.FastEntrySet ? new ObjectIterable<Float2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Float2ShortMap.Entry> iterator() { return ((Float2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2ShortMap.Entry> action) { ((Float2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2ShortMap map, Consumer<Float2ShortMap.Entry> action) {
		ObjectSet<Float2ShortMap.Entry> entries = map.float2ShortEntrySet();
		if(entries instanceof Float2ShortMap.FastEntrySet) ((Float2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortMap synchronize(Float2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortMap synchronize(Float2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortSortedMap synchronize(Float2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortSortedMap synchronize(Float2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortOrderedMap synchronize(Float2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortOrderedMap synchronize(Float2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortNavigableMap synchronize(Float2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2ShortNavigableMap synchronize(Float2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2ShortMap unmodifiable(Float2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ShortOrderedMap unmodifiable(Float2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ShortSortedMap unmodifiable(Float2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2ShortNavigableMap unmodifiable(Float2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2ShortMap.Entry unmodifiable(Float2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2ShortMap.Entry unmodifiable(Map.Entry<Float, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2ShortMap singleton(float key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2ShortMap {
		final float key;
		final short value;
		FloatSet keySet;
		ShortCollection values;
		ObjectSet<Float2ShortMap.Entry> entrySet;
		
		SingletonMap(float key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(float key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(float key, short defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2ShortMap.Entry> float2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2ShortMap {
		@Override
		public short put(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(float key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(float key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(float key, short defaultValue) { return (short)0; }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Float2ShortMap.Entry> float2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2ShortMap.Entry entry) {
			super(entry.getFloatKey(), entry.getShortValue());
		}
		
		@Override
		public void set(float key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2ShortNavigableMap {
		Float2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Float2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2ShortNavigableMap descendingMap() { return Float2ShortMaps.synchronize(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2ShortMap.Entry firstEntry() { return Float2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2ShortMap.Entry lastEntry() { return Float2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2ShortNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2ShortNavigableMap headMap(float toKey, boolean inclusive) { return Float2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2ShortNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2ShortNavigableMap subMap(float fromKey, float toKey) { return Float2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ShortNavigableMap headMap(float toKey) { return Float2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ShortNavigableMap tailMap(float fromKey) { return Float2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2ShortMap.Entry lowerEntry(float key) { return Float2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2ShortMap.Entry higherEntry(float key) { return Float2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2ShortMap.Entry floorEntry(float key) { return Float2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2ShortMap.Entry ceilingEntry(float key) { return Float2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2ShortOrderedMap {
		Float2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Float2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Float2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2ShortSortedMap {
		Float2ShortSortedMap map;
		
		UnmodifyableSortedMap(Float2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2ShortSortedMap subMap(float fromKey, float toKey) { return Float2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2ShortSortedMap headMap(float toKey) { return Float2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2ShortSortedMap tailMap(float fromKey) { return Float2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Float2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2ShortMap implements Float2ShortMap {
		Float2ShortMap map;
		ShortCollection values;
		FloatSet keys;
		ObjectSet<Float2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Float2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(float key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(float key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(float key) { return map.get(key); }
		@Override
		public short getOrDefault(float key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Float2ShortMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2ShortMap.Entry> float2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2ShortMap.Entry>
	{
		ObjectSet<Float2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Float2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2ShortMap.Entry> iterator() {
			return new ObjectIterator<Float2ShortMap.Entry>() {
				ObjectIterator<Float2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2ShortMap.Entry next() { return Float2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2ShortNavigableMap {
		Float2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Float2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2ShortNavigableMap descendingMap() { synchronized(mutex) { return Float2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Float2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2ShortNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2ShortNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2ShortNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2ShortNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ShortNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ShortNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2ShortMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2ShortMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2ShortMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2ShortMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float2ShortNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ShortNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ShortNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2ShortNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ShortNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ShortNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2ShortMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2ShortMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2ShortMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2ShortMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2ShortOrderedMap {
		Float2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Float2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(float key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(float key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Float2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2ShortSortedMap {
		Float2ShortSortedMap map;
		
		SynchronizedSortedMap(Float2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2ShortSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2ShortSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2ShortSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Float2ShortSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2ShortSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ShortSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2ShortSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2ShortMap implements Float2ShortMap {
		Float2ShortMap map;
		ShortCollection values;
		FloatSet keys;
		ObjectSet<Float2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(float key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(float key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(float key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(float key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(float key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(float key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Float2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(FloatShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(float key, FloatShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(float key, Float2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(float key, FloatShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short mergeShort(float key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Float2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(float key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Float2ShortMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2ShortMap.Entry> float2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2ShortEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Short get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Short getOrDefault(Object key, Short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Short put(Float key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Float key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Float key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Float key, Function<? super Float, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Float key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}