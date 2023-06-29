package speiger.src.collections.chars.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.consumer.CharByteConsumer;
import speiger.src.collections.chars.functions.function.Char2ByteFunction;
import speiger.src.collections.chars.functions.function.CharByteUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteOrderedMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.bytes.functions.function.BytePredicate;
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
public class Char2ByteArrayMap extends AbstractChar2ByteMap implements Char2ByteOrderedMap
{
	/** The Backing keys array */
	protected transient char[] keys;
	/** The Backing values array */
	protected transient byte[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected CharOrderedSet keySet;
	/** Values cache */
	protected ByteCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Char2ByteArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Char2ByteArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new char[minCapacity];
		values = new byte[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Char2ByteArrayMap(Character[] keys, Byte[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Char2ByteArrayMap(Character[] keys, Byte[] values, int length) {
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
	public Char2ByteArrayMap(char[] keys, byte[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ByteArrayMap(char[] keys, byte[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Char2ByteArrayMap(Map<? extends Character, ? extends Byte> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Char2ByteArrayMap(Char2ByteMap map) {
		this(map.size());
		for(ObjectIterator<Char2ByteMap.Entry> iter = getFastIterator(map);iter.hasNext();size++) {
			Char2ByteMap.Entry entry = iter.next();
			keys[size] = entry.getCharKey();
			values[size] = entry.getByteValue();
		}
	}
	
	@Override
	public byte put(char key, byte value) {
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
	public byte putIfAbsent(char key, byte value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(values[index] == getDefaultReturnValue()) {
			byte oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public byte addTo(char key, byte value) {
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
	public byte subFrom(char key, byte value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		byte oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public byte putAndMoveToFirst(char key, byte value) {
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
	public byte putAndMoveToLast(char key, byte value) {
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
	public boolean moveToFirst(char key) {
		int index = findIndex(key);
		if(index > 0) {
			moveIndexToFirst(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(char key) {
		int index = findIndex(key);
		if(index >= 0 && index < size-1) {
			moveIndexToLast(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsKey(char key) {
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
	public byte get(char key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public byte getOrDefault(char key, byte defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public byte getAndMoveToFirst(char key) {
		int index = findIndex(key);
		if(index >= 0) {
			byte value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte getAndMoveToLast(char key) {
		int index = findIndex(key);
		if(index >= 0) {
			byte value = values[index];
			moveIndexToLast(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public char firstCharKey() {
		if(size <= 0) throw new NoSuchElementException();
		return keys[0];
	}
	
	@Override
	public char lastCharKey() {
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
	public char pollFirstCharKey() {
		if(size == 0) throw new NoSuchElementException();
		char result = keys[0];
		removeIndex(0);
		return result;
	}
	
	@Override
	public char pollLastCharKey() {
		if(size == 0) throw new NoSuchElementException();
		char result = keys[size-1];
		removeIndex(size-1);
		return result;
	}
	
	@Override
	public byte remove(char key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public byte removeOrDefault(char key, byte defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		byte value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(char key, byte value) {
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
	public void forEach(CharByteConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public CharOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public ByteCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(char key, byte oldValue, byte newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public byte replace(char key, byte value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte computeByte(char key, CharByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = mappingFunction.applyAsByte(key, getDefaultReturnValue());
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = mappingFunction.applyAsByte(key);
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		return newValue;
	}
		
	@Override
	public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = valueProvider.getAsByte();
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		return newValue;
	}
		
	@Override
	public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) return getDefaultReturnValue();
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) {
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
	public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = mappingFunction.applyAsByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			byte newValue = valueProvider.getAsByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) {
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
	public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) {
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
	public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2ByteMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
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
		Arrays.fill(keys, 0, size, (char)0);
		Arrays.fill(values, 0, size, (byte)0);
		size = 0;
	}
	
	public Char2ByteArrayMap copy() {
		Char2ByteArrayMap map = new Char2ByteArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		char key = keys[index];
		byte value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		char key = keys[index];
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
	
	protected void insertIndex(int index, char key, byte value) {
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
			keys[i+to] = (char)0;
			values[i+to] = (byte)0;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (char)0;
			values[size] = (byte)0;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (char)0;
		values[size] = (byte)0;
	}
	
	protected int findIndex(char key, byte value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(char key) {
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
			if(Objects.equals(key, Character.valueOf(keys[i])) && Objects.equals(value, Byte.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Character.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Byte.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2ByteMap.Entry> implements Char2ByteOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2ByteMap.Entry o) {
			return Char2ByteArrayMap.this.moveToFirst(o.getCharKey());
		}
		
		@Override
		public boolean moveToLast(Char2ByteMap.Entry o) {
			return Char2ByteArrayMap.this.moveToLast(o.getCharKey());
		}
		
		@Override
		public Char2ByteMap.Entry first() {
			return new BasicEntry(firstCharKey(), firstByteValue());
		}
		
		@Override
		public Char2ByteMap.Entry last() {
			return new BasicEntry(lastCharKey(), lastByteValue());
		}
		
		@Override
		public Char2ByteMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstCharKey(), firstByteValue());
			pollFirstCharKey();
			return entry;
		}
		
		@Override
		public Char2ByteMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastCharKey(), lastByteValue());
			pollLastCharKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(Char2ByteMap.Entry fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Char2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Char2ByteMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Char2ByteMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Char2ByteMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Char2ByteMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Char2ByteMap.Entry reduce(ObjectObjectUnaryOperator<Char2ByteMap.Entry, Char2ByteMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2ByteMap.Entry state = null;
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
		public Char2ByteMap.Entry findFirst(Predicate<Char2ByteMap.Entry> filter) {
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
		public int count(Predicate<Char2ByteMap.Entry> filter) {
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
				if(o instanceof Char2ByteMap.Entry) {
					Char2ByteMap.Entry entry = (Char2ByteMap.Entry)o;
					int index = Char2ByteArrayMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return entry.getByteValue() == Char2ByteArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Char2ByteArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(Char2ByteArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Char2ByteMap.Entry) {
					Char2ByteMap.Entry entry = (Char2ByteMap.Entry)o;
					return Char2ByteArrayMap.this.remove(entry.getCharKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Char2ByteArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Char2ByteArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2ByteArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractCharSet implements CharOrderedSet {
		@Override
		public boolean contains(char e) { return containsKey(e); }
		
		@Override
		public boolean remove(char o) {
			int oldSize = size;
			Char2ByteArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char o) { return Char2ByteArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(char o) { return Char2ByteArrayMap.this.moveToLast(o); }
		@Override
		public CharListIterator iterator() { return new KeyIterator(); }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Char2ByteArrayMap.this.size(); }
		@Override
		public void clear() { Char2ByteArrayMap.this.clear(); }
		@Override
		public char firstChar() { return firstCharKey(); }
		@Override
		public char pollFirstChar() { return pollFirstCharKey(); }
		@Override
		public char lastChar() { return lastCharKey(); }
		@Override
		public char pollLastChar() { return pollLastCharKey(); }
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(CharConsumer action) { 
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(keys[i++])); 
		}
		
		@Override
		public void forEachIndexed(IntCharConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, keys[i++])); 
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 
		}
		
		@Override
		public boolean matchesAny(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.applyAsChar(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsChar(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public char findFirst(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) return keys[i];
			}
			return (char)0;
		}
		
		@Override
		public int count(CharPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(keys[i])) result++;
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
			return Char2ByteArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2ByteArrayMap.this.clear();
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntByteConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
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
		public byte findFirst(BytePredicate filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return (byte)0;
		}
		
		@Override
		public int count(BytePredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(char from) {
			index = findIndex(from);
		}
		
		@Override
		public Char2ByteMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2ByteMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Char2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(char from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Char2ByteMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Char2ByteMap.Entry previous() {
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
		public void set(Char2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Char2ByteMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements CharListIterator {
		public KeyIterator() {}
		public KeyIterator(char element) {
			index = findIndex(element);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public char previousChar() {
			return keys[previousEntry()];
		}

		@Override
		public char nextChar() {
			return keys[nextEntry()];
		}

		@Override
		public void set(char e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(char e) { throw new UnsupportedOperationException(); }	
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
		protected char key;
		protected byte value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public char getCharKey() {
			return key;
		}

		@Override
		public byte getByteValue() {
			return value;
		}
		
		@Override
		public byte setValue(byte value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Char2ByteMap.Entry, Map.Entry<Character, Byte> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public char getCharKey() {
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
				if(obj instanceof Char2ByteMap.Entry) {
					Char2ByteMap.Entry entry = (Char2ByteMap.Entry)obj;
					return keys[index] == entry.getCharKey() && getByteValue() == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Byte && getCharKey() == ((Character)key).charValue() && getByteValue() == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(getCharKey()) ^ Byte.hashCode(getByteValue());
		}
		
		@Override
		public String toString() {
			return Character.toString(getCharKey()) + "=" + Byte.toString(getByteValue());
		}
	}
}