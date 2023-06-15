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
import speiger.src.collections.chars.functions.consumer.CharBooleanConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.function.CharBooleanUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.utils.BooleanCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2BooleanMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2BooleanMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2BooleanMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2BooleanMap.Entry> fastIterator(Char2BooleanMap map) {
		ObjectSet<Char2BooleanMap.Entry> entries = map.char2BooleanEntrySet();
		return entries instanceof Char2BooleanMap.FastEntrySet ? ((Char2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2BooleanMap.Entry> fastIterable(Char2BooleanMap map) {
		ObjectSet<Char2BooleanMap.Entry> entries = map.char2BooleanEntrySet();
		return map instanceof Char2BooleanMap.FastEntrySet ? new ObjectIterable<Char2BooleanMap.Entry>(){
			@Override
			public ObjectIterator<Char2BooleanMap.Entry> iterator() { return ((Char2BooleanMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2BooleanMap.Entry> action) { ((Char2BooleanMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2BooleanMap map, Consumer<Char2BooleanMap.Entry> action) {
		ObjectSet<Char2BooleanMap.Entry> entries = map.char2BooleanEntrySet();
		if(entries instanceof Char2BooleanMap.FastEntrySet) ((Char2BooleanMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanMap synchronize(Char2BooleanMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanMap synchronize(Char2BooleanMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanSortedMap synchronize(Char2BooleanSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanSortedMap synchronize(Char2BooleanSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanOrderedMap synchronize(Char2BooleanOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanOrderedMap synchronize(Char2BooleanOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanNavigableMap synchronize(Char2BooleanNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2BooleanNavigableMap synchronize(Char2BooleanNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2BooleanMap unmodifiable(Char2BooleanMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2BooleanOrderedMap unmodifiable(Char2BooleanOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2BooleanSortedMap unmodifiable(Char2BooleanSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2BooleanNavigableMap unmodifiable(Char2BooleanNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2BooleanMap.Entry unmodifiable(Char2BooleanMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2BooleanMap.Entry unmodifiable(Map.Entry<Character, Boolean> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2BooleanMap singleton(char key, boolean value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2BooleanMap {
		final char key;
		final boolean value;
		CharSet keySet;
		BooleanCollection values;
		ObjectSet<Char2BooleanMap.Entry> entrySet;
		
		SingletonMap(char key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean put(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(char key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(char key, boolean defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public boolean computeBoolean(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(char key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Char2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public BooleanCollection values() { 
			if(values == null) values = BooleanCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2BooleanMap.Entry> char2BooleanEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2BooleanMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2BooleanMap {
		@Override
		public boolean put(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(char key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(char key) { return getDefaultReturnValue(); }
		@Override
		public boolean getOrDefault(char key, boolean defaultValue) { return defaultValue; }
		@Override
		public boolean computeBoolean(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(char key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Char2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public BooleanCollection values() { return BooleanCollections.empty(); }
		@Override
		public ObjectSet<Char2BooleanMap.Entry> char2BooleanEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2BooleanMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Boolean> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2BooleanMap.Entry entry) {
			super(entry.getCharKey(), entry.getBooleanValue());
		}
		
		@Override
		public void set(char key, boolean value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2BooleanNavigableMap {
		Char2BooleanNavigableMap map;
		
		UnmodifyableNavigableMap(Char2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2BooleanNavigableMap descendingMap() { return Char2BooleanMaps.unmodifiable(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2BooleanMap.Entry firstEntry() { return Char2BooleanMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2BooleanMap.Entry lastEntry() { return Char2BooleanMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2BooleanMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2BooleanMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2BooleanNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2BooleanMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2BooleanNavigableMap headMap(char toKey, boolean inclusive) { return Char2BooleanMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2BooleanNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2BooleanMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2BooleanNavigableMap subMap(char fromKey, char toKey) { return Char2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2BooleanNavigableMap headMap(char toKey) { return Char2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2BooleanNavigableMap tailMap(char fromKey) { return Char2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2BooleanMap.Entry lowerEntry(char key) { return Char2BooleanMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2BooleanMap.Entry higherEntry(char key) { return Char2BooleanMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2BooleanMap.Entry floorEntry(char key) { return Char2BooleanMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2BooleanMap.Entry ceilingEntry(char key) { return Char2BooleanMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2BooleanNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2BooleanOrderedMap {
		Char2BooleanOrderedMap map;
		
		UnmodifyableOrderedMap(Char2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putAndMoveToLast(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Char2BooleanOrderedMap copy() { return map.copy(); }
		@Override
		public CharOrderedSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return (CharOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.char2BooleanEntrySet());
			return (ObjectOrderedSet<Char2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2BooleanSortedMap {
		Char2BooleanSortedMap map;
		
		UnmodifyableSortedMap(Char2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2BooleanSortedMap subMap(char fromKey, char toKey) { return Char2BooleanMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2BooleanSortedMap headMap(char toKey) { return Char2BooleanMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2BooleanSortedMap tailMap(char fromKey) { return Char2BooleanMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public boolean firstBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public Char2BooleanSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2BooleanMap implements Char2BooleanMap {
		Char2BooleanMap map;
		BooleanCollection values;
		CharSet keys;
		ObjectSet<Char2BooleanMap.Entry> entrySet;
		
		UnmodifyableMap(Char2BooleanMap map) {
			this.map = map;
		}
		
		@Override
		public boolean put(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean putIfAbsent(char key, boolean value){ throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeOrDefault(char key, boolean defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, boolean value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean get(char key) {
			boolean type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public boolean getOrDefault(char key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean computeBoolean(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsent(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(char key, CharPredicate mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresent(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsent(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(char key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public boolean mergeBoolean(char key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllBoolean(Char2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(CharBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBooleans(Char2BooleanMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Char2BooleanMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2BooleanEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Char2BooleanMap.Entry>
	{
		ObjectOrderedSet<Char2BooleanMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Char2BooleanMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Char2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Char2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Char2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Char2BooleanMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Char2BooleanMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Char2BooleanMap.Entry> iterator(Char2BooleanMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Char2BooleanMap.Entry first() { return set.first(); }
		@Override
		public Char2BooleanMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Char2BooleanMap.Entry last() { return set.last(); }
		@Override
		public Char2BooleanMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2BooleanMap.Entry>
	{
		ObjectSet<Char2BooleanMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2BooleanMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2BooleanMap.Entry> action) {
			s.forEach(T -> action.accept(Char2BooleanMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2BooleanMap.Entry> iterator() {
			return new ObjectIterator<Char2BooleanMap.Entry>() {
				ObjectIterator<Char2BooleanMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2BooleanMap.Entry next() { return Char2BooleanMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2BooleanNavigableMap {
		Char2BooleanNavigableMap map;
		
		SynchronizedNavigableMap(Char2BooleanNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2BooleanNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2BooleanNavigableMap descendingMap() { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public CharNavigableSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Char2BooleanMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2BooleanMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Char2BooleanMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2BooleanMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2BooleanNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2BooleanNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2BooleanNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2BooleanNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2BooleanNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2BooleanNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2BooleanMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2BooleanMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2BooleanMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2BooleanMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2BooleanNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2BooleanMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2BooleanMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2BooleanMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2BooleanMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2BooleanOrderedMap {
		Char2BooleanOrderedMap map;
		
		SynchronizedOrderedMap(Char2BooleanOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2BooleanOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public boolean putAndMoveToFirst(char key, boolean value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public boolean putAndMoveToLast(char key, boolean value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public boolean getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public boolean getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Char2BooleanOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharOrderedSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return (CharOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2BooleanEntrySet(), mutex);
			return (ObjectOrderedSet<Char2BooleanMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2BooleanSortedMap {
		Char2BooleanSortedMap map;
		
		SynchronizedSortedMap(Char2BooleanSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2BooleanSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2BooleanSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2BooleanSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2BooleanSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public boolean firstBooleanValue() { synchronized(mutex) { return map.firstBooleanValue(); } }
		@Override
		public boolean lastBooleanValue() { synchronized(mutex) { return map.lastBooleanValue(); } }
		@Override
		public Char2BooleanSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2BooleanSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2BooleanSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2BooleanMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2BooleanMap implements Char2BooleanMap {
		Char2BooleanMap map;
		BooleanCollection values;
		CharSet keys;
		ObjectSet<Char2BooleanMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2BooleanMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2BooleanMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public boolean getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2BooleanMap setDefaultReturnValue(boolean v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public boolean put(char key, boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public boolean putIfAbsent(char key, boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2BooleanMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Char2BooleanMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Boolean> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, boolean[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(boolean value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public boolean get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public boolean remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public boolean removeOrDefault(char key, boolean defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, boolean value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, boolean oldValue, boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public boolean replace(char key, boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBooleans(Char2BooleanMap m) { synchronized(mutex) { map.replaceBooleans(m); } }
		@Override
		public void replaceBooleans(CharBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBooleans(mappingFunction); } }
		@Override
		public boolean computeBoolean(char key, CharBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBoolean(key, mappingFunction); } }
		@Override
		public boolean computeBooleanNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsent(char key, CharPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(char key, CharPredicate mappingFunction) { synchronized(mutex) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresent(char key, CharBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresent(key, mappingFunction); } }
		@Override
		public boolean computeBooleanIfPresentNonDefault(char key, CharBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public boolean supplyBooleanIfAbsent(char key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsent(key, valueProvider); } }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(char key, BooleanSupplier valueProvider) { synchronized(mutex) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public boolean mergeBoolean(char key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeBoolean(key, value, mappingFunction); } }
		@Override
		public void mergeAllBoolean(Char2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllBoolean(m, mappingFunction); } }
		@Override
		public boolean getOrDefault(char key, boolean defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharBooleanConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Char2BooleanMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public BooleanCollection values() {
			if(values == null) values = BooleanCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2BooleanEntrySet(), mutex);
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
		public Boolean put(Character key, Boolean value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Boolean remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Boolean putIfAbsent(Character key, Boolean value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Boolean oldValue, Boolean newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Boolean replace(Character key, Boolean value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Boolean compute(Character key, BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfAbsent(Character key, Function<? super Character, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean computeIfPresent(Character key, BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Boolean merge(Character key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Boolean> action) { synchronized(mutex) { map.forEach(action); } }
	}
}