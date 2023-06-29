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
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.UnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
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
 * @param <V> the keyType of elements maintained by this Collection
 */
public class Object2ObjectArrayMap<T, V> extends AbstractObject2ObjectMap<T, V> implements Object2ObjectOrderedMap<T, V>
{
	/** The Backing keys array */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient V[] values;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** KeySet cache */
	protected ObjectOrderedSet<T> keySet;
	/** Values cache */
	protected ObjectCollection<V> valuesC;
	/** EntrySet cache */
	protected FastOrderedSet<T, V> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Object2ObjectArrayMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2ObjectArrayMap(int minCapacity) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		keys = (T[])new Object[minCapacity];
		values = (V[])new Object[minCapacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ObjectArrayMap(T[] keys, V[] values) {
		this(keys, values, keys.length);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param length the amount of values that should be pulled from the array
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ObjectArrayMap(T[] keys, V[] values, int length) {
		this(length);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		putAll(keys, values, 0, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2ObjectArrayMap(Map<? extends T, ? extends V> map) {
		this(map.size());
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2ObjectArrayMap(Object2ObjectMap<T, V> map) {
		this(map.size());
		for(ObjectIterator<Object2ObjectMap.Entry<T, V>> iter = getFastIterator(map);iter.hasNext();size++) {
			Object2ObjectMap.Entry<T, V> entry = iter.next();
			keys[size] = entry.getKey();
			values[size] = entry.getValue();
		}
	}
	
	@Override
	public V put(T key, V value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		V oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public V putIfAbsent(T key, V value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		else if(Objects.equals(values[index], getDefaultReturnValue())) {
			V oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		return values[index];
	}
	
	@Override
	public V putAndMoveToFirst(T key, V value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(0, key, value);
			size++;
			return getDefaultReturnValue();
		}
		V lastValue = values[index];
		values[index] = value;
		moveIndexToFirst(index);
		return lastValue;
	}
	
	@Override
	public V putAndMoveToLast(T key, V value) {
		int index = findIndex(key);
		if(index < 0) {
			insertIndex(size++, key, value);
			return getDefaultReturnValue();
		}
		V lastValue = values[index];
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
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(Object value) {
		return findValue(value) >= 0;
	}
	
	@Override
	public V getObject(T key) {
		int index = findIndex(key);
		return index < 0 ? getDefaultReturnValue() : values[index];
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		int index = findIndex(key);
		return index < 0 ? defaultValue : values[index];
	}
	
	@Override
	public V getAndMoveToFirst(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			V value = values[index];
			moveIndexToFirst(index);
			return value;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public V getAndMoveToLast(T key) {
		int index = findIndex(key);
		if(index >= 0) {
			V value = values[index];
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
	public V firstValue() {
		if(size <= 0) throw new NoSuchElementException();
		return values[0];
	}
	
	@Override
	public V lastValue() {
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
	public V rem(T key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		V value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public V remOrDefault(T key, V defaultValue) {
		int index = findIndex(key);
		if(index < 0) return defaultValue;
		V value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public V remove(Object key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		V value = values[index];
		removeIndex(index);
		return value;
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = findIndex(key, value);
		if(index < 0) return false;
		removeIndex(index);
		return true;
	}
	
	@Override
	public void forEach(ObjectObjectConsumer<T, V> action) {
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
	public ObjectCollection<V> values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}

	@Override
	public ObjectOrderedSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public boolean replace(T key, V oldValue, V newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public V replace(T key, V value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		V oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			V newValue = mappingFunction.apply(key, getDefaultReturnValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		V newValue = mappingFunction.apply(key, values[index]);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public V computeIfAbsent(T key, UnaryOperator<T, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1) {
			V newValue = mappingFunction.apply(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		V newValue = values[index];
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			newValue = mappingFunction.apply(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
		
	@Override
	public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index == -1) {
			V newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			insertIndex(size++, key, newValue);
			return newValue;
		}
		V newValue = values[index];
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
		
	@Override
	public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index == -1 || Objects.equals(values[index], getDefaultReturnValue())) return getDefaultReturnValue();
		V newValue = mappingFunction.apply(key, values[index]);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Objects.requireNonNull(value);
		int index = findIndex(key);
		V newValue = index == -1 || Objects.equals(values[index], getDefaultReturnValue()) ? value : mappingFunction.apply(values[index], value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index == -1) insertIndex(size++, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ObjectMap.Entry<T, V> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			V newValue = index == -1 || Objects.equals(values[index], getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(values[index], entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) {
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
		Arrays.fill(values, 0, size, null);
		size = 0;
	}
	
	public Object2ObjectArrayMap<T, V> copy() {
		Object2ObjectArrayMap<T, V> map = new Object2ObjectArrayMap<>();
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, keys.length);
		return map;
	}
	
	protected void moveIndexToFirst(int index) {
		if(index == 0) return;
		T key = keys[index];
		V value = values[index];
		System.arraycopy(keys, 0, keys, 1, index);
		System.arraycopy(values, 0, values, 1, index);
		keys[0] = key;
		values[0] = value;
	}
	
	protected void moveIndexToLast(int index) {
		if(index == size-1) return;
		T key = keys[index];
		V value = values[index];
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
	
	protected void insertIndex(int index, T key, V value) {
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
			values[i+to] = null;
		}
		size -= length;
	}
	
	protected void removeIndex(int index) {
		if(index == size-1) {
			size--;
			keys[size] = null;
			values[size] = null;
			return;
		}
		System.arraycopy(keys, index+1, keys, index, size-index-1);
		System.arraycopy(values, index+1, values, index, size-index-1);		
		size--;
		keys[size] = null;
		values[size] = null;
	}
	
	protected int findIndex(Object key, Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i]) && Objects.equals(value, values[i])) return i;
		return -1;		
	}
	
	protected int findIndex(Object key) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(key, keys[i])) return i;
		return -1;
	}
	
	protected int findValue(Object value) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(value, values[i])) return i;
		return -1;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<T, V>> implements Object2ObjectOrderedMap.FastOrderedSet<T, V> {
		@Override
		public boolean addAndMoveToFirst(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2ObjectMap.Entry<T, V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2ObjectMap.Entry<T, V> o) {
			return Object2ObjectArrayMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2ObjectMap.Entry<T, V> o) {
			return Object2ObjectArrayMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> first() {
			return new BasicEntry<>(firstKey(), firstValue());
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> last() {
			return new BasicEntry<>(lastKey(), lastValue());
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> pollFirst() {
			BasicEntry<T, V> entry = new BasicEntry<>(firstKey(), firstValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> pollLast() {
			BasicEntry<T, V> entry = new BasicEntry<>(lastKey(), lastValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> iterator(Object2ObjectMap.Entry<T, V> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2ObjectMap.Entry<T, V>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2ObjectMap.Entry<T, V>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			MapEntry entry = new MapEntry();
			for(int i = 0;i<size;i++) {
				entry.set(i);
				action.accept(entry);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2ObjectMap.Entry<T, V>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(i, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ObjectMap.Entry<T, V>> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++) {
				action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2ObjectMap.Entry<T, V>> filter) {
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
		public boolean matchesNone(Predicate<Object2ObjectMap.Entry<T, V>> filter) {
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
		public boolean matchesAll(Predicate<Object2ObjectMap.Entry<T, V>> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Object2ObjectMap.Entry<T, V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> reduce(ObjectObjectUnaryOperator<Object2ObjectMap.Entry<T, V>, Object2ObjectMap.Entry<T, V>> operator) {
			Objects.requireNonNull(operator);
			Object2ObjectMap.Entry<T, V> state = null;
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
		public Object2ObjectMap.Entry<T, V> findFirst(Predicate<Object2ObjectMap.Entry<T, V>> filter) {
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
		public int count(Predicate<Object2ObjectMap.Entry<T, V>> filter) {
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
				if(o instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)o;
					int index = Object2ObjectArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Object2ObjectArrayMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2ObjectArrayMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Object2ObjectArrayMap.this.values[index]);
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)o;
					return Object2ObjectArrayMap.this.remove(entry.getKey(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2ObjectArrayMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Object2ObjectArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ObjectArrayMap.this.clear();
		}
	}
	
	private class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		public boolean contains(Object e) { return containsKey(e); }
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2ObjectArrayMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T o) { return Object2ObjectArrayMap.this.moveToFirst(o); }
		@Override
		public boolean moveToLast(T o) { return Object2ObjectArrayMap.this.moveToLast(o); }
		@Override
		public ObjectListIterator<T> iterator() { return new KeyIterator(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { return new KeyIterator(fromElement); } 
		@Override
		public int size() { return Object2ObjectArrayMap.this.size(); }
		@Override
		public void clear() { Object2ObjectArrayMap.this.clear(); }
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
	
	private class Values extends AbstractObjectCollection<V> {
		@Override
		public boolean contains(Object e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(V o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<V> iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Object2ObjectArrayMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2ObjectArrayMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super V> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(values[i++]));
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<V> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;action.accept(i, values[i++]));
		}
		
		@Override
		public boolean matchesAny(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(!filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = 0;i<size;i++) {
				state = operator.apply(state, values[i]);
			}
			return state;
		}
		
		@Override
		public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
			Objects.requireNonNull(operator);
			V state = null;
			boolean empty = true;
			for(int i = 0;i<size;i++) {
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.apply(state, values[i]);
			}
			return state;
		}
		
		@Override
		public V findFirst(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) return values[i];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(int i = 0;i<size;i++) {
				if(filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<T, V>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			index = findIndex(from);
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2ObjectMap.Entry<T, V> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2ObjectMap.Entry<T, V> e) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<T, V>> {
		MapEntry entry = null;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			index = findIndex(from);
			if(index == -1) throw new NoSuchElementException();
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> previous() {
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
		public void set(Object2ObjectMap.Entry<T, V> e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(Object2ObjectMap.Entry<T, V> e) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements ObjectListIterator<V> {
		@Override
		public V previous() {
			return values[previousEntry()];
		}

		@Override
		public V next() {
			return values[nextEntry()];
		}

		@Override
		public void set(V e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(V e) { throw new UnsupportedOperationException(); }	
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
		protected V value;
		
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
		public V getValue() {
			return value;
		}
		
		@Override
		public V setValue(V value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	private class MapEntry implements Object2ObjectMap.Entry<T, V>, Map.Entry<T, V> {
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
		public V getValue() {
			return values[index];
		}

		@Override
		public V setValue(V value) {
			V oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)obj;
					return Objects.equals(keys[index], entry.getKey()) && Objects.equals(getValue(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return Objects.equals(getKey(), key) && Objects.equals(getValue(), value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Objects.toString(getValue());
		}
	}
}