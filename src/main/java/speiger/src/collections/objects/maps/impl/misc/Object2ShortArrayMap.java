package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.ToShortFunction;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.shorts.functions.function.ShortPredicate;
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
public class Object2ShortArrayMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortOrderedMap<T>
{
	/** The Backing keys array */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient short[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ObjectOrderedSet<T> keySet;
	/** Values cache */
	protected ShortCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet<T> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Object2ShortArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2ShortArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = (T[])new Object[minCapacity];
		values = new short[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Object2ShortArrayMap(T[] keys, Short[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Object2ShortArrayMap(T[] keys, Short[] values, int length) {
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
	public Object2ShortArrayMap(T[] keys, short[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ShortArrayMap(T[] keys, short[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2ShortArrayMap(Map<? extends T, ? extends Short> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2ShortArrayMap(Object2ShortMap<T> map) {
		this(map.size());
		for(ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(map);iter.hasNext();size++) {
			Object2ShortMap.Entry<T> entry = iter.next();
			keys[size] = entry.getKey();
			values[size] = entry.getShortValue();
		}
	}
	
	@Override
	public short put(T key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short putIfAbsent(T key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(values[index] == getDefaultReturnValue()) {
			short oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public short addTo(T key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[index];
		values[index] += value;
		return oldValue;
	}
	
	@Override
	public short subFrom(T key, short value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		short oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public short putAndMoveToFirst(T key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		short lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public short putAndMoveToLast(T key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		short lastValue = values[index];
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
	public boolean containsValue(short value) {
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
	public short getShort(T key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public short getOrDefault(Object key, short defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public short getAndMoveToFirst(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			short value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short getAndMoveToLast(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			short value = values[index];
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
	public short firstShortValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public short lastShortValue() {
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
	public short rem(T key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public short remOrDefault(T key, short defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		short value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(T key, short value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Short remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Short.valueOf(getDefaultReturnValue());
		short value = values[index];
		removeIndex(index);
		return Short.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(ObjectShortConsumer<T> action) {
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
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(T key, short oldValue, short newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public short replace(T key, short value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(T key, ToShortFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = mappingFunction.applyAsShort(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
		
	@Override
	public short supplyShortIfAbsent(T key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = valueProvider.getAsShort();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
		
	@Override
	public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public short computeShortIfAbsentNonDefault(T key, ToShortFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public short supplyShortIfAbsentNonDefault(T key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = valueProvider.getAsShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public short computeShortIfPresentNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		short newValue = index == -1 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			short newValue = index == -1 || values[index] == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(values[index], entry.getShortValue());
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
		Arrays.fill(keys, 0, size, null);
		Arrays.fill(values, 0, size, (short)0);
		size = 0;
	}
	
	public Object2ShortArrayMap<T> copy() {
		Object2ShortArrayMap<T> map = new Object2ShortArrayMap<>();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		T key = keys[index];
		short value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		T key = keys[index];
		short value = values[index];
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
	
	protected void insertIndex(int index, T key, short value) {
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
			values[i+to] = (short)0;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = null;
			values[size] = (short)0;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = null;
		values[size] = (short)0;
	}
	
	protected int findIndex(T key, short value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(keys[i], key) && values[i] == value) return i;
		return -1;		
	}
	
	protected int findValue(short value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i]) && Objects.equals(value, Short.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i])) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Short.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Object2ShortMap.Entry<T>> implements Object2ShortOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2ShortMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2ShortMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2ShortMap.Entry<T> o) {
			return Object2ShortArrayMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2ShortMap.Entry<T> o) {
			return Object2ShortArrayMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2ShortMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstShortValue());
		}
		
		@Override
		public Object2ShortMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastShortValue());
		}
		
		@Override
		public Object2ShortMap.Entry<T> pollFirst() {
			BasicEntry<T> entry = new BasicEntry<>(firstKey(), firstShortValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2ShortMap.Entry<T> pollLast() {
			BasicEntry<T> entry = new BasicEntry<>(lastKey(), lastShortValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ShortMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ShortMap.Entry<T>> iterator(Object2ShortMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ShortMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ShortMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2ShortMap.Entry<T>> filter) {
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
		public boolean matchesNone(Predicate<Object2ShortMap.Entry<T>> filter) {
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
		public boolean matchesAll(Predicate<Object2ShortMap.Entry<T>> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Object2ShortMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Object2ShortMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2ShortMap.Entry<T>, Object2ShortMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2ShortMap.Entry<T> state = null;
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
		public Object2ShortMap.Entry<T> findFirst(Predicate<Object2ShortMap.Entry<T>> filter) {
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
		public int count(Predicate<Object2ShortMap.Entry<T>> filter) {
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
				if(o instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)o;
					int index = Object2ShortArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getShortValue() == Object2ShortArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2ShortArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Short.valueOf(Object2ShortArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)o;
					return Object2ShortArrayMap.this.remove(entry.getKey(), entry.getShortValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2ShortArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Object2ShortArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ShortArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		public boolean contains(Object e) { return containsKey(e); }
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2ShortArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T o) { return Object2ShortArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(T o) { return Object2ShortArrayMap.this.moveToLast(o); }
		@Override
		public ObjectListIterator<T> iterator() { return new KeyIterator(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Object2ShortArrayMap.this.size(); }
		@Override
		public void clear() { Object2ShortArrayMap.this.clear(); }
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
			return Object2ShortArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ShortArrayMap.this.clear();
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntShortConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsShort(state, values[i]);
			}
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			index = findIndex(from);
		}
		
		@Override
		public Object2ShortMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2ShortMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2ShortMap.Entry<T> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2ShortMap.Entry<T> e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<T>> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Object2ShortMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Object2ShortMap.Entry<T> previous() {
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
		public void set(Object2ShortMap.Entry<T> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2ShortMap.Entry<T> e) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements ShortListIterator {
		@Override
		public short previousShort() {
			return values[previousEntry()];
		}

		@Override
		public short nextShort() {
			return values[nextEntry()];
		}

		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }	
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
		protected short value;
		
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
		public short getShortValue() {
			return value;
		}
		
		@Override
		public short setValue(short value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Object2ShortMap.Entry<T>, Map.Entry<T, Short> {
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
				if(obj instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && getShortValue() == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Short && Objects.equals(getKey(), key) && getShortValue() == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Short.hashCode(getShortValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Short.toString(getShortValue());
		}
	}
}