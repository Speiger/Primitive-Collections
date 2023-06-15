package speiger.src.collections.chars.utils.maps;

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
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.consumer.CharByteConsumer;
import speiger.src.collections.chars.functions.function.Char2ByteFunction;
import speiger.src.collections.chars.functions.function.CharByteUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.utils.ByteCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2ByteMap.Entry> fastIterator(Char2ByteMap map) {
		ObjectSet<Char2ByteMap.Entry> entries = map.char2ByteEntrySet();
		return entries instanceof Char2ByteMap.FastEntrySet ? ((Char2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2ByteMap.Entry> fastIterable(Char2ByteMap map) {
		ObjectSet<Char2ByteMap.Entry> entries = map.char2ByteEntrySet();
		return map instanceof Char2ByteMap.FastEntrySet ? new ObjectIterable<Char2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Char2ByteMap.Entry> iterator() { return ((Char2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2ByteMap.Entry> action) { ((Char2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2ByteMap map, Consumer<Char2ByteMap.Entry> action) {
		ObjectSet<Char2ByteMap.Entry> entries = map.char2ByteEntrySet();
		if(entries instanceof Char2ByteMap.FastEntrySet) ((Char2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteMap synchronize(Char2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteMap synchronize(Char2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteSortedMap synchronize(Char2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteSortedMap synchronize(Char2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteOrderedMap synchronize(Char2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteOrderedMap synchronize(Char2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteNavigableMap synchronize(Char2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ByteNavigableMap synchronize(Char2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2ByteMap unmodifiable(Char2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ByteOrderedMap unmodifiable(Char2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ByteSortedMap unmodifiable(Char2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ByteNavigableMap unmodifiable(Char2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2ByteMap.Entry unmodifiable(Char2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2ByteMap.Entry unmodifiable(Map.Entry<Character, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2ByteMap singleton(char key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2ByteMap {
		final char key;
		final byte value;
		CharSet keySet;
		ByteCollection values;
		ObjectSet<Char2ByteMap.Entry> entrySet;
		
		SingletonMap(char key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(char key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(char key, byte defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public byte computeByte(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2ByteMap.Entry> char2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2ByteMap {
		@Override
		public byte put(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(char key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(char key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(char key, byte defaultValue) { return defaultValue; }
		@Override
		public byte computeByte(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Char2ByteMap.Entry> char2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2ByteMap.Entry entry) {
			super(entry.getCharKey(), entry.getByteValue());
		}
		
		@Override
		public void set(char key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2ByteNavigableMap {
		Char2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Char2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2ByteNavigableMap descendingMap() { return Char2ByteMaps.unmodifiable(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2ByteMap.Entry firstEntry() { return Char2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2ByteMap.Entry lastEntry() { return Char2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ByteNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2ByteNavigableMap headMap(char toKey, boolean inclusive) { return Char2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2ByteNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2ByteNavigableMap subMap(char fromKey, char toKey) { return Char2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ByteNavigableMap headMap(char toKey) { return Char2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ByteNavigableMap tailMap(char fromKey) { return Char2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(char e) { throw new UnsupportedOperationException(); }
		@Override
		public char getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(char e) { throw new UnsupportedOperationException(); }
		@Override
		public char getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public char lowerKey(char key) { return map.lowerKey(key); }
		@Override
		public char higherKey(char key) { return map.higherKey(key); }
		@Override
		public char floorKey(char key) { return map.floorKey(key); }
		@Override
		public char ceilingKey(char key) { return map.ceilingKey(key); }
		@Override
		public Char2ByteMap.Entry lowerEntry(char key) { return Char2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2ByteMap.Entry higherEntry(char key) { return Char2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2ByteMap.Entry floorEntry(char key) { return Char2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2ByteMap.Entry ceilingEntry(char key) { return Char2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2ByteNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2ByteOrderedMap {
		Char2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Char2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Char2ByteOrderedMap copy() { return map.copy(); }
		@Override
		public CharOrderedSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return (CharOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.char2ByteEntrySet());
			return (ObjectOrderedSet<Char2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2ByteSortedMap {
		Char2ByteSortedMap map;
		
		UnmodifyableSortedMap(Char2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2ByteSortedMap subMap(char fromKey, char toKey) { return Char2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ByteSortedMap headMap(char toKey) { return Char2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ByteSortedMap tailMap(char fromKey) { return Char2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public CharSortedSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Char2ByteSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2ByteMap implements Char2ByteMap {
		Char2ByteMap map;
		ByteCollection values;
		CharSet keys;
		ObjectSet<Char2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Char2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(char key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(char key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(char key) {
			byte type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public byte getOrDefault(char key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public byte computeByte(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(Char2ByteMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Char2ByteMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2ByteMap.Entry> char2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2ByteEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Char2ByteMap.Entry>
	{
		ObjectOrderedSet<Char2ByteMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Char2ByteMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Char2ByteMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(Char2ByteMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Char2ByteMap.Entry first() { return set.first(); }
		@Override
		public Char2ByteMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ByteMap.Entry last() { return set.last(); }
		@Override
		public Char2ByteMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2ByteMap.Entry>
	{
		ObjectSet<Char2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Char2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2ByteMap.Entry> iterator() {
			return new ObjectIterator<Char2ByteMap.Entry>() {
				ObjectIterator<Char2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2ByteMap.Entry next() { return Char2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2ByteNavigableMap {
		Char2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Char2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2ByteNavigableMap descendingMap() { synchronized(mutex) { return Char2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public CharNavigableSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Char2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Char2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2ByteNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2ByteNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2ByteNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2ByteNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ByteNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ByteNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2ByteMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2ByteMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2ByteMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2ByteMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2ByteNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ByteNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(char e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public char getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(char e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public char getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Character lowerKey(Character key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Character floorKey(Character key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Character ceilingKey(Character key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Character higherKey(Character key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Char2ByteMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2ByteMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2ByteMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2ByteMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2ByteOrderedMap {
		Char2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Char2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(char key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(char key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Char2ByteOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharOrderedSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return (CharOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ByteEntrySet(), mutex);
			return (ObjectOrderedSet<Char2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2ByteSortedMap {
		Char2ByteSortedMap map;
		
		SynchronizedSortedMap(Char2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2ByteSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ByteSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ByteSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public CharSortedSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Char2ByteSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2ByteSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ByteSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ByteSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2ByteMap implements Char2ByteMap {
		Char2ByteMap map;
		ByteCollection values;
		CharSet keys;
		ObjectSet<Char2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(char key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(char key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(char key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(char key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(char key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(char key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Char2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(CharByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(char key, CharByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteNonDefault(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) { synchronized(mutex) { return map.supplyByteIfAbsent(key, valueProvider); } }
		@Override
		public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) { synchronized(mutex) { return map.supplyByteIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(char key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Char2ByteMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2ByteMap.Entry> char2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ByteEntrySet(), mutex);
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
		public Byte put(Character key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Character key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Character key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Character key, BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Character key, Function<? super Character, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Character key, BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Character key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}