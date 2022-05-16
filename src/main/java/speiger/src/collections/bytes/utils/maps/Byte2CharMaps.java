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
import speiger.src.collections.bytes.functions.consumer.ByteCharConsumer;
import speiger.src.collections.bytes.functions.function.Byte2CharFunction;
import speiger.src.collections.bytes.functions.function.ByteCharUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.utils.CharCollections;
import speiger.src.collections.chars.utils.CharSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2CharMap.Entry> fastIterator(Byte2CharMap map) {
		ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
		return entries instanceof Byte2CharMap.FastEntrySet ? ((Byte2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2CharMap.Entry> fastIterable(Byte2CharMap map) {
		ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
		return map instanceof Byte2CharMap.FastEntrySet ? new ObjectIterable<Byte2CharMap.Entry>(){
			@Override
			public ObjectIterator<Byte2CharMap.Entry> iterator() { return ((Byte2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2CharMap.Entry> action) { ((Byte2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2CharMap map, Consumer<Byte2CharMap.Entry> action) {
		ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
		if(entries instanceof Byte2CharMap.FastEntrySet) ((Byte2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharMap synchronize(Byte2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharMap synchronize(Byte2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharSortedMap synchronize(Byte2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharSortedMap synchronize(Byte2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharOrderedMap synchronize(Byte2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharOrderedMap synchronize(Byte2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharNavigableMap synchronize(Byte2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2CharNavigableMap synchronize(Byte2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2CharMap unmodifiable(Byte2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2CharOrderedMap unmodifiable(Byte2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2CharSortedMap unmodifiable(Byte2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2CharNavigableMap unmodifiable(Byte2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2CharMap.Entry unmodifiable(Byte2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2CharMap.Entry unmodifiable(Map.Entry<Byte, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2CharMap singleton(byte key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2CharMap {
		final byte key;
		final char value;
		ByteSet keySet;
		CharCollection values;
		ObjectSet<Byte2CharMap.Entry> entrySet;
		
		SingletonMap(byte key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(byte key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(byte key, char defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2CharMap.Entry> byte2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2CharMap {
		@Override
		public char put(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(byte key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(byte key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(byte key, char defaultValue) { return (char)0; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Byte2CharMap.Entry> byte2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2CharMap.Entry entry) {
			super(entry.getByteKey(), entry.getCharValue());
		}
		
		@Override
		public void set(byte key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2CharNavigableMap {
		Byte2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2CharNavigableMap descendingMap() { return Byte2CharMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2CharMap.Entry firstEntry() { return Byte2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2CharMap.Entry lastEntry() { return Byte2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2CharNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2CharNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2CharNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2CharNavigableMap subMap(byte fromKey, byte toKey) { return Byte2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2CharNavigableMap headMap(byte toKey) { return Byte2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2CharNavigableMap tailMap(byte fromKey) { return Byte2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2CharMap.Entry lowerEntry(byte key) { return Byte2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2CharMap.Entry higherEntry(byte key) { return Byte2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2CharMap.Entry floorEntry(byte key) { return Byte2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2CharMap.Entry ceilingEntry(byte key) { return Byte2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2CharNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2CharOrderedMap {
		Byte2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Byte2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2CharSortedMap {
		Byte2CharSortedMap map;
		
		UnmodifyableSortedMap(Byte2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2CharSortedMap subMap(byte fromKey, byte toKey) { return Byte2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2CharSortedMap headMap(byte toKey) { return Byte2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2CharSortedMap tailMap(byte fromKey) { return Byte2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Byte2CharSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2CharMap implements Byte2CharMap {
		Byte2CharMap map;
		CharCollection values;
		ByteSet keys;
		ObjectSet<Byte2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(byte key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(byte key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(byte key) { return map.get(key); }
		@Override
		public char getOrDefault(byte key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2CharMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2CharMap.Entry> byte2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2CharMap.Entry>
	{
		ObjectSet<Byte2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2CharMap.Entry> iterator() {
			return new ObjectIterator<Byte2CharMap.Entry>() {
				ObjectIterator<Byte2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2CharMap.Entry next() { return Byte2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2CharNavigableMap {
		Byte2CharNavigableMap map;
		
		SynchronizedNavigableMap(Byte2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2CharNavigableMap descendingMap() { synchronized(mutex) { return Byte2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2CharMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2CharNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2CharNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2CharNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2CharNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2CharNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2CharNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2CharMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2CharMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2CharMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2CharMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2CharNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2CharNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2CharNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2CharNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2CharNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2CharNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2CharNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2CharMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2CharMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2CharMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2CharMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2CharOrderedMap {
		Byte2CharOrderedMap map;
		
		SynchronizedOrderedMap(Byte2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(byte key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(byte key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Byte2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2CharSortedMap {
		Byte2CharSortedMap map;
		
		SynchronizedSortedMap(Byte2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2CharSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2CharSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2CharSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Byte2CharSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2CharSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2CharSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2CharSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2CharMap implements Byte2CharMap {
		Byte2CharMap map;
		CharCollection values;
		ByteSet keys;
		ObjectSet<Byte2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(byte key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(byte key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(byte key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(byte key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(byte key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(byte key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Byte2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(ByteCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(byte key, ByteCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(byte key, Byte2CharFunction mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(byte key, ByteCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char mergeChar(byte key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Byte2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(byte key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2CharMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2CharMap.Entry> byte2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2CharEntrySet(), mutex);
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
		public Character put(Byte key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Byte key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Byte key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Byte key, Function<? super Byte, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Byte key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}