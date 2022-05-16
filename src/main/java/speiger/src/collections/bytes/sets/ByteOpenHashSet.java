package speiger.src.collections.bytes.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.utils.ByteIterators;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ByteOpenHashSet extends AbstractByteSet implements ITrimmable
{
	/** The Backing keys array */
	protected transient byte[] keys;
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
	public ByteOpenHashSet() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public ByteOpenHashSet(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ByteOpenHashSet(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new byte[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ByteOpenHashSet(byte[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ByteOpenHashSet(byte[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ByteOpenHashSet(byte[] array, int offset, int length) {
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
	public ByteOpenHashSet(byte[] array, int offset, int length, float loadFactor) {
		this(length < 0 ? 0 : length);
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ByteOpenHashSet(Collection<? extends Byte> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ByteOpenHashSet(Collection<? extends Byte> collection, float loadFactor) {
		this(collection.size(), loadFactor);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ByteOpenHashSet(ByteCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ByteOpenHashSet(ByteCollection collection, float loadFactor) {
		this(collection.size());
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ByteOpenHashSet(Iterator<Byte> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ByteOpenHashSet(Iterator<Byte> iterator, float loadFactor) {
		this(ByteIterators.wrap(iterator), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ByteOpenHashSet(ByteIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ByteOpenHashSet(ByteIterator iterator, float loadFactor) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, loadFactor);
		while(iterator.hasNext()) add(iterator.nextByte());
	}
	
	@Override
	public boolean add(byte o) {
		if(o == (byte)0) {
			if(containsNull) return false;
			containsNull = true;
			onNodeAdded(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Byte.hashCode(o)) & mask;
			byte current = keys[pos];
			if(current != (byte)0) {
				if(current == o) return false;
				while((current = keys[pos = (++pos & mask)]) != (byte)0)
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
	public boolean addAll(Collection<? extends Byte> c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(ByteCollection c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());		
		return super.addAll(c);
	}
	
	@Override
	public boolean contains(Object o) {
		if(o == null) return false;
		if(o instanceof Byte && ((Byte)o).byteValue() == (byte)0) return containsNull;
		int pos = HashUtil.mix(o.hashCode()) & mask;
		byte current = keys[pos];
		if(current == (byte)0) return false;
		if(Objects.equals(o, Byte.valueOf(current))) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (byte)0) return false;
			else if(Objects.equals(o, Byte.valueOf(current))) return true;
		}
	}

	@Override
	public boolean remove(Object o) {
		if(o == null) return false;
		if(o instanceof Byte && ((Byte)o).byteValue() == (byte)0) return (containsNull ? removeNullIndex() : false);
		int pos = HashUtil.mix(o.hashCode()) & mask;
		byte current = keys[pos];
		if(current == (byte)0) return false;
		if(Objects.equals(o, Byte.valueOf(current))) return removeIndex(pos);
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (byte)0) return false;
			else if(Objects.equals(o, Byte.valueOf(current))) return removeIndex(pos);
		}
	}

	@Override
	public boolean contains(byte o) {
		if(o == (byte)0) return containsNull;
		int pos = HashUtil.mix(Byte.hashCode(o)) & mask;
		byte current = keys[pos];
		if(current == (byte)0) return false;
		if(current == o) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (byte)0) return false;
			else if(current == o) return true;
		}
	}
	
	@Override
	public boolean remove(byte o) {
		if(o == (byte)0) return containsNull ? removeNullIndex() : false;
		int pos = HashUtil.mix(Byte.hashCode(o)) & mask;
		byte current = keys[pos];
		if(current == (byte)0) return false;
		if(current == o) return removeIndex(pos);
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == (byte)0) return false;
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
		keys = new byte[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	@Override
	public void forEach(ByteConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0) action.accept(keys[i]);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		if(size() <= 0) return;
		if(containsNull) action.accept(input, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0) action.accept(input, keys[i]);
		}
	}
	
	@Override
	public boolean matchesAny(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return false;
		if(containsNull && filter.get(keys[nullIndex])) return true;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0 && filter.get(keys[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && filter.get(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0 && filter.get(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && !filter.get(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0 && !filter.get(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = identity;
		if(containsNull) state = operator.applyAsByte(state, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] == (byte)0) continue;
			state = operator.applyAsByte(state, keys[i]);
		}
		return state;
	}
	
	@Override
	public byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = (byte)0;
		boolean empty = true;
		if(containsNull) {
			state = keys[nullIndex];
			empty = false;
		}
		for(int i = 0;i<size;i++) {
			if(keys[i] == (byte)0) continue;
			if(empty) {
				empty = false;
				state = keys[i];
				continue;
			}
			state = operator.applyAsByte(state, keys[i]);
		}
		return state;
	}
	
	@Override
	public byte findFirst(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return (byte)0;
		if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0 && filter.get(keys[i])) return keys[i];
		}
		return (byte)0;
	}
	
	@Override
	public int count(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return 0;
		int result = 0;
		if(containsNull && filter.get(keys[nullIndex])) result++;
		for(int i = nullIndex-1;i>=0;i--) {
			if(keys[i] != (byte)0 && filter.get(keys[i])) result++;
		}
		return result;
	}
	
	private void ensureCapacity(int newCapacity) {
		int size = HashUtil.arraySize(newCapacity, loadFactor);
		if(size > nullIndex) rehash(size);
	}
	
	protected boolean removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : false;
		keys[pos] = (byte)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return true;
	}
	
	protected boolean removeNullIndex() {
		containsNull = false;
		keys[nullIndex] = (byte)0;
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
		byte current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if((current = keys[startPos]) == (byte)0) {
					keys[last] = (byte)0;
					return;
				}
				slot = HashUtil.mix(Byte.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			onNodeMoved(startPos, last);
		}
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		byte[] newKeys = new byte[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Set was modified during rehash");
				if(keys[i] != (byte)0) break;
			}
			if(newKeys[pos = HashUtil.mix(Byte.hashCode(keys[i])) & newMask] != (byte)0)
				while(newKeys[pos = (++pos & newMask)] != (byte)0);
			newKeys[pos] = keys[i];
		}
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
	}
	
	@Override
	public ByteIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public ByteOpenHashSet copy() {
		ByteOpenHashSet set = new ByteOpenHashSet(0, loadFactor);
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
		Arrays.fill(keys, (byte)0);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	private class SetIterator implements ByteIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		ByteList wrapped = null;
		
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
						if(keys[pos] != (byte)0){
							nextIndex = pos;
							break;
						}
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				byte value = wrapped.getByte(nextIndex);
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			byte value = keys[(lastReturned = nextIndex)];
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		@Override
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(lastReturned == nullIndex) {
				containsNull = false;
				keys[nullIndex] = (byte)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				ByteOpenHashSet.this.remove(wrapped.getByte(-returnedPos - 1));
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			byte current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == (byte)0) {
						keys[last] = (byte)0;
						return;
					}
					slot = HashUtil.mix(Byte.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new ByteArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
			}
		}
	}
}