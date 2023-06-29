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
import speiger.src.collections.chars.functions.consumer.CharShortConsumer;
import speiger.src.collections.chars.functions.function.Char2ShortFunction;
import speiger.src.collections.chars.functions.function.CharShortUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.utils.ShortCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2ShortMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2ShortMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2ShortMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2ShortMap.Entry> fastIterator(Char2ShortMap map) {
		ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
		return entries instanceof Char2ShortMap.FastEntrySet ? ((Char2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2ShortMap.Entry> fastIterable(Char2ShortMap map) {
		ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
		return map instanceof Char2ShortMap.FastEntrySet ? new ObjectIterable<Char2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Char2ShortMap.Entry> iterator() { return ((Char2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2ShortMap.Entry> action) { ((Char2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2ShortMap map, Consumer<Char2ShortMap.Entry> action) {
		ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
		if(entries instanceof Char2ShortMap.FastEntrySet) ((Char2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortMap synchronize(Char2ShortMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortMap synchronize(Char2ShortMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortSortedMap synchronize(Char2ShortSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortSortedMap synchronize(Char2ShortSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortOrderedMap synchronize(Char2ShortOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortOrderedMap synchronize(Char2ShortOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortNavigableMap synchronize(Char2ShortNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2ShortNavigableMap synchronize(Char2ShortNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2ShortMap unmodifiable(Char2ShortMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ShortOrderedMap unmodifiable(Char2ShortOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ShortSortedMap unmodifiable(Char2ShortSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2ShortNavigableMap unmodifiable(Char2ShortNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2ShortMap.Entry unmodifiable(Char2ShortMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2ShortMap.Entry unmodifiable(Map.Entry<Character, Short> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2ShortMap singleton(char key, short value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2ShortMap {
		final char key;
		final short value;
		CharSet keySet;
		ShortCollection values;
		ObjectSet<Char2ShortMap.Entry> entrySet;
		
		SingletonMap(char key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short put(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(char key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public short getOrDefault(char key, short defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public short computeShort(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ShortCollection values() { 
			if(values == null) values = ShortCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2ShortMap.Entry> char2ShortEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2ShortMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2ShortMap {
		@Override
		public short put(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short addTo(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(char key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(char key) { return getDefaultReturnValue(); }
		@Override
		public short getOrDefault(char key, short defaultValue) { return defaultValue; }
		@Override
		public short computeShort(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public ShortCollection values() { return ShortCollections.empty(); }
		@Override
		public ObjectSet<Char2ShortMap.Entry> char2ShortEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2ShortMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Short> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2ShortMap.Entry entry) {
			super(entry.getCharKey(), entry.getShortValue());
		}
		
		@Override
		public void set(char key, short value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2ShortNavigableMap {
		Char2ShortNavigableMap map;
		
		UnmodifyableNavigableMap(Char2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2ShortNavigableMap descendingMap() { return Char2ShortMaps.unmodifiable(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2ShortMap.Entry firstEntry() { return Char2ShortMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2ShortMap.Entry lastEntry() { return Char2ShortMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2ShortMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ShortMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ShortNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2ShortMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2ShortNavigableMap headMap(char toKey, boolean inclusive) { return Char2ShortMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2ShortNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2ShortMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2ShortNavigableMap subMap(char fromKey, char toKey) { return Char2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ShortNavigableMap headMap(char toKey) { return Char2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ShortNavigableMap tailMap(char fromKey) { return Char2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2ShortMap.Entry lowerEntry(char key) { return Char2ShortMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2ShortMap.Entry higherEntry(char key) { return Char2ShortMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2ShortMap.Entry floorEntry(char key) { return Char2ShortMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2ShortMap.Entry ceilingEntry(char key) { return Char2ShortMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2ShortNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2ShortOrderedMap {
		Char2ShortOrderedMap map;
		
		UnmodifyableOrderedMap(Char2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putAndMoveToLast(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public short getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Char2ShortOrderedMap copy() { return map.copy(); }
		@Override
		public CharOrderedSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return (CharOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Char2ShortMap.Entry> char2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.char2ShortEntrySet());
			return (ObjectOrderedSet<Char2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2ShortSortedMap {
		Char2ShortSortedMap map;
		
		UnmodifyableSortedMap(Char2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2ShortSortedMap subMap(char fromKey, char toKey) { return Char2ShortMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ShortSortedMap headMap(char toKey) { return Char2ShortMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ShortSortedMap tailMap(char fromKey) { return Char2ShortMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public short firstShortValue() { return map.firstShortValue(); }
		@Override
		public short lastShortValue() { return map.lastShortValue(); }
		@Override
		public Char2ShortSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2ShortMap implements Char2ShortMap {
		Char2ShortMap map;
		ShortCollection values;
		CharSet keys;
		ObjectSet<Char2ShortMap.Entry> entrySet;
		
		UnmodifyableMap(Char2ShortMap map) {
			this.map = map;
		}
		
		@Override
		public short put(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short putIfAbsent(char key, short value){ throw new UnsupportedOperationException(); }
		@Override
		public short addTo(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short subFrom(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public short removeOrDefault(char key, short defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, short value) { throw new UnsupportedOperationException(); }
		@Override
		public short get(char key) {
			short type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public short getOrDefault(char key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short computeShort(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(CharShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceShorts(Char2ShortMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Char2ShortMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2ShortMap.Entry> char2ShortEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2ShortEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Char2ShortMap.Entry>
	{
		ObjectOrderedSet<Char2ShortMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Char2ShortMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Char2ShortMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> iterator(Char2ShortMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Char2ShortMap.Entry first() { return set.first(); }
		@Override
		public Char2ShortMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ShortMap.Entry last() { return set.last(); }
		@Override
		public Char2ShortMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2ShortMap.Entry>
	{
		ObjectSet<Char2ShortMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2ShortMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2ShortMap.Entry> action) {
			s.forEach(T -> action.accept(Char2ShortMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2ShortMap.Entry> iterator() {
			return new ObjectIterator<Char2ShortMap.Entry>() {
				ObjectIterator<Char2ShortMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2ShortMap.Entry next() { return Char2ShortMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2ShortNavigableMap {
		Char2ShortNavigableMap map;
		
		SynchronizedNavigableMap(Char2ShortNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2ShortNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2ShortNavigableMap descendingMap() { synchronized(mutex) { return Char2ShortMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public CharNavigableSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Char2ShortMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2ShortMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Char2ShortMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2ShortMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2ShortNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2ShortNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2ShortNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2ShortNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ShortNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ShortNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2ShortMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2ShortMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2ShortMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2ShortMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2ShortNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ShortNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2ShortMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2ShortMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2ShortMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2ShortMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2ShortOrderedMap {
		Char2ShortOrderedMap map;
		
		SynchronizedOrderedMap(Char2ShortOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2ShortOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public short putAndMoveToFirst(char key, short value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public short putAndMoveToLast(char key, short value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public short getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public short getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Char2ShortOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharOrderedSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return (CharOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Char2ShortMap.Entry> char2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ShortEntrySet(), mutex);
			return (ObjectOrderedSet<Char2ShortMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2ShortSortedMap {
		Char2ShortSortedMap map;
		
		SynchronizedSortedMap(Char2ShortSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2ShortSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2ShortSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ShortSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ShortSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public short firstShortValue() { synchronized(mutex) { return map.firstShortValue(); } }
		@Override
		public short lastShortValue() { synchronized(mutex) { return map.lastShortValue(); } }
		@Override
		public Char2ShortSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2ShortSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ShortSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ShortSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2ShortMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2ShortMap implements Char2ShortMap {
		Char2ShortMap map;
		ShortCollection values;
		CharSet keys;
		ObjectSet<Char2ShortMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2ShortMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2ShortMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public short getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2ShortMap setDefaultReturnValue(short v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public short put(char key, short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public short putIfAbsent(char key, short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2ShortMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public short addTo(char key, short value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public short subFrom(char key, short value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2ShortMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2ShortMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Short> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, short[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(short value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public short get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public short remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public short removeOrDefault(char key, short defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, short value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, short oldValue, short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public short replace(char key, short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceShorts(Char2ShortMap m) { synchronized(mutex) { map.replaceShorts(m); } }
		@Override
		public void replaceShorts(CharShortUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceShorts(mappingFunction); } }
		@Override
		public short computeShort(char key, CharShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShort(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsent(key, mappingFunction); } }
		@Override
		public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresent(key, mappingFunction); } }
		@Override
		public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsent(key, valueProvider); } }
		@Override
		public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) { synchronized(mutex) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeShortIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) { synchronized(mutex) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeShort(key, value, mappingFunction); } }
		@Override
		public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllShort(m, mappingFunction); } }
		@Override
		public short getOrDefault(char key, short defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharShortConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Char2ShortMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ShortCollection values() {
			if(values == null) values = ShortCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2ShortMap.Entry> char2ShortEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ShortEntrySet(), mutex);
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
		public Short put(Character key, Short value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Short remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Short putIfAbsent(Character key, Short value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Short oldValue, Short newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Short replace(Character key, Short value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Short compute(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfAbsent(Character key, Function<? super Character, ? extends Short> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short computeIfPresent(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Short merge(Character key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Short> action) { synchronized(mutex) { map.forEach(action); } }
	}
}