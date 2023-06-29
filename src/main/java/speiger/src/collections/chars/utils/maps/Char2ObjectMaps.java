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
import speiger.src.collections.chars.functions.consumer.CharObjectConsumer;
import speiger.src.collections.chars.functions.function.CharFunction;
import speiger.src.collections.chars.functions.function.CharObjectUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectSortedMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectOrderedMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Char2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Char2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Char2ObjectMap<V> empty() { 
		return (Char2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Char2ObjectMap.Entry<V>> fastIterator(Char2ObjectMap<V> map) {
		ObjectSet<Char2ObjectMap.Entry<V>> entries = map.char2ObjectEntrySet();
		return entries instanceof Char2ObjectMap.FastEntrySet ? ((Char2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Char2ObjectMap.Entry<V>> fastIterable(Char2ObjectMap<V> map) {
		ObjectSet<Char2ObjectMap.Entry<V>> entries = map.char2ObjectEntrySet();
		return map instanceof Char2ObjectMap.FastEntrySet ? new ObjectIterable<Char2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Char2ObjectMap.Entry<V>> iterator() { return ((Char2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Char2ObjectMap.Entry<V>> action) { ((Char2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Char2ObjectMap<V> map, Consumer<Char2ObjectMap.Entry<V>> action) {
		ObjectSet<Char2ObjectMap.Entry<V>> entries = map.char2ObjectEntrySet();
		if(entries instanceof Char2ObjectMap.FastEntrySet) ((Char2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectMap<V> synchronize(Char2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectMap<V> synchronize(Char2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectSortedMap<V> synchronize(Char2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectSortedMap<V> synchronize(Char2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectOrderedMap<V> synchronize(Char2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectOrderedMap<V> synchronize(Char2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectNavigableMap<V> synchronize(Char2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Char2ObjectNavigableMap<V> synchronize(Char2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Char2ObjectMap<V> unmodifiable(Char2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Char2ObjectOrderedMap<V> unmodifiable(Char2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Char2ObjectSortedMap<V> unmodifiable(Char2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Char2ObjectNavigableMap<V> unmodifiable(Char2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Char2ObjectMap.Entry<V> unmodifiable(Char2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Char2ObjectMap.Entry<V> unmodifiable(Map.Entry<Character, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Char2ObjectMap<V> singleton(char key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractChar2ObjectMap<V> {
		final char key;
		final V value;
		CharSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Char2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(char key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(char key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(char key) { return Objects.equals(this.key, Character.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(char key, V defaultValue) { return Objects.equals(this.key, Character.valueOf(key)) ? value : defaultValue; }
		@Override
		public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public CharSet keySet() { 
			if(keySet == null) keySet = CharSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractChar2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractChar2ObjectMap<V> {
		@Override
		public V put(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(char key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(char key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(char key, V defaultValue) { return defaultValue; }
		@Override
		public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public CharSet keySet() { return CharSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractChar2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Character, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Char2ObjectMap.Entry<V> entry) {
			super(entry.getCharKey(), entry.getValue());
		}
		
		@Override
		public void set(char key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Char2ObjectNavigableMap<V> {
		Char2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Char2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Char2ObjectNavigableMap<V> descendingMap() { return Char2ObjectMaps.unmodifiable(map.descendingMap()); }
		@Override
		public CharNavigableSet navigableKeySet() { return CharSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public CharNavigableSet keySet() { return CharSets.unmodifiable(map.keySet()); }
		@Override
		public CharNavigableSet descendingKeySet() { return CharSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Char2ObjectMap.Entry<V> firstEntry() { return Char2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Char2ObjectMap.Entry<V> lastEntry() { return Char2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Char2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { return Char2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey, boolean inclusive) { return Char2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey, boolean inclusive) { return Char2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, char toKey) { return Char2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey) { return Char2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey) { return Char2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public Char2ObjectMap.Entry<V> lowerEntry(char key) { return Char2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Char2ObjectMap.Entry<V> higherEntry(char key) { return Char2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Char2ObjectMap.Entry<V> floorEntry(char key) { return Char2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Char2ObjectMap.Entry<V> ceilingEntry(char key) { return Char2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Char2ObjectNavigableMap<V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Char2ObjectOrderedMap<V> {
		Char2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Char2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharKey() { return map.firstCharKey(); }
		@Override
		public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public char lastCharKey() { return map.lastCharKey(); }
		@Override
		public char pollLastCharKey() { throw new UnsupportedOperationException(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Char2ObjectOrderedMap<V> copy() { return map.copy(); }
		@Override
		public CharOrderedSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return (CharOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.char2ObjectEntrySet());
			return (ObjectOrderedSet<Char2ObjectMap.Entry<V>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Char2ObjectSortedMap<V> {
		Char2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Char2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public Char2ObjectSortedMap<V> subMap(char fromKey, char toKey) { return Char2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Char2ObjectSortedMap<V> headMap(char toKey) { return Char2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Char2ObjectSortedMap<V> tailMap(char fromKey) { return Char2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
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
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Char2ObjectSortedMap<V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectMap<V> {
		Char2ObjectMap<V> map;
		ObjectCollection<V> values;
		CharSet keys;
		ObjectSet<Char2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Char2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(char key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(char key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(char key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(char key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(char key) {
			V type = map.get(key);
			return Objects.equals(type, map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public V getOrDefault(char key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(Char2ObjectMap<V> m) { throw new UnsupportedOperationException(); }
		@Override
		public Char2ObjectMap<V> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharSet keySet() { 
			if(keys == null) keys = CharSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.char2ObjectEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<V> extends UnmodifyableEntrySet<V> implements ObjectOrderedSet<Char2ObjectMap.Entry<V>>
	{
		ObjectOrderedSet<Char2ObjectMap.Entry<V>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Char2ObjectMap.Entry<V>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Char2ObjectMap.Entry<V>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> iterator(Char2ObjectMap.Entry<V> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Char2ObjectMap.Entry<V> first() { return set.first(); }
		@Override
		public Char2ObjectMap.Entry<V> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Char2ObjectMap.Entry<V> last() { return set.last(); }
		@Override
		public Char2ObjectMap.Entry<V> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Char2ObjectMap.Entry<V>>
	{
		ObjectSet<Char2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Char2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Char2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Char2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Char2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Char2ObjectMap.Entry<V>>() {
				ObjectIterator<Char2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Char2ObjectMap.Entry<V> next() { return Char2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Char2ObjectNavigableMap<V> {
		Char2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Char2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Char2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Char2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public CharNavigableSet navigableKeySet() { synchronized(mutex) { return CharSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public CharNavigableSet descendingKeySet() { synchronized(mutex) { return CharSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public CharNavigableSet keySet() { synchronized(mutex) { return CharSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Char2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Char2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Char2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Char2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey, boolean inclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey, boolean inclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public char lowerKey(char key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public char higherKey(char key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public char floorKey(char key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public char ceilingKey(char key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Char2ObjectMap.Entry<V> lowerEntry(char key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Char2ObjectMap.Entry<V> higherEntry(char key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Char2ObjectMap.Entry<V> floorEntry(char key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Char2ObjectMap.Entry<V> ceilingEntry(char key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Char2ObjectNavigableMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> headMap(Character toKey, boolean inclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> tailMap(Character fromKey, boolean inclusive) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> headMap(Character toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectNavigableMap<V> tailMap(Character fromKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public Char2ObjectMap.Entry<V> lowerEntry(Character key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Char2ObjectMap.Entry<V> floorEntry(Character key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Char2ObjectMap.Entry<V> ceilingEntry(Character key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Char2ObjectMap.Entry<V> higherEntry(Character key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Char2ObjectOrderedMap<V> {
		Char2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Char2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Char2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(char key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(char key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(char key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(char key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(char key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(char key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public char firstCharKey() { synchronized(mutex) { return map.firstCharKey(); } }
		@Override
		public char pollFirstCharKey() { synchronized(mutex) { return map.pollFirstCharKey(); } }
		@Override
		public char lastCharKey() { synchronized(mutex) { return map.lastCharKey(); } }
		@Override
		public char pollLastCharKey() { synchronized(mutex) { return map.pollLastCharKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Char2ObjectOrderedMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharOrderedSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return (CharOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ObjectEntrySet(), mutex);
			return (ObjectOrderedSet<Char2ObjectMap.Entry<V>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Char2ObjectSortedMap<V> {
		Char2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Char2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Char2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public CharComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Char2ObjectSortedMap<V> subMap(char fromKey, char toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Char2ObjectSortedMap<V> headMap(char toKey)  { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Char2ObjectSortedMap<V> tailMap(char fromKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
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
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Char2ObjectSortedMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Character firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Character lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Char2ObjectSortedMap<V> subMap(Character fromKey, Character toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectSortedMap<V> headMap(Character toKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Char2ObjectSortedMap<V> tailMap(Character fromKey) { synchronized(mutex) { return Char2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectMap<V> {
		Char2ObjectMap<V> map;
		ObjectCollection<V> values;
		CharSet keys;
		ObjectSet<Char2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Char2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Char2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractChar2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(char key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(char key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Char2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Char2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Character, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(char[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(char key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(char key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(char key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(char key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(char key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(char key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(char key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Char2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(CharObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { synchronized(mutex) { return map.supplyIfAbsent(key, valueProvider); } }
		@Override
		public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(char key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(CharObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Char2ObjectMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public CharSet keySet() {
			if(keys == null) keys = CharSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.char2ObjectEntrySet(), mutex);
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
		public V put(Character key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Character key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Character key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Character key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Character, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Character key, BiFunction<? super Character, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Character key, Function<? super Character, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Character key, BiFunction<? super Character, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Character key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Character, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}