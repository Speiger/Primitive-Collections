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
import speiger.src.collections.bytes.functions.consumer.ByteShortConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ShortFunction;
import speiger.src.collections.bytes.functions.function.ByteShortUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.utils.ShortCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Byte2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2ShortMap.Entry> fastIterator(Byte2ShortMap map) {
		ObjectSet<Byte2ShortMap.Entry> entries = map.byte2ShortEntrySet();
		return entries instanceof Byte2ShortMap.FastEntrySet ? ((Byte2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2ShortMap.Entry> fastIterable(Byte2ShortMap map) {
		ObjectSet<Byte2ShortMap.Entry> entries = map.byte2ShortEntrySet();
		return map instanceof Byte2ShortMap.FastEntrySet ? new ObjectIterable<Byte2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Byte2ShortMap.Entry> iterator() { return ((Byte2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2ShortMap.Entry> action) { ((Byte2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2ShortMap map, Consumer<Byte2ShortMap.Entry> action) {
		ObjectSet<Byte2ShortMap.Entry> entries = map.byte2ShortEntrySet();
		if(entries instanceof Byte2ShortMap.FastEntrySet) ((Byte2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortMap synchronize(Byte2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortMap synchronize(Byte2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortSortedMap synchronize(Byte2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortSortedMap synchronize(Byte2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortOrderedMap synchronize(Byte2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortOrderedMap synchronize(Byte2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortNavigableMap synchronize(Byte2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ShortNavigableMap synchronize(Byte2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2ShortMap unmodifiable(Byte2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ShortOrderedMap unmodifiable(Byte2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ShortSortedMap unmodifiable(Byte2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ShortNavigableMap unmodifiable(Byte2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2ShortMap.Entry unmodifiable(Byte2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2ShortMap.Entry unmodifiable(Map.Entry<Byte, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2ShortMap singleton(byte key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2ShortMap {
		final byte key;
		final short value;
		ByteSet keySet;
		ShortCollection values;
		ObjectSet<Byte2ShortMap.Entry> entrySet;
		
		SingletonMap(byte key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(byte key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(byte key, short defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2ShortMap {
		@Override
		public short put(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(byte key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(byte key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(byte key, short defaultValue) { return defaultValue; }
		@Override
		public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2ShortMap.Entry entry) {
			super(entry.getByteKey(), entry.getShortValue());
		}
		
		@Override
		public void set(byte key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2ShortNavigableMap {
		Byte2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2ShortNavigableMap descendingMap() { return Byte2ShortMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet keySet() { return ByteSets.unmodifiable(map.keySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2ShortMap.Entry firstEntry() { return Byte2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2ShortMap.Entry lastEntry() { return Byte2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ShortNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2ShortNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2ShortNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2ShortNavigableMap subMap(byte fromKey, byte toKey) { return Byte2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ShortNavigableMap headMap(byte toKey) { return Byte2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ShortNavigableMap tailMap(byte fromKey) { return Byte2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2ShortMap.Entry lowerEntry(byte key) { return Byte2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2ShortMap.Entry higherEntry(byte key) { return Byte2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2ShortMap.Entry floorEntry(byte key) { return Byte2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2ShortMap.Entry ceilingEntry(byte key) { return Byte2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2ShortNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2ShortOrderedMap {
		Byte2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Byte2ShortOrderedMap copy() { return map.copy(); }
		@Override
		public ByteOrderedSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return (ByteOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.byte2ShortEntrySet());
			return (ObjectOrderedSet<Byte2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2ShortSortedMap {
		Byte2ShortSortedMap map;
		
		UnmodifyableSortedMap(Byte2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2ShortSortedMap subMap(byte fromKey, byte toKey) { return Byte2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ShortSortedMap headMap(byte toKey) { return Byte2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ShortSortedMap tailMap(byte fromKey) { return Byte2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Byte2ShortSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2ShortMap implements Byte2ShortMap {
		Byte2ShortMap map;
		ShortCollection values;
		ByteSet keys;
		ObjectSet<Byte2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(byte key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(byte key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(byte key) {
			short type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public short getOrDefault(byte key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(ByteShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(Byte2ShortMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ShortMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Byte2ShortMap.Entry>
	{
		ObjectOrderedSet<Byte2ShortMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Byte2ShortMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Byte2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Byte2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Byte2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Byte2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Byte2ShortMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator(Byte2ShortMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Byte2ShortMap.Entry first() { return set.first(); }
		@Override
		public Byte2ShortMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ShortMap.Entry last() { return set.last(); }
		@Override
		public Byte2ShortMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2ShortMap.Entry>
	{
		ObjectSet<Byte2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2ShortMap.Entry> iterator() {
			return new ObjectIterator<Byte2ShortMap.Entry>() {
				ObjectIterator<Byte2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2ShortMap.Entry next() { return Byte2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2ShortNavigableMap {
		Byte2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Byte2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2ShortNavigableMap descendingMap() { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ByteNavigableSet keySet() { synchronized(mutex) { return ByteSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Byte2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Byte2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2ShortNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2ShortNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2ShortNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2ShortNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ShortNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ShortNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2ShortMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2ShortMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2ShortMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2ShortMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2ShortNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2ShortMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2ShortMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2ShortMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2ShortMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2ShortOrderedMap {
		Byte2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Byte2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(byte key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(byte key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Byte2ShortOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ByteOrderedSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return (ByteOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2ShortEntrySet(), mutex);
			return (ObjectOrderedSet<Byte2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2ShortSortedMap {
		Byte2ShortSortedMap map;
		
		SynchronizedSortedMap(Byte2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2ShortSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ShortSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ShortSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Byte2ShortSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2ShortSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ShortSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2ShortMap implements Byte2ShortMap {
		Byte2ShortMap map;
		ShortCollection values;
		ByteSet keys;
		ObjectSet<Byte2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(byte key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(byte key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(byte key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(byte key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(byte key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(byte key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Byte2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(ByteShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsent(key, valueProvider); } }
		@Override
		public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(byte key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Byte2ShortMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2ShortEntrySet(), mutex);
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
		public Short put(Byte key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Byte key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Byte key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Byte key, BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Byte key, Function<? super Byte, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Byte key, BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Byte key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}