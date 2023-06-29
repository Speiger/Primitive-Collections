package speiger.src.collections.floats.maps.impl.concurrent;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.BiFunction;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.floats.functions.consumer.FloatBooleanConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatBooleanUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanConcurrentMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.collections.BooleanBidirectionalIterator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.BooleanPredicate;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A TypeSpecific ConcurrentHashMap implementation that is based on <a href="https://github.com/google/guava">Guavas</a> approach and backing array implementations.
 * Like <a href="https://github.com/google/guava">Guavas</a> implementation this solution can be accessed by multiple threads, but it is not as flexible as Javas implementation.
 * The concurrencyLevel decides how many pools exist, and each pool can be accessed by 1 thread for writing and as many threads for reading.
 * Though it is ill adviced to iterate over the collection using the Iterator if the Map is written to. Keep that in mind.
 * 
 * 
 */
public class Float2BooleanConcurrentOpenHashMap extends AbstractFloat2BooleanMap implements Float2BooleanConcurrentMap, ITrimmable
{
	/** Segment Limit */
	private static final int MAX_SEGMENTS = 1 << 16;
	/** Buckets of the ConcurrentMap */
	protected transient Segment[] segments;
	/** Bitshift of the HashCode */
	protected transient int segmentShift;
	/** Max Bits thats used in the segments */
	protected transient int segmentMask;
	/** EntrySet cache */
	protected transient FastEntrySet entrySet;
	/** KeySet cache */
	protected transient FloatSet keySet;
	/** Values cache */
	protected transient BooleanCollection values;
	
	/**
	 * Copy constructor that doesn't trigger the building of segments and allows to copy it faster.
	 * @param unused not used, Just to keep all constructors accessible.
	 */
	protected Float2BooleanConcurrentOpenHashMap(boolean unused) {}
	
	/**
	 * Default Constructor
	 */
	public Float2BooleanConcurrentOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2BooleanConcurrentOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);		
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2BooleanConcurrentOpenHashMap(int minCapacity, float loadFactor) {
		this(minCapacity, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity and concurrencyLevel
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2BooleanConcurrentOpenHashMap(int minCapacity, int concurrencyLevel) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Constructor that defines the load factor and concurrencyLevel
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2BooleanConcurrentOpenHashMap(float loadFactor, int concurrencyLevel) {
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
	public Float2BooleanConcurrentOpenHashMap(int minCapacity, float loadFactor, int concurrencyLevel) {
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
			segments[i] = new Segment(this, segmentCapacity, loadFactor, i == 0);
		}
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2BooleanConcurrentOpenHashMap(Float[] keys, Boolean[] values) {
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
	public Float2BooleanConcurrentOpenHashMap(Float[] keys, Boolean[] values, float loadFactor) {
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
	public Float2BooleanConcurrentOpenHashMap(Float[] keys, Boolean[] values, int concurrencyLevel) {
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
	public Float2BooleanConcurrentOpenHashMap(Float[] keys, Boolean[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i].booleanValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2BooleanConcurrentOpenHashMap(float[] keys, boolean[] values) {
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
	public Float2BooleanConcurrentOpenHashMap(float[] keys, boolean[] values, float loadFactor) {
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
	public Float2BooleanConcurrentOpenHashMap(float[] keys, boolean[] values, int concurrencyLevel) {
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
	public Float2BooleanConcurrentOpenHashMap(float[] keys, boolean[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2BooleanConcurrentOpenHashMap(Map<? extends Float, ? extends Boolean> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2BooleanConcurrentOpenHashMap(Map<? extends Float, ? extends Boolean> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Float2BooleanConcurrentOpenHashMap(Map<? extends Float, ? extends Boolean> map, int concurrencyLevel) {
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
	public Float2BooleanConcurrentOpenHashMap(Map<? extends Float, ? extends Boolean> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2BooleanConcurrentOpenHashMap(Float2BooleanMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Float2BooleanConcurrentOpenHashMap(Float2BooleanMap map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
 	 */
	public Float2BooleanConcurrentOpenHashMap(Float2BooleanMap map, int concurrencyLevel) {
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
	public Float2BooleanConcurrentOpenHashMap(Float2BooleanMap map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	@Override
	public boolean put(float key, boolean value) {
		int hash = getHashCode(key);
		return getSegment(hash).put(hash, key, value);
	}

	@Override
	public boolean putIfAbsent(float key, boolean value) {
		int hash = getHashCode(key);
		return getSegment(hash).putIfAbsent(hash, key, value);
	}
	
	@Override
	public boolean remove(float key) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key);
	}
	
	@Override
	public boolean remove(float key, boolean value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}

	@Override
	public boolean removeOrDefault(float key, boolean defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).removeOrDefault(hash, key, defaultValue);
	}
	
	@Override
	public boolean get(float key) {
		int hash = getHashCode(key);
		return getSegment(hash).get(hash, key);
	}
	
	@Override
	public Boolean get(Object key) {
		int hash = getHashCode(key);
		return Boolean.valueOf(getSegment(hash).get(hash, key));
	}
	
	@Override
	public boolean getOrDefault(float key, boolean defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).getOrDefault(hash, key, defaultValue);
	}
	
	
	@Override
	public void forEach(FloatBooleanConsumer action) {
		for(int i = 0,m=segments.length;i<m;i++) {
			segments[i].forEach(action);
		}
	}
	
	@Override
	public Float2BooleanConcurrentOpenHashMap copy() {
		Float2BooleanConcurrentOpenHashMap copy = new Float2BooleanConcurrentOpenHashMap(false);
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
	public boolean containsValue(boolean value) {
		for(int i = 0,m=segments.length;i<m;i++) {
			if(segments[i].containsValue(value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean replace(float key, boolean oldValue, boolean newValue) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, oldValue, newValue);
	}

	@Override
	public boolean replace(float key, boolean value) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, value);
	}

	@Override
	public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).compute(hash, key, mappingFunction);
	}
	
	@Override
	public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfAbsent(hash, key, mappingFunction);
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int hash = getHashCode(key);
		return getSegment(hash).supplyIfAbsent(hash, key, valueProvider);
	}
	
	@Override
	public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfPresent(hash, key, mappingFunction);
	}
	
	@Override
	public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeNonDefault(hash, key, mappingFunction);
	}
	
	@Override
	public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfAbsentNonDefault(hash, key, mappingFunction);
	}

	@Override
	public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int hash = getHashCode(key);
		return getSegment(hash).supplyIfAbsentNonDefault(hash, key, valueProvider);
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfPresentNonDefault(hash, key, mappingFunction);
	}
	
	@Override
	public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
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
	public boolean trim(int size) {
		int segmentCapacity = size / segments.length;
		if(segmentCapacity * segments.length < size) {
			segmentCapacity++;
		}
		boolean result = false;
		for(int i = 0, m=segments.length;i<m;i++) {
			result |= segments[i].trim(segmentCapacity);
		}
		return result;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int segmentCapacity = size / segments.length;
		if(segmentCapacity * segments.length < size) {
			segmentCapacity++;
		}
		for(int i = 0, m=segments.length;i<m;i++) {
			segments[i].clearAndTrim(segmentCapacity);
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
	public ObjectSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public BooleanCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	protected int getSegmentIndex(int hash) {
		return (hash >>> segmentShift) & segmentMask;
	}
	
	protected Segment getSegment(int hash) {
	    return segments[(hash >>> segmentShift) & segmentMask];
	}
	
	protected int getHashCode(float key) {
		return HashUtil.mix(Float.hashCode(key));
	}
	
	protected int getHashCode(Object obj) {
		return HashUtil.mix(Objects.hashCode(obj));
	}
	
	private class MapEntrySet extends AbstractObjectSet<Float2BooleanMap.Entry> implements Float2BooleanMap.FastEntrySet {
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Float2BooleanMap.Entry> action) {
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(new ValueMapEntry(index, i));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2BooleanMap.Entry> action) {
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						entry.set(index, i);
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
		public void forEachIndexed(IntObjectConsumer<Float2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			int count = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						action.accept(count++, new ValueMapEntry(index, i));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						action.accept(input, new ValueMapEntry(index, i));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(index, i);
						if(filter.test(entry)) return true;
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
		public boolean matchesNone(Predicate<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(index, i);
						if(filter.test(entry)) return false;
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
		public boolean matchesAll(Predicate<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(index, i);
						if(!filter.test(entry)) return false;
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
		public <E> E reduce(E identity, BiFunction<E, Float2BooleanMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.apply(state, new ValueMapEntry(index, i));
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
		public Float2BooleanMap.Entry reduce(ObjectObjectUnaryOperator<Float2BooleanMap.Entry, Float2BooleanMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2BooleanMap.Entry state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(empty) {
							empty = false;
							state = new ValueMapEntry(index, i);
							index = (int)seg.links[index];
							continue;
						}
						state = operator.apply(state, new ValueMapEntry(index, i));
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
		public Float2BooleanMap.Entry findFirst(Predicate<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				int index = seg.firstIndex;
				long stamp = seg.readLock();
				try {
					while(index != -1) {
						entry.set(index, i);
						if(filter.test(entry)) return entry;
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
		public int count(Predicate<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			MapEntry entry = new MapEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						entry.set(index, i);
						if(filter.test(entry)) result++;
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
				if(o instanceof Float2BooleanMap.Entry) {
					Float2BooleanMap.Entry entry = (Float2BooleanMap.Entry)o;
					float key = entry.getFloatKey();
					int hash = getHashCode(key);
					Segment seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, key);
						if(index >= 0) return entry.getBooleanValue() == seg.values[index];
					}
					finally {
						seg.unlockRead(stamp);
					}
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int hash = getHashCode(entry.getKey());
					Segment seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, entry.getKey());
						if(index >= 0) return Objects.equals(entry.getValue(), Boolean.valueOf(seg.values[index]));						
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
				if(o instanceof Float2BooleanMap.Entry) {
					Float2BooleanMap.Entry entry = (Float2BooleanMap.Entry)o;
					return Float2BooleanConcurrentOpenHashMap.this.remove(entry.getFloatKey(), entry.getBooleanValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2BooleanConcurrentOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Float2BooleanConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2BooleanConcurrentOpenHashMap.this.clear();
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
			Float2BooleanConcurrentOpenHashMap.this.remove(o);
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
			return Float2BooleanConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2BooleanConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public void forEachIndexed(IntFloatConsumer action) {
			Objects.requireNonNull(action);
			int count = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(count++, seg.keys[index]);
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
				Segment seg = segments[i];
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
		public boolean matchesAny(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.keys[index])) return true;
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
		public boolean matchesNone(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.keys[index])) return false;
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
		public boolean matchesAll(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(!filter.test(seg.keys[index])) return false;
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
				Segment seg = segments[i];
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
				Segment seg = segments[i];
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
		public float findFirst(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.keys[index])) return seg.keys[index];
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
		public int count(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.keys[index])) result++;
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
	
	private class Values extends AbstractBooleanCollection {
		@Override
		public boolean contains(boolean e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(boolean o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BooleanIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Float2BooleanConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2BooleanConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public void forEachIndexed(IntBooleanConsumer action) {
			Objects.requireNonNull(action);
			int count = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(count++, seg.values[index]);
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}			
		}
		
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesAny(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.values[index])) return true;
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
		public boolean matchesNone(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(filter.test(seg.values[index])) return false;
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
		public boolean matchesAll(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(!filter.test(seg.values[index])) return false;
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
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.applyAsBoolean(state, seg.values[index]);
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
		public boolean reduce(BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = false;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
						state = operator.applyAsBoolean(state, seg.values[index]);
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
		public boolean findFirst(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.values[index])) return seg.values[index];
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
		public int count(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.test(seg.values[index])) result++;
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
	
	private class FastEntryIterator extends MapIterator implements ObjectBidirectionalIterator<Float2BooleanMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		
		@Override
		public Float2BooleanMap.Entry next() {
			entry.set(nextEntry(), currentSegment());
			return entry;
		}
		
		@Override
		public Float2BooleanMap.Entry previous() {
			entry.set(previousEntry(), currentSegment());
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectBidirectionalIterator<Float2BooleanMap.Entry> {
		MapEntry entry;
		
		public EntryIterator() {}
		
		@Override
		public Float2BooleanMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry(), currentSegment());
		}
	
		@Override
		public Float2BooleanMap.Entry previous() {
			return entry = new ValueMapEntry(previousEntry(), currentSegment());
		}
	
		@Override
		public void remove() {
			super.remove();
			entry.clear();
		}
	}
	
	private class ValueIterator extends MapIterator implements BooleanBidirectionalIterator {
		public ValueIterator() {}
		
		@Override
		public boolean previousBoolean() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public boolean nextBoolean() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected boolean entry(int entry, int segment) {
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
			Segment seg = segments[currentSegment];
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
					seg.values[seg.nullIndex] = false;
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
								seg.values[last] = false;
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
	
	protected class ValueMapEntry extends MapEntry {
		protected float key;
		protected boolean value;
		
		public ValueMapEntry(int index, int segmentIndex) {
			super(index, segmentIndex);
			key = segments[segmentIndex].keys[index];
			value = segments[segmentIndex].values[index];
		}
		
		@Override
		public float getFloatKey() {
			return key;
		}

		@Override
		public boolean getBooleanValue() {
			return value;
		}
		
		@Override
		public boolean setValue(boolean value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Float2BooleanMap.Entry, Map.Entry<Float, Boolean> {
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
		public boolean getBooleanValue() {
			return segments[segmentIndex].values[index];
		}
		
		@Override
		public boolean setValue(boolean value) {
			Segment seg = segments[segmentIndex];
			long stamp = seg.writeLock();
			try
			{
				boolean oldValue = seg.values[index];
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
				if(obj instanceof Float2BooleanMap.Entry) {
					Float2BooleanMap.Entry entry = (Float2BooleanMap.Entry)obj;
					return Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(entry.getFloatKey()) && getBooleanValue() == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Boolean && Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(((Float)key).floatValue()) && getBooleanValue() == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(getFloatKey()) ^ Boolean.hashCode(getBooleanValue());
		}
		
		@Override
		public String toString() {
			return Float.toString(getFloatKey()) + "=" + Boolean.toString(getBooleanValue());
		}
	}
	
	protected static class Segment extends StampedLock
	{
		private static final long serialVersionUID = -446894977795760975L;
		protected final Float2BooleanConcurrentOpenHashMap map;
		/** The Backing keys array */
		protected transient float[] keys;
		/** The Backing values array */
		protected transient boolean[] values;
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
		
		protected Segment(Float2BooleanConcurrentOpenHashMap map) {
			this.map = map;
		}
		
		protected Segment(Float2BooleanConcurrentOpenHashMap map, int minCapacity, float loadFactor, boolean isNullContainer) {
			this.map = map;
			this.minCapacity = minCapacity;
			this.loadFactor = loadFactor;
			mask = minCapacity - 1;
			maxFill = Math.min((int)Math.ceil(minCapacity * loadFactor), minCapacity - 1);
			nullIndex = isNullContainer ? minCapacity : -1;
			int arraySize = minCapacity + (isNullContainer ? 1 : 0);
			keys = new float[arraySize];
			values = new boolean[arraySize];
			links = new long[arraySize];
		}
		
		protected Segment copy(Float2BooleanConcurrentOpenHashMap newMap) {
			long stamp = readLock();
			try
			{
				Segment copy = new Segment(newMap);
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
		
		protected boolean getDefaultReturnValue() {
			return map.getDefaultReturnValue();
		}
		
		protected boolean put(int hash, float key, boolean value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				boolean oldValue = values[slot];
				values[slot] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean putIfAbsent(int hash, float key, boolean value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				else if(values[slot] == getDefaultReturnValue()) {
					boolean oldValue = values[slot];
					values[slot] = value;
					return oldValue;
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
		
		protected boolean containsValue(boolean value) {
			long stamp = readLock();
			try {
				int index = firstIndex;
				while(index != -1) {
					if(values[index] == value) return true;
					index = (int)links[index];
				}
				return false;				
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
					if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Boolean.valueOf(values[index]))) return true;
					index = (int)links[index];
				}
				return false;
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected boolean get(int hash, float key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected boolean get(int hash, Object key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected boolean getOrDefault(int hash, float key, boolean defaultValue) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? defaultValue : values[slot];				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected void forEach(FloatBooleanConsumer action) {
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
		
		protected boolean remove(int hash, float key) {
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
		
		protected boolean removeOrDefault(int hash, float key, boolean defaultValue) {
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
		
		protected Boolean remove(int hash, Object key) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return Boolean.valueOf(getDefaultReturnValue());
				return Boolean.valueOf(removeIndex(slot));
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean remove(int hash, float key, boolean value) {
			long stamp = writeLock();
			try {
				if(Float.floatToIntBits(key) == 0) {
					if(containsNull && value == values[nullIndex]) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				float current = keys[pos];
				if(Float.floatToIntBits(current) == 0) return false;
				if(Float.floatToIntBits(current) == Float.floatToIntBits(key) && value == values[pos]) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
					else if(Float.floatToIntBits(current) == Float.floatToIntBits(key) && value == values[pos]) {
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
				if(key == null || (key instanceof Float && Float.floatToIntBits(((Float)key).floatValue()) == 0)) {
					if(containsNull && Objects.equals(value, Boolean.valueOf(values[nullIndex]))) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				float current = keys[pos];
				if(Float.floatToIntBits(current) == 0) return false;
				if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Boolean.valueOf(values[pos]))) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
					else if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Boolean.valueOf(values[pos]))){
						removeIndex(pos);
						return true;
					}
				}				
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean replace(int hash, float key, boolean oldValue, boolean newValue) {
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
		
		protected boolean replace(int hash, float key, boolean value) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) return getDefaultReturnValue();
				boolean oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean compute(int hash, float key, FloatBooleanUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
				values[index] = newValue;
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean computeIfAbsent(int hash, float key, FloatPredicate mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = mappingFunction.test(key);
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = values[index];
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
				
		protected boolean supplyIfAbsent(int hash, float key, BooleanSupplier valueProvider) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = valueProvider.getAsBoolean();
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = values[index];
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
				
		protected boolean computeIfPresent(int hash, float key, FloatBooleanUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) return getDefaultReturnValue();
				boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
				values[index] = newValue;
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean computeNonDefault(int hash, float key, FloatBooleanUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
				if(newValue == getDefaultReturnValue()) {
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
		
		protected boolean computeIfAbsentNonDefault(int hash, float key, FloatPredicate mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = mappingFunction.test(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = mappingFunction.test(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean supplyIfAbsentNonDefault(int hash, float key, BooleanSupplier valueProvider) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					boolean newValue = valueProvider.getAsBoolean();
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				boolean newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = valueProvider.getAsBoolean();
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean computeIfPresentNonDefault(int hash, float key, FloatBooleanUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
				boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
				if(newValue == getDefaultReturnValue()) {
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
		
		protected boolean merge(int hash, float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				boolean newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(values[index], value);
				if(newValue == getDefaultReturnValue()) {
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
				Arrays.fill(values, false);
				firstIndex = -1;
				lastIndex = -1;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean trim(int size) {
			int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
			if(request >= mask+1 || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
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
			if(request >= mask+1) {
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
				values = new boolean[arraySize];
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
		
		protected void insert(int slot, float key, boolean value) {
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
		
		protected boolean removeIndex(int pos) {
			if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
			boolean value = values[pos];
			keys[pos] = 0F;
			values[pos] = false;
			size--;
			onNodeRemoved(pos);
			shiftKeys(pos);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected boolean removeNullIndex() {
			boolean value = values[nullIndex];
			containsNull = false;
			keys[nullIndex] = 0F;
			values[nullIndex] = false;
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
			if(Float.floatToIntBits(((Float)key).floatValue()) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
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
						values[last] = false;
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
			boolean[] newValues = new boolean[arraySize];
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