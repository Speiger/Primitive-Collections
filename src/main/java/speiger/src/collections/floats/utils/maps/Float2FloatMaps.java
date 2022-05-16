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
import speiger.src.collections.floats.functions.consumer.FloatFloatConsumer;
import speiger.src.collections.floats.functions.function.Float2FloatFunction;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.utils.FloatCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Float2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2FloatMap.Entry> fastIterator(Float2FloatMap map) {
		ObjectSet<Float2FloatMap.Entry> entries = map.float2FloatEntrySet();
		return entries instanceof Float2FloatMap.FastEntrySet ? ((Float2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2FloatMap.Entry> fastIterable(Float2FloatMap map) {
		ObjectSet<Float2FloatMap.Entry> entries = map.float2FloatEntrySet();
		return map instanceof Float2FloatMap.FastEntrySet ? new ObjectIterable<Float2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Float2FloatMap.Entry> iterator() { return ((Float2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2FloatMap.Entry> action) { ((Float2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2FloatMap map, Consumer<Float2FloatMap.Entry> action) {
		ObjectSet<Float2FloatMap.Entry> entries = map.float2FloatEntrySet();
		if(entries instanceof Float2FloatMap.FastEntrySet) ((Float2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatMap synchronize(Float2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatMap synchronize(Float2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatSortedMap synchronize(Float2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatSortedMap synchronize(Float2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatOrderedMap synchronize(Float2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatOrderedMap synchronize(Float2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatNavigableMap synchronize(Float2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2FloatNavigableMap synchronize(Float2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2FloatMap unmodifiable(Float2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2FloatOrderedMap unmodifiable(Float2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2FloatSortedMap unmodifiable(Float2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2FloatNavigableMap unmodifiable(Float2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2FloatMap.Entry unmodifiable(Float2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2FloatMap.Entry unmodifiable(Map.Entry<Float, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2FloatMap singleton(float key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2FloatMap {
		final float key;
		final float value;
		FloatSet keySet;
		FloatCollection values;
		ObjectSet<Float2FloatMap.Entry> entrySet;
		
		SingletonMap(float key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(float key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(float key, float defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2FloatMap.Entry> float2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2FloatMap {
		@Override
		public float put(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(float key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(float key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(float key, float defaultValue) { return 0F; }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Float2FloatMap.Entry> float2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2FloatMap.Entry entry) {
			super(entry.getFloatKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(float key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2FloatNavigableMap {
		Float2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Float2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2FloatNavigableMap descendingMap() { return Float2FloatMaps.synchronize(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2FloatMap.Entry firstEntry() { return Float2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2FloatMap.Entry lastEntry() { return Float2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2FloatNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2FloatNavigableMap headMap(float toKey, boolean inclusive) { return Float2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2FloatNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2FloatNavigableMap subMap(float fromKey, float toKey) { return Float2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2FloatNavigableMap headMap(float toKey) { return Float2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2FloatNavigableMap tailMap(float fromKey) { return Float2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2FloatMap.Entry lowerEntry(float key) { return Float2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2FloatMap.Entry higherEntry(float key) { return Float2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2FloatMap.Entry floorEntry(float key) { return Float2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2FloatMap.Entry ceilingEntry(float key) { return Float2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2FloatOrderedMap {
		Float2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Float2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Float2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2FloatSortedMap {
		Float2FloatSortedMap map;
		
		UnmodifyableSortedMap(Float2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2FloatSortedMap subMap(float fromKey, float toKey) { return Float2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2FloatSortedMap headMap(float toKey) { return Float2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2FloatSortedMap tailMap(float fromKey) { return Float2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Float2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2FloatMap implements Float2FloatMap {
		Float2FloatMap map;
		FloatCollection values;
		FloatSet keys;
		ObjectSet<Float2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Float2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(float key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(float key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(float key) { return map.get(key); }
		@Override
		public float getOrDefault(float key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Float2FloatMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2FloatMap.Entry> float2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2FloatMap.Entry>
	{
		ObjectSet<Float2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Float2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2FloatMap.Entry> iterator() {
			return new ObjectIterator<Float2FloatMap.Entry>() {
				ObjectIterator<Float2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2FloatMap.Entry next() { return Float2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2FloatNavigableMap {
		Float2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Float2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2FloatNavigableMap descendingMap() { synchronized(mutex) { return Float2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Float2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2FloatNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2FloatNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2FloatNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2FloatNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2FloatNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2FloatNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2FloatMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2FloatMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2FloatMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2FloatMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float2FloatNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2FloatNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2FloatNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2FloatNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2FloatNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2FloatNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2FloatMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2FloatMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2FloatMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2FloatMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2FloatOrderedMap {
		Float2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Float2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(float key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(float key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Float2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2FloatSortedMap {
		Float2FloatSortedMap map;
		
		SynchronizedSortedMap(Float2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2FloatSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2FloatSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2FloatSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Float2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2FloatSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2FloatSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2FloatSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2FloatMap implements Float2FloatMap {
		Float2FloatMap map;
		FloatCollection values;
		FloatSet keys;
		ObjectSet<Float2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(float key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(float key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(float key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(float key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(float key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(float key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Float2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(float key, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(float key, Float2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(float key, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float mergeFloat(float key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Float2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(float key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Float2FloatMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2FloatMap.Entry> float2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2FloatEntrySet(), mutex);
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
		public Float put(Float key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Float key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Float key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Float key, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Float key, Function<? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Float key, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Float key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}