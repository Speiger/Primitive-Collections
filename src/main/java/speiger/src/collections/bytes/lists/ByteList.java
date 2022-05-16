package speiger.src.collections.bytes.lists;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.Comparator;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.bytes.utils.ByteLists;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific List interface that reduces boxing/unboxing and adds a couple extra quality of life features
 */
public interface ByteList extends ByteCollection, List<Byte>
{
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @return true if the list was modified
	 * @see List#add(Object)
	 */
	@Override
	public boolean add(byte e);
	
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @param index index at which the specified element is to be inserted
	 * @see List#add(int, Object)
	 */
	public void add(int index, byte e);
	
	/**
	 * A Helper function that will only add elements if it is not present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfAbsent(byte e) {
		if(indexOf(e) == -1) return add(e);
		return false;
	}
	
	/**
	 * A Helper function that will only add elements if it is present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfPresent(byte e) {
		if(indexOf(e) != -1) return add(e);
		return false;
	}
	
	/**
	 * A Type-Specific addAll Function to reduce (un)boxing
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, ByteCollection c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @return true if the list was modified
	 */
	public boolean addAll(ByteList c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 */
	public boolean addAll(int index, ByteList c);
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the value that is requested
	 * @return the value at the given index
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#get(int)
	 */
	public byte getByte(int index);
	
	/**
	 * A Type-Specific set function to reduce (un)boxing
	 * @param index index of the element to replace
	 * @param e element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#set(int, Object)
	 */
	public byte set(int index, byte e);
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @see List#remove(int)
	 */
	public byte removeByte(int index);
	
	/**
	 * A Type-Specific indexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int indexOf(byte e);
	
	/**
	 * A Type-Specific lastIndexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the lastIndex of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int lastIndexOf(byte e);

	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	public default void replaceBytes(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		ByteListIterator iter = listIterator();
		while (iter.hasNext())
		iter.set(SanityChecks.castToByte(o.applyAsInt(iter.nextByte())));
	}
	
	/**
	 * A function to fast add elements to the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(byte... a) { addElements(size(), a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(int from, byte... a) { addElements(from, a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @param offset the start index of the array should be read from
	 * @param length how many elements should be read from
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public void addElements(int from, byte[] a, int offset, int length);
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	public default byte[] getElements(int from, byte[] a) { return getElements(from, a, 0, a.length); }
	
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
	public byte[] getElements(int from, byte[] a, int offset, int length);
	
	/**
	 * a function to fast remove elements from the list.
	 * @param from the start index of where the elements should be removed from (inclusive)
	 * @param to the end index of where the elements should be removed to (exclusive)
	 */
	public void removeElements(int from, int to);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public byte swapRemove(int index);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param e the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean swapRemoveByte(byte e);
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	public byte[] extractElements(int from, int to);
	
	/**
	 * Helper function that allows to fastFill a buffer reducing the duplication requirement
	 * @param buffer where the data should be stored in.
	 */
	public default void fillBuffer(ByteBuffer buffer) { buffer.put(toByteArray()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default void sort(Comparator<? super Byte> c) {
		sort((K, V) -> c.compare(Byte.valueOf(K), Byte.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see ByteArrays#stableSort(byte[], ByteComparator)
	 */
	public default void sort(ByteComparator c) {
		byte[] array = toByteArray();
		if(c != null) ByteArrays.stableSort(array, c);
		else ByteArrays.stableSort(array);
		ByteListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextByte();
			iter.set(array[i]);
		}
	}
	
	/** 
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @param c the sorter of the elements, can be null
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Deprecated
	public default void unstableSort(Comparator<? super Byte> c) {
		unstableSort((K, V) -> c.compare(Byte.valueOf(K), Byte.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see ByteArrays#unstableSort(byte[], ByteComparator)
	 */
	public default void unstableSort(ByteComparator c) {
		byte[] array = toByteArray();
		if(c != null) ByteArrays.unstableSort(array, c);
		else ByteArrays.unstableSort(array);
		ByteListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextByte();
			iter.set(array[i]);
		}
	}
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator
	 */
	@Override
	public ByteListIterator listIterator();
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ByteListIterator listIterator(int index);
	
	/**
	 * A Type-Specific List of subList
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public ByteList subList(int from, int to);
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @return a new List that is synchronized
	 * @see ByteLists#synchronize
	 */
	public default ByteList synchronize() { return ByteLists.synchronize(this); }
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new List Wrapper that is synchronized
	 * @see ByteLists#synchronize
	 */
	public default ByteList synchronize(Object mutex) { return ByteLists.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped List that is unmodifiable
	 * @return a new List Wrapper that is unmodifiable
	 * @see ByteLists#unmodifiable
	 */
	public default ByteList unmodifiable() { return ByteLists.unmodifiable(this); }
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	public void size(int size);
	
	@Override
	public ByteList copy();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Byte e) {
		return ByteCollection.super.add(e);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Byte get(int index) {
		return Byte.valueOf(getByte(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Byte set(int index, Byte e) {
		return Byte.valueOf(set(index, e.byteValue()));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int indexOf(Object o) {
		return indexOf(((Byte)o).byteValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int lastIndexOf(Object o) {
		return lastIndexOf(((Byte)o).byteValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return ByteCollection.super.contains(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return ByteCollection.super.remove(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Byte remove(int index) {
		return Byte.valueOf(removeByte(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void replaceAll(UnaryOperator<Byte> o) {
		Objects.requireNonNull(o);
		replaceBytes(T -> o.apply(Byte.valueOf((byte)T)).byteValue());
	}
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 0); }
}