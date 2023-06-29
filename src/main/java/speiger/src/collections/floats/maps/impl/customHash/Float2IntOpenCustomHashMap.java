package speiger.src.collections.floats.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.floats.functions.consumer.FloatIntConsumer;
import speiger.src.collections.floats.functions.function.Float2IntFunction;
import speiger.src.collections.floats.functions.function.FloatIntUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 */
public class Float2IntOpenCustomHashMap extends AbstractFloat2IntMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient float[] keys;
	/** The Backing values array */
	protected transient int[] values;
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
	protected transient IntCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final FloatStrategy strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Float2IntOpenCustomHashMap(FloatStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2IntOpenCustomHashMap(int minCapacity, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(int minCapacity, float loadFactor, FloatStrategy strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new float[nullIndex + 1];
		values = new int[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2IntOpenCustomHashMap(Float[] keys, Integer[] values, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(Float[] keys, Integer[] values, float loadFactor, FloatStrategy strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2IntOpenCustomHashMap(float[] keys, int[] values, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(float[] keys, int[] values, float loadFactor, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(Map<? extends Float, ? extends Integer> map, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(Map<? extends Float, ? extends Integer> map, float loadFactor, FloatStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Float2IntOpenCustomHashMap(Float2IntMap map, FloatStrategy strategy) {
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
	public Float2IntOpenCustomHashMap(Float2IntMap map, float loadFactor, FloatStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public int put(float key, int value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		int oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public int putIfAbsent(float key, int value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(values[slot] == getDefaultReturnValue()) {
			int oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public int addTo(float key, int value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		int oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public int subFrom(float key, int value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		int oldValue = values[slot];
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
		return key instanceof Float && findIndex(((Float)key).floatValue()) >= 0;
	}
	
	@Override
	public boolean containsValue(int value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], 0F) && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Integer.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], 0F) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Integer.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public int remove(float key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public int removeOrDefault(float key, int defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Integer remove(Object key) {
		if(!(key instanceof Float)) return getDefaultReturnValue();
		int slot = findIndex(((Float)key).floatValue());
		if(slot < 0) return Integer.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(float key, int value) {
		if(strategy.equals(key, 0F)) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		float current = keys[pos];
		if(strategy.equals(current, 0F)) return false;
		if(strategy.equals(current, key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0F)) return false;
			else if(strategy.equals(current, key) && value == values[pos]) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null || (key instanceof Float && strategy.equals(((Float)key).floatValue(), 0F))) {
			if(containsNull && Objects.equals(value, Integer.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		if(!(key instanceof Float)) return false;
		float keyType = ((Float)key).floatValue();
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		float current = keys[pos];
		if(strategy.equals(current, 0F)) return false;
		if(strategy.equals(current, keyType) && Objects.equals(value, Integer.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0F)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Integer.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public int get(float key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Integer get(Object key) {
		if(!(key instanceof Float)) return getDefaultReturnValue();
		int slot = findIndex(((Float)key).floatValue());
		return Integer.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public int getOrDefault(float key, int defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Float2IntOpenCustomHashMap copy() {
		Float2IntOpenCustomHashMap map = new Float2IntOpenCustomHashMap(0, loadFactor, strategy);
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
	public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(FloatIntConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0F)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(float key, int oldValue, int newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public int replace(float key, int value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		int oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public int computeInt(float key, FloatIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = mappingFunction.applyAsInt(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
		
	@Override
	public int supplyIntIfAbsent(float key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = valueProvider.getAsInt();
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
	
	@Override
	public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = mappingFunction.applyAsInt(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsInt(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			int newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		int newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2IntMap.Entry entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
			int index = findIndex(key);
			int newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(values[index], entry.getIntValue());
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
		Arrays.fill(values, 0);
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
		keys = new float[request + 1];
		values = new int[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(float key) {
		if(strategy.equals(key, 0F)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		float current = keys[pos];
		if(!strategy.equals(current, 0F)) {
			if(strategy.equals(current, key)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), 0F))
				if(strategy.equals(current, key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Float key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		float keyType = ((Float)key).floatValue();
		if(strategy.equals(keyType, 0F)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		float current = keys[pos];
		if(!strategy.equals(current, 0F)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), 0F))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		int value = values[pos];
		keys[pos] = 0F;
		values[pos] = 0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected int removeNullIndex() {
		int value = values[nullIndex];
		containsNull = false;
		keys[nullIndex] = 0F;
		values[nullIndex] = 0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, float key, int value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		float[] newKeys = new float[newSize + 1];
		int[] newValues = new int[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(!strategy.equals(keys[i], 0F)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask], 0F))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], 0F));
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
				if(strategy.equals((current = keys[startPos]), 0F)) {
					keys[last] = 0F;
					values[last] = 0;
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
		protected float key;
		protected int value;
		
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
		public int getIntValue() {
			return value;
		}
		
		@Override
		public int setValue(int value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Float2IntMap.Entry, Map.Entry<Float, Integer> {
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
		public int getIntValue() {
			return values[index];
		}

		@Override
		public int setValue(int value) {
			int oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2IntMap.Entry) {
					Float2IntMap.Entry entry = (Float2IntMap.Entry)obj;
					return Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(entry.getFloatKey()) && getIntValue() == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Integer && Float.floatToIntBits(getFloatKey()) == Float.floatToIntBits(((Float)key).floatValue()) && getIntValue() == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return strategy.hashCode(getFloatKey()) ^ Integer.hashCode(getIntValue());
		}
		
		@Override
		public String toString() {
			return Float.toString(getFloatKey()) + "=" + Integer.toString(getIntValue());
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Float2IntMap.Entry> implements Float2IntMap.FastEntrySet {
		@Override
		public ObjectIterator<Float2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Float2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Float2IntMap.Entry> action) {
			if(containsNull) action.accept(new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0F)) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2IntMap.Entry> action) {
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Float2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Float2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Float2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Float2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(!filter.test(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new ValueMapEntry(nullIndex));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0F)) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Float2IntMap.Entry reduce(ObjectObjectUnaryOperator<Float2IntMap.Entry, Float2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2IntMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new ValueMapEntry(nullIndex);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0F)) continue;
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
		public Float2IntMap.Entry findFirst(Predicate<Float2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					if(filter.test(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Float2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			if(containsNull) {
				entry.set(nullIndex);
				if(filter.test(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Float2IntOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2IntMap.Entry) {
					Float2IntMap.Entry entry = (Float2IntMap.Entry)o;
					int index = Float2IntOpenCustomHashMap.this.findIndex(entry.getFloatKey());
					if(index >= 0) return entry.getIntValue() == Float2IntOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!(entry.getKey() instanceof Float)) return false;
					int index = Float2IntOpenCustomHashMap.this.findIndex((Float)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Float2IntOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2IntMap.Entry) {
					Float2IntMap.Entry entry = (Float2IntMap.Entry)o;
					return Float2IntOpenCustomHashMap.this.remove(entry.getFloatKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2IntOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
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
			Float2IntOpenCustomHashMap.this.remove(o);
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
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Float2IntOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0F)) action.accept(keys[i]);
		}
		
		@Override
		public void forEachIndexed(IntFloatConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, keys[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(index++, keys[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && !filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			if(containsNull) state = operator.applyAsFloat(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0F)) continue;
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
				if(strategy.equals(keys[i], 0F)) continue;
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
				if(!strategy.equals(keys[i], 0F) && filter.test(keys[i])) return keys[i];
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
				if(!strategy.equals(keys[i], 0F) && filter.test(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractIntCollection {
		@Override
		public boolean contains(int e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(int o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public IntIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Float2IntOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(IntConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0F)) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(0, values[nullIndex]);
			for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.test(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.test(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			if(containsNull) state = operator.applyAsInt(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0F)) continue;
				state = operator.applyAsInt(state, values[i]);
			}
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0F)) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsInt(state, values[i]);
			}
			return state;
		}
		
		@Override
		public int findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			if(containsNull && filter.test(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(values[i])) return values[i];
			}
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.test(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0F) && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Float2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Float2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Float2IntMap.Entry> {
		MapEntry entry;
		@Override
		public Float2IntMap.Entry next() {
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
	
	private class ValueIterator extends MapIterator implements IntIterator {
		@Override
		public int nextInt() {
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
						if(!strategy.equals(keys[pos], 0F)){
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
				values[nullIndex] = 0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Float2IntOpenCustomHashMap.this.remove(wrapped[-returnedPos - 1]);
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
					if(strategy.equals((current = keys[startPos]), 0F)) {
						keys[last] = 0F;
						values[last] = 0;
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