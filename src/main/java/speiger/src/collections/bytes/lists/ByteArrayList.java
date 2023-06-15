package speiger.src.collections.bytes.lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
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
import speiger.src.collections.bytes.utils.IByteArray;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific Array-based implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link IByteArray}, {@link ByteStack}
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulkactions.
 * 
 */
public class ByteArrayList extends AbstractByteList implements IByteArray, ByteStack
{
	static final int DEFAULT_ARRAY_SIZE = 10;
	
	/** The backing array */
	protected transient byte[] data;
	/** The current size of the elements stored in the backing array */
	protected int size = 0;
	
	/**
	 * Creates a new ArrayList with a Empty array.
	 */
	public ByteArrayList() {
		data = ByteArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Creates a new ArrayList with the specific requested size
	 * @param size the minimum initial size of the Backing array
	 */
	public ByteArrayList(int size) {
		if(size < 0) throw new IllegalStateException("Size has to be 0 or greater");
		data = new byte[size];
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ByteArrayList(Collection<? extends Byte> c) {
		this(c.size());
		size = ByteIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ByteArrayList(ByteCollection c) {
		this(c.size());
		size = ByteIterators.unwrap(data, c.iterator());
	}
	
	/**
	 * Creates a new ArrayList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ByteArrayList(ByteList l) {
		this(l.size());
		size = l.size();
		l.getElements(0, data, 0, size);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public ByteArrayList(byte... a) {
		this(a, 0, a.length);
	}
	
	/**
	 * Creates a new ArrayList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ByteArrayList(byte[] a, int length) {
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
	public ByteArrayList(byte[] a, int offset, int length) {
		this(length);
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		System.arraycopy(a, offset, data, 0, length);
		size = length;
	}
	
	/**
	 * Creates a wrapped arraylist that uses the array as backing array
	 * @param a elements that should be wrapped
 	 * @return a Wrapped list using the input array
	 */
	public static ByteArrayList wrap(byte... a) {
		return wrap(a, a.length);
	}
	
	/**
	 * Creates a wrapped arraylist that uses the array as backing array and a custom fill size
	 * @param a elements that should be wrapped
	 * @param length the size of the elements within the array
 	 * @return a Wrapped list using the input array
	 */
	public static ByteArrayList wrap(byte[] a, int length) {
		SanityChecks.checkArrayCapacity(a.length, 0, length);
		ByteArrayList list = new ByteArrayList();
		list.data = a;
		list.size = length;
		return list;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return true (as specified by {@link Collection#add})
	 */
	@Override
	public boolean add(byte e) {
		grow(size + 1);
		data[size++] = e;
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
		checkAddRange(index);
		grow(size + 1);
		if(index != size) System.arraycopy(data, index, data, index+1, size - index);
		data[index] = e;
		size++;
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
		grow(size + add);
		if(index != size) System.arraycopy(data, index, data, index+add, size - index);
		size+=add;
		Iterator<? extends Byte> iter = c.iterator();
		while(add-- != 0) data[index++] = iter.next().byteValue();
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
		grow(size + add);
		if(index != size) System.arraycopy(data, index, data, index+add, size - index);
		size+=add;
		ByteIterator iter = c.iterator();
		while(add-- != 0) data[index++] = iter.nextByte();
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
		checkAddRange(index);
		grow(size + add);
		if(index != size) System.arraycopy(data, index, data, index+add, size - index);
		size+=add;
		c.getElements(0, data, index, c.size());
		return true;
	}
	
	@Override
	public boolean addAll(byte[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		grow(size + length);
		System.arraycopy(e, offset, data, size, length);
		size+=length;
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
		checkAddRange(from);
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		grow(size + length);
		if(from != size) System.arraycopy(data, from, data, from+length, size - from);
		size+=length;
		System.arraycopy(a, offset, data, from, length);
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
		SanityChecks.checkArrayCapacity(size, from, length);
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
		if(to != size) System.arraycopy(data, to, data, from, size - to);
		size -= length;
	}
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	@Override
	public byte[] extractElements(int from, int to) {
		int length = to - from;
		if(length <= 0) return ByteArrays.EMPTY_ARRAY;
		byte[] a = new byte[length];
		System.arraycopy(data, from, a, 0, length);
		if(to != size) System.arraycopy(data, to, data, from, size - to);
		size -= length;		
		return a;
	}

	@Override
	public void fillBuffer(ByteBuffer buffer) {
		buffer.put(data, 0, size);
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
		if(o == null) return -1;
		for(int i = 0;i<size;i++) {
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
		if(o == null) return -1;
		for(int i = size - 1;i>=0;i--) {
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
		for(int i = 0;i<size;i++) {
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
		for(int i = size - 1;i>=0;i--) {
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
		if(c != null) ByteArrays.stableSort(data, size, c);
		else ByteArrays.stableSort(data, size);
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(java.util.Comparator)
	 * @see ByteArrays#unstableSort(byte[], ByteComparator)
	 */
	@Override
	public void unstableSort(ByteComparator c) {
		if(c != null) ByteArrays.unstableSort(data, size, c);
		else ByteArrays.unstableSort(data, size);		
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
	
	/**
	 * Provides the Underlying Array in the Implementation
	 * @return underlying Array
	 * @throws ClassCastException if the return type does not match the underlying array. (Only for Object Implementations)
	 */
	@Override
	public byte[] elements() {
		return data;
	}
	
	/**
	 * A Type Specific foreach function that reduces (un)boxing
	 * 
	 * @implSpec
	 * <p>The default implementation behaves as if:
	 * <pre>{@code
	 * 	for(int i = 0;i<size;i++)
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
		for(int i = 0;i<size;i++)
			action.accept(data[i]);
	}
	
	@Override
	public <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.test(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public byte findFirst(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(data[i])) return data[i];
		}
		return (byte)0;
	}
	
	@Override
	public byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsByte(state, data[i]);
		}
		return state;
	}
	
	@Override
	public byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = (byte)0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
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
		int result = 0;
		for(int i = 0;i<size;i++) {
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
		byte old = data[index];
		data[index] = e;
		return old;
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
		for(int i = 0;i<size;i++)
			data[i] = SanityChecks.castToByte(o.applyAsInt(data[i]));
	}
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to fetch
	 * @return the value of the requested index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public byte removeByte(int index) {
		checkRange(index);
		byte old = data[index];
		size--;
		if(index != size) System.arraycopy(data, index+1, data, index, size - index);
		return old;
	}
	
	public byte swapRemove(int index) {
		checkRange(index);
		byte old = data[index];
		size--;
		data[index] = data[size];
		return old;
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
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(Byte.valueOf(data[i]))) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
	}
	
	/**
	 * A function to retain all elements that were provided in the other collection
	 * This function might delegate to a more appropriate function if necessary
	 * @param c the elements that should be kept. If empty, ByteArrayList#clear is called.
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		if(c.isEmpty()) {
			boolean modifed = size > 0;
			clear();
			return modifed;
		}
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(Byte.valueOf(data[i]))) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
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
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!filter.test(Byte.valueOf(data[i]))) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
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
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean removeAll(ByteCollection c, ByteConsumer r) {
		if(c.isEmpty()) return false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean modified = j != size;
		size = j;
		return modified;
	}
	
	/**
	 * A function to retain all elements that were provided in the other collection
	 * This function might delegate to a more appropriate function if necessary
	 * @param c the elements that should be kept. If empty, ByteArrayList#clear is called.
	 * @return true if the collection was modified
	 * @throws NullPointerException if the collection is null
	 */
	@Override
	public boolean retainAll(ByteCollection c) {
		if(c.isEmpty()) {
			boolean modifed = size > 0;
			clear();
			return modifed;
		}
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean retainAll(ByteCollection c, ByteConsumer r) {
		if(c.isEmpty()) {
			boolean modifed = size > 0;
			forEach(r);
			clear();
			return modifed;
		}
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean modified = j != size;
		size = j;
		return modified;
	}
	
	/**
	 * A optimized List#removeIf(Predicate) that more quickly removes elements from the list then the ArrayList implementation
	 * @param filter the filter to remove elements
	 * @return true if the list was modified
	 */
	@Override
	public boolean remIf(IntPredicate filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!filter.test(data[i])) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
	}
	
	/**
	 * A toArray implementation that ensures the Array itself is a Object.
	 * @return a Array of the elements in the list
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
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
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		for(int i = 0;i<size;i++)
			a[i] = (E)Byte.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public byte[] toByteArray(byte[] a) {
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
		return size;
	}
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	@Override
	public void size(int size) {
		if(size > data.length)
			data = Arrays.copyOf(data, size);
		else if(size < size() && size >= 0)
			Arrays.fill(data, size, size(), (byte)0);
		this.size = size;
	}
	
	/**
	 * A function to clear all elements in the list.
	 */
	@Override
	public void clear() {
		size = 0;
	}
	
	/**
	 * Trims the original collection down to the size of the current elements or the requested size depending which is bigger
	 * @param size the requested trim size.
	 */
	@Override
	public boolean trim(int size) {
		if(size > size() || size() == data.length) return false;
		int value = Math.max(size, size());
		data = value == 0 ? ByteArrays.EMPTY_ARRAY : Arrays.copyOf(data, value);
		return true;
	}
	
	/**
	 * Trims the collection down to the requested size and clears all elements while doing so
	 * @param size the amount of elements that should be allowed
	 * @note this will enforce minimum size of the collection itself
	 */
	@Override
	public void clearAndTrim(int size) {
		if(data.length <= size) {
			clear();
			return;
		}
		data = size == 0 ? ByteArrays.EMPTY_ARRAY : new byte[size];
		this.size = size;
	}
	
	/**
	 * Increases the capacity of this implementation instance, if necessary,
	 * to ensure that it can hold at least the number of elements specified by
	 * the minimum capacity argument.
	 *
	 * @param size the desired minimum capacity
	 */
	@Override
	public void ensureCapacity(int size) {
		grow(size);
	}
	
	@Override
	public ByteArrayList copy() {
		ByteArrayList list = new ByteArrayList();
		list.data = Arrays.copyOf(data, data.length);
		list.size = size;
		return list;
	}
	
	protected void grow(int capacity) {
		if(capacity <= data.length) return;
		data = Arrays.copyOf(data, data == ByteArrays.EMPTY_ARRAY ? Math.max(DEFAULT_ARRAY_SIZE, capacity) : (int)Math.max(Math.min((long)data.length + (data.length >> 1), SanityChecks.MAX_ARRAY_SIZE), capacity));		
	}
	
	protected void checkRange(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}
	
	protected void checkAddRange(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public IntStream primitiveStream() { return StreamSupport.intStream(ByteSplititerators.createArrayJavaSplititerator(data, size, 16464), false); }
		/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 * @note characteristics are ordered, sized, subsized
	 */
	@Override
	public ByteSplititerator spliterator() { return ByteSplititerators.createArraySplititerator(data, size, 16464); }
}