package speiger.src.collections.floats.lists;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import java.util.Objects;
import java.util.Comparator;
import java.util.function.UnaryOperator;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.floats.utils.FloatLists;
import speiger.src.collections.floats.utils.FloatSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific List interface that reduces boxing/unboxing and adds a couple extra quality of life features
 */
public interface FloatList extends FloatCollection, List<Float>
{
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @return true if the list was modified
	 * @see List#add(Object)
	 */
	@Override
	public boolean add(float e);
	
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @param index index at which the specified element is to be inserted
	 * @see List#add(int, Object)
	 */
	public void add(int index, float e);
	
	/**
	 * A Helper function that will only add elements if it is not present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfAbsent(float e) {
		if(indexOf(e) == -1) return add(e);
		return false;
	}
	
	/**
	 * A Helper function that will only add elements if it is present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfPresent(float e) {
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
	public boolean addAll(int index, FloatCollection c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @return true if the list was modified
	 */
	public boolean addAll(FloatList c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 */
	public boolean addAll(int index, FloatList c);
	
	/**
	 * Helper method that returns the first element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return first element of the list
	 */
	public default float getFirstFloat() {
		return getFloat(0);
	}
	
	/**
	 * Helper method that returns the last element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return last element of the list
	 */
	public default float getLastFloat() {
		return getFloat(size() - 1);
	}
	
	/**
	 * Helper method that removes and returns the first element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return first element of the list and removes it
	 */
	public default float removeFirstFloat() {
		return removeFloat(0);
	}
	
	/**
	 * Helper method that removes and returns the last element of a List.
	 * This function was introduced due to how annoying it is to get/remove the last element of a list.
	 * This simplifies this process a bit.
	 * @return last element of the list and removes it
	 */
	public default float removeLastFloat() {
		return removeFloat(size() - 1);
	}
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the value that is requested
	 * @return the value at the given index
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#get(int)
	 */
	public float getFloat(int index);
	
	/**
	 * A Type-Specific set function to reduce (un)boxing
	 * @param index index of the element to replace
	 * @param e element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#set(int, Object)
	 */
	public float set(int index, float e);
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @see List#remove(int)
	 */
	public float removeFloat(int index);
	
	/**
	 * A Type-Specific indexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int indexOf(float e);
	
	/**
	 * A Type-Specific lastIndexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the lastIndex of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int lastIndexOf(float e);

	/**
	 * A Type-Specific replace function to reduce (un)boxing
	 * @param o the action to replace the values
	 * @throws NullPointerException if o is null
	 */
	public default void replaceFloats(DoubleUnaryOperator o) {
		Objects.requireNonNull(o);
		FloatListIterator iter = listIterator();
		while (iter.hasNext())
		iter.set(SanityChecks.castToFloat(o.applyAsDouble(iter.nextFloat())));
	}
	
	/**
	 * A function to fast add elements to the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(float... a) { addElements(size(), a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(int from, float... a) { addElements(from, a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @param offset the start index of the array should be read from
	 * @param length how many elements should be read from
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public void addElements(int from, float[] a, int offset, int length);
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	public default float[] getElements(int from, float[] a) { return getElements(from, a, 0, a.length); }
	
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
	public float[] getElements(int from, float[] a, int offset, int length);
	
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
	public float swapRemove(int index);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param e the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean swapRemoveFloat(float e);
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	public float[] extractElements(int from, int to);
	
	/**
	 * Helper function that allows to fastFill a buffer reducing the duplication requirement
	 * @param buffer where the data should be stored in.
	 */
	public default void fillBuffer(FloatBuffer buffer) { buffer.put(toFloatArray()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default void sort(Comparator<? super Float> c) {
		sort((K, V) -> c.compare(Float.valueOf(K), Float.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see FloatArrays#stableSort(float[], FloatComparator)
	 */
	public default void sort(FloatComparator c) {
		float[] array = toFloatArray();
		if(c != null) FloatArrays.stableSort(array, c);
		else FloatArrays.stableSort(array);
		FloatListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextFloat();
			iter.set(array[i]);
		}
	}
	
	/** 
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @param c the sorter of the elements, can be null
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Deprecated
	public default void unstableSort(Comparator<? super Float> c) {
		unstableSort((K, V) -> c.compare(Float.valueOf(K), Float.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see FloatArrays#unstableSort(float[], FloatComparator)
	 */
	public default void unstableSort(FloatComparator c) {
		float[] array = toFloatArray();
		if(c != null) FloatArrays.unstableSort(array, c);
		else FloatArrays.unstableSort(array);
		FloatListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextFloat();
			iter.set(array[i]);
		}
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	@Override
	public default void forEachIndexed(IntFloatConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size();i<m;action.accept(i, getFloat(i++)));
	}
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator
	 */
	@Override
	public FloatListIterator listIterator();
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public FloatListIterator listIterator(int index);
	
	/**
	 * A Type-Specific List of subList
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public FloatList subList(int from, int to);
	
	/**
	 * A Type-Specific List Helper that shows all elements in reverse.
	 * @return a list wrapper that has all elements reversed!
	 */
	public FloatList reversed();
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @return a new List that is synchronized
	 * @see FloatLists#synchronize
	 */
	public default FloatList synchronize() { return FloatLists.synchronize(this); }
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new List Wrapper that is synchronized
	 * @see FloatLists#synchronize
	 */
	public default FloatList synchronize(Object mutex) { return FloatLists.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped List that is unmodifiable
	 * @return a new List Wrapper that is unmodifiable
	 * @see FloatLists#unmodifiable
	 */
	public default FloatList unmodifiable() { return FloatLists.unmodifiable(this); }
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	public void size(int size);
	
	@Override
	public FloatList copy();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Float e) {
		return FloatCollection.super.add(e);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Float get(int index) {
		return Float.valueOf(getFloat(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Float set(int index, Float e) {
		return Float.valueOf(set(index, e.floatValue()));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int indexOf(Object o) {
		return indexOf(((Float)o).floatValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int lastIndexOf(Object o) {
		return lastIndexOf(((Float)o).floatValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return FloatCollection.super.contains(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return FloatCollection.super.remove(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Float remove(int index) {
		return Float.valueOf(removeFloat(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void replaceAll(UnaryOperator<Float> o) {
		Objects.requireNonNull(o);
		replaceFloats(T -> o.apply(Float.valueOf((float)T)).floatValue());
	}
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default FloatSplititerator spliterator() { return FloatSplititerators.createSplititerator(this, 0); }
}