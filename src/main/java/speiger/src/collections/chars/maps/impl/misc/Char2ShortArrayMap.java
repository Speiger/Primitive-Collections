package speiger.src.collections.chars.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.functions.consumer.CharShortConsumer;
import speiger.src.collections.chars.functions.function.Char2ShortFunction;
import speiger.src.collections.chars.functions.function.CharShortUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortOrderedMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
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
public class Char2ShortArrayMap extends AbstractChar2ShortMap implements Char2ShortOrderedMap
{
	/** The Backing keys array */
	protected transient char[] keys;
	/** The Backing values array */
	protected transient short[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected CharSet keySet;
	/** Values cache */
	protected ShortCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Char2ShortArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Char2ShortArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new char[minCapacity];
		values = new short[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Char2ShortArrayMap(Character[] keys, Short[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Char2ShortArrayMap(Character[] keys, Short[] values, int length) {
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
	public Char2ShortArrayMap(char[] keys, short[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ShortArrayMap(char[] keys, short[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Char2ShortArrayMap(Map<? extends Character, ? extends Short> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Char2ShortArrayMap(Char2ShortMap map) {
		this(map.size());
		for(ObjectIterator<Char2ShortMap.Entry> iter = Char2ShortMaps.fastIterator(map);iter.hasNext();size++) {
			Char2ShortMap.Entry entry = iter.next();
			keys[size] = entry.getCharKey();
			values[size] = entry.getShortValue();
		}
	}
	
	@Override
	public short put(char key, short value) {
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
	public short putIfAbsent(char key, short value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public short addTo(char key, short value) {
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
	public short subFrom(char key, short value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		short oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public short putAndMoveToFirst(char key, short value) {
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
	public short putAndMoveToLast(char key, short value) {
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
		if(index < size-1) {
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
	public short get(char key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public short getOrDefault(char key, short defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public short getAndMoveToFirst(char key) {
		int index = findIndex(key);
		if(index >= 0) {
			short value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short getAndMoveToLast(char key) {
		int index = findIndex(key);
		if(index >= 0) {
			short value = values[index];
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
	public short remove(char key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public short removeOrDefault(char key, short defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		short value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(char key, short value) {
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
	public void forEach(CharShortConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public CharSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Char2ShortMap.Entry> char2ShortEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(char key, short oldValue, short newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public short replace(char key, short value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(char key, CharShortUnaryOperator mappingFunction) {
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
	public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			short newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) {
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
	public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) {
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
	public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2ShortMap.Entry entry : Char2ShortMaps.fastIterable(m)) {
			char key = entry.getCharKey();
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
		Arrays.fill(keys, 0, size, (char)0);
		Arrays.fill(values, 0, size, (short)0);
		size = 0;
	}
	
	public Char2ShortArrayMap copy() {
		Char2ShortArrayMap map = new Char2ShortArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		char key = keys[index];
		short value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		char key = keys[index];
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
	
	protected void insertIndex(int index, char key, short value) {
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
			values[i+to] = (short)0;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (char)0;
			values[size] = (short)0;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (char)0;
		values[size] = (short)0;
	}
	
	protected int findIndex(char key, short value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && values[i] == value) return i;
		return -1;		
	}
	
	protected int findIndex(char key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(short value) {
		for(int i = size-1;i>=0;i--)
			if(values[i] == value) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Character.valueOf(keys[i])) && Objects.equals(value, Short.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Character.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Short.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2ShortMap.Entry> implements Char2ShortOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2ShortMap.Entry o) {
			return Char2ShortArrayMap.this.moveToFirst(o.getCharKey());
		}
		
		@Override
		public boolean moveToLast(Char2ShortMap.Entry o) {
			return Char2ShortArrayMap.this.moveToLast(o.getCharKey());
		}
		
		@Override
		public Char2ShortMap.Entry first() {
			return new BasicEntry(firstCharKey(), firstShortValue());
		}
		
		@Override
		public Char2ShortMap.Entry last() {
			return new BasicEntry(lastCharKey(), lastShortValue());
		}
		
		@Override
		public Char2ShortMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstCharKey(), firstShortValue());
			pollFirstCharKey();
			return entry;
		}
		
		@Override
		public Char2ShortMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastCharKey(), lastShortValue());
			pollLastCharKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> iterator(Char2ShortMap.Entry fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ShortMap.Entry> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				action.accept(entry);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Char2ShortMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Char2ShortMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Char2ShortMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Char2ShortMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Char2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Char2ShortMap.Entry, Char2ShortMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2ShortMap.Entry state = null;
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
		public Char2ShortMap.Entry findFirst(Object2BooleanFunction<Char2ShortMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Char2ShortMap.Entry> filter) {
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
				if(o instanceof Char2ShortMap.Entry) {
					Char2ShortMap.Entry entry = (Char2ShortMap.Entry)o;
					int index = Char2ShortArrayMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return entry.getShortValue() == Char2ShortArrayMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Char2ShortArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Short.valueOf(Char2ShortArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Char2ShortMap.Entry) {
					Char2ShortMap.Entry entry = (Char2ShortMap.Entry)o;
					return Char2ShortArrayMap.this.remove(entry.getCharKey(), entry.getShortValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Char2ShortArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Char2ShortArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2ShortArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractCharSet implements CharOrderedSet {
		@Override
		public boolean contains(char e) { return containsKey(e); }
		
		@Override
		public boolean remove(char o) {
			int oldSize = size;
			Char2ShortArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char o) { return Char2ShortArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(char o) { return Char2ShortArrayMap.this.moveToLast(o); }
		@Override
		public CharListIterator iterator() { return new KeyIterator(); }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Char2ShortArrayMap.this.size(); }
		@Override
		public void clear() { Char2ShortArrayMap.this.clear(); }
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
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 

		}
		
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(keys[i])) return false;
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
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return keys[i];
			}
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) result++;
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
			return Char2ShortArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2ShortArrayMap.this.clear();
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(values[i])) return false;
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
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return values[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2ShortMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(char from) {
			index = findIndex(from);
		}
		
		@Override
		public Char2ShortMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2ShortMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2ShortMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Char2ShortMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2ShortMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(char from) {
			index = findIndex(from);
		}
		
		@Override
		public Char2ShortMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Char2ShortMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
		
		@Override
		public void set(Char2ShortMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Char2ShortMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements CharListIterator {
		public KeyIterator() {}
		public KeyIterator(char element) {
			index = findIndex(element);
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
	
	private class MapEntry implements Char2ShortMap.Entry, Map.Entry<Character, Short> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public char getCharKey() {
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
				if(obj instanceof Char2ShortMap.Entry) {
					Char2ShortMap.Entry entry = (Char2ShortMap.Entry)obj;
					return keys[index] == entry.getCharKey() && values[index] == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Short && keys[index] == ((Character)key).charValue() && values[index] == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(keys[index]) ^ Short.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Character.toString(keys[index]) + "=" + Short.toString(values[index]);
		}
	}
}