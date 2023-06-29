package speiger.src.collections.ints.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.ints.functions.function.IntBooleanUnaryOperator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanOrderedMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.booleans.functions.function.BooleanPredicate;
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
public class Int2BooleanArrayMap extends AbstractInt2BooleanMap implements Int2BooleanOrderedMap
{
	/** The Backing keys array */
	protected transient int[] keys;
	/** The Backing values array */
	protected transient boolean[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected IntOrderedSet keySet;
	/** Values cache */
	protected BooleanCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Int2BooleanArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Int2BooleanArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new int[minCapacity];
		values = new boolean[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Int2BooleanArrayMap(Integer[] keys, Boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Int2BooleanArrayMap(Integer[] keys, Boolean[] values, int length) {
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
	public Int2BooleanArrayMap(int[] keys, boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2BooleanArrayMap(int[] keys, boolean[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Int2BooleanArrayMap(Map<? extends Integer, ? extends Boolean> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Int2BooleanArrayMap(Int2BooleanMap map) {
		this(map.size());
		for(ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(map);iter.hasNext();size++) {
			Int2BooleanMap.Entry entry = iter.next();
			keys[size] = entry.getIntKey();
			values[size] = entry.getBooleanValue();
		}
	}
	
	@Override
	public boolean put(int key, boolean value) {
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
	public boolean putIfAbsent(int key, boolean value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(values[index] == getDefaultReturnValue()) {
			boolean oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public boolean putAndMoveToFirst(int key, boolean value) {
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
	public boolean putAndMoveToLast(int key, boolean value) {
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
	public boolean get(int key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public boolean getOrDefault(int key, boolean defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public boolean getAndMoveToFirst(int key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean getAndMoveToLast(int key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
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
	public boolean remove(int key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean removeOrDefault(int key, boolean defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(int key, boolean value) {
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
	public void forEach(IntBooleanConsumer action) {
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
	public BooleanCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(int key, boolean oldValue, boolean newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public boolean replace(int key, boolean value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public boolean computeBoolean(int key, IntBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(int key, IntPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = mappingFunction.test(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		return newValue;
	}
		
	@Override
	public boolean supplyBooleanIfAbsent(int key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = valueProvider.getAsBoolean();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		return newValue;
	}
		
	@Override
	public boolean computeBooleanIfPresent(int key, IntBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanNonDefault(int key, IntBooleanUnaryOperator mappingFunction) {
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
	public boolean computeBooleanIfAbsentNonDefault(int key, IntPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = mappingFunction.test(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.test(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(int key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(int key, IntBooleanUnaryOperator mappingFunction) {
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
	public boolean mergeBoolean(int key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
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
	public void mergeAllBoolean(Int2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2BooleanMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
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
		Arrays.fill(keys, 0, size, 0);
		Arrays.fill(values, 0, size, false);
		size = 0;
	}
	
	public Int2BooleanArrayMap copy() {
		Int2BooleanArrayMap map = new Int2BooleanArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		int key = keys[index];
		boolean value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		int key = keys[index];
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
	
	protected void insertIndex(int index, int key, boolean value) {
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
			values[i+to] = false;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = 0;
			values[size] = false;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = 0;
		values[size] = false;
	}
	
	protected int findIndex(int key, boolean value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(int key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(boolean value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Integer.valueOf(keys[i])) && Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Integer.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Int2BooleanMap.Entry> implements Int2BooleanOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Int2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Int2BooleanMap.Entry o) {
			return Int2BooleanArrayMap.this.moveToFirst(o.getIntKey());
		}
		
		@Override
		public boolean moveToLast(Int2BooleanMap.Entry o) {
			return Int2BooleanArrayMap.this.moveToLast(o.getIntKey());
		}
		
		@Override
		public Int2BooleanMap.Entry first() {
			return new BasicEntry(firstIntKey(), firstBooleanValue());
		}
		
		@Override
		public Int2BooleanMap.Entry last() {
			return new BasicEntry(lastIntKey(), lastBooleanValue());
		}
		
		@Override
		public Int2BooleanMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstIntKey(), firstBooleanValue());
			pollFirstIntKey();
			return entry;
		}
		
		@Override
		public Int2BooleanMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastIntKey(), lastBooleanValue());
			pollLastIntKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2BooleanMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2BooleanMap.Entry> iterator(Int2BooleanMap.Entry fromElement) {
			return new EntryIterator(fromElement.getIntKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2BooleanMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2BooleanMap.Entry> fastIterator(int fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Int2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Int2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Int2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Int2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Int2BooleanMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Int2BooleanMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Int2BooleanMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Int2BooleanMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Int2BooleanMap.Entry reduce(ObjectObjectUnaryOperator<Int2BooleanMap.Entry, Int2BooleanMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Int2BooleanMap.Entry state = null;
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
		public Int2BooleanMap.Entry findFirst(Predicate<Int2BooleanMap.Entry> filter) {
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
		public int count(Predicate<Int2BooleanMap.Entry> filter) {
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
				if(o instanceof Int2BooleanMap.Entry) {
					Int2BooleanMap.Entry entry = (Int2BooleanMap.Entry)o;
					int index = Int2BooleanArrayMap.this.findIndex(entry.getIntKey());
					if(index >= 0) return entry.getBooleanValue() == Int2BooleanArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Int2BooleanArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Boolean.valueOf(Int2BooleanArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Int2BooleanMap.Entry) {
					Int2BooleanMap.Entry entry = (Int2BooleanMap.Entry)o;
					return Int2BooleanArrayMap.this.remove(entry.getIntKey(), entry.getBooleanValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Int2BooleanArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Int2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Int2BooleanArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractIntSet implements IntOrderedSet {
		@Override
		public boolean contains(int e) { return containsKey(e); }
		
		@Override
		public boolean remove(int o) {
			int oldSize = size;
			Int2BooleanArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int o) { return Int2BooleanArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(int o) { return Int2BooleanArrayMap.this.moveToLast(o); }
		@Override
		public IntListIterator iterator() { return new KeyIterator(); }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Int2BooleanArrayMap.this.size(); }
		@Override
		public void clear() { Int2BooleanArrayMap.this.clear(); }
		@Override
		public int firstInt() { return firstIntKey(); }
		@Override
		public int pollFirstInt() { return pollFirstIntKey(); }
		@Override
		public int lastInt() { return lastIntKey(); }
		@Override
		public int pollLastInt() { return pollLastIntKey(); }
		
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
		public int reduce(IntIntUnaryOperator operator) {
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
			return state;
		}
		
		@Override
		public int findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return keys[i];
			}
			return 0;
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
			return Int2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Int2BooleanArrayMap.this.clear();
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntBooleanConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
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
		public boolean findFirst(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return false;
		}
		
		@Override
		public int count(BooleanPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Int2BooleanMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(int from) {
			index = findIndex(from);
		}
		
		@Override
		public Int2BooleanMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Int2BooleanMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Int2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Int2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Int2BooleanMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(int from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Int2BooleanMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Int2BooleanMap.Entry previous() {
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
		public void set(Int2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Int2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements IntListIterator {
		public KeyIterator() {}
		public KeyIterator(int element) {
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
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
		protected int key;
		protected boolean value;
		
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
		public boolean getBooleanValue() {
			return value;
		}
		
		@Override
		public boolean setValue(boolean value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Int2BooleanMap.Entry, Map.Entry<Integer, Boolean> {
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
				if(obj instanceof Int2BooleanMap.Entry) {
					Int2BooleanMap.Entry entry = (Int2BooleanMap.Entry)obj;
					return keys[index] == entry.getIntKey() && getBooleanValue() == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Boolean && getIntKey() == ((Integer)key).intValue() && getBooleanValue() == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(getIntKey()) ^ Boolean.hashCode(getBooleanValue());
		}
		
		@Override
		public String toString() {
			return Integer.toString(getIntKey()) + "=" + Boolean.toString(getBooleanValue());
		}
	}
}