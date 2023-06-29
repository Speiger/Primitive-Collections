package speiger.src.collections.bytes.utils.maps;

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
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.consumer.ByteBooleanConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.functions.function.ByteBooleanUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.utils.BooleanCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Byte2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2BooleanMap.Entry> fastIterator(Byte2BooleanMap map) {
		ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
		return entries instanceof Byte2BooleanMap.FastEntrySet ? ((Byte2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2BooleanMap.Entry> fastIterable(Byte2BooleanMap map) {
		ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
		return map instanceof Byte2BooleanMap.FastEntrySet ? new ObjectIterable<Byte2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Byte2BooleanMap.Entry> iterator() { return ((Byte2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2BooleanMap.Entry> action) { ((Byte2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2BooleanMap map, Consumer<Byte2BooleanMap.Entry> action) {
		ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
		if(entries instanceof Byte2BooleanMap.FastEntrySet) ((Byte2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanMap synchronize(Byte2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanMap synchronize(Byte2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanSortedMap synchronize(Byte2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanSortedMap synchronize(Byte2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanOrderedMap synchronize(Byte2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanOrderedMap synchronize(Byte2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanNavigableMap synchronize(Byte2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2BooleanNavigableMap synchronize(Byte2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2BooleanMap unmodifiable(Byte2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2BooleanOrderedMap unmodifiable(Byte2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2BooleanSortedMap unmodifiable(Byte2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2BooleanNavigableMap unmodifiable(Byte2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2BooleanMap.Entry unmodifiable(Byte2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2BooleanMap.Entry unmodifiable(Map.Entry<Byte, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2BooleanMap singleton(byte key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2BooleanMap {
		final byte key;
		final boolean value;
		ByteSet keySet;
		BooleanCollection values;
		ObjectSet<Byte2BooleanMap.Entry> entrySet;
		
		SingletonMap(byte key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(byte key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(byte key, boolean defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public boolean computeBoolean(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(byte key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Byte2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2BooleanMap {
		@Override
		public boolean put(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(byte key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(byte key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(byte key, boolean defaultValue) { return defaultValue; }
		@Override
		public boolean computeBoolean(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(byte key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Byte2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2BooleanMap.Entry entry) {
			super(entry.getByteKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(byte key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2BooleanNavigableMap {
		Byte2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2BooleanNavigableMap descendingMap() { return Byte2BooleanMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet keySet() { return ByteSets.unmodifiable(map.keySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2BooleanMap.Entry firstEntry() { return Byte2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2BooleanMap.Entry lastEntry() { return Byte2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2BooleanNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2BooleanNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2BooleanNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2BooleanNavigableMap subMap(byte fromKey, byte toKey) { return Byte2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2BooleanNavigableMap headMap(byte toKey) { return Byte2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2BooleanNavigableMap tailMap(byte fromKey) { return Byte2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public byte lowerKey(byte key) { return map.lowerKey(key); }
		@Override
		public byte higherKey(byte key) { return map.higherKey(key); }
		@Override
		public byte floorKey(byte key) { return map.floorKey(key); }
		@Override
		public byte ceilingKey(byte key) { return map.ceilingKey(key); }
		@Override
		public Byte2BooleanMap.Entry lowerEntry(byte key) { return Byte2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2BooleanMap.Entry higherEntry(byte key) { return Byte2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2BooleanMap.Entry floorEntry(byte key) { return Byte2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2BooleanMap.Entry ceilingEntry(byte key) { return Byte2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2BooleanNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2BooleanOrderedMap {
		Byte2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Byte2BooleanOrderedMap copy() { return map.copy(); }
		@Override
		public ByteOrderedSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return (ByteOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.byte2BooleanEntrySet());
			return (ObjectOrderedSet<Byte2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2BooleanSortedMap {
		Byte2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Byte2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2BooleanSortedMap subMap(byte fromKey, byte toKey) { return Byte2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2BooleanSortedMap headMap(byte toKey) { return Byte2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2BooleanSortedMap tailMap(byte fromKey) { return Byte2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public ByteSortedSet keySet() { return ByteSets.unmodifiable(map.keySet()); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Byte2BooleanSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2BooleanMap implements Byte2BooleanMap {
		Byte2BooleanMap map;
		BooleanCollection values;
		ByteSet keys;
		ObjectSet<Byte2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(byte key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(byte key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(byte key) {
			boolean type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public boolean getOrDefault(byte key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean computeBoolean(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(byte key, BytePredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(byte key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(byte key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Byte2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(ByteBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(Byte2BooleanMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Byte2BooleanMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Byte2BooleanMap.Entry>
	{
		ObjectOrderedSet<Byte2BooleanMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Byte2BooleanMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Byte2BooleanMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> iterator(Byte2BooleanMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Byte2BooleanMap.Entry first() { return set.first(); }
		@Override
		public Byte2BooleanMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2BooleanMap.Entry last() { return set.last(); }
		@Override
		public Byte2BooleanMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2BooleanMap.Entry>
	{
		ObjectSet<Byte2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Byte2BooleanMap.Entry>() {
				ObjectIterator<Byte2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2BooleanMap.Entry next() { return Byte2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2BooleanNavigableMap {
		Byte2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Byte2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ByteNavigableSet keySet() { synchronized(mutex) { return ByteSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Byte2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Byte2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2BooleanNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2BooleanNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2BooleanNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2BooleanNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2BooleanNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2BooleanNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2BooleanMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2BooleanMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2BooleanMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2BooleanMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2BooleanNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(byte e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public byte getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(byte e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public byte getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Byte lowerKey(Byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Byte floorKey(Byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Byte ceilingKey(Byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Byte higherKey(Byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Byte2BooleanMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2BooleanMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2BooleanMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2BooleanMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2BooleanOrderedMap {
		Byte2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Byte2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(byte key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(byte key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Byte2BooleanOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ByteOrderedSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return (ByteOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2BooleanEntrySet(), mutex);
			return (ObjectOrderedSet<Byte2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2BooleanSortedMap {
		Byte2BooleanSortedMap map;
		
		SynchronizedSortedMap(Byte2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2BooleanSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2BooleanSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2BooleanSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public ByteSortedSet keySet() { synchronized(mutex) { return ByteSets.synchronize(map.keySet(), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Byte2BooleanSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2BooleanSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2BooleanSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2BooleanMap implements Byte2BooleanMap {
		Byte2BooleanMap map;
		BooleanCollection values;
		ByteSet keys;
		ObjectSet<Byte2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(byte key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(byte key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Byte2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(byte key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(byte key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Byte2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(ByteBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(byte key, ByteBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(byte key, BytePredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(byte key, ByteBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsent(byte key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsent(key, valueProvider); } }
		@Override
		public boolean computeBooleanNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(byte key, BytePredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresentNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(byte key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public boolean mergeBoolean(byte key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Byte2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(byte key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Byte2BooleanMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2BooleanEntrySet(), mutex);
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
		public Boolean put(Byte key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Byte key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Byte key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Byte key, BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Byte key, Function<? super Byte, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Byte key, BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Byte key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}