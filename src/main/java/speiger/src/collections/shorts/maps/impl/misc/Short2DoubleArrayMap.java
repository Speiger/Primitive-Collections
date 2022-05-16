package speiger.src.collections.shorts.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.consumer.ShortDoubleConsumer;
import speiger.src.collections.shorts.functions.function.Short2DoubleFunction;
import speiger.src.collections.shorts.functions.function.ShortDoubleUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleOrderedMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
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
public class Short2DoubleArrayMap extends AbstractShort2DoubleMap implements Short2DoubleOrderedMap
{
	/** The Backing keys array */
	protected transient short[] keys;
	/** The Backing values array */
	protected transient double[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ShortSet keySet;
	/** Values cache */
	protected DoubleCollection valuesC;
	/** EntrySet cache */
	protected FastOrderedSet entrySet;
	
	/**
	 * Default Constructor
	 */
	public Short2DoubleArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Short2DoubleArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = new short[minCapacity];
		values = new double[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Short2DoubleArrayMap(Short[] keys, Double[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them) with a custom length
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in length
	 */
	public Short2DoubleArrayMap(Short[] keys, Double[] values, int length) {
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
	public Short2DoubleArrayMap(short[] keys, double[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2DoubleArrayMap(short[] keys, double[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Short2DoubleArrayMap(Map<? extends Short, ? extends Double> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Short2DoubleArrayMap(Short2DoubleMap map) {
		this(map.size());
		for(ObjectIterator<Short2DoubleMap.Entry> iter = Short2DoubleMaps.fastIterator(map);iter.hasNext();size++) {
			Short2DoubleMap.Entry entry = iter.next();
			keys[size] = entry.getShortKey();
			values[size] = entry.getDoubleValue();
		}
	}
	
	@Override
	public double put(short key, double value) {
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
	public double putIfAbsent(short key, double value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		return values[index];
	}
	
	@Override
	public double addTo(short key, double value) {
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
	public double subFrom(short key, double value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		double oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	
	@Override
	public double putAndMoveToFirst(short key, double value) {
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
	public double putAndMoveToLast(short key, double value) {
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
		if(index < size-1) {
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
	public double get(short key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public double getOrDefault(short key, double defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public double getAndMoveToFirst(short key) {
		int index = findIndex(key);
		if(index >= 0) {
			double value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double getAndMoveToLast(short key) {
		int index = findIndex(key);
		if(index >= 0) {
			double value = values[index];
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
	public double remove(short key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public double removeOrDefault(short key, double defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		double value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(short key, double value) {
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
	public void forEach(ShortDoubleConsumer action) {
		if(size() <= 0) return;
		for(int i = 0;i<size;i++)
			action.accept(keys[i], values[i]);
	}
	
	@Override
	public ShortSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}

	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Short2DoubleMap.Entry> short2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(short key, double oldValue, double newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public double replace(short key, double value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public double computeDouble(short key, ShortDoubleUnaryOperator mappingFunction) {
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
	public double computeDoubleIfAbsent(short key, Short2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = mappingFunction.get(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = mappingFunction.get(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public double supplyDoubleIfAbsent(short key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			double newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public double computeDoubleIfPresent(short key, ShortDoubleUnaryOperator mappingFunction) {
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
	public double mergeDouble(short key, double value, DoubleDoubleUnaryOperator mappingFunction) {
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
	public void mergeAllDouble(Short2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2DoubleMap.Entry entry : Short2DoubleMaps.fastIterable(m)) {
			short key = entry.getShortKey();
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
		Arrays.fill(keys, 0, size, (short)0);
		Arrays.fill(values, 0, size, 0D);
		size = 0;
	}
	
	public Short2DoubleArrayMap copy() {
		Short2DoubleArrayMap map = new Short2DoubleArrayMap();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		short key = keys[index];
		double value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		short key = keys[index];
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
	
	protected void insertIndex(int index, short key, double value) {
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
			values[i+to] = 0D;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = (short)0;
			values[size] = 0D;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = (short)0;
		values[size] = 0D;
	}
	
	protected int findIndex(short key, double value) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key && Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return i;
		return -1;		
	}
	
	protected int findIndex(short key) {
		for(int i = size-1;i>=0;i--)
			if(keys[i] == key) return i;
		return -1;
	}
	
	protected int findValue(double value) {
		for(int i = size-1;i>=0;i--)
			if(Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return i;
		return -1;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Short.valueOf(keys[i])) && Objects.equals(value, Double.valueOf(values[i]))) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, Short.valueOf(keys[i]))) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, Double.valueOf(values[i]))) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Short2DoubleMap.Entry> implements Short2DoubleOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Short2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Short2DoubleMap.Entry o) {
			return Short2DoubleArrayMap.this.moveToFirst(o.getShortKey());
		}
		
		@Override
		public boolean moveToLast(Short2DoubleMap.Entry o) {
			return Short2DoubleArrayMap.this.moveToLast(o.getShortKey());
		}
		
		@Override
		public Short2DoubleMap.Entry first() {
			return new BasicEntry(firstShortKey(), firstDoubleValue());
		}
		
		@Override
		public Short2DoubleMap.Entry last() {
			return new BasicEntry(lastShortKey(), lastDoubleValue());
		}
		
		@Override
		public Short2DoubleMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstShortKey(), firstDoubleValue());
			pollFirstShortKey();
			return entry;
		}
		
		@Override
		public Short2DoubleMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastShortKey(), lastDoubleValue());
			pollLastShortKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> iterator(Short2DoubleMap.Entry fromElement) {
			return new EntryIterator(fromElement.getShortKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> fastIterator(short fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Short2DoubleMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Short2DoubleMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			BasicEntry entry = new BasicEntry();
			for(int i = 0;i<size;i++) {
				entry.set(keys[i], values[i]);
				action.accept(entry);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Short2DoubleMap.Entry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Short2DoubleMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Short2DoubleMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Short2DoubleMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Short2DoubleMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Short2DoubleMap.Entry reduce(ObjectObjectUnaryOperator<Short2DoubleMap.Entry, Short2DoubleMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Short2DoubleMap.Entry state = null;
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
		public Short2DoubleMap.Entry findFirst(Object2BooleanFunction<Short2DoubleMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Short2DoubleMap.Entry> filter) {
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
				if(o instanceof Short2DoubleMap.Entry) {
					Short2DoubleMap.Entry entry = (Short2DoubleMap.Entry)o;
					int index = Short2DoubleArrayMap.this.findIndex(entry.getShortKey());
					if(index >= 0) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(Short2DoubleArrayMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Short2DoubleArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Double.valueOf(Short2DoubleArrayMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Short2DoubleMap.Entry) {
					Short2DoubleMap.Entry entry = (Short2DoubleMap.Entry)o;
					return Short2DoubleArrayMap.this.remove(entry.getShortKey(), entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Short2DoubleArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Short2DoubleArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Short2DoubleArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractShortSet implements ShortOrderedSet {
		@Override
		public boolean contains(short e) { return containsKey(e); }
		
		@Override
		public boolean remove(short o) {
			int oldSize = size;
			Short2DoubleArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short o) { return Short2DoubleArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(short o) { return Short2DoubleArrayMap.this.moveToLast(o); }
		@Override
		public ShortListIterator iterator() { return new KeyIterator(); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Short2DoubleArrayMap.this.size(); }
		@Override
		public void clear() { Short2DoubleArrayMap.this.clear(); }
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
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(input, keys[i++])); 

		}
		
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(keys[i])) return false;
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
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) return keys[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(keys[i])) result++;
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
			return Short2DoubleArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Short2DoubleArrayMap.this.clear();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.get(values[i])) return false;
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
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) return values[i];
			}
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2DoubleMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(short from) {
			index = findIndex(from);
		}
		
		@Override
		public Short2DoubleMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Short2DoubleMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Short2DoubleMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Short2DoubleMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Short2DoubleMap.Entry> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(short from) {
			index = findIndex(from);
		}
		
		@Override
		public Short2DoubleMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Short2DoubleMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
		
		@Override
		public void set(Short2DoubleMap.Entry e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Short2DoubleMap.Entry e) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ShortListIterator {
		public KeyIterator() {}
		public KeyIterator(short element) {
			index = findIndex(element);
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
	
	private class MapEntry implements Short2DoubleMap.Entry, Map.Entry<Short, Double> {
		int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2DoubleMap.Entry) {
					Short2DoubleMap.Entry entry = (Short2DoubleMap.Entry)obj;
					return keys[index] == entry.getShortKey() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Double && keys[index] == ((Short)key).shortValue() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(keys[index]) ^ Double.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Short.toString(keys[index]) + "=" + Double.toString(values[index]);
		}
	}
}