package speiger.src.collections.bytes.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Arrays
 */
public class ByteArrays
{
	/** Default Limit for Insertion/Selection Sort */
	public static final int BASE_THRESHOLD = 16;
	/** Default Threshold for Multithreaded Sorting Algorythm options*/
	public static final int PARALLEL_THRESHOLD = 8192;
	
	/** Empty Array Reference used for Uninitialized Collections */
	public static final byte[] EMPTY_ARRAY = new byte[0];
	
	/**
	 * A Helper function to convert a Primitive Array to a Byte Array.
	 * @param a the array that should be converted
	 * @return a Byte Array of the input array.
	 */
	public static Byte[] wrap(byte[] a) {
		return wrap(a, 0, a.length);
	}
	
	/**
	 * A Helper function to convert a Primitive Array to a Byte Array.
	 * @param a the array that should be converted
	 * @param length the maximum length that should be coverted
	 * @return a Byte Array of the input array.
	 */
	public static Byte[] wrap(byte[] a, int length) {
		return wrap(a, 0, length);
	}
	
	/**
	 * A Helper function to convert a Primitive Array to a Byte Array.
	 * @param a the array that should be converted
	 * @param offset the starting offset of the inputarray
	 * @param length the maximum length that should be coverted
	 * @return a Byte Array of the input array.
	 */
	public static Byte[] wrap(byte[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		Byte[] result = new Byte[length];
		for(int i = offset;i<length;i++)
			result[i] = Byte.valueOf(a[i]);
		return result;
	}
	
	/**
	 * A Helper function to convert a Byte Array to a byte Array.
	 * @param a the array that should be converted
	 * @return a byte Array of the input array.
	 */
	public static byte[] unwrap(Byte[] a) {
		return unwrap(a, 0, a.length);
	}
	
	/**
	 * A Helper function to convert a Byte Array to a byte Array.
	 * @param a the array that should be converted
	 * @param length the maximum length that should be coverted
	 * @return a byte Array of the input array.
	 */
	public static byte[] unwrap(Byte[] a, int length) {
		return unwrap(a, 0, length);
	}
	
	/**
	 * A Helper function to convert a Byte Array to a byte Array.
	 * @param a the array that should be converted
	 * @param offset the starting offset of the inputarray
	 * @param length the maximum length that should be coverted
	 * @return a byte Array of the input array.
	 */
	public static byte[] unwrap(Byte[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		byte[] result = new byte[length];
		for(int i = offset;i<length;i++)
			result[i] = a[i].byteValue();
		return result;
	}
	
	/**
	 * A Helper function that pours all elements of a iterator into a Array
	 * @param iter the elements that should be gathered.
	 * @return array with all elements of the iterator
	 */
	public static byte[] pour(ByteIterator iter) {
		return pour(iter, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function that pours all elements of a iterator into a Array
	 * @param iter the elements that should be gathered.
	 * @param max how many elements should be added
	 * @return array with all requested elements of the iterator
	 */
	public static byte[] pour(ByteIterator iter, int max) {
		ByteCollections.CollectionWrapper list = ByteCollections.wrapper();
		ByteIterators.pour(iter, list, max);
		return list.toByteArray(new byte[list.size()]);
	}
	
	
	/**
	 * Method to validate if the current value is the lowest value in the heap
	 * @param data the current heap.
	 * @param size the size of the heap
	 * @param index the index that should be validated
	 * @param comp the comparator to sort the heap. Can be null
	 * @return the index the element was shifted to
	 */
	public static int shiftDown(byte[] data, int size, int index, ByteComparator comp) {
		int half = size >>> 1;
		byte value = data[index];
		if(comp != null) {
			while(index < half) {
				int child = (index << 1) + 1;
				byte childValue = data[child];
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
				byte childValue = data[child];
				int right = child+1;
				if(right < size && Byte.compare(data[right], childValue) < 0) childValue = data[child = right];
				if(Byte.compare(value, childValue) <= 0) break;
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
	public static int shiftUp(byte[] data, int index, ByteComparator comp) {
		byte value = data[index];
		if(comp != null) {
			while(index > 0) {
				int parent = (index - 1) >>> 1;
				byte parentValue = data[parent];
				if(comp.compare(value, parentValue) >= 0) break;
				data[index] = parentValue;
				index = parent;
			}
		}
		else {
			while(index > 0) {
				int parent = (index - 1) >>> 1;
				byte parentValue = data[parent];
				if(Byte.compare(value, parentValue) >= 0) break;
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
	public static byte[] heapify(byte[] data, int size, ByteComparator comp) {
		for(int i = (size >>> 1) - 1;i>=0;shiftDown(data, size, i--, comp));
		return data;
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @note This uses the SanityChecks#getRandom
	 * @return the provided sorted array
	 */
	public static byte[] shuffle(byte[] array) {
		return shuffle(array, SanityChecks.getRandom());
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param length the length of the array
	 * @note This uses the SanityChecks#getRandom
	 * @return the provided sorted array
	 */
	public static byte[] shuffle(byte[] array, int length) {
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
	public static byte[] shuffle(byte[] array, int offset, int length) {
		return shuffle(array, offset, length, SanityChecks.getRandom());
	}
	
	/**
	 * Simple Shuffle method for Arrays.
	 * @param array the elements that should be shuffled
	 * @param random the Random Number Generator that should be used for the shuffling
	 * @return the provided sorted array
	 */
	public static byte[] shuffle(byte[] array, Random random) {
		for(int i = array.length-1; i>=0;i--) {
			int p = random.nextInt(i + 1);
			byte t = array[i];
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
	public static byte[] shuffle(byte[] array, int length, Random random) {
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
	public static byte[] shuffle(byte[] array, int offset, int length, Random random) {
		for(int i = length-1; i>=0;i--) {
			int p = offset + random.nextInt(i + 1);
			byte t = array[offset+i];
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
	public static byte[] reverse(byte[] array) {
		return reverse(array, 0, array.length);
	}
	
	/**
	 * Simple Array Reversal method
	 * @param array the Array that should flip
	 * @param length the length of the array
	 * @return the provided array
	 */
	public static byte[] reverse(byte[] array, int length) {
		return reverse(array, 0, length);
	}
	
	/**
	 * Simple Array Reversal method
	 * @param array the Array that should flip
	 * @param length the length of the array
	 * @param offset the start of the array
	 * @return the provided array
	 */
	public static byte[] reverse(byte[] array, int offset, int length) {
		  for (int i = offset, mid = offset + length >> 1, j = offset + length - 1; i < mid; i++, j--) {
			  byte temp = array[i];
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
	public static byte[] stableSort(byte[] array, ByteComparator comp) {
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
	public static void stableSort(byte[] array, int length, ByteComparator comp) {
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
	public static void stableSort(byte[] array, int from, int to, ByteComparator comp) {
		mergeSort(array, null, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Stable sort referres to Mergesort or Insertionsort
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static byte[] stableSort(byte[] array) {
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
	public static void stableSort(byte[] array, int length) {
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
	public static void stableSort(byte[] array, int from, int to) {
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
	public static byte[] unstableSort(byte[] array, ByteComparator comp) {
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
	public static void unstableSort(byte[] array, int length, ByteComparator comp) {
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
	public static void unstableSort(byte[] array, int from, int to, ByteComparator comp) {
		quickSort(array, from, to, comp);
	}
	
	/**
	 * Sorts an array according to the natural ascending order, 
	 * potentially dynamically choosing an appropriate algorithm given the type and size of the array.
	 * Unstable sort referres to QuickSort or SelectionSort
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static byte[] unstableSort(byte[] array) {
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
	public static void unstableSort(byte[] array, int length) {
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
	public static void unstableSort(byte[] array, int from, int to) {
		quickSort(array, from, to);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param comp the Comparator that decides the sorting order
	 * @return input array
	 */
	public static byte[] insertionSort(byte[] array, ByteComparator comp) {
		insertionSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void insertionSort(byte[] array, int length, ByteComparator comp) {
		insertionSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Insertion Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void insertionSort(byte[] array, int from, int to, ByteComparator comp) {
		for (int i = from+1;i<to; i++) {
			byte current = array[i];
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
	public static byte[] insertionSort(byte[] array) {
		insertionSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using InsertionSort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void insertionSort(byte[] array, int length) {
		insertionSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using InsertionSort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void insertionSort(byte[] array, int from, int to) {
		for (int i = from+1;i<to; i++) {
			byte current = array[i];
			int j = i - 1;
			while(j >= from && Byte.compare(current, array[j]) < 0) {
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
	public static byte[] selectionSort(byte[] array, ByteComparator comp) {
		selectionSort(array, 0, array.length, comp);
		return array;
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void selectionSort(byte[] array, int length, ByteComparator comp) {
		selectionSort(array, 0, length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void selectionSort(byte[] array, int from, int to, ByteComparator comp) {
		for (int i = from; i < to; i++) {
			byte min = array[i];
			int minId = i;
			for(int j = i+1; j < to; j++) {
				if(comp.compare(array[j], min) < 0) {
					min = array[j];
					minId = j;
				}
			}
			byte temp = array[i];
			array[i] = min;
			array[minId] = temp;
		}
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort, 
	 * @param array the array that needs to be sorted
	 * @return input array
	 */
	public static byte[] selectionSort(byte[] array) {
		selectionSort(array, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void selectionSort(byte[] array, int length) {
		selectionSort(array, 0, length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Selection Sort,
	 * @param array the array that needs to be sorted
	 * @param from where the array should be sorted from
	 * @param to where the array should be sorted to
	 */
	public static void selectionSort(byte[] array, int from, int to) {
		for (int i = from,m=to-1; i < m; i++) {
			byte min = array[i];
			int minId = i;
			for(int j = i+1; j < to; j++) {
				if(Byte.compare(array[j], min) < 0) {
					min = array[j];
					minId = j;
				}
			}
			byte temp = array[i];
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
	public static byte[] mergeSort(byte[] array, ByteComparator comp) {
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
	public static void mergeSort(byte[] array, int length, ByteComparator comp) {
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
	public static void mergeSort(byte[] array, byte[] supp, int from, int to, ByteComparator comp) {
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
	public static byte[] mergeSort(byte[] array) {
		mergeSort(array, null, 0, array.length);
		return array;
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 */
	public static void mergeSort(byte[] array, int length) {
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
	public static void mergeSort(byte[] array, byte[] supp, int from, int to) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to);
			return;
		}
		if(supp == null) supp = Arrays.copyOf(array, to);
		int mid = (from + to) >>> 1;
		mergeSort(supp, array, from, mid);
		mergeSort(supp, array, mid, to);
		if(Byte.compare(supp[mid - 1], supp[mid]) <= 0)
		{
			System.arraycopy(supp, from, array, from, to - from);
			return;
		}
		for(int p = from, q = mid;from < to;from++) {
			if(q >= to || p < mid && Byte.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
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
	public static void parallelMergeSort(byte[] array, ByteComparator comp) {
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
	public static void parallelMergeSort(byte[] array, int length, ByteComparator comp) {
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
	public static void parallelMergeSort(byte[] array, byte[] supp, int from, int to, ByteComparator comp) {
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
	public static void parallelMergeSort(byte[] array) {
		parallelMergeSort(array, null, 0, array.length);
	}
	
	/**
	 * Sorts an array according to the natural ascending order using Parallel Merge Sort,
	 * This implementation was copied from <a href="https://github.com/vigna/fastutil">FastUtil</a> with a couple custom optimizations
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @note This parallelization is invoked through {@link SanityChecks#invokeTask} which the threadpool can be changed as needed
	 */
	public static void parallelMergeSort(byte[] array, int length) {
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
	public static void parallelMergeSort(byte[] array, byte[] supp, int from, int to) {
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
	public static void memFreeMergeSort(byte[] array, ByteComparator comp) {
		memFreeMergeSort(array, 0, array.length, comp);
	}
	
	/**
	 * Sorts the specified range of elements according to the order induced by the specified comparator using Memory Free Merge Sort,
	 * This implementation is inspired by <a href="https://github.com/vigna/fastutil">FastUtil</a> original merge sort, but without the need to allocate a copy of the original Array. It is in Very Unsorted Instances 50% slower then Mergesort, otherwise it as fast.
	 * @param array the array that needs to be sorted
	 * @param length the maxmium size of the array to be sorted
	 * @param comp the Comparator that decides the sorting order
	 */
	public static void memFreeMergeSort(byte[] array, int length, ByteComparator comp) {
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
	public static void memFreeMergeSort(byte[] array, int from, int to, ByteComparator comp) {
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
					byte value = array[j];
					System.arraycopy(array, i, array, i+1, j - i);
					array[i] = value;
					i++;
					j++;
					continue;
				}
				byte[] data = new byte[k - j];
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
	public static byte[] memFreeMergeSort(byte[] array) {
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
	public static void memFreeMergeSort(byte[] array, int length) {
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
	public static void memFreeMergeSort(byte[] array, int from, int to) {
		if(to - from < BASE_THRESHOLD) {
			insertionSort(array, from, to);
			return;
		}
		int mid = (from + to) >>> 1;
		memFreeMergeSort(array, from, mid);
		memFreeMergeSort(array, mid, to);
		if(Byte.compare(array[mid - 1], array[mid]) <= 0)
			return;
		for(int i = from, j = mid, comp;i < j && j < to;) {
			if((comp = Byte.compare(array[i], array[j])) < 0)
				i++;
			else if(comp == 0) swap(array, ++i, j);
			else {
				int k = j;
				for(;k < to - 1 && Byte.compare(array[i], array[k + 1]) > 0;k++);
				if(j == k) {
					swap(array, i++, j);
					continue;
				}
				else if(j + 1 == k) {
					byte value = array[j];
					System.arraycopy(array, i, array, i+1, j - i);
					array[i] = value;
					i++;
					j++;
					continue;
				}
				byte[] data = new byte[k - j];
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
	public static void parallelMemFreeMergeSort(byte[] array, ByteComparator comp) {
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
	public static void parallelMemFreeMergeSort(byte[] array, int length, ByteComparator comp) {
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
	public static void parallelMemFreeMergeSort(byte[] array, int from, int to, ByteComparator comp) {
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
	public static void parallelMemFreeMergeSort(byte[] array) {
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
	public static void parallelMemFreeMergeSort(byte[] array, int length) {
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
	public static void parallelMemFreeMergeSort(byte[] array, int from, int to) {
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
	public static byte[] quickSort(byte[] array, ByteComparator comp) {
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
	public static void quickSort(byte[] array, int length, ByteComparator comp) {
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
	public static void quickSort(byte[] array, int from, int to, ByteComparator comp) {
		int length = to - from;
		if(length <= 0) return;
		if(length < BASE_THRESHOLD) {
			selectionSort(array, from, to, comp);
			return;			
		}
		byte pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8, comp) : medium(array, from, from + (length / 2), to - 1, comp)];
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
	public static byte[] quickSort(byte[] array) {
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
	public static void quickSort(byte[] array, int length) {
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
	public static void quickSort(byte[] array, int from, int to) {
		int length = to - from;
		if(length <= 0) return;
		if(length < BASE_THRESHOLD) {
			selectionSort(array, from, to);
			return;			
		}
		byte pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8) : medium(array, from, from + (length / 2), to - 1)];
		int a = from, b = a, c = to - 1, d = c;
		for(int comp = 0;;swap(array, b++, c--)) {
			for(;b<=c && (comp = Byte.compare(array[b], pivot)) <= 0;b++) {
				if(comp == 0) swap(array, a++, b);
			}
			for(;c>=b && (comp = Byte.compare(array[c], pivot)) >= 0;c--) {
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
	public static void parallelQuickSort(byte[] array, ByteComparator comp) {
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
	public static void parallelQuickSort(byte[] array, int length, ByteComparator comp) {
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
	public static void parallelQuickSort(byte[] array, int from, int to, ByteComparator comp) {
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
	public static void parallelQuickSort(byte[] array) {
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
	public static void parallelQuickSort(byte[] array, int length) {
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
	public static void parallelQuickSort(byte[] array, int from, int to) {
		if(SanityChecks.canParallelTask() && to - from >= PARALLEL_THRESHOLD) {
			SanityChecks.invokeTask(new QuickSortAction(array, from, to));
			return;
		}
		quickSort(array, from, to);
	}
	
	static void swap(byte[] a, int from, int to) {
		byte t = a[from];
		a[from] = a[to];
		a[to] = t;
	}
	
	static void swap(byte[] a, int from, int to, int length) {
		to -= length;
		for(int i = 0;i<length;i++,swap(a, from++, to++));
	}
	
	static int subMedium(byte[] data, int a, int b, int c, int length, ByteComparator comp) {
		return medium(data, medium(data, a, a + length, a + (length * 2), comp), medium(data, b - length, b, b + length, comp), medium(data, c - (length * 2), c - length, c, comp), comp);
	}
	
	static int medium(byte[] data, int a, int b, int c, ByteComparator comp) {
		return comp.compare(data[a], data[b]) < 0 ? (comp.compare(data[b], data[c]) < 0 ? b : comp.compare(data[a], data[c]) < 0 ? c : a) : (comp.compare(data[b], data[c]) > 0 ? b : comp.compare(data[a], data[c]) > 0 ? c : a);
	}
	
	static int subMedium(byte[] data, int a, int b, int c, int length) {
		return medium(data, medium(data, a, a + length, a + (length * 2)), medium(data, b - length, b, b + length), medium(data, c - (length * 2), c - length, c));
	}
	
	static int medium(byte[] data, int a, int b, int c) {
		return Byte.compare(data[a], data[b]) < 0 ? (Byte.compare(data[b], data[c]) < 0 ? b : Byte.compare(data[a], data[c]) < 0 ? c : a) : (Byte.compare(data[b], data[c]) > 0 ? b : Byte.compare(data[a], data[c]) > 0 ? c : a);
	}
	
	static class QuickSortAction extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		byte[] array;
		int from;
		int to;
		
		QuickSortAction(byte[] array, int from, int to)
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
			byte pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8) : medium(array, from, from + (length / 2), to - 1)];
			int a = from, b = a, c = to - 1, d = c;
			for(int comp = 0;;swap(array, b++, c--)) {
				for(;b<=c && (comp = Byte.compare(array[b], pivot)) <= 0;b++) {
					if(comp == 0) swap(array, a++, b);
				}
				for(;c>=b && (comp = Byte.compare(array[c], pivot)) >= 0;c--) {
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
		byte[] array;
		int from;
		int to;
		ByteComparator comp;
		
		QuickSortActionComp(byte[] array, int from, int to, ByteComparator comp)
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
			byte pivot = array[length > 128 ? subMedium(array, from, from + (length / 2), to - 1, length / 8, comp) : medium(array, from, from + (length / 2), to - 1, comp)];
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
		byte[] array;
		byte[] supp;
		int from;
		int to;
		
		MergeSortAction(byte[] array, byte[] supp, int from, int to)
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
			if(Byte.compare(supp[mid - 1], supp[mid]) <= 0)
			{
				System.arraycopy(supp, from, array, from, to - from);
				return;
			}
			for(int p = from, q = mid;from < to;from++) {
				if(q >= to || p < mid && Byte.compare(supp[p], supp[q]) < 0) array[from] = supp[p++];
				else array[from] = supp[q++];
			}
		}
	}
	
	static class MergeSortActionComp extends RecursiveAction {
		private static final long serialVersionUID = 0L;
		byte[] array;
		byte[] supp;
		int from;
		int to;
		ByteComparator comp;
		
		MergeSortActionComp(byte[] array, byte[] supp, int from, int to, ByteComparator comp)
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
		byte[] array;
		int from;
		int to;
		
		MemFreeMergeSortAction(byte[] array, int from, int to)
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
			if(Byte.compare(array[mid - 1], array[mid]) <= 0)
				return;
			for(int i = from, j = mid, comp;i < j && j < to;) {
				if((comp = Byte.compare(array[i], array[j])) < 0)
					i++;
				else if(comp == 0) swap(array, ++i, j);
				else {
					int k = j;
					for(;k < to - 1 && Byte.compare(array[i], array[k + 1]) > 0;k++);
					if(j == k) {
						swap(array, i++, j);
						continue;
					}
					else if(j + 1 == k) {
						byte value = array[j];
						System.arraycopy(array, i, array, i+1, j - i);
						array[i] = value;
						i++;
						j++;
						continue;
					}
					byte[] data = new byte[k - j];
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
		byte[] array;
		int from;
		int to;
		ByteComparator comp;
		
		MemFreeMergeSortActionComp(byte[] array, int from, int to, ByteComparator comp)
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
						byte value = array[j];
						System.arraycopy(array, i, array, i+1, j - i);
						array[i] = value;
						i++;
						j++;
						continue;
					}
					byte[] data = new byte[k - j];
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