package speiger.src.collections.floats.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.consumer.FloatBooleanConsumer;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.functions.function.FloatBooleanUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanOrderedMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.maps.Float2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;

/**
 * A Very Specific Type Specific implementation of a ArrayMap.
 * This type of map is for very specific use cases that usaully would have lead to Tupled Lists otherwise.
 * It also does not allow duplication (except for array constructors) and checks from last to first.
 * It is not designed to be used as a HashMap replacement due to the poor performance it would cause.
 * @note in this implementation SubMaps do NOT keep track of parent changes fully. For performance reasons it will just have a start/end index and not values
 * Anything within that range will be updated appropiatly a shrink/growth of elements will break SubMaps in some ways. This can be useful but be careful
 * @note this implementation does not shrink and only grows.
 */
public class Float2BooleanArrayMap extends AbstractFloat2BooleanMap implements Float2BooleanOrderedMap
{
	/** The Backing keys array */
	protected transient float[] keys;
	/** The Backing values array */
	protected transient boolean[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected FloatSet keySet;
	/** Values cache */
	protected BooleanCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Float2BooleanArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2BooleanArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new float[minCapacity];
		values = new boolean[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Float2BooleanArrayMap(Float[] keys, Boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Float2BooleanArrayMap(Float[] keys, Boolean[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2BooleanArrayMap(float[] keys, boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2BooleanArrayMap(float[] keys, boolean[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2BooleanArrayMap(Map<? extends Float, ? extends Boolean> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2BooleanArrayMap(Float2BooleanMap map) {
		this(map.size());
		for(ObjectIterator<Float2BooleanMap.Entry> iter = Float2BooleanMaps.fastIterator(map);iter.hasNext();size++) {
			Float2BooleanMap.Entry entry = iter.next();
			keys[size] = entry.getFloatKey();
			values[size] = entry.getBooleanValue();
		}
	}
	
	@Override
	public boolean put(float key, boolean value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		boolean oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public boolean putIfAbsent(float key, boolean value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public boolean putAndMoveToFirst(float key, boolean value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		boolean lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public boolean putAndMoveToLast(float key, boolean value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		boolean lastValue = values[index];
		values[index] = value;
		moveIndexToLast(index);
		return lastValue;
	}
	
	@Override
	public boolean moveToFirst(float key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(float key) {
		int index = findIndex(key);
		if(index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsKey(float key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		return findValue(value) >= 0;
	}
	
	@Override
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(Object value) {
		return findValue(value) >= 0;
	}
	
	@Override
	public boolean get(float key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public boolean getOrDefault(float key, boolean defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public boolean getAndMoveToFirst(float key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean getAndMoveToLast(float key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float firstFloatKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public float lastFloatKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[size-1];
	}
	
	@Override
	public boolean firstBooleanValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public boolean lastBooleanValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[size-1];
	}
	
	@Override
	public float pollFirstFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		float result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public float pollLastFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		float result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public boolean remove(float key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean removeOrDefault(float key, boolean defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(float key, boolean value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Boolean remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Boolean.valueOf(getDefaultReturnValue());
		boolean value = values[index];
		removeIndex(index);
		return Boolean.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(FloatBooleanConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public BooleanCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(float key, boolean oldValue, boolean newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public boolean replace(float key, boolean value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	
	@Override
	public boolean computeBooleanIfAbsent(float key, Float2BooleanFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = valueProvider.getBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		boolean newValue = index == -1 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2BooleanMap.Entry entry : Float2BooleanMaps.fastIterable(m)) {
			float key = entry.getFloatKey();
			int index = findIndex(key);
			boolean newValue = index == -1 || values[index] == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(values[index], entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) {
				if(index >= 0)
					removeIndex(index);
			}
			else if(index == -1) insertIndex(size++, key, newValue);
			else values[index] = newValue;
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
		Arrays.fill(keys, 0, size, 0F);
		Arrays.fill(values, 0, size, false);
		size = 0;
	}
	
	public Float2BooleanArrayMap copy() {
		Float2BooleanArrayMap map = new Float2BooleanArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		float key = keys[index];
		boolean value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		float key = keys[index];
		boolean value = values[index];
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);
		keys[size-1] = key;
		values[size-1] = value;
	}
	
	protected void grow(int newSize) {
		if(newSize < keys.length) return;
		newSize = Math.max(newSize, keys.length == 0 ? 2 : keys.length * 2);
		keys = Arrays.copyOf(keys, newSize);
		values = Arrays.copyOf(values, newSize);
	}
	
	protected void insertIndex(int index, float key, boolean value) {
		grow(size+1);
		if(index != size) {
			System.arraycopy(keys, index, keys, index+1, size-index);
			System.arraycopy(values, index, values, index+1, size-index);		
		}
		keys[index] = key;
		values[index] = value;
	}
	
	protected void removeRange(int from, int to) {
		if(from < 0 || from >= size) throw new IllegalStateException("From Element ");
		int length = to - from;
		if(length <= 0) return;
		if(to != size) {
			System.arraycopy(keys, to, keys, from, size - to);
			System.arraycopy(values, to, values, from, size - to);
		}
		for(int i = 0;i<length;i++) {
			keys[i+to] = 0F;
			values[i+to] = false;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = 0F;
			values[size] = false;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = 0F;
		values[size] = false;
	}
	
	protected int findIndex(float key, boolean value) {
		for(int i = size-1;i>=0;i--)
			if(Float.floatToIntBits(keys[i]) == Float.floatToIntBits(key) && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(float key) {
		for(int i = size-1;i>=0;i--)
			if(Float.floatToIntBits(keys[i]) == Float.floatToIntBits(key)) return i;
		return -1;
	}
	
	protected int findValue(boolean value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Float.valueOf(keys[i])) && Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Float.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Float2BooleanMap.Entry> implements Float2BooleanOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Float2BooleanMap.Entry o) {
			return Float2BooleanArrayMap.this.moveToFirst(o.getFloatKey());
		}
		
		@Override
		public boolean moveToLast(Float2BooleanMap.Entry o) {
			return Float2BooleanArrayMap.this.moveToLast(o.getFloatKey());
		}
		
		@Override
		public Float2BooleanMap.Entry first() {
			return new BasicEntry(firstFloatKey(), firstBooleanValue());
		}
		
		@Override
		public Float2BooleanMap.Entry last() {
			return new BasicEntry(lastFloatKey(), lastBooleanValue());
		}
		
		@Override
		public Float2BooleanMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstFloatKey(), firstBooleanValue());
			pollFirstFloatKey();
			return entry;
		}
		
		@Override
		public Float2BooleanMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastFloatKey(), lastBooleanValue());
			pollLastFloatKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> iterator(Float2BooleanMap.Entry fromElement) {
			return new EntryIterator(fromElement.getFloatKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2BooleanMap.Entry> fastIterator(float fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Float2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				action.accept(entry);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				if(filter.getBoolean(entry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				if(filter.getBoolean(entry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				if(!filter.getBoolean(entry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2BooleanMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Float2BooleanMap.Entry reduce(ObjectObjectUnaryOperator<Float2BooleanMap.Entry, Float2BooleanMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2BooleanMap.Entry state = null;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
		public Float2BooleanMap.Entry findFirst(Object2BooleanFunction<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				if(filter.getBoolean(entry)) return entry;
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Float2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				if(filter.getBoolean(entry)) result++;
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2BooleanMap.Entry) {
					Float2BooleanMap.Entry entry = (Float2BooleanMap.Entry)o;
					int index = Float2BooleanArrayMap.this.findIndex(entry.getFloatKey());
					if(index >= 0) return entry.getBooleanValue() == Float2BooleanArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Float2BooleanArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Boolean.valueOf(Float2BooleanArrayMap.this.values[index]));
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
					return Float2BooleanArrayMap.this.remove(entry.getFloatKey(), entry.getBooleanValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2BooleanArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Float2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2BooleanArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractFloatSet implements FloatOrderedSet {
		@Override
		public boolean contains(float e) { return containsKey(e); }
		
		@Override
		public boolean remove(float o) {
			int oldSize = size;
			Float2BooleanArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float o) { return Float2BooleanArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(float o) { return Float2BooleanArrayMap.this.moveToLast(o); }
		@Override
		public FloatListIterator iterator() { return new KeyIterator(); }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Float2BooleanArrayMap.this.size(); }
		@Override
		public void clear() { Float2BooleanArrayMap.this.clear(); }
		@Override
		public float firstFloat() { return firstFloatKey(); }
		@Override
		public float pollFirstFloat() { return pollFirstFloatKey(); }
		@Override
		public float lastFloat() { return lastFloatKey(); }
		@Override
		public float pollLastFloat() { return pollLastFloatKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(FloatConsumer action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 

		}
		
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsFloat(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public float reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return keys[i];
			}
			return 0F;
		}
		
		@Override
		public int count(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) result++;
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
			return Float2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2BooleanArrayMap.this.clear();
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public boolean matchesAny(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsBoolean(state, values[i]);
			}
			return state;
		}
		
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = false;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsBoolean(state, values[i]);
			}
			return state;
		}
		
		@Override
		public boolean findFirst(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return values[i];
			}
			return false;
		}
		
		@Override
		public int count(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Float2BooleanMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(float from) {
			index = findIndex(from);
		}
		
		@Override
		public Float2BooleanMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Float2BooleanMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Float2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Float2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Float2BooleanMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(float from) {
			index = findIndex(from);
		}
		
		@Override
		public Float2BooleanMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Float2BooleanMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
		
		@Override
		public void set(Float2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Float2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements FloatListIterator {
		public KeyIterator() {}
		public KeyIterator(float element) {
			index = findIndex(element);
		}
		@Override
		public float previousFloat() {
			return keys[previousEntry()];
		}

		@Override
		public float nextFloat() {
			return keys[nextEntry()];
		}

		@Override
		public void set(float e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(float e) { throw new UnsupportedOperationException(); }	
	}
	
	private class ValueIterator extends MapIterator implements BooleanListIterator {
		@Override
		public boolean previousBoolean() {
			return values[previousEntry()];
		}

		@Override
		public boolean nextBoolean() {
			return values[nextEntry()];
		}

		@Override
		public void set(boolean e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }	
	}
	
	private class MapIterator {
		int index;
		int lastReturned = -1;

		public boolean hasNext() {
			return index < size;
		}
		
		public boolean hasPrevious() {
			return index > 0;
		}
		
		public int nextIndex() {
			return index;
		}
		
		public int previousIndex() {
			return index-1;
		}
		
		public void remove() {
			if(lastReturned == -1)
				throw new IllegalStateException();
			removeIndex(lastReturned);
			if(lastReturned < index)
				index--;
			lastReturned = -1;
		}
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = index;
			return index--;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = index;
			return index++;
		}
		
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, (size() - 1) - index);
			index += steps;
			return steps;
		}
		
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			return steps;
		}
	}
	
	private class MapEntry implements Float2BooleanMap.Entry, Map.Entry<Float, Boolean> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public float getFloatKey() {
			return keys[index];
		}

		@Override
		public boolean getBooleanValue() {
			return values[index];
		}

		@Override
		public boolean setValue(boolean value) {
			boolean oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2BooleanMap.Entry) {
					Float2BooleanMap.Entry entry = (Float2BooleanMap.Entry)obj;
					return Float.floatToIntBits(keys[index]) == Float.floatToIntBits(entry.getFloatKey()) && values[index] == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Boolean && Float.floatToIntBits(keys[index]) == Float.floatToIntBits(((Float)key).floatValue()) && values[index] == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(keys[index]) ^ Boolean.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Float.toString(keys[index]) + "=" + Boolean.toString(values[index]);
		}
	}
}