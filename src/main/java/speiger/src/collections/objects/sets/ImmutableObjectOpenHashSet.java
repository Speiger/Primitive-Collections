package speiger.src.collections.objects.sets;

import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.utils.ObjectIterators;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 * @param <T> the type of elements maintained by this Collection
 */
public class ImmutableObjectOpenHashSet<T> extends AbstractObjectSet<T> implements ObjectOrderedSet<T>
{
	/** The Backing keys array */
	protected transient T[] keys;
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
	protected ImmutableObjectOpenHashSet() {}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ImmutableObjectOpenHashSet(T[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObjectOpenHashSet(T[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableObjectOpenHashSet(T[] array, int offset, int length) {
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
	public ImmutableObjectOpenHashSet(T[] array, int offset, int length, float loadFactor) {
		init(array, offset, length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableObjectOpenHashSet(Collection<? extends T> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObjectOpenHashSet(Collection<? extends T> collection, float loadFactor) {
		init(collection.toArray((T[])new Object[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableObjectOpenHashSet(ObjectCollection<T> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObjectOpenHashSet(ObjectCollection<T> collection, float loadFactor) {
		init(collection.toArray((T[])new Object[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableObjectOpenHashSet(Iterator<T> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObjectOpenHashSet(Iterator<T> iterator, float loadFactor) {
		this(ObjectIterators.wrap(iterator), loadFactor);
	}
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableObjectOpenHashSet(ObjectIterator<T> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObjectOpenHashSet(ObjectIterator<T> iterator, float loadFactor) {
		T[] array = ObjectArrays.pour(iterator);
		init(array, 0, array.length, loadFactor);
	}
	
	protected void init(T[] a, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		T[] newKeys = (T[])new Object[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			T o = a[i];
			if(o == null) {
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
			int pos = HashUtil.mix(Objects.hashCode(o)) & newMask;
			T current = newKeys[pos];
			if(current != null) {
				if(Objects.equals(current, o)) continue;
				while((current = newKeys[pos = (++pos & newMask)]) != null) {
					if(Objects.equals(current, o)) {
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
	public boolean add(T o) { throw new UnsupportedOperationException(); }
	@Override
	public T addOrGet(T o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(Collection<? extends T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(T o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(T o) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean contains(Object o) {
		if(o == null) return containsNull;
		int pos = HashUtil.mix(o.hashCode()) & mask;
		T current = keys[pos];
		if(current == null) return false;
		if(Objects.equals(o, current)) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == null) return false;
			else if(Objects.equals(o, current)) return true;
		}
	}
	
	@Override
	public T first() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public T pollFirst() { throw new UnsupportedOperationException(); }
	
	@Override
	public T last() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public T pollLast() { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object o) { throw new UnsupportedOperationException(); }

	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.getBoolean(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.getBoolean(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
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
				state = keys[index];
				empty = false;
			}
			else state = operator.apply(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public T findFirst(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.getBoolean(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return null;
	}
	
	@Override
	public int count(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		int index = firstIndex;
		while(index != -1) {
			if(filter.getBoolean(keys[index])) result++;
			index = (int)links[index];
		}
		return result;
	}
	
	@Override
	public ObjectListIterator<T> iterator() {
		return new SetIterator();
	}
	
	@Override
	public ObjectBidirectionalIterator<T> iterator(T fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public ImmutableObjectOpenHashSet<T> copy() {
		ImmutableObjectOpenHashSet<T> set = new ImmutableObjectOpenHashSet<>();
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

	private class SetIterator implements ObjectListIterator<T> {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(T from) {
			if(from == null) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(Objects.equals(keys[lastIndex], from)) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Objects.hashCode(from)) & mask;
				while(keys[pos] != null) {
					if(Objects.equals(keys[pos], from)) {
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
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public T next() {
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
		public void set(Object e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(Object e) { throw new UnsupportedOperationException(); }
	}
}