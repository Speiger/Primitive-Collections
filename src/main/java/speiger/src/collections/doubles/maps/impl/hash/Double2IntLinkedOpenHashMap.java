package speiger.src.collections.doubles.maps.impl.hash;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.consumer.DoubleIntConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntOrderedMap;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;

/**
 * A Type Specific LinkedHashMap implementation that uses specific arrays to create links between nodes to remove the wrapping of elements
 * to greatly reduce memory usage. In Addition adding some helper methods to move around elements.
 * This implementation of SortedMap does not support SubMaps of any kind. It implements the interface due to sortability and first/last access
 */
public class Double2IntLinkedOpenHashMap extends Double2IntOpenHashMap implements Double2IntOrderedMap
{
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Default Constructor
	 */
	public Double2IntLinkedOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Double2IntLinkedOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2IntLinkedOpenHashMap(int minCapacity, float loadFactor) {
		super(minCapacity, loadFactor);
		links = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2IntLinkedOpenHashMap(Double[] keys, Integer[] values) {
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
	public Double2IntLinkedOpenHashMap(Double[] keys, Integer[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].doubleValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2IntLinkedOpenHashMap(double[] keys, int[] values) {
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
	public Double2IntLinkedOpenHashMap(double[] keys, int[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Double2IntLinkedOpenHashMap(Map<? extends Double, ? extends Integer> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2IntLinkedOpenHashMap(Map<? extends Double, ? extends Integer> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Double2IntLinkedOpenHashMap(Double2IntMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Double2IntLinkedOpenHashMap(Double2IntMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public int putAndMoveToFirst(double key, int value) {
		if(Double.doubleToLongBits(key) == 0) {
			if(containsNull) {
				int lastValue = values[nullIndex];
				values[nullIndex] = value;
				moveToFirstIndex(nullIndex);
				return lastValue;
			}
			values[nullIndex] = value;
			containsNull = true;
			onNodeAdded(nullIndex);
			moveToFirstIndex(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Double.hashCode(key)) & mask;
			while(Double.doubleToLongBits(keys[pos]) != 0) {
				if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(key)) {
					int lastValue = values[pos];
					values[pos] = value;
					moveToFirstIndex(pos);
					return lastValue;
				}
				pos = ++pos & mask;
			}
			keys[pos] = key;
			values[pos] = value;
			onNodeAdded(pos);
			moveToFirstIndex(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return getDefaultReturnValue();
	}
	
	@Override
	public int putAndMoveToLast(double key, int value) {
		if(Double.doubleToLongBits(key) == 0) {
			if(containsNull) {
				int lastValue = values[nullIndex];
				values[nullIndex] = value;
				moveToLastIndex(nullIndex);
				return lastValue;
			}
			values[nullIndex] = value;
			containsNull = true;
			onNodeAdded(nullIndex);
			moveToLastIndex(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Double.hashCode(key)) & mask;
			while(Double.doubleToLongBits(keys[pos]) != 0) {
				if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(key)) {
					int lastValue = values[pos];
					values[pos] = value;
					moveToLastIndex(pos);
					return lastValue;
				}
				pos = ++pos & mask;
			}
			keys[pos] = key;
			values[pos] = value;
			onNodeAdded(pos);
			moveToLastIndex(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean moveToFirst(double key) {
		if(Double.doubleToLongBits(firstDoubleKey()) == Double.doubleToLongBits(key)) return false;
		if(Double.doubleToLongBits(key) == 0) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Double.hashCode(key)) & mask;
			while(Double.doubleToLongBits(keys[pos]) != 0) {
				if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(key)) {
					moveToFirstIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(double key) {
		if(Double.doubleToLongBits(lastDoubleKey()) == Double.doubleToLongBits(key)) return false;
		if(Double.doubleToLongBits(key) == 0) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Double.hashCode(key)) & mask;
			while(Double.doubleToLongBits(keys[pos]) != 0) {
				if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(key)) {
					moveToLastIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public int getAndMoveToFirst(double key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public int getAndMoveToLast(double key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
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
	public Double2IntLinkedOpenHashMap copy() {
		Double2IntLinkedOpenHashMap map = new Double2IntLinkedOpenHashMap(0, loadFactor);
		map.minCapacity = minCapacity;
		map.mask = mask;
		map.maxFill = maxFill;
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
	public double firstDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public double pollFirstDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		double result = keys[pos];
		size--;
		if(Double.doubleToLongBits(result) == 0) {
			containsNull = false;
			keys[nullIndex] = 0D;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public double lastDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public double pollLastDoubleKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		double result = keys[pos];
		size--;
		if(Double.doubleToLongBits(result) == 0) {
			containsNull = false;
			keys[nullIndex] = 0D;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
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
	public ObjectSet<Double2IntMap.Entry> double2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public DoubleSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
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
	public void clear() {
		super.clear();
		firstIndex = lastIndex = -1;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= size) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new double[request + 1];
		values = new int[request + 1];
		links = new long[request + 1];
		firstIndex = lastIndex = -1;
		this.size = 0;
		containsNull = false;
	}
	
	protected void moveToFirstIndex(int startPos) {
		if(size == 1 || firstIndex == startPos) return;
		if(lastIndex == startPos) {
			lastIndex = (int)(links[startPos] >>> 32);
			links[lastIndex] |= 0xFFFFFFFFL;
		}
		else {
			long link = links[startPos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
		links[firstIndex] ^= ((links[firstIndex] ^ ((startPos & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
		links[startPos] = 0xFFFFFFFF00000000L | (firstIndex & 0xFFFFFFFFL);
		firstIndex = startPos;
	}
	
	protected void moveToLastIndex(int startPos) {
		if(size == 1 || lastIndex == startPos) return;
		if(firstIndex == startPos) {
			firstIndex = (int)links[startPos];
			links[lastIndex] |= 0xFFFFFFFF00000000L;
		}
		else {
			long link = links[startPos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
		links[lastIndex] ^= ((links[lastIndex] ^ (startPos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
		links[startPos] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
		lastIndex = startPos;
	}
	
	@Override
	protected void onNodeAdded(int pos) {
		if(size == 0) {
			firstIndex = lastIndex = pos;
			links[pos] = -1L;
		}
		else {
			links[lastIndex] ^= ((links[lastIndex] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[pos] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
			lastIndex = pos;
		}
	}
	
	@Override
	protected void onNodeRemoved(int pos) {
		if(size == 0) firstIndex = lastIndex = -1;
		else if(firstIndex == pos) {
			firstIndex = (int)links[pos];
			if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		}
		else if(lastIndex == pos) {
			lastIndex = (int)(links[pos] >>> 32);
			if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		}
		else {
			long link = links[pos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
	}
	
	@Override
	protected void onNodeMoved(int from, int to) {
		if(size == 1) {
			firstIndex = lastIndex = to;
			links[to] = -1L;
		}
		else if(firstIndex == from) {
			firstIndex = to;
			links[(int)links[from]] ^= ((links[(int)links[from]] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			links[to] = links[from];
		}
		else if(lastIndex == from) {
			lastIndex = to;
			links[(int)(links[from] >>> 32)] ^= ((links[(int)(links[from] >>> 32)] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[to] = links[from];
		}
		else {
			long link = links[from];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			links[to] = link;
		}
	}
	
	@Override
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		double[] newKeys = new double[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(Double.doubleToLongBits(keys[i]) == 0) pos = newSize;
			else {
				pos = HashUtil.mix(Double.hashCode(keys[i])) & newMask;
				while(Double.doubleToLongBits(newKeys[pos]) != 0) pos = ++pos & newMask;
			}
			newKeys[pos] = keys[i];
			newValues[pos] = values[i];
			if(prev != -1) {
				newLinks[newPrev] ^= ((newLinks[newPrev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				newLinks[pos] ^= ((newLinks[pos] ^ ((newPrev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
				newPrev = pos;
			}
			else {
				newPrev = firstIndex = pos;
				newLinks[pos] = -1L;
			}
			i = (int)links[prev = i];
		}
		links = newLinks;
		lastIndex = newPrev;
		if(newPrev != -1) newLinks[newPrev] |= 0xFFFFFFFFL;
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
		values = newValues;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Double2IntMap.Entry> implements Double2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Double2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Double2IntMap.Entry o) {
			return Double2IntLinkedOpenHashMap.this.moveToFirst(o.getDoubleKey());
		}
		
		@Override
		public boolean moveToLast(Double2IntMap.Entry o) {
			return Double2IntLinkedOpenHashMap.this.moveToLast(o.getDoubleKey());
		}
		
		@Override
		public Double2IntMap.Entry first() {
			return new BasicEntry(firstDoubleKey(), firstIntValue());
		}
		
		@Override
		public Double2IntMap.Entry last() {
			return new BasicEntry(lastDoubleKey(), lastIntValue());
		}
		
		@Override
		public Double2IntMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstDoubleKey(), firstIntValue());
			pollFirstDoubleKey();
			return entry;
		}
		
		@Override
		public Double2IntMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastDoubleKey(), lastIntValue());
			pollLastDoubleKey();
			return entry;
		}
		
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
		public boolean matchesAny(Object2BooleanFunction<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
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
		public Double2IntMap.Entry findFirst(Object2BooleanFunction<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Double2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) result++;
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
					int index = Double2IntLinkedOpenHashMap.this.findIndex(entry.getDoubleKey());
					if(index >= 0) return entry.getIntValue() == Double2IntLinkedOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Double2IntLinkedOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Double2IntLinkedOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Double2IntMap.Entry) {
					Double2IntMap.Entry entry = (Double2IntMap.Entry)o;
					return Double2IntLinkedOpenHashMap.this.remove(entry.getDoubleKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Double2IntLinkedOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Double2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2IntLinkedOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractDoubleSet implements DoubleOrderedSet {
		@Override
		public boolean contains(double e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(double o) {
			int oldSize = size;
			Double2IntLinkedOpenHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAndMoveToFirst(double o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(double o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(double o) {
			return Double2IntLinkedOpenHashMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(double o) {
			return Double2IntLinkedOpenHashMap.this.moveToLast(o);
		}
		
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
			return Double2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2IntLinkedOpenHashMap.this.clear();
		}
		
		@Override
		public double firstDouble() {
			return firstDoubleKey();
		}
		
		@Override
		public double pollFirstDouble() {
			return pollFirstDoubleKey();
		}

		@Override
		public double lastDouble() {
			return lastDoubleKey();
		}

		@Override
		public double pollLastDouble() {
			return pollLastDoubleKey();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
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
		public boolean matchesAny(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1) {
				if(!filter.get(keys[index])) return false;
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
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0D;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) result++;
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
			return Double2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2IntLinkedOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
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
		public boolean matchesAny(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1) {
				if(filter.get(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1) {
				if(!filter.get(values[index])) return false;
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
		public int findFirst(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return values[index];
				index = (int)links[index];
			}
			return 0;
		}
		
		@Override
		public int count(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) result++;
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
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(double from) {
			super(from);
		}
		
		@Override
		public Double2IntMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Double2IntMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

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
		
		public void remove() {
			if(current == -1) throw new IllegalStateException();
			ensureIndexKnown();
			if(current == previous) {
				index--;
				previous = (int)(links[current] >>> 32);
			}
			else next = (int)links[current];
			size--;
			if(previous == -1) firstIndex = next;
			else links[previous] ^= ((links[previous] ^ (next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			
			if(next == -1) lastIndex = previous;
			else links[next] ^= ((links[next] ^ ((previous & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			if(current == nullIndex) {
				current = -1;
				containsNull = false;
				keys[nullIndex] = 0D;
				values[nullIndex] = 0;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				double current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(Double.doubleToLongBits((current = keys[startPos])) == 0) {
							keys[last] = 0D;
							values[last] = 0;
							return;
						}
						slot = HashUtil.mix(Double.hashCode(current)) & mask;
						if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
						startPos = ++startPos & mask;
					}
					keys[last] = current;
					values[last] = values[startPos];
					if(next == startPos) next = last;
					if(previous == startPos) previous = last;
					onNodeMoved(startPos, last);
				}
			}
		}
		
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