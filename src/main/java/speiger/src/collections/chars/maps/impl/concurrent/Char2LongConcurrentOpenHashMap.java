package speiger.src.collections.chars.maps.impl.concurrent;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;

import java.util.function.BiFunction;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.consumer.CharLongConsumer;
import speiger.src.collections.chars.functions.function.Char2LongFunction;
import speiger.src.collections.chars.functions.function.CharLongUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongConcurrentMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
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
 */
public class Char2LongConcurrentOpenHashMap extends AbstractChar2LongMap implements Char2LongConcurrentMap
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
	protected transient CharSet keySet;
	/** Values cache */
	protected transient LongCollection values;
	
	/**
	 * Copy constructor that doesn't trigger the building of segments and allows to copy it faster.
	 * @param unused not used, Just to keep all constructors accessible.
	 */
	protected Char2LongConcurrentOpenHashMap(boolean unused) {}
	
	/**
	 * Default Constructor
	 */
	public Char2LongConcurrentOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Char2LongConcurrentOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);		
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Char2LongConcurrentOpenHashMap(int minCapacity, float loadFactor) {
		this(minCapacity, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * Constructor that defines the minimum capacity and concurrencyLevel
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Char2LongConcurrentOpenHashMap(int minCapacity, int concurrencyLevel) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, concurrencyLevel);
	}
	
	/**
	 * Constructor that defines the load factor and concurrencyLevel
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Char2LongConcurrentOpenHashMap(float loadFactor, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(int minCapacity, float loadFactor, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(Character[] keys, Long[] values) {
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
	public Char2LongConcurrentOpenHashMap(Character[] keys, Long[] values, float loadFactor) {
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
	public Char2LongConcurrentOpenHashMap(Character[] keys, Long[] values, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(Character[] keys, Long[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].charValue(), values[i].longValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2LongConcurrentOpenHashMap(char[] keys, long[] values) {
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
	public Char2LongConcurrentOpenHashMap(char[] keys, long[] values, float loadFactor) {
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
	public Char2LongConcurrentOpenHashMap(char[] keys, long[] values, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(char[] keys, long[] values, float loadFactor, int concurrencyLevel) {
		this(keys.length, loadFactor, concurrencyLevel);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Char2LongConcurrentOpenHashMap(Map<? extends Character, ? extends Long> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Char2LongConcurrentOpenHashMap(Map<? extends Character, ? extends Long> map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
	 */
	public Char2LongConcurrentOpenHashMap(Map<? extends Character, ? extends Long> map, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(Map<? extends Character, ? extends Long> map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Char2LongConcurrentOpenHashMap(Char2LongMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Char2LongConcurrentOpenHashMap(Char2LongMap map, float loadFactor) {
		this(map, loadFactor, HashUtil.DEFAULT_MIN_CONCURRENCY);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param concurrencyLevel decides how many operations can be performed at once.
	 * @throws IllegalStateException if the concurrencyLevel is either below/equal to 0 or above/equal to 65535
 	 */
	public Char2LongConcurrentOpenHashMap(Char2LongMap map, int concurrencyLevel) {
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
	public Char2LongConcurrentOpenHashMap(Char2LongMap map, float loadFactor, int concurrencyLevel) {
		this(map.size(), loadFactor, concurrencyLevel);
		putAll(map);
	}
	
	@Override
	public long put(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).put(hash, key, value);
	}

	@Override
	public long putIfAbsent(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).putIfAbsent(hash, key, value);
	}
	
	@Override
	public long addTo(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).addTo(hash, key, value);
	}

	@Override
	public long subFrom(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).subFrom(hash, key, value);
	}
	
	@Override
	public long remove(char key) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key);
	}
	
	@Override
	public boolean remove(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int hash = getHashCode(key);
		return getSegment(hash).remove(hash, key, value);
	}

	@Override
	public long removeOrDefault(char key, long defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).removeOrDefault(hash, key, defaultValue);
	}
	
	@Override
	public long get(char key) {
		int hash = getHashCode(key);
		return getSegment(hash).get(hash, key);
	}
	
	@Override
	public Long get(Object key) {
		int hash = getHashCode(key);
		return Long.valueOf(getSegment(hash).get(hash, key));
	}
	
	@Override
	public long getOrDefault(char key, long defaultValue) {
		int hash = getHashCode(key);
		return getSegment(hash).getOrDefault(hash, key, defaultValue);
	}
	
	
	@Override
	public void forEach(CharLongConsumer action) {
		for(int i = 0,m=segments.length;i<m;i++) {
			segments[i].forEach(action);
		}
	}
	
	@Override
	public Char2LongConcurrentOpenHashMap copy() {
		Char2LongConcurrentOpenHashMap copy = new Char2LongConcurrentOpenHashMap(false);
		copy.segmentShift = segmentShift;
		copy.segmentMask = segmentMask;
		copy.segments = new Segment[segments.length];
		for(int i = 0,m=segments.length;i<m;i++)
			copy.segments[i] = segments[i].copy(copy);
		return copy;
	}
	
	@Override
	public boolean containsKey(char key) {
		int hash = getHashCode(key);
		return getSegment(hash).containsKey(hash, key);
	}
	
	@Override
	public boolean containsValue(long value) {
		for(int i = 0,m=segments.length;i<m;i++) {
			if(segments[i].containsValue(value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean replace(char key, long oldValue, long newValue) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, oldValue, newValue);
	}

	@Override
	public long replace(char key, long value) {
		int hash = getHashCode(key);
		return getSegment(hash).replace(hash, key, value);
	}

	@Override
	public long computeLong(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).compute(hash, key, mappingFunction);
	}

	@Override
	public long computeLongIfAbsent(char key, Char2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfAbsent(hash, key, mappingFunction);
	}

	@Override
	public long supplyLongIfAbsent(char key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int hash = getHashCode(key);
		return getSegment(hash).supplyIfAbsent(hash, key, valueProvider);
	}

	@Override
	public long computeLongIfPresent(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int hash = getHashCode(key);
		return getSegment(hash).computeIfPresent(hash, key, mappingFunction);
	}

	@Override
	public long mergeLong(char key, long value, LongLongUnaryOperator mappingFunction) {
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
	public ObjectSet<Char2LongMap.Entry> char2LongEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public CharSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public LongCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	protected int getSegmentIndex(int hash) {
		return (hash >>> segmentShift) & segmentMask;
	}
	
	protected Segment getSegment(int hash) {
	    return segments[(hash >>> segmentShift) & segmentMask];
	}
	
	protected int getHashCode(char key) {
		return HashUtil.mix(Character.hashCode(key));
	}
	
	protected int getHashCode(Object obj) {
		return HashUtil.mix(Objects.hashCode(obj));
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2LongMap.Entry> implements Char2LongMap.FastEntrySet {
		@Override
		public ObjectBidirectionalIterator<Char2LongMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2LongMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2LongMap.Entry> action) {
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						action.accept(new BasicEntry(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2LongMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2LongMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						action.accept(input, new BasicEntry(seg.keys[index], seg.values[index]));
						index = (int)seg.links[index];
					}					
				}
				finally {
					seg.unlockRead(stamp);
				}
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Char2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesNone(Object2BooleanFunction<Char2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesAll(Object2BooleanFunction<Char2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public <E> E reduce(E identity, BiFunction<E, Char2LongMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.apply(state, new BasicEntry(seg.keys[index], seg.values[index]));
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
		public Char2LongMap.Entry reduce(ObjectObjectUnaryOperator<Char2LongMap.Entry, Char2LongMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2LongMap.Entry state = null;
			boolean empty = true;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						if(empty) {
							empty = false;
							state = new BasicEntry(seg.keys[index], seg.values[index]);
							index = (int)seg.links[index];
							continue;
						}
						state = operator.apply(state, new BasicEntry(seg.keys[index], seg.values[index]));
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
		public Char2LongMap.Entry findFirst(Object2BooleanFunction<Char2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public int count(Object2BooleanFunction<Char2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			BasicEntry entry = new BasicEntry();
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
				if(o instanceof Char2LongMap.Entry) {
					Char2LongMap.Entry entry = (Char2LongMap.Entry)o;
					char key = entry.getCharKey();
					int hash = getHashCode(key);
					Segment seg = getSegment(hash);
					long stamp = seg.readLock();
					try {
						int index = seg.findIndex(hash, key);
						if(index >= 0) return entry.getLongValue() == seg.values[index];
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
						if(index >= 0) return Objects.equals(entry.getValue(), Long.valueOf(seg.values[index]));						
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
				if(o instanceof Char2LongMap.Entry) {
					Char2LongMap.Entry entry = (Char2LongMap.Entry)o;
					return Char2LongConcurrentOpenHashMap.this.remove(entry.getCharKey(), entry.getLongValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Char2LongConcurrentOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Char2LongConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2LongConcurrentOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractCharSet implements CharSet {
		
		@Override
		public boolean add(char key) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(char e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(char o) {
			int oldSize = size();
			Char2LongConcurrentOpenHashMap.this.remove(o);
			return size() != oldSize;
		}
		
		@Override
		public CharBidirectionalIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Char2LongConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2LongConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(CharConsumer action) {
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
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
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
		public boolean matchesAny(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesNone(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesAll(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1){
						state = operator.applyAsChar(state, seg.keys[index]);
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
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
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
						state = operator.applyAsChar(state, seg.keys[index]);
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
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
	
	private class Values extends AbstractLongCollection {
		@Override
		public boolean contains(long e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(long o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public LongIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Char2LongConcurrentOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2LongConcurrentOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(LongConsumer action) {
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
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
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
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
				long stamp = seg.readLock();
				try {
					int index = seg.firstIndex;
					while(index != -1) {
						state = operator.applyAsLong(state, seg.values[index]);
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
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
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
						state = operator.applyAsLong(state, seg.values[index]);
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
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0,m=segments.length;i<m;i++) {
				Segment seg = segments[i];
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
	
	private class FastEntryIterator extends MapIterator implements ObjectBidirectionalIterator<Char2LongMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		
		@Override
		public Char2LongMap.Entry next() {
			entry.set(nextEntry(), currentSegment());
			return entry;
		}
		
		@Override
		public Char2LongMap.Entry previous() {
			entry.set(previousEntry(), currentSegment());
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectBidirectionalIterator<Char2LongMap.Entry> {
		MapEntry entry;
		
		public EntryIterator() {}
		
		@Override
		public Char2LongMap.Entry next() {
			return entry = new MapEntry(nextEntry(), currentSegment());
		}
	
		@Override
		public Char2LongMap.Entry previous() {
			return entry = new MapEntry(previousEntry(), currentSegment());
		}
	
		@Override
		public void remove() {
			super.remove();
			entry.clear();
		}
	}
	
	private class ValueIterator extends MapIterator implements LongBidirectionalIterator {
		public ValueIterator() {}
		
		@Override
		public long previousLong() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public long nextLong() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected long entry(int entry, int segment) {
			return segments[segment].values[entry];
		}
	}
	
	private class KeyIterator extends MapIterator implements CharBidirectionalIterator {
		
		public KeyIterator() {}
		
		@Override
		public char previousChar() {
			return entry(previousEntry(), currentSegment());
		}
		
		@Override
		public char nextChar() {
			return entry(nextEntry(), currentSegment());
		}
		
		protected char entry(int entry, int segment) {
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
					seg.keys[seg.nullIndex] = (char)0;
					seg.values[seg.nullIndex] = 0L;
				}
				else {
					int slot, last, startPos = current;
					current = -1;
					char current;
					while(true) {
						startPos = ((last = startPos) + 1) & seg.mask;
						while(true){
							if((current = seg.keys[startPos]) == (char)0) {
								seg.keys[last] = (char)0;
								seg.values[last] = 0L;
								return;
							}
							slot = HashUtil.mix(Character.hashCode(current)) & seg.mask;
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
	
	protected class MapEntry implements Char2LongMap.Entry, Map.Entry<Character, Long> {
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
		public char getCharKey() {
			return segments[segmentIndex].keys[index];
		}
		
		@Override
		public long getLongValue() {
			return segments[segmentIndex].values[index];
		}
		
		@Override
		public long setValue(long value) {
			Segment seg = segments[segmentIndex];
			long stamp = seg.writeLock();
			try
			{
				long oldValue = getLongValue();
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
				if(obj instanceof Char2LongMap.Entry) {
					Char2LongMap.Entry entry = (Char2LongMap.Entry)obj;
					return getCharKey() == entry.getCharKey() && getLongValue() == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Long && getCharKey() == ((Character)key).charValue() && getLongValue() == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(getCharKey()) ^ Long.hashCode(getLongValue());
		}
		
		@Override
		public String toString() {
			return Character.toString(getCharKey()) + "=" + Long.toString(getLongValue());
		}
	}
	
	protected static class Segment extends StampedLock
	{
		private static final long serialVersionUID = -446894977795760975L;
		protected final Char2LongConcurrentOpenHashMap map;
		/** The Backing keys array */
		protected transient char[] keys;
		/** The Backing values array */
		protected transient long[] values;
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
		
		protected Segment(Char2LongConcurrentOpenHashMap map) {
			this.map = map;
		}
		
		protected Segment(Char2LongConcurrentOpenHashMap map, int minCapacity, float loadFactor, boolean isNullContainer) {
			this.map = map;
			this.minCapacity = minCapacity;
			this.loadFactor = loadFactor;
			mask = minCapacity - 1;
			maxFill = Math.min((int)Math.ceil(minCapacity * loadFactor), minCapacity - 1);
			nullIndex = isNullContainer ? minCapacity : -1;
			int arraySize = minCapacity + (isNullContainer ? 1 : 0);
			keys = new char[arraySize];
			values = new long[arraySize];
			links = new long[arraySize];
		}
		
		protected Segment copy(Char2LongConcurrentOpenHashMap newMap) {
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
		
		protected long getDefaultReturnValue() {
			return map.getDefaultReturnValue();
		}
		
		protected long put(int hash, char key, long value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				long oldValue = values[slot];
				values[slot] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected long putIfAbsent(int hash, char key, long value) {
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
		
		protected long addTo(int hash, char key, long value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) {
					insert(-slot-1, key, value);
					return getDefaultReturnValue();
				}
				long oldValue = values[slot];
				values[slot] += value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected long subFrom(int hash, char key, long value) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return getDefaultReturnValue();
				long oldValue = values[slot];
				values[slot] -= value;
				if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean containsKey(int hash, char key) {
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
		
		protected boolean containsValue(long value) {
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
					if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Long.valueOf(values[index]))) return true;
					index = (int)links[index];
				}
				return false;
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected long get(int hash, char key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected long get(int hash, Object key) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? getDefaultReturnValue() : values[slot];	
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected long getOrDefault(int hash, char key, long defaultValue) {
			long stamp = readLock();
			try {
				int slot = findIndex(hash, key);
				return slot < 0 ? defaultValue : values[slot];				
			}
			finally {
				unlockRead(stamp);
			}
		}
		
		protected void forEach(CharLongConsumer action) {
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
		
		protected long remove(int hash, char key) {
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
		
		protected long removeOrDefault(int hash, char key, long defaultValue) {
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
		
		protected Long remove(int hash, Object key) {
			long stamp = writeLock();
			try {
				int slot = findIndex(hash, key);
				if(slot < 0) return Long.valueOf(getDefaultReturnValue());
				return Long.valueOf(removeIndex(slot));
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean remove(int hash, char key, long value) {
			long stamp = writeLock();
			try {
				if(key == (char)0) {
					if(containsNull && value == values[nullIndex]) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				char current = keys[pos];
				if(current == (char)0) return false;
				if(current == key && value == values[pos]) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if((current = keys[pos = (++pos & mask)]) == (char)0) return false;
					else if(current == key && value == values[pos]) {
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
					if(containsNull && Objects.equals(value, Long.valueOf(values[nullIndex]))) {
						removeNullIndex();
						return true;
					}
					return false;
				}
				int pos = hash & mask;
				char current = keys[pos];
				if(current == (char)0) return false;
				if(Objects.equals(key, Character.valueOf(current)) && Objects.equals(value, Long.valueOf(values[pos]))) {
					removeIndex(pos);
					return true;
				}
				while(true) {
					if((current = keys[pos = (++pos & mask)]) == (char)0) return false;
					else if(Objects.equals(key, Character.valueOf(current)) && Objects.equals(value, Long.valueOf(values[pos]))){
						removeIndex(pos);
						return true;
					}
				}				
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected boolean replace(int hash, char key, long oldValue, long newValue) {
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
		
		protected long replace(int hash, char key, long value) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) return getDefaultReturnValue();
				long oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected long compute(int hash, char key, CharLongUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					long newValue = mappingFunction.applyAsLong(key, getDefaultReturnValue());
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				long newValue = mappingFunction.applyAsLong(key, values[index]);
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
		
		protected long computeIfAbsent(int hash, char key, Char2LongFunction mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					long newValue = mappingFunction.get(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				long newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = mappingFunction.get(key);
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected long supplyIfAbsent(int hash, char key, LongSupplier valueProvider) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0) {
					long newValue = valueProvider.getLong();
					if(newValue == getDefaultReturnValue()) return newValue;
					insert(-index-1, key, newValue);
					return newValue;
				}
				long newValue = values[index];
				if(newValue == getDefaultReturnValue()) {
					newValue = valueProvider.getLong();
					if(newValue == getDefaultReturnValue()) return newValue;
					values[index] = newValue;
				}
				return newValue;
			}
			finally {
				unlockWrite(stamp);
			}
		}
		
		protected long computeIfPresent(int hash, char key, CharLongUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
				long newValue = mappingFunction.applyAsLong(key, values[index]);
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
		
		protected long merge(int hash, char key, long value, LongLongUnaryOperator mappingFunction) {
			long stamp = writeLock();
			try {
				int index = findIndex(hash, key);
				long newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(values[index], value);
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
				Arrays.fill(keys, (char)0);
				Arrays.fill(values, 0L);
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
				keys = new char[arraySize];
				values = new long[arraySize];
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
		
		protected void insert(int slot, char key, long value) {
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
		
		protected long removeIndex(int pos) {
			if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
			long value = values[pos];
			keys[pos] = (char)0;
			values[pos] = 0L;
			size--;
			onNodeRemoved(pos);
			shiftKeys(pos);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected long removeNullIndex() {
			long value = values[nullIndex];
			containsNull = false;
			keys[nullIndex] = (char)0;
			values[nullIndex] = 0L;
			size--;
			onNodeRemoved(nullIndex);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return value;
		}
		
		protected int findIndex(int hash, char key) {
			if(key == (char)0) return containsNull ? nullIndex : -(nullIndex + 1);
			int pos = hash & mask;
			char current = keys[pos];
			if(current != (char)0) {
				if(current == key) return pos;
				while((current = keys[pos = (++pos & mask)]) != (char)0)
					if(current == key) return pos;
			}
			return -(pos + 1);
		}
		
		protected int findIndex(int hash, Object key) {
			if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
			int pos = hash & mask;
			char current = keys[pos];
			if(current != (char)0) {
				if(Objects.equals(key, Character.valueOf(current))) return pos;
				while((current = keys[pos = (++pos & mask)]) != (char)0)
					if(Objects.equals(key, Character.valueOf(current))) return pos;
			}
			return -(pos + 1);
		}
		
		protected void shiftKeys(int startPos) {
			int slot, last;
			char current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == (char)0) {
						keys[last] = (char)0;
						values[last] = 0L;
						return;
					}
					slot = HashUtil.mix(Character.hashCode(current)) & mask;
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
			char[] newKeys = new char[arraySize];
			long[] newValues = new long[arraySize];
			long[] newLinks = new long[arraySize];
			int i = firstIndex, prev = -1, newPrev = -1, pos;
			firstIndex = -1;
			for(int j = size; j-- != 0;) {
				if(keys[i] == (char)0) pos = newSize;
				else {
					pos = HashUtil.mix(Character.hashCode(keys[i])) & newMask;
					while(newKeys[pos] != (char)0) pos = ++pos & newMask;
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