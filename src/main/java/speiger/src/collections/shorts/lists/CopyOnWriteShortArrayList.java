package speiger.src.collections.shorts.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.IntPredicate;import java.util.function.IntUnaryOperator;
import java.nio.ShortBuffer;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortStack;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.shorts.utils.ShortIterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.shorts.collections.ShortSplititerator;
import speiger.src.collections.shorts.utils.ShortSplititerators;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Array-based implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link ITrimmable}, {@link ShortStack}
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulkactions.
 */
public class CopyOnWriteShortArrayList extends AbstractShortList implements ITrimmable, ShortStack
{
	/** Access lock */
    transient ReentrantLock lock = new ReentrantLock();
	/** The backing array */
	protected transient short[] data;
	
	/**
	 * Creates a new ArrayList with a Empty array.
	 */
	public CopyOnWriteShortArrayList() {
		data = ShortArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public CopyOnWriteShortArrayList(Collection<? extends Short> c) {
		data = new short[c.size()];
		ShortIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public CopyOnWriteShortArrayList(ShortCollection c) {
		data = new short[c.size()];
		ShortIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public CopyOnWriteShortArrayList(ShortList l) {
		data = new short[l.size()];
		l.getElements(0, data, 0, data.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public CopyOnWriteShortArrayList(short... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public CopyOnWriteShortArrayList(short[] a, int length) {
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
	public CopyOnWriteShortArrayList(short[] a, int offset, int length) {
		data = new short[length];
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		System.arraycopy(a, offset, data, 0, length);
	}
	
	
	private void setArray(short[] data) {
		this.data = data;
	}
	
	private short[] getArray() {
		return data;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return true (as specified by {@link Collection#add})
	 */
	@Override
	public boolean add(short e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] newElements = Arrays.copyOf(data, data.length+1);
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
	public void push(short e) {
		add(e);
	}
	
	/**
	 * Appends the specified element to the index of the list
	 * @param index the index where to append the element to
	 * @param e the element to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public void add(int index, short e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			short[] newElements;
			
			if(index == size) {
				newElements = Arrays.copyOf(data, size+1);
			}
			else {
				newElements = new short[size+1];
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
	public boolean addAll(int index, Collection<? extends Short> c) {
		if(c instanceof ShortCollection) return addAll(index, (ShortCollection)c);
		int add = c.size();
		if(add <= 0) return false;
		if(c.contains(null)) throw new NullPointerException();
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			short[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new short[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			Iterator<? extends Short> iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.next().shortValue();
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
	public boolean addAll(int index, ShortCollection c) {
		if(c instanceof ShortList) return addAll(index, (ShortList)c);
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			short[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new short[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			ShortIterator iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.nextShort();
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
	public boolean addAll(int index, ShortList c) {
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			short[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new short[size+add];
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
	public boolean addAll(short[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int size = data.length;
			short[] newElements = Arrays.copyOf(data, size+length);
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
	public void addElements(int from, short[] a, int offset, int length) {
		if(length <= 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			int size = data.length;
			if(from < 0 || from > size) throw new IndexOutOfBoundsException("Index: " + from + ", Size: " + size);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			short[] newElements;
			if(from == size) {
				newElements = Arrays.copyOf(data, size+length);
			}
			else {
				newElements = new short[size+length];
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
	public short[] getElements(int from, short[] a, int offset, int length) {
		short[] data = this.data;
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
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length-length;
				short[] newElements = new short[size];
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
	public short[] extractElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return ShortArrays.EMPTY_ARRAY;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short[] a = new short[length];
			System.arraycopy(data, from, a, 0, length);
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length;
				short[] newElements = new short[size-length];
				if(from != 0) System.arraycopy(data, 0, newElements, 0, from);
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
	public void fillBuffer(ShortBuffer buffer) {
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
		short[] data = this.data;
		if(o == null) return -1;
		for(int i = 0,m=data.length;i<m;i++) {
			if(Objects.equals(o, Short.valueOf(data[i]))) return i;
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
		short[] data = this.data;
		if(o == null) return -1;
		for(int i = data.length - 1;i>=0;i--) {
			if(Objects.equals(o, Short.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(short e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(short e) {
		short[] data = this.data;
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
	public int lastIndexOf(short e) {
		short[] data = this.data;
		for(int i = data.length - 1;i>=0;i--) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(java.util.Comparator)
	 * @see ShortArrays#stableSort(short[], ShortComparator)
	 */
	@Override
	public void sort(ShortComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] newData = Arrays.copyOf(data, data.length);
			if(c != null) ShortArrays.stableSort(newData, newData.length, c);
			else ShortArrays.stableSort(newData, newData.length);
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
	 * @see ShortArrays#unstableSort(short[], ShortComparator)
	 */
	@Override
	public void unstableSort(ShortComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] newData = Arrays.copyOf(data, data.length);
			if(c != null) ShortArrays.unstableSort(newData, newData.length, c);
			else ShortArrays.unstableSort(newData, newData.length);
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
	public short getShort(int index) {
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
	public short peek(int index) {
		checkRange((size() - 1) - index);
		return data[(size() - 1) - index];
	}
	
	@Override
	public ShortListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new COWIterator(data, index);
	}
	
	@Override
	public ShortList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(data.length, fromIndex, toIndex-fromIndex);
		return new COWSubList(this, lock, this::setArray, this::getArray, 0, fromIndex, toIndex);
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
	public void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public short findFirst(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		short[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return (short)0;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short[] data = this.data;
		short state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsShort(state, data[i]);
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short[] data = this.data;
		short state = (short)0;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsShort(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		short[] data = this.data;
		int result = 0;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) result++;
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
	public short set(int index, short e) {
		checkRange(index);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short old = data[index];
			if(old != e) {
				short[] newElements = Arrays.copyOf(data, data.length);
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
	public void replaceAll(UnaryOperator<Short> o) {
		Objects.requireNonNull(o);
		replaceShorts(T -> o.apply(Short.valueOf(SanityChecks.castToShort(T))).shortValue());
	}
	
	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	@Override
	public void replaceShorts(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] newData = Arrays.copyOf(data, data.length);
			for(int i = 0,m=newData.length;i<m;i++)
				newData[i] = SanityChecks.castToShort(o.applyAsInt(newData[i]));
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
	public short removeShort(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short result = data[index];
			if(index == data.length-1) this.data = Arrays.copyOf(data, data.length-1);
			else {
				int size = data.length-1;
				short[] newElements = new short[size];
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
	public short swapRemove(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short result = data[index];
			short[] newElements = Arrays.copyOf(data, data.length-1);
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
	public boolean remShort(short type) {
		int index = indexOf(type);
		if(index == -1) return false;
		removeShort(index);
		return true;
	}
	
	/**
	 * A Type-Specific pop function to reduce (un)boxing
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public short pop() {
		return removeShort(size() - 1);
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
			short[] data = this.data;
			short[] newElements = new short[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!c.contains(Short.valueOf(data[i]))) newElements[j++] = data[i];
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
			short[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = ShortArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			short[] newElements = new short[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(c.contains(Short.valueOf(data[i]))) newElements[j++] = data[i];
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
	public boolean removeIf(Predicate<? super Short> filter) {
		Objects.requireNonNull(filter);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short[] newElements = new short[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!filter.test(Short.valueOf(data[i]))) newElements[j++] = data[i];
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
	public boolean removeAll(ShortCollection c) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short[] newElements = new short[data.length];
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
	public boolean removeAll(ShortCollection c, ShortConsumer r) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			short[] newElements = new short[data.length];
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
	public boolean retainAll(ShortCollection c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = ShortArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			short[] newElements = new short[data.length];
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
	public boolean retainAll(ShortCollection c, ShortConsumer r) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			short[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					forEach(r);
					this.data = ShortArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			short[] newElements = new short[data.length];
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
			short[] data = this.data;
			short[] newElements = new short[data.length];
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
		short[] data = this.data;
		int size = data.length;
		if(size == 0) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size];
		for(int i = 0;i<size;i++)
			obj[i] = Short.valueOf(data[i]);
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
		short[] data = this.data;
		int size = data.length;
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		for(int i = 0;i<size;i++)
			a[i] = (E)Short.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public short[] toShortArray(short[] a) {
		short[] data = this.data;
		int size = data.length;
		if(a.length < size) a = new short[size];
		System.arraycopy(data, 0, a, 0, size);
		if (a.length > size) a[size] = (short)0;
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
			data = ShortArrays.EMPTY_ARRAY;
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
			data = ShortArrays.EMPTY_ARRAY;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public CopyOnWriteShortArrayList copy() {
		CopyOnWriteShortArrayList list = new CopyOnWriteShortArrayList();
		list.data = Arrays.copyOf(data, data.length);
		return list;
	}
	
	protected void checkRange(int index) {
		if (index < 0 || index >= data.length)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + data.length);
	}
	
	protected void checkAddRange(int index) {
		if (index < 0 || index > data.length)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + data.length);
	}

	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public IntStream primitiveStream() { return StreamSupport.intStream(ShortSplititerators.createArrayJavaSplititerator(data, data.length, 16464), false); }
		/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public ShortSplititerator spliterator() { return ShortSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	static final class COWIterator implements ShortListIterator
	{
		short[] data;
		int index;
		
		public COWIterator(short[] data, int index) {
			this.data = data;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < data.length;
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return data[--index];
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
		public void set(short e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, data.length - index);
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
	
	private static class COWSubList extends AbstractShortList
	{
		final AbstractShortList list;
		final ReentrantLock lock;
		final Consumer<short[]> setter;
		final Supplier<short[]> getter;
		final int parentOffset;
		final int offset;
		int size;
		
		public COWSubList(AbstractShortList list, ReentrantLock lock, Consumer<short[]> setter, Supplier<short[]> getter, int offset, int from, int to) {
			this.list = list;
			this.lock = lock;
			this.setter = setter;
			this.getter = getter;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, short element) {
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
		public boolean addAll(int index, Collection<? extends Short> c) {
			int add = c.size();
			if(add <= 0) return false;
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
		public boolean addAll(int index, ShortCollection c) {
			int add = c.size();
			if(add <= 0) return false;
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
		public boolean addAll(int index, ShortList c) {
			int add = c.size();
			if(add <= 0) return false;
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
		public void addElements(int from, short[] a, int offset, int length) {
			if(length <= 0) return;
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
		public short[] getElements(int from, short[] a, int offset, int length) {
			SanityChecks.checkArrayCapacity(size, from, length);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			return list.getElements(from+parentOffset, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) {
			if(to-from <= 0) return;
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
		public short[] extractElements(int from, int to) {
			lock.lock();
			try {
				checkSubRange(from);
				checkAddSubRange(to);
				short[] result = list.extractElements(from+parentOffset, to+parentOffset);
				size -= result.length;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public short getShort(int index) {
			checkSubRange(index);
			return list.getShort(parentOffset+index);
		}

		@Override
		public short set(int index, short element) {
			lock.lock();
			try {
				checkSubRange(index);
				return list.set(parentOffset+index, element);
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public short swapRemove(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short result = data[offset+index];
				short[] newElements = new short[data.length-1];
				int end = offset+size-1;
				System.arraycopy(data, 0, newElements, 0, end);
				if(end != newElements.length) System.arraycopy(data, end+1, newElements, end, data.length-end-1);
				if(index+offset != end) newElements[index+offset] = data[end];
				setter.accept(newElements);
				size--;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public short removeShort(int index) {
			lock.lock();
			try {
				checkSubRange(index);
				short result = list.removeShort(index+parentOffset);
				size--;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean remShort(short type) {
			int index = indexOf(type);
			if(index == -1) return false;
			removeShort(index);
			return true;
		}
		
		
		@Override
		public void sort(ShortComparator c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newData = Arrays.copyOf(data, data.length);
				if(c != null) ShortArrays.stableSort(newData, offset, offset+size, c);
				else ShortArrays.stableSort(newData, offset, offset+size);
				setter.accept(newData);
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public void unstableSort(ShortComparator c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newData = Arrays.copyOf(data, data.length);
				if(c != null) ShortArrays.unstableSort(newData, offset, offset+size, c);
				else ShortArrays.unstableSort(newData, offset, offset+size);
				setter.accept(newData);
			}
			finally {
				lock.unlock();
			}	
		}
		
		
		@Override
		@Deprecated
		public boolean removeAll(Collection<?> c) {
			if(c.isEmpty()) return false;
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!c.contains(Short.valueOf(data[i]))) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		@Deprecated
		public boolean retainAll(Collection<?> c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						clear();
						return true;
					}
					return false;
				}
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(c.contains(Short.valueOf(data[i]))) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		@Deprecated
		public boolean removeIf(Predicate<? super Short> filter) {
			Objects.requireNonNull(filter);
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!filter.test(Short.valueOf(data[i]))) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean removeAll(ShortCollection c) {
			if(c.isEmpty()) return false;
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!c.contains(data[i])) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean removeAll(ShortCollection c, ShortConsumer r) {
			if(c.isEmpty()) return false;
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!c.contains(data[i])) newElements[j++] = data[i];
					else r.accept(data[i]);
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean retainAll(ShortCollection c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						clear();
						return true;
					}
					return false;
				}
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(c.contains(data[i])) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));;
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean retainAll(ShortCollection c, ShortConsumer r) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						forEach(r);
						clear();
						return true;
					}
					return false;
				}
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(c.contains(data[i])) newElements[j++] = data[i];
					else r.accept(data[i]);
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean remIf(IntPredicate filter) {
			Objects.requireNonNull(filter);
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newElements = new short[data.length];
				int j = 0;
				for(int i=0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!filter.test(data[i])) newElements[j++] = data[i];
				}
				if(data.length != j) {
					setter.accept(Arrays.copyOf(newElements, j));
					size -= data.length - j;
					return true;
				}
				return false;
			}
			finally {
				lock.unlock();
			}
		}
		
		
		@Override
		@Deprecated
		public void replaceAll(UnaryOperator<Short> o) {
			Objects.requireNonNull(o);
			replaceShorts(T -> o.apply(Short.valueOf(SanityChecks.castToShort(T))).shortValue());
		}
		
		@Override
		public void replaceShorts(IntUnaryOperator o) {
			Objects.requireNonNull(o);
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				short[] data = getter.get();
				short[] newData = Arrays.copyOf(data, data.length);
				for(int i = 0,m=size;i<m;i++)
					newData[i+offset] = SanityChecks.castToShort(o.applyAsInt(newData[i+offset]));
				setter.accept(newData);
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
			lock.lock();
			try {
				list.removeElements(parentOffset, parentOffset+size);
				size = 0;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public ShortSplititerator spliterator() { return ShortSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public ShortListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new COWSubListIterator(this, index);
		}
		
		@Override
		public ShortList subList(int fromIndex, int toIndex) {
			SanityChecks.checkArrayCapacity(size, fromIndex, toIndex-fromIndex);
			return new COWSubList(this, lock, setter, getter, offset, fromIndex, toIndex);
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
	
	private static class COWSubListIterator implements ShortListIterator
	{
		AbstractShortList list;
		int index;
		
		COWSubListIterator(AbstractShortList list, int index) {
			this.list = list;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < list.size();
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return list.getShort(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return list.getShort(--index);
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
		public void set(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, list.size() - index);
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