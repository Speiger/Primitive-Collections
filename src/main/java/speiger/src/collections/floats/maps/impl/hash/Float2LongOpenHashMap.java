package speiger.src.collections.floats.maps.impl.hash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.LongPredicate;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntLongConsumer;
import speiger.src.collections.floats.functions.consumer.FloatLongConsumer;
import speiger.src.collections.floats.functions.function.Float2LongFunction;
import speiger.src.collections.floats.functions.function.FloatLongUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class Float2LongOpenHashMap extends AbstractFloat2LongMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient float[] keys;
	/** The Backing values array */
	protected transient long[] values;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Minimum array size the HashMap will be */
	protected transient int minCapacity;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** EntrySet cache */
	protected transient FastEntrySet entrySet;
	/** KeySet cache */
	protected transient FloatSet keySet;
	/** Values cache */
	protected transient LongCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	
	/**
	 * Default Constructor
	 */
	public Float2LongOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2LongOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2LongOpenHashMap(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new float[nullIndex + 1];
		values = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2LongOpenHashMap(Float[] keys, Long[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2LongOpenHashMap(Float[] keys, Long[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i].longValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2LongOpenHashMap(float[] keys, long[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2LongOpenHashMap(float[] keys, long[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2LongOpenHashMap(Map<? extends Float, ? extends Long> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2LongOpenHashMap(Map<? extends Float, ? extends Long> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2LongOpenHashMap(Float2LongMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Float2LongOpenHashMap(Float2LongMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public long put(float key, long value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		long oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public long putIfAbsent(float key, long value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(values[slot] == getDefaultReturnValue()) {
			long oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public long addTo(float key, long value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		long oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public long subFrom(float key, long value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		long oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(float key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(long value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Float.floatToIntBits(keys[i]) != 0 && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Long.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Float.floatToIntBits(keys[i]) != 0 && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Long.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public long remove(float key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public long removeOrDefault(float key, long defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Long remove(Object key) {
		int slot = findIndex(key);
		if(slot < 0) return Long.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(float key, long value) {
		if(Float.floatToIntBits(key) == 0) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(Float.hashCode(key)) & mask;
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
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null || (key instanceof Float && Float.floatToIntBits(((Float)key).floatValue()) == 0)) {
			if(containsNull && Objects.equals(value, Long.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(key.hashCode()) & mask;
		float current = keys[pos];
		if(Float.floatToIntBits(current) == 0) return false;
		if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Long.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
			else if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Long.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public long get(float key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Long get(Object key) {
		int slot = findIndex(key);
		return Long.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public long getOrDefault(float key, long defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Float2LongOpenHashMap copy() {
		Float2LongOpenHashMap map = new Float2LongOpenHashMap(0, loadFactor);
		map.minCapacity = minCapacity;
		map.mask = mask;
		map.maxFill = maxFill;
		map.nullIndex = nullIndex;
		map.containsNull = containsNull;
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, values.length);
		return map;
	}
	
	@Override
	public ObjectSet<Float2LongMap.Entry> float2LongEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public LongCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(FloatLongConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(Float.floatToIntBits(keys[i]) != 0) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(float key, long oldValue, long newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public long replace(float key, long value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		long oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public long computeLong(float key, FloatLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			long newValue = mappingFunction.applyAsLong(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
			return newValue;
		}
		long newValue = mappingFunction.applyAsLong(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
		
	@Override
	public long computeLongIfAbsent(float key, Float2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			long newValue = mappingFunction.applyAsLong(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		long newValue = values[index];
		return newValue;
	}
	
	@Override
	public long supplyLongIfAbsent(float key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			long newValue = valueProvider.getAsLong();
			insert(-index-1, key, newValue);
			return newValue;
		}
		long newValue = values[index];
		return newValue;
	}
		
	@Override
	public long computeLongIfPresent(float key, FloatLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		long newValue = mappingFunction.applyAsLong(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public long computeLongNonDefault(float key, FloatLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
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
	
	@Override
	public long computeLongIfAbsentNonDefault(float key, Float2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			long newValue = mappingFunction.applyAsLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		long newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public long supplyLongIfAbsentNonDefault(float key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			long newValue = valueProvider.getAsLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		long newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public long computeLongIfPresentNonDefault(float key, FloatLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		long newValue = mappingFunction.applyAsLong(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public long mergeLong(float key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		long newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Float2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2LongMap.Entry entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
			int index = findIndex(key);
			long newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(values[index], entry.getLongValue());
			if(newValue == getDefaultReturnValue()) {
				if(index >= 0)
					removeIndex(index);
			}
			else if(index < 0) insert(-index-1, key, newValue);
			else values[index] = newValue;
		}
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		containsNull = false;
		Arrays.fill(keys, 0F);
		Arrays.fill(values, 0L);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex || this.size >= Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new float[request + 1];
		values = new long[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(float key) {
		if(Float.floatToIntBits(key) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Float.hashCode(key)) & mask;
		float current = keys[pos];
		if(Float.floatToIntBits(current) != 0) {
			if(Float.floatToIntBits(current) == Float.floatToIntBits(key)) return pos;
			while(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Float.floatToIntBits(current) == Float.floatToIntBits(key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		if(key instanceof Float && Float.floatToIntBits(((Float)key).floatValue()) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		float current = keys[pos];
		if(Float.floatToIntBits(current) != 0) {
			if(Objects.equals(key, Float.valueOf(current))) return pos;
			while(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Objects.equals(key, Float.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected long removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		long value = values[pos];
		keys[pos] = 0F;
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
		keys[nullIndex] = 0F;
		values[nullIndex] = 0L;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, float key, long value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		float[] newKeys = new float[newSize + 1];
		long[] newValues = new long[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(Float.floatToIntBits(keys[i]) != 0) break;
			}
			if(Float.floatToIntBits(newKeys[pos = HashUtil.mix(Float.hashCode(keys[i])) & newMask]) != 0)
				while(Float.floatToIntBits(newKeys[pos = (++pos & newMask)]) != 0);
			newKeys[pos] = keys[i];
			newValues[pos] = values[i];
		}
		newValues[newSize] = values[nullIndex];
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
		values = newValues;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		float current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(Float.floatToIntBits((current = keys[startPos])) == 0) {
					keys[last] = 0F;
					values[last] = 0L;
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
	
	protected class ValueMapEntry extends MapEntry {
		protected float key;
		protected long value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public float getFloatKey() {
			return key;
		}

		@Override
		public long getLongValue() {
			return value;
		}
		
		@Override
		public long setValue(long value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Float2LongMap.Entry, Map.Entry<Float, Long> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public float getFloatKey() {
			return keys[index];
		}

		@Override
		public long getLongValue() {
			return values[index];
		}

		@Override
		public long setValue(long value) {
			long oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2LongMap.Entry) {
					Float2LongMap.Entry entry = (Float2LongMap.Entry)obj;
					return Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(entry.getFloatKey()) && getLongValue() == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Long && Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(((Float)key).floatValue()) && getLongValue() == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(getFloatKey()) ^ Long.hashCode(getLongValue());
		}
		
		@Override
		public String toString() {
			return Float.toString(getFloatKey()) + "=" + Long.toString(getLongValue());
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Float2LongMap.Entry> implements Float2LongMap.FastEntrySet {
		@Override
		public ObjectIterator<Float2LongMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Float2LongMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Float2LongMap.Entry> action) {
			if(containsNull) action.accept(new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--)
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2LongMap.Entry> action) {
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Float2LongMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2LongMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Float2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Float2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Float2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(!filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2LongMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Float2LongMap.Entry reduce(ObjectObjectUnaryOperator<Float2LongMap.Entry, Float2LongMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2LongMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new ValueMapEntry(nullIndex);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = new ValueMapEntry(i);
					continue;
				}
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Float2LongMap.Entry findFirst(Predicate<Float2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					if(filter.test(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Float2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Float2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2LongOpenHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2LongMap.Entry) {
					Float2LongMap.Entry entry = (Float2LongMap.Entry)o;
					int index = Float2LongOpenHashMap.this.findIndex(entry.getFloatKey());
					if(index >= 0) return entry.getLongValue() == Float2LongOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Float2LongOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Long.valueOf(Float2LongOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2LongMap.Entry) {
					Float2LongMap.Entry entry = (Float2LongMap.Entry)o;
					return Float2LongOpenHashMap.this.remove(entry.getFloatKey(), entry.getLongValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2LongOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractFloatSet {
		@Override
		public boolean contains(float e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(float o) {
			int oldSize = size;
			Float2LongOpenHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(float o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public FloatIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Float2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2LongOpenHashMap.this.clear();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(FloatConsumer action) {
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(keys[i]);
		}
		
		@Override
		public void forEachIndexed(IntFloatConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, keys[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(index++, keys[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && !filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			if(containsNull) state = operator.applyAsFloat(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				state = operator.applyAsFloat(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public float reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsFloat(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public float findFirst(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0F;
			if(containsNull && filter.test(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(keys[i])) return keys[i];
			}
			return 0F;
		}
		
		@Override
		public int count(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(keys[i])) result++;
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
			return Float2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2LongOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(LongConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntLongConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, values[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(LongPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(LongPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(LongPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			if(containsNull) state = operator.applyAsLong(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				state = operator.applyAsLong(state, values[i]);
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsLong(state, values[i]);
			}
			return state;
		}
		
		@Override
		public long findFirst(LongPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			if(containsNull && filter.test(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(values[i])) return values[i];
			}
			return 0L;
		}
		
		@Override
		public int count(LongPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Float2LongMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Float2LongMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Float2LongMap.Entry> {
		MapEntry entry;
		@Override
		public Float2LongMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements FloatIterator {
		@Override
		public float nextFloat() {
			return keys[nextEntry()];
		}
	}
	
	private class ValueIterator extends MapIterator implements LongIterator {
		@Override
		public long nextLong() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		float[] wrapped = null;
		int wrappedIndex = 0;
		
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				if(returnNull) {
					returnNull = false;
					nextIndex = nullIndex;
				}
				else
				{
					while(true) {
						if(--pos < 0) {
							if(wrapped == null || wrappedIndex <= -pos - 1) break;
							nextIndex = -pos - 1;
							break;
						}
						if(Float.floatToIntBits(keys[pos]) != 0){
							nextIndex = pos;
							break;
						}
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				int value = findIndex(wrapped[nextIndex]);
				if(value < 0) throw new IllegalStateException("Entry ["+nextIndex+"] was removed during Iteration");
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			int value = (lastReturned = nextIndex);
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(lastReturned == nullIndex) {
				containsNull = false;
				keys[nullIndex] = 0F;
				values[nullIndex] = 0L;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Float2LongOpenHashMap.this.remove(wrapped[-returnedPos - 1]);
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			float current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(Float.floatToIntBits((current = keys[startPos])) == 0) {
						keys[last] = 0F;
						values[last] = 0L;
						return;
					}
					slot = HashUtil.mix(Float.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) addWrapper(keys[startPos]);
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
		
		private void addWrapper(float value) {
			if(wrapped == null) wrapped = new float[2];
			else if(wrappedIndex >= wrapped.length) {
				float[] newArray = new float[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}