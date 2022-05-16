package speiger.src.collections.bytes.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.consumer.ByteDoubleConsumer;
import speiger.src.collections.bytes.functions.function.Byte2DoubleFunction;
import speiger.src.collections.bytes.functions.function.ByteDoubleUnaryOperator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.utils.maps.Byte2DoubleMaps;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 */
public class Byte2DoubleOpenCustomHashMap extends AbstractByte2DoubleMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient byte[] keys;
	/** The Backing values array */
	protected transient double[] values;
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
	protected transient DoubleCollection valuesC;
	
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
	public Byte2DoubleOpenCustomHashMap(ByteStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Byte2DoubleOpenCustomHashMap(int minCapacity, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(int minCapacity, float loadFactor, ByteStrategy strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new byte[nullIndex + 1];
		values = new double[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2DoubleOpenCustomHashMap(Byte[] keys, Double[] values, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(Byte[] keys, Double[] values, float loadFactor, ByteStrategy strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].byteValue(), values[i].doubleValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2DoubleOpenCustomHashMap(byte[] keys, double[] values, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(byte[] keys, double[] values, float loadFactor, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(Map<? extends Byte, ? extends Double> map, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(Map<? extends Byte, ? extends Double> map, float loadFactor, ByteStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Byte2DoubleOpenCustomHashMap(Byte2DoubleMap map, ByteStrategy strategy) {
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
	public Byte2DoubleOpenCustomHashMap(Byte2DoubleMap map, float loadFactor, ByteStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public double put(byte key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public double putIfAbsent(byte key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		return values[slot];
	}
	
	@Override
	public double addTo(byte key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public double subFrom(byte key, double value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		double oldValue = values[slot];
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
	public boolean containsValue(double value) {
		if(containsNull && Double.doubleToLongBits(values[nullIndex]) == Double.doubleToLongBits(value)) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], (byte)0) && Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Double.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], (byte)0) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Double.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public double remove(byte key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public double removeOrDefault(byte key, double defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Double remove(Object key) {
		if(!(key instanceof Byte)) return getDefaultReturnValue();
		int slot = findIndex(((Byte)key).byteValue());
		if(slot < 0) return Double.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(byte key, double value) {
		if(strategy.equals(key, (byte)0)) {
			if(containsNull && Double.doubleToLongBits(value) == Double.doubleToLongBits(values[nullIndex])) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		byte current = keys[pos];
		if(strategy.equals(current, (byte)0)) return false;
		if(strategy.equals(current, key) && Double.doubleToLongBits(value) == Double.doubleToLongBits(values[pos])) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0)) return false;
			else if(strategy.equals(current, key) && Double.doubleToLongBits(value) == Double.doubleToLongBits(values[pos])) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null) {
			if(containsNull && Objects.equals(value, Double.valueOf(values[nullIndex]))) {
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
		if(strategy.equals(current, keyType) && Objects.equals(value, Double.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Double.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public double get(byte key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Double get(Object key) {
		if(!(key instanceof Byte)) return getDefaultReturnValue();
		int slot = findIndex(((Byte)key).byteValue());
		return Double.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public double getOrDefault(byte key, double defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Byte2DoubleOpenCustomHashMap copy() {
		Byte2DoubleOpenCustomHashMap map = new Byte2DoubleOpenCustomHashMap(0, loadFactor, strategy);
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
	public ObjectSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ByteSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ByteDoubleConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], (byte)0)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(byte key, double oldValue, double newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public double replace(byte key, double value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public double computeDouble(byte key, ByteDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			double newValue = mappingFunction.applyAsDouble(key, getDefaultReturnValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(byte key, Byte2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			double newValue = mappingFunction.get(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = mappingFunction.get(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public double supplyDoubleIfAbsent(byte key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			double newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}

	@Override
	public double computeDoubleIfPresent(byte key, ByteDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue())) return getDefaultReturnValue();
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double mergeDouble(byte key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		double newValue = index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(values[index], value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Byte2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2DoubleMap.Entry entry : Byte2DoubleMaps.fastIterable(m)) {
			byte key = entry.getByteKey();
			int index = findIndex(key);
			double newValue = index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(values[index], entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
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
		Arrays.fill(values, 0D);
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
		keys = new byte[request + 1];
		values = new double[request + 1];
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
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		byte current = keys[pos];
		if(!strategy.equals(current, (byte)0)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), (byte)0))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
	}
	
	protected double removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		double value = values[pos];
		keys[pos] = (byte)0;
		values[pos] = 0D;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected double removeNullIndex() {
		double value = values[nullIndex];
		containsNull = false;
		keys[nullIndex] = (byte)0;
		values[nullIndex] = 0D;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, byte key, double value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		byte[] newKeys = new byte[newSize + 1];
		double[] newValues = new double[newSize + 1];
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
					values[last] = 0D;
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
	
	protected class MapEntry implements Byte2DoubleMap.Entry, Map.Entry<Byte, Double> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public byte getByteKey() {
			return keys[index];
		}

		@Override
		public double getDoubleValue() {
			return values[index];
		}

		@Override
		public double setValue(double value) {
			double oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2DoubleMap.Entry) {
					Byte2DoubleMap.Entry entry = (Byte2DoubleMap.Entry)obj;
					return keys[index] == entry.getByteKey() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Double && keys[index] == ((Byte)key).byteValue() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return strategy.hashCode(keys[index]) ^ Double.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Byte.toString(keys[index]) + "=" + Double.toString(values[index]);
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Byte2DoubleMap.Entry> implements Byte2DoubleMap.FastEntrySet {
		@Override
		public ObjectIterator<Byte2DoubleMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Byte2DoubleMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Byte2DoubleMap.Entry> action) {
			if(containsNull) action.accept(new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Byte2DoubleMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2DoubleMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Byte2DoubleMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Byte2DoubleMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Byte2DoubleMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(!filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					if(!filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Byte2DoubleMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Byte2DoubleMap.Entry reduce(ObjectObjectUnaryOperator<Byte2DoubleMap.Entry, Byte2DoubleMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Byte2DoubleMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new BasicEntry(keys[nullIndex], values[nullIndex]);
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(strategy.equals(keys[i], (byte)0)) continue;
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
		public Byte2DoubleMap.Entry findFirst(Object2BooleanFunction<Byte2DoubleMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Byte2DoubleMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Byte2DoubleOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2DoubleOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2DoubleMap.Entry) {
					Byte2DoubleMap.Entry entry = (Byte2DoubleMap.Entry)o;
					int index = Byte2DoubleOpenCustomHashMap.this.findIndex(entry.getByteKey());
					if(index >= 0) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(Byte2DoubleOpenCustomHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!(entry.getKey() instanceof Byte)) return false;
					int index = Byte2DoubleOpenCustomHashMap.this.findIndex((Byte)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Double.valueOf(Byte2DoubleOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2DoubleMap.Entry) {
					Byte2DoubleMap.Entry entry = (Byte2DoubleMap.Entry)o;
					return Byte2DoubleOpenCustomHashMap.this.remove(entry.getByteKey(), entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Byte2DoubleOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
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
			Byte2DoubleOpenCustomHashMap.this.remove(o);
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
			return Byte2DoubleOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2DoubleOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(keys[i]);
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
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && !filter.get(keys[i])) return false;
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
			for(int i = 0;i<size;i++) {
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
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(keys[i])) return keys[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractDoubleCollection {
		@Override
		public boolean contains(double e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(double o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public DoubleIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Byte2DoubleOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2DoubleOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], (byte)0)) action.accept(values[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && !filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			if(containsNull) state = operator.applyAsDouble(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				state = operator.applyAsDouble(state, values[i]);
			}
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(strategy.equals(keys[i], (byte)0)) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsDouble(state, values[i]);
			}
			return state;
		}
		
		@Override
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0D;
			if(containsNull && filter.get(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(values[i])) return values[i];
			}
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], (byte)0) && filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2DoubleMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Byte2DoubleMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Byte2DoubleMap.Entry> {
		MapEntry entry;
		@Override
		public Byte2DoubleMap.Entry next() {
			return entry = new MapEntry(nextEntry());
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
	
	private class ValueIterator extends MapIterator implements DoubleIterator {
		@Override
		public double nextDouble() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		ByteList wrapped = null;
		
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
				int value = findIndex(wrapped.getByte(nextIndex));
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
				values[nullIndex] = 0D;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Byte2DoubleOpenCustomHashMap.this.remove(wrapped.getByte(-returnedPos - 1));
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
						values[last] = 0D;
						return;
					}
					slot = HashUtil.mix(strategy.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new ByteArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
	}
}