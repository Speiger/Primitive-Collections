package speiger.src.collections.bytes.lists;

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
import java.nio.ByteBuffer;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteStack;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.bytes.utils.ByteIterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Array-based implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link ITrimmable}, {@link ByteStack}
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulkactions.
 */
public class CopyOnWriteByteArrayList extends AbstractByteList implements ITrimmable, ByteStack
{
	/** Access lock */
    transient ReentrantLock lock = new ReentrantLock();
	/** The backing array */
	protected transient byte[] data;
	
	/**
	 * Creates a new ArrayList with a Empty array.
	 */
	public CopyOnWriteByteArrayList() {
		data = ByteArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public CopyOnWriteByteArrayList(Collection<? extends Byte> c) {
		data = new byte[c.size()];
		ByteIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public CopyOnWriteByteArrayList(ByteCollection c) {
		data = new byte[c.size()];
		ByteIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public CopyOnWriteByteArrayList(ByteList l) {
		data = new byte[l.size()];
		l.getElements(0, data, 0, data.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public CopyOnWriteByteArrayList(byte... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public CopyOnWriteByteArrayList(byte[] a, int length) {
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
	public CopyOnWriteByteArrayList(byte[] a, int offset, int length) {
		data = new byte[length];
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		System.arraycopy(a, offset, data, 0, length);
	}
	
	
	private void setArray(byte[] data) {
		this.data = data;
	}
	
	private byte[] getArray() {
		return data;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return true (as specified by {@link Collection#add})
	 */
	@Override
	public boolean add(byte e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] newElements = Arrays.copyOf(data, data.length+1);
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
	public void push(byte e) {
		add(e);
	}
	
	/**
	 * Appends the specified element to the index of the list
	 * @param index the index where to append the element to
	 * @param e the element to append to the list
	 * @throws IndexOutOfBoundsException if index is outside of the lists range
	 */
	@Override
	public void add(int index, byte e) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			byte[] newElements;
			
			if(index == size) {
				newElements = Arrays.copyOf(data, size+1);
			}
			else {
				newElements = new byte[size+1];
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
	public boolean addAll(int index, Collection<? extends Byte> c) {
		if(c instanceof ByteCollection) return addAll(index, (ByteCollection)c);
		int add = c.size();
		if(add <= 0) return false;
		if(c.contains(null)) throw new NullPointerException();
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			byte[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new byte[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			Iterator<? extends Byte> iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.next().byteValue();
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
	public boolean addAll(int index, ByteCollection c) {
		if(c instanceof ByteList) return addAll(index, (ByteList)c);
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			byte[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new byte[size+add];
				System.arraycopy(data, 0, newElements, 0, index);
				System.arraycopy(data, index, newElements, index + add, size - index);
			}
			ByteIterator iter = c.iterator();
			while(add-- != 0) newElements[index++] = iter.nextByte();
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
	public boolean addAll(int index, ByteList c) {
		int add = c.size();
		if(add <= 0) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			int size = data.length;
			if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			byte[] newElements;
			if(index == size) {
				newElements = Arrays.copyOf(data, size+add);
			}
			else {
				newElements = new byte[size+add];
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
	public boolean addAll(byte[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			int size = data.length;
			byte[] newElements = Arrays.copyOf(data, size+length);
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
	public void addElements(int from, byte[] a, int offset, int length) {
		if(length <= 0) return;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			int size = data.length;
			if(from < 0 || from > size) throw new IndexOutOfBoundsException("Index: " + from + ", Size: " + size);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			byte[] newElements;
			if(from == size) {
				newElements = Arrays.copyOf(data, size+length);
			}
			else {
				newElements = new byte[size+length];
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
	public byte[] getElements(int from, byte[] a, int offset, int length) {
		byte[] data = this.data;
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
			byte[] data = this.data;
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length-length;
				byte[] newElements = new byte[size];
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
	public byte[] extractElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return ByteArrays.EMPTY_ARRAY;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte[] a = new byte[length];
			System.arraycopy(data, from, a, 0, length);
			if(to == data.length) this.data = Arrays.copyOf(data, data.length - length);
			else {
				int size = data.length;
				byte[] newElements = new byte[size-length];
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
	public void fillBuffer(ByteBuffer buffer) {
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
		byte[] data = this.data;
		if(o == null) return -1;
		for(int i = 0,m=data.length;i<m;i++) {
			if(Objects.equals(o, Byte.valueOf(data[i]))) return i;
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
		byte[] data = this.data;
		if(o == null) return -1;
		for(int i = data.length - 1;i>=0;i--) {
			if(Objects.equals(o, Byte.valueOf(data[i]))) return i;
		}
		return -1;
	}
	
	/**
	 * A Type Specific implementation of the Collection#contains function.
	 * @param e the element that is searched for.
	 * @return if the element was found
	 */
	@Override
	public boolean contains(byte e) {
		return indexOf(e) != -1;
	}
	
	/**
	 * A Type-Specific function to find the index of a given element
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 */
	@Override
	public int indexOf(byte e) {
		byte[] data = this.data;
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
	public int lastIndexOf(byte e) {
		byte[] data = this.data;
		for(int i = data.length - 1;i>=0;i--) {
			if(data[i] == e) return i;
		}
		return -1;
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(java.util.Comparator)
	 * @see ByteArrays#stableSort(byte[], ByteComparator)
	 */
	@Override
	public void sort(ByteComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] newData = Arrays.copyOf(data, data.length);
			if(c != null) ByteArrays.stableSort(newData, newData.length, c);
			else ByteArrays.stableSort(newData, newData.length);
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
	 * @see ByteArrays#unstableSort(byte[], ByteComparator)
	 */
	@Override
	public void unstableSort(ByteComparator c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] newData = Arrays.copyOf(data, data.length);
			if(c != null) ByteArrays.unstableSort(newData, newData.length, c);
			else ByteArrays.unstableSort(newData, newData.length);
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
	public byte getByte(int index) {
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
	public byte peek(int index) {
		checkRange((size() - 1) - index);
		return data[(size() - 1) - index];
	}
	
	@Override
	public ByteListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new COWIterator(data, index);
	}
	
	@Override
	public ByteList subList(int fromIndex, int toIndex) {
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
	public void forEach(ByteConsumer action) {
		Objects.requireNonNull(action);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(BytePredicate filter) {
		Objects.requireNonNull(filter);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(BytePredicate filter) {
		Objects.requireNonNull(filter);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(BytePredicate filter) {
		Objects.requireNonNull(filter);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public byte findFirst(BytePredicate filter) {
		Objects.requireNonNull(filter);
		byte[] data = this.data;
		for(int i = 0,m=data.length;i<m;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return (byte)0;
	}
	
	@Override
	public byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte[] data = this.data;
		byte state = identity;
		for(int i = 0,m=data.length;i<m;i++) {
			state = operator.applyAsByte(state, data[i]);
		}
		return state;
	}
	
	@Override
	public byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte[] data = this.data;
		byte state = (byte)0;
		boolean empty = true;
		for(int i = 0,m=data.length;i<m;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsByte(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(BytePredicate filter) {
		Objects.requireNonNull(filter);
		byte[] data = this.data;
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
	public byte set(int index, byte e) {
		checkRange(index);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte old = data[index];
			if(old != e) {
				byte[] newElements = Arrays.copyOf(data, data.length);
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
	public void replaceAll(UnaryOperator<Byte> o) {
		Objects.requireNonNull(o);
		replaceBytes(T -> o.apply(Byte.valueOf(SanityChecks.castToByte(T))).byteValue());
	}
	
	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	@Override
	public void replaceBytes(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] newData = Arrays.copyOf(data, data.length);
			for(int i = 0,m=newData.length;i<m;i++)
				newData[i] = SanityChecks.castToByte(o.applyAsInt(newData[i]));
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
	public byte removeByte(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte result = data[index];
			if(index == data.length-1) this.data = Arrays.copyOf(data, data.length-1);
			else {
				int size = data.length-1;
				byte[] newElements = new byte[size];
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
	public byte swapRemove(int index) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte result = data[index];
			byte[] newElements = Arrays.copyOf(data, data.length-1);
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
	public boolean remByte(byte type) {
		int index = indexOf(type);
		if(index == -1) return false;
		removeByte(index);
		return true;
	}
	
	/**
	 * A Type-Specific pop function to reduce (un)boxing
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public byte pop() {
		return removeByte(size() - 1);
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
			byte[] data = this.data;
			byte[] newElements = new byte[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!c.contains(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
			byte[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = ByteArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			byte[] newElements = new byte[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(c.contains(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
	public boolean removeIf(Predicate<? super Byte> filter) {
		Objects.requireNonNull(filter);
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte[] newElements = new byte[data.length];
			int j = 0;
			for(int i= 0,m=data.length;i<m;i++) {
				if(!filter.test(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
	public boolean removeAll(ByteCollection c) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte[] newElements = new byte[data.length];
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
	public boolean removeAll(ByteCollection c, ByteConsumer r) {
		if(c.isEmpty()) return false;
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			byte[] newElements = new byte[data.length];
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
	public boolean retainAll(ByteCollection c) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					this.data = ByteArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			byte[] newElements = new byte[data.length];
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
	public boolean retainAll(ByteCollection c, ByteConsumer r) {
		ReentrantLock lock = this.lock;
		lock.lock();
		try {
			byte[] data = this.data;
			if(c.isEmpty()) {
				if(data.length > 0) {
					forEach(r);
					this.data = ByteArrays.EMPTY_ARRAY;
					return true;
				}
				return false;
			}
			byte[] newElements = new byte[data.length];
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
			byte[] data = this.data;
			byte[] newElements = new byte[data.length];
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
		byte[] data = this.data;
		int size = data.length;
		if(size == 0) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size];
		for(int i = 0;i<size;i++)
			obj[i] = Byte.valueOf(data[i]);
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
		byte[] data = this.data;
		int size = data.length;
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		for(int i = 0;i<size;i++)
			a[i] = (E)Byte.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public byte[] toByteArray(byte[] a) {
		byte[] data = this.data;
		int size = data.length;
		if(a.length < size) a = new byte[size];
		System.arraycopy(data, 0, a, 0, size);
		if (a.length > size) a[size] = (byte)0;
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
			data = ByteArrays.EMPTY_ARRAY;
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
			data = ByteArrays.EMPTY_ARRAY;
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public CopyOnWriteByteArrayList copy() {
		CopyOnWriteByteArrayList list = new CopyOnWriteByteArrayList();
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
	public IntStream primitiveStream() { return StreamSupport.intStream(ByteSplititerators.createArrayJavaSplititerator(data, data.length, 16464), false); }
		/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public ByteSplititerator spliterator() { return ByteSplititerators.createArraySplititerator(data, data.length, 16464); }
	
	static final class COWIterator implements ByteListIterator
	{
		byte[] data;
		int index;
		
		public COWIterator(byte[] data, int index) {
			this.data = data;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < data.length;
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public byte previousByte() {
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
		public void set(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
		
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
	
	private static class COWSubList extends AbstractByteList
	{
		final AbstractByteList list;
		final ReentrantLock lock;
		final Consumer<byte[]> setter;
		final Supplier<byte[]> getter;
		final int parentOffset;
		final int offset;
		int size;
		
		public COWSubList(AbstractByteList list, ReentrantLock lock, Consumer<byte[]> setter, Supplier<byte[]> getter, int offset, int from, int to) {
			this.list = list;
			this.lock = lock;
			this.setter = setter;
			this.getter = getter;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, byte element) {
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
		public boolean addAll(int index, Collection<? extends Byte> c) {
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
		public boolean addAll(int index, ByteCollection c) {
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
		public boolean addAll(int index, ByteList c) {
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
		public void addElements(int from, byte[] a, int offset, int length) {
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
		public byte[] getElements(int from, byte[] a, int offset, int length) {
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
		public byte[] extractElements(int from, int to) {
			lock.lock();
			try {
				checkSubRange(from);
				checkAddSubRange(to);
				byte[] result = list.extractElements(from+parentOffset, to+parentOffset);
				size -= result.length;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public byte getByte(int index) {
			checkSubRange(index);
			return list.getByte(parentOffset+index);
		}

		@Override
		public byte set(int index, byte element) {
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
		public byte swapRemove(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte result = data[offset+index];
				byte[] newElements = new byte[data.length-1];
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
		public byte removeByte(int index) {
			lock.lock();
			try {
				checkSubRange(index);
				byte result = list.removeByte(index+parentOffset);
				size--;
				return result;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public boolean remByte(byte type) {
			int index = indexOf(type);
			if(index == -1) return false;
			removeByte(index);
			return true;
		}
		
		
		@Override
		public void sort(ByteComparator c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newData = Arrays.copyOf(data, data.length);
				if(c != null) ByteArrays.stableSort(newData, offset, offset+size, c);
				else ByteArrays.stableSort(newData, offset, offset+size);
				setter.accept(newData);
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public void unstableSort(ByteComparator c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newData = Arrays.copyOf(data, data.length);
				if(c != null) ByteArrays.unstableSort(newData, offset, offset+size, c);
				else ByteArrays.unstableSort(newData, offset, offset+size);
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
				byte[] data = getter.get();
				byte[] newElements = new byte[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!c.contains(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
				byte[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						clear();
						return true;
					}
					return false;
				}
				byte[] newElements = new byte[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(c.contains(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
		public boolean removeIf(Predicate<? super Byte> filter) {
			Objects.requireNonNull(filter);
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newElements = new byte[data.length];
				int j = 0;
				for(int i= 0,m=data.length;i<m;i++) {
					if(i < offset || i >= offset+size) newElements[j++] = data[i];
					else if(!filter.test(Byte.valueOf(data[i]))) newElements[j++] = data[i];
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
		public boolean removeAll(ByteCollection c) {
			if(c.isEmpty()) return false;
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newElements = new byte[data.length];
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
		public boolean removeAll(ByteCollection c, ByteConsumer r) {
			if(c.isEmpty()) return false;
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newElements = new byte[data.length];
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
		public boolean retainAll(ByteCollection c) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						clear();
						return true;
					}
					return false;
				}
				byte[] newElements = new byte[data.length];
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
		public boolean retainAll(ByteCollection c, ByteConsumer r) {
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				if(c.isEmpty()) {
					if(size > 0) {
						forEach(r);
						clear();
						return true;
					}
					return false;
				}
				byte[] newElements = new byte[data.length];
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
				byte[] data = getter.get();
				byte[] newElements = new byte[data.length];
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
		public void replaceAll(UnaryOperator<Byte> o) {
			Objects.requireNonNull(o);
			replaceBytes(T -> o.apply(Byte.valueOf(SanityChecks.castToByte(T))).byteValue());
		}
		
		@Override
		public void replaceBytes(IntUnaryOperator o) {
			Objects.requireNonNull(o);
			ReentrantLock lock = this.lock;
			lock.lock();
			try {
				byte[] data = getter.get();
				byte[] newData = Arrays.copyOf(data, data.length);
				for(int i = 0,m=size;i<m;i++)
					newData[i+offset] = SanityChecks.castToByte(o.applyAsInt(newData[i+offset]));
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
		public ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public ByteListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new COWSubListIterator(this, index);
		}
		
		@Override
		public ByteList subList(int fromIndex, int toIndex) {
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
	
	private static class COWSubListIterator implements ByteListIterator
	{
		AbstractByteList list;
		int index;
		
		COWSubListIterator(AbstractByteList list, int index) {
			this.list = list;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < list.size();
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			return list.getByte(index++);
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public byte previousByte() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return list.getByte(--index);
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
		public void set(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
		
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