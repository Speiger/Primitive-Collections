package speiger.src.collections.bytes.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.bytes.utils.ByteIterators;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableByteOpenHashSet extends AbstractByteSet implements ByteOrderedSet
{
	/** The Backing keys array */
	protected transient byte[] keys;
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** Amount of Elements stored in the HashSet */
	protected int size;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Helper constructor to optimize the copying
	 * Only accessible through implementations
	 */
	protected ImmutableByteOpenHashSet() {}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ImmutableByteOpenHashSet(byte[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableByteOpenHashSet(byte[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableByteOpenHashSet(byte[] array, int offset, int length) {
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
	public ImmutableByteOpenHashSet(byte[] array, int offset, int length, float loadFactor) {
		init(array, offset, length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ImmutableByteOpenHashSet(Collection<? extends Byte> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ImmutableByteOpenHashSet(Collection<? extends Byte> collection, float loadFactor) {
		init(ByteArrays.unwrap(collection.toArray(new Byte[collection.size()])), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableByteOpenHashSet(ByteCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableByteOpenHashSet(ByteCollection collection, float loadFactor) {
		init(collection.toByteArray(new byte[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableByteOpenHashSet(Iterator<Byte> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableByteOpenHashSet(Iterator<Byte> iterator, float loadFactor) {
		this(ByteIterators.wrap(iterator), loadFactor);
	}
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableByteOpenHashSet(ByteIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableByteOpenHashSet(ByteIterator iterator, float loadFactor) {
		byte[] array = ByteArrays.pour(iterator);
		init(array, 0, array.length, loadFactor);
	}
	
	protected void init(byte[] a, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		byte[] newKeys = new byte[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			byte o = a[i];
			if(o == (byte)0) {
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
				continue;
			}
			boolean found = true;
			int pos = HashUtil.mix(Byte.hashCode(o)) & newMask;
			byte current = newKeys[pos];
			if(current != (byte)0) {
				if(current == o) continue;
				while((current = newKeys[pos = (++pos & newMask)]) != (byte)0) {
					if(current == o) {
						found = false;
						break;
					}
				}
			}
			if(found) {
				size++;
				newKeys[pos] = o;
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
		links = newLinks;
		lastIndex = prev;
		if(prev != -1) newLinks[prev] |= 0xFFFFFFFFL;
	}
	
	@Override
	public boolean add(byte o) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Byte> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(ByteCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToFirst(byte o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToLast(byte o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(byte o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(byte o) { throw new UnsupportedOperationException(); }
	
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
	public byte firstByte() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public byte pollFirstByte() { throw new UnsupportedOperationException(); }
	
	@Override
	public byte lastByte() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public byte pollLastByte() { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object o) { throw new UnsupportedOperationException(); }

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
	public boolean remove(byte o) { throw new UnsupportedOperationException(); }
	
	@Override
	public void forEach(ByteConsumer action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(!filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = identity;
		int index = firstIndex;
		while(index != -1) {
			state = operator.applyAsByte(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = (byte)0;
		boolean empty = true;
		int index = firstIndex;
		while(index != -1) {
			if(empty) {
				state = keys[index];
				empty = false;
			}
			else state = operator.applyAsByte(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public byte findFirst(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return (byte)0;
	}
	
	@Override
	public int count(Byte2BooleanFunction filter) {
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
	public ByteListIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public ByteBidirectionalIterator iterator(byte fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public ImmutableByteOpenHashSet copy() {
		ImmutableByteOpenHashSet set = new ImmutableByteOpenHashSet();
		set.containsNull = containsNull;
		set.firstIndex = firstIndex;
		set.lastIndex = lastIndex;
		set.size = size;
		set.mask = mask;
		set.nullIndex = nullIndex;
		set.keys = Arrays.copyOf(keys, keys.length);
		set.links = Arrays.copyOf(links, links.length);
		return set;
	}
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() {
		return size;
	}

	private class SetIterator implements ByteListIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(byte from) {
			if(from == (byte)0) {
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
				int pos = HashUtil.mix(Byte.hashCode(from)) & mask;
				while(keys[pos] != (byte)0) {
					if(keys[pos] == from) {
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
		public void remove() { throw new UnsupportedOperationException(); }
		
		@Override
		public byte previousByte() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public byte nextByte() {
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
		public void set(byte e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
	}
}