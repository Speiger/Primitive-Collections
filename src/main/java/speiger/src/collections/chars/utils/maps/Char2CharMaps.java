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
import speiger.src.collections.chars.functions.consumer.CharCharConsumer;
import speiger.src.collections.chars.functions.function.CharUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2CharMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2CharMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2CharMap.Entry> fastIterator(Char2CharMap map) {
		ObjectSet<Char2CharMap.Entry> entries = map.char2CharEntrySet();
		return entries instanceof Char2CharMap.FastEntrySet ? ((Char2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2CharMap.Entry> fastIterable(Char2CharMap map) {
		ObjectSet<Char2CharMap.Entry> entries = map.char2CharEntrySet();
		return map instanceof Char2CharMap.FastEntrySet ? new ObjectIterable<Char2CharMap.Entry>(){
			@Override
			public ObjectIterator<Char2CharMap.Entry> iterator() { return ((Char2CharMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2CharMap.Entry> action) { ((Char2CharMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2CharMap map, Consumer<Char2CharMap.Entry> action) {
		ObjectSet<Char2CharMap.Entry> entries = map.char2CharEntrySet();
		if(entries instanceof Char2CharMap.FastEntrySet) ((Char2CharMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharMap synchronize(Char2CharMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharMap synchronize(Char2CharMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharSortedMap synchronize(Char2CharSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharSortedMap synchronize(Char2CharSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharOrderedMap synchronize(Char2CharOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharOrderedMap synchronize(Char2CharOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharNavigableMap synchronize(Char2CharNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2CharNavigableMap synchronize(Char2CharNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2CharMap unmodifiable(Char2CharMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2CharOrderedMap unmodifiable(Char2CharOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2CharSortedMap unmodifiable(Char2CharSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2CharNavigableMap unmodifiable(Char2CharNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2CharMap.Entry unmodifiable(Char2CharMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2CharMap.Entry unmodifiable(Map.Entry<Character, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2CharMap singleton(char key, char value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2CharMap {
		final char key;
		final char value;
		CharSet keySet;
		CharCollection values;
		ObjectSet<Char2CharMap.Entry> entrySet;
		
		SingletonMap(char key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(char key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(char key, char defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public char computeChar(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Char2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2CharMap.Entry> char2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2CharMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2CharMap {
		@Override
		public char put(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(char key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(char key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(char key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Char2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Char2CharMap.Entry> char2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2CharMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2CharMap.Entry entry) {
			super(entry.getCharKey(), entry.getCharValue());
		}
		
		@Override
		public void set(char key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2CharNavigableMap {
		Char2CharNavigableMap map;
		
		UnmodifyableNavigableMap(Char2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2CharNavigableMap descendingMap() { return Char2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2CharMap.Entry firstEntry() { return Char2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2CharMap.Entry lastEntry() { return Char2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2CharMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2CharMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2CharNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2CharNavigableMap headMap(char toKey, boolean inclusive) { return Char2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2CharNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2CharNavigableMap subMap(char fromKey, char toKey) { return Char2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2CharNavigableMap headMap(char toKey) { return Char2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2CharNavigableMap tailMap(char fromKey) { return Char2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2CharMap.Entry lowerEntry(char key) { return Char2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2CharMap.Entry higherEntry(char key) { return Char2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2CharMap.Entry floorEntry(char key) { return Char2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2CharMap.Entry ceilingEntry(char key) { return Char2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2CharNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2CharOrderedMap {
		Char2CharOrderedMap map;
		
		UnmodifyableOrderedMap(Char2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Char2CharOrderedMap copy() { return map.copy(); }
		@Override
		public CharOrderedSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return (CharOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Char2CharMap.Entry> char2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.char2CharEntrySet());
			return (ObjectOrderedSet<Char2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2CharSortedMap {
		Char2CharSortedMap map;
		
		UnmodifyableSortedMap(Char2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2CharSortedMap subMap(char fromKey, char toKey) { return Char2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2CharSortedMap headMap(char toKey) { return Char2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2CharSortedMap tailMap(char fromKey) { return Char2CharMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Char2CharSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2CharMap implements Char2CharMap {
		Char2CharMap map;
		CharCollection values;
		CharSet keys;
		ObjectSet<Char2CharMap.Entry> entrySet;
		
		UnmodifyableMap(Char2CharMap map) {
			this.map = map;
		}
		
		@Override
		public char put(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(char key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char removeOrDefault(char key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char get(char key) {
			char type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(char key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Char2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Char2CharMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Char2CharMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2CharMap.Entry> char2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Char2CharMap.Entry>
	{
		ObjectOrderedSet<Char2CharMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Char2CharMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Char2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Char2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Char2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Char2CharMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Char2CharMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Char2CharMap.Entry> iterator(Char2CharMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Char2CharMap.Entry first() { return set.first(); }
		@Override
		public Char2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Char2CharMap.Entry last() { return set.last(); }
		@Override
		public Char2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2CharMap.Entry>
	{
		ObjectSet<Char2CharMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2CharMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2CharMap.Entry> action) {
			s.forEach(T -> action.accept(Char2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2CharMap.Entry> iterator() {
			return new ObjectIterator<Char2CharMap.Entry>() {
				ObjectIterator<Char2CharMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2CharMap.Entry next() { return Char2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2CharNavigableMap {
		Char2CharNavigableMap map;
		
		SynchronizedNavigableMap(Char2CharNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2CharNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2CharNavigableMap descendingMap() { synchronized(mutex) { return Char2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public CharNavigableSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Char2CharMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2CharMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Char2CharMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2CharMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2CharNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2CharNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2CharNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2CharNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2CharNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2CharNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2CharMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2CharMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2CharMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2CharMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2CharNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2CharNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2CharMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2CharMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2CharMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2CharMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2CharOrderedMap {
		Char2CharOrderedMap map;
		
		SynchronizedOrderedMap(Char2CharOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2CharOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(char key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(char key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Char2CharOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharOrderedSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return (CharOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Char2CharMap.Entry> char2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Char2CharMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2CharSortedMap {
		Char2CharSortedMap map;
		
		SynchronizedSortedMap(Char2CharSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2CharSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2CharSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2CharSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2CharSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Char2CharSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2CharSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2CharSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2CharSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2CharMap implements Char2CharMap {
		Char2CharMap map;
		CharCollection values;
		CharSet keys;
		ObjectSet<Char2CharMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2CharMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2CharMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2CharMap setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(char key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(char key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2CharMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(char key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(char key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2CharMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2CharMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public char remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public char removeOrDefault(char key, char defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(char key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Char2CharMap m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(char key, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(char key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Char2CharMap m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(char key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharCharConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Char2CharMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2CharMap.Entry> char2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2CharEntrySet(), mutex);
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
		public Character put(Character key, Character value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Character remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Character putIfAbsent(Character key, Character value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Character oldValue, Character newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Character replace(Character key, Character value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Character compute(Character key, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfAbsent(Character key, Function<? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character computeIfPresent(Character key, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Character merge(Character key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Character> action) { synchronized(mutex) { map.forEach(action); } }
	}
}