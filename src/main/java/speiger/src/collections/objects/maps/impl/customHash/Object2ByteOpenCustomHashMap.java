package speiger.src.collections.objects.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.ToByteFunction;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;


import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Object2ByteOpenCustomHashMap<T> extends AbstractObject2ByteMap<T> implements ITrimmable
{
	/** The Backing keys array */
	protected transient T[] keys;
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
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient ByteCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final ObjectStrategy<? super T> strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2ByteOpenCustomHashMap(ObjectStrategy<? super T> strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2ByteOpenCustomHashMap(int minCapacity, ObjectStrategy<? super T> strategy) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteOpenCustomHashMap(int minCapacity, float loadFactor, ObjectStrategy<? super T> strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = (T[])new Object[nullIndex + 1];
		values = new byte[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ByteOpenCustomHashMap(T[] keys, Byte[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteOpenCustomHashMap(T[] keys, Byte[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].byteValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ByteOpenCustomHashMap(T[] keys, byte[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteOpenCustomHashMap(T[] keys, byte[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2ByteOpenCustomHashMap(Map<? extends T, ? extends Byte> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2ByteOpenCustomHashMap(Map<? extends T, ? extends Byte> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2ByteOpenCustomHashMap(Object2ByteMap<T> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Object2ByteOpenCustomHashMap(Object2ByteMap<T> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public byte put(T key, byte value) {
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
	public byte putIfAbsent(T key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(values[slot] == getDefaultReturnValue()) {
			byte oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public byte addTo(T key, byte value) {
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
	public byte subFrom(T key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		byte oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		return findIndex((T)key) >= 0;
	}
	
	@Override
	public boolean containsValue(byte value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], null) && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], null) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public byte rem(T key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public byte remOrDefault(T key, byte defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Byte remove(Object key) {
		int slot = findIndex((T)key);
		if(slot < 0) return Byte.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(T key, byte value) {
		if(strategy.equals(key, null)) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		T current = keys[pos];
		if(strategy.equals(current, null)) return false;
		if(strategy.equals(current, key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), null)) return false;
			else if(strategy.equals(current, key) && value == values[pos]) {
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
		T keyType = (T)key;
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		T current = keys[pos];
		if(strategy.equals(current, null)) return false;
		if(strategy.equals(current, keyType) && Objects.equals(value, Byte.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), null)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Byte.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public byte getByte(T key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Byte get(Object key) {
		int slot = findIndex((T)key);
		return Byte.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public byte getOrDefault(T key, byte defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Object2ByteOpenCustomHashMap<T> copy() {
		Object2ByteOpenCustomHashMap<T> map = new Object2ByteOpenCustomHashMap<>(0, loadFactor, strategy);
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
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectByteConsumer<T> action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], null)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, byte oldValue, byte newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public byte replace(T key, byte value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = mappingFunction.applyAsByte(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(T key, ToByteFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = mappingFunction.applyAsByte(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		return newValue;
	}
		
	@Override
	public byte supplyByteIfAbsent(T key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = valueProvider.getAsByte();
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		return newValue;
	}
	
	@Override
	public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction) {
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
	public byte computeByteIfAbsentNonDefault(T key, ToByteFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = mappingFunction.applyAsByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte supplyByteIfAbsentNonDefault(T key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			byte newValue = valueProvider.getAsByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte computeByteIfPresentNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction) {
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
	public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction) {
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
	public void mergeAllByte(Object2ByteMap<T> m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ByteMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
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
		Arrays.fill(keys, null);
		Arrays.fill(values, (byte)0);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
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
		keys = (T[])new Object[request + 1];
		values = new byte[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(T key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		T keyType = (T)key;
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		T current = keys[pos];
		if(!strategy.equals(current, null)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), null))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
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
	
	protected void insert(int slot, T key, byte value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		T[] newKeys = (T[])new Object[newSize + 1];
		byte[] newValues = new byte[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(!strategy.equals(keys[i], null)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask], null))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], null));
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
		T current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(strategy.equals((current = keys[startPos]), null)) {
					keys[last] = null;
					values[last] = (byte)0;
					return;
				}
				slot = HashUtil.mix(strategy.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			values[last] = values[startPos];
			onNodeMoved(startPos, last);
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected byte value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public byte getByteValue() {
			return value;
		}
		
		@Override
		public byte setValue(byte value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2ByteMap.Entry<T>, Map.Entry<T, Byte> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public T getKey() {
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
			return strategy.hashCode(getKey()) ^ Byte.hashCode(getByteValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Byte.toString(getByteValue());
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Object2ByteMap.Entry<T>> implements Object2ByteMap.FastEntrySet<T> {
		@Override
		public ObjectIterator<Object2ByteMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Object2ByteMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			if(containsNull) action.accept(new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2ByteMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ByteMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(!filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2ByteMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Object2ByteMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2ByteMap.Entry<T>, Object2ByteMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2ByteMap.Entry<T> state = null;
			boolean empty = true;
			if(containsNull) {
				state = new ValueMapEntry(nullIndex);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
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
		public Object2ByteMap.Entry<T> findFirst(Predicate<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					if(filter.test(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Object2ByteMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Object2ByteOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					int index = Object2ByteOpenCustomHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getByteValue() == Object2ByteOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2ByteOpenCustomHashMap.this.findIndex((T)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(Object2ByteOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					return Object2ByteOpenCustomHashMap.this.remove(entry.getKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2ByteOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> {
		@Override
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2ByteOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Object2ByteOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(keys[i]);
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, keys[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(index++, keys[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && !filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				state = operator.apply(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.apply(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public T findFirst(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			if(containsNull && filter.test(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(keys[i])) return keys[i];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(keys[i])) result++;
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
			return Object2ByteOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ByteOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntByteConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, values[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			if(containsNull) state = operator.applyAsByte(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
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
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
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
		public byte findFirst(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			if(containsNull && filter.test(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(values[i])) return values[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2ByteMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		@Override
		public Object2ByteMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Object2ByteMap.Entry<T>> {
		MapEntry entry;
		@Override
		public Object2ByteMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
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
		T[] wrapped = null;
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
						if(!strategy.equals(keys[pos], null)){
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
				keys[nullIndex] = null;
				values[nullIndex] = (byte)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Object2ByteOpenCustomHashMap.this.remove(wrapped[-returnedPos - 1]);
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			T current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(strategy.equals((current = keys[startPos]), null)) {
						keys[last] = null;
						values[last] = (byte)0;
						return;
					}
					slot = HashUtil.mix(strategy.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) addWrapper(keys[startPos]);
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
		
		private void addWrapper(T value) {
			if(wrapped == null) wrapped = (T[])new Object[2];
			else if(wrappedIndex >= wrapped.length) {
				T[] newArray = (T[])new Object[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}