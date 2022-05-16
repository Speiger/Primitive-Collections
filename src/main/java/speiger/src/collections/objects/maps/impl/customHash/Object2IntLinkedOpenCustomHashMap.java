package speiger.src.collections.objects.maps.impl.customHash;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.utils.HashUtil;

/**
 * A Type Specific LinkedHashMap that allows for custom HashControl. That uses arrays to create links between nodes.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 * This implementation of SortedMap does not support SubMaps of any kind. It implements the interface due to sortability and first/last access
 * @param <T> the type of elements maintained by this Collection
 */
public class Object2IntLinkedOpenCustomHashMap<T> extends Object2IntOpenCustomHashMap<T> implements Object2IntOrderedMap<T>
{
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Default Constructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2IntLinkedOpenCustomHashMap(ObjectStrategy<? super T> strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2IntLinkedOpenCustomHashMap(int minCapacity, ObjectStrategy<? super T> strategy) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2IntLinkedOpenCustomHashMap(int minCapacity, float loadFactor, ObjectStrategy<? super T> strategy) {
		super(minCapacity, loadFactor, strategy);
		links = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2IntLinkedOpenCustomHashMap(T[] keys, Integer[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2IntLinkedOpenCustomHashMap(T[] keys, Integer[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2IntLinkedOpenCustomHashMap(T[] keys, int[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2IntLinkedOpenCustomHashMap(T[] keys, int[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2IntLinkedOpenCustomHashMap(Map<? extends T, ? extends Integer> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2IntLinkedOpenCustomHashMap(Map<? extends T, ? extends Integer> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
 	 */
	public Object2IntLinkedOpenCustomHashMap(Object2IntMap<T> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Object2IntLinkedOpenCustomHashMap(Object2IntMap<T> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public int putAndMoveToFirst(T key, int value) {
		if(strategy.equals(key, null)) {
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
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], null)) {
				if(strategy.equals(keys[pos], key)) {
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
	public int putAndMoveToLast(T key, int value) {
		if(strategy.equals(key, null)) {
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
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], null)) {
				if(strategy.equals(keys[pos], key)) {
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
	public boolean moveToFirst(T key) {
		if(strategy.equals(firstKey(), key)) return false;
		if(strategy.equals(key, null)) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], null)) {
				if(strategy.equals(keys[pos], key)) {
					moveToFirstIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(T key) {
		if(strategy.equals(lastKey(), key)) return false;
		if(strategy.equals(key, null)) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], null)) {
				if(strategy.equals(keys[pos], key)) {
					moveToLastIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public int getAndMoveToFirst(T key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public int getAndMoveToLast(T key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
	}
	
	@Override
	public Object2IntLinkedOpenCustomHashMap<T> copy() {
		Object2IntLinkedOpenCustomHashMap<T> map = new Object2IntLinkedOpenCustomHashMap<>(0, loadFactor, strategy);
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
	public T firstKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public T pollFirstKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		T result = keys[pos];
		size--;
		if(strategy.equals(result, null)) {
			containsNull = false;
			keys[nullIndex] = null;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public T lastKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public T pollLastKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		T result = keys[pos];
		size--;
		if(strategy.equals(result, null)) {
			containsNull = false;
			keys[nullIndex] = null;
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
	public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectIntConsumer<T> action) {
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
		keys = (T[])new Object[request + 1];
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
		T[] newKeys = (T[])new Object[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(strategy.equals(keys[i], null)) pos = newSize;
			else {
				pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask;
				while(!strategy.equals(newKeys[pos], null)) pos = ++pos & newMask;
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
	
	private class MapEntrySet extends AbstractObjectSet<Object2IntMap.Entry<T>> implements Object2IntOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2IntMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2IntMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2IntMap.Entry<T> o) {
			return Object2IntLinkedOpenCustomHashMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2IntMap.Entry<T> o) {
			return Object2IntLinkedOpenCustomHashMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2IntMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstIntValue());
		}
		
		@Override
		public Object2IntMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastIntValue());
		}
		
		@Override
		public Object2IntMap.Entry<T> pollFirst() {
			BasicEntry<T> entry = new BasicEntry<>(firstKey(), firstIntValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2IntMap.Entry<T> pollLast() {
			BasicEntry<T> entry = new BasicEntry<>(lastKey(), lastIntValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2IntMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2IntMap.Entry<T>> iterator(Object2IntMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2IntMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2IntMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2IntMap.Entry<T>> action) {
			int index = firstIndex;
			while(index != -1) {
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2IntMap.Entry<T>> action) {
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2IntMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Object2IntMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Object2IntMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Object2IntMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2IntMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Object2IntMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2IntMap.Entry<T>, Object2IntMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2IntMap.Entry<T> state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new BasicEntry<>(keys[index], values[index]);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Object2IntMap.Entry<T> findFirst(Object2BooleanFunction<Object2IntMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Object2IntMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<T> entry = new BasicEntry<>();
			int result = 0;
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
				if(o instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)o;
					int index = Object2IntLinkedOpenCustomHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getIntValue() == Object2IntLinkedOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2IntLinkedOpenCustomHashMap.this.findIndex((T)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Object2IntLinkedOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)o;
					return Object2IntLinkedOpenCustomHashMap.this.remove(entry.getKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2IntLinkedOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Object2IntLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2IntLinkedOpenCustomHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2IntLinkedOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(T o) {
			return Object2IntLinkedOpenCustomHashMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(T o) {
			return Object2IntLinkedOpenCustomHashMap.this.moveToLast(o);
		}
		
		@Override
		public ObjectListIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Object2IntLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2IntLinkedOpenCustomHashMap.this.clear();
		}
		
		@Override
		public T first() {
			return firstKey();
		}
		
		@Override
		public T pollFirst() {
			return pollFirstKey();
		}

		@Override
		public T last() {
			return lastKey();
		}

		@Override
		public T pollLast() {
			return pollLastKey();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.getBoolean(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) result++;
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
			return Object2IntLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2IntLinkedOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(IntConsumer action) {
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
			while(index != -1){
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
			while(index != -1){
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2IntMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2IntMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2IntMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2IntMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2IntMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2IntMap.Entry<T>> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2IntMap.Entry<T> next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Object2IntMap.Entry<T> previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Object2IntMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2IntMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ObjectListIterator<T> {
		
		public KeyIterator() {}
		public KeyIterator(T from) {
			super(from);
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
		
		MapIterator(T from) {
			if(strategy.equals(from, null)) {
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
				int pos = HashUtil.mix(strategy.hashCode(from)) & mask;
				while(!strategy.equals(keys[pos], null)) {
					if(strategy.equals(keys[pos], from)) {
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
			
			if (next == -1) lastIndex = previous;
			else links[next] ^= ((links[next] ^ ((previous & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			if(current == nullIndex) {
				current = -1;
				containsNull = false;
				keys[nullIndex] = null;
				values[nullIndex] = 0;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				T current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(strategy.equals((current = keys[startPos]), null)) {
							keys[last] = null;
							values[last] = 0;
							return;
						}
						slot = HashUtil.mix(strategy.hashCode(current)) & mask;
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