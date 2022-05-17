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
import speiger.src.collections.chars.functions.consumer.CharIntConsumer;
import speiger.src.collections.chars.functions.function.Char2IntFunction;
import speiger.src.collections.chars.functions.function.CharIntUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.ints.utils.IntSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2IntMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2IntMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2IntMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2IntMap.Entry> fastIterator(Char2IntMap map) {
		ObjectSet<Char2IntMap.Entry> entries = map.char2IntEntrySet();
		return entries instanceof Char2IntMap.FastEntrySet ? ((Char2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2IntMap.Entry> fastIterable(Char2IntMap map) {
		ObjectSet<Char2IntMap.Entry> entries = map.char2IntEntrySet();
		return map instanceof Char2IntMap.FastEntrySet ? new ObjectIterable<Char2IntMap.Entry>(){
			@Override
			public ObjectIterator<Char2IntMap.Entry> iterator() { return ((Char2IntMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2IntMap.Entry> action) { ((Char2IntMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2IntMap map, Consumer<Char2IntMap.Entry> action) {
		ObjectSet<Char2IntMap.Entry> entries = map.char2IntEntrySet();
		if(entries instanceof Char2IntMap.FastEntrySet) ((Char2IntMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntMap synchronize(Char2IntMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntMap synchronize(Char2IntMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntSortedMap synchronize(Char2IntSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntSortedMap synchronize(Char2IntSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntOrderedMap synchronize(Char2IntOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntOrderedMap synchronize(Char2IntOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntNavigableMap synchronize(Char2IntNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2IntNavigableMap synchronize(Char2IntNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2IntMap unmodifiable(Char2IntMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2IntOrderedMap unmodifiable(Char2IntOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2IntSortedMap unmodifiable(Char2IntSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2IntNavigableMap unmodifiable(Char2IntNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2IntMap.Entry unmodifiable(Char2IntMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2IntMap.Entry unmodifiable(Map.Entry<Character, Integer> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2IntMap singleton(char key, int value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2IntMap {
		final char key;
		final int value;
		CharSet keySet;
		IntCollection values;
		ObjectSet<Char2IntMap.Entry> entrySet;
		
		SingletonMap(char key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int put(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(char key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public int getOrDefault(char key, int defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public IntCollection values() { 
			if(values == null) values = IntSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2IntMap.Entry> char2IntEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2IntMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2IntMap {
		@Override
		public int put(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int addTo(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(char key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(char key) { return getDefaultReturnValue(); }
		@Override
		public int getOrDefault(char key, int defaultValue) { return 0; }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public IntCollection values() { return IntCollections.empty(); }
		@Override
		public ObjectSet<Char2IntMap.Entry> char2IntEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2IntMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Integer> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2IntMap.Entry entry) {
			super(entry.getCharKey(), entry.getIntValue());
		}
		
		@Override
		public void set(char key, int value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2IntNavigableMap {
		Char2IntNavigableMap map;
		
		UnmodifyableNavigableMap(Char2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2IntNavigableMap descendingMap() { return Char2IntMaps.synchronize(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2IntMap.Entry firstEntry() { return Char2IntMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2IntMap.Entry lastEntry() { return Char2IntMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2IntMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2IntMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2IntNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2IntMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2IntNavigableMap headMap(char toKey, boolean inclusive) { return Char2IntMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2IntNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2IntMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2IntNavigableMap subMap(char fromKey, char toKey) { return Char2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2IntNavigableMap headMap(char toKey) { return Char2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2IntNavigableMap tailMap(char fromKey) { return Char2IntMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2IntMap.Entry lowerEntry(char key) { return Char2IntMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2IntMap.Entry higherEntry(char key) { return Char2IntMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2IntMap.Entry floorEntry(char key) { return Char2IntMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2IntMap.Entry ceilingEntry(char key) { return Char2IntMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2IntOrderedMap {
		Char2IntOrderedMap map;
		
		UnmodifyableOrderedMap(Char2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putAndMoveToLast(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public int getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Char2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2IntSortedMap {
		Char2IntSortedMap map;
		
		UnmodifyableSortedMap(Char2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2IntSortedMap subMap(char fromKey, char toKey) { return Char2IntMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2IntSortedMap headMap(char toKey) { return Char2IntMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2IntSortedMap tailMap(char fromKey) { return Char2IntMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public int firstIntValue() { return map.firstIntValue(); }
		@Override
		public int lastIntValue() { return map.lastIntValue(); }
		@Override
		public Char2IntSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2IntMap implements Char2IntMap {
		Char2IntMap map;
		IntCollection values;
		CharSet keys;
		ObjectSet<Char2IntMap.Entry> entrySet;
		
		UnmodifyableMap(Char2IntMap map) {
			this.map = map;
		}
		
		@Override
		public int put(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int putIfAbsent(char key, int value){ throw new UnsupportedOperationException(); }
		@Override
		public int addTo(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int subFrom(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public int removeOrDefault(char key, int defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, int value) { throw new UnsupportedOperationException(); }
		@Override
		public int get(char key) { return map.get(key); }
		@Override
		public int getOrDefault(char key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Char2IntMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2IntMap.Entry> char2IntEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2IntEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2IntMap.Entry>
	{
		ObjectSet<Char2IntMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2IntMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2IntMap.Entry> action) {
			s.forEach(T -> action.accept(Char2IntMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2IntMap.Entry> iterator() {
			return new ObjectIterator<Char2IntMap.Entry>() {
				ObjectIterator<Char2IntMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2IntMap.Entry next() { return Char2IntMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2IntNavigableMap {
		Char2IntNavigableMap map;
		
		SynchronizedNavigableMap(Char2IntNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2IntNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2IntNavigableMap descendingMap() { synchronized(mutex) { return Char2IntMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Char2IntMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2IntMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2IntMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2IntMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2IntNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2IntNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2IntNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2IntNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2IntNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2IntNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2IntMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2IntMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2IntMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2IntMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Char2IntNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2IntNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2IntNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2IntNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2IntNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2IntNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2IntMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2IntMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2IntMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2IntMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2IntOrderedMap {
		Char2IntOrderedMap map;
		
		SynchronizedOrderedMap(Char2IntOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2IntOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public int putAndMoveToFirst(char key, int value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public int putAndMoveToLast(char key, int value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public int getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public int getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Char2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2IntSortedMap {
		Char2IntSortedMap map;
		
		SynchronizedSortedMap(Char2IntSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2IntSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2IntSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2IntSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2IntSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public int firstIntValue() { synchronized(mutex) { return map.firstIntValue(); } }
		@Override
		public int lastIntValue() { synchronized(mutex) { return map.lastIntValue(); } }
		@Override
		public Char2IntSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2IntSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2IntSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2IntSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2IntMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2IntMap implements Char2IntMap {
		Char2IntMap map;
		IntCollection values;
		CharSet keys;
		ObjectSet<Char2IntMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2IntMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2IntMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public int getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2IntMap setDefaultReturnValue(int v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public int put(char key, int value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public int putIfAbsent(char key, int value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2IntMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public int addTo(char key, int value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public int subFrom(char key, int value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2IntMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2IntMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Integer> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, int[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(int value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public int get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public int remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public int removeOrDefault(char key, int defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, int value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, int oldValue, int newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public int replace(char key, int value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceInts(Char2IntMap m) { synchronized(mutex) { map.replaceInts(m); } }
		@Override
		public void replaceInts(CharIntUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceInts(mappingFunction); } }
		@Override
		public int computeInt(char key, CharIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeInt(key, mappingFunction); } }
		@Override
		public int computeIntIfAbsent(char key, Char2IntFunction mappingFunction) { synchronized(mutex) { return map.computeIntIfAbsent(key, mappingFunction); } }
		@Override
		public int computeIntIfPresent(char key, CharIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeIntIfPresent(key, mappingFunction); } }
		@Override
		public int mergeInt(char key, int value, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeInt(key, value, mappingFunction); } }
		@Override
		public void mergeAllInt(Char2IntMap m, IntIntUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllInt(m, mappingFunction); } }
		@Override
		public int getOrDefault(char key, int defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharIntConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Char2IntMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public IntCollection values() {
			if(values == null) values = IntCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2IntMap.Entry> char2IntEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2IntEntrySet(), mutex);
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
		public Integer put(Character key, Integer value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Integer remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Integer putIfAbsent(Character key, Integer value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Integer oldValue, Integer newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Integer replace(Character key, Integer value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Integer compute(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfAbsent(Character key, Function<? super Character, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer computeIfPresent(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Integer merge(Character key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Integer> action) { synchronized(mutex) { map.forEach(action); } }
	}
}