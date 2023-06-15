package speiger.src.collections.floats.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.DoublePredicate;import java.util.function.DoubleUnaryOperator;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.floats.utils.FloatIterators;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.floats.utils.FloatSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Immutable implementation of list that is written to reduce (un)boxing
 */
public class ImmutableFloatList extends AbstractFloatList
{	
	/** The backing array */
	protected transient float[] data;
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ImmutableFloatList(Collection<? extends Float> c) {
		data = FloatArrays.pour(FloatIterators.wrap(c.iterator()));
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableFloatList(FloatCollection c) {
		data = FloatArrays.pour(c.iterator());
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ImmutableFloatList(FloatList l) {
		float[] temp = new float[l.size()];
		l.getElements(0, temp, 0, l.size());
		data = temp;
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Array.
	 * @param a the array that should be copied
	 */
	public ImmutableFloatList(float... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ImmutableFloatList(float[] a, int length) {
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
	public ImmutableFloatList(float[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		data = Arrays.copyOfRange(a, offset, offset+length);
	}
	
	@Override
	public boolean add(float e) { throw new UnsupportedOperationException(); }
	@Override
	public void add(int index, float e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Float> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, FloatCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, FloatList c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(float[] e, int offset, int length) { throw new UnsupportedOperationException(); }
	@Override
	public void addElements(int from, float[] a, int offset, int length) { throw new UnsupportedOperationException(); }
	
	@Override
	public float[] getElements(int from, float[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		SanityChecks.checkArrayCapacity(data.length, from, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	@Override
	public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	@Override
	public float[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
	
	
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
			if(Objects.equals(o, Float.valueOf(data[i]))) return i;
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
			if(Objects.equals(o, Float.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(float e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(float e) {
		for(int i = 0,m=data.length;i<m;i++) {
			if(Float.floatToIntBits(data[i]) == Float.floatToIntBits(e)) return i;
		}
		return -1;
	}
	
	/**
	 * A Type-Specific function to find the last index of a given element
	 * @param e the element that is searched for
	 * @return the last index of the element if found. (if not found then -1)
	 */
	@Override
	public int lastIndexOf(float e) {
		for(int i = data.length - 1;i>=0;i--) {
			if(Float.floatToIntBits(data[i]) == Float.floatToIntBits(e)) return i;
		}
		return -1;
	}
	
	@Override
	public void sort(FloatComparator c) { throw new UnsupportedOperationException(); }
	@Override
	public void unstableSort(FloatComparator c) { throw new UnsupportedOperationException(); }
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public float getFloat(int index) {
		checkRange(index);
		return data[index];
	}
	
	@Override
	public ImmutableFloatList copy() {
		return new ImmutableFloatList(Arrays.copyOf(data, data.length));
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
	public void forEach(FloatConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public float findFirst(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return 0F;
	}
	
	@Override
	public float reduce(float identity, FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsFloat(state, data[i]);
		}
		return state;
	}
	
	@Override
	public float reduce(FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = 0F;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsFloat(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) result++;
		}
		return result;
	}
	
	@Override
	public FloatListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new FloatListIter(index);
	}
	
	@Override
	public float set(int index, float e) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Float> o) { throw new UnsupportedOperationException(); }
	@Override
	public void replaceFloats(DoubleUnaryOperator o) { throw new UnsupportedOperationException(); }
	@Override
	public float removeFloat(int index) { throw new UnsupportedOperationException(); }
	@Override
	public float swapRemove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean remFloat(float type) { throw new UnsupportedOperationException(); }

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Float> filter) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(FloatCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(FloatCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(FloatCollection c, FloatConsumer r) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(FloatCollection c, FloatConsumer r) { throw new UnsupportedOperationException(); }
	
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
			obj[i] = Float.valueOf(data[i]);
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
			a[i] = (E)Float.valueOf(data[i]);
		if (a.length > data.length) a[data.length] = null;
		return a;
	}
	
	@Override
	public float[] toFloatArray(float[] a) {
		if(a.length < data.length) a = new float[data.length];
		System.arraycopy(data, 0, a, 0, data.length);
		if (a.length > data.length) a[data.length] = 0F;
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
	public DoubleStream primitiveStream() { return StreamSupport.doubleStream(FloatSplititerators.createArrayJavaSplititerator(data, data.length, 16464), false); }
		/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public FloatSplititerator spliterator() { return FloatSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	private class FloatListIter implements FloatListIterator {
		int index;
		
		FloatListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public float nextFloat() {
			if(!hasNext()) throw new NoSuchElementException();
			return getFloat(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public float previousFloat() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return getFloat(--index);
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
		public void set(float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(float e) { throw new UnsupportedOperationException(); }
		
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