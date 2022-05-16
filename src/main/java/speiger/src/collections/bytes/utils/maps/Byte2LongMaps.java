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
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.consumer.ByteLongConsumer;
import speiger.src.collections.bytes.functions.function.Byte2LongFunction;
import speiger.src.collections.bytes.functions.function.ByteLongUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.utils.LongCollections;
import speiger.src.collections.longs.utils.LongSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2LongMap.Entry> fastIterator(Byte2LongMap map) {
		ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
		return entries instanceof Byte2LongMap.FastEntrySet ? ((Byte2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2LongMap.Entry> fastIterable(Byte2LongMap map) {
		ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
		return map instanceof Byte2LongMap.FastEntrySet ? new ObjectIterable<Byte2LongMap.Entry>(){
			@Override
			public ObjectIterator<Byte2LongMap.Entry> iterator() { return ((Byte2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2LongMap.Entry> action) { ((Byte2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2LongMap map, Consumer<Byte2LongMap.Entry> action) {
		ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
		if(entries instanceof Byte2LongMap.FastEntrySet) ((Byte2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongMap synchronize(Byte2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongMap synchronize(Byte2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongSortedMap synchronize(Byte2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongSortedMap synchronize(Byte2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongOrderedMap synchronize(Byte2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongOrderedMap synchronize(Byte2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongNavigableMap synchronize(Byte2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2LongNavigableMap synchronize(Byte2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2LongMap unmodifiable(Byte2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2LongOrderedMap unmodifiable(Byte2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2LongSortedMap unmodifiable(Byte2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2LongNavigableMap unmodifiable(Byte2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2LongMap.Entry unmodifiable(Byte2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2LongMap.Entry unmodifiable(Map.Entry<Byte, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2LongMap singleton(byte key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2LongMap {
		final byte key;
		final long value;
		ByteSet keySet;
		LongCollection values;
		ObjectSet<Byte2LongMap.Entry> entrySet;
		
		SingletonMap(byte key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(byte key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(byte key, long defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2LongMap.Entry> byte2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2LongMap {
		@Override
		public long put(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(byte key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(byte key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(byte key, long defaultValue) { return 0L; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Byte2LongMap.Entry> byte2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2LongMap.Entry entry) {
			super(entry.getByteKey(), entry.getLongValue());
		}
		
		@Override
		public void set(byte key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2LongNavigableMap {
		Byte2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2LongNavigableMap descendingMap() { return Byte2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2LongMap.Entry firstEntry() { return Byte2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2LongMap.Entry lastEntry() { return Byte2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2LongNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2LongNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2LongNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2LongNavigableMap subMap(byte fromKey, byte toKey) { return Byte2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2LongNavigableMap headMap(byte toKey) { return Byte2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2LongNavigableMap tailMap(byte fromKey) { return Byte2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2LongMap.Entry lowerEntry(byte key) { return Byte2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2LongMap.Entry higherEntry(byte key) { return Byte2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2LongMap.Entry floorEntry(byte key) { return Byte2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2LongMap.Entry ceilingEntry(byte key) { return Byte2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2LongOrderedMap {
		Byte2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Byte2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2LongSortedMap {
		Byte2LongSortedMap map;
		
		UnmodifyableSortedMap(Byte2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2LongSortedMap subMap(byte fromKey, byte toKey) { return Byte2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2LongSortedMap headMap(byte toKey) { return Byte2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2LongSortedMap tailMap(byte fromKey) { return Byte2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Byte2LongSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2LongMap implements Byte2LongMap {
		Byte2LongMap map;
		LongCollection values;
		ByteSet keys;
		ObjectSet<Byte2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(byte key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(byte key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(byte key) { return map.get(key); }
		@Override
		public long getOrDefault(byte key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2LongMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2LongMap.Entry> byte2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2LongMap.Entry>
	{
		ObjectSet<Byte2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2LongMap.Entry> iterator() {
			return new ObjectIterator<Byte2LongMap.Entry>() {
				ObjectIterator<Byte2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2LongMap.Entry next() { return Byte2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2LongNavigableMap {
		Byte2LongNavigableMap map;
		
		SynchronizedNavigableMap(Byte2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2LongNavigableMap descendingMap() { synchronized(mutex) { return Byte2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2LongMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2LongNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2LongNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2LongNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2LongNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2LongNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2LongNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2LongMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2LongMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2LongMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2LongMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2LongNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2LongNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2LongNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2LongNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2LongNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2LongNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2LongMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2LongMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2LongMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2LongMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2LongOrderedMap {
		Byte2LongOrderedMap map;
		
		SynchronizedOrderedMap(Byte2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(byte key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(byte key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Byte2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2LongSortedMap {
		Byte2LongSortedMap map;
		
		SynchronizedSortedMap(Byte2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2LongSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2LongSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2LongSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Byte2LongSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2LongSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2LongSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2LongSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2LongMap implements Byte2LongMap {
		Byte2LongMap map;
		LongCollection values;
		ByteSet keys;
		ObjectSet<Byte2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(byte key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(byte key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(byte key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(byte key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(byte key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(byte key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Byte2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(ByteLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(byte key, ByteLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(byte key, Byte2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(byte key, ByteLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(byte key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Byte2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(byte key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2LongMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2LongMap.Entry> byte2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2LongEntrySet(), mutex);
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
		public Long put(Byte key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Byte key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Byte key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Byte key, Function<? super Byte, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Byte key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}