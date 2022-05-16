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
import speiger.src.collections.bytes.functions.consumer.ByteObjectConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ObjectFunction;
import speiger.src.collections.bytes.functions.function.ByteObjectUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	public static final Byte2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Byte2ObjectMap.Entry<V>> fastIterator(Byte2ObjectMap<V> map) {
		ObjectSet<Byte2ObjectMap.Entry<V>> entries = map.byte2ObjectEntrySet();
		return entries instanceof Byte2ObjectMap.FastEntrySet ? ((Byte2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Byte2ObjectMap.Entry<V>> fastIterable(Byte2ObjectMap<V> map) {
		ObjectSet<Byte2ObjectMap.Entry<V>> entries = map.byte2ObjectEntrySet();
		return map instanceof Byte2ObjectMap.FastEntrySet ? new ObjectIterable<Byte2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Byte2ObjectMap.Entry<V>> iterator() { return ((Byte2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2ObjectMap.Entry<V>> action) { ((Byte2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Byte2ObjectMap<V> map, Consumer<Byte2ObjectMap.Entry<V>> action) {
		ObjectSet<Byte2ObjectMap.Entry<V>> entries = map.byte2ObjectEntrySet();
		if(entries instanceof Byte2ObjectMap.FastEntrySet) ((Byte2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Byte2ObjectMap<V> empty() { 
		return (Byte2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectMap<V> synchronize(Byte2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectMap<V> synchronize(Byte2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectSortedMap<V> synchronize(Byte2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectSortedMap<V> synchronize(Byte2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectOrderedMap<V> synchronize(Byte2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectOrderedMap<V> synchronize(Byte2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectNavigableMap<V> synchronize(Byte2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Byte2ObjectNavigableMap<V> synchronize(Byte2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Byte2ObjectMap<V> unmodifiable(Byte2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Byte2ObjectOrderedMap<V> unmodifiable(Byte2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Byte2ObjectSortedMap<V> unmodifiable(Byte2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Byte2ObjectNavigableMap<V> unmodifiable(Byte2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Byte2ObjectMap.Entry<V> unmodifiable(Byte2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Byte2ObjectMap.Entry<V> unmodifiable(Map.Entry<Byte, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Byte2ObjectMap<V> singleton(byte key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractByte2ObjectMap<V> {
		final byte key;
		final V value;
		ByteSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Byte2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(byte key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(byte key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(byte key, V defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractByte2ObjectMap<V> {
		@Override
		public V put(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(byte key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(byte key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(byte key, V defaultValue) { return null; }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractByte2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Byte, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2ObjectMap.Entry<V> entry) {
			super(entry.getByteKey(), entry.getValue());
		}
		
		@Override
		public void set(byte key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Byte2ObjectNavigableMap<V> {
		Byte2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Byte2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2ObjectNavigableMap<V> descendingMap() { return Byte2ObjectMaps.synchronize(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2ObjectMap.Entry<V> firstEntry() { return Byte2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2ObjectMap.Entry<V> lastEntry() { return Byte2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2ObjectNavigableMap<V> subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2ObjectNavigableMap<V> headMap(byte toKey, boolean inclusive) { return Byte2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2ObjectNavigableMap<V> tailMap(byte fromKey, boolean inclusive) { return Byte2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2ObjectNavigableMap<V> subMap(byte fromKey, byte toKey) { return Byte2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ObjectNavigableMap<V> headMap(byte toKey) { return Byte2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ObjectNavigableMap<V> tailMap(byte fromKey) { return Byte2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Byte2ObjectMap.Entry<V> lowerEntry(byte key) { return Byte2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2ObjectMap.Entry<V> higherEntry(byte key) { return Byte2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2ObjectMap.Entry<V> floorEntry(byte key) { return Byte2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2ObjectMap.Entry<V> ceilingEntry(byte key) { return Byte2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Byte2ObjectOrderedMap<V> {
		Byte2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Byte2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Byte2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Byte2ObjectSortedMap<V> {
		Byte2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Byte2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2ObjectSortedMap<V> subMap(byte fromKey, byte toKey) { return Byte2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2ObjectSortedMap<V> headMap(byte toKey) { return Byte2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2ObjectSortedMap<V> tailMap(byte fromKey) { return Byte2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Byte2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractByte2ObjectMap<V> implements Byte2ObjectMap<V> {
		Byte2ObjectMap<V> map;
		ObjectCollection<V> values;
		ByteSet keys;
		ObjectSet<Byte2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Byte2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(byte key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(byte key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(byte key) { return map.get(key); }
		@Override
		public V getOrDefault(byte key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Byte2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.byte2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Byte2ObjectMap.Entry<V>>
	{
		ObjectSet<Byte2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Byte2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Byte2ObjectMap.Entry<V>>() {
				ObjectIterator<Byte2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2ObjectMap.Entry<V> next() { return Byte2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Byte2ObjectNavigableMap<V> {
		Byte2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Byte2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Byte2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2ObjectNavigableMap<V> subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2ObjectNavigableMap<V> headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2ObjectNavigableMap<V> tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2ObjectNavigableMap<V> subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ObjectNavigableMap<V> headMap(byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ObjectNavigableMap<V> tailMap(byte fromKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2ObjectMap.Entry<V> lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2ObjectMap.Entry<V> higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2ObjectMap.Entry<V> floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2ObjectMap.Entry<V> ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> headMap(Byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectNavigableMap<V> tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Byte2ObjectMap.Entry<V> lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2ObjectMap.Entry<V> floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2ObjectMap.Entry<V> ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2ObjectMap.Entry<V> higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Byte2ObjectOrderedMap<V> {
		Byte2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Byte2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(byte key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(byte key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Byte2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Byte2ObjectSortedMap<V> {
		Byte2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Byte2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2ObjectSortedMap<V> subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2ObjectSortedMap<V> headMap(byte toKey)  { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2ObjectSortedMap<V> tailMap(byte fromKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Byte2ObjectSortedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2ObjectSortedMap<V> subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectSortedMap<V> headMap(Byte toKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2ObjectSortedMap<V> tailMap(Byte fromKey) { synchronized(mutex) { return Byte2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractByte2ObjectMap<V> implements Byte2ObjectMap<V> {
		Byte2ObjectMap<V> map;
		ObjectCollection<V> values;
		ByteSet keys;
		ObjectSet<Byte2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(byte key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(byte key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Byte2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(byte key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(byte key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Byte2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(ByteObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(byte key, ByteObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(byte key, Byte2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(byte key, ByteObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V merge(byte key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Byte2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(byte key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Byte2ObjectMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2ObjectEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public V get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public V getOrDefault(Object key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public V put(Byte key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Byte key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Byte key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Byte key, Function<? super Byte, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Byte key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}