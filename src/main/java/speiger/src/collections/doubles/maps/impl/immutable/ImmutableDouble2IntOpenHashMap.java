package speiger.src.collections.doubles.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.doubles.functions.consumer.DoubleIntConsumer;
import speiger.src.collections.doubles.functions.function.Double2IntFunction;
import speiger.src.collections.doubles.functions.function.DoubleIntUnaryOperator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.maps.interfaces.Double2IntOrderedMap;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;

import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableDouble2IntOpenHashMap extends AbstractDouble2IntMap implements Double2IntOrderedMap
{
	/** The Backing keys array */
	protected transient double[] keys;
	/** The Backing values array */
	protected transient int[] values;
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	/** EntrySet cache */
	protected transient FastOrderedSet entrySet;
	/** KeySet cache */
	protected transient DoubleOrderedSet keySet;
	/** Values cache */
	protected transient IntCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableDouble2IntOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableDouble2IntOpenHashMap(Double[] keys, Integer[] values) {
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
	public ImmutableDouble2IntOpenHashMap(Double[] keys, Integer[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(DoubleArrays.unwrap(keys), IntArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableDouble2IntOpenHashMap(double[] keys, int[] values) {
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
	public ImmutableDouble2IntOpenHashMap(double[] keys, int[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableDouble2IntOpenHashMap(Map<? extends Double, ? extends Integer> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableDouble2IntOpenHashMap(Map<? extends Double, ? extends Integer> map, float loadFactor) {
		double[] keys = new double[map.size()];
		int[] values = new int[keys.length];
		int index = 0;
		for(Map.Entry<? extends Double, ? extends Integer> entry : map.entrySet()) {
			keys[index] = entry.getKey().doubleValue();
			values[index] = entry.getValue().intValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableDouble2IntOpenHashMap(Double2IntMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableDouble2IntOpenHashMap(Double2IntMap map, float loadFactor) {
		double[] keys = new double[map.size()];
		int[] values = new int[keys.length];
		int index = 0;
		for(Double2IntMap.Entry entry : getFastIterable(map)) {
			keys[index] = entry.getDoubleKey();
			values[index] = entry.getIntValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(double[] a, int[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		double[] newKeys = new double[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			double o = a[i];
			if(Double.doubleToLongBits(o) == 0) {
				if(!containsNull) {
					size++;
					if(prev != -1) {
						newLinks[prev] ^= ((newLinks[prev] ^ (newSize & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						newLinks[newSize] ^= ((newLinks[newSize] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
						prev = newSize;
					}
					else {
						prev = firstIndex = newSize;
						newLinks[newSize] = -1L;
					}
				}
				containsNull = true;
				newValues[newSize] = b[i];
				continue;
			}
			boolean found = true;
			int pos = HashUtil.mix(Double.hashCode(o)) & newMask;
			double current = newKeys[pos];
			if(Double.doubleToLongBits(current) != 0) {
				if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) {
					newValues[pos] = b[i];
					continue;
				}
				while(Double.doubleToLongBits((current = newKeys[pos = (++pos & newMask)])) != 0) {
					if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) {
						found = false;
						newValues[pos] = b[i];
						break;
					}
				}
			}
			if(found) {
				size++;
				newKeys[pos] = o;
				newValues[pos] = b[i];
				if(prev != -1) {
					newLinks[prev] ^= ((newLinks[prev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
					newLinks[pos] ^= ((newLinks[pos] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
					prev = pos;
				}
				else {
					prev = firstIndex = pos;
					newLinks[pos] = -1L;
				}
			}
		}
		nullIndex = newSize;
		mask = newMask;
		keys = newKeys;
		values = newValues;
		links = newLinks;
		lastIndex = prev;
		if(prev != -1) newLinks[prev] |= 0xFFFFFFFFL;
	}
	
	@Override
	public int put(double key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int putIfAbsent(double key, int value) { throw new UnsupportedOperationException(); }	
	@Override
	public int addTo(double key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int subFrom(double key, int value) { throw new UnsupportedOperationException(); }	
	@Override
	public int putAndMoveToFirst(double key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int putAndMoveToLast(double key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(double key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(double key) { throw new UnsupportedOperationException(); }
	@Override
	public int getAndMoveToFirst(double key) { throw new UnsupportedOperationException(); }
	@Override
	public int getAndMoveToLast(double key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(double key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(int value) {
		int index = firstIndex;
		while(index != -1) {
			if(values[index] == value) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		int index = firstIndex;
		while(index != -1) {
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Integer.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public int remove(double key) { throw new UnsupportedOperationException(); }
	
	@Override
	public int removeOrDefault(double key, int defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Integer remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(double key, int value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public int get(double key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Integer get(Object key) {
		int slot = findIndex(key);
		return Integer.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public int getOrDefault(double key, int defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public double firstDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public double pollFirstDoubleKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public double lastDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public double pollLastDoubleKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public int firstIntValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public int lastIntValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectOrderedSet<Double2IntMap.Entry> double2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public DoubleOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableDouble2IntOpenHashMap copy() {
		ImmutableDouble2IntOpenHashMap map = new ImmutableDouble2IntOpenHashMap();
		map.mask = mask;
		map.nullIndex = nullIndex;
		map.containsNull = containsNull;
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, values.length);
		map.links = Arrays.copyOf(links, links.length);
		map.firstIndex = firstIndex;
		map.lastIndex = lastIndex;
		return map;
	}
	
	@Override
	public void forEach(DoubleIntConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(double key, int oldValue, int newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public int replace(double key, int value) { throw new UnsupportedOperationException(); }
	
	@Override
	public int computeInt(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfAbsent(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int supplyIntIfAbsent(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfPresent(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfAbsentNonDefault(double key, Double2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int supplyIntIfAbsentNonDefault(double key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfPresentNonDefault(double key, DoubleIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int mergeInt(double key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllInt(Double2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(double key) {
		if(Double.doubleToLongBits(key) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Double.hashCode(key)) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) != 0) {
			if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key)) return pos;
			while(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		if(Double.doubleToLongBits(((Double)key).doubleValue()) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) != 0) {
			if(Objects.equals(key, Double.valueOf(current))) return pos;
			while(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Objects.equals(key, Double.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Double2IntMap.Entry, Map.Entry<Double, Integer> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public double getDoubleKey() {
			return keys[index];
		}

		@Override
		public int getIntValue() {
			return values[index];
		}

		@Override
		public int setValue(int value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Double2IntMap.Entry) {
					Double2IntMap.Entry entry = (Double2IntMap.Entry)obj;
					return Double.doubleToLongBits(keys[index]) == Double.doubleToLongBits(entry.getDoubleKey()) && values[index] == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Double)) return false;
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Integer && Double.doubleToLongBits(keys[index]) == Double.doubleToLongBits(((Double)key).doubleValue()) && values[index] == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(keys[index]) ^ Integer.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Double.toString(keys[index]) + "=" + Integer.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Double2IntMap.Entry> implements Double2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Double2IntMap.Entry first() {
			return new BasicEntry(firstDoubleKey(), firstIntValue());
		}
		
		@Override
		public Double2IntMap.Entry last() {
			return new BasicEntry(lastDoubleKey(), lastIntValue());
		}
		
		@Override
		public Double2IntMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Double2IntMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator(Double2IntMap.Entry fromElement) {
			return new EntryIterator(fromElement.getDoubleKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> fastIterator(double fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Double2IntMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Double2IntMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Double2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1) {
				action.accept(count++, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Double2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Double2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Double2IntMap.Entry reduce(ObjectObjectUnaryOperator<Double2IntMap.Entry, Double2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Double2IntMap.Entry state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new BasicEntry(keys[index], values[index]);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Double2IntMap.Entry findFirst(Predicate<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) result++;
				index = (int)links[index];
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Double2IntMap.Entry) {
					Double2IntMap.Entry entry = (Double2IntMap.Entry)o;
					int index = ImmutableDouble2IntOpenHashMap.this.findIndex(entry.getDoubleKey());
					if(index >= 0) return entry.getIntValue() == ImmutableDouble2IntOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableDouble2IntOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(ImmutableDouble2IntOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableDouble2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractDoubleSet implements DoubleOrderedSet {
		@Override
		public boolean contains(double e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(double o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(double o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(double o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(double o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(double o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(double o) { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableDouble2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public double firstDouble() {
			return firstDoubleKey();
		}
		
		@Override
		public double pollFirstDouble() { throw new UnsupportedOperationException(); }

		@Override
		public double lastDouble() {
			return lastDoubleKey();
		}

		@Override
		public double pollLastDouble() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(DoubleConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntDoubleConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1){
				action.accept(count++, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.test(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsDouble(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsDouble(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public double findFirst(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0D;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0D;
		}
		
		@Override
		public int count(DoublePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.test(keys[index])) result++;
				index = (int)links[index];
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
			return ImmutableDouble2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(IntConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1){
				action.accept(count++, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.test(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsInt(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsInt(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public int findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return values[index];
				index = (int)links[index];
			}
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Double2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(double from) {
			super(from);
		}
		
		@Override
		public Double2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Double2IntMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Double2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Double2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Double2IntMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(double from) {
			super(from);
		}
		
		@Override
		public Double2IntMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Double2IntMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Double2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Double2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements DoubleListIterator {
		
		public KeyIterator() {}
		public KeyIterator(double from) {
			super(from);
		}
		
		@Override
		public double previousDouble() {
			return keys[previousEntry()];
		}

		@Override
		public double nextDouble() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(double e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements IntListIterator {
		public ValueIterator() {}
		
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
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(double from) {
			if(Double.doubleToLongBits(from) == 0) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(keys[lastIndex] == from) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Double.hashCode(from)) & mask;
				while(Double.doubleToLongBits(keys[pos]) != 0) {
					if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(from)) {
						next = (int)links[pos];
						previous = pos;
						break;
					}
					pos = ++pos & mask;
				}
				if(previous == -1 && next == -1)
					throw new NoSuchElementException("The element was not found");
			}
		}
		
		public boolean hasNext() {
			return next != -1;
		}

		public boolean hasPrevious() {
			return previous != -1;
		}
		
		public int nextIndex() {
			ensureIndexKnown();
			return index;
		}
		
		public int previousIndex() {
			ensureIndexKnown();
			return index - 1;
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return current;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			current = next;
			next = (int)(links[current]);
			previous = current;
			if(index >= 0) index++;
			return current;
		}
		
		private void ensureIndexKnown() {
			if(index == -1) {
				if(previous == -1) {
					index = 0;
				}
				else if(next == -1) {
					index = size;
				}
				else {
					index = 1;
					for(int pos = firstIndex;pos != previous;pos = (int)links[pos], index++);
				}
			}
		}
	}
}