package speiger.src.collections.ints.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.nio.IntBuffer;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntStack;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.ints.utils.IntIterators;
import speiger.src.collections.objects.utils.ObjectArrays;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.utils.IntSplititerators;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Array-based implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link ITrimmable}, {@link IntStack}
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulkactions.
 */
public class CopyOnWriteIntArrayList extends AbstractIntList implements ITrimmable, IntStack
{
	/** Access lock */
    transient ReentrantLock lock = new ReentrantLock();
	/** The backing array */
	protected transient int[] data;
	
	/**
	 * Creates a new ArrayList with a Empty array.
	 */
	public CopyOnWriteIntArrayList() {
		data = IntArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public CopyOnWriteIntArrayList(Collection<? extends Integer> c) {
		data = new int[c.size()];
		IntIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public CopyOnWriteIntArrayList(IntCollection c) {
		data = new int[c.size()];
		IntIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public CopyOnWriteIntArrayList(IntList l) {
		data = new int[l.size()];
		l.getElements(0, data, 0, data.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public CopyOnWriteIntArrayList(int... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public CopyOnWriteIntArrayList(int[] a, int length) {
		this(a, 0, length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array with in the custom range.
	 * @param a the array that should be copied
	 * @param offset the starting offset of where the array should be copied from
	 * @param length the desired length that should be copied
	 * @throws IllegalStateException if offset is smaller then 0
	 * @throws IllegalStateException if the offset + length exceeds the array length
	 */
	public CopyOnWriteIntArrayList(int[] a, int offset, int length) {
		data = new int[length];
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		System.arraycopy(a, offset, data, 0, length);
	}
	
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return true (as specified by {@link Collection#add})
	 */
	@Override
	public boolean add(int e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] newElements = Arrays.copyOf(data, data.length+1);
			newElements[newElements.length-1] = e;
			data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
		return true;
	}
	
	/**
	 * Appends the specified element to the end of this Stack.
	 * @param e element to be appended to this Stack
	 */
	@Override
	public void push(int e) {
		add(e);
	}
	
	/**
	 * Appends the specified element to the index of the list
	 * @param index the index where to append the element to
	 * @param e the element to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public void add(int index, int e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			int[] newElements;
			
			if(index == size) {
				newElements = Arrays.copyOf(data, size+1);
			}
			else {
				newElements = new int[size+1];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + 1, size - index);
			}
			newElements[index] = e;
			this.data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
	}
	
	/**
	 * Appends the specified elements to the index of the list.
	 * This function may delegate to more appropriate function if necessary
	 * @param index the index where to append the elements to
	 * @param c the elements to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 * @throws NullPointerException if collection contains a null element
	 */
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Integer> c) {
		if(c instanceof IntCollection) return addAll(index, (IntCollection)c);
		int add = c.size();
		if(add <= 0) return false;
		if(c.contains(null)) throw new NullPointerException();
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			int[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new int[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			Iterator<? extends Integer> iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.next().intValue();
			this.data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
		return true;
	}
	
	/**
	 * Appends the specified elements to the index of the list.
	 * This function may delegate to more appropriate function if necessary
	 * @param index the index where to append the elements to
	 * @param c the elements to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public boolean addAll(int index, IntCollection c) {
		if(c instanceof IntList) return addAll(index, (IntList)c);
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			int[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new int[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			IntIterator iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.nextInt();
			this.data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
		return true;
	}
	
	/**
	 * Appends the specified elements to the index of the list.
	 * @param index the index where to append the elements to
	 * @param c the elements to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public boolean addAll(int index, IntList c) {
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			int[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new int[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			c.getElements(0, newElements, index, c.size());
			this.data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
		return true;
	}
	
	@Override
	public boolean addAll(int[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int size = data.length;
			int[] newElements = Arrays.copyOf(data, size+length);
			System.arraycopy(e, offset, newElements, size, length);
			data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
		return true;
	}
	
	/**
	 * Appends the specified array elements to the index of the list.
	 * @param from the index where to append the elements to
	 * @param a the elements to append to the list
	 * @param offset where to start ino the array
	 * @param length the amount of elements to insert
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public void addElements(int from, int[] a, int offset, int length) {
		if(length <= 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int size = data.length;
			if(from < 0 || from > size) throw new IndexOutOfBoundsException("Index: " + from + ", Size: " + size);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			int[] newElements;
			if(from == size) {
				newElements = Arrays.copyOf(data, size+length);
			}
			else {
				newElements = new int[size+length];
				System.arraycopy(data, 0, newElements, 0, from);
				System.arraycopy(data, from, newElements, from + length, size - from);
			}
			System.arraycopy(a, offset, newElements, from, length);
			this.data = newElements;
		}
		finally { 
			lock.unlock(); 
		}
	}
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @param offset the startIndex of where the array should be written to
	 * @param length the number of elements the values should be fetched from
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	@Override
	public int[] getElements(int from, int[] a, int offset, int length) {
		int[] data = this.data;
		SanityChecks.checkArrayCapacity(data.length, from, length);
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		System.arraycopy(data, from, a, offset, length);
		return a;
	}
	
	/**
	 * a function to fast remove elements from the list.
	 * @param from the start index of where the elements should be removed from (inclusive)
	 * @param to the end index of where the elements should be removed to (exclusive)
	 */
	@Override
	public void removeElements(int from, int to) {
		int length = to - from;
		if(length <= 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length-length;
				int[] newElements = new int[size];
				System.arraycopy(data, 0, newElements, 0, from);
				System.arraycopy(data, to, newElements, from, data.length - to);
				this.data = newElements;
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	@Override
	public int[] extractElements(int from, int to) {
		int length = to - from;
		if(length <= 0) return IntArrays.EMPTY_ARRAY;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] a = new int[length];
			System.arraycopy(data, from, a, 0, length);
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length-1;
				int[] newElements = new int[size];
				System.arraycopy(data, 0, newElements, 0, from);
				System.arraycopy(data, to, newElements, from, size - to);
				this.data = newElements;
			}
			return a;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public void fillBuffer(IntBuffer buffer) {
		buffer.put(data, 0, data.length);
	}
	
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
		int[] data = this.data;
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
		int[] data = this.data;
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
		int[] data = this.data;
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
		int[] data = this.data;
		for(int i = data.length - 1;i>=0;i--) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(java.util.Comparator)
	 * @see IntArrays#stableSort(int[], IntComparator)
	 */
	@Override
	public void sort(IntComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] newData = Arrays.copyOf(data, data.length);
			if(c != null) IntArrays.stableSort(newData, newData.length, c);
			else IntArrays.stableSort(newData, newData.length);
			data = newData;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(java.util.Comparator)
	 * @see IntArrays#unstableSort(int[], IntComparator)
	 */
	@Override
	public void unstableSort(IntComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] newData = Arrays.copyOf(data, data.length);
			if(c != null) IntArrays.unstableSort(newData, newData.length, c);
			else IntArrays.unstableSort(newData, newData.length);
			data = newData;
		}
		finally {
			lock.unlock();
		}	
	}
	
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
	
	/**
	 * Provides the Selected Object from the stack.
	 * Top to bottom
	 * @param index of the element that should be provided
	 * @return the element that was requested
	 * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
	 * @see speiger.src.collections.utils.Stack#peek(int)
	 */
	@Override
	public int peek(int index) {
		checkRange((size() - 1) - index);
		return data[(size() - 1) - index];
	}
	
	@Override
	public IntListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new COWIterator(data, index);
	}
	
	@Override
	public IntList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(data.length, fromIndex, toIndex-fromIndex);
		return new COWSubList(this, 0, fromIndex, toIndex);
	}
	
	/**
	 * A Type Specific foreach function that reduces (un)boxing
	 * 
	 * @implSpec
	 * <p>The default implementation behaves as if:
	 * <pre>{@code
	 * 	for(int i = 0,m=data.length;i<m;i++)
	 *		action.accept(data[i]);
	 * }</pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 * @see Iterable#forEach(java.util.function.Consumer)
	 */
	@Override
	public void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectIntConsumer<E> action) {
		Objects.requireNonNull(action);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public int findFirst(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) return data[i];
		}
		return 0;
	}
	
	@Override
	public int reduce(int identity, IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int[] data = this.data;
		int state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsInt(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int reduce(IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int[] data = this.data;
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
		int[] data = this.data;
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.get(data[i])) result++;
		}
		return result;
	}
	
	/**
	 * A Type-Specific set function to reduce (un)boxing
	 * @param index the index of the element to set
	 * @param e the value that should be set
	 * @return the previous element
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public int set(int index, int e) {
		checkRange(index);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int old = data[index];
			if(old != e) {
				int[] newElements = Arrays.copyOf(data, data.length);
				newElements[index] = e;
				data = newElements;
			}
			return old;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to replace all values in the list
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Integer> o) {
		Objects.requireNonNull(o);
		replaceInts(T -> o.apply(Integer.valueOf(T)).intValue());
	}
	
	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	@Override
	public void replaceInts(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] newData = Arrays.copyOf(data, data.length);
			for(int i = 0,m=newData.length;i<m;i++)
				newData[i] = o.applyAsInt(newData[i]);
			data = newData;
		}
		finally {
			lock.unlock();
		}
	}
	
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public int removeInt(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int result = data[index];
			if(index == data.length-1) this.data = Arrays.copyOf(data, data.length-1);
			else {
				int size = data.length-1;
				int[] newElements = new int[size];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index + 1, newElements, index, size - index);
				this.data = newElements;
			}
			return result;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public int swapRemove(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int result = data[index];
			int[] newElements = Arrays.copyOf(data, data.length-1);
			if(index != newElements.length) newElements[index] = data[data.length-1];
			this.data = newElements;
			return result;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A Type-Specific implementation of remove. This implementation iterates over the elements until it finds the element that is searched for or it runs out of elements.
	 * It stops after finding the first element
	 * @param type the element that is searched for
	 * @return true if the element was found and removed.
	 */
	@Override
	public boolean remInt(int type) {
		int index = indexOf(type);
		if(index == -1) return false;
		removeInt(index);
		return true;
	}
	
	/**
	 * A Type-Specific pop function to reduce (un)boxing
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public int pop() {
		return removeInt(size() - 1);
	}
	
	/**
	 * A function to remove all elements that were provided in the other collection
	 * This function might delegate to a more appropriate function if necessary
	 * @param c the elements that should be removed
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!c.contains(Integer.valueOf(data[i]))) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to retain all elements that were provided in the other collection
	 * This function might delegate to a more appropriate function if necessary
	 * @param c the elements that should be kept. If empty, LongArrayList#clear is called.
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = IntArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(c.contains(Integer.valueOf(data[i]))) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A optimized List#removeIf(Predicate) that more quickly removes elements from the list then the ArrayList implementation
	 * @param filter the filter to remove elements
	 * @return true if the list was modified
	 */
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Integer> filter) {
		Objects.requireNonNull(filter);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!filter.test(Integer.valueOf(data[i]))) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to remove all elements that were provided in the other collection
	 * @param c the elements that should be removed
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	public boolean removeAll(IntCollection c) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!c.contains(data[i])) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public boolean removeAll(IntCollection c, IntConsumer r) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!c.contains(data[i])) newElements[j++] = data[i];
				else r.accept(data[i]);
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to retain all elements that were provided in the other collection
	 * This function might delegate to a more appropriate function if necessary
	 * @param c the elements that should be kept. If empty, LongArrayList#clear is called.
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	public boolean retainAll(IntCollection c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = IntArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(c.contains(data[i])) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public boolean retainAll(IntCollection c, IntConsumer r) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					forEach(r);
					this.data = IntArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(c.contains(data[i])) newElements[j++] = data[i];
				else r.accept(data[i]);
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A optimized List#removeIf(Predicate) that more quickly removes elements from the list then the ArrayList implementation
	 * @param filter the filter to remove elements
	 * @return true if the list was modified
	 */
	@Override
	public boolean remIf(IntPredicate filter) {
		Objects.requireNonNull(filter);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int[] data = this.data;
			int[] newElements = new int[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!filter.test(data[i])) newElements[j++] = data[i];
			}
			if(data.length != j) {
				this.data = Arrays.copyOf(newElements, j);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @return a Array of the elements in the list
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		int[] data = this.data;
		int size = data.length;
		Object[] obj = new Object[size];
		for(int i = 0;i<size;i++)
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
		int[] data = this.data;
		int size = data.length;
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		for(int i = 0;i<size;i++)
			a[i] = (E)Integer.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public int[] toIntArray(int[] a) {
		int[] data = this.data;
		int size = data.length;
		if(a.length < size) a = new int[size];
		System.arraycopy(data, 0, a, 0, size);
		if (a.length > size) a[size] = 0;
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
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	@Override
	public void size(int size) {
		if(size == data.length || size < 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			data = Arrays.copyOf(data, size);
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * A function to clear all elements in the list.
	 */
	@Override
	public void clear() {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			data = IntArrays.EMPTY_ARRAY;
		}
		finally {
			lock.unlock();
		}
	}
	
	/**
	 * Trims the original collection down to the size of the current elements or the requested size depending which is bigger
	 * @param size the requested trim size.
	 */
	@Override
	public boolean trim(int size) {
		return false;
	}
	
	/**
	 * Trims the collection down to the requested size and clears all elements while doing so
	 * @param size the amount of elements that should be allowed
	 * @note this will enforce minimum size of the collection itself
	 */
	@Override
	public void clearAndTrim(int size) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			data = IntArrays.EMPTY_ARRAY;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public CopyOnWriteIntArrayList copy() {
		CopyOnWriteIntArrayList list = new CopyOnWriteIntArrayList();
		list.data = Arrays.copyOf(data, data.length);
		return list;
	}
	
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
	
	static final class COWIterator implements IntListIterator
	{
		int[] data;
		int index;
		
		public COWIterator(int[] data, int index) {
			this.data = data;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < data.length;
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return data[index--];
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
			int steps = Math.min(amount, (data.length - 1) - index);
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
	
	private static class COWSubList extends AbstractIntList
	{
		final CopyOnWriteIntArrayList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public COWSubList(CopyOnWriteIntArrayList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, int element) {
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkAddSubRange(index);
				list.add(parentOffset+index, element);
				size++;				
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) {
			int add = c.size();
			if(add <= 0) return false;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkAddSubRange(index);
				list.addAll(parentOffset+index, c);
				this.size += add;
			}
			finally {
				lock.unlock();
			}
			return true;
		}
		
		@Override
		public boolean addAll(int index, IntCollection c) {
			int add = c.size();
			if(add <= 0) return false;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkAddSubRange(index);
				list.addAll(parentOffset+index, c);
				this.size += add;
			}
			finally {
				lock.unlock();
			}
			return true;
		}

		@Override
		public boolean addAll(int index, IntList c) {
			int add = c.size();
			if(add <= 0) return false;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkAddSubRange(index);
				list.addAll(parentOffset+index, c);
				this.size += add;
			}
			finally {
				lock.unlock();
			}
			return true;
		}
		
		@Override
		public void addElements(int from, int[] a, int offset, int length) {
			if(length <= 0) return;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkAddSubRange(from);
				list.addElements(parentOffset+from, a, offset, length);
				this.size += length;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) {
			SanityChecks.checkArrayCapacity(size, from, length);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			return list.getElements(from+this.offset, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) {
			if(to-from <= 0) return;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkSubRange(from);
				checkAddSubRange(to);
				list.removeElements(from+parentOffset, to+parentOffset);
				size -= to - from;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int[] extractElements(int from, int to) {
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkSubRange(from);
				checkAddSubRange(to);
				int[] result = list.extractElements(from+parentOffset, to+parentOffset);
				size -= result.length;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int getInt(int index) {
			checkSubRange(index);
			return list.getInt(offset+index);
		}

		@Override
		public int set(int index, int element) {
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkSubRange(index);
				return list.set(offset+index, element);
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int swapRemove(int index) {
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkSubRange(index);
				int result = list.swapRemove(index+parentOffset);
				size--;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int removeInt(int index) {
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				checkSubRange(index);
				int result = list.removeInt(index+parentOffset);
				size--;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public void clear() {
			if(size == 0) return;
			ReentrantLock lock = list.lock;
			lock.lock();
			try {
				list.removeElements(offset, offset+size);
				size = 0;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public IntSplititerator spliterator() { return IntSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public IntListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new COWSubListIterator(this, index);
		}
		
		@Override
		public IntList subList(int fromIndex, int toIndex) {
			SanityChecks.checkArrayCapacity(size, fromIndex, toIndex-fromIndex);
			return new COWSubList(list, offset, fromIndex, toIndex);
		}
		
		protected void checkSubRange(int index) {
			if (index < 0 || index >= size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		protected void checkAddSubRange(int index) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}
	
	private static class COWSubListIterator implements IntListIterator
	{
		AbstractIntList list;
		int index;
		
		COWSubListIterator(AbstractIntList list, int index) {
			this.list = list;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < list.size();
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return list.getInt(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return list.getInt(--index);
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
			int steps = Math.min(amount, (list.size() - 1) - index);
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