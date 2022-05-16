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
import speiger.src.collections.bytes.functions.consumer.ByteIntConsumer;
import speiger.src.collections.bytes.functions.function.Byte2IntFunction;
import speiger.src.collections.bytes.functions.function.ByteIntUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.ints.utils.IntSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2IntMap.Entry> fastIterator(Byte2IntMap map) {
		ObjectSet<Byte2IntMap.Entry> entries = map.byte2IntEntrySet();
		return entries instanceof Byte2IntMap.FastEntrySet ? ((Byte2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2IntMap.Entry> fastIterable(Byte2IntMap map) {
		ObjectSet<Byte2IntMap.Entry> entries = map.byte2IntEntrySet();
		return map instanceof Byte2IntMap.FastEntrySet ? new ObjectIterable<Byte2IntMap.Entry>(){
			@Override
			public ObjectIterator<Byte2IntMap.Entry> iterator() { return ((Byte2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2IntMap.Entry> action) { ((Byte2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2IntMap map, Consumer<Byte2IntMap.Entry> action) {
		ObjectSet<Byte2IntMap.Entry> entries = map.byte2IntEntrySet();
		if(entries instanceof Byte2IntMap.FastEntrySet) ((Byte2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntMap synchronize(Byte2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntMap synchronize(Byte2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntSortedMap synchronize(Byte2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntSortedMap synchronize(Byte2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntOrderedMap synchronize(Byte2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntOrderedMap synchronize(Byte2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntNavigableMap synchronize(Byte2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2IntNavigableMap synchronize(Byte2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2IntMap unmodifiable(Byte2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2IntOrderedMap unmodifiable(Byte2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2IntSortedMap unmodifiable(Byte2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2IntNavigableMap unmodifiable(Byte2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2IntMap.Entry unmodifiable(Byte2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2IntMap.Entry unmodifiable(Map.Entry<Byte, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2IntMap singleton(byte key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2IntMap {
		final byte key;
		final int value;
		ByteSet keySet;
		IntCollection values;
		ObjectSet<Byte2IntMap.Entry> entrySet;
		
		SingletonMap(byte key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(byte key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(byte key, int defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2IntMap {
		@Override
		public int put(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(byte key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(byte key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(byte key, int defaultValue) { return 0; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2IntMap.Entry entry) {
			super(entry.getByteKey(), entry.getIntValue());
		}
		
		@Override
		public void set(byte key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2IntNavigableMap {
		Byte2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2IntNavigableMap descendingMap() { return Byte2IntMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2IntMap.Entry firstEntry() { return Byte2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2IntMap.Entry lastEntry() { return Byte2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2IntNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, byte toKey) { return Byte2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2IntNavigableMap headMap(byte toKey) { return Byte2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey) { return Byte2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2IntMap.Entry lowerEntry(byte key) { return Byte2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2IntMap.Entry higherEntry(byte key) { return Byte2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2IntMap.Entry floorEntry(byte key) { return Byte2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2IntMap.Entry ceilingEntry(byte key) { return Byte2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2IntOrderedMap {
		Byte2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Byte2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2IntSortedMap {
		Byte2IntSortedMap map;
		
		UnmodifyableSortedMap(Byte2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2IntSortedMap subMap(byte fromKey, byte toKey) { return Byte2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2IntSortedMap headMap(byte toKey) { return Byte2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2IntSortedMap tailMap(byte fromKey) { return Byte2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Byte2IntSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2IntMap implements Byte2IntMap {
		Byte2IntMap map;
		IntCollection values;
		ByteSet keys;
		ObjectSet<Byte2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(byte key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(byte key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(byte key) { return map.get(key); }
		@Override
		public int getOrDefault(byte key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2IntMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2IntMap.Entry>
	{
		ObjectSet<Byte2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2IntMap.Entry> iterator() {
			return new ObjectIterator<Byte2IntMap.Entry>() {
				ObjectIterator<Byte2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2IntMap.Entry next() { return Byte2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2IntNavigableMap {
		Byte2IntNavigableMap map;
		
		SynchronizedNavigableMap(Byte2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2IntNavigableMap descendingMap() { synchronized(mutex) { return Byte2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2IntMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2IntNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2IntNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2IntMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2IntMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2IntMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2IntMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2IntNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2IntNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2IntNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2IntNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2IntNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2IntNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2IntMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2IntMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2IntMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2IntMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2IntOrderedMap {
		Byte2IntOrderedMap map;
		
		SynchronizedOrderedMap(Byte2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(byte key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(byte key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Byte2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2IntSortedMap {
		Byte2IntSortedMap map;
		
		SynchronizedSortedMap(Byte2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2IntSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2IntSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2IntSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Byte2IntSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2IntSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2IntSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2IntSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2IntMap implements Byte2IntMap {
		Byte2IntMap map;
		IntCollection values;
		ByteSet keys;
		ObjectSet<Byte2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(byte key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(byte key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(byte key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(byte key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(byte key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(byte key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Byte2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(ByteIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(byte key, ByteIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(byte key, Byte2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(byte key, ByteIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int mergeInt(byte key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Byte2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(byte key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2IntMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2IntEntrySet(), mutex);
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
		public Integer put(Byte key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Byte key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Byte key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Byte key, BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Byte key, Function<? super Byte, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Byte key, BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Byte key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}