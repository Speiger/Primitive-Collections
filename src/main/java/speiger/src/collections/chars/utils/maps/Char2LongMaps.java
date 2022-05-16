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
import speiger.src.collections.chars.functions.consumer.CharLongConsumer;
import speiger.src.collections.chars.functions.function.Char2LongFunction;
import speiger.src.collections.chars.functions.function.CharLongUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.utils.LongCollections;
import speiger.src.collections.longs.utils.LongSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Char2LongMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2LongMap.Entry> fastIterator(Char2LongMap map) {
		ObjectSet<Char2LongMap.Entry> entries = map.char2LongEntrySet();
		return entries instanceof Char2LongMap.FastEntrySet ? ((Char2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2LongMap.Entry> fastIterable(Char2LongMap map) {
		ObjectSet<Char2LongMap.Entry> entries = map.char2LongEntrySet();
		return map instanceof Char2LongMap.FastEntrySet ? new ObjectIterable<Char2LongMap.Entry>(){
			@Override
			public ObjectIterator<Char2LongMap.Entry> iterator() { return ((Char2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2LongMap.Entry> action) { ((Char2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2LongMap map, Consumer<Char2LongMap.Entry> action) {
		ObjectSet<Char2LongMap.Entry> entries = map.char2LongEntrySet();
		if(entries instanceof Char2LongMap.FastEntrySet) ((Char2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2LongMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongMap synchronize(Char2LongMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongMap synchronize(Char2LongMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongSortedMap synchronize(Char2LongSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongSortedMap synchronize(Char2LongSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongOrderedMap synchronize(Char2LongOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongOrderedMap synchronize(Char2LongOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongNavigableMap synchronize(Char2LongNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2LongNavigableMap synchronize(Char2LongNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2LongMap unmodifiable(Char2LongMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2LongOrderedMap unmodifiable(Char2LongOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2LongSortedMap unmodifiable(Char2LongSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2LongNavigableMap unmodifiable(Char2LongNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2LongMap.Entry unmodifiable(Char2LongMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2LongMap.Entry unmodifiable(Map.Entry<Character, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2LongMap singleton(char key, long value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2LongMap {
		final char key;
		final long value;
		CharSet keySet;
		LongCollection values;
		ObjectSet<Char2LongMap.Entry> entrySet;
		
		SingletonMap(char key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(char key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(char key, long defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2LongMap.Entry> char2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2LongMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2LongMap {
		@Override
		public long put(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(char key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(char key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(char key, long defaultValue) { return 0L; }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Char2LongMap.Entry> char2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2LongMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2LongMap.Entry entry) {
			super(entry.getCharKey(), entry.getLongValue());
		}
		
		@Override
		public void set(char key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2LongNavigableMap {
		Char2LongNavigableMap map;
		
		UnmodifyableNavigableMap(Char2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2LongNavigableMap descendingMap() { return Char2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2LongMap.Entry firstEntry() { return Char2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2LongMap.Entry lastEntry() { return Char2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2LongMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2LongMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2LongNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2LongNavigableMap headMap(char toKey, boolean inclusive) { return Char2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2LongNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2LongNavigableMap subMap(char fromKey, char toKey) { return Char2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2LongNavigableMap headMap(char toKey) { return Char2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2LongNavigableMap tailMap(char fromKey) { return Char2LongMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2LongMap.Entry lowerEntry(char key) { return Char2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2LongMap.Entry higherEntry(char key) { return Char2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2LongMap.Entry floorEntry(char key) { return Char2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2LongMap.Entry ceilingEntry(char key) { return Char2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2LongOrderedMap {
		Char2LongOrderedMap map;
		
		UnmodifyableOrderedMap(Char2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Char2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2LongSortedMap {
		Char2LongSortedMap map;
		
		UnmodifyableSortedMap(Char2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2LongSortedMap subMap(char fromKey, char toKey) { return Char2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2LongSortedMap headMap(char toKey) { return Char2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2LongSortedMap tailMap(char fromKey) { return Char2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Char2LongSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2LongMap implements Char2LongMap {
		Char2LongMap map;
		LongCollection values;
		CharSet keys;
		ObjectSet<Char2LongMap.Entry> entrySet;
		
		UnmodifyableMap(Char2LongMap map) {
			this.map = map;
		}
		
		@Override
		public long put(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(char key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public long removeOrDefault(char key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long get(char key) { return map.get(key); }
		@Override
		public long getOrDefault(char key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Char2LongMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2LongMap.Entry> char2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2LongMap.Entry>
	{
		ObjectSet<Char2LongMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2LongMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2LongMap.Entry> action) {
			s.forEach(T -> action.accept(Char2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2LongMap.Entry> iterator() {
			return new ObjectIterator<Char2LongMap.Entry>() {
				ObjectIterator<Char2LongMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2LongMap.Entry next() { return Char2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2LongNavigableMap {
		Char2LongNavigableMap map;
		
		SynchronizedNavigableMap(Char2LongNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2LongNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2LongNavigableMap descendingMap() { synchronized(mutex) { return Char2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Char2LongMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2LongMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2LongMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2LongMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2LongNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2LongNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2LongNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2LongNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2LongNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2LongNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2LongMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2LongMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2LongMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2LongMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Char2LongNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2LongNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2LongNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2LongNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2LongNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2LongNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2LongMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2LongMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2LongMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2LongMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2LongOrderedMap {
		Char2LongOrderedMap map;
		
		SynchronizedOrderedMap(Char2LongOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2LongOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(char key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(char key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Char2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2LongSortedMap {
		Char2LongSortedMap map;
		
		SynchronizedSortedMap(Char2LongSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2LongSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2LongSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2LongSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2LongSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Char2LongSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2LongSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2LongSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2LongSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2LongMap implements Char2LongMap {
		Char2LongMap map;
		LongCollection values;
		CharSet keys;
		ObjectSet<Char2LongMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2LongMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2LongMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2LongMap setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(char key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(char key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2LongMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(char key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(char key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2LongMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2LongMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public long remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public long removeOrDefault(char key, long defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(char key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Char2LongMap m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(CharLongUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(char key, CharLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(char key, Char2LongFunction mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(char key, CharLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(char key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Char2LongMap m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(char key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharLongConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Char2LongMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2LongMap.Entry> char2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2LongEntrySet(), mutex);
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
		public Long put(Character key, Long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Long remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Long putIfAbsent(Character key, Long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Long oldValue, Long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Long replace(Character key, Long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Long compute(Character key, BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfAbsent(Character key, Function<? super Character, ? extends Long> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long computeIfPresent(Character key, BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Long merge(Character key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Long> action) { synchronized(mutex) { map.forEach(action); } }
	}
}