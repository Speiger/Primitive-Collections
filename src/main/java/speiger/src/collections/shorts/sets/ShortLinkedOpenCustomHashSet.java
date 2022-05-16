package speiger.src.collections.shorts.sets;

import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.utils.ShortIterators;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific LinkedHashSet that allows for custom HashControl. That uses arrays to create links between nodes.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 * This implementation of SortedSet does not support SubSet of any kind. It implements the interface due to sortability and first/last access
 */
public class ShortLinkedOpenCustomHashSet extends ShortOpenCustomHashSet implements ShortOrderedSet
{
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public ShortLinkedOpenCustomHashSet(ShortStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public ShortLinkedOpenCustomHashSet(int minCapacity, ShortStrategy strategy) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortLinkedOpenCustomHashSet(int minCapacity, float loadFactor, ShortStrategy strategy) {
		super(minCapacity, loadFactor, strategy);
		links = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public ShortLinkedOpenCustomHashSet(short[] array, ShortStrategy strategy) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortLinkedOpenCustomHashSet(short[] array, float loadFactor, ShortStrategy strategy) {
		this(array, 0, array.length, loadFactor, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ShortLinkedOpenCustomHashSet(short[] array, int offset, int length, ShortStrategy strategy) {
		this(array, offset, length, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ShortLinkedOpenCustomHashSet(short[] array, int offset, int length, float loadFactor, ShortStrategy strategy) {
		this(length < 0 ? 0 : length, strategy);
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	@Deprecated
	public ShortLinkedOpenCustomHashSet(Collection<? extends Short> collection, ShortStrategy strategy) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ShortLinkedOpenCustomHashSet(Collection<? extends Short> collection, float loadFactor, ShortStrategy strategy) {
		this(collection.size(), loadFactor, strategy);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public ShortLinkedOpenCustomHashSet(ShortCollection collection, ShortStrategy strategy) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortLinkedOpenCustomHashSet(ShortCollection collection, float loadFactor, ShortStrategy strategy) {
		this(collection.size(), strategy);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public ShortLinkedOpenCustomHashSet(Iterator<Short> iterator, ShortStrategy strategy) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortLinkedOpenCustomHashSet(Iterator<Short> iterator, float loadFactor, ShortStrategy strategy) {
		this(ShortIterators.wrap(iterator), loadFactor, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public ShortLinkedOpenCustomHashSet(ShortIterator iterator, ShortStrategy strategy) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**	
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortLinkedOpenCustomHashSet(ShortIterator iterator, float loadFactor, ShortStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, loadFactor, strategy);
		while(iterator.hasNext()) add(iterator.nextShort());
	}
	
	@Override
	public boolean addAndMoveToFirst(short o) {
		if(strategy.equals(o, (short)0)) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return false;
			}
			containsNull = true;
			onNodeAdded(nullIndex);
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
			while(!strategy.equals(keys[pos], (short)0)) {
				if(strategy.equals(keys[pos], o)) {
					moveToFirstIndex(pos);
					return false;
				}
				pos = ++pos & mask;
			}
			keys[pos] = o;
			onNodeAdded(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return true;
	}
	
	@Override
	public boolean addAndMoveToLast(short o) {
		if(strategy.equals(o, (short)0)) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return false;
			}
			containsNull = true;
			onNodeAdded(nullIndex);
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
			while(!strategy.equals(keys[pos], (short)0)) {
				if(strategy.equals(keys[pos], o)) {
					moveToLastIndex(pos);
					return false;
				}
				pos = ++pos & mask;
			}
			keys[pos] = o;
			onNodeAdded(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return true;
	}
	
	@Override
	public boolean moveToFirst(short o) {
		if(strategy.equals(firstShort(), o)) return false;
		if(strategy.equals(o, (short)0)) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
			while(!strategy.equals(keys[pos], (short)0)) {
				if(strategy.equals(keys[pos], o)) {
					moveToFirstIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(short o) {
		if(strategy.equals(lastShort(), o)) return false;
		if(strategy.equals(o, (short)0)) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
			while(!strategy.equals(keys[pos], (short)0)) {
				if(strategy.equals(keys[pos], o)) {
					moveToLastIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
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
	public short firstShort() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public short pollFirstShort() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		short result = keys[pos];
		size--;
		if(strategy.equals(result, (short)0)) {
			containsNull = false;
			keys[nullIndex] = (short)0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public short lastShort() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public short pollLastShort() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		short result = keys[pos];
		size--;
		if(strategy.equals(result, (short)0)) {
			containsNull = false;
			keys[nullIndex] = (short)0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
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
		short[] newKeys = new short[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(strategy.equals(keys[i], (short)0)) pos = newSize;
			else {
				pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask;
				while(!strategy.equals(newKeys[pos], (short)0)) pos = ++pos & newMask;
			}
			newKeys[pos] = keys[i];
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
		keys = new short[request + 1];
		links = new long[request + 1];
		firstIndex = lastIndex = -1;
		this.size = 0;
		containsNull = false;
	}
	
	@Override
	public void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(!filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		int index = firstIndex;
		while(index != -1) {
			state = operator.applyAsShort(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		int index = firstIndex;
		while(index != -1) {
			if(empty) {
				state = keys[index];
				empty = false;
			}
			else state = operator.applyAsShort(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public short findFirst(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return (short)0;
	}
	
	@Override
	public int count(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) result++;
			index = (int)links[index];
		}
		return result;
	}
	
	@Override
	public ShortListIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public ShortBidirectionalIterator iterator(short fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public ShortLinkedOpenCustomHashSet copy() {
		ShortLinkedOpenCustomHashSet set = new ShortLinkedOpenCustomHashSet(0, loadFactor, strategy);
		set.minCapacity = minCapacity;
		set.mask = mask;
		set.maxFill = maxFill;
		set.nullIndex = nullIndex;
		set.containsNull = containsNull;
		set.size = size;
		set.keys = Arrays.copyOf(keys, keys.length);
		set.links = Arrays.copyOf(links, links.length);
		set.firstIndex = firstIndex;
		set.lastIndex = lastIndex;
		return set;
	}
	
	private class SetIterator implements ShortListIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(short from) {
			if(strategy.equals(from, (short)0)) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(strategy.equals(keys[lastIndex], from)) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(strategy.hashCode(from)) & mask;
				while(!strategy.equals(keys[pos], (short)0)) {
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
		
		@Override
		public boolean hasNext() {
			return next != -1;
		}

		@Override
		public boolean hasPrevious() {
			return previous != -1;
		}
		
		@Override
		public int nextIndex() {
			ensureIndexKnown();
			return index;
		}
		
		@Override
		public int previousIndex() {
			ensureIndexKnown();
			return index - 1;
		}
		
		@Override
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
				keys[nullIndex] = (short)0;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				short current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(strategy.equals((current = keys[startPos]), (short)0)) {
							keys[last] = (short)0;
							return;
						}
						slot = HashUtil.mix(strategy.hashCode(current)) & mask;
						if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
						startPos = ++startPos & mask;
					}
					keys[last] = current;
					if(next == startPos) next = last;
					if(previous == startPos) previous = last;
					onNodeMoved(startPos, last);
				}
			}
		}
		
		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			current = next;
			next = (int)(links[current]);
			previous = current;
			if(index >= 0) index++;
			return keys[current];
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
		
		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
	}
}