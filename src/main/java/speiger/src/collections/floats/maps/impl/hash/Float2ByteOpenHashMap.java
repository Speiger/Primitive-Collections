package speiger.src.collections.floats.maps.impl.hash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.consumer.FloatByteConsumer;
import speiger.src.collections.floats.functions.function.Float2ByteFunction;
import speiger.src.collections.floats.functions.function.FloatByteUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.floats.utils.maps.Float2ByteMaps;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
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
public class Float2ByteOpenHashMap extends AbstractFloat2ByteMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient float[] keys;
	/** The Backing values array */
	protected transient byte[] values;
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
	protected transient ByteCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	
	/**
	 * Default Constructor
	 */
	public Float2ByteOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2ByteOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ByteOpenHashMap(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new float[nullIndex + 1];
		values = new byte[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ByteOpenHashMap(Float[] keys, Byte[] values) {
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
	public Float2ByteOpenHashMap(Float[] keys, Byte[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i].byteValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ByteOpenHashMap(float[] keys, byte[] values) {
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
	public Float2ByteOpenHashMap(float[] keys, byte[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2ByteOpenHashMap(Map<? extends Float, ? extends Byte> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2ByteOpenHashMap(Map<? extends Float, ? extends Byte> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2ByteOpenHashMap(Float2ByteMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Float2ByteOpenHashMap(Float2ByteMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public byte put(float key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		byte oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public byte putIfAbsent(float key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		return values[slot];
	}
	
	@Override
	public byte addTo(float key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		byte oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public byte subFrom(float key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		byte oldValue = values[slot];
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
	public boolean containsValue(byte value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Float.floatToIntBits(keys[i]) != 0 && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Float.floatToIntBits(keys[i]) != 0 && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public byte remove(float key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public byte removeOrDefault(float key, byte defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Byte remove(Object key) {
		int slot = findIndex(key);
		if(slot < 0) return Byte.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(float key, byte value) {
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
		if(key == null) {
			if(containsNull && Objects.equals(value, Byte.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(key.hashCode()) & mask;
		float current = keys[pos];
		if(Float.floatToIntBits(current) == 0) return false;
		if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Byte.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) == 0) return false;
			else if(Objects.equals(key, Float.valueOf(current)) && Objects.equals(value, Byte.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public byte get(float key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Byte get(Object key) {
		int slot = findIndex(key);
		return Byte.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public byte getOrDefault(float key, byte defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Float2ByteOpenHashMap copy() {
		Float2ByteOpenHashMap map = new Float2ByteOpenHashMap(0, loadFactor);
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
	public ObjectSet<Float2ByteMap.Entry> float2ByteEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ByteCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(FloatByteConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(Float.floatToIntBits(keys[i]) != 0) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(float key, byte oldValue, byte newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public byte replace(float key, byte value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte computeByte(float key, FloatByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
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
	
	@Override
	public byte computeByteIfAbsent(float key, Float2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte supplyByteIfAbsent(float key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
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
	
	@Override
	public byte computeByteIfPresent(float key, FloatByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte mergeByte(float key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		byte newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Float2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2ByteMap.Entry entry : Float2ByteMaps.fastIterable(m)) {
			float key = entry.getFloatKey();
			int index = findIndex(key);
			byte newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(values[index], entry.getByteValue());
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
		Arrays.fill(values, (byte)0);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= size || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= size) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new float[request + 1];
		values = new byte[request + 1];
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
		int pos = HashUtil.mix(key.hashCode()) & mask;
		float current = keys[pos];
		if(Float.floatToIntBits(current) != 0) {
			if(Objects.equals(key, Float.valueOf(current))) return pos;
			while(Float.floatToIntBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Objects.equals(key, Float.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected byte removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		byte value = values[pos];
		keys[pos] = 0F;
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
		keys[nullIndex] = 0F;
		values[nullIndex] = (byte)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, float key, byte value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		float[] newKeys = new float[newSize + 1];
		byte[] newValues = new byte[newSize + 1];
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
					values[last] = (byte)0;
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
	
	protected class MapEntry implements Float2ByteMap.Entry, Map.Entry<Float, Byte> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public float getFloatKey() {
			return keys[index];
		}

		@Override
		public byte getByteValue() {
			return values[index];
		}

		@Override
		public byte setValue(byte value) {
			byte oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2ByteMap.Entry) {
					Float2ByteMap.Entry entry = (Float2ByteMap.Entry)obj;
					return Float.floatToIntBits(keys[index]) == Float.floatToIntBits(entry.getFloatKey()) && values[index] == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Byte && Float.floatToIntBits(keys[index]) == Float.floatToIntBits(((Float)key).floatValue()) && values[index] == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(keys[index]) ^ Byte.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Float.toString(keys[index]) + "=" + Byte.toString(values[index]);
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Float2ByteMap.Entry> implements Float2ByteMap.FastEntrySet {
		@Override
		public ObjectIterator<Float2ByteMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Float2ByteMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Float2ByteMap.Entry> action) {
			if(containsNull) action.accept(new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--)
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2ByteMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Float2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Float2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Float2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(!filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(!filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2ByteMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Float2ByteMap.Entry reduce(ObjectObjectUnaryOperator<Float2ByteMap.Entry, Float2ByteMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2ByteMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new BasicEntry(keys[nullIndex], values[nullIndex]);
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = new BasicEntry(keys[i], values[i]);
					continue;
				}
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Float2ByteMap.Entry findFirst(Object2BooleanFunction<Float2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Float2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Float2ByteOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ByteOpenHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2ByteMap.Entry) {
					Float2ByteMap.Entry entry = (Float2ByteMap.Entry)o;
					int index = Float2ByteOpenHashMap.this.findIndex(entry.getFloatKey());
					if(index >= 0) return entry.getByteValue() == Float2ByteOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Float2ByteOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(Float2ByteOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2ByteMap.Entry) {
					Float2ByteMap.Entry entry = (Float2ByteMap.Entry)o;
					return Float2ByteOpenHashMap.this.remove(entry.getFloatKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2ByteOpenHashMap.this.remove(entry.getKey(), entry.getValue());
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
			Float2ByteOpenHashMap.this.remove(o);
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
			return Float2ByteOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ByteOpenHashMap.this.clear();
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
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && !filter.get(keys[i])) return false;
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
			for(int i = 0;i<size;i++) {
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
		public float findFirst(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0F;
			if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(keys[i])) return keys[i];
			}
			return 0F;
		}
		
		@Override
		public int count(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(keys[i])) result++;
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
			return Float2ByteOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2ByteOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(values[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && !filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			if(containsNull) state = operator.applyAsByte(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				state = operator.applyAsByte(state, values[i]);
			}
			return state;
		}
		
		@Override
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(Float.floatToIntBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsByte(state, values[i]);
			}
			return state;
		}
		
		@Override
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			if(containsNull && filter.get(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(values[i])) return values[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Float.floatToIntBits(keys[i]) != 0 && filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Float2ByteMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Float2ByteMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Float2ByteMap.Entry> {
		MapEntry entry;
		@Override
		public Float2ByteMap.Entry next() {
			return entry = new MapEntry(nextEntry());
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
	
	private class ValueIterator extends MapIterator implements ByteIterator {
		@Override
		public byte nextByte() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		FloatList wrapped = null;
		
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
							if(wrapped == null || wrapped.size() <= -pos - 1) break;
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
				int value = findIndex(wrapped.getFloat(nextIndex));
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
				values[nullIndex] = (byte)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Float2ByteOpenHashMap.this.remove(wrapped.getFloat(-returnedPos - 1));
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
						values[last] = (byte)0;
						return;
					}
					slot = HashUtil.mix(Float.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new FloatArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
	}
}