package speiger.src.collections.ints.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.ints.utils.IntIterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.utils.IntSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Immutable implementation of list that is written to reduce (un)boxing
 */
public class ImmutableIntList extends AbstractIntList
{	
	/** The backing array */
	protected transient int[] data;
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ImmutableIntList(Collection<? extends Integer> c) {
		data = IntArrays.pour(IntIterators.wrap(c.iterator()));
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableIntList(IntCollection c) {
		data = IntArrays.pour(c.iterator());
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ImmutableIntList(IntList l) {
		int[] temp = new int[l.size()];
		l.getElements(0, temp, 0, l.size());
		data = temp;
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Array.
	 * @param a the array that should be copied
	 */
	public ImmutableIntList(int... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ImmutableIntList(int[] a, int length) {
		this(a, 0, length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with in the custom range.
	 * @param a the array that should be copied
	 * @param offset the starting offset of where the array should be copied from
	 * @param length the desired length that should be copied
	 * @throws IllegalStateException if offset is smaller then 0
	 * @throws IllegalStateException if the offset + length exceeds the array length
	 */
	public ImmutableIntList(int[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		data = Arrays.copyOfRange(a, offset, offset+length);
	}
	
	@Override
	public boolean add(int e) { throw new UnsupportedOperationException(); }
	@Override
	public void add(int index, int e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, IntCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, IntList c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int[] e, int offset, int length) { throw new UnsupportedOperationException(); }
	@Override
	public void addElements(int from, int[] a, int offset, int length) { throw new UnsupportedOperationException(); }
	
	@Override
	public int[] getElements(int from, int[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(data.length, offset, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	@Override
	public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	@Override
	public int[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
	
	
	/**
	 * A function to find if the Element is present in this list.
	 * @param o the element that is searched for
	 * @return if the element was found.
	 */
	@Override
	@Deprecated
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}
	
	/**
	 * A function to find the index of a given element
	 * @param o the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	@Deprecated
	public int indexOf(Object o) {
		if(o == null) return -1;
		for(int i = 0,m=data.length;i<m;i++) {
			if(Objects.equals(o, Integer.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A function to find the last index of a given element
	 * @param o the element that is searched for
	 * @return the last index of the element if found. (if not found then -1)
	 */
	@Override
	@Deprecated
	public int lastIndexOf(Object o) {
		if(o == null) return -1;
		for(int i = data.length - 1;i>=0;i--) {
			if(Objects.equals(o, Integer.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(int e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(int e) {
		for(int i = 0,m=data.length;i<m;i++) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	/**
	 * A Type-Specific function to find the last index of a given element
	 * @param e the element that is searched for
	 * @return the last index of the element if found. (if not found then -1)
	 */
	@Override
	public int lastIndexOf(int e) {
		for(int i = data.length - 1;i>=0;i--) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	@Override
	public void sort(IntComparator c) { throw new UnsupportedOperationException(); }
	@Override
	public void unstableSort(IntComparator c) { throw new UnsupportedOperationException(); }
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public int getInt(int index) {
		checkRange(index);
		return data[index];
	}
	
	@Override
	public ImmutableIntList copy() {
		return new ImmutableIntList(Arrays.copyOf(data, data.length));
	}
	
	/**
	 * A Type Specific foreach function that reduces (un)boxing
	 * 
	 * @implSpec
	 * <p>The default implementation behaves as if:
	 * <pre>{@code
	 * 	for(int i = 0;i<size;i++)
	 * 		action.accept(data[i]);
	 * }</pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 * @see Iterable#forEach(java.util.function.Consumer)
	 */
	@Override
	public void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectIntConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public int findFirst(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return data[i];
		}
		return 0;
	}
	
	@Override
	public int reduce(int identity, IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsInt(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int reduce(IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = 0;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsInt(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) result++;
		}
		return result;
	}
	
	@Override
	public IntListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new IntListIter(index);
	}
	
	@Override
	public int set(int index, int e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Integer> o) { throw new UnsupportedOperationException(); }
	@Override
	public void replaceInts(IntUnaryOperator o) { throw new UnsupportedOperationException(); }
	@Override
	public int removeInt(int index) { throw new UnsupportedOperationException(); }
	@Override
	public int swapRemove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean remInt(int type) { throw new UnsupportedOperationException(); }

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Integer> filter) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(IntCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(IntCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(IntCollection c, IntConsumer r) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(IntCollection c, IntConsumer r) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remIf(IntPredicate filter) { throw new UnsupportedOperationException(); }
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @return a Array of the elements in the list
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		Object[] obj = new Object[data.length];
		for(int i = 0,m=data.length;i<m;i++)
			obj[i] = Integer.valueOf(data[i]);
		return obj;
	}
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @param a original array. If null a Object array with the right size is created. If to small the Array of the same type is created with the right size
	 * @return a Array of the elements in the list
	 */
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[data.length];
		else if(a.length < data.length) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), data.length);
		for(int i = 0,m=data.length;i<m;i++)
			a[i] = (E)Integer.valueOf(data[i]);
		if (a.length > data.length) a[data.length] = null;
		return a;
	}
	
	@Override
	public int[] toIntArray(int[] a) {
		if(a.length < data.length) a = new int[data.length];
		System.arraycopy(data, 0, a, 0, data.length);
		if (a.length > data.length) a[data.length] = 0;
		return a;
	}
	
	/**
	 * A function to return the size of the list
	 * @return the size of elements in the list
	 */
	@Override
	public int size() {
		return data.length;
	}
	
	@Override
	public void size(int size) { throw new UnsupportedOperationException(); }
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected void checkRange(int index) {
		if (index < 0 || index >= data.length)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + data.length);
	}
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public IntStream primitiveStream() { return StreamSupport.intStream(IntSplititerators.createArrayJavaSplititerator(data, data.length, 16464), false); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public IntSplititerator spliterator() { return IntSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	private class IntListIter implements IntListIterator {
		int index;
		
		IntListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return getInt(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return getInt(--index);
		}
		
		@Override
		public int nextIndex() {
			return index;
		}
		
		@Override
		public int previousIndex() {
			return index-1;
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }
		
		@Override
		public void set(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, (size() - 1) - index);
			index += steps;
			return steps;
		}
		
		@Override
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			return steps;
		}
	}
}