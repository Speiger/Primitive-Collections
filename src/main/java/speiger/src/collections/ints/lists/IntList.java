package speiger.src.collections.ints.lists;

import java.nio.IntBuffer;
import java.util.List;
import java.util.function.IntUnaryOperator;

import java.util.Objects;
import java.util.Comparator;
import java.util.function.UnaryOperator;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.collections.ints.utils.IntSplititerators;

/**
 * A Type Specific List interface that reduces boxing/unboxing and adds a couple extra quality of life features
 */
public interface IntList extends IntCollection, List<Integer>
{
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @return true if the list was modified
	 * @see List#add(Object)
	 */
	@Override
	public boolean add(int e);
	
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @param index index at which the specified element is to be inserted
	 * @see List#add(int, Object)
	 */
	public void add(int index, int e);
	
	/**
	 * A Helper function that will only add elements if it is not present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfAbsent(int e) {
		if(indexOf(e) == -1) return add(e);
		return false;
	}
	
	/**
	 * A Helper function that will only add elements if it is present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfPresent(int e) {
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
	public boolean addAll(int index, IntCollection c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @return true if the list was modified
	 */
	public boolean addAll(IntList c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 */
	public boolean addAll(int index, IntList c);
	
	/**
	 * Helper method that returns the first element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return first element of the list
	 */
	public default int getFirstInt() {
		return getInt(0);
	}
	
	/**
	 * Helper method that returns the last element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return last element of the list
	 */
	public default int getLastInt() {
		return getInt(size() - 1);
	}
	
	/**
	 * Helper method that removes and returns the first element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return first element of the list and removes it
	 */
	public default int removeFirstInt() {
		return removeInt(0);
	}
	
	/**
	 * Helper method that removes and returns the last element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return last element of the list and removes it
	 */
	public default int removeLastInt() {
		return removeInt(size() - 1);
	}
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the value that is requested
	 * @return the value at the given index
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#get(int)
	 */
	public int getInt(int index);
	
	/**
	 * A Type-Specific set function to reduce (un)boxing
	 * @param index index of the element to replace
	 * @param e element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#set(int, Object)
	 */
	public int set(int index, int e);
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @see List#remove(int)
	 */
	public int removeInt(int index);
	
	/**
	 * A Type-Specific indexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int indexOf(int e);
	
	/**
	 * A Type-Specific lastIndexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the lastIndex of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int lastIndexOf(int e);

	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	public default void replaceInts(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		IntListIterator iter = listIterator();
		while (iter.hasNext())
			iter.set(o.applyAsInt(iter.nextInt()));
	}
	
	/**
	 * A function to fast add elements to the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(int... a) { addElements(size(), a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(int from, int... a) { addElements(from, a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @param offset the start index of the array should be read from
	 * @param length how many elements should be read from
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public void addElements(int from, int[] a, int offset, int length);
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	public default int[] getElements(int from, int[] a) { return getElements(from, a, 0, a.length); }
	
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
	public int[] getElements(int from, int[] a, int offset, int length);
	
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
	public int swapRemove(int index);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param e the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean swapRemoveInt(int e);
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	public int[] extractElements(int from, int to);
	
	/**
	 * Helper function that allows to fastFill a buffer reducing the duplication requirement
	 * @param buffer where the data should be stored in.
	 */
	public default void fillBuffer(IntBuffer buffer) { buffer.put(toIntArray()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default void sort(Comparator<? super Integer> c) {
		sort((K, V) -> c.compare(Integer.valueOf(K), Integer.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see IntArrays#stableSort(int[], IntComparator)
	 */
	public default void sort(IntComparator c) {
		int[] array = toIntArray();
		if(c != null) IntArrays.stableSort(array, c);
		else IntArrays.stableSort(array);
		IntListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextInt();
			iter.set(array[i]);
		}
	}
	
	/** 
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @param c the sorter of the elements, can be null
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Deprecated
	public default void unstableSort(Comparator<? super Integer> c) {
		unstableSort((K, V) -> c.compare(Integer.valueOf(K), Integer.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see IntArrays#unstableSort(int[], IntComparator)
	 */
	public default void unstableSort(IntComparator c) {
		int[] array = toIntArray();
		if(c != null) IntArrays.unstableSort(array, c);
		else IntArrays.unstableSort(array);
		IntListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextInt();
			iter.set(array[i]);
		}
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	@Override
	public default void forEachIndexed(IntIntConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size();i<m;action.accept(i, getInt(i++)));
	}
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator
	 */
	@Override
	public IntListIterator listIterator();
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public IntListIterator listIterator(int index);
	
	/**
	 * Creates a Iterator that follows the indecies provided.<br>
	 * For example if the Lists Contents is:<br> -1, 0 1 <br>and the indecies are: <br>0, 1, 2, 2, 1, 0<br>
	 * then the iterator will return the following values: <br>-1, 0, 1, 1, 0, -1
	 * @param indecies that should be used for the iteration.
	 * @return a custom indexed iterator
	 */
	public IntListIterator indexedIterator(int...indecies);
	
	/**
	 * Creates a Iterator that follows the indecies provided.<br>
	 * For example if the Lists Contents is:<br> -1, 0 1 <br>and the indecies are: <br>0, 1, 2, 2, 1, 0<br>
	 * then the iterator will return the following values: <br>-1, 0, 1, 1, 0, -1
	 * @param indecies that should be used for the iteration.
	 * @return a custom indexed iterator
	 */
	public IntListIterator indexedIterator(IntList indecies);
	
	/**
	 * A Type-Specific List of subList
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public IntList subList(int from, int to);
	
	/**
	 * A Type-Specific List Helper that shows all elements in reverse.
	 * @return a list wrapper that has all elements reversed!
	 */
	public IntList reversed();
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @return a new List that is synchronized
	 * @see IntLists#synchronize
	 */
	public default IntList synchronize() { return IntLists.synchronize(this); }
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new List Wrapper that is synchronized
	 * @see IntLists#synchronize
	 */
	public default IntList synchronize(Object mutex) { return IntLists.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped List that is unmodifiable
	 * @return a new List Wrapper that is unmodifiable
	 * @see IntLists#unmodifiable
	 */
	public default IntList unmodifiable() { return IntLists.unmodifiable(this); }
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	public void size(int size);
	
	@Override
	public IntList copy();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Integer e) {
		return IntCollection.super.add(e);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Integer get(int index) {
		return Integer.valueOf(getInt(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Integer set(int index, Integer e) {
		return Integer.valueOf(set(index, e.intValue()));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int indexOf(Object o) {
		return indexOf(((Integer)o).intValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int lastIndexOf(Object o) {
		return lastIndexOf(((Integer)o).intValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return IntCollection.super.contains(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return IntCollection.super.remove(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Integer remove(int index) {
		return Integer.valueOf(removeInt(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void replaceAll(UnaryOperator<Integer> o) {
		Objects.requireNonNull(o);
		replaceInts(T -> o.apply(Integer.valueOf(T)).intValue());	
	}
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default IntSplititerator spliterator() { return IntSplititerators.createSplititerator(this, 0); }
}