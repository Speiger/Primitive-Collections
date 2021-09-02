package speiger.src.collections.objects.utils;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectLinkedList;
import speiger.src.collections.objects.lists.ObjectList;

@SuppressWarnings("javadoc")
public class StringSortTest
{
	public static final String[] NAMES = loadNames();
	
	private static final String[] loadNames()
	{
		List<String> list = new ArrayList<>();
		try(BufferedReader reader = Files.newBufferedReader(new File("src/test/resources/testAssets/strings/names.txt").toPath()))
		{
			reader.lines().forEach(list::add);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		list.sort(null);
		return list.toArray(new String[list.size()]);
	}
	
	@Test
	public void testArrayListSort() {
		ObjectList<String> list = new ObjectArrayList<>(NAMES.clone());
		ObjectLists.shuffle(list);
		Assert.assertFalse(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		list.sort(String::compareTo);
		Assert.assertTrue(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		ObjectLists.shuffle(list);
		Assert.assertFalse(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		list.sort(null);
		Assert.assertTrue(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
	}
	
	@Test
	public void testLinkedListSort() {
		ObjectList<String> list = new ObjectLinkedList<>(NAMES.clone());
		ObjectLists.shuffle(list);
		Assert.assertFalse(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		list.sort(String::compareTo);
		Assert.assertTrue(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		ObjectLists.shuffle(list);
		Assert.assertFalse(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
		list.sort(null);
		Assert.assertTrue(Arrays.equals(list.toArray(new String[list.size()]), NAMES));
	}
	
	@Test
	public void testInsertionSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.insertionSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.insertionSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
	
	@Test
	public void testMergeSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.mergeSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.mergeSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
	
	@Test
	public void testSelectionSortSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.selectionSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.selectionSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
	
	@Test
	public void testQuickSortSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.quickSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.quickSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
	
	@Test
	public void testParallelMergeSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.parallelMergeSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.parallelMergeSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
	
	@Test
	public void testParallelQuickSort() {
		String[] array = ObjectArrays.shuffle(NAMES.clone());
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.parallelQuickSort(array);
		Assert.assertArrayEquals(NAMES, array);
		ObjectArrays.shuffle(array);
		Assert.assertFalse(Arrays.equals(array, NAMES));
		ObjectArrays.parallelQuickSort(array, String::compareTo);
		Assert.assertArrayEquals(NAMES, array);
	}
}
