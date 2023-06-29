package speiger.src.collections.bytes.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.bytes.functions.consumer.ByteShortConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ShortFunction;
import speiger.src.collections.bytes.functions.function.ByteShortUnaryOperator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;

import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 */
public class Byte2ShortOpenCustomHashMap extends AbstractByte2ShortMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient byte[] keys;
	/** The Backing values array */
	protected transient short[] values;
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
	protected transient ByteSet keySet;
	/** Values cache */
	protected transient ShortCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final ByteStrategy strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Byte2ShortOpenCustomHashMap(ByteStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Byte2ShortOpenCustomHashMap(int minCapacity, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(int minCapacity, float loadFactor, ByteStrategy strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new byte[nullIndex + 1];
		values = new short[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2ShortOpenCustomHashMap(Byte[] keys, Short[] values, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(Byte[] keys, Short[] values, float loadFactor, ByteStrategy strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].byteValue(), values[i].shortValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2ShortOpenCustomHashMap(byte[] keys, short[] values, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(byte[] keys, short[] values, float loadFactor, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(Map<? extends Byte, ? extends Short> map, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(Map<? extends Byte, ? extends Short> map, float loadFactor, ByteStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Byte2ShortOpenCustomHashMap(Byte2ShortMap map, ByteStrategy strategy) {
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
	public Byte2ShortOpenCustomHashMap(Byte2ShortMap map, float loadFactor, ByteStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public short put(byte key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public short putIfAbsent(byte key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(values[slot] == getDefaultReturnValue()) {
			short oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public short addTo(byte key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public short subFrom(byte key, short value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		short oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(byte key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return key instanceof Byte && findIndex(((Byte)key).byteValue()) >= 0;
	}
	
	@Override
	public boolean containsValue(short value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], (byte)0) && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Short.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], (byte)0) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Short.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public short remove(byte key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public short removeOrDefault(byte key, short defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Short remove(Object key) {
		if(!(key instanceof Byte)) return getDefaultReturnValue();
		int slot = findIndex(((Byte)key).byteValue());
		if(slot < 0) return Short.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(byte key, short value) {
		if(strategy.equals(key, (byte)0)) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		byte current = keys[pos];
		if(strategy.equals(current, (byte)0)) return false;
		if(strategy.equals(current, key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0)) return false;
			else if(strategy.equals(current, key) && value == values[pos]) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null || (key instanceof Byte && strategy.equals(((Byte)key).byteValue(), (byte)0))) {
			if(containsNull && Objects.equals(value, Short.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		if(!(key instanceof Byte)) return false;
		byte keyType = ((Byte)key).byteValue();
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		byte current = keys[pos];
		if(strategy.equals(current, (byte)0)) return false;
		if(strategy.equals(current, keyType) && Objects.equals(value, Short.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Short.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public short get(byte key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Short get(Object key) {
		if(!(key instanceof Byte)) return getDefaultReturnValue();
		int slot = findIndex(((Byte)key).byteValue());
		return Short.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public short getOrDefault(byte key, short defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Byte2ShortOpenCustomHashMap copy() {
		Byte2ShortOpenCustomHashMap map = new Byte2ShortOpenCustomHashMap(0, loadFactor, strategy);
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
	public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ByteSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ByteShortConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], (byte)0)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(byte key, short oldValue, short newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public short replace(byte key, short value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.applyAsShort(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
		
	@Override
	public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = valueProvider.getAsShort();
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
	
	@Override
	public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = valueProvider.getAsShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		short newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2ShortMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
			int index = findIndex(key);
			short newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(values[index], entry.getShortValue());
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
		Arrays.fill(keys, (byte)0);
		Arrays.fill(values, (short)0);
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
		keys = new byte[request + 1];
		values = new short[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(byte key) {
		if(strategy.equals(key, (byte)0)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		byte current = keys[pos];
		if(!strategy.equals(current, (byte)0)) {
			if(strategy.equals(current, key)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0))
				if(strategy.equals(current, key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Byte key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		byte keyType = ((Byte)key).byteValue();
		if(strategy.equals(keyType, (byte)0)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		byte current = keys[pos];
		if(!strategy.equals(current, (byte)0)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
	}
	
	protected short removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		short value = values[pos];
		keys[pos] = (byte)0;
		values[pos] = (short)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected short removeNullIndex() {
		short value = values[nullIndex];
		containsNull = false;
		keys[nullIndex] = (byte)0;
		values[nullIndex] = (short)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, byte key, short value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		byte[] newKeys = new byte[newSize + 1];
		short[] newValues = new short[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(!strategy.equals(keys[i], (byte)0)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask], (byte)0))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], (byte)0));
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
		byte current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(strategy.equals((current = keys[startPos]), (byte)0)) {
					keys[last] = (byte)0;
					values[last] = (short)0;
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
		protected byte key;
		protected short value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public byte getByteKey() {
			return key;
		}

		@Override
		public short getShortValue() {
			return value;
		}
		
		@Override
		public short setValue(short value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Byte2ShortMap.Entry, Map.Entry<Byte, Short> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public byte getByteKey() {
			return keys[index];
		}

		@Override
		public short getShortValue() {
			return values[index];
		}

		@Override
		public short setValue(short value) {
			short oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2ShortMap.Entry) {
					Byte2ShortMap.Entry entry = (Byte2ShortMap.Entry)obj;
					return getByteKey() == entry.getByteKey() && getShortValue() == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Short && getByteKey() == ((Byte)key).byteValue() && getShortValue() == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return strategy.hashCode(getByteKey()) ^ Short.hashCode(getShortValue());
		}
		
		@Override
		public String toString() {
			return Byte.toString(getByteKey()) + "=" + Short.toString(getShortValue());
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Byte2ShortMap.Entry> implements Byte2ShortMap.FastEntrySet {
		@Override
		public ObjectIterator<Byte2ShortMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Byte2ShortMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Byte2ShortMap.Entry> action) {
			if(containsNull) action.accept(new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Byte2ShortMap.Entry> action) {
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Byte2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Byte2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Byte2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Byte2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(!filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Byte2ShortMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Byte2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Byte2ShortMap.Entry, Byte2ShortMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Byte2ShortMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new ValueMapEntry(nullIndex);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
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
		public Byte2ShortMap.Entry findFirst(Predicate<Byte2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					if(filter.test(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Byte2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Byte2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2ShortMap.Entry) {
					Byte2ShortMap.Entry entry = (Byte2ShortMap.Entry)o;
					int index = Byte2ShortOpenCustomHashMap.this.findIndex(entry.getByteKey());
					if(index >= 0) return entry.getShortValue() == Byte2ShortOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!(entry.getKey() instanceof Byte)) return false;
					int index = Byte2ShortOpenCustomHashMap.this.findIndex((Byte)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Short.valueOf(Byte2ShortOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2ShortMap.Entry) {
					Byte2ShortMap.Entry entry = (Byte2ShortMap.Entry)o;
					return Byte2ShortOpenCustomHashMap.this.remove(entry.getByteKey(), entry.getShortValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Byte2ShortOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractByteSet {
		@Override
		public boolean contains(byte e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(byte o) {
			int oldSize = size;
			Byte2ShortOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(byte o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ByteIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Byte2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(keys[i]);
		}
		
		@Override
		public void forEachIndexed(IntByteConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, keys[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(index++, keys[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && !filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			if(containsNull) state = operator.applyAsByte(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				state = operator.applyAsByte(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsByte(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public byte findFirst(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			if(containsNull && filter.test(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(keys[i])) return keys[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractShortCollection {
		@Override
		public boolean contains(short e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(short o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ShortIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Byte2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntShortConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, values[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			if(containsNull) state = operator.applyAsShort(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				state = operator.applyAsShort(state, values[i]);
			}
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsShort(state, values[i]);
			}
			return state;
		}
		
		@Override
		public short findFirst(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (short)0;
			if(containsNull && filter.test(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(values[i])) return values[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2ShortMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Byte2ShortMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Byte2ShortMap.Entry> {
		MapEntry entry;
		@Override
		public Byte2ShortMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements ByteIterator {
		@Override
		public byte nextByte() {
			return keys[nextEntry()];
		}
	}
	
	private class ValueIterator extends MapIterator implements ShortIterator {
		@Override
		public short nextShort() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		byte[] wrapped = null;
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
						if(!strategy.equals(keys[pos], (byte)0)){
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
				keys[nullIndex] = (byte)0;
				values[nullIndex] = (short)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Byte2ShortOpenCustomHashMap.this.remove(wrapped[-returnedPos - 1]);
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			byte current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(strategy.equals((current = keys[startPos]), (byte)0)) {
						keys[last] = (byte)0;
						values[last] = (short)0;
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
		
		private void addWrapper(byte value) {
			if(wrapped == null) wrapped = new byte[2];
			else if(wrappedIndex >= wrapped.length) {
				byte[] newArray = new byte[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}