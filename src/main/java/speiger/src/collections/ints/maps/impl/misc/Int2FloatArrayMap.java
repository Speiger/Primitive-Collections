package speiger.src.collections.ints.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.OptionalInt;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.function.Int2FloatFunction;
import speiger.src.collections.ints.functions.function.IntFloatUnaryOperator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatOrderedMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatOrderedCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.OptionalFloat;
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
public class Int2FloatArrayMap extends AbstractInt2FloatMap implements Int2FloatOrderedMap
{
	/** The Backing keys array */
	protected transient int[] keys;
	/** The Backing values array */
	protected transient float[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected IntOrderedSet keySet;
	/** Values cache */
	protected FloatOrderedCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Int2FloatArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Int2FloatArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new int[minCapacity];
		values = new float[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Int2FloatArrayMap(Integer[] keys, Float[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Int2FloatArrayMap(Integer[] keys, Float[] values, int length) {
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
	public Int2FloatArrayMap(int[] keys, float[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2FloatArrayMap(int[] keys, float[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Int2FloatArrayMap(Map<? extends Integer, ? extends Float> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Int2FloatArrayMap(Int2FloatMap map) {
		this(map.size());
		for(ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(map);iter.hasNext();size++) {
			Int2FloatMap.Entry entry = iter.next();
			keys[size] = entry.getIntKey();
			values[size] = entry.getFloatValue();
		}
	}
	
	@Override
	public float put(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		float oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public float putIfAbsent(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue())) {
			float oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public float addTo(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		float oldValue = values[index];
		values[index] += value;
		return oldValue;
	}
	
	@Override
	public float subFrom(int key, float value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		float oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public float putAndMoveToFirst(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		float lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public float putAndMoveToLast(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		float lastValue = values[index];
		values[index] = value;
		moveIndexToLast(index);
		return lastValue;
	}
	
	@Override
	public float putFirst(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public float putLast(int key, float value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public boolean moveToFirst(int key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(int key) {
		int index = findIndex(key);
		if(index >= 0 && index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsKey(int key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(float value) {
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
	public float get(int key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public float getOrDefault(int key, float defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public float getAndMoveToFirst(int key) {
		int index = findIndex(key);
		if(index >= 0) {
			float value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float getAndMoveToLast(int key) {
		int index = findIndex(key);
		if(index >= 0) {
			float value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int firstIntKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public int lastIntKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[size-1];
	}
	
	@Override
	public float firstFloatValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public float lastFloatValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[size-1];
	}
	
	@Override
	public int pollFirstIntKey() {
		if(size == 0) throw new NoSuchElementException();
		int result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public int pollLastIntKey() {
		if(size == 0) throw new NoSuchElementException();
		int result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public Int2FloatMap.Entry firstEntry() {
		if(size == 0) throw new NoSuchElementException();
		return new BasicEntry(keys[0], values[0]);
	}
	
	@Override
	public Int2FloatMap.Entry lastEntry() {
		if(size == 0) throw new NoSuchElementException();
		return new BasicEntry(keys[size-1], values[size-1]);
	}
	
	@Override
	public Int2FloatMap.Entry pollFirstEntry() {
		if(size == 0) throw new NoSuchElementException();
		BasicEntry result = new BasicEntry(keys[0], values[0]);
		removeIndex(0);
		return result;
	}
	
	@Override
	public Int2FloatMap.Entry pollLastEntry() {
		if(size == 0) throw new NoSuchElementException();
		BasicEntry result = new BasicEntry(keys[size-1], values[size-1]);
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public float remove(int key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		float value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public float removeOrDefault(int key, float defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		float value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(int key, float value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Float remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Float.valueOf(getDefaultReturnValue());
		float value = values[index];
		removeIndex(index);
		return Float.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(IntFloatConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public IntOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public FloatOrderedCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Int2FloatMap.Entry> int2FloatEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(int key, float oldValue, float newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public float replace(int key, float value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		float oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = mappingFunction.applyAsFloat(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = mappingFunction.applyAsFloat(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = values[index];
		return newValue;
	}
		
	@Override
	public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = valueProvider.getAsFloat();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = values[index];
		return newValue;
	}
		
	@Override
	public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = mappingFunction.applyAsFloat(key, getDefaultReturnValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = mappingFunction.applyAsFloat(key);
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = values[index];
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			newValue = mappingFunction.applyAsFloat(key);
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			float newValue = valueProvider.getAsFloat();
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		float newValue = values[index];
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			newValue = valueProvider.getAsFloat();
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue())) return getDefaultReturnValue();
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		float newValue = index == -1 || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(values[index], value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2FloatMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			int index = findIndex(key);
			float newValue = index == -1 || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(values[index], entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
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
		Arrays.fill(keys, 0, size, 0);
		Arrays.fill(values, 0, size, 0F);
		size = 0;
	}
	
	public Int2FloatArrayMap copy() {
		Int2FloatArrayMap map = new Int2FloatArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		int key = keys[index];
		float value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		int key = keys[index];
		float value = values[index];
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
	
	protected void insertIndex(int index, int key, float value) {
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
			keys[i+to] = 0;
			values[i+to] = 0F;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = 0;
			values[size] = 0F;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = 0;
		values[size] = 0F;
	}
	
	protected int findIndex(int key, float value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && Float.floatToIntBits(values[i]) == Float.floatToIntBits(value)) return i;
		return -1;		
	}
	
	protected int findIndex(int key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(float value) {
		for(int i = size-1;i>=0;i--)
			if(Float.floatToIntBits(values[i]) == Float.floatToIntBits(value)) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Integer.valueOf(keys[i])) && Objects.equals(value, Float.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Integer.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Float.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Int2FloatMap.Entry> implements Int2FloatOrderedMap.FastOrderedSet {
		@Override
		public void addFirst(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public void addLast(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2FloatMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Int2FloatMap.Entry o) {
			return Int2FloatArrayMap.this.moveToFirst(o.getIntKey());
		}
		
		@Override
		public boolean moveToLast(Int2FloatMap.Entry o) {
			return Int2FloatArrayMap.this.moveToLast(o.getIntKey());
		}
		
		@Override
		public Int2FloatMap.Entry getFirst() {
			return new BasicEntry(firstIntKey(), firstFloatValue());
		}
		
		@Override
		public Int2FloatMap.Entry getLast() {
			return new BasicEntry(lastIntKey(), lastFloatValue());
		}
		
		@Override
		public Int2FloatMap.Entry removeFirst() {
			BasicEntry entry = new BasicEntry(firstIntKey(), firstFloatValue());
			pollFirstIntKey();
			return entry;
		}
		
		@Override
		public Int2FloatMap.Entry removeLast() {
			BasicEntry entry = new BasicEntry(lastIntKey(), lastFloatValue());
			pollLastIntKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> iterator() {
			return new EntryIterator(true);
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> reverseIterator() {
			return new EntryIterator(false);
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> iterator(Int2FloatMap.Entry fromElement) {
			return new EntryIterator(fromElement.getIntKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> fastIterator() {
			return new FastEntryIterator(true);
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2FloatMap.Entry> fastIterator(int fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Int2FloatMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Int2FloatMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Int2FloatMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Int2FloatMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Int2FloatMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(filter.test(entry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Int2FloatMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(filter.test(entry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Int2FloatMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(!filter.test(entry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Int2FloatMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Optional<Int2FloatMap.Entry> reduce(ObjectObjectUnaryOperator<Int2FloatMap.Entry, Int2FloatMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Int2FloatMap.Entry state = null;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = new ValueMapEntry(i);
					continue;
				}
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<Int2FloatMap.Entry> findFirst(Predicate<Int2FloatMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(filter.test(entry)) return Optional.ofNullable(entry);
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<Int2FloatMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(filter.test(entry)) result++;
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Int2FloatMap.Entry) {
					Int2FloatMap.Entry entry = (Int2FloatMap.Entry)o;
					int index = Int2FloatArrayMap.this.findIndex(entry.getIntKey());
					if(index >= 0) return Float.floatToIntBits(entry.getFloatValue()) == Float.floatToIntBits(Int2FloatArrayMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Int2FloatArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Float.valueOf(Int2FloatArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Int2FloatMap.Entry) {
					Int2FloatMap.Entry entry = (Int2FloatMap.Entry)o;
					return Int2FloatArrayMap.this.remove(entry.getIntKey(), entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Int2FloatArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Int2FloatArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Int2FloatArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractIntSet implements IntOrderedSet {
		@Override
		public boolean contains(int e) { return containsKey(e); }
		
		@Override
		public boolean remove(int o) {
			int oldSize = size;
			Int2FloatArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		@Override
		public void addFirst(int o) { throw new UnsupportedOperationException(); }
		@Override
		public void addLast(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int o) { return Int2FloatArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(int o) { return Int2FloatArrayMap.this.moveToLast(o); }
		@Override
		public IntListIterator iterator() { return new KeyIterator(true); }
		@Override
		public IntListIterator reverseIterator() { return new KeyIterator(false); }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Int2FloatArrayMap.this.size(); }
		@Override
		public void clear() { Int2FloatArrayMap.this.clear(); }
		@Override
		public int getFirstInt() { return firstIntKey(); }
		@Override
		public int removeFirstInt() { return pollFirstIntKey(); }
		@Override
		public int getLastInt() { return lastIntKey(); }
		@Override
		public int removeLastInt() { return pollLastIntKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(IntConsumer action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsInt(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public OptionalInt reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsInt(state, keys[i]);
			}
			return empty ? OptionalInt.empty() : OptionalInt.of(state);
		}
		
		@Override
		public OptionalInt findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return OptionalInt.of(keys[i]);
			}
			return OptionalInt.empty();
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractFloatCollection implements FloatOrderedCollection {
		@Override
		public boolean contains(float e) { return containsValue(e); }
		
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		@Override
		public FloatIterator iterator() { return new ValueIterator(true); }
		@Override
		public int size() { return Int2FloatArrayMap.this.size(); }
		@Override
		public void clear() { Int2FloatArrayMap.this.clear(); }
		@Override
		public FloatOrderedCollection reversed() { return new AbstractFloatCollection.ReverseFloatOrderedCollection(this, this::reverseIterator); }
		private FloatIterator reverseIterator() {
			return new ValueIterator(false);
		}
		@Override
		public void addFirst(float e) { throw new UnsupportedOperationException(); }
		@Override
		public void addLast(float e) { throw new UnsupportedOperationException(); }
		@Override
		public float getFirstFloat() { return firstFloatValue(); }
		@Override
		public float removeFirstFloat() {
			float result = firstFloatValue();
			pollFirstIntKey();
			return result; 
		}
		@Override
		public float getLastFloat() { return lastFloatValue(); }
		@Override
		public float removeLastFloat() {
			float result = lastFloatValue();
			pollLastIntKey();
			return result; 
		}
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntFloatConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsFloat(state, values[i]);
			}
			return state;
		}
		
		@Override
		public OptionalFloat reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsFloat(state, values[i]);
			}
			return empty ? OptionalFloat.empty() : OptionalFloat.of(state);
		}
		
		@Override
		public OptionalFloat findFirst(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return OptionalFloat.of(values[i]);
			}
			return OptionalFloat.empty();
		}
		
		@Override
		public int count(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Int2FloatMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator(boolean start) { super(start); }
		public FastEntryIterator(int element) { super(element); }
		
		@Override
		public Int2FloatMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Int2FloatMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Int2FloatMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Int2FloatMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Int2FloatMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator(boolean start) { super(start); }
		public EntryIterator(int element) { super(element); }
		
		@Override
		public Int2FloatMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Int2FloatMap.Entry previous() {
			return entry = new ValueMapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			if(entry != null && entry.index != -1) {
				entry.index = -1;				
			}
		}
		
		@Override
		public void set(Int2FloatMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Int2FloatMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements IntListIterator {
		public KeyIterator(boolean start) { super(start); }
		public KeyIterator(int element) { super(element); }
		
		@Override
		public int previousInt() {
			return keys[previousEntry()];
		}

		@Override
		public int nextInt() {
			return keys[nextEntry()];
		}

		@Override
		public void set(int e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }	
	}
	
	private class ValueIterator extends MapIterator implements FloatListIterator {
		public ValueIterator(boolean start) { super(start); }
		public ValueIterator(int element) { super(element); }
		
		@Override
		public float previousFloat() {
			return values[previousEntry()];
		}

		@Override
		public float nextFloat() {
			return values[nextEntry()];
		}

		@Override
		public void set(float e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(float e) { throw new UnsupportedOperationException(); }	
	}
	
	private class MapIterator {
		boolean forward;
		int index;
		int lastReturned = -1;
		
		MapIterator(boolean start) {
			this.forward = start;
			this.index = start ? 0 : size;
		}
		
		MapIterator(int element) {
			this.forward = true;
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
		public boolean hasNext() {
			return forward ? index < size : index > 0;
		}
		
		public boolean hasPrevious() {
			return forward ? index > 0 : index < size;
		}
		
		public int nextIndex() {
			if(forward) return index;
			return size - index;
		}
		
		public int previousIndex() {
			if(forward) return index-1;
			return (size - index)-1;
		}
		
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			removeIndex(lastReturned);
			if(lastReturned < index)
				index--;
			lastReturned = -1;
		}
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			if(forward) {
				index--;
				return (lastReturned = index);
			}
			lastReturned = index;
			return index++;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			if(forward) {
				lastReturned = index;
				return index++;
			}
			index--;
			return (lastReturned = index);
		}
		
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			return forward ? moveForward(amount) : moveBackwards(amount);
		}
		
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			return forward ? moveBackwards(amount) : moveForward(amount);
		}
		
		private int moveForward(int amount) {
			int steps = Math.min(amount, size() - index);
			index += steps;
			if(steps > 0) lastReturned = Math.min(index-1, size()-1);
			return steps;
		}
		
		private int moveBackwards(int amount) {
			int steps = Math.min(amount, index);
			index -= steps;
			if(steps > 0) lastReturned = Math.min(index, size()-1);
			return steps;
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected int key;
		protected float value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public int getIntKey() {
			return key;
		}

		@Override
		public float getFloatValue() {
			return value;
		}
		
		@Override
		public float setValue(float value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Int2FloatMap.Entry, Map.Entry<Integer, Float> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public int getIntKey() {
			return keys[index];
		}

		@Override
		public float getFloatValue() {
			return values[index];
		}

		@Override
		public float setValue(float value) {
			float oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2FloatMap.Entry) {
					Int2FloatMap.Entry entry = (Int2FloatMap.Entry)obj;
					return keys[index] == entry.getIntKey() && Float.floatToIntBits(getFloatValue()) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Float && getIntKey() == ((Integer)key).intValue() && Float.floatToIntBits(getFloatValue()) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(getIntKey()) ^ Float.hashCode(getFloatValue());
		}
		
		@Override
		public String toString() {
			return Integer.toString(getIntKey()) + "=" + Float.toString(getFloatValue());
		}
	}
}