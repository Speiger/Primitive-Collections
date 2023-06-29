package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.objects.functions.function.ToCharFunction;
import speiger.src.collections.objects.functions.function.ObjectCharUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.utils.CharCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2CharMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2CharMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2CharMap<T> empty() { 
		return (Object2CharMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2CharMap.Entry<T>> fastIterator(Object2CharMap<T> map) {
		ObjectSet<Object2CharMap.Entry<T>> entries = map.object2CharEntrySet();
		return entries instanceof Object2CharMap.FastEntrySet ? ((Object2CharMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2CharMap.Entry<T>> fastIterable(Object2CharMap<T> map) {
		ObjectSet<Object2CharMap.Entry<T>> entries = map.object2CharEntrySet();
		return map instanceof Object2CharMap.FastEntrySet ? new ObjectIterable<Object2CharMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2CharMap.Entry<T>> iterator() { return ((Object2CharMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2CharMap.Entry<T>> action) { ((Object2CharMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the keyType of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2CharMap<T> map, Consumer<Object2CharMap.Entry<T>> action) {
		ObjectSet<Object2CharMap.Entry<T>> entries = map.object2CharEntrySet();
		if(entries instanceof Object2CharMap.FastEntrySet) ((Object2CharMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharMap<T> synchronize(Object2CharMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharMap<T> synchronize(Object2CharMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharSortedMap<T> synchronize(Object2CharSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharSortedMap<T> synchronize(Object2CharSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharOrderedMap<T> synchronize(Object2CharOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharOrderedMap<T> synchronize(Object2CharOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharNavigableMap<T> synchronize(Object2CharNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2CharNavigableMap<T> synchronize(Object2CharNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2CharMap<T> unmodifiable(Object2CharMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2CharOrderedMap<T> unmodifiable(Object2CharOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2CharSortedMap<T> unmodifiable(Object2CharSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2CharNavigableMap<T> unmodifiable(Object2CharNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2CharMap.Entry<T> unmodifiable(Object2CharMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2CharMap.Entry<T> unmodifiable(Map.Entry<T, Character> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2CharMap<T> singleton(T key, char value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2CharMap<T> {
		final T key;
		final char value;
		ObjectSet<T> keySet;
		CharCollection values;
		ObjectSet<Object2CharMap.Entry<T>> entrySet;
		
		SingletonMap(T key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char put(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public char remOrDefault(T key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char getChar(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public char getOrDefault(T key, char defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public CharCollection values() { 
			if(values == null) values = CharCollections.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2CharMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2CharMap<T> {
		@Override
		public char put(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char addTo(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public char remOrDefault(T key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char getChar(T key) { return getDefaultReturnValue(); }
		@Override
		public char getOrDefault(T key, char defaultValue) { return defaultValue; }
		@Override
		public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public CharCollection values() { return CharCollections.empty(); }
		@Override
		public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2CharMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Character> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2CharMap.Entry<T> entry) {
			super(entry.getKey(), entry.getCharValue());
		}
		
		@Override
		public void set(T key, char value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2CharNavigableMap<T> {
		Object2CharNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2CharNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2CharNavigableMap<T> descendingMap() { return Object2CharMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2CharMap.Entry<T> firstEntry() { return Object2CharMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2CharMap.Entry<T> lastEntry() { return Object2CharMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2CharMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2CharMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2CharNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2CharMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2CharNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2CharMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2CharNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2CharMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2CharNavigableMap<T> subMap(T fromKey, T toKey) { return Object2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2CharNavigableMap<T> headMap(T toKey) { return Object2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2CharNavigableMap<T> tailMap(T fromKey) { return Object2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2CharMap.Entry<T> lowerEntry(T key) { return Object2CharMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2CharMap.Entry<T> higherEntry(T key) { return Object2CharMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2CharMap.Entry<T> floorEntry(T key) { return Object2CharMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2CharMap.Entry<T> ceilingEntry(T key) { return Object2CharMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2CharNavigableMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2CharOrderedMap<T> {
		Object2CharOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2CharOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putAndMoveToLast(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public char getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Object2CharOrderedMap<T> copy() { return map.copy(); }
		@Override
		public ObjectOrderedSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return (ObjectOrderedSet<T>)keys;
		}
				
		@Override
		public ObjectOrderedSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.object2CharEntrySet());
			return (ObjectOrderedSet<Object2CharMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2CharSortedMap<T> {
		Object2CharSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2CharSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2CharSortedMap<T> subMap(T fromKey, T toKey) { return Object2CharMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2CharSortedMap<T> headMap(T toKey) { return Object2CharMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2CharSortedMap<T> tailMap(T fromKey) { return Object2CharMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public ObjectSortedSet<T> keySet() { return ObjectSets.unmodifiable(map.keySet()); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { throw new UnsupportedOperationException(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { throw new UnsupportedOperationException(); }
		@Override
		public char firstCharValue() { return map.firstCharValue(); }
		@Override
		public char lastCharValue() { return map.lastCharValue(); }
		@Override
		public Object2CharSortedMap<T> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2CharMap<T> implements Object2CharMap<T> {
		Object2CharMap<T> map;
		CharCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2CharMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2CharMap<T> map) {
			this.map = map;
		}
		
		@Override
		public char put(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char putIfAbsent(T key, char value){ throw new UnsupportedOperationException(); }
		@Override
		public char addTo(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char subFrom(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public char remOrDefault(T key, char defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, char value) { throw new UnsupportedOperationException(); }
		@Override
		public char getChar(T key) {
			char type = map.getChar(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public char getOrDefault(T key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsent(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsent(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfAbsentNonDefault(T key, ToCharFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char computeCharIfPresentNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public char supplyCharIfAbsentNonDefault(T key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(ObjectCharUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceChars(Object2CharMap<T> m) { throw new UnsupportedOperationException(); }
		@Override
		public Object2CharMap<T> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2CharEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<T> extends UnmodifyableEntrySet<T> implements ObjectOrderedSet<Object2CharMap.Entry<T>>
	{
		ObjectOrderedSet<Object2CharMap.Entry<T>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Object2CharMap.Entry<T>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Object2CharMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2CharMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Object2CharMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Object2CharMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Object2CharMap.Entry<T>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Object2CharMap.Entry<T>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Object2CharMap.Entry<T>> iterator(Object2CharMap.Entry<T> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Object2CharMap.Entry<T> first() { return set.first(); }
		@Override
		public Object2CharMap.Entry<T> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Object2CharMap.Entry<T> last() { return set.last(); }
		@Override
		public Object2CharMap.Entry<T> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2CharMap.Entry<T>>
	{
		ObjectSet<Object2CharMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2CharMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2CharMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2CharMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2CharMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2CharMap.Entry<T>>() {
				ObjectIterator<Object2CharMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2CharMap.Entry<T> next() { return Object2CharMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2CharNavigableMap<T> {
		Object2CharNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2CharNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2CharNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2CharNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2CharMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Object2CharMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2CharMap.Entry<T> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Object2CharMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2CharMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2CharNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2CharMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2CharNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2CharMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2CharNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2CharMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2CharNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2CharNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2CharNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2CharMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2CharMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2CharMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2CharMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2CharNavigableMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2CharOrderedMap<T> {
		Object2CharOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2CharOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2CharOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public char putAndMoveToFirst(T key, char value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public char putAndMoveToLast(T key, char value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public char getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public char getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Object2CharOrderedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectOrderedSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return (ObjectOrderedSet<T>)keys;
		}
		
		@Override
		public ObjectOrderedSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2CharEntrySet(), mutex);
			return (ObjectOrderedSet<Object2CharMap.Entry<T>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2CharSortedMap<T> {
		Object2CharSortedMap<T> map;
		
		SynchronizedSortedMap(Object2CharSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2CharSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2CharSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2CharMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2CharSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2CharMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2CharSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2CharMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public ObjectSortedSet<T> keySet() { synchronized(mutex) { return ObjectSets.synchronize(map.keySet(), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public char firstCharValue() { synchronized(mutex) { return map.firstCharValue(); } }
		@Override
		public char lastCharValue() { synchronized(mutex) { return map.lastCharValue(); } }
		@Override
		public Object2CharSortedMap<T> copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2CharMap<T> implements Object2CharMap<T> {
		Object2CharMap<T> map;
		CharCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2CharMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2CharMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2CharMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public char getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2CharMap<T> setDefaultReturnValue(char v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public char put(T key, char value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public char putIfAbsent(T key, char value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2CharMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public char addTo(T key, char value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public char subFrom(T key, char value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2CharMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2CharMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Character> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, char[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(char value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public char getChar(T key) { synchronized(mutex) { return map.getChar(key); } }
		@Override
		public char rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public char remOrDefault(T key, char defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, char value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, char oldValue, char newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public char replace(T key, char value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceChars(Object2CharMap<T> m) { synchronized(mutex) { map.replaceChars(m); } }
		@Override
		public void replaceChars(ObjectCharUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceChars(mappingFunction); } }
		@Override
		public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeChar(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsent(T key, ToCharFunction<T> mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsent(key, mappingFunction); } }
		@Override
		public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeCharIfPresent(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsent(T key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsent(key, valueProvider); } }
		@Override
		public char computeCharNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeCharNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfAbsentNonDefault(T key, ToCharFunction<T> mappingFunction) { synchronized(mutex) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); } }
		@Override
		public char computeCharIfPresentNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeCharIfPresentNonDefault(key, mappingFunction); } }
		@Override
		public char supplyCharIfAbsentNonDefault(T key, CharSupplier valueProvider) { synchronized(mutex) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); } }
		@Override
		public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeChar(key, value, mappingFunction); } }
		@Override
		public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllChar(m, mappingFunction); } }
		@Override
		public char getOrDefault(T key, char defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectCharConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Object2CharMap<T> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public CharCollection values() {
			if(values == null) values = CharCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2CharEntrySet(), mutex);
			return entrySet;
		}
		
	}
}