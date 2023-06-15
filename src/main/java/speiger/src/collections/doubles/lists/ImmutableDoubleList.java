package speiger.src.collections.doubles.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.doubles.utils.DoubleIterators;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Immutable implementation of list that is written to reduce (un)boxing
 */
public class ImmutableDoubleList extends AbstractDoubleList
{	
	/** The backing array */
	protected transient double[] data;
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ImmutableDoubleList(Collection<? extends Double> c) {
		data = DoubleArrays.pour(DoubleIterators.wrap(c.iterator()));
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableDoubleList(DoubleCollection c) {
		data = DoubleArrays.pour(c.iterator());
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ImmutableDoubleList(DoubleList l) {
		double[] temp = new double[l.size()];
		l.getElements(0, temp, 0, l.size());
		data = temp;
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Array.
	 * @param a the array that should be copied
	 */
	public ImmutableDoubleList(double... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ImmutableDoubleList(double[] a, int length) {
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
	public ImmutableDoubleList(double[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		data = Arrays.copyOfRange(a, offset, offset+length);
	}
	
	@Override
	public boolean add(double e) { throw new UnsupportedOperationException(); }
	@Override
	public void add(int index, double e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, DoubleCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, DoubleList c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(double[] e, int offset, int length) { throw new UnsupportedOperationException(); }
	@Override
	public void addElements(int from, double[] a, int offset, int length) { throw new UnsupportedOperationException(); }
	
	@Override
	public double[] getElements(int from, double[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		SanityChecks.checkArrayCapacity(data.length, from, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	@Override
	public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	@Override
	public double[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
	
	
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
			if(Objects.equals(o, Double.valueOf(data[i]))) return i;
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
			if(Objects.equals(o, Double.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(double e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(double e) {
		for(int i = 0,m=data.length;i<m;i++) {
			if(Double.doubleToLongBits(data[i]) == Double.doubleToLongBits(e)) return i;
		}
		return -1;
	}
	
	/**
	 * A Type-Specific function to find the last index of a given element
	 * @param e the element that is searched for
	 * @return the last index of the element if found. (if not found then -1)
	 */
	@Override
	public int lastIndexOf(double e) {
		for(int i = data.length - 1;i>=0;i--) {
			if(Double.doubleToLongBits(data[i]) == Double.doubleToLongBits(e)) return i;
		}
		return -1;
	}
	
	@Override
	public void sort(DoubleComparator c) { throw new UnsupportedOperationException(); }
	@Override
	public void unstableSort(DoubleComparator c) { throw new UnsupportedOperationException(); }
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public double getDouble(int index) {
		checkRange(index);
		return data[index];
	}
	
	@Override
	public ImmutableDoubleList copy() {
		return new ImmutableDoubleList(Arrays.copyOf(data, data.length));
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
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public double findFirst(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return 0D;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsDouble(state, data[i]);
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsDouble(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) result++;
		}
		return result;
	}
	
	@Override
	public DoubleListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new DoubleListIter(index);
	}
	
	@Override
	public double set(int index, double e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Double> o) { throw new UnsupportedOperationException(); }
	@Override
	public void replaceDoubles(DoubleUnaryOperator o) { throw new UnsupportedOperationException(); }
	@Override
	public double removeDouble(int index) { throw new UnsupportedOperationException(); }
	@Override
	public double swapRemove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean remDouble(double type) { throw new UnsupportedOperationException(); }

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Double> filter) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remIf(DoublePredicate filter) { throw new UnsupportedOperationException(); }
	
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
			obj[i] = Double.valueOf(data[i]);
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
			a[i] = (E)Double.valueOf(data[i]);
		if (a.length > data.length) a[data.length] = null;
		return a;
	}
	
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a.length < data.length) a = new double[data.length];
		System.arraycopy(data, 0, a, 0, data.length);
		if (a.length > data.length) a[data.length] = 0D;
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
	public DoubleStream primitiveStream() { return StreamSupport.doubleStream(DoubleSplititerators.createArrayJavaSplititerator(data, data.length, 16464), false); }
		/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public DoubleSplititerator spliterator() { return DoubleSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	private class DoubleListIter implements DoubleListIterator {
		int index;
		
		DoubleListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			return getDouble(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return getDouble(--index);
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
		public void set(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }
		
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