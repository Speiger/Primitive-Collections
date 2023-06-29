package speiger.src.collections.bytes.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.bytes.functions.consumer.ByteBooleanConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.functions.function.ByteBooleanUnaryOperator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanOrderedMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
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
public class Byte2BooleanArrayMap extends AbstractByte2BooleanMap implements Byte2BooleanOrderedMap
{
	/** The Backing keys array */
	protected transient byte[] keys;
	/** The Backing values array */
	protected transient boolean[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ByteOrderedSet keySet;
	/** Values cache */
	protected BooleanCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Byte2BooleanArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Byte2BooleanArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new byte[minCapacity];
		values = new boolean[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Byte2BooleanArrayMap(Byte[] keys, Boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Byte2BooleanArrayMap(Byte[] keys, Boolean[] values, int length) {
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
	public Byte2BooleanArrayMap(byte[] keys, boolean[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2BooleanArrayMap(byte[] keys, boolean[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Byte2BooleanArrayMap(Map<? extends Byte, ? extends Boolean> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Byte2BooleanArrayMap(Byte2BooleanMap map) {
		this(map.size());
		for(ObjectIterator<Byte2BooleanMap.Entry> iter = getFastIterator(map);iter.hasNext();size++) {
			Byte2BooleanMap.Entry entry = iter.next();
			keys[size] = entry.getByteKey();
			values[size] = entry.getBooleanValue();
		}
	}
	
	@Override
	public boolean put(byte key, boolean value) {
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
	public boolean putIfAbsent(byte key, boolean value) {
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
	public boolean putAndMoveToFirst(byte key, boolean value) {
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
	public boolean putAndMoveToLast(byte key, boolean value) {
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
	public boolean moveToFirst(byte key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(byte key) {
		int index = findIndex(key);
		if(index >= 0 && index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsKey(byte key) {
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
	public boolean get(byte key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public boolean getOrDefault(byte key, boolean defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public boolean getAndMoveToFirst(byte key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean getAndMoveToLast(byte key) {
		int index = findIndex(key);
		if(index >= 0) {
			boolean value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte firstByteKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public byte lastByteKey() {
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
	public byte pollFirstByteKey() {
		if(size == 0) throw new NoSuchElementException();
		byte result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public byte pollLastByteKey() {
		if(size == 0) throw new NoSuchElementException();
		byte result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public boolean remove(byte key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean removeOrDefault(byte key, boolean defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		boolean value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(byte key, boolean value) {
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
	public void forEach(ByteBooleanConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public ByteOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public BooleanCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(byte key, boolean oldValue, boolean newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public boolean replace(byte key, boolean value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		boolean oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public boolean computeBoolean(byte key, ByteBooleanUnaryOperator mappingFunction) {
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
	public boolean computeBooleanIfAbsent(byte key, BytePredicate mappingFunction) {
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
	public boolean supplyBooleanIfAbsent(byte key, BooleanSupplier valueProvider) {
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
	public boolean computeBooleanIfPresent(byte key, ByteBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) {
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
	public boolean computeBooleanIfAbsentNonDefault(byte key, BytePredicate mappingFunction) {
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
	public boolean supplyBooleanIfAbsentNonDefault(byte key, BooleanSupplier valueProvider) {
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
	public boolean computeBooleanIfPresentNonDefault(byte key, ByteBooleanUnaryOperator mappingFunction) {
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
	public boolean mergeBoolean(byte key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
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
	public void mergeAllBoolean(Byte2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2BooleanMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
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
		Arrays.fill(keys, 0, size, (byte)0);
		Arrays.fill(values, 0, size, false);
		size = 0;
	}
	
	public Byte2BooleanArrayMap copy() {
		Byte2BooleanArrayMap map = new Byte2BooleanArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		byte key = keys[index];
		boolean value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		byte key = keys[index];
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
	
	protected void insertIndex(int index, byte key, boolean value) {
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
			keys[i+to] = (byte)0;
			values[i+to] = false;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (byte)0;
			values[size] = false;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (byte)0;
		values[size] = false;
	}
	
	protected int findIndex(byte key, boolean value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(byte key) {
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
			if(Objects.equals(key, Byte.valueOf(keys[i])) && Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Byte.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Boolean.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Byte2BooleanMap.Entry> implements Byte2BooleanOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Byte2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Byte2BooleanMap.Entry o) {
			return Byte2BooleanArrayMap.this.moveToFirst(o.getByteKey());
		}
		
		@Override
		public boolean moveToLast(Byte2BooleanMap.Entry o) {
			return Byte2BooleanArrayMap.this.moveToLast(o.getByteKey());
		}
		
		@Override
		public Byte2BooleanMap.Entry first() {
			return new BasicEntry(firstByteKey(), firstBooleanValue());
		}
		
		@Override
		public Byte2BooleanMap.Entry last() {
			return new BasicEntry(lastByteKey(), lastBooleanValue());
		}
		
		@Override
		public Byte2BooleanMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstByteKey(), firstBooleanValue());
			pollFirstByteKey();
			return entry;
		}
		
		@Override
		public Byte2BooleanMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastByteKey(), lastBooleanValue());
			pollLastByteKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> iterator(Byte2BooleanMap.Entry fromElement) {
			return new EntryIterator(fromElement.getByteKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2BooleanMap.Entry> fastIterator(byte fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Byte2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Byte2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Byte2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Byte2BooleanMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Byte2BooleanMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Byte2BooleanMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Byte2BooleanMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Byte2BooleanMap.Entry reduce(ObjectObjectUnaryOperator<Byte2BooleanMap.Entry, Byte2BooleanMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Byte2BooleanMap.Entry state = null;
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
		public Byte2BooleanMap.Entry findFirst(Predicate<Byte2BooleanMap.Entry> filter) {
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
		public int count(Predicate<Byte2BooleanMap.Entry> filter) {
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
				if(o instanceof Byte2BooleanMap.Entry) {
					Byte2BooleanMap.Entry entry = (Byte2BooleanMap.Entry)o;
					int index = Byte2BooleanArrayMap.this.findIndex(entry.getByteKey());
					if(index >= 0) return entry.getBooleanValue() == Byte2BooleanArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Byte2BooleanArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Boolean.valueOf(Byte2BooleanArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2BooleanMap.Entry) {
					Byte2BooleanMap.Entry entry = (Byte2BooleanMap.Entry)o;
					return Byte2BooleanArrayMap.this.remove(entry.getByteKey(), entry.getBooleanValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Byte2BooleanArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Byte2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2BooleanArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractByteSet implements ByteOrderedSet {
		@Override
		public boolean contains(byte e) { return containsKey(e); }
		
		@Override
		public boolean remove(byte o) {
			int oldSize = size;
			Byte2BooleanArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte o) { return Byte2BooleanArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(byte o) { return Byte2BooleanArrayMap.this.moveToLast(o); }
		@Override
		public ByteListIterator iterator() { return new KeyIterator(); }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Byte2BooleanArrayMap.this.size(); }
		@Override
		public void clear() { Byte2BooleanArrayMap.this.clear(); }
		@Override
		public byte firstByte() { return firstByteKey(); }
		@Override
		public byte pollFirstByte() { return pollFirstByteKey(); }
		@Override
		public byte lastByte() { return lastByteKey(); }
		@Override
		public byte pollLastByte() { return pollLastByteKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(ByteConsumer action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public void forEachIndexed(IntByteConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 
		}
		
		@Override
		public boolean matchesAny(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsByte(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
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
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return keys[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(BytePredicate filter) {
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
			return Byte2BooleanArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2BooleanArrayMap.this.clear();
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Byte2BooleanMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(byte from) {
			index = findIndex(from);
		}
		
		@Override
		public Byte2BooleanMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Byte2BooleanMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Byte2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Byte2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Byte2BooleanMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(byte from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Byte2BooleanMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Byte2BooleanMap.Entry previous() {
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
		public void set(Byte2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Byte2BooleanMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ByteListIterator {
		public KeyIterator() {}
		public KeyIterator(byte element) {
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public byte previousByte() {
			return keys[previousEntry()];
		}

		@Override
		public byte nextByte() {
			return keys[nextEntry()];
		}

		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }	
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
		protected byte key;
		protected boolean value;
		
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
		public boolean getBooleanValue() {
			return value;
		}
		
		@Override
		public boolean setValue(boolean value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Byte2BooleanMap.Entry, Map.Entry<Byte, Boolean> {
		int index = -1;
		
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
				if(obj instanceof Byte2BooleanMap.Entry) {
					Byte2BooleanMap.Entry entry = (Byte2BooleanMap.Entry)obj;
					return keys[index] == entry.getByteKey() && getBooleanValue() == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Boolean && getByteKey() == ((Byte)key).byteValue() && getBooleanValue() == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(getByteKey()) ^ Boolean.hashCode(getBooleanValue());
		}
		
		@Override
		public String toString() {
			return Byte.toString(getByteKey()) + "=" + Boolean.toString(getBooleanValue());
		}
	}
}