package speiger.src.collections.shorts.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.utils.ShortIterators;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ShortOpenHashSet extends AbstractShortSet implements ITrimmable
{
	/** The Backing keys array */
	protected transient short[] keys;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Minimum array size the HashSet will be */
	protected transient int minCapacity;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	
	/** Amount of Elements stored in the HashSet */
	protected int size;
	/** How full the Array is allowed to get before resize */
	protected final float loadFactor;
	
	/**
	 * Default Constructor
	 */
	public ShortOpenHashSet() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public ShortOpenHashSet(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortOpenHashSet(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new short[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ShortOpenHashSet(short[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortOpenHashSet(short[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ShortOpenHashSet(short[] array, int offset, int length) {
		this(array, offset, length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ShortOpenHashSet(short[] array, int offset, int length, float loadFactor) {
		this(length < 0 ? 0 : length);
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ShortOpenHashSet(Collection<? extends Short> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ShortOpenHashSet(Collection<? extends Short> collection, float loadFactor) {
		this(collection.size(), loadFactor);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ShortOpenHashSet(ShortCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortOpenHashSet(ShortCollection collection, float loadFactor) {
		this(collection.size());
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ShortOpenHashSet(Iterator<Short> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortOpenHashSet(Iterator<Short> iterator, float loadFactor) {
		this(ShortIterators.wrap(iterator), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ShortOpenHashSet(ShortIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ShortOpenHashSet(ShortIterator iterator, float loadFactor) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, loadFactor);
		while(iterator.hasNext()) add(iterator.nextShort());
	}
	
	@Override
	public boolean add(short o) {
		if(o == (short)0) {
			if(containsNull) return false;
			containsNull = true;
			onNodeAdded(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Short.hashCode(o)) & mask;
			short current = keys[pos];
			if(current != (short)0) {
				if(current == o) return false;
				while((current = keys[pos = (++pos & mask)]) != (short)0)
					if(current == o) return false;
			}
			keys[pos] = o;
			onNodeAdded(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return true;
	}
	
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Short> c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(ShortCollection c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());		
		return super.addAll(c);
	}
	
	@Override
	public boolean contains(Object o) {
		if(o == null) return false;
		if(o instanceof Short && ((Short)o).shortValue() == (short)0) return containsNull;
		int pos = HashUtil.mix(o.hashCode()) & mask;
		short current = keys[pos];
		if(current == (short)0) return false;
		if(Objects.equals(o, Short.valueOf(current))) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (short)0) return false;
			else if(Objects.equals(o, Short.valueOf(current))) return true;
		}
	}

	@Override
	public boolean remove(Object o) {
		if(o == null) return false;
		if(o instanceof Short && ((Short)o).shortValue() == (short)0) return (containsNull ? removeNullIndex() : false);
		int pos = HashUtil.mix(o.hashCode()) & mask;
		short current = keys[pos];
		if(current == (short)0) return false;
		if(Objects.equals(o, Short.valueOf(current))) return removeIndex(pos);
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (short)0) return false;
			else if(Objects.equals(o, Short.valueOf(current))) return removeIndex(pos);
		}
	}

	@Override
	public boolean contains(short o) {
		if(o == (short)0) return containsNull;
		int pos = HashUtil.mix(Short.hashCode(o)) & mask;
		short current = keys[pos];
		if(current == (short)0) return false;
		if(current == o) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (short)0) return false;
			else if(current == o) return true;
		}
	}
	
	@Override
	public boolean remove(short o) {
		if(o == (short)0) return containsNull ? removeNullIndex() : false;
		int pos = HashUtil.mix(Short.hashCode(o)) & mask;
		short current = keys[pos];
		if(current == (short)0) return false;
		if(current == o) return removeIndex(pos);
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (short)0) return false;
			else if(current == o) return removeIndex(pos);
		}
	}
	
	@Override
	public boolean trim(int size) {
		int newSize = HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor));
		if(newSize >= nullIndex || size >= Math.min((int)Math.ceil(newSize * loadFactor), newSize - 1)) return false;
		try {
			rehash(newSize);
		}
		catch(OutOfMemoryError e) { return false; }
		return true;
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
		this.size = 0;
		containsNull = false;
	}
	
	@Override
	public void forEach(ShortConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0) action.accept(keys[i]);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		if(size() <= 0) return;
		if(containsNull) action.accept(input, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0) action.accept(input, keys[i]);
		}
	}
	
	@Override
	public boolean matchesAny(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return false;
		if(containsNull && filter.get(keys[nullIndex])) return true;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0 && filter.get(keys[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && filter.get(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0 && filter.get(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && !filter.get(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0 && !filter.get(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		if(containsNull) state = operator.applyAsShort(state, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] == (short)0) continue;
			state = operator.applyAsShort(state, keys[i]);
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		if(containsNull) {
			state = keys[nullIndex];
			empty = false;
		}
		for(int i = 0;i<size;i++) {
			if(keys[i] == (short)0) continue;
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
		if(size() <= 0) return (short)0;
		if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0 && filter.get(keys[i])) return keys[i];
		}
		return (short)0;
	}
	
	@Override
	public int count(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return 0;
		int result = 0;
		if(containsNull && filter.get(keys[nullIndex])) result++;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (short)0 && filter.get(keys[i])) result++;
		}
		return result;
	}
	
	private void ensureCapacity(int newCapacity) {
		int size = HashUtil.arraySize(newCapacity, loadFactor);
		if(size > nullIndex) rehash(size);
	}
	
	protected boolean removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : false;
		keys[pos] = (short)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return true;
	}
	
	protected boolean removeNullIndex() {
		containsNull = false;
		keys[nullIndex] = (short)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return true;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		short current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if((current = keys[startPos]) == (short)0) {
					keys[last] = (short)0;
					return;
				}
				slot = HashUtil.mix(Short.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			onNodeMoved(startPos, last);
		}
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		short[] newKeys = new short[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Set was modified during rehash");
				if(keys[i] != (short)0) break;
			}
			if(newKeys[pos = HashUtil.mix(Short.hashCode(keys[i])) & newMask] != (short)0)
				while(newKeys[pos = (++pos & newMask)] != (short)0);
			newKeys[pos] = keys[i];
		}
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
	}
	
	@Override
	public ShortIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public ShortOpenHashSet copy() {
		ShortOpenHashSet set = new ShortOpenHashSet(0, loadFactor);
		set.minCapacity = minCapacity;
		set.mask = mask;
		set.maxFill = maxFill;
		set.nullIndex = nullIndex;
		set.containsNull = containsNull;
		set.size = size;
		set.keys = Arrays.copyOf(keys, keys.length);
		return set;
	}
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		containsNull = false;
		Arrays.fill(keys, (short)0);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	private class SetIterator implements ShortIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		ShortList wrapped = null;
		
		@Override
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				if(returnNull) {
					returnNull = false;
					nextIndex = nullIndex;
				}
				else
				{
					while(true) {
						if(--pos < 0) {
							if(wrapped == null || wrapped.size() <= -pos - 1) break;
							nextIndex = -pos - 1;
							break;
						}
						if(keys[pos] != (short)0){
							nextIndex = pos;
							break;
						}
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				short value = wrapped.getShort(nextIndex);
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			short value = keys[(lastReturned = nextIndex)];
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		@Override
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(lastReturned == nullIndex) {
				containsNull = false;
				keys[nullIndex] = (short)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				ShortOpenHashSet.this.remove(wrapped.getShort(-returnedPos - 1));
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			short current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == (short)0) {
						keys[last] = (short)0;
						return;
					}
					slot = HashUtil.mix(Short.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new ShortArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
			}
		}
	}
}