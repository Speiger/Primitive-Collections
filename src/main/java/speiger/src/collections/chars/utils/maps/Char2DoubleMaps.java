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
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.consumer.CharDoubleConsumer;
import speiger.src.collections.chars.functions.function.Char2DoubleFunction;
import speiger.src.collections.chars.functions.function.CharDoubleUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleCollections;
import speiger.src.collections.doubles.utils.DoubleSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Char2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2DoubleMap.Entry> fastIterator(Char2DoubleMap map) {
		ObjectSet<Char2DoubleMap.Entry> entries = map.char2DoubleEntrySet();
		return entries instanceof Char2DoubleMap.FastEntrySet ? ((Char2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2DoubleMap.Entry> fastIterable(Char2DoubleMap map) {
		ObjectSet<Char2DoubleMap.Entry> entries = map.char2DoubleEntrySet();
		return map instanceof Char2DoubleMap.FastEntrySet ? new ObjectIterable<Char2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Char2DoubleMap.Entry> iterator() { return ((Char2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2DoubleMap.Entry> action) { ((Char2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2DoubleMap map, Consumer<Char2DoubleMap.Entry> action) {
		ObjectSet<Char2DoubleMap.Entry> entries = map.char2DoubleEntrySet();
		if(entries instanceof Char2DoubleMap.FastEntrySet) ((Char2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleMap synchronize(Char2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleMap synchronize(Char2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleSortedMap synchronize(Char2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleSortedMap synchronize(Char2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleOrderedMap synchronize(Char2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleOrderedMap synchronize(Char2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleNavigableMap synchronize(Char2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2DoubleNavigableMap synchronize(Char2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2DoubleMap unmodifiable(Char2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2DoubleOrderedMap unmodifiable(Char2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2DoubleSortedMap unmodifiable(Char2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2DoubleNavigableMap unmodifiable(Char2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2DoubleMap.Entry unmodifiable(Char2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2DoubleMap.Entry unmodifiable(Map.Entry<Character, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2DoubleMap singleton(char key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2DoubleMap {
		final char key;
		final double value;
		CharSet keySet;
		DoubleCollection values;
		ObjectSet<Char2DoubleMap.Entry> entrySet;
		
		SingletonMap(char key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(char key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(char key, double defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2DoubleMap.Entry> char2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2DoubleMap {
		@Override
		public double put(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(char key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(char key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(char key, double defaultValue) { return 0D; }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Char2DoubleMap.Entry> char2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2DoubleMap.Entry entry) {
			super(entry.getCharKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(char key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2DoubleNavigableMap {
		Char2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Char2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2DoubleNavigableMap descendingMap() { return Char2DoubleMaps.synchronize(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2DoubleMap.Entry firstEntry() { return Char2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2DoubleMap.Entry lastEntry() { return Char2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2DoubleNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2DoubleNavigableMap headMap(char toKey, boolean inclusive) { return Char2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2DoubleNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2DoubleNavigableMap subMap(char fromKey, char toKey) { return Char2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2DoubleNavigableMap headMap(char toKey) { return Char2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2DoubleNavigableMap tailMap(char fromKey) { return Char2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2DoubleMap.Entry lowerEntry(char key) { return Char2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2DoubleMap.Entry higherEntry(char key) { return Char2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2DoubleMap.Entry floorEntry(char key) { return Char2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2DoubleMap.Entry ceilingEntry(char key) { return Char2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2DoubleOrderedMap {
		Char2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Char2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Char2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2DoubleSortedMap {
		Char2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Char2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2DoubleSortedMap subMap(char fromKey, char toKey) { return Char2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2DoubleSortedMap headMap(char toKey) { return Char2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2DoubleSortedMap tailMap(char fromKey) { return Char2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Char2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2DoubleMap implements Char2DoubleMap {
		Char2DoubleMap map;
		DoubleCollection values;
		CharSet keys;
		ObjectSet<Char2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Char2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(char key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(char key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(char key) { return map.get(key); }
		@Override
		public double getOrDefault(char key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Char2DoubleMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2DoubleMap.Entry>
	{
		ObjectSet<Char2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Char2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Char2DoubleMap.Entry>() {
				ObjectIterator<Char2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2DoubleMap.Entry next() { return Char2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2DoubleNavigableMap {
		Char2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Char2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Char2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2DoubleNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2DoubleNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2DoubleNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2DoubleNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2DoubleNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2DoubleNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2DoubleMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2DoubleMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2DoubleMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2DoubleMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2DoubleMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2DoubleMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2DoubleMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2DoubleMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2DoubleOrderedMap {
		Char2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Char2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(char key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(char key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Char2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2DoubleSortedMap {
		Char2DoubleSortedMap map;
		
		SynchronizedSortedMap(Char2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2DoubleSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2DoubleSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2DoubleSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Char2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2DoubleSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2DoubleSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2DoubleMap implements Char2DoubleMap {
		Char2DoubleMap map;
		DoubleCollection values;
		CharSet keys;
		ObjectSet<Char2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(char key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(char key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(char key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(char key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(char key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(char key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Char2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(CharDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(char key, CharDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(char key, Char2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(char key, CharDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double mergeDouble(char key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Char2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(char key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Char2DoubleMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2DoubleEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Double get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Double getOrDefault(Object key, Double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Double put(Character key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Character key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Character key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Character key, BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Character key, Function<? super Character, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Character key, BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Character key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}