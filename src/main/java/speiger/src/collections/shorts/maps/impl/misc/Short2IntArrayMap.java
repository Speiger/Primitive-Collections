package speiger.src.collections.shorts.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.consumer.ShortIntConsumer;
import speiger.src.collections.shorts.functions.function.Short2IntFunction;
import speiger.src.collections.shorts.functions.function.ShortIntUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntOrderedMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

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
public class Short2IntArrayMap extends AbstractShort2IntMap implements Short2IntOrderedMap
{
	/** The Backing keys array */
	protected transient short[] keys;
	/** The Backing values array */
	protected transient int[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ShortOrderedSet keySet;
	/** Values cache */
	protected IntCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Short2IntArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Short2IntArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new short[minCapacity];
		values = new int[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Short2IntArrayMap(Short[] keys, Integer[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Short2IntArrayMap(Short[] keys, Integer[] values, int length) {
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
	public Short2IntArrayMap(short[] keys, int[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2IntArrayMap(short[] keys, int[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Short2IntArrayMap(Map<? extends Short, ? extends Integer> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Short2IntArrayMap(Short2IntMap map) {
		this(map.size());
		for(ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(map);iter.hasNext();size++) {
			Short2IntMap.Entry entry = iter.next();
			keys[size] = entry.getShortKey();
			values[size] = entry.getIntValue();
		}
	}
	
	@Override
	public int put(short key, int value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		int oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public int putIfAbsent(short key, int value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(values[index] == getDefaultReturnValue()) {
			int oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public int addTo(short key, int value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		int oldValue = values[index];
		values[index] += value;
		return oldValue;
	}
	
	@Override
	public int subFrom(short key, int value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		int oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public int putAndMoveToFirst(short key, int value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		int lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public int putAndMoveToLast(short key, int value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		int lastValue = values[index];
		values[index] = value;
		moveIndexToLast(index);
		return lastValue;
	}
	
	@Override
	public boolean moveToFirst(short key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(short key) {
		int index = findIndex(key);
		if(index >= 0 && index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsKey(short key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(int value) {
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
	public int get(short key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public int getOrDefault(short key, int defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public int getAndMoveToFirst(short key) {
		int index = findIndex(key);
		if(index >= 0) {
			int value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int getAndMoveToLast(short key) {
		int index = findIndex(key);
		if(index >= 0) {
			int value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short firstShortKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public short lastShortKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[size-1];
	}
	
	@Override
	public int firstIntValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public int lastIntValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[size-1];
	}
	
	@Override
	public short pollFirstShortKey() {
		if(size == 0) throw new NoSuchElementException();
		short result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public short pollLastShortKey() {
		if(size == 0) throw new NoSuchElementException();
		short result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public int remove(short key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		int value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public int removeOrDefault(short key, int defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		int value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(short key, int value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Integer remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Integer.valueOf(getDefaultReturnValue());
		int value = values[index];
		removeIndex(index);
		return Integer.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(ShortIntConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public ShortOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Short2IntMap.Entry> short2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(short key, int oldValue, int newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public int replace(short key, int value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		int oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public int computeInt(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(short key, Short2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = mappingFunction.applyAsInt(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
		
	@Override
	public int supplyIntIfAbsent(short key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = valueProvider.getAsInt();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
		
	@Override
	public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntNonDefault(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public int computeIntIfAbsentNonDefault(short key, Short2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = mappingFunction.applyAsInt(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public int supplyIntIfAbsentNonDefault(short key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			int newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
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
	public int computeIntIfPresentNonDefault(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int mergeInt(short key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		int newValue = index == -1 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Short2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2IntMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			int index = findIndex(key);
			int newValue = index == -1 || values[index] == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(values[index], entry.getIntValue());
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
		Arrays.fill(keys, 0, size, (short)0);
		Arrays.fill(values, 0, size, 0);
		size = 0;
	}
	
	public Short2IntArrayMap copy() {
		Short2IntArrayMap map = new Short2IntArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		short key = keys[index];
		int value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		short key = keys[index];
		int value = values[index];
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
	
	protected void insertIndex(int index, short key, int value) {
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
			keys[i+to] = (short)0;
			values[i+to] = 0;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (short)0;
			values[size] = 0;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (short)0;
		values[size] = 0;
	}
	
	protected int findIndex(short key, int value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(short key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(int value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Short.valueOf(keys[i])) && Objects.equals(value, Integer.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Short.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Integer.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Short2IntMap.Entry> implements Short2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Short2IntMap.Entry o) {
			return Short2IntArrayMap.this.moveToFirst(o.getShortKey());
		}
		
		@Override
		public boolean moveToLast(Short2IntMap.Entry o) {
			return Short2IntArrayMap.this.moveToLast(o.getShortKey());
		}
		
		@Override
		public Short2IntMap.Entry first() {
			return new BasicEntry(firstShortKey(), firstIntValue());
		}
		
		@Override
		public Short2IntMap.Entry last() {
			return new BasicEntry(lastShortKey(), lastIntValue());
		}
		
		@Override
		public Short2IntMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstShortKey(), firstIntValue());
			pollFirstShortKey();
			return entry;
		}
		
		@Override
		public Short2IntMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastShortKey(), lastIntValue());
			pollLastShortKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> iterator(Short2IntMap.Entry fromElement) {
			return new EntryIterator(fromElement.getShortKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator(short fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Short2IntMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Short2IntMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Short2IntMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Short2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Short2IntMap.Entry reduce(ObjectObjectUnaryOperator<Short2IntMap.Entry, Short2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Short2IntMap.Entry state = null;
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
		public Short2IntMap.Entry findFirst(Predicate<Short2IntMap.Entry> filter) {
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
		public int count(Predicate<Short2IntMap.Entry> filter) {
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
				if(o instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)o;
					int index = Short2IntArrayMap.this.findIndex(entry.getShortKey());
					if(index >= 0) return entry.getIntValue() == Short2IntArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Short2IntArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Short2IntArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)o;
					return Short2IntArrayMap.this.remove(entry.getShortKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Short2IntArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Short2IntArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Short2IntArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractShortSet implements ShortOrderedSet {
		@Override
		public boolean contains(short e) { return containsKey(e); }
		
		@Override
		public boolean remove(short o) {
			int oldSize = size;
			Short2IntArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short o) { return Short2IntArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(short o) { return Short2IntArrayMap.this.moveToLast(o); }
		@Override
		public ShortListIterator iterator() { return new KeyIterator(); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Short2IntArrayMap.this.size(); }
		@Override
		public void clear() { Short2IntArrayMap.this.clear(); }
		@Override
		public short firstShort() { return firstShortKey(); }
		@Override
		public short pollFirstShort() { return pollFirstShortKey(); }
		@Override
		public short lastShort() { return lastShortKey(); }
		@Override
		public short pollLastShort() { return pollLastShortKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(ShortConsumer action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public void forEachIndexed(IntShortConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsShort(state, keys[i]);
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
					state = keys[i];
					continue;
				}
				state = operator.applyAsShort(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public short findFirst(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return keys[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) result++;
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
			return Short2IntArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Short2IntArrayMap.this.clear();
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsInt(state, values[i]);
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
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(short from) {
			index = findIndex(from);
		}
		
		@Override
		public Short2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Short2IntMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Short2IntMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Short2IntMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Short2IntMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(short from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Short2IntMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Short2IntMap.Entry previous() {
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
		public void set(Short2IntMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Short2IntMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ShortListIterator {
		public KeyIterator() {}
		public KeyIterator(short element) {
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public short previousShort() {
			return keys[previousEntry()];
		}

		@Override
		public short nextShort() {
			return keys[nextEntry()];
		}

		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }	
	}
	
	private class ValueIterator extends MapIterator implements IntListIterator {
		@Override
		public int previousInt() {
			return values[previousEntry()];
		}

		@Override
		public int nextInt() {
			return values[nextEntry()];
		}

		@Override
		public void set(int e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }	
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
		protected short key;
		protected int value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public short getShortKey() {
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
	
	private class MapEntry implements Short2IntMap.Entry, Map.Entry<Short, Integer> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)obj;
					return keys[index] == entry.getShortKey() && getIntValue() == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Integer && getShortKey() == ((Short)key).shortValue() && getIntValue() == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(getShortKey()) ^ Integer.hashCode(getIntValue());
		}
		
		@Override
		public String toString() {
			return Short.toString(getShortKey()) + "=" + Integer.toString(getIntValue());
		}
	}
}