package speiger.src.collections.booleans.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.BooleanPredicate;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.booleans.utils.BooleanIterators;
import speiger.src.collections.booleans.collections.BooleanSplititerator;
import speiger.src.collections.booleans.utils.BooleanSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Immutable implementation of list that is written to reduce (un)boxing
 */
public class ImmutableBooleanList extends AbstractBooleanList
{	
	/** The backing array */
	protected transient boolean[] data;
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ImmutableBooleanList(Collection<? extends Boolean> c) {
		data = BooleanArrays.pour(BooleanIterators.wrap(c.iterator()));
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableBooleanList(BooleanCollection c) {
		data = BooleanArrays.pour(c.iterator());
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ImmutableBooleanList(BooleanList l) {
		boolean[] temp = new boolean[l.size()];
		l.getElements(0, temp, 0, l.size());
		data = temp;
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Array.
	 * @param a the array that should be copied
	 */
	public ImmutableBooleanList(boolean... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ImmutableBooleanList(boolean[] a, int length) {
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
	public ImmutableBooleanList(boolean[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		data = Arrays.copyOfRange(a, offset, offset+length);
	}
	
	@Override
	public boolean add(boolean e) { throw new UnsupportedOperationException(); }
	@Override
	public void add(int index, boolean e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Boolean> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, BooleanCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, BooleanList c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(boolean[] e, int offset, int length) { throw new UnsupportedOperationException(); }
	@Override
	public void addElements(int from, boolean[] a, int offset, int length) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean[] getElements(int from, boolean[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		SanityChecks.checkArrayCapacity(data.length, from, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	@Override
	public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	@Override
	public boolean[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
	
	
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
			if(Objects.equals(o, Boolean.valueOf(data[i]))) return i;
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
			if(Objects.equals(o, Boolean.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(boolean e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(boolean e) {
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
	public int lastIndexOf(boolean e) {
		for(int i = data.length - 1;i>=0;i--) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	@Override
	public void sort(BooleanComparator c) { throw new UnsupportedOperationException(); }
	@Override
	public void unstableSort(BooleanComparator c) { throw new UnsupportedOperationException(); }
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public boolean getBoolean(int index) {
		checkRange(index);
		return data[index];
	}
	
	@Override
	public ImmutableBooleanList copy() {
		return new ImmutableBooleanList(Arrays.copyOf(data, data.length));
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
	public void forEach(BooleanConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean findFirst(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return false;
	}
	
	@Override
	public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsBoolean(state, data[i]);
		}
		return state;
	}
	
	@Override
	public boolean reduce(BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = false;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsBoolean(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) result++;
		}
		return result;
	}
	
	@Override
	public BooleanListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new BooleanListIter(index);
	}
	
	@Override
	public boolean set(int index, boolean e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Boolean> o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeBoolean(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean swapRemove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean remBoolean(boolean type) { throw new UnsupportedOperationException(); }

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Boolean> filter) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(BooleanCollection c, BooleanConsumer r) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(BooleanCollection c, BooleanConsumer r) { throw new UnsupportedOperationException(); }
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @return a Array of the elements in the list
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		if(data.length == 0) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[data.length];
		for(int i = 0,m=data.length;i<m;i++)
			obj[i] = Boolean.valueOf(data[i]);
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
			a[i] = (E)Boolean.valueOf(data[i]);
		if (a.length > data.length) a[data.length] = null;
		return a;
	}
	
	@Override
	public boolean[] toBooleanArray(boolean[] a) {
		if(a.length < data.length) a = new boolean[data.length];
		System.arraycopy(data, 0, a, 0, data.length);
		if (a.length > data.length) a[data.length] = false;
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
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public BooleanSplititerator spliterator() { return BooleanSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	private class BooleanListIter implements BooleanListIterator {
		int index;
		
		BooleanListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			return getBoolean(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public boolean previousBoolean() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return getBoolean(--index);
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
		public void set(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, size() - index);
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