package speiger.src.collections.longs.maps.impl.customHash;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.consumer.LongObjectConsumer;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectOrderedMap;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;

import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;

/**
 * A Type Specific LinkedHashMap that allows for custom HashControl. That uses arrays to create links between nodes.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 * This implementation of SortedMap does not support SubMaps of any kind. It implements the interface due to sortability and first/last access
 * @param <V> the type of elements maintained by this Collection 
 */
public class Long2ObjectLinkedOpenCustomHashMap<V> extends Long2ObjectOpenCustomHashMap<V> implements Long2ObjectOrderedMap<V>
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
	public Long2ObjectLinkedOpenCustomHashMap(LongStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Long2ObjectLinkedOpenCustomHashMap(int minCapacity, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(int minCapacity, float loadFactor, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(Long[] keys, V[] values, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(Long[] keys, V[] values, float loadFactor, LongStrategy strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].longValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ObjectLinkedOpenCustomHashMap(long[] keys, V[] values, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(long[] keys, V[] values, float loadFactor, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(Map<? extends Long, ? extends V> map, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(Map<? extends Long, ? extends V> map, float loadFactor, LongStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
 	 */
	public Long2ObjectLinkedOpenCustomHashMap(Long2ObjectMap<V> map, LongStrategy strategy) {
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
	public Long2ObjectLinkedOpenCustomHashMap(Long2ObjectMap<V> map, float loadFactor, LongStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public V putAndMoveToFirst(long key, V value) {
		if(strategy.equals(key, 0L)) {
			if(containsNull) {
				V lastValue = values[nullIndex];
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
			while(!strategy.equals(keys[pos], 0L)) {
				if(strategy.equals(keys[pos], key)) {
					V lastValue = values[pos];
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
	public V putAndMoveToLast(long key, V value) {
		if(strategy.equals(key, 0L)) {
			if(containsNull) {
				V lastValue = values[nullIndex];
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
			while(!strategy.equals(keys[pos], 0L)) {
				if(strategy.equals(keys[pos], key)) {
					V lastValue = values[pos];
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
	public boolean moveToFirst(long key) {
		if(strategy.equals(firstLongKey(), key)) return false;
		if(strategy.equals(key, 0L)) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], 0L)) {
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
	public boolean moveToLast(long key) {
		if(strategy.equals(lastLongKey(), key)) return false;
		if(strategy.equals(key, 0L)) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
			while(!strategy.equals(keys[pos], 0L)) {
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
	public V getAndMoveToFirst(long key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public V getAndMoveToLast(long key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
	}
	
	@Override
	public Long2ObjectLinkedOpenCustomHashMap<V> copy() {
		Long2ObjectLinkedOpenCustomHashMap<V> map = new Long2ObjectLinkedOpenCustomHashMap<>(0, loadFactor, strategy);
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
	public long firstLongKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public long pollFirstLongKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		long result = keys[pos];
		size--;
		if(strategy.equals(result, 0L)) {
			containsNull = false;
			keys[nullIndex] = 0L;
			values[nullIndex] = null;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public long lastLongKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public long pollLastLongKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		long result = keys[pos];
		size--;
		if(strategy.equals(result, 0L)) {
			containsNull = false;
			keys[nullIndex] = 0L;
			values[nullIndex] = null;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public V firstValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public V lastValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}
	
	@Override
	public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public LongSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(LongObjectConsumer<V> action) {
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
		keys = new long[request + 1];
		values = (V[])new Object[request + 1];
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
		long[] newKeys = new long[newSize + 1];
		V[] newValues = (V[])new Object[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(strategy.equals(keys[i], 0L)) pos = newSize;
			else {
				pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask;
				while(!strategy.equals(newKeys[pos], 0L)) pos = ++pos & newMask;
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
	
	private class MapEntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> implements Long2ObjectOrderedMap.FastOrderedSet<V> {
		@Override
		public boolean addAndMoveToFirst(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Long2ObjectMap.Entry<V> o) {
			return Long2ObjectLinkedOpenCustomHashMap.this.moveToFirst(o.getLongKey());
		}
		
		@Override
		public boolean moveToLast(Long2ObjectMap.Entry<V> o) {
			return Long2ObjectLinkedOpenCustomHashMap.this.moveToLast(o.getLongKey());
		}
		
		@Override
		public Long2ObjectMap.Entry<V> first() {
			return new BasicEntry<>(firstLongKey(), firstValue());
		}
		
		@Override
		public Long2ObjectMap.Entry<V> last() {
			return new BasicEntry<>(lastLongKey(), lastValue());
		}
		
		@Override
		public Long2ObjectMap.Entry<V> pollFirst() {
			BasicEntry<V> entry = new BasicEntry<>(firstLongKey(), firstValue());
			pollFirstLongKey();
			return entry;
		}
		
		@Override
		public Long2ObjectMap.Entry<V> pollLast() {
			BasicEntry<V> entry = new BasicEntry<>(lastLongKey(), lastValue());
			pollLastLongKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> iterator(Long2ObjectMap.Entry<V> fromElement) {
			return new EntryIterator(fromElement.getLongKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(long fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
			int index = firstIndex;
			while(index != -1) {
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Long2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Long2ObjectMap.Entry<V>, E> operator) {
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
		public Long2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Long2ObjectMap.Entry<V>, Long2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Long2ObjectMap.Entry<V> state = null;
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
		public Long2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<V> entry = new BasicEntry<>();
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
				if(o instanceof Long2ObjectMap.Entry) {
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>)o;
					int index = Long2ObjectLinkedOpenCustomHashMap.this.findIndex(entry.getLongKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Long2ObjectLinkedOpenCustomHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!(entry.getKey() instanceof Long)) return false;
					int index = Long2ObjectLinkedOpenCustomHashMap.this.findIndex((Long)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Long2ObjectLinkedOpenCustomHashMap.this.values[index]);
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Long2ObjectMap.Entry) {
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>)o;
					return Long2ObjectLinkedOpenCustomHashMap.this.remove(entry.getLongKey(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Long2ObjectLinkedOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Long2ObjectLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ObjectLinkedOpenCustomHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractLongSet implements LongOrderedSet {
		@Override
		public boolean contains(long e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(long o) {
			int oldSize = size;
			Long2ObjectLinkedOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(long o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(long o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(long o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(long o) {
			return Long2ObjectLinkedOpenCustomHashMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(long o) {
			return Long2ObjectLinkedOpenCustomHashMap.this.moveToLast(o);
		}
		
		@Override
		public LongListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public LongBidirectionalIterator iterator(long fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Long2ObjectLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ObjectLinkedOpenCustomHashMap.this.clear();
		}
		
		@Override
		public long firstLong() {
			return firstLongKey();
		}
		
		@Override
		public long pollFirstLong() {
			return pollFirstLongKey();
		}

		@Override
		public long lastLong() {
			return lastLongKey();
		}

		@Override
		public long pollLastLong() {
			return pollLastLongKey();
		}
		
		@Override
		public void forEach(LongConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
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
		public boolean matchesNone(Long2BooleanFunction filter) {
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
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.get(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsLong(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsLong(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
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
	
	private class Values extends AbstractObjectCollection<V> {
		@Override
		@Deprecated
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
			return Long2ObjectLinkedOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ObjectLinkedOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super V> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.getBoolean(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
			Objects.requireNonNull(operator);
			V state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public V findFirst(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return values[index];
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Long2ObjectMap.Entry<V>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(long from) {
			super(from);
		}
		
		@Override
		public Long2ObjectMap.Entry<V> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Long2ObjectMap.Entry<V> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Long2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Long2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Long2ObjectMap.Entry<V>> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(long from) {
			super(from);
		}
		
		@Override
		public Long2ObjectMap.Entry<V> next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Long2ObjectMap.Entry<V> previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Long2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Long2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements LongListIterator {
		
		public KeyIterator() {}
		public KeyIterator(long from) {
			super(from);
		}
		
		@Override
		public long previousLong() {
			return keys[previousEntry()];
		}

		@Override
		public long nextLong() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(long e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(long e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements ObjectListIterator<V> {
		public ValueIterator() {}
		
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
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(long from) {
			if(strategy.equals(from, 0L)) {
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
				while(!strategy.equals(keys[pos], 0L)) {
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
				keys[nullIndex] = 0L;
				values[nullIndex] = null;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				long current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(strategy.equals((current = keys[startPos]), 0L)) {
							keys[last] = 0L;
							values[last] = null;
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