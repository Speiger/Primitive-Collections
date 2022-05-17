package speiger.src.collections.bytes.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.consumer.ByteByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ByteFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteOrderedMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2ByteMaps;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
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
public class Byte2ByteArrayMap extends AbstractByte2ByteMap implements Byte2ByteOrderedMap
{
	/** The Backing keys array */
	protected transient byte[] keys;
	/** The Backing values array */
	protected transient byte[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ByteSet keySet;
	/** Values cache */
	protected ByteCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Byte2ByteArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Byte2ByteArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new byte[minCapacity];
		values = new byte[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Byte2ByteArrayMap(Byte[] keys, Byte[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Byte2ByteArrayMap(Byte[] keys, Byte[] values, int length) {
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
	public Byte2ByteArrayMap(byte[] keys, byte[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2ByteArrayMap(byte[] keys, byte[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Byte2ByteArrayMap(Map<? extends Byte, ? extends Byte> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Byte2ByteArrayMap(Byte2ByteMap map) {
		this(map.size());
		for(ObjectIterator<Byte2ByteMap.Entry> iter = Byte2ByteMaps.fastIterator(map);iter.hasNext();size++) {
			Byte2ByteMap.Entry entry = iter.next();
			keys[size] = entry.getByteKey();
			values[size] = entry.getByteValue();
		}
	}
	
	@Override
	public byte put(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte putIfAbsent(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public byte addTo(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		byte oldValue = values[index];
		values[index] += value;
		return oldValue;
	}
	
	@Override
	public byte subFrom(byte key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		byte oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public byte putAndMoveToFirst(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		byte lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public byte putAndMoveToLast(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		byte lastValue = values[index];
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
		if(index < size-1) {
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
	public boolean containsValue(byte value) {
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
	public byte get(byte key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public byte getOrDefault(byte key, byte defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public byte getAndMoveToFirst(byte key) {
		int index = findIndex(key);
		if(index >= 0) {
			byte value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte getAndMoveToLast(byte key) {
		int index = findIndex(key);
		if(index >= 0) {
			byte value = values[index];
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
	public byte firstByteValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public byte lastByteValue() {
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
	public byte remove(byte key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public byte removeOrDefault(byte key, byte defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		byte value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(byte key, byte value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public Byte remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return Byte.valueOf(getDefaultReturnValue());
		byte value = values[index];
		removeIndex(index);
		return Byte.valueOf(value);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(ByteByteConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public ByteSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public ByteCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(byte key, byte oldValue, byte newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public byte replace(byte key, byte value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte computeByte(byte key, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = mappingFunction.applyAsByte(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(byte key, Byte2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte supplyByteIfAbsent(byte key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = valueProvider.getByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte computeByteIfPresent(byte key, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte mergeByte(byte key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		byte newValue = index == -1 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Byte2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2ByteMap.Entry entry : Byte2ByteMaps.fastIterable(m)) {
			byte key = entry.getByteKey();
			int index = findIndex(key);
			byte newValue = index == -1 || values[index] == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(values[index], entry.getByteValue());
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
		Arrays.fill(values, 0, size, (byte)0);
		size = 0;
	}
	
	public Byte2ByteArrayMap copy() {
		Byte2ByteArrayMap map = new Byte2ByteArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		byte key = keys[index];
		byte value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		byte key = keys[index];
		byte value = values[index];
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
	
	protected void insertIndex(int index, byte key, byte value) {
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
			values[i+to] = (byte)0;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (byte)0;
			values[size] = (byte)0;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (byte)0;
		values[size] = (byte)0;
	}
	
	protected int findIndex(byte key, byte value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(byte key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(byte value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Byte.valueOf(keys[i])) && Objects.equals(value, Byte.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Byte.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Byte.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Byte2ByteMap.Entry> implements Byte2ByteOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Byte2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Byte2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Byte2ByteMap.Entry o) {
			return Byte2ByteArrayMap.this.moveToFirst(o.getByteKey());
		}
		
		@Override
		public boolean moveToLast(Byte2ByteMap.Entry o) {
			return Byte2ByteArrayMap.this.moveToLast(o.getByteKey());
		}
		
		@Override
		public Byte2ByteMap.Entry first() {
			return new BasicEntry(firstByteKey(), firstByteValue());
		}
		
		@Override
		public Byte2ByteMap.Entry last() {
			return new BasicEntry(lastByteKey(), lastByteValue());
		}
		
		@Override
		public Byte2ByteMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstByteKey(), firstByteValue());
			pollFirstByteKey();
			return entry;
		}
		
		@Override
		public Byte2ByteMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastByteKey(), lastByteValue());
			pollLastByteKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> iterator(Byte2ByteMap.Entry fromElement) {
			return new EntryIterator(fromElement.getByteKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator(byte fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Byte2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Byte2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				action.accept(entry);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Byte2ByteMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Byte2ByteMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Byte2ByteMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Byte2ByteMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Byte2ByteMap.Entry reduce(ObjectObjectUnaryOperator<Byte2ByteMap.Entry, Byte2ByteMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Byte2ByteMap.Entry state = null;
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
		public Byte2ByteMap.Entry findFirst(Object2BooleanFunction<Byte2ByteMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Byte2ByteMap.Entry> filter) {
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
				if(o instanceof Byte2ByteMap.Entry) {
					Byte2ByteMap.Entry entry = (Byte2ByteMap.Entry)o;
					int index = Byte2ByteArrayMap.this.findIndex(entry.getByteKey());
					if(index >= 0) return entry.getByteValue() == Byte2ByteArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Byte2ByteArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(Byte2ByteArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Byte2ByteMap.Entry) {
					Byte2ByteMap.Entry entry = (Byte2ByteMap.Entry)o;
					return Byte2ByteArrayMap.this.remove(entry.getByteKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Byte2ByteArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Byte2ByteArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2ByteArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractByteSet implements ByteOrderedSet {
		@Override
		public boolean contains(byte e) { return containsKey(e); }
		
		@Override
		public boolean remove(byte o) {
			int oldSize = size;
			Byte2ByteArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte o) { return Byte2ByteArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(byte o) { return Byte2ByteArrayMap.this.moveToLast(o); }
		@Override
		public ByteListIterator iterator() { return new KeyIterator(); }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Byte2ByteArrayMap.this.size(); }
		@Override
		public void clear() { Byte2ByteArrayMap.this.clear(); }
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
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 

		}
		
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(keys[i])) return false;
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
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return keys[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractByteCollection {
		@Override
		public boolean contains(byte e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(byte o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ByteIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Byte2ByteArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Byte2ByteArrayMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsByte(state, values[i]);
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
					state = values[i];
					continue;
				}
				state = operator.applyAsByte(state, values[i]);
			}
			return state;
		}
		
		@Override
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return values[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Byte2ByteMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(byte from) {
			index = findIndex(from);
		}
		
		@Override
		public Byte2ByteMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Byte2ByteMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Byte2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Byte2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Byte2ByteMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(byte from) {
			index = findIndex(from);
		}
		
		@Override
		public Byte2ByteMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Byte2ByteMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
		
		@Override
		public void set(Byte2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Byte2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ByteListIterator {
		public KeyIterator() {}
		public KeyIterator(byte element) {
			index = findIndex(element);
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
	
	private class ValueIterator extends MapIterator implements ByteListIterator {
		@Override
		public byte previousByte() {
			return values[previousEntry()];
		}

		@Override
		public byte nextByte() {
			return values[nextEntry()];
		}

		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }	
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
	
	private class MapEntry implements Byte2ByteMap.Entry, Map.Entry<Byte, Byte> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public byte getByteKey() {
			return keys[index];
		}

		@Override
		public byte getByteValue() {
			return values[index];
		}

		@Override
		public byte setValue(byte value) {
			byte oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2ByteMap.Entry) {
					Byte2ByteMap.Entry entry = (Byte2ByteMap.Entry)obj;
					return keys[index] == entry.getByteKey() && values[index] == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Byte && keys[index] == ((Byte)key).byteValue() && values[index] == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(keys[index]) ^ Byte.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Byte.toString(keys[index]) + "=" + Byte.toString(values[index]);
		}
	}
}