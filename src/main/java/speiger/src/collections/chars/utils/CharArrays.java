package speiger.src.collections.chars.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Arrays
 */
public class CharArrays
{
	/** Default Limit for Insertion/Selection Sort */
	public static final int BASE_THRESHOLD = 16;
	/** Default Threshold for Multithreaded Sorting Algorythm options*/
	public static final int PARALLEL_THRESHOLD = 8192;
	
	/** Empty Array Reference used for Uninitialized Collections */
	public static final char[] EMPTY_ARRAY = new char[0];
	
	/**
	 * A Helper function to convert a Primitive Array to a Character Array.
	 * @param a the array that should be converted
	 * @return a Character Array of the input array.
	 */
	public static Character[] wrap(char[] a) {
		return wrap(a, 0, a.length);
	}
	
	/**
	 * A Helper function to convert a Primitive Array to a Character Array.
	 * @param a the array that should be converted
	 * @param length the maximum length that should be coverted
	 * @return a Character Array of the input array.
	 */
	public static Character[] wrap(char[] a, int length) {
		return wrap(a, 0, length);
	}
	
	/**
	 * A Helper function to convert a Primitive Array to a Character Array.
	 * @param a the array that should be converted
	 * @param offset the starting offset of the inputarray
	 * @param length the maximum length that should be coverted
	 * @return a Character Array of the input array.
	 */
	public static Character[] wrap(char[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		Character[] result = new Character[length];
		for(int i = offset;i<length;i++)
			result[i] = Character.valueOf(a[i]);
		return result;
	}
	
	/**
	 * A Helper function to convert a Character Array to a char Array.
	 * @param a the array that should be converted
	 * @return a char Array of the input array.
	 */
	public static char[] unwrap(Character[] a) {
		return unwrap(a, 0, a.length);
	}
	
	/**
	 * A Helper function to convert a Character Array to a char Array.
	 * @param a the array that should be converted
	 * @param length the maximum length that should be coverted
	 * @return a char Array of the input array.
	 */
	public static char[] unwrap(Character[] a, int length) {
		return unwrap(a, 0, length);
	}
	
	/**
	 * A Helper function to convert a Character Array to a char Array.
	 * @param a the array that should be converted
	 * @param offset the starting offset of the inputarray
	 * @param length the maximum length that should be coverted
	 * @return a char Array of the input array.
	 */
	public static char[] unwrap(Character[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		char[] result = new char[length];
		for(int i = offset;i<length;i++)
			result[i] = a[i].charValue();
		return result;
	}
	
	/**
	 * A Helper function that pours all elements of a iterator into a Array
	 * @param iter the elements that should be gathered.
	 * @return array with all elements of the iterator
	 */
	public static char[] pour(CharIterator iter) {
		return pour(iter, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function that pours all elements of a iterator into a Array
	 * @param iter the elements that should be gathered.
	 * @param max how many elements should be added
	 * @return array with all requested elements of the iterator
	 */
	public static char[] pour(CharIterator iter, int max) {
		CharCollections.CollectionWrapper list = CharCollections.wrapper();
		CharIterators.pour(iter, list, max);
		return list.toCharArray(new char[list.size()]);
	}
	
	
	/**
	 * Method to validate if the current value is the lowest value in the heap
	 * @param data the current heap.
	 * @param size the size of the heap
	 * @param index the index that should be validated
	 * @param comp the comparator to sort the heap. Can be null
	 * @return the index the element was shifted to
	 */
	public static int shiftDown(char[] data, int size, int index, CharComparator comp) {
		int half = size >>> 1;
		char value = data[index];
		if(comp != null) {
			while(index < half) {
				int child = (index << 1) + 1;
				char childValue = data[child];
				int right = child+1;
				if(right < size && comp.compare(data[right], childValue) < 0) childValue = data[child = right];
				if(comp.compare(value, childValue) <= 0) break;
				data[index] = childValue;
				index = child;
			}
		}
		else {
			while(index < half) {
				int child = (index << 1) + 1;
				char childValue = data[child];
				int right = child+1;
				if(right < size && Character.compare(data[right], childValue) < 0) childValue = data[child = right];
				if(Character.compare(value, childValue) <= 0) break;
				data[index] = childValue;
				index = child;
			}
		}
		data[index] = value;
		return index;
	}
	
	/**
	 * Method to sort a specific value into the heap.
	 * @param data the heap itself.
	 * @param index that should be heapified.
	 * @param comp the comparator to sort the heap. Can be null
	 * @return the index the element was shifted to
	 */
	public static int shiftUp(char[] data, int index, CharComparator comp) {
		char value = data[index];
		if(comp != null) {
			while(index > 0) {
				int parent = (index - 1) >>> 1;
				char parentValue = data[parent];
				if(comp.compare(value, parentValue) >= 0) break;
				data[index] = parentValue;
				index = parent;
			}
		}
		else {
			while(index > 0) {
				int parent = (index - 1) >>> 1;
				char parentValue = data[parent];
				if(Character.compare(value, parentValue) >= 0) break;
				data[index] = parentValue;
				index = parent;
			}
		}
		data[index] = value;
		return index;
	}
	
	/**
	 * Helper function to create a Heap out of an array.
	 * @param data the array to heapify
	 * @param size the current size of elements within the array.
	 * @param comp the Comparator to sort the array. Can be null
	 * @return the input array
	 */
	public static char[] heapify(char[] data, int size, CharComparator comp) {
		for(int i = (size >>> 1) - 1;i>=0;shiftDown(data, size, i--, comp));
		return data;
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @note This uses the SanityChecks#getRandom
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array) {
		return shuffle(array, SanityChecks.getRandom());
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param length the length of the array
	 * @note This uses the SanityChecks#getRandom
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array, int length) {
		return shuffle(array, 0, length, SanityChecks.getRandom());
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param offset the start array
	 * @param length the length of the array
	 * @note This uses the SanityChecks#getRandom
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array, int offset, int length) {
		return shuffle(array, offset, length, SanityChecks.getRandom());
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param random the Random Number Generator that should be used for the shuffling
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array, Random random) {
		for(int i = array.length-1; i>=0;i--) {
			int p = random.nextInt(i + 1);
			char t = array[i];
			array[i] = array[p];
			array[p] = t;
		}
		return array;
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param length the length of the array
	 * @param random the Random Number Generator that should be used for the shuffling
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array, int length, Random random) {
		return shuffle(array, 0, length, random);
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param offset the start array
	 * @param length the length of the array
	 * @param random the Random Number Generator that should be used for the shuffling
	 * @return the provided sorted array
	 */
	public static char[] shuffle(char[] array, int offset, int length, Random random) {
		for(int i = length-1; i>=0;i--) {
			int p = offset + random.nextInt(i + 1);
			char t = array[offset+i];
			array[offset+i] = array[p];
			array[p] = t;
		}
		return array;
	}
	
	/**
	 * Simple Array Reversal method
	 * @param array the Array that should flip
	 * @return the provided array
	 */
	public static char[] reverse(char[] array) {
		return reverse(array, 0, array.length);
	}
	
	/**
	 * Simple Array Reversal method
	 * @param array the Array that should flip
	 * @param length the length of the array
	 * @return the provided array
	 */
	public static char[] reverse(char[] array, int length) {
		return reverse(array, 0, length);
	}
	
	/**
	 * Simple Array Reversal method
	 * @param array the Array that should flip
	 * @param length the length of the array
	 * @param offset the start of the array
	 * @return the provided array
	 */
	public static char[] reverse(char[] array, int offset, int length) {
		  for (int i = offset, mid = offset + length >> 1, j = offset + length - 1; i < mid; i++, j--) {
			  char temp = array[i];
			  array[i] = array[j];
			  array[j] = temp;
		  }
		  return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array.
	 */
	public static char[] stableSort(char[] array, CharComparator comp) {
		stableSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void stableSort(char[] array, int length, CharComparator comp) {
		stableSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void stableSort(char[] array, int from, int to, CharComparator comp) {
		mergeSort(array, null, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] stableSort(char[] array) {
		stableSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void stableSort(char[] array, int length) {
		stableSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void stableSort(char[] array, int from, int to) {
		mergeSort(array, null, from, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static char[] unstableSort(char[] array, CharComparator comp) {
		unstableSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void unstableSort(char[] array, int length, CharComparator comp) {
		unstableSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator,
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void unstableSort(char[] array, int from, int to, CharComparator comp) {
		quickSort(array, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] unstableSort(char[] array) {
		unstableSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void unstableSort(char[] array, int length) {
		unstableSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void unstableSort(char[] array, int from, int to) {
		quickSort(array, from, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static char[] insertionSort(char[] array, CharComparator comp) {
		insertionSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void insertionSort(char[] array, int length, CharComparator comp) {
		insertionSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void insertionSort(char[] array, int from, int to, CharComparator comp) {
		for (int i = from+1;i<to; i++) {
			char current = array[i];
			int j = i - 1;
			while(j >= from && comp.compare(current, array[j]) < 0) {
				array[j+1] = array[j--];
			}
			array[j+1] = current;
		}
	}
	
	/**
	 * Sorts an array according to the natural ascending order using InsertionSort, 
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] insertionSort(char[] array) {
		insertionSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using InsertionSort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void insertionSort(char[] array, int length) {
		insertionSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using InsertionSort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void insertionSort(char[] array, int from, int to) {
		for (int i = from+1;i<to; i++) {
			char current = array[i];
			int j = i - 1;
			while(j >= from && Character.compare(current, array[j]) < 0) {
				array[j+1] = array[j--];
			}
			array[j+1] = current;
		}
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static char[] selectionSort(char[] array, CharComparator comp) {
		selectionSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void selectionSort(char[] array, int length, CharComparator comp) {
		selectionSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void selectionSort(char[] array, int from, int to, CharComparator comp) {
		for (int i = from; i < to; i++) {
			char min = array[i];
			int minId = i;
			for(int j = i+1; j < to; j++) {
				if(comp.compare(array[j], min) < 0) {
					min = array[j];
					minId = j;
				}
			}
			char temp = array[i];
			array[i] = min;
			array[minId] = temp;
		}
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort, 
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] selectionSort(char[] array) {
		selectionSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void selectionSort(char[] array, int length) {
		selectionSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void selectionSort(char[] array, int from, int to) {
		for (int i = from,m=to-1; i < m; i++) {
			char min = array[i];
			int minId = i;
			for(int j = i+1; j < to; j++) {
				if(Character.compare(array[j], min) < 0) {
					min = array[j];
					minId = j;
				}
			}
			char temp = array[i];
			array[i] = min;
			array[minId] = temp;
		}
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static char[] mergeSort(char[] array, CharComparator comp) {
		mergeSort(array, null, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void mergeSort(char[] array, int length, CharComparator comp) {
		mergeSort(array, null, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param supp the auxillary array that is used to simplify the sorting
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void mergeSort(char[] array, char[] supp, int from, int to, CharComparator comp) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to, comp);
			return;
		}
		if(supp == null) supp = Arrays.copyOf(array, to);
		int mid = (from + to) >>> 1;
		mergeSort(supp, array, from, mid, comp);
		mergeSort(supp, array, mid, to, comp);
		if(comp.compare(supp[mid - 1], supp[mid]) <= 0)
		{
			System.arraycopy(supp, from, array, from, to - from);
			return;
		}
		for(int p = from, q = mid;from < to;from++) {
			if(q >= to || p < mid && comp.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
			else array[from] = supp[q++];
		}
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Merge Sort, 
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] mergeSort(char[] array) {
		mergeSort(array, null, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void mergeSort(char[] array, int length) {
		mergeSort(array, null, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param supp the auxillary array that is used to simplify the sorting
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void mergeSort(char[] array, char[] supp, int from, int to) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to);
			return;
		}
		if(supp == null) supp = Arrays.copyOf(array, to);
		int mid = (from + to) >>> 1;
		mergeSort(supp, array, from, mid);
		mergeSort(supp, array, mid, to);
		if(Character.compare(supp[mid - 1], supp[mid]) <= 0)
		{
			System.arraycopy(supp, from, array, from, to - from);
			return;
		}
		for(int p = from, q = mid;from < to;from++) {
			if(q >= to || p < mid && Character.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
			else array[from] = supp[q++];
		}
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using a Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array, CharComparator comp) {
		parallelMergeSort(array, null, 0, array.length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array, int length, CharComparator comp) {
		parallelMergeSort(array, null, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param supp the auxillary array that is used to simplify the sorting
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array, char[] supp, int from, int to, CharComparator comp) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new MergeSortActionComp(array, supp, from, to, comp));
			return;
		}
		mergeSort(array, supp, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Merge Sort, 
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array) {
		parallelMergeSort(array, null, 0, array.length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array, int length) {
		parallelMergeSort(array, null, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param supp the auxillary array that is used to simplify the sorting
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(char[] array, char[] supp, int from, int to) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new MergeSortAction(array, supp, from, to));
			return;
		}
		mergeSort(array, supp, from, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array. It is in Very Unsorted Instances 50% slower then Mergesort, otherwise it as fast.
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void memFreeMergeSort(char[] array, CharComparator comp) {
		memFreeMergeSort(array, 0, array.length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array. It is in Very Unsorted Instances 50% slower then Mergesort, otherwise it as fast.
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void memFreeMergeSort(char[] array, int length, CharComparator comp) {
		memFreeMergeSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array. It is in Very Unsorted Instances 50% slower then Mergesort, otherwise it as fast.
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void memFreeMergeSort(char[] array, int from, int to, CharComparator comp) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to, comp);
			return;
		}
		int mid = (from + to) >>> 1;
		memFreeMergeSort(array, from, mid, comp);
		memFreeMergeSort(array, mid, to, comp);
		if(comp.compare(array[mid - 1], array[mid]) <= 0)
			return;
		for(int i = from, j = mid, compare;i < j && j < to;) {
			if((compare = comp.compare(array[i], array[j])) < 0)
				i++;
			else if(compare == 0) swap(array, ++i, j);
			else {
				int k = j;
				for(;k < to - 1 && comp.compare(array[i], array[k + 1]) > 0;k++);
				if(j == k) {
					swap(array, i++, j);
					continue;
				}
				else if(j + 1 == k) {
					char value = array[j];
					System.arraycopy(array, i, array, i+1, j - i);
					array[i] = value;
					i++;
					j++;
					continue;
				}
				char[] data = new char[k - j];
				System.arraycopy(array, j, data, 0, data.length);
				System.arraycopy(array, i, array, i+data.length, j - i);
				System.arraycopy(data, 0, array, i, data.length);
				i+=data.length;
				j+=data.length;
			}
		}
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Memory Free Merge Sort, 
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] memFreeMergeSort(char[] array) {
		memFreeMergeSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Memory Free Merge Sort, 
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void memFreeMergeSort(char[] array, int length) {
		memFreeMergeSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Memory Free Merge Sort, 
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void memFreeMergeSort(char[] array, int from, int to) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to);
			return;
		}
		int mid = (from + to) >>> 1;
		memFreeMergeSort(array, from, mid);
		memFreeMergeSort(array, mid, to);
		if(Character.compare(array[mid - 1], array[mid]) <= 0)
			return;
		for(int i = from, j = mid, comp;i < j && j < to;) {
			if((comp = Character.compare(array[i], array[j])) < 0)
				i++;
			else if(comp == 0) swap(array, ++i, j);
			else {
				int k = j;
				for(;k < to - 1 && Character.compare(array[i], array[k + 1]) > 0;k++);
				if(j == k) {
					swap(array, i++, j);
					continue;
				}
				else if(j + 1 == k) {
					char value = array[j];
					System.arraycopy(array, i, array, i+1, j - i);
					array[i] = value;
					i++;
					j++;
					continue;
				}
				char[] data = new char[k - j];
				System.arraycopy(array, j, data, 0, data.length);
				System.arraycopy(array, i, array, i+data.length, j - i);
				System.arraycopy(data, 0, array, i, data.length);
				i+=data.length;
				j+=data.length;
			}
		}
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array, CharComparator comp) {
		parallelMemFreeMergeSort(array, 0, array.length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array, int length, CharComparator comp) {
		parallelMemFreeMergeSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array, int from, int to, CharComparator comp) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new MemFreeMergeSortActionComp(array, from, to, comp));
			return;
		}
		memFreeMergeSort(array, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Memory Free Merge Sort, 
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array) {
		parallelMemFreeMergeSort(array, 0, array.length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array, int length) {
		parallelMemFreeMergeSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array.
	 * It is depending on the size and the unsorted level of the input array slower or almost as fast as normal merge sort. Depending on the test size i can be 0.5x slower (5000 elements) or 4x slower (50000 elements) under the assumtion that the array is in its worst case scenario.
	 * It does stack allocate tiny amounts of data for shifting around elements.
	 * @author Speiger
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMemFreeMergeSort(char[] array, int from, int to) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new MemFreeMergeSortAction(array, from, to));
			return;
		}
		memFreeMergeSort(array, from, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static char[] quickSort(char[] array, CharComparator comp) {
		quickSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void quickSort(char[] array, int length, CharComparator comp) {
		quickSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void quickSort(char[] array, int from, int to, CharComparator comp) {
		int length = to - from;
		if(length <= 0) return;
		if(length < BASE_THRESHOLD) {
			selectionSort(array, from, to, comp);
			return;			
		}
		char pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8, comp) : medium(array, from, from + (length / 2), to - 1, comp)];
		int a = from, b = a, c = to - 1, d = c;
		for(int compare;;swap(array, b++, c--)) {
			for(;b<=c && (compare = comp.compare(array[b], pivot)) <= 0;b++) {
				if(compare == 0) swap(array, a++, b);
			}
			for(;c>=b && (compare = comp.compare(array[c], pivot)) >= 0;c--) {
				if(compare == 0) swap(array, c, d--);
			}
			if(b>c) break;
		}
		swap(array, from, b, Math.min(a - from, b - a)); 
		swap(array, b, to, Math.min(d - c, to - d - 1));
		if((length = b - a) > 1) quickSort(array, from, from + length, comp);
		if((length = d - c) > 1) quickSort(array, to - length, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Quick Sort, 
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static char[] quickSort(char[] array) {
		quickSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void quickSort(char[] array, int length) {
		quickSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void quickSort(char[] array, int from, int to) {
		int length = to - from;
		if(length <= 0) return;
		if(length < BASE_THRESHOLD) {
			selectionSort(array, from, to);
			return;			
		}
		char pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8) : medium(array, from, from + (length / 2), to - 1)];
		int a = from, b = a, c = to - 1, d = c;
		for(int comp = 0;;swap(array, b++, c--)) {
			for(;b<=c && (comp = Character.compare(array[b], pivot)) <= 0;b++) {
				if(comp == 0) swap(array, a++, b);
			}
			for(;c>=b && (comp = Character.compare(array[c], pivot)) >= 0;c--) {
				if(comp == 0) swap(array, c, d--);
			}
			if(b>c) break;
		}
		swap(array, from, b, Math.min(a - from, b - a)); 
		swap(array, b, to, Math.min(d - c, to - d - 1));
		if((length = b - a) > 1) quickSort(array, from, from + length);
		if((length = d - c) > 1) quickSort(array, to - length, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array, CharComparator comp) {
		parallelQuickSort(array, 0, array.length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array, int length, CharComparator comp) {
		parallelQuickSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Parallel Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array, int from, int to, CharComparator comp) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new QuickSortActionComp(array, from, to, comp));
			return;
		}
		quickSort(array, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Quick Sort, 
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array) {
		parallelQuickSort(array, 0, array.length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array, int length) {
		parallelQuickSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Quick Sort,
	 * This implementation is a custom of <a href="https://github.com/vigna/fastutil">FastUtil</a> quicksort but with a different code structure,
	 * and that sorting Algorithm is based on the tuned quicksort adapted from Jon L. Bentley and M. DouglasMcIlroy, "Engineering a Sort Function", Software: Practice and Experience, 23(11), pages1249−1265, 1993. 
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelQuickSort(char[] array, int from, int to) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new QuickSortAction(array, from, to));
			return;
		}
		quickSort(array, from, to);
	}
	
	static void swap(char[] a, int from, int to) {
		char t = a[from];
		a[from] = a[to];
		a[to] = t;
	}
	
	static void swap(char[] a, int from, int to, int length) {
		to -= length;
		for(int i = 0;i<length;i++,swap(a, from++, to++));
	}
	
	static int subMedium(char[] data, int a, int b, int c, int length, CharComparator comp) {
		return medium(data, medium(data, a, a + length, a + (length * 2), comp), medium(data, b - length, b, b + length, comp), medium(data, c - (length * 2), c - length, c, comp), comp);
	}
	
	static int medium(char[] data, int a, int b, int c, CharComparator comp) {
		return comp.compare(data[a], data[b]) < 0 ? (comp.compare(data[b], data[c]) < 0 ? b : comp.compare(data[a], data[c]) < 0 ? c : a) : (comp.compare(data[b], data[c]) > 0 ? b : comp.compare(data[a], data[c]) > 0 ? c : a);
	}
	
	static int subMedium(char[] data, int a, int b, int c, int length) {
		return medium(data, medium(data, a, a + length, a + (length * 2)), medium(data, b - length, b, b + length), medium(data, c - (length * 2), c - length, c));
	}
	
	static int medium(char[] data, int a, int b, int c) {
		return Character.compare(data[a], data[b]) < 0 ? (Character.compare(data[b], data[c]) < 0 ? b : Character.compare(data[a], data[c]) < 0 ? c : a) : (Character.compare(data[b], data[c]) > 0 ? b : Character.compare(data[a], data[c]) > 0 ? c : a);
	}
	
	static class QuickSortAction extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		int from;
		int to;
		
		QuickSortAction(char[] array, int from, int to)
		{
			this.array = array;
			this.from = from;
			this.to = to;
		}
		
		@Override
		protected void compute()
		{
			int length = to - from;
			if(length <= 0) return;
			if(length < BASE_THRESHOLD) {
				selectionSort(array, from, to);
				return;			
			}
			char pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8) : medium(array, from, from + (length / 2), to - 1)];
			int a = from, b = a, c = to - 1, d = c;
			for(int comp = 0;;swap(array, b++, c--)) {
				for(;b<=c && (comp = Character.compare(array[b], pivot)) <= 0;b++) {
					if(comp == 0) swap(array, a++, b);
				}
				for(;c>=b && (comp = Character.compare(array[c], pivot)) >= 0;c--) {
					if(comp == 0) swap(array, c, d--);
				}
				if(b>c) break;
			}
			swap(array, from, b, Math.min(a - from, b - a)); 
			swap(array, b, to, Math.min(d - c, to - d - 1));
			if(b - a > 1 && d - c > 1) invokeAll(new QuickSortAction(array, from, from + (b - a)), new QuickSortAction(array, to - (d - c), to));
			else if(b - a > 1) new QuickSortAction(array, from, from + (b - a)).invoke();
			else if(d - c > 1) new QuickSortAction(array, to - (d - c), to).invoke();
		}
	}
	
	static class QuickSortActionComp extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		int from;
		int to;
		CharComparator comp;
		
		QuickSortActionComp(char[] array, int from, int to, CharComparator comp)
		{
			this.array = array;
			this.from = from;
			this.to = to;
			this.comp = comp;
		}
		
		@Override
		protected void compute()
		{
			int length = to - from;
			if(length <= 0) return;
			if(length < BASE_THRESHOLD) {
				selectionSort(array, from, to, comp);
				return;			
			}
			char pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8, comp) : medium(array, from, from + (length / 2), to - 1, comp)];
			int a = from, b = a, c = to - 1, d = c;
			for(int compare;;swap(array, b++, c--)) {
				for(;b<=c && (compare = comp.compare(array[b], pivot)) <= 0;b++) {
					if(compare == 0) swap(array, a++, b);
				}
				for(;c>=b && (compare = comp.compare(array[c], pivot)) >= 0;c--) {
					if(compare == 0) swap(array, c, d--);
				}
				if(b>c) break;
			}
			swap(array, from, b, Math.min(a - from, b - a)); 
			swap(array, b, to, Math.min(d - c, to - d - 1));
			if(b - a > 1 && d - c > 1) invokeAll(new QuickSortActionComp(array, from, from + (b - a), comp), new QuickSortActionComp(array, to - (d - c), to, comp));
			else if(b - a > 1) new QuickSortActionComp(array, from, from + (b - a), comp).invoke();
			else if(d - c > 1) new QuickSortActionComp(array, to - (d - c), to, comp).invoke();
		}
	}
	
	static class MergeSortAction extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		char[] supp;
		int from;
		int to;
		
		MergeSortAction(char[] array, char[] supp, int from, int to)
		{
			this.array = array;
			this.supp = supp;
			this.from = from;
			this.to = to;
		}
		
		@Override
		protected void compute()
		{
			if(to - from < BASE_THRESHOLD) {
				insertionSort(array, from, to);
				return;
			}
			if(supp == null) supp = Arrays.copyOf(array, to);
			int mid = (from + to) >>> 1;
			invokeAll(new MergeSortAction(supp, array, from, mid), new MergeSortAction(supp, array, mid, to));
			if(Character.compare(supp[mid - 1], supp[mid]) <= 0)
			{
				System.arraycopy(supp, from, array, from, to - from);
				return;
			}
			for(int p = from, q = mid;from < to;from++) {
				if(q >= to || p < mid && Character.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
				else array[from] = supp[q++];
			}
		}
	}
	
	static class MergeSortActionComp extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		char[] supp;
		int from;
		int to;
		CharComparator comp;
		
		MergeSortActionComp(char[] array, char[] supp, int from, int to, CharComparator comp)
		{
			this.array = array;
			this.supp = supp;
			this.from = from;
			this.to = to;
			this.comp = comp;
		}
		
		@Override
		protected void compute()
		{
			if(to - from < BASE_THRESHOLD) {
				insertionSort(array, from, to, comp);
				return;
			}
			if(supp == null) supp = Arrays.copyOf(array, to);
			int mid = (from + to) >>> 1;
			invokeAll(new MergeSortActionComp(supp, array, from, mid, comp), new MergeSortActionComp(supp, array, mid, to, comp));
			if(comp.compare(supp[mid - 1], supp[mid]) <= 0)
			{
				System.arraycopy(supp, from, array, from, to - from);
				return;
			}
			for(int p = from, q = mid;from < to;from++) {
				if(q >= to || p < mid && comp.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
				else array[from] = supp[q++];
			}
		}
	}
	
	static class MemFreeMergeSortAction extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		int from;
		int to;
		
		MemFreeMergeSortAction(char[] array, int from, int to)
		{
			this.array = array;
			this.from = from;
			this.to = to;
		}
		
		@Override
		protected void compute()
		{
			if(to - from < BASE_THRESHOLD) {
				insertionSort(array, from, to);
				return;
			}
			int mid = (from + to) >>> 1;
			invokeAll(new MemFreeMergeSortAction(array, from, mid), new MemFreeMergeSortAction(array, mid, to));
			if(Character.compare(array[mid - 1], array[mid]) <= 0)
				return;
			for(int i = from, j = mid, comp;i < j && j < to;) {
				if((comp = Character.compare(array[i], array[j])) < 0)
					i++;
				else if(comp == 0) swap(array, ++i, j);
				else {
					int k = j;
					for(;k < to - 1 && Character.compare(array[i], array[k + 1]) > 0;k++);
					if(j == k) {
						swap(array, i++, j);
						continue;
					}
					else if(j + 1 == k) {
						char value = array[j];
						System.arraycopy(array, i, array, i+1, j - i);
						array[i] = value;
						i++;
						j++;
						continue;
					}
					char[] data = new char[k - j];
					System.arraycopy(array, j, data, 0, data.length);
					System.arraycopy(array, i, array, i+data.length, j - i);
					System.arraycopy(data, 0, array, i, data.length);
					i+=data.length;
					j+=data.length;
				}
			}
		}
	}
	
	static class MemFreeMergeSortActionComp extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		char[] array;
		int from;
		int to;
		CharComparator comp;
		
		MemFreeMergeSortActionComp(char[] array, int from, int to, CharComparator comp)
		{
			this.array = array;
			this.from = from;
			this.to = to;
			this.comp = comp;
		}
		
		@Override
		protected void compute()
		{
			if(to - from < BASE_THRESHOLD) {
				insertionSort(array, from, to, comp);
				return;
			}
			int mid = (from + to) >>> 1;
			invokeAll(new MemFreeMergeSortActionComp(array, from, mid, comp), new MemFreeMergeSortActionComp(array, mid, to, comp));

			if(comp.compare(array[mid - 1], array[mid]) <= 0)
				return;
			for(int i = from, j = mid, compare;i < j && j < to;) {
				if((compare = comp.compare(array[i], array[j])) < 0)
					i++;
				else if(compare == 0) swap(array, ++i, j);
				else {
					int k = j;
					for(;k < to - 1 && comp.compare(array[i], array[k + 1]) > 0;k++);
					if(j == k) {
						swap(array, i++, j);
						continue;
					}
					else if(j + 1 == k) {
						char value = array[j];
						System.arraycopy(array, i, array, i+1, j - i);
						array[i] = value;
						i++;
						j++;
						continue;
					}
					char[] data = new char[k - j];
					System.arraycopy(array, j, data, 0, data.length);
					System.arraycopy(array, i, array, i+data.length, j - i);
					System.arraycopy(data, 0, array, i, data.length);
					i+=data.length;
					j+=data.length;
				}
			}
		}
	}
}