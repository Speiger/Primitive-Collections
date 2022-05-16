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
import speiger.src.collections.chars.functions.consumer.CharFloatConsumer;
import speiger.src.collections.chars.functions.function.Char2FloatFunction;
import speiger.src.collections.chars.functions.function.CharFloatUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.utils.FloatCollections;
import speiger.src.collections.floats.utils.FloatSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Char2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Char2FloatMap.Entry> fastIterator(Char2FloatMap map) {
		ObjectSet<Char2FloatMap.Entry> entries = map.char2FloatEntrySet();
		return entries instanceof Char2FloatMap.FastEntrySet ? ((Char2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Char2FloatMap.Entry> fastIterable(Char2FloatMap map) {
		ObjectSet<Char2FloatMap.Entry> entries = map.char2FloatEntrySet();
		return map instanceof Char2FloatMap.FastEntrySet ? new ObjectIterable<Char2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Char2FloatMap.Entry> iterator() { return ((Char2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2FloatMap.Entry> action) { ((Char2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Char2FloatMap map, Consumer<Char2FloatMap.Entry> action) {
		ObjectSet<Char2FloatMap.Entry> entries = map.char2FloatEntrySet();
		if(entries instanceof Char2FloatMap.FastEntrySet) ((Char2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Char2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatMap synchronize(Char2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatMap synchronize(Char2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatSortedMap synchronize(Char2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatSortedMap synchronize(Char2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatOrderedMap synchronize(Char2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatOrderedMap synchronize(Char2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatNavigableMap synchronize(Char2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Char2FloatNavigableMap synchronize(Char2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Char2FloatMap unmodifiable(Char2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2FloatOrderedMap unmodifiable(Char2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Char2FloatSortedMap unmodifiable(Char2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Char2FloatNavigableMap unmodifiable(Char2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2FloatMap.Entry unmodifiable(Char2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Char2FloatMap.Entry unmodifiable(Map.Entry<Character, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Char2FloatMap singleton(char key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractChar2FloatMap {
		final char key;
		final float value;
		CharSet keySet;
		FloatCollection values;
		ObjectSet<Char2FloatMap.Entry> entrySet;
		
		SingletonMap(char key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(char key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(char key, float defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2FloatMap.Entry> char2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractChar2FloatMap {
		@Override
		public float put(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(char key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(char key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(char key, float defaultValue) { return 0F; }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Char2FloatMap.Entry> char2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractChar2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Character, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2FloatMap.Entry entry) {
			super(entry.getCharKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(char key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Char2FloatNavigableMap {
		Char2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Char2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2FloatNavigableMap descendingMap() { return Char2FloatMaps.synchronize(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2FloatMap.Entry firstEntry() { return Char2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2FloatMap.Entry lastEntry() { return Char2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2FloatNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2FloatNavigableMap headMap(char toKey, boolean inclusive) { return Char2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2FloatNavigableMap tailMap(char fromKey, boolean inclusive) { return Char2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2FloatNavigableMap subMap(char fromKey, char toKey) { return Char2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2FloatNavigableMap headMap(char toKey) { return Char2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2FloatNavigableMap tailMap(char fromKey) { return Char2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2FloatMap.Entry lowerEntry(char key) { return Char2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2FloatMap.Entry higherEntry(char key) { return Char2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2FloatMap.Entry floorEntry(char key) { return Char2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2FloatMap.Entry ceilingEntry(char key) { return Char2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Char2FloatOrderedMap {
		Char2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Char2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Char2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Char2FloatSortedMap {
		Char2FloatSortedMap map;
		
		UnmodifyableSortedMap(Char2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2FloatSortedMap subMap(char fromKey, char toKey) { return Char2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2FloatSortedMap headMap(char toKey) { return Char2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2FloatSortedMap tailMap(char fromKey) { return Char2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollLastCharKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Char2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractChar2FloatMap implements Char2FloatMap {
		Char2FloatMap map;
		FloatCollection values;
		CharSet keys;
		ObjectSet<Char2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Char2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(char key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(char key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(char key) { return map.get(key); }
		@Override
		public float getOrDefault(char key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Char2FloatMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2FloatMap.Entry> char2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.char2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Char2FloatMap.Entry>
	{
		ObjectSet<Char2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Char2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2FloatMap.Entry> iterator() {
			return new ObjectIterator<Char2FloatMap.Entry>() {
				ObjectIterator<Char2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2FloatMap.Entry next() { return Char2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Char2FloatNavigableMap {
		Char2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Char2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2FloatNavigableMap descendingMap() { synchronized(mutex) { return Char2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Char2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2FloatNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2FloatNavigableMap headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2FloatNavigableMap tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2FloatNavigableMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2FloatNavigableMap headMap(char toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2FloatNavigableMap tailMap(char fromKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2FloatMap.Entry lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2FloatMap.Entry higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2FloatMap.Entry floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2FloatMap.Entry ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2FloatNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Char2FloatNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2FloatNavigableMap headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2FloatNavigableMap tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2FloatNavigableMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2FloatNavigableMap headMap(Character toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2FloatNavigableMap tailMap(Character fromKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2FloatMap.Entry lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2FloatMap.Entry floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2FloatMap.Entry ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2FloatMap.Entry higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Char2FloatOrderedMap {
		Char2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Char2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(char key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(char key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Char2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Char2FloatSortedMap {
		Char2FloatSortedMap map;
		
		SynchronizedSortedMap(Char2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2FloatSortedMap subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2FloatSortedMap headMap(char toKey)  { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2FloatSortedMap tailMap(char fromKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Char2FloatSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2FloatSortedMap subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2FloatSortedMap headMap(Character toKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2FloatSortedMap tailMap(Character fromKey) { synchronized(mutex) { return Char2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractChar2FloatMap implements Char2FloatMap {
		Char2FloatMap map;
		FloatCollection values;
		CharSet keys;
		ObjectSet<Char2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(char key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(char key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(char key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(char key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Char2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Char2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(char key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(char key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Char2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(CharFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(char key, CharFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(char key, Char2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(char key, CharFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float mergeFloat(char key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Char2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(char key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Char2FloatMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2FloatMap.Entry> char2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2FloatEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Float get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Float getOrDefault(Object key, Float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Float put(Character key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Character key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Character key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Character key, Function<? super Character, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Character key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}