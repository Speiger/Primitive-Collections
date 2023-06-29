package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.objects.functions.function.ToDoubleFunction;
import speiger.src.collections.objects.functions.function.ObjectDoubleUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;

/**
 * A Very Specific Type Specific implementation of a ArrayMap.
 * This type of map is for very specific use cases that usaully would have lead to Tupled Lists otherwise.
 * It also does not allow duplication (except for array constructors) and checks from last to first.
 * It is not designed to be used as a HashMap replacement due to the poor performance it would cause.
 * @note in this implementation SubMaps do NOT keep track of parent changes fully. For performance reasons it will just have a start/end index and not values
 * Anything within that range will be updated appropiatly a shrink/growth of elements will break SubMaps in some ways. This can be useful but be careful
 * @note this implementation does not shrink and only grows.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Object2DoubleArrayMap<T> extends AbstractObject2DoubleMap<T> implements Object2DoubleOrderedMap<T>
{
	/** The Backing keys array */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient double[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ObjectOrderedSet<T> keySet;
	/** Values cache */
	protected DoubleCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet<T> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Object2DoubleArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2DoubleArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = (T[])new Object[minCapacity];
		values = new double[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Object2DoubleArrayMap(T[] keys, Double[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Object2DoubleArrayMap(T[] keys, Double[] values, int length) {
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
	public Object2DoubleArrayMap(T[] keys, double[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2DoubleArrayMap(T[] keys, double[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2DoubleArrayMap(Map<? extends T, ? extends Double> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2DoubleArrayMap(Object2DoubleMap<T> map) {
		this(map.size());
		for(ObjectIterator<Object2DoubleMap.Entry<T>> iter = getFastIterator(map);iter.hasNext();size++) {
			Object2DoubleMap.Entry<T> entry = iter.next();
			keys[size] = entry.getKey();
			values[size] = entry.getDoubleValue();
		}
	}
	
	@Override
	public double put(T key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public double putIfAbsent(T key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue())) {
			double oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public double addTo(T key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[index];
		values[index] += value;
		return oldValue;
	}
	
	@Override
	public double subFrom(T key, double value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		double oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public double putAndMoveToFirst(T key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		double lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public double putAndMoveToLast(T key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		double lastValue = values[index];
		values[index] = value;
		moveIndexToLast(index);
		return lastValue;
	}
	
	@Override
	public boolean moveToFirst(T key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(T key) {
		int index = findIndex(key);
		if(index >= 0 && index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
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
	public double getDouble(T key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public double getOrDefault(Object key, double defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public double getAndMoveToFirst(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			double value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double getAndMoveToLast(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			double value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public T firstKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public T lastKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[size-1];
	}
	
	@Override
	public double firstDoubleValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public double lastDoubleValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[size-1];
	}
	
	@Override
	public T pollFirstKey() {
		if(size == 0) throw new NoSuchElementException();
		T result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public T pollLastKey() {
		if(size == 0) throw new NoSuchElementException();
		T result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public double rem(T key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public double remOrDefault(T key, double defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		double value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(T key, double value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Double remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Double.valueOf(getDefaultReturnValue());
		double value = values[index];
		removeIndex(index);
		return Double.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(ObjectDoubleConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public ObjectOrderedSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(T key, double oldValue, double newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public double replace(T key, double value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = mappingFunction.applyAsDouble(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(T key, ToDoubleFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = mappingFunction.applyAsDouble(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		return newValue;
	}
		
	@Override
	public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = valueProvider.getAsDouble();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		return newValue;
	}
		
	@Override
	public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = mappingFunction.applyAsDouble(key, getDefaultReturnValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
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
	public double computeDoubleIfAbsentNonDefault(T key, ToDoubleFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = mappingFunction.applyAsDouble(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = mappingFunction.applyAsDouble(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public double supplyDoubleIfAbsentNonDefault(T key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = valueProvider.getAsDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = valueProvider.getAsDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public double computeDoubleIfPresentNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue())) return getDefaultReturnValue();
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		double newValue = index == -1 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(values[index], value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2DoubleMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			double newValue = index == -1 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(values[index], entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
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
		Arrays.fill(keys, 0, size, null);
		Arrays.fill(values, 0, size, 0D);
		size = 0;
	}
	
	public Object2DoubleArrayMap<T> copy() {
		Object2DoubleArrayMap<T> map = new Object2DoubleArrayMap<>();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		T key = keys[index];
		double value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		T key = keys[index];
		double value = values[index];
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
	
	protected void insertIndex(int index, T key, double value) {
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
			keys[i+to] = null;
			values[i+to] = 0D;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = null;
			values[size] = 0D;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = null;
		values[size] = 0D;
	}
	
	protected int findIndex(T key, double value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(keys[i], key) && Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return i;
		return -1;		
	}
	
	protected int findValue(double value) {
		for(int i = size-1;i>=0;i--)
			if(Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i]) && Objects.equals(value, Double.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i])) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Double.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<T>> implements Object2DoubleOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2DoubleMap.Entry<T> o) {
			return Object2DoubleArrayMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2DoubleMap.Entry<T> o) {
			return Object2DoubleArrayMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstDoubleValue());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastDoubleValue());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> pollFirst() {
			BasicEntry<T> entry = new BasicEntry<>(firstKey(), firstDoubleValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> pollLast() {
			BasicEntry<T> entry = new BasicEntry<>(lastKey(), lastDoubleValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator(Object2DoubleMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2DoubleMap.Entry<T>> filter) {
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
		public boolean matchesNone(Predicate<Object2DoubleMap.Entry<T>> filter) {
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
		public boolean matchesAll(Predicate<Object2DoubleMap.Entry<T>> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Object2DoubleMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2DoubleMap.Entry<T>, Object2DoubleMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2DoubleMap.Entry<T> state = null;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
		public Object2DoubleMap.Entry<T> findFirst(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				if(filter.test(entry)) return entry;
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Object2DoubleMap.Entry<T>> filter) {
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
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					int index = Object2DoubleArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(Object2DoubleArrayMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2DoubleArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Double.valueOf(Object2DoubleArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					return Object2DoubleArrayMap.this.remove(entry.getKey(), entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2DoubleArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Object2DoubleArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2DoubleArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		public boolean contains(Object e) { return containsKey(e); }
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2DoubleArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T o) { return Object2DoubleArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(T o) { return Object2DoubleArrayMap.this.moveToLast(o); }
		@Override
		public ObjectListIterator<T> iterator() { return new KeyIterator(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Object2DoubleArrayMap.this.size(); }
		@Override
		public void clear() { Object2DoubleArrayMap.this.clear(); }
		@Override
		public T first() { return firstKey(); }
		@Override
		public T pollFirst() { return pollFirstKey(); }
		@Override
		public T last() { return lastKey(); }
		@Override
		public T pollLast() { return pollLastKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super T> action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return keys[i];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) result++;
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
			return Object2DoubleArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2DoubleArrayMap.this.clear();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntDoubleConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsDouble(state, values[i]);
			}
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
		public double findFirst(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return 0D;
		}
		
		@Override
		public int count(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			index = findIndex(from);
		}
		
		@Override
		public Object2DoubleMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2DoubleMap.Entry<T> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2DoubleMap.Entry<T> e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Object2DoubleMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> previous() {
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
		public void set(Object2DoubleMap.Entry<T> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2DoubleMap.Entry<T> e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ObjectListIterator<T> {
		public KeyIterator() {}
		public KeyIterator(T element) {
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public T previous() {
			return keys[previousEntry()];
		}

		@Override
		public T next() {
			return keys[nextEntry()];
		}

		@Override
		public void set(T e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(T e) { throw new UnsupportedOperationException(); }	
	}
	
	private class ValueIterator extends MapIterator implements DoubleListIterator {
		@Override
		public double previousDouble() {
			return values[previousEntry()];
		}

		@Override
		public double nextDouble() {
			return values[nextEntry()];
		}

		@Override
		public void set(double e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }	
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
			if(lastReturned == -1) throw new IllegalStateException();
			removeIndex(lastReturned);
			if(lastReturned < index)
				index--;
			lastReturned = -1;
		}
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return (lastReturned = index);
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = index;
			return index++;
		}
		
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, size() - index);
			index += steps;
			if(steps > 0) lastReturned = Math.min(index-1, size()-1);
			return steps;
		}
		
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			if(steps > 0) lastReturned = Math.min(index, size()-1);
			return steps;
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected double value;
		
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
		public double getDoubleValue() {
			return value;
		}
		
		@Override
		public double setValue(double value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Object2DoubleMap.Entry<T>, Map.Entry<T, Double> {
		int index = -1;
		
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
				if(obj instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && Double.doubleToLongBits(getDoubleValue()) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Double && Objects.equals(getKey(), key) && Double.doubleToLongBits(getDoubleValue()) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Double.hashCode(getDoubleValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Double.toString(getDoubleValue());
		}
	}
}