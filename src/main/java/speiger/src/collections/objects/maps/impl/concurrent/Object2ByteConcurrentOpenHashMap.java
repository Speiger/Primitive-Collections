package speiger.src.collections.objects.maps.impl.concurrent;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;

import java.util.function.BiFunction;

import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.Object2ByteFunction;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteConcurrentMap;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;

import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
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
 * @param <T> the type of elements maintained by this Collection
 */
public class Object2ByteConcurrentOpenHashMap<T> extends AbstractObject2ByteMap<T> implements Object2ByteConcurrentMap<T>
{
	/** Segment Limit */
	private static final int MAX_SEGMENTS = 1 << 16;
	/** Buckets of the ConcurrentMap */
	protected transient Segment<T>[] segments;
	/** Bitshift of the HashCode */
	protected transient int segmentShift;
	/** Max Bits thats used in the segments */
	protected transient int segmentMask;
	/** EntrySet cache */
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient ByteCollection values;
	
	/**
	 * Copy constructor that doesn't trigger the building of segments and allows to copy it faster.
	 * @param unused not used, Just to keep all constructors accessible.
	 */
	protected Object2ByteConcurrentOpenHashMap(boolean unused) {}
	
	/**
	 * Default Constructor
	 */
	public Object2ByteConcurrentOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2ByteConcurrentOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);		
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteConcurrentOpenHashMap(int minCapacity, float loadFactor) {
		this(minCapacity, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity and concurrencyLevel
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Object2ByteConcurrentOpenHashMap(int minCapacity, int concurrencyLevel) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Constructor that defines the load factor and concurrencyLevel
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Object2ByteConcurrentOpenHashMap(float loadFactor, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(int minCapacity, float loadFactor, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, Byte[] values) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, Byte[] values, float loadFactor) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, Byte[] values, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, Byte[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].byteValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ByteConcurrentOpenHashMap(T[] keys, byte[] values) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, byte[] values, float loadFactor) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, byte[] values, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(T[] keys, byte[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2ByteConcurrentOpenHashMap(Map<? extends T, ? extends Byte> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteConcurrentOpenHashMap(Map<? extends T, ? extends Byte> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Object2ByteConcurrentOpenHashMap(Map<? extends T, ? extends Byte> map, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(Map<? extends T, ? extends Byte> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2ByteConcurrentOpenHashMap(Object2ByteMap<T> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Object2ByteConcurrentOpenHashMap(Object2ByteMap<T> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
 	 */
	public Object2ByteConcurrentOpenHashMap(Object2ByteMap<T> map, int concurrencyLevel) {
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
	public Object2ByteConcurrentOpenHashMap(Object2ByteMap<T> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	@Override
	public byte put(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).put(hash, key, value);
	}

	@Override
	public byte putIfAbsent(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).putIfAbsent(hash, key, value);
	}
	
	@Override
	public byte addTo(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).addTo(hash, key, value);
	}

	@Override
	public byte subFrom(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).subFrom(hash, key, value);
	}
	
	@Override
	public byte rem(T key) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key);
	}
	
	@Override
	public boolean remove(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}

	@Override
	public byte remOrDefault(T key, byte defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).removeOrDefault(hash, key, defaultValue);
	}
	
	@Override
	public byte getByte(T key) {
		int hash = getHashCode(key);
		return getSegment(hash).get(hash, key);
	}
	
	@Override
	public Byte get(Object key) {
		int hash = getHashCode(key);
		return Byte.valueOf(getSegment(hash).get(hash, key));
	}
	
	@Override
	public byte getOrDefault(T key, byte defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).getOrDefault(hash, key, defaultValue);
	}
	
	
	@Override
	public void forEach(ObjectByteConsumer<T> action) {
		for(int i = 0,m=segments.length;i<m;i++) {
			segments[i].forEach(action);
		}
	}
	
	@Override
	public Object2ByteConcurrentOpenHashMap<T> copy() {
		Object2ByteConcurrentOpenHashMap<T> copy = new Object2ByteConcurrentOpenHashMap<>(false);
		copy.segmentShift = segmentShift;
		copy.segmentMask = segmentMask;
		copy.segments = new Segment[segments.length];
		for(int i = 0,m=segments.length;i<m;i++)
			copy.segments[i] = segments[i].copy(copy);
		return copy;
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(int i = 0,m=segments.length;i<m;i++) {
			if(segments[i].containsValue(value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean replace(T key, byte oldValue, byte newValue) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, oldValue, newValue);
	}

	@Override
	public byte replace(T key, byte value) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, value);
	}

	@Override
	public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).compute(hash, key, mappingFunction);
	}

	@Override
	public byte computeByteIfAbsent(T key, Object2ByteFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfAbsent(hash, key, mappingFunction);
	}

	@Override
	public byte supplyByteIfAbsent(T key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int hash = getHashCode(key);
		return getSegment(hash).supplyIfAbsent(hash, key, valueProvider);
	}

	@Override
	public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfPresent(hash, key, mappingFunction);
	}

	@Override
	public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction) {
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
	public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ByteCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	protected int getSegmentIndex(int hash) {
		return (hash >>> segmentShift) & segmentMask;
	}
	
	protected Segment<T> getSegment(int hash) {
	    return segments[(hash >>> segmentShift) & segmentMask];
	}
	
	protected int getHashCode(Object obj) {
		return HashUtil.mix(Objects.hashCode(obj));
	}
	
	private class MapEntrySet extends AbstractObjectSet<Object2ByteMap.Entry<T>> implements Object2ByteMap.FastEntrySet<T> {
		@Override
		public ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public void fastForEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ByteMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public boolean matchesAny(Object2BooleanFunction<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public boolean matchesNone(Object2BooleanFunction<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public boolean matchesAll(Object2BooleanFunction<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public <E> E reduce(E identity, BiFunction<E, Object2ByteMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public Object2ByteMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2ByteMap.Entry<T>, Object2ByteMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2ByteMap.Entry<T> state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public Object2ByteMap.Entry<T> findFirst(Object2BooleanFunction<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public int count(Object2BooleanFunction<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			BasicEntry<T> entry = new BasicEntry<>();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					T key = entry.getKey();
					int hash = getHashCode(key);
					Segment<T> seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, key);
						if(index >= 0) return entry.getByteValue() == seg.values[index];
					}
					finally {
						seg.unlockRead(stamp);
					}
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int hash = getHashCode(entry.getKey());
					Segment<T> seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, entry.getKey());
						if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(seg.values[index]));						
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
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					return Object2ByteConcurrentOpenHashMap.this.remove(entry.getKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2ByteConcurrentOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Object2ByteConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteConcurrentOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> implements ObjectSet<T> {
		
		@Override
		public boolean add(T key) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size();
			Object2ByteConcurrentOpenHashMap.this.remove(o);
			return size() != oldSize;
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Object2ByteConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public boolean matchesAny(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.keys[index])) return true;
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
		public boolean matchesNone(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.keys[index])) return false;
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
		public boolean matchesAll(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(!filter.getBoolean(seg.keys[index])) return false;
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
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						state = operator.apply(state, seg.keys[index]);
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
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
						state = operator.apply(state, seg.keys[index]);
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
		public T findFirst(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.keys[index])) return seg.keys[index];
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
		public int count(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.getBoolean(seg.keys[index])) result++;
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
	
	private class Values extends AbstractByteCollection {
		@Override
		public boolean contains(byte e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(byte o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ByteIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Object2ByteConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.values[index])) return true;
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
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(filter.get(seg.values[index])) return false;
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
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(!filter.get(seg.values[index])) return false;
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
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.applyAsByte(state, seg.values[index]);
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
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
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
						state = operator.applyAsByte(state, seg.values[index]);
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
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.values[index])) return seg.values[index];
						index = (int)seg.links[index];
					}
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment<T> seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						if(filter.get(seg.values[index])) result++;
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
	
	private class FastEntryIterator extends MapIterator implements ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		
		@Override
		public Object2ByteMap.Entry<T> next() {
			entry.set(nextEntry(), currentSegment());
			return entry;
		}
		
		@Override
		public Object2ByteMap.Entry<T> previous() {
			entry.set(previousEntry(), currentSegment());
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> {
		MapEntry entry;
		
		public EntryIterator() {}
		
		@Override
		public Object2ByteMap.Entry<T> next() {
			return entry = new MapEntry(nextEntry(), currentSegment());
		}
	
		@Override
		public Object2ByteMap.Entry<T> previous() {
			return entry = new MapEntry(previousEntry(), currentSegment());
		}
	
		@Override
		public void remove() {
			super.remove();
			entry.clear();
		}
	}
	
	private class ValueIterator extends MapIterator implements ByteBidirectionalIterator {
		public ValueIterator() {}
		
		@Override
		public byte previousByte() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public byte nextByte() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected byte entry(int entry, int segment) {
			return segments[segment].values[entry];
		}
	}
	
	private class KeyIterator extends MapIterator implements ObjectBidirectionalIterator<T> {
		
		public KeyIterator() {}
		
		@Override
		public T previous() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public T next() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected T entry(int entry, int segment) {
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
			Segment<T> seg = segments[currentSegment];
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
					seg.keys[seg.nullIndex] = null;
					seg.values[seg.nullIndex] = (byte)0;
				}
				else {
					int slot, last, startPos = current;
					current = -1;
					T current;
					while(true) {
						startPos = ((last = startPos) + 1) & seg.mask;
						while(true){
							if((current = seg.keys[startPos]) == null) {
								seg.keys[last] = null;
								seg.values[last] = (byte)0;
								return;
							}
							slot = HashUtil.mix(Objects.hashCode(current)) & seg.mask;
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
	
	protected class MapEntry implements Object2ByteMap.Entry<T>, Map.Entry<T, Byte> {
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
		public T getKey() {
			return segments[segmentIndex].keys[index];
		}
		
		@Override
		public byte getByteValue() {
			return segments[segmentIndex].values[index];
		}
		
		@Override
		public byte setValue(byte value) {
			Segment<T> seg = segments[segmentIndex];
			long stamp = seg.writeLock();
			try
			{
				byte oldValue = getByteValue();
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
				if(obj instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && getByteValue() == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Byte && Objects.equals(getKey(), key) && getByteValue() == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Byte.hashCode(getByteValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Byte.toString(getByteValue());
		}
	}
	
	protected static class Segment<T> extends StampedLock
	{
		private static final long serialVersionUID = -446894977795760975L;
		protected final Object2ByteConcurrentOpenHashMap<T> map;
		/** The Backing keys array */
		protected transient T[] keys;
		/** The Backing values array */
		protected transient byte[] values;
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
		
		protected Segment(Object2ByteConcurrentOpenHashMap<T> map) {
			this.map = map;
		}
		
		protected Segment(Object2ByteConcurrentOpenHashMap<T> map, int minCapacity, float loadFactor, boolean isNullContainer) {
			this.map = map;
			this.minCapacity = minCapacity;
			this.loadFactor = loadFactor;
			mask = minCapacity - 1;
			maxFill = Math.min((int)Math.ceil(minCapacity * loadFactor), minCapacity - 1);
			nullIndex = isNullContainer ? minCapacity : -1;
			int arraySize = minCapacity + (isNullContainer ? 1 : 0);
			keys = (T[])new Object[arraySize];
			values = new byte[arraySize];
			links = new long[arraySize];
		}
		
		protected Segment<T> copy(Object2ByteConcurrentOpenHashMap<T> newMap) {
			long stamp = readLock();
			try
			{
				Segment<T> copy = new Segment<>(newMap);
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
		
		protected byte getDefaultReturnValue() {
			return map.getDefaultReturnValue();
		}
		
		protected byte put(int hash, T key, byte value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				byte oldValue = values[slot];
				values[slot] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected byte putIfAbsent(int hash, T key, byte value) {
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
		
		protected byte addTo(int hash, T key, byte value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				byte oldValue = values[slot];
				values[slot] += value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected byte subFrom(int hash, T key, byte value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return getDefaultReturnValue();
				byte oldValue = values[slot];
				values[slot] -= value;
				if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
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
		
		protected boolean containsValue(byte value) {
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
					if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[index]))) return true;
					index = (int)links[index];
				}
				return false;
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected byte get(int hash, Object key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected byte getOrDefault(int hash, T key, byte defaultValue) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? defaultValue : values[slot];				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected void forEach(ObjectByteConsumer<T> action) {
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
		
		protected byte removeOrDefault(int hash, T key, byte defaultValue) {
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
		
		protected Byte remove(int hash, Object key) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return Byte.valueOf(getDefaultReturnValue());
				return Byte.valueOf(removeIndex(slot));
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean remove(int hash, T key, byte value) {
			long stamp = writeLock();
			try {
				if(key == null) {
					if(containsNull && value == values[nullIndex]) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				T current = keys[pos];
				if(current == null) return false;
				if(Objects.equals(current, key) && value == values[pos]) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if((current = keys[pos = (++pos & mask)]) == null) return false;
					else if(Objects.equals(current, key) && value == values[pos]) {
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
					if(containsNull && Objects.equals(value, Byte.valueOf(values[nullIndex]))) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				T current = keys[pos];
				if(current == null) return false;
				if(Objects.equals(key, current) && Objects.equals(value, Byte.valueOf(values[pos]))) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if((current = keys[pos = (++pos & mask)]) == null) return false;
					else if(Objects.equals(key, current) && Objects.equals(value, Byte.valueOf(values[pos]))){
						removeIndex(pos);
						return true;
					}
				}				
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean replace(int hash, T key, byte oldValue, byte newValue) {
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
		
		protected byte replace(int hash, T key, byte value) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) return getDefaultReturnValue();
				byte oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected byte compute(int hash, T key, ObjectByteUnaryOperator<T> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					byte newValue = mappingFunction.applyAsByte(key, getDefaultReturnValue());
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				byte newValue = mappingFunction.applyAsByte(key, values[index]);
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
		
		protected byte computeIfAbsent(int hash, T key, Object2ByteFunction<T> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					byte newValue = mappingFunction.getByte(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				byte newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = mappingFunction.getByte(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected byte supplyIfAbsent(int hash, T key, ByteSupplier valueProvider) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					byte newValue = valueProvider.getByte();
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				byte newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = valueProvider.getByte();
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected byte computeIfPresent(int hash, T key, ObjectByteUnaryOperator<T> mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
				byte newValue = mappingFunction.applyAsByte(key, values[index]);
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
		
		protected byte merge(int hash, T key, byte value, ByteByteUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				byte newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(values[index], value);
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
				Arrays.fill(keys, null);
				Arrays.fill(values, (byte)0);
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
				keys = (T[])new Object[arraySize];
				values = new byte[arraySize];
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
		
		protected void insert(int slot, T key, byte value) {
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
		
		protected byte removeIndex(int pos) {
			if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
			byte value = values[pos];
			keys[pos] = null;
			values[pos] = (byte)0;
			size--;
			onNodeRemoved(pos);
			shiftKeys(pos);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected byte removeNullIndex() {
			byte value = values[nullIndex];
			containsNull = false;
			keys[nullIndex] = null;
			values[nullIndex] = (byte)0;
			size--;
			onNodeRemoved(nullIndex);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected int findIndex(int hash, Object key) {
			if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
			int pos = hash & mask;
			T current = keys[pos];
			if(current != null) {
				if(Objects.equals(key, current)) return pos;
				while((current = keys[pos = (++pos & mask)]) != null)
					if(Objects.equals(key, current)) return pos;
			}
			return -(pos + 1);
		}
		
		protected void shiftKeys(int startPos) {
			int slot, last;
			T current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == null) {
						keys[last] = null;
						values[last] = (byte)0;
						return;
					}
					slot = HashUtil.mix(Objects.hashCode(current)) & mask;
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
			T[] newKeys = (T[])new Object[arraySize];
			byte[] newValues = new byte[arraySize];
			long[] newLinks = new long[arraySize];
			int i = firstIndex, prev = -1, newPrev = -1, pos;
			firstIndex = -1;
			for(int j = size; j-- != 0;) {
				if(keys[i] == null) pos = newSize;
				else {
					pos = HashUtil.mix(Objects.hashCode(keys[i])) & newMask;
					while(newKeys[pos] != null) pos = ++pos & newMask;
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