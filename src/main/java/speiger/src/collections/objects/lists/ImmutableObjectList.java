package speiger.src.collections.objects.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.collections.ObjectSplititerator;
import speiger.src.collections.objects.utils.ObjectSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Immutable implementation of list that is written to reduce (un)boxing
 *
 * @param <T> the type of elements maintained by this Collection
 */
public class ImmutableObjectList<T> extends AbstractObjectList<T>
{	
	/** The backing array */
	protected transient T[] data;
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableObjectList(Collection<? extends T> c) {
		data = ObjectArrays.pour(ObjectIterators.wrap(c.iterator()));
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ImmutableObjectList(ObjectCollection<T> c) {
		data = ObjectArrays.pour(c.iterator());
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ImmutableObjectList(ObjectList<T> l) {
		T[] temp = (T[])new Object[l.size()];
		l.getElements(0, temp, 0, l.size());
		data = temp;
	}
	
	/**
	 * Creates a new Immutable copy of the contents of the Array.
	 * @param a the array that should be copied
	 */
	public ImmutableObjectList(T... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ImmutableList copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ImmutableObjectList(T[] a, int length) {
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
	public ImmutableObjectList(T[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		data = Arrays.copyOfRange(a, offset, offset+length);
	}
	
	@Override
	public boolean add(T e) { throw new UnsupportedOperationException(); }
	@Override
	public void add(int index, T e) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int index, ObjectList<T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(T[] e, int offset, int length) { throw new UnsupportedOperationException(); }
	@Override
	public void addElements(int from, T[] a, int offset, int length) { throw new UnsupportedOperationException(); }
	
	@Override
	public T[] getElements(int from, T[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(data.length, offset, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	@Override
	public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	@Override
	public <K> K[] extractElements(int from, int to, Class<K> type) { throw new UnsupportedOperationException(); }
	
	
	/**
	 * A function to find if the Element is present in this list.
	 * @param o the element that is searched for
	 * @return if the element was found.
	 */
	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}
	
	/**
	 * A function to find the index of a given element
	 * @param o the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(Object o) {
		if(o == null) {
			for(int i = 0,m=data.length;i<m;i++)
				if(data[i] == null) return i;
			return -1;
		}
		for(int i = 0,m=data.length;i<m;i++) {
			if(Objects.equals(o, data[i])) return i;
		}
		return -1;
	}
	
	/**
	 * A function to find the last index of a given element
	 * @param o the element that is searched for
	 * @return the last index of the element if found. (if not found then -1)
	 */
	@Override
	public int lastIndexOf(Object o) {
		if(o == null) {
			for(int i = data.length - 1;i>=0;i--)
				if(data[i] == null) return i;
			return -1;
		}
		for(int i = data.length - 1;i>=0;i--) {
			if(Objects.equals(o, data[i])) return i;
		}
		return -1;
	}
	
	@Override
	public void sort(Comparator<? super T> c) { throw new UnsupportedOperationException(); }
	@Override
	public void unstableSort(Comparator<? super T> c) { throw new UnsupportedOperationException(); }
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public T get(int index) {
		checkRange(index);
		return data[index];
	}
	
	@Override
	public ImmutableObjectList<T> copy() {
		return new ImmutableObjectList<>(Arrays.copyOf(data, data.length));
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
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.getBoolean(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.getBoolean(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.getBoolean(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public T findFirst(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.getBoolean(data[i])) return data[i];
		}
		return null;
	}
	
	@Override
	public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
		Objects.requireNonNull(operator);
		E state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.apply(state, data[i]);
		}
		return state;
	}

	@Override
	public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
		Objects.requireNonNull(operator);
		T state = null;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.apply(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.getBoolean(data[i])) result++;
		}
		return result;
	}
	
	@Override
	public ObjectListIterator<T> listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new ObjectListIter(index);
	}
	
	@Override
	public T set(int index, T e) { throw new UnsupportedOperationException(); }
	@Override
	public void replaceAll(UnaryOperator<T> o) { throw new UnsupportedOperationException(); }
	@Override
	public T remove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public T swapRemove(int index) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeIf(Predicate<? super T> filter) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) { throw new UnsupportedOperationException(); }
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @return a Array of the elements in the list
	 */
	@Override
	public Object[] toArray() {
		Object[] obj = new Object[data.length];
		for(int i = 0,m=data.length;i<m;i++)
			obj[i] = data[i];
		return obj;
	}
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @param a original array. If null a Object array with the right size is created. If to small the Array of the same type is created with the right size
	 * @return a Array of the elements in the list
	 */
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[data.length];
		else if(a.length < data.length) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), data.length);
		for(int i = 0,m=data.length;i<m;i++)
			a[i] = (E)data[i];
		if (a.length > data.length) a[data.length] = null;
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
	public ObjectSplititerator<T> spliterator() { return ObjectSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	private class ObjectListIter implements ObjectListIterator<T> {
		int index;
		
		ObjectListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return get(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return get(--index);
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
		public void set(T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(T e) { throw new UnsupportedOperationException(); }
		
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