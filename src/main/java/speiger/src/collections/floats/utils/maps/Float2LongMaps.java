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
import speiger.src.collections.floats.functions.consumer.FloatLongConsumer;
import speiger.src.collections.floats.functions.function.Float2LongFunction;
import speiger.src.collections.floats.functions.function.FloatLongUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongSortedMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongOrderedMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.utils.LongCollections;
import speiger.src.collections.longs.utils.LongSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Float2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Float2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Float2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Float2LongMap.Entry> fastIterator(Float2LongMap map) {
		ObjectSet<Float2LongMap.Entry> entries = map.float2LongEntrySet();
		return entries instanceof Float2LongMap.FastEntrySet ? ((Float2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Float2LongMap.Entry> fastIterable(Float2LongMap map) {
		ObjectSet<Float2LongMap.Entry> entries = map.float2LongEntrySet();
		return map instanceof Float2LongMap.FastEntrySet ? new ObjectIterable<Float2LongMap.Entry>(){
			@Override
			public ObjectIterator<Float2LongMap.Entry> iterator() { return ((Float2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Float2LongMap.Entry> action) { ((Float2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Float2LongMap map, Consumer<Float2LongMap.Entry> action) {
		ObjectSet<Float2LongMap.Entry> entries = map.float2LongEntrySet();
		if(entries instanceof Float2LongMap.FastEntrySet) ((Float2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongMap synchronize(Float2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongMap synchronize(Float2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongSortedMap synchronize(Float2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongSortedMap synchronize(Float2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongOrderedMap synchronize(Float2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongOrderedMap synchronize(Float2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongNavigableMap synchronize(Float2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Float2LongNavigableMap synchronize(Float2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Float2LongMap unmodifiable(Float2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2LongOrderedMap unmodifiable(Float2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Float2LongSortedMap unmodifiable(Float2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Float2LongNavigableMap unmodifiable(Float2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2LongMap.Entry unmodifiable(Float2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Float2LongMap.Entry unmodifiable(Map.Entry<Float, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Float2LongMap singleton(float key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractFloat2LongMap {
		final float key;
		final long value;
		FloatSet keySet;
		LongCollection values;
		ObjectSet<Float2LongMap.Entry> entrySet;
		
		SingletonMap(float key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(float key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(float key) { return Objects.equals(this.key, Float.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(float key, long defaultValue) { return Objects.equals(this.key, Float.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public FloatSet keySet() { 
			if(keySet == null) keySet = FloatSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Float2LongMap.Entry> float2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractFloat2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractFloat2LongMap {
		@Override
		public long put(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(float key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(float key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(float key, long defaultValue) { return 0L; }
		@Override
		public FloatSet keySet() { return FloatSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Float2LongMap.Entry> float2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractFloat2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Float, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Float2LongMap.Entry entry) {
			super(entry.getFloatKey(), entry.getLongValue());
		}
		
		@Override
		public void set(float key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Float2LongNavigableMap {
		Float2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Float2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Float2LongNavigableMap descendingMap() { return Float2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public FloatNavigableSet navigableKeySet() { return FloatSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public FloatNavigableSet descendingKeySet() { return FloatSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Float2LongMap.Entry firstEntry() { return Float2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Float2LongMap.Entry lastEntry() { return Float2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Float2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Float2LongNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { return Float2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Float2LongNavigableMap headMap(float toKey, boolean inclusive) { return Float2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Float2LongNavigableMap tailMap(float fromKey, boolean inclusive) { return Float2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Float2LongNavigableMap subMap(float fromKey, float toKey) { return Float2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2LongNavigableMap headMap(float toKey) { return Float2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2LongNavigableMap tailMap(float fromKey) { return Float2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Float2LongMap.Entry lowerEntry(float key) { return Float2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Float2LongMap.Entry higherEntry(float key) { return Float2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Float2LongMap.Entry floorEntry(float key) { return Float2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Float2LongMap.Entry ceilingEntry(float key) { return Float2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Float2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Float2LongOrderedMap {
		Float2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Float2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(float key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(float key) { throw new UnsupportedOperationException(); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Float2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Float2LongSortedMap {
		Float2LongSortedMap map;
		
		UnmodifyableSortedMap(Float2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public Float2LongSortedMap subMap(float fromKey, float toKey) { return Float2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Float2LongSortedMap headMap(float toKey) { return Float2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Float2LongSortedMap tailMap(float fromKey) { return Float2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public float firstFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float lastFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Float2LongSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractFloat2LongMap implements Float2LongMap {
		Float2LongMap map;
		LongCollection values;
		FloatSet keys;
		ObjectSet<Float2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Float2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(float key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(float key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(float key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(float key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(float key) { return map.get(key); }
		@Override
		public long getOrDefault(float key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Float2LongMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatSet keySet() { 
			if(keys == null) keys = FloatSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Float2LongMap.Entry> float2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.float2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Float2LongMap.Entry>
	{
		ObjectSet<Float2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Float2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Float2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Float2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Float2LongMap.Entry> iterator() {
			return new ObjectIterator<Float2LongMap.Entry>() {
				ObjectIterator<Float2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Float2LongMap.Entry next() { return Float2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Float2LongNavigableMap {
		Float2LongNavigableMap map;
		
		SynchronizedNavigableMap(Float2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Float2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Float2LongNavigableMap descendingMap() { synchronized(mutex) { return Float2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public FloatNavigableSet navigableKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public FloatNavigableSet descendingKeySet() { synchronized(mutex) { return FloatSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Float2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2LongMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Float2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Float2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Float2LongNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Float2LongNavigableMap headMap(float toKey, boolean inclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Float2LongNavigableMap tailMap(float fromKey, boolean inclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Float2LongNavigableMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2LongNavigableMap headMap(float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2LongNavigableMap tailMap(float fromKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float lowerKey(float key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public float higherKey(float key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public float floorKey(float key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public float ceilingKey(float key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Float2LongMap.Entry lowerEntry(float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Float2LongMap.Entry higherEntry(float key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Float2LongMap.Entry floorEntry(float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Float2LongMap.Entry ceilingEntry(float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Float2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float2LongNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Float2LongNavigableMap headMap(Float toKey, boolean inclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2LongNavigableMap tailMap(Float fromKey, boolean inclusive) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Float2LongNavigableMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2LongNavigableMap headMap(Float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2LongNavigableMap tailMap(Float fromKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Float2LongMap.Entry lowerEntry(Float key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Float2LongMap.Entry floorEntry(Float key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Float2LongMap.Entry ceilingEntry(Float key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Float2LongMap.Entry higherEntry(Float key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Float2LongOrderedMap {
		Float2LongOrderedMap map;
		
		SynchronizedOrderedMap(Float2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Float2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(float key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(float key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(float key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(float key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(float key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(float key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Float2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Float2LongSortedMap {
		Float2LongSortedMap map;
		
		SynchronizedSortedMap(Float2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Float2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Float2LongSortedMap subMap(float fromKey, float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Float2LongSortedMap headMap(float toKey)  { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Float2LongSortedMap tailMap(float fromKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public float firstFloatKey() { synchronized(mutex) { return map.firstFloatKey(); } }
		@Override
		public float pollFirstFloatKey() { synchronized(mutex) { return map.pollFirstFloatKey(); } }
		@Override
		public float lastFloatKey() { synchronized(mutex) { return map.lastFloatKey(); } }
		@Override
		public float pollLastFloatKey() { synchronized(mutex) { return map.pollLastFloatKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Float2LongSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Float firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Float lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Float2LongSortedMap subMap(Float fromKey, Float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Float2LongSortedMap headMap(Float toKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Float2LongSortedMap tailMap(Float fromKey) { synchronized(mutex) { return Float2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractFloat2LongMap implements Float2LongMap {
		Float2LongMap map;
		LongCollection values;
		FloatSet keys;
		ObjectSet<Float2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Float2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Float2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractFloat2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(float key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(float key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Float2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(float key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(float key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Float2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Float2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Float, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(float[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(float key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(float key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(float key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(float key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(float key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(float key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(float key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Float2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(FloatLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(float key, FloatLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(float key, Float2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(float key, FloatLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(float key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Float2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(float key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(FloatLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Float2LongMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public FloatSet keySet() {
			if(keys == null) keys = FloatSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Float2LongMap.Entry> float2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.float2LongEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Long get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Long getOrDefault(Object key, Long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Long put(Float key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Float key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Float key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Float key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Float key, BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Float key, Function<? super Float, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Float key, BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Float key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Float, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}