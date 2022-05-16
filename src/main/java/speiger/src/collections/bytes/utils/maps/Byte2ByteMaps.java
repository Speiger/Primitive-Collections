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
import speiger.src.collections.bytes.functions.consumer.ByteByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ByteFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.utils.ByteCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2ByteMap.Entry> fastIterator(Byte2ByteMap map) {
		ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
		return entries instanceof Byte2ByteMap.FastEntrySet ? ((Byte2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2ByteMap.Entry> fastIterable(Byte2ByteMap map) {
		ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
		return map instanceof Byte2ByteMap.FastEntrySet ? new ObjectIterable<Byte2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Byte2ByteMap.Entry> iterator() { return ((Byte2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2ByteMap.Entry> action) { ((Byte2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2ByteMap map, Consumer<Byte2ByteMap.Entry> action) {
		ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
		if(entries instanceof Byte2ByteMap.FastEntrySet) ((Byte2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteMap synchronize(Byte2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteMap synchronize(Byte2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteSortedMap synchronize(Byte2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteSortedMap synchronize(Byte2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteOrderedMap synchronize(Byte2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteOrderedMap synchronize(Byte2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteNavigableMap synchronize(Byte2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2ByteNavigableMap synchronize(Byte2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2ByteMap unmodifiable(Byte2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ByteOrderedMap unmodifiable(Byte2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ByteSortedMap unmodifiable(Byte2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2ByteNavigableMap unmodifiable(Byte2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2ByteMap.Entry unmodifiable(Byte2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2ByteMap.Entry unmodifiable(Map.Entry<Byte, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2ByteMap singleton(byte key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2ByteMap {
		final byte key;
		final byte value;
		ByteSet keySet;
		ByteCollection values;
		ObjectSet<Byte2ByteMap.Entry> entrySet;
		
		SingletonMap(byte key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(byte key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(byte key, byte defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2ByteMap.Entry> byte2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2ByteMap {
		@Override
		public byte put(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(byte key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(byte key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(byte key, byte defaultValue) { return (byte)0; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Byte2ByteMap.Entry> byte2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2ByteMap.Entry entry) {
			super(entry.getByteKey(), entry.getByteValue());
		}
		
		@Override
		public void set(byte key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2ByteNavigableMap {
		Byte2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2ByteNavigableMap descendingMap() { return Byte2ByteMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2ByteMap.Entry firstEntry() { return Byte2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2ByteMap.Entry lastEntry() { return Byte2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ByteNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2ByteNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2ByteNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2ByteNavigableMap subMap(byte fromKey, byte toKey) { return Byte2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ByteNavigableMap headMap(byte toKey) { return Byte2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ByteNavigableMap tailMap(byte fromKey) { return Byte2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2ByteMap.Entry lowerEntry(byte key) { return Byte2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2ByteMap.Entry higherEntry(byte key) { return Byte2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2ByteMap.Entry floorEntry(byte key) { return Byte2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2ByteMap.Entry ceilingEntry(byte key) { return Byte2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2ByteOrderedMap {
		Byte2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Byte2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2ByteSortedMap {
		Byte2ByteSortedMap map;
		
		UnmodifyableSortedMap(Byte2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2ByteSortedMap subMap(byte fromKey, byte toKey) { return Byte2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ByteSortedMap headMap(byte toKey) { return Byte2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ByteSortedMap tailMap(byte fromKey) { return Byte2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Byte2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2ByteMap implements Byte2ByteMap {
		Byte2ByteMap map;
		ByteCollection values;
		ByteSet keys;
		ObjectSet<Byte2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(byte key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(byte key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(byte key) { return map.get(key); }
		@Override
		public byte getOrDefault(byte key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2ByteMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2ByteMap.Entry>
	{
		ObjectSet<Byte2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2ByteMap.Entry> iterator() {
			return new ObjectIterator<Byte2ByteMap.Entry>() {
				ObjectIterator<Byte2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2ByteMap.Entry next() { return Byte2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2ByteNavigableMap {
		Byte2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Byte2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2ByteNavigableMap descendingMap() { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2ByteNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2ByteNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2ByteNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2ByteNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ByteNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ByteNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2ByteMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2ByteMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2ByteMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2ByteMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2ByteNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2ByteMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2ByteMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2ByteMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2ByteMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2ByteOrderedMap {
		Byte2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Byte2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(byte key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(byte key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Byte2ByteOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2ByteSortedMap {
		Byte2ByteSortedMap map;
		
		SynchronizedSortedMap(Byte2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2ByteSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ByteSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ByteSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Byte2ByteSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2ByteSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ByteSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2ByteMap implements Byte2ByteMap {
		Byte2ByteMap map;
		ByteCollection values;
		ByteSet keys;
		ObjectSet<Byte2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(byte key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(byte key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(byte key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(byte key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(byte key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(byte key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Byte2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(byte key, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(byte key, Byte2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(byte key, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte mergeByte(byte key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Byte2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(byte key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2ByteMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2ByteEntrySet(), mutex);
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
		public Byte put(Byte key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Byte key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Byte key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Byte key, Function<? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Byte key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}