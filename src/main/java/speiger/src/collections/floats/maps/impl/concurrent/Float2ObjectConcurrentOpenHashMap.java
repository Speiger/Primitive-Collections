package speiger.src.collections.floats.maps.impl.concurrent;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;

import java.util.function.BiFunction;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.consumer.FloatObjectConsumer;
import speiger.src.collections.floats.functions.function.Float2ObjectFunction;
import speiger.src.collections.floats.functions.function.FloatObjectUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectConcurrentMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;


import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;

/**
 * A TypeSpecific ConcurrentHashMap implementation that is based on <a href="https://github.com/google/guava">Guavas</a> approach and backing array implementations.
 * Like <a href="https://github.com/google/guava">Guavas</a> implementation this solution can be accessed by multiple threads, but it is not as flexible as Javas implementation.
 * The concurrencyLevel decides how many pools exist, and each pool can be accessed by 1 thread for writing and as many threads for reading.
 * Though it is ill adviced to iterate over the collection using the Iterator if the Map is written to. Keep that in mind.
 * 
 * 
 * @param <V> the type of elements maintained by this Collection
 */
public class Float2ObjectConcurrentOpenHashMap<V> extends AbstractFloat2ObjectMap<V> implements Float2ObjectConcurrentMap<V>
{
	/** Segment Limit */
	private static final int MAX_SEGMENTS = 1 << 16;
	/** Buckets of the ConcurrentMap */
	protected transient Segment<V>[] segments;
	/** Bitshift of the HashCode */
	protected transient int segmentShift;
	/** Max Bits thats used in the segments */
	protected transient int segmentMask;
	/** EntrySet cache */
	protected transient FastEntrySet<V> entrySet;
	/** KeySet cache */
	protected transient FloatSet keySet;
	/** Values cache */
	protected transient ObjectCollection<V> values;
	
	/**
	 * Copy constructor that doesn't trigger the building of segments and allows to copy it faster.
	 * @param unused not used, Just to keep all constructors accessible.
	 */
	protected Float2ObjectConcurrentOpenHashMap(boolean unused) {}
	
	/**
	 * Default Constructor
	 */
	public Float2ObjectConcurrentOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2ObjectConcurrentOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);		
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ObjectConcurrentOpenHashMap(int minCapacity, float loadFactor) {
		this(minCapacity, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity and concurrencyLevel
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(int minCapacity, int concurrencyLevel) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Constructor that defines the load factor and concurrencyLevel
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(float loadFactor, int concurrencyLevel) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, loadFactor, concurrencyLevel);
	}
	
	/**
	 * Constructor that defines the minimum capacity, load factor and concurrencyLevel
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(int minCapacity, float loadFactor, int concurrencyLevel) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		if(concurrencyLevel <= 0 || concurrencyLevel >= MAX_SEGMENTS) throw new IllegalStateException("concurrencyLevel has to be between 0 and 65536");
		int segmentCount = HashUtil.nextPowerOfTwo(concurrencyLevel);
		int shift = Integer.numberOfTrailingZeros(segmentCount);
		segments = new Segment[segmentCount];
		segmentShift = 32 - shift;
		segmentMask = segmentCount - 1;
		int segmentCapacity = minCapacity / segmentCount;
		if(segmentCapacity * segmentCount < minCapacity) {
			segmentCapacity++;
		}
		segmentCapacity = HashUtil.arraySize(segmentCapacity, loadFactor);
		for(int i = 0;i<segmentCount;i++) {
			segments[i] = new Segment<>(this, segmentCapacity, loadFactor, i == 0);
		}
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectConcurrentOpenHashMap(Float[] keys, V[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ObjectConcurrentOpenHashMap(Float[] keys, V[] values, float loadFactor) {
		this(keys, values, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(Float[] keys, V[] values, int concurrencyLevel) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(Float[] keys, V[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectConcurrentOpenHashMap(float[] keys, V[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ObjectConcurrentOpenHashMap(float[] keys, V[] values, float loadFactor) {
		this(keys, values, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(float[] keys, V[] values, int concurrencyLevel) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(float[] keys, V[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2ObjectConcurrentOpenHashMap(Map<? extends Float, ? extends V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ObjectConcurrentOpenHashMap(Map<? extends Float, ? extends V> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(Map<? extends Float, ? extends V> map, int concurrencyLevel) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2ObjectConcurrentOpenHashMap(Map<? extends Float, ? extends V> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2ObjectConcurrentOpenHashMap(Float2ObjectMap<V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Float2ObjectConcurrentOpenHashMap(Float2ObjectMap<V> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
 	 */
	public Float2ObjectConcurrentOpenHashMap(Float2ObjectMap<V> map, int concurrencyLevel) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
 	 */
	public Float2ObjectConcurrentOpenHashMap(Float2ObjectMap<V> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	@Override
	public V put(float key, V value) {
		int hash = getHashCode(key);
		return getSegment(hash).put(hash, key, value);
	}

	@Override
	public V putIfAbsent(float key, V value) {
		int hash = getHashCode(key);
		return getSegment(hash).putIfAbsent(hash, key, value);
	}
	
	@Override
	public V remove(float key) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key);
	}
	
	@Override
	public boolean remove(float key, V value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}

	@Override
	public V removeOrDefault(float key, V defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).removeOrDefault(hash, key, defaultValue);
	}
	
	@Override
	public V get(float key) {
		int hash = getHashCode(key);
		return getSegment(hash).get(hash, key);
	}
	
	@Override
	public V get(Object key) {
		int hash = getHashCode(key);
		return getSegment(hash).get(hash, key);
	}
	
	@Override
	public V getOrDefault(float key, V defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).getOrDefault(hash, key, defaultValue);
	}
	
	
	@Override
	public void forEach(FloatObjectConsumer<V> action) {
		for(int i = 0,m=segments.length;i<m;i++) {
			segments[i].forEach(action);
		}
	}
	
	@Override
	public Float2ObjectConcurrentOpenHashMap<V> copy() {
		Float2ObjectConcurrentOpenHashMap<V> copy = new Float2ObjectConcurrentOpenHashMap<>(false);
		copy.segmentShift = segmentShift;
		copy.segmentMask = segmentMask;
		copy.segments = new Segment[segments.length];
		for(int i = 0,m=segments.length;i<m;i++)
			copy.segments[i] = segments[i].copy(copy);
		return copy;
	}
	
	@Override
	public boolean containsKey(float key) {
		int hash = getHashCode(key);
		return getSegment(hash).containsKey(hash, key);
	}
	
	@Override
	public boolean replace(float key, V oldValue, V newValue) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, oldValue, newValue);
	}

	@Override
	public V replace(float key, V value) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, value);
	}

	@Override
	public V compute(float key, FloatObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).compute(hash, key, mappingFunction);
	}

	@Override
	public V computeIfAbsent(float key, Float2ObjectFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfAbsent(hash, key, mappingFunction);
	}

	@Override
	public V supplyIfAbsent(float key, ObjectSupplier<V> valueProvider) {
		Objects.requireNonNull(valueProvider);
		int hash = getHashCode(key);
		return getSegment(hash).supplyIfAbsent(hash, key, valueProvider);
	}

	@Override
	public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfPresent(hash, key, mappingFunction);
	}

	@Override
	public V merge(float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).merge(hash, key, value, mappingFunction);
	}
	
	@Override
	public void clear() {
		for(int i = 0,m=segments.length;i<m;i++) {
			segments[i].clear();
		}
	}
	
	@Override
	public boolean isEmpty() {
		for(int i = 0,m=segments.length;i<m;i++) {
			if(segments[i].size > 0) return false;
		}
		return true;
	}
	
	@Override
	public int size() {
		long size = 0L;
		for(int i = 0,m=segments.length;i<m;i++) {
			size += segments[i].size;
		}
		return size > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)size;
	}
	
	@Override
	public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(values == null) values = new Values();
		return values;
	}
	
	protected int getSegmentIndex(int hash) {
		return (hash >>> segmentShift) & segmentMask;
	}
	
	protected Segment<V> getSegment(int hash) {
	    return segments[(hash >>> segmentShift) & segmentMask];
	}
	
	protected int getHashCode(float key) {
		return HashUtil.mix(Float.hashCode(key));
	}
	
	protected int getHashCode(Object obj) {
		return HashUtil.mix(Objects.hashCode(obj));
	}
	
	private class MapEntrySet extends AbstractObjectSet<Float2ObjectMap.Entry<V>> implements Float2ObjectMap.FastEntrySet<V> {
		@Override
		public ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(new BasicEntry<>(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						entry.set(seg.keys[index], seg.values[index]);
						action.accept(entry);
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						action.accept(input, new BasicEntry<>(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(seg.keys[index], seg.values[index]);
						if(filter.getBoolean(entry)) return true;
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(seg.keys[index], seg.values[index]);
						if(filter.getBoolean(entry)) return false;
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(seg.keys[index], seg.values[index]);
						if(!filter.getBoolean(entry)) return false;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2ObjectMap.Entry<V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.apply(state, new BasicEntry<>(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public Float2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Float2ObjectMap.Entry<V>, Float2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Float2ObjectMap.Entry<V> state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(empty) {
							empty = false;
							state = new BasicEntry<>(seg.keys[index], seg.values[index]);
							index = (int)seg.links[index];
							continue;
						}
						state = operator.apply(state, new BasicEntry<>(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public Float2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				int index = seg.firstIndex;
				long stamp = seg.readLock();
				try {
					while(index != -1) {
						entry.set(seg.keys[index], seg.values[index]);
						if(filter.getBoolean(entry)) return entry;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			BasicEntry<V> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(seg.keys[index], seg.values[index]);
						if(filter.getBoolean(entry)) result++;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2ObjectMap.Entry) {
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>)o;
					float key = entry.getFloatKey();
					int hash = getHashCode(key);
					Segment<V> seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, key);
						if(index >= 0) return Objects.equals(entry.getValue(), seg.values[index]);
					}
					finally {
						seg.unlockRead(stamp);
					}
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int hash = getHashCode(entry.getKey());
					Segment<V> seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, entry.getKey());
						if(index >= 0) return Objects.equals(entry.getValue(), seg.values[index]);						
					}
					finally {
						seg.unlockRead(stamp);
					}
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2ObjectMap.Entry) {
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>)o;
					return Float2ObjectConcurrentOpenHashMap.this.remove(entry.getFloatKey(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2ObjectConcurrentOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Float2ObjectConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ObjectConcurrentOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractFloatSet implements FloatSet {
		
		@Override
		public boolean add(float key) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(float e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(float o) {
			int oldSize = size();
			Float2ObjectConcurrentOpenHashMap.this.remove(o);
			return size() != oldSize;
		}
		
		@Override
		public FloatBidirectionalIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Float2ObjectConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ObjectConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(seg.keys[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(input, seg.keys[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.keys[index])) return true;
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.keys[index])) return false;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(!filter.get(seg.keys[index])) return false;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						state = operator.applyAsFloat(state, seg.keys[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public float reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(empty) {
							empty = false;
							state = seg.keys[index];
							index = (int)seg.links[index];
							continue;
						}
						state = operator.applyAsFloat(state, seg.keys[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public float findFirst(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.keys[index])) return seg.keys[index];
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return 0F;
		}
		
		@Override
		public int count(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.keys[index])) result++;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return result;
		}
	}
	
	private class Values extends AbstractObjectCollection<V> {
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(V o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<V> iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Float2ObjectConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ObjectConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super V> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(seg.values[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(input, seg.values[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.values[index])) return true;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(filter.getBoolean(seg.values[index])) return false;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(!filter.getBoolean(seg.values[index])) return false;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.apply(state, seg.values[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
			Objects.requireNonNull(operator);
			V state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(empty) {
							empty = false;
							state = seg.values[index];
							index = (int)seg.links[index];
							continue;
						}
						state = operator.apply(state, seg.values[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return state;
		}
		
		@Override
		public V findFirst(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.values[index])) return seg.values[index];
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<V> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.values[index])) result++;
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		
		@Override
		public Float2ObjectMap.Entry<V> next() {
			entry.set(nextEntry(), currentSegment());
			return entry;
		}
		
		@Override
		public Float2ObjectMap.Entry<V> previous() {
			entry.set(previousEntry(), currentSegment());
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>> {
		MapEntry entry;
		
		public EntryIterator() {}
		
		@Override
		public Float2ObjectMap.Entry<V> next() {
			return entry = new MapEntry(nextEntry(), currentSegment());
		}
	
		@Override
		public Float2ObjectMap.Entry<V> previous() {
			return entry = new MapEntry(previousEntry(), currentSegment());
		}
	
		@Override
		public void remove() {
			super.remove();
			entry.clear();
		}
	}
	
	private class ValueIterator extends MapIterator implements ObjectBidirectionalIterator<V> {
		public ValueIterator() {}
		
		@Override
		public V previous() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public V next() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected V entry(int entry, int segment) {
			return segments[segment].values[entry];
		}
	}
	
	private class KeyIterator extends MapIterator implements FloatBidirectionalIterator {
		
		public KeyIterator() {}
		
		@Override
		public float previousFloat() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public float nextFloat() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected float entry(int entry, int segment) {
			return segments[segment].keys[entry];
		}
	}
	
	private class MapIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int previousSegment = -1;
		int nextSegment = -1;
		int currentSegment = -1;
		
		MapIterator() {
			currentSegment = getFirstSegment();
			if(currentSegment != -1) next = segments[currentSegment].firstIndex;
		}
		
		public boolean hasNext() {
			return next != -1 || nextSegment != -1;
		}
		
		public boolean hasPrevious() {
			return previous != -1 || previousSegment != -1;
		}
		
		public int currentSegment() {
			return currentSegment;
		}
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			if(previousSegment != -1) {
				nextSegment = currentSegment;
				currentSegment = previousSegment;
				previousSegment = -1;
				next = current = segments[currentSegment].lastIndex;
			}
			else {
				if(next != -1) nextSegment = -1;
				next = current = previous;
			}
			findPreviousIndex();
			return current;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			if(nextSegment != -1) {
				previousSegment = currentSegment;
				currentSegment = nextSegment;
				nextSegment = -1;
				previous = current = segments[currentSegment].firstIndex;
			}
			else {
				if(previous != -1) previousSegment = -1;
				previous = current = next;
			}
			findNextIndex();
			return current;
		}
		
		public void remove() {
			if(current == -1) throw new IllegalStateException();
			Segment<V> seg = segments[currentSegment];
			long stamp = seg.writeLock();
			try {
				if(current == previous) findPreviousIndex();
				else findNextIndex();
				seg.size--;
				if(previous == -1) seg.firstIndex = next;
				else seg.links[previous] ^= ((seg.links[previous] ^ (next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				
				if(next == -1) seg.lastIndex = previous;
				else seg.links[next] ^= ((seg.links[next] ^ ((previous & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
				
				if(current == seg.nullIndex) {
					current = -1;
					seg.containsNull = false;
					seg.keys[seg.nullIndex] = 0F;
					seg.values[seg.nullIndex] = null;
				}
				else {
					int slot, last, startPos = current;
					current = -1;
					float current;
					while(true) {
						startPos = ((last = startPos) + 1) & seg.mask;
						while(true){
							if(Float.floatToIntBits((current = seg.keys[startPos])) == 0) {
								seg.keys[last] = 0F;
								seg.values[last] = null;
								return;
							}
							slot = HashUtil.mix(Float.hashCode(current)) & seg.mask;
							if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
							startPos = ++startPos & seg.mask;
						}
						seg.keys[last] = current;
						seg.values[last] = seg.values[startPos];
						if(next == startPos) next = last;
						if(previous == startPos) previous = last;
						seg.onNodeMoved(startPos, last);
					}
				}
			}
			finally {
				seg.unlockWrite(stamp);
			}
		}
		
		protected void findPreviousIndex() {
			previous = (int)(segments[currentSegment].links[current] >>> 32);
			if(previous == -1) {
				previousSegment = findPreviousSegment(currentSegment-1);
			}
		}
		
		protected void findNextIndex() {
			next = (int)(segments[currentSegment].links[current]);
			if(next == -1) {
				nextSegment = findNextSegment(currentSegment+1);
			}
		}
		
		private int getFirstSegment() {
			for(int i = 0,m=segments.length;i<m;i++) {
				if(segments[i].firstIndex != -1) return i;
			}
			return -1;
		}
		
		private int findNextSegment(int index) {
			for(;index < segments.length && segments[index].firstIndex == -1;index++);
			return index >= segments.length ? -1 : index;
		}
		
		private int findPreviousSegment(int index) {
			for(;index >= 0 && segments[index].lastIndex == -1;index--);
			return index >= 0 ? index : -1;
		}
	}
	
	protected class MapEntry implements Float2ObjectMap.Entry<V>, Map.Entry<Float, V> {
		int index = -1;
		int segmentIndex = -1;
		
		public MapEntry() {}
		public MapEntry(int index, int segmentIndex) {
			set(index, segmentIndex);
		}
		
		public void set(int index, int segmentIndex) {
			this.index = index;
			this.segmentIndex = segmentIndex;
		}
		
		public void clear() {
			index = -1;
			segmentIndex = -1;
		}
		
		@Override
		public float getFloatKey() {
			return segments[segmentIndex].keys[index];
		}
		
		@Override
		public V getValue() {
			return segments[segmentIndex].values[index];
		}
		
		@Override
		public V setValue(V value) {
			Segment<V> seg = segments[segmentIndex];
			long stamp = seg.writeLock();
			try
			{
				V oldValue = getValue();
				seg.values[index] = value;
				return oldValue;
			}
			finally {
				seg.unlockWrite(stamp);
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2ObjectMap.Entry) {
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>)obj;
					return Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(entry.getFloatKey()) && Objects.equals(getValue(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(((Float)key).floatValue()) && Objects.equals(getValue(), value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(getFloatKey()) ^ Objects.hashCode(getValue());
		}
		
		@Override
		public String toString() {
			return Float.toString(getFloatKey()) + "=" + Objects.toString(getValue());
		}
	}
	
	protected static class Segment<V> extends StampedLock
	{
		private static final long serialVersionUID = -446894977795760975L;
		protected final Float2ObjectConcurrentOpenHashMap<V> map;
		/** The Backing keys array */
		protected transient float[] keys;
		/** The Backing values array */
		protected transient V[] values;
		/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
		protected transient long[] links;
		/** The First Index in the Map */
		protected int firstIndex = -1;
		/** The Last Index in the Map */
		protected int lastIndex = -1;
		/** If a null value is present */
		protected transient boolean containsNull;
		/** Index of the Null Value */
		protected transient int nullIndex;
		/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
		protected transient int maxFill;
		/** Max Index that is allowed to be searched through nullIndex - 1 */
		protected transient int mask;
		/** Amount of Elements stored in the HashMap */
		protected int size;
		/** Minimum array size the Segment will be */
		protected transient int minCapacity;
		/** How full the Arrays are allowed to get before resize */
		protected float loadFactor;
		
		protected Segment(Float2ObjectConcurrentOpenHashMap<V> map) {
			this.map = map;
		}
		
		protected Segment(Float2ObjectConcurrentOpenHashMap<V> map, int minCapacity, float loadFactor, boolean isNullContainer) {
			this.map = map;
			this.minCapacity = minCapacity;
			this.loadFactor = loadFactor;
			mask = minCapacity - 1;
			maxFill = Math.min((int)Math.ceil(minCapacity * loadFactor), minCapacity - 1);
			nullIndex = isNullContainer ? minCapacity : -1;
			int arraySize = minCapacity + (isNullContainer ? 1 : 0);
			keys = new float[arraySize];
			values = (V[])new Object[arraySize];
			links = new long[arraySize];
		}
		
		protected Segment<V> copy(Float2ObjectConcurrentOpenHashMap<V> newMap) {
			long stamp = readLock();
			try
			{
				Segment<V> copy = new Segment<>(newMap);
				copy.keys = Arrays.copyOf(keys, keys.length);
				copy.values = Arrays.copyOf(values, values.length);
				copy.links = Arrays.copyOf(links, links.length);
				copy.firstIndex = firstIndex;
				copy.lastIndex = lastIndex;
				copy.containsNull = containsNull;
				copy.nullIndex = nullIndex;
				copy.maxFill = maxFill;
				copy.mask = mask;
				copy.size = size;
				copy.minCapacity = minCapacity;
				copy.loadFactor = loadFactor;
				return copy;				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected V getDefaultReturnValue() {
			return map.getDefaultReturnValue();
		}
		
		protected V put(int hash, float key, V value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				V oldValue = values[slot];
				values[slot] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V putIfAbsent(int hash, float key, V value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				return values[slot];
			}
			finally {
				unlockWrite(stamp);
			}	
		}
		
		
		protected boolean containsKey(int hash, float key) {
			long stamp = readLock();
			try {
				return findIndex(hash, key) >= 0;				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		@Deprecated
		protected boolean containsKey(int hash, Object key) {
			long stamp = readLock();
			try {
				return findIndex(hash, key) >= 0;				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		@Deprecated
		protected boolean containsValue(Object value) {
			long stamp = readLock();
			try {
				int index = firstIndex;
				while(index != -1) {
					if(Objects.equals(values[index], value)) return true;
					index = (int)links[index];
				}
				return false;
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected V get(int hash, float key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected V get(int hash, Object key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected V getOrDefault(int hash, float key, V defaultValue) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? defaultValue : values[slot];				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected void forEach(FloatObjectConsumer<V> action) {
			long stamp = readLock();
			try {
				int index = firstIndex;
				while(index != -1) {
					action.accept(keys[index], values[index]);
					index = (int)links[index];
				}				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected V remove(int hash, float key) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return getDefaultReturnValue();
				return removeIndex(slot);
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V removeOrDefault(int hash, float key, V defaultValue) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return defaultValue;
				return removeIndex(slot);
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V remove(int hash, Object key) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return getDefaultReturnValue();
				return removeIndex(slot);
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean remove(int hash, float key, V value) {
			long stamp = writeLock();
			try {
				if(Float.floatToIntBits(key) == 0) {
					if(containsNull && Objects.equals(value, values[nullIndex])) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				float current = keys[pos];
				if(Float.floatToIntBits(current) == 0) return false;
				if(Float.floatToIntBits(current) == Float.floatToIntBits(key) && Objects.equals(value, values[pos])) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
					else if(Float.floatToIntBits(current) == Float.floatToIntBits(key) && Objects.equals(value, values[pos])) {
						removeIndex(pos);
						return true;
					}
				}
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean remove(int hash, Object key, Object value) {
			long stamp = writeLock();
			try
			{
				if(key == null) {
					if(containsNull && Objects.equals(value, values[nullIndex])) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				float current = keys[pos];
				if(Float.floatToIntBits(current) == 0) return false;
				if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, values[pos])) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
					else if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, values[pos])){
						removeIndex(pos);
						return true;
					}
				}				
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean replace(int hash, float key, V oldValue, V newValue) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0 || values[index] != oldValue) return false;
				values[index] = newValue;
				return true;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V replace(int hash, float key, V value) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) return getDefaultReturnValue();
				V oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V compute(int hash, float key, FloatObjectUnaryOperator<V> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					V newValue = mappingFunction.apply(key, getDefaultReturnValue());
					if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				V newValue = mappingFunction.apply(key, values[index]);
				if(Objects.equals(newValue, getDefaultReturnValue())) {
					removeIndex(index);
					return newValue;
				}
				values[index] = newValue;
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V computeIfAbsent(int hash, float key, Float2ObjectFunction<V> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					V newValue = mappingFunction.get(key);
					if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				V newValue = values[index];
				if(Objects.equals(newValue, getDefaultReturnValue())) {
					newValue = mappingFunction.get(key);
					if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V supplyIfAbsent(int hash, float key, ObjectSupplier<V> valueProvider) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					V newValue = valueProvider.get();
					if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				V newValue = values[index];
				if(Objects.equals(newValue, getDefaultReturnValue())) {
					newValue = valueProvider.get();
					if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V computeIfPresent(int hash, float key, FloatObjectUnaryOperator<V> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0 || Objects.equals(values[index], getDefaultReturnValue())) return getDefaultReturnValue();
				V newValue = mappingFunction.apply(key, values[index]);
				if(Objects.equals(newValue, getDefaultReturnValue())) {
					removeIndex(index);
					return newValue;
				}
				values[index] = newValue;
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected V merge(int hash, float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				V newValue = index < 0 || Objects.equals(values[index], getDefaultReturnValue()) ? value : mappingFunction.apply(values[index], value);
				if(Objects.equals(newValue, getDefaultReturnValue())) {
					if(index >= 0)
						removeIndex(index);
				}
				else if(index < 0) insert(-index-1, key, newValue);
				else values[index] = newValue;
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected void clear() {
			if(size == 0) return;
			long stamp = writeLock();
			try {
				size = 0;
				containsNull = false;
				Arrays.fill(keys, 0F);
				Arrays.fill(values, null);
				firstIndex = -1;
				lastIndex = -1;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean trim(int size) {
			int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
			if(request >= size || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
			long stamp = writeLock();
			try {
				try {
					rehash(request);
				}
				catch(OutOfMemoryError noMemory) { return false; }
				return true;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected void clearAndTrim(int size) {
			int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
			if(request >= size) {
				clear();
				return;
			}
			long stamp = writeLock();
			try {
				if(nullIndex != -1) {
					nullIndex = request;
				}
				mask = request-1;
				maxFill = Math.min((int)Math.ceil(request * loadFactor), request - 1);
				int arraySize = request + (nullIndex != -1 ? 1 : 0);
				keys = new float[arraySize];
				values = (V[])new Object[arraySize];
				links = new long[arraySize];
				this.size = 0;
				firstIndex = -1;
				lastIndex = -1;
				containsNull = false;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected void insert(int slot, float key, V value) {
			if(slot == nullIndex) containsNull = true;
			keys[slot] = key;
			values[slot] = value;
			if(size == 0) {
				firstIndex = lastIndex = slot;
				links[slot] = -1L;
			}
			else {
				links[lastIndex] ^= ((links[lastIndex] ^ (slot & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				links[slot] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
				lastIndex = slot;
			}
			if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		}
		
		protected V removeIndex(int pos) {
			if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
			V value = values[pos];
			keys[pos] = 0F;
			values[pos] = null;
			size--;
			onNodeRemoved(pos);
			shiftKeys(pos);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected V removeNullIndex() {
			V value = values[nullIndex];
			containsNull = false;
			keys[nullIndex] = 0F;
			values[nullIndex] = null;
			size--;
			onNodeRemoved(nullIndex);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected int findIndex(int hash, float key) {
			if(Float.floatToIntBits(key) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
			int pos = hash & mask;
			float current = keys[pos];
			if(Float.floatToIntBits(current) != 0) {
				if(Float.floatToIntBits(current) == Float.floatToIntBits(key)) return pos;
				while(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) != 0)
					if(Float.floatToIntBits(current) == Float.floatToIntBits(key)) return pos;
			}
			return -(pos + 1);
		}
		
		protected int findIndex(int hash, Object key) {
			if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
			int pos = hash & mask;
			float current = keys[pos];
			if(Float.floatToIntBits(current) != 0) {
				if(Objects.equals(key, Float.valueOf(current))) return pos;
				while(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) != 0)
					if(Objects.equals(key, Float.valueOf(current))) return pos;
			}
			return -(pos + 1);
		}
		
		protected void shiftKeys(int startPos) {
			int slot, last;
			float current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(Float.floatToIntBits((current = keys[startPos])) == 0) {
						keys[last] = 0F;
						values[last] = null;
						return;
					}
					slot = HashUtil.mix(Float.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				keys[last] = current;
				values[last] = values[startPos];
				onNodeMoved(startPos, last);
			}
		}
		
		protected void rehash(int newSize) {
			int newMask = newSize - 1;
			int arraySize = newSize + (nullIndex != -1 ? 1 : 0);
			float[] newKeys = new float[arraySize];
			V[] newValues = (V[])new Object[arraySize];
			long[] newLinks = new long[arraySize];
			int i = firstIndex, prev = -1, newPrev = -1, pos;
			firstIndex = -1;
			for(int j = size; j-- != 0;) {
				if(Float.floatToIntBits(keys[i]) == 0) pos = newSize;
				else {
					pos = HashUtil.mix(Float.hashCode(keys[i])) & newMask;
					while(Float.floatToIntBits(newKeys[pos]) != 0) pos = ++pos & newMask;
				}
				newKeys[pos] = keys[i];
				newValues[pos] = values[i];
				if(prev != -1) {
					newLinks[newPrev] ^= ((newLinks[newPrev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
					newLinks[pos] ^= ((newLinks[pos] ^ ((newPrev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
					newPrev = pos;
				}
				else {
					newPrev = firstIndex = pos;
					newLinks[pos] = -1L;
				}
				i = (int)links[prev = i];
			}
			links = newLinks;
			lastIndex = newPrev;
			if(newPrev != -1) newLinks[newPrev] |= 0xFFFFFFFFL;
			if(nullIndex != -1) {
				nullIndex = newSize;
			}
			mask = newMask;
			maxFill = Math.min((int)Math.ceil(newSize * loadFactor), newSize - 1);
			keys = newKeys;
			values = newValues;
		}
		
		protected void onNodeRemoved(int pos) {
			if(size == 0) firstIndex = lastIndex = -1;
			else if(firstIndex == pos) {
				firstIndex = (int)links[pos];
				if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
			}
			else if(lastIndex == pos) {
				lastIndex = (int)(links[pos] >>> 32);
				if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
			}
			else {
				long link = links[pos];
				int prev = (int)(link >>> 32);
				int next = (int)link;
				links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
			}
		}
		
		protected void onNodeMoved(int from, int to) {
			if(size == 1) {
				firstIndex = lastIndex = to;
				links[to] = -1L;
			}
			else if(firstIndex == from) {
				firstIndex = to;
				links[(int)links[from]] ^= ((links[(int)links[from]] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
				links[to] = links[from];
			}
			else if(lastIndex == from) {
				lastIndex = to;
				links[(int)(links[from] >>> 32)] ^= ((links[(int)(links[from] >>> 32)] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				links[to] = links[from];
			}
			else {
				long link = links[from];
				int prev = (int)(link >>> 32);
				int next = (int)link;
				links[prev] ^= ((links[prev] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				links[next] ^= ((links[next] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
				links[to] = link;
			}
		}
	}
}