package speiger.src.collections.floats.maps.impl.hash;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.functions.consumer.FloatIntConsumer;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntOrderedMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
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
public class Float2IntLinkedOpenHashMap extends Float2IntOpenHashMap implements Float2IntOrderedMap
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
	public Float2IntLinkedOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Float2IntLinkedOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2IntLinkedOpenHashMap(int minCapacity, float loadFactor) {
		super(minCapacity, loadFactor);
		links = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2IntLinkedOpenHashMap(Float[] keys, Integer[] values) {
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
	public Float2IntLinkedOpenHashMap(Float[] keys, Integer[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2IntLinkedOpenHashMap(float[] keys, int[] values) {
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
	public Float2IntLinkedOpenHashMap(float[] keys, int[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2IntLinkedOpenHashMap(Map<? extends Float, ? extends Integer> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Float2IntLinkedOpenHashMap(Map<? extends Float, ? extends Integer> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2IntLinkedOpenHashMap(Float2IntMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Float2IntLinkedOpenHashMap(Float2IntMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public int putAndMoveToFirst(float key, int value) {
		if(Float.floatToIntBits(key) == 0) {
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
			int pos = HashUtil.mix(Float.hashCode(key)) & mask;
			while(Float.floatToIntBits(keys[pos]) != 0) {
				if(Float.floatToIntBits(keys[pos]) == Float.floatToIntBits(key)) {
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
	public int putAndMoveToLast(float key, int value) {
		if(Float.floatToIntBits(key) == 0) {
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
			int pos = HashUtil.mix(Float.hashCode(key)) & mask;
			while(Float.floatToIntBits(keys[pos]) != 0) {
				if(Float.floatToIntBits(keys[pos]) == Float.floatToIntBits(key)) {
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
	public boolean moveToFirst(float key) {
		if(Float.floatToIntBits(firstFloatKey()) == Float.floatToIntBits(key)) return false;
		if(Float.floatToIntBits(key) == 0) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Float.hashCode(key)) & mask;
			while(Float.floatToIntBits(keys[pos]) != 0) {
				if(Float.floatToIntBits(keys[pos]) == Float.floatToIntBits(key)) {
					moveToFirstIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(float key) {
		if(Float.floatToIntBits(lastFloatKey()) == Float.floatToIntBits(key)) return false;
		if(Float.floatToIntBits(key) == 0) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Float.hashCode(key)) & mask;
			while(Float.floatToIntBits(keys[pos]) != 0) {
				if(Float.floatToIntBits(keys[pos]) == Float.floatToIntBits(key)) {
					moveToLastIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public int getAndMoveToFirst(float key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public int getAndMoveToLast(float key) {
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
	public Float2IntLinkedOpenHashMap copy() {
		Float2IntLinkedOpenHashMap map = new Float2IntLinkedOpenHashMap(0, loadFactor);
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
	public float firstFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public float pollFirstFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		float result = keys[pos];
		size--;
		if(Float.floatToIntBits(result) == 0) {
			containsNull = false;
			keys[nullIndex] = 0F;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public float lastFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public float pollLastFloatKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		float result = keys[pos];
		size--;
		if(Float.floatToIntBits(result) == 0) {
			containsNull = false;
			keys[nullIndex] = 0F;
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
	public ObjectSet<Float2IntMap.Entry> float2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public FloatSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(FloatIntConsumer action) {
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
		keys = new float[request + 1];
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
		float[] newKeys = new float[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(Float.floatToIntBits(keys[i]) == 0) pos = newSize;
			else {
				pos = HashUtil.mix(Float.hashCode(keys[i])) & newMask;
				while(Float.floatToIntBits(newKeys[pos]) != 0) pos = ++pos & newMask;
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
	
	private class MapEntrySet extends AbstractObjectSet<Float2IntMap.Entry> implements Float2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Float2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Float2IntMap.Entry o) {
			return Float2IntLinkedOpenHashMap.this.moveToFirst(o.getFloatKey());
		}
		
		@Override
		public boolean moveToLast(Float2IntMap.Entry o) {
			return Float2IntLinkedOpenHashMap.this.moveToLast(o.getFloatKey());
		}
		
		@Override
		public Float2IntMap.Entry first() {
			return new BasicEntry(firstFloatKey(), firstIntValue());
		}
		
		@Override
		public Float2IntMap.Entry last() {
			return new BasicEntry(lastFloatKey(), lastIntValue());
		}
		
		@Override
		public Float2IntMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstFloatKey(), firstIntValue());
			pollFirstFloatKey();
			return entry;
		}
		
		@Override
		public Float2IntMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastFloatKey(), lastIntValue());
			pollLastFloatKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> iterator(Float2IntMap.Entry fromElement) {
			return new EntryIterator(fromElement.getFloatKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Float2IntMap.Entry> fastIterator(float fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Float2IntMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Float2IntMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Float2IntMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Float2IntMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Float2IntMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Float2IntMap.Entry, E> operator) {
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
		public Float2IntMap.Entry reduce(ObjectObjectUnaryOperator<Float2IntMap.Entry, Float2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Float2IntMap.Entry state = null;
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
		public Float2IntMap.Entry findFirst(Object2BooleanFunction<Float2IntMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Float2IntMap.Entry> filter) {
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
				if(o instanceof Float2IntMap.Entry) {
					Float2IntMap.Entry entry = (Float2IntMap.Entry)o;
					int index = Float2IntLinkedOpenHashMap.this.findIndex(entry.getFloatKey());
					if(index >= 0) return entry.getIntValue() == Float2IntLinkedOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Float2IntLinkedOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Float2IntLinkedOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Float2IntMap.Entry) {
					Float2IntMap.Entry entry = (Float2IntMap.Entry)o;
					return Float2IntLinkedOpenHashMap.this.remove(entry.getFloatKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Float2IntLinkedOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Float2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntLinkedOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractFloatSet implements FloatOrderedSet {
		@Override
		public boolean contains(float e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(float o) {
			int oldSize = size;
			Float2IntLinkedOpenHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAndMoveToFirst(float o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(float o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(float o) {
			return Float2IntLinkedOpenHashMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(float o) {
			return Float2IntLinkedOpenHashMap.this.moveToLast(o);
		}
		
		@Override
		public FloatListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Float2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntLinkedOpenHashMap.this.clear();
		}
		
		@Override
		public float firstFloat() {
			return firstFloatKey();
		}
		
		@Override
		public float pollFirstFloat() {
			return pollFirstFloatKey();
		}

		@Override
		public float lastFloat() {
			return lastFloatKey();
		}

		@Override
		public float pollLastFloat() {
			return pollLastFloatKey();
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) {
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
		public boolean matchesNone(Float2BooleanFunction filter) {
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
		public boolean matchesAll(Float2BooleanFunction filter) {
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
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsFloat(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public float reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsFloat(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public float findFirst(Float2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0F;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0F;
		}
		
		@Override
		public int count(Float2BooleanFunction filter) {
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
			return Float2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Float2IntLinkedOpenHashMap.this.clear();
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Float2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(float from) {
			super(from);
		}
		
		@Override
		public Float2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Float2IntMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Float2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Float2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Float2IntMap.Entry> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(float from) {
			super(from);
		}
		
		@Override
		public Float2IntMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Float2IntMap.Entry previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Float2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Float2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements FloatListIterator {
		
		public KeyIterator() {}
		public KeyIterator(float from) {
			super(from);
		}
		
		@Override
		public float previousFloat() {
			return keys[previousEntry()];
		}

		@Override
		public float nextFloat() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(float e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(float e) { throw new UnsupportedOperationException(); }
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
		
		MapIterator(float from) {
			if(Float.floatToIntBits(from) == 0) {
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
				int pos = HashUtil.mix(Float.hashCode(from)) & mask;
				while(Float.floatToIntBits(keys[pos]) != 0) {
					if(Float.floatToIntBits(keys[pos]) == Float.floatToIntBits(from)) {
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
				keys[nullIndex] = 0F;
				values[nullIndex] = 0;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				float current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(Float.floatToIntBits((current = keys[startPos])) == 0) {
							keys[last] = 0F;
							values[last] = 0;
							return;
						}
						slot = HashUtil.mix(Float.hashCode(current)) & mask;
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