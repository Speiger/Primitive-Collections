package speiger.src.collections.ints.utils;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class SortingTests
{
	public static final int[] SMALL_TEST = IntStream.range(0, 5000).toArray();
	public static final int[] LARGE_TEST = IntStream.range(0, 50000).toArray();
	
	@Test
	public void testInsertionSort() {
		int[] array = IntArrays.shuffle(SMALL_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.insertionSort(array);
		Assert.assertArrayEquals(SMALL_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.insertionSort(array, Integer::compare);
		Assert.assertArrayEquals(SMALL_TEST, array);
	}
	
	@Test
	public void testMergeSort() {
		int[] array = IntArrays.shuffle(SMALL_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.mergeSort(array);
		Assert.assertArrayEquals(SMALL_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.mergeSort(array, Integer::compare);
		Assert.assertArrayEquals(SMALL_TEST, array);
	}
	
	@Test
	public void testMemFreeMergeSort() {
		int[] array = IntArrays.shuffle(SMALL_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.memFreeMergeSort(array);
		Assert.assertArrayEquals(SMALL_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.memFreeMergeSort(array, Integer::compare);
		Assert.assertArrayEquals(SMALL_TEST, array);
	}
	
	@Test
	public void testSelectionSortSort() {
		int[] array = IntArrays.shuffle(SMALL_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.selectionSort(array);
		Assert.assertArrayEquals(SMALL_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.selectionSort(array, Integer::compare);
		Assert.assertArrayEquals(SMALL_TEST, array);
	}
	
	@Test
	public void testQuickSortSort() {
		int[] array = IntArrays.shuffle(SMALL_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.quickSort(array);
		Assert.assertArrayEquals(SMALL_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, SMALL_TEST));
		IntArrays.quickSort(array, Integer::compare);
		Assert.assertArrayEquals(SMALL_TEST, array);
	}
	
	@Test
	public void testParallelMergeSort() {
		int[] array = IntArrays.shuffle(LARGE_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelMergeSort(array);
		Assert.assertArrayEquals(LARGE_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelMergeSort(array, Integer::compare);
		Assert.assertArrayEquals(LARGE_TEST, array);
	}
	
	@Test
	public void testParallelMemFreeMergeSort() {
		int[] array = IntArrays.shuffle(LARGE_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelMemFreeMergeSort(array);
		Assert.assertArrayEquals(LARGE_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelMemFreeMergeSort(array, Integer::compare);
		Assert.assertArrayEquals(LARGE_TEST, array);
	}
	
	@Test
	public void testParallelQuickSort() {
		int[] array = IntArrays.shuffle(LARGE_TEST.clone());
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelQuickSort(array);
		Assert.assertArrayEquals(LARGE_TEST, array);
		IntArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, LARGE_TEST));
		IntArrays.parallelQuickSort(array, Integer::compare);
		Assert.assertArrayEquals(LARGE_TEST, array);
	}

}
