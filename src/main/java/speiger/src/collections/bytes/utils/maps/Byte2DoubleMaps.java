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
import speiger.src.collections.bytes.functions.consumer.ByteDoubleConsumer;
import speiger.src.collections.bytes.functions.function.Byte2DoubleFunction;
import speiger.src.collections.bytes.functions.function.ByteDoubleUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleCollections;
import speiger.src.collections.doubles.utils.DoubleSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2DoubleMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2DoubleMap EMPTY = new EmptyMap();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2DoubleMap.Entry> fastIterator(Byte2DoubleMap map) {
		ObjectSet<Byte2DoubleMap.Entry> entries = map.byte2DoubleEntrySet();
		return entries instanceof Byte2DoubleMap.FastEntrySet ? ((Byte2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2DoubleMap.Entry> fastIterable(Byte2DoubleMap map) {
		ObjectSet<Byte2DoubleMap.Entry> entries = map.byte2DoubleEntrySet();
		return map instanceof Byte2DoubleMap.FastEntrySet ? new ObjectIterable<Byte2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Byte2DoubleMap.Entry> iterator() { return ((Byte2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2DoubleMap.Entry> action) { ((Byte2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2DoubleMap map, Consumer<Byte2DoubleMap.Entry> action) {
		ObjectSet<Byte2DoubleMap.Entry> entries = map.byte2DoubleEntrySet();
		if(entries instanceof Byte2DoubleMap.FastEntrySet) ((Byte2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2DoubleMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleMap synchronize(Byte2DoubleMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleMap synchronize(Byte2DoubleMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleSortedMap synchronize(Byte2DoubleSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleSortedMap synchronize(Byte2DoubleSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleOrderedMap synchronize(Byte2DoubleOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleOrderedMap synchronize(Byte2DoubleOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleNavigableMap synchronize(Byte2DoubleNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2DoubleNavigableMap synchronize(Byte2DoubleNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2DoubleMap unmodifiable(Byte2DoubleMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2DoubleOrderedMap unmodifiable(Byte2DoubleOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2DoubleSortedMap unmodifiable(Byte2DoubleSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2DoubleNavigableMap unmodifiable(Byte2DoubleNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2DoubleMap.Entry unmodifiable(Byte2DoubleMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2DoubleMap.Entry unmodifiable(Map.Entry<Byte, Double> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : new UnmodifyableEntry(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2DoubleMap singleton(byte key, double value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2DoubleMap {
		final byte key;
		final double value;
		ByteSet keySet;
		DoubleCollection values;
		ObjectSet<Byte2DoubleMap.Entry> entrySet;
		
		SingletonMap(byte key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double put(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(byte key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public double getOrDefault(byte key, double defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public DoubleCollection values() { 
			if(values == null) values = DoubleSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2DoubleMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2DoubleMap {
		@Override
		public double put(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double addTo(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(byte key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(byte key) { return getDefaultReturnValue(); }
		@Override
		public double getOrDefault(byte key, double defaultValue) { return 0D; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public DoubleCollection values() { return DoubleCollections.empty(); }
		@Override
		public ObjectSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2DoubleMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Double> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2DoubleMap.Entry entry) {
			super(entry.getByteKey(), entry.getDoubleValue());
		}
		
		@Override
		public void set(byte key, double value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2DoubleNavigableMap {
		Byte2DoubleNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2DoubleNavigableMap descendingMap() { return Byte2DoubleMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2DoubleMap.Entry firstEntry() { return Byte2DoubleMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2DoubleMap.Entry lastEntry() { return Byte2DoubleMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2DoubleMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2DoubleMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2DoubleNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2DoubleMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2DoubleNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2DoubleMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2DoubleNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2DoubleMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2DoubleNavigableMap subMap(byte fromKey, byte toKey) { return Byte2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2DoubleNavigableMap headMap(byte toKey) { return Byte2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2DoubleNavigableMap tailMap(byte fromKey) { return Byte2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2DoubleMap.Entry lowerEntry(byte key) { return Byte2DoubleMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2DoubleMap.Entry higherEntry(byte key) { return Byte2DoubleMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2DoubleMap.Entry floorEntry(byte key) { return Byte2DoubleMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2DoubleMap.Entry ceilingEntry(byte key) { return Byte2DoubleMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2DoubleOrderedMap {
		Byte2DoubleOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putAndMoveToLast(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public double getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Byte2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2DoubleSortedMap {
		Byte2DoubleSortedMap map;
		
		UnmodifyableSortedMap(Byte2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2DoubleSortedMap subMap(byte fromKey, byte toKey) { return Byte2DoubleMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2DoubleSortedMap headMap(byte toKey) { return Byte2DoubleMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2DoubleSortedMap tailMap(byte fromKey) { return Byte2DoubleMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public double firstDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public Byte2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2DoubleMap implements Byte2DoubleMap {
		Byte2DoubleMap map;
		DoubleCollection values;
		ByteSet keys;
		ObjectSet<Byte2DoubleMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2DoubleMap map) {
			this.map = map;
		}
		
		@Override
		public double put(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double putIfAbsent(byte key, double value){ throw new UnsupportedOperationException(); }
		@Override
		public double addTo(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double subFrom(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public double removeOrDefault(byte key, double defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, double value) { throw new UnsupportedOperationException(); }
		@Override
		public double get(byte key) { return map.get(key); }
		@Override
		public double getOrDefault(byte key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2DoubleMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2DoubleEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2DoubleMap.Entry>
	{
		ObjectSet<Byte2DoubleMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2DoubleMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2DoubleMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2DoubleMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2DoubleMap.Entry> iterator() {
			return new ObjectIterator<Byte2DoubleMap.Entry>() {
				ObjectIterator<Byte2DoubleMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2DoubleMap.Entry next() { return Byte2DoubleMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2DoubleNavigableMap {
		Byte2DoubleNavigableMap map;
		
		SynchronizedNavigableMap(Byte2DoubleNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2DoubleNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2DoubleNavigableMap descendingMap() { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2DoubleMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2DoubleMap.Entry lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2DoubleMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2DoubleMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2DoubleNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2DoubleNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2DoubleNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2DoubleNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2DoubleNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2DoubleNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2DoubleMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2DoubleMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2DoubleMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2DoubleMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2DoubleNavigableMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2DoubleMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2DoubleMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2DoubleMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2DoubleMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2DoubleOrderedMap {
		Byte2DoubleOrderedMap map;
		
		SynchronizedOrderedMap(Byte2DoubleOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2DoubleOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public double putAndMoveToFirst(byte key, double value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public double putAndMoveToLast(byte key, double value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public double getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public double getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Byte2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2DoubleSortedMap {
		Byte2DoubleSortedMap map;
		
		SynchronizedSortedMap(Byte2DoubleSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2DoubleSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2DoubleSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2DoubleSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2DoubleSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public double firstDoubleValue() { synchronized(mutex) { return map.firstDoubleValue(); } }
		@Override
		public double lastDoubleValue() { synchronized(mutex) { return map.lastDoubleValue(); } }
		@Override
		public Byte2DoubleSortedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2DoubleSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2DoubleSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2DoubleMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2DoubleMap implements Byte2DoubleMap {
		Byte2DoubleMap map;
		DoubleCollection values;
		ByteSet keys;
		ObjectSet<Byte2DoubleMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2DoubleMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2DoubleMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public double getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2DoubleMap setDefaultReturnValue(double v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public double put(byte key, double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public double putIfAbsent(byte key, double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2DoubleMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public double addTo(byte key, double value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public double subFrom(byte key, double value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2DoubleMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2DoubleMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Double> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, double[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(double value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public double get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public double remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public double removeOrDefault(byte key, double defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, double value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, double oldValue, double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public double replace(byte key, double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceDoubles(Byte2DoubleMap m) { synchronized(mutex) { map.replaceDoubles(m); } }
		@Override
		public void replaceDoubles(ByteDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceDoubles(mappingFunction); } }
		@Override
		public double computeDouble(byte key, ByteDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDouble(key, mappingFunction); } }
		@Override
		public double computeDoubleIfAbsent(byte key, Byte2DoubleFunction mappingFunction) { synchronized(mutex) { return map.computeDoubleIfAbsent(key, mappingFunction); } }
		@Override
		public double computeDoubleIfPresent(byte key, ByteDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeDoubleIfPresent(key, mappingFunction); } }
		@Override
		public double mergeDouble(byte key, double value, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeDouble(key, value, mappingFunction); } }
		@Override
		public void mergeAllDouble(Byte2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllDouble(m, mappingFunction); } }
		@Override
		public double getOrDefault(byte key, double defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteDoubleConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2DoubleMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public DoubleCollection values() {
			if(values == null) values = DoubleCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2DoubleEntrySet(), mutex);
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
		public Double put(Byte key, Double value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Double remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Double putIfAbsent(Byte key, Double value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Double oldValue, Double newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Double replace(Byte key, Double value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Double compute(Byte key, BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfAbsent(Byte key, Function<? super Byte, ? extends Double> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double computeIfPresent(Byte key, BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Double merge(Byte key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Double> action) { synchronized(mutex) { map.forEach(action); } }
	}
}