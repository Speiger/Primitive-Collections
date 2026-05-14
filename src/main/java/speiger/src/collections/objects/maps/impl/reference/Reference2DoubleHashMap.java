package speiger.src.collections.objects.maps.impl.reference;

import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.OptionalDouble;

import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.objects.functions.function.ToDoubleFunction;
import speiger.src.collections.objects.functions.function.ObjectDoubleUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Reference2DoubleHashMap<T> extends AbstractObject2DoubleMap<T> implements ITrimmable
{
	/** The Backing keys array */
	protected transient WeakKey<T>[] keys;
	/** The Backing values array */
	protected transient double[] values;
	/** Minimum array size the HashMap will be */
	protected transient int minCapacity;
	/** Current capacity of the HashMap */
	protected transient int capacity;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** EntrySet cache */
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient DoubleCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	
	protected final ReferenceQueue<Object> queue = new ReferenceQueue<>();
	
	/**
	 * Default Constructor
	 */
	public Reference2DoubleHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Reference2DoubleHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2DoubleHashMap(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = capacity = HashUtil.arraySize(minCapacity, loadFactor);
		mask = this.capacity - 1;
		maxFill = Math.min((int)Math.ceil(mask * loadFactor), mask);
		keys = new WeakKey[this.capacity];
		values = new double[this.capacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Reference2DoubleHashMap(T[] keys, Double[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2DoubleHashMap(T[] keys, Double[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].doubleValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Reference2DoubleHashMap(T[] keys, double[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2DoubleHashMap(T[] keys, double[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Reference2DoubleHashMap(Map<? extends T, ? extends Double> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2DoubleHashMap(Map<? extends T, ? extends Double> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Reference2DoubleHashMap(Object2DoubleMap<T> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Reference2DoubleHashMap(Object2DoubleMap<T> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public double put(T key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public double putIfAbsent(T key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(Double.doubleToLongBits(values[slot]) == Double.doubleToLongBits(getDefaultReturnValue())) {
			double oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public double addTo(T key, double value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		double oldValue = values[slot];
		values[slot] += value;
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
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(int i = capacity-1;i >= 0;i--)
			if(keys[i] != null && Double.doubleToLongBits(values[i]) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		for(int i = capacity-1;i >= 0;i--)
			if(keys[i] != null && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Double.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public double rem(T key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public double remOrDefault(T key, double defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Double remove(Object key) {
		int slot = findIndex(key);
		if(slot < 0) return Double.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(T key, double value) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		int pos = HashUtil.mix(Objects.hashCode(key)) & mask;
		WeakKey<T> current = keys[pos];
		if(current == null) return false;
		if(Objects.equals(current.get(), key) && Double.doubleToLongBits(value) == Double.doubleToLongBits(values[pos])) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == null) return false;
			else if(Objects.equals(current.get(), key) && Double.doubleToLongBits(value) == Double.doubleToLongBits(values[pos])) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		WeakKey<T> current = keys[pos];
		if(current == null) return false;
		if(Objects.equals(key, current.get()) && Objects.equals(value, Double.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == null) return false;
			else if(Objects.equals(key, current.get()) && Objects.equals(value, Double.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public double getDouble(T key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Double get(Object key) {
		int slot = findIndex(key);
		return Double.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public double getOrDefault(T key, double defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Reference2DoubleHashMap<T> copy() {
		Reference2DoubleHashMap<T> map = new Reference2DoubleHashMap<>(0, loadFactor);
		map.minCapacity = minCapacity;
		map.capacity = capacity;
		map.mask = mask;
		map.maxFill = maxFill;
		map.size = size;
		map.keys = new WeakKey[keys.length];
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) {
				map.keys[i] = new WeakKey<>(keys[i].get(), map.queue).index(i);
			}
		}
		map.values = Arrays.copyOf(values, values.length);
		return map;
	}
	
	@Override
	public ObjectSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectDoubleConsumer<T> action) {
		removeStaleEntries();
		if(size() <= 0) return;
		for(int i = capacity-1;i>=0;i--) {
			if(keys[i] != null) action.accept(keys[i].get(), values[i]);
		}
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
		if(index < 0) {
			double newValue = mappingFunction.applyAsDouble(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
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
		if(index < 0) {
			double newValue = mappingFunction.applyAsDouble(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		return newValue;
	}
	
	@Override
	public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			double newValue = valueProvider.getAsDouble();
			insert(-index-1, key, newValue);
			return newValue;
		}
		double newValue = values[index];
		return newValue;
	}
		
	@Override
	public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		double newValue = mappingFunction.applyAsDouble(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public double computeDoubleNonDefault(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			double newValue = mappingFunction.applyAsDouble(key, getDefaultReturnValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
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
		if(index < 0) {
			double newValue = mappingFunction.applyAsDouble(key);
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
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
		if(index < 0) {
			double newValue = valueProvider.getAsDouble();
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) return newValue;
			insert(-index-1, key, newValue);
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
		if(index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue())) return getDefaultReturnValue();
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
		double newValue = index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(values[index], value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2DoubleMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			double newValue = index < 0 || Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(values[index], entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
				if(index >= 0)
					removeIndex(index);
			}
			else if(index < 0) insert(-index-1, key, newValue);
			else values[index] = newValue;
		}
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) {
				keys[i].index(-1);
				keys[i] = null;
			}
		}
		Arrays.fill(values, 0D);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= capacity || this.size >= Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= capacity) {
			clear();
			return;
		}
		capacity = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(capacity * loadFactor), capacity - 1);
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) keys[i].index(-1);
		}
		keys = new WeakKey[request + 1];
		values = new double[request + 1];
		this.size = 0;
	}
	
	protected int findIndex(Object key) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		WeakKey<T> current = keys[pos];
		if(current != null) {
			if(Objects.equals(key, current.get())) return pos;
			while((current = keys[pos = (++pos & mask)]) != null)
				if(Objects.equals(key, current.get())) return pos;
		}
		return -(pos + 1);
	}
	
	protected double removeIndex(int pos) {
		double value = values[pos];
		keys[pos].index(-1);
		keys[pos] = null;
		values[pos] = 0D;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(capacity > minCapacity && size < maxFill / 4 && capacity > HashUtil.DEFAULT_MIN_CAPACITY) rehash(capacity / 2);
		return value;
	}
	
	protected void insert(int slot, T key, double value) {
		keys[slot] = new WeakKey<>(key, queue).index(slot);
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		WeakKey<T>[] newKeys = new WeakKey[newSize + 1];
		double[] newValues = new double[newSize + 1];
		for(int i = capacity, pos = 0, j = (size);j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(keys[i] != null) break;
			}
			if(newKeys[pos = HashUtil.mix(keys[i].hash()) & newMask] != null)
				while(newKeys[pos = (++pos & newMask)] != null);
			newKeys[pos] = keys[i].index(pos);
			newValues[pos] = values[i];
		}
		capacity = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(capacity * loadFactor), capacity - 1);
		keys = newKeys;
		values = newValues;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		WeakKey<T> current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if((current = keys[startPos]) == null) {
					keys[last] = null;
					values[last] = 0D;
					return;
				}
				slot = HashUtil.mix(current.hash()) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current.index(last);
			values[last] = values[startPos];
			onNodeMoved(startPos, last);
		}
	}
	
	private void removeStaleEntries() {
		for(Object o; (o = queue.poll()) != null;) {
			synchronized(queue) {
				int index = ((WeakKey<T>)o).index();
				if(index == -1) continue;
				removeIndex(index);
			}
		}
	}
	
	protected static class WeakKey<T> extends WeakReference<T> {
		int index;
		int hash;
		
		public WeakKey(T referent, ReferenceQueue<? super T> q) {
			super(referent, q);
			this.hash = Objects.hashCode(referent);
		}
		
		public WeakKey<T> index(int index) {
			this.index = index;
			return this;
		}
		
		public int hash() { return hash; }
		public int index() { return index; }
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected double value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index].get();
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
	
	protected class MapEntry implements Object2DoubleMap.Entry<T>, Map.Entry<T, Double> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public T getKey() {
			return keys[index].get();
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
					return Objects.equals(getKey(), entry.getKey()) && Double.doubleToLongBits(getDoubleValue()) == Double.doubleToLongBits(entry.getDoubleValue());
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
	
	private final class MapEntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<T>> implements Object2DoubleMap.FastEntrySet<T> {
		@Override
		public ObjectIterator<Object2DoubleMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Object2DoubleMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2DoubleMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2DoubleMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Optional<Object2DoubleMap.Entry<T>> reduce(ObjectObjectUnaryOperator<Object2DoubleMap.Entry<T>, Object2DoubleMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2DoubleMap.Entry<T> state = null;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = new ValueMapEntry(i);
					continue;
				}
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<Object2DoubleMap.Entry<T>> findFirst(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return Optional.ofNullable(entry);
				}
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<Object2DoubleMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Reference2DoubleHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2DoubleHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					int index = Reference2DoubleHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(Reference2DoubleHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Reference2DoubleHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Double.valueOf(Reference2DoubleHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					return Reference2DoubleHashMap.this.remove(entry.getKey(), entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Reference2DoubleHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> {
		@Override
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Reference2DoubleHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Reference2DoubleHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2DoubleHashMap.this.clear();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(keys[i].get());
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, keys[i].get());
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, keys[i].get());
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && !filter.test(keys[i].get())) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.apply(state, keys[i].get());
			}
			return state;
		}
		
		@Override
		public Optional<T> reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = keys[i].get();
					continue;
				}
				state = operator.apply(state, keys[i].get());
			}
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<T> findFirst(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return Optional.ofNullable(keys[i].get());
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) result++;
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
			return Reference2DoubleHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2DoubleHashMap.this.clear();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntDoubleConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.applyAsDouble(state, values[i]);
			}
			return state;
		}
		
		@Override
		public OptionalDouble reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsDouble(state, values[i]);
			}
			return empty ? OptionalDouble.empty() : OptionalDouble.of(state);
		}
		
		@Override
		public OptionalDouble findFirst(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return OptionalDouble.empty();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return OptionalDouble.of(values[i]);
			}
			return OptionalDouble.empty();
		}
		
		@Override
		public int count(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		@Override
		public Object2DoubleMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry;
		@Override
		public Object2DoubleMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()].get();
		}
	}
	
	private class ValueIterator extends MapIterator implements DoubleIterator {
		@Override
		public double nextDouble() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = capacity;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		WeakKey<T>[] wrapped = null;
		int wrappedIndex = 0;
		
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				while(true) {
					if(--pos < 0) {
						if(wrapped == null || wrappedIndex <= -pos - 1) break;
						nextIndex = -pos - 1;
						break;
					}
					if(keys[pos] != null){
						nextIndex = pos;
						break;
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				int value = wrapped[nextIndex].index();
				if(value < 0) throw new IllegalStateException("Entry ["+nextIndex+"] was removed during Iteration");
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			int value = (lastReturned = nextIndex);
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Reference2DoubleHashMap.this.remove(wrapped[-returnedPos - 1].get());
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			WeakKey<T> current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == null) {
						keys[last] = null;
						values[last] = 0D;
						return;
					}
					slot = HashUtil.mix(current.hash()) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) addWrapper(keys[startPos]);
				keys[last] = current.index(last);
				values[last] = values[startPos];
			}
		}
		
		private void addWrapper(WeakKey<T> value) {
			if(wrapped == null) wrapped = new WeakKey[2];
			else if(wrappedIndex >= wrapped.length) {
				WeakKey<T>[] newArray = new WeakKey[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}