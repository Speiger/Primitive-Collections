package speiger.src.collections.ints.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.tests.ListTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntListTest extends BaseIntCollectionTest
{
	@Override
	protected abstract IntList create(int[] data);
	protected EnumSet<ListTest> getValidListTests() { return EnumSet.allOf(ListTest.class); }
	
	@Test
	public void testAddIndex() {
		if(getValidListTests().contains(ListTest.ADD_INDEX)) return;
		IntList list = create(TEST_ARRAY);
		list.add(50, -10);
		Assert.assertEquals(TEST_ARRAY.length+1, list.size());
		Assert.assertEquals(-10, list.getInt(50));
	}
	
	@Test
	public void testAddList() {
		if(getValidListTests().contains(ListTest.ADD_LIST)) return;
		IntList list = create(TEST_ARRAY);
		IntList adding = create(ADD_ARRAY);
		list.addAll(adding);
		for(int i = 0;i<ADD_ARRAY.length;i++) Assert.assertEquals(ADD_ARRAY[i], list.getInt(TEST_ARRAY.length + i));
	}
	
	@Test
	public void testAddIndexCollection() {
		if(getValidListTests().contains(ListTest.ADD_INDEX_COLLECTION)) return;
		IntList list = create(TEST_ARRAY);
		IntList adding = create(ADD_ARRAY);
		list.addAll(0, IntCollections.unmodifiable(adding));
		for(int i = 0;i<ADD_ARRAY.length;i++) Assert.assertEquals(ADD_ARRAY[i], list.getInt(i));
	}
	
	@Test
	public void testAddIndexList() {
		if(getValidListTests().contains(ListTest.ADD_INDEX_LIST)) return;
		IntList list = create(TEST_ARRAY);
		IntList adding = create(ADD_ARRAY);
		list.addAll(adding);
		for(int i = 0;i<ADD_ARRAY.length;i++) Assert.assertEquals(ADD_ARRAY[i], list.getInt(TEST_ARRAY.length + i));
	}
	
	@Test
	public void testAddElements() {
		if(getValidListTests().contains(ListTest.ADD_ELEMENTS)) return;
		IntList list = create(TEST_ARRAY);
		IntList adding = create(ADD_ARRAY);
		list.addAll(0, adding);
		for(int i = 0;i<ADD_ARRAY.length;i++) Assert.assertEquals(ADD_ARRAY[i], list.getInt(i));
	}
	
	@Test
	@SuppressWarnings("deprecation")	
	public void testIndex() {
		if(getValidListTests().contains(ListTest.INDEX)) return;
		IntList list = create(TEST_ARRAY);
		Assert.assertEquals(50, list.indexOf(50));
		Assert.assertEquals(25, list.indexOf(25));
		Assert.assertEquals(75, list.indexOf(75));
		Assert.assertEquals(50, list.indexOf(Integer.valueOf(50)));
		Assert.assertEquals(25, list.indexOf(Integer.valueOf(25)));
		Assert.assertEquals(75, list.indexOf(Integer.valueOf(75)));
	}
	
	@Test
	@SuppressWarnings("deprecation")	
	public void testLastIndex() {
		if(getValidListTests().contains(ListTest.LAST_INDEX)) return;
		IntList list = create(TEST_ARRAY);
		Assert.assertEquals(50, list.lastIndexOf(50));
		Assert.assertEquals(25, list.lastIndexOf(25));
		Assert.assertEquals(75, list.lastIndexOf(75));
		Assert.assertEquals(50, list.lastIndexOf(Integer.valueOf(50)));
		Assert.assertEquals(25, list.lastIndexOf(Integer.valueOf(25)));
		Assert.assertEquals(75, list.lastIndexOf(Integer.valueOf(75)));
	}
	
	@Test
	public void testGet() {
		if(getValidListTests().contains(ListTest.GET)) return;
		IntList list = create(TEST_ARRAY);
		for(int i = 0;i<list.size();i++) Assert.assertEquals(TEST_ARRAY[i], list.getInt(i));
	}
	
	@Test
	public void testGetElements() {
		if(getValidListTests().contains(ListTest.GET_ELEMENTS)) return;
		int[] elements = create(TEST_ARRAY).extractElements(25, 75);
		for(int i = 0;i<elements.length;i++) Assert.assertEquals(TEST_ARRAY[i+25], elements[i]);
	}
	
	@Test
	public void testSet() {
		if(getValidListTests().contains(ListTest.SET)) return;
		IntList list = create(TEST_ARRAY);
		Assert.assertEquals(25, list.set(25, -124));
		Assert.assertEquals(-124, list.getInt(25));
	}
	
	@Test
	public void testReSize() {
		if(getValidListTests().contains(ListTest.RE_SIZE)) return;
		IntList list = create(TEST_ARRAY);
		Assert.assertEquals(TEST_ARRAY.length, list.size());
		list.size(25);
		Assert.assertEquals(25, list.size());
	}
	
	@Test
	public void testSort() {
		if(getValidListTests().contains(ListTest.SORT)) return;
		IntList list = create(TEST_ARRAY);
		Collections.shuffle(list);
		Assert.assertFalse(Arrays.equals(TEST_ARRAY, list.toIntArray()));
		list.sort(null);
		Assert.assertArrayEquals(TEST_ARRAY, list.toIntArray());
		Collections.shuffle(list);
		Assert.assertFalse(Arrays.equals(TEST_ARRAY, list.toIntArray()));
		list.unstableSort(null);
		Assert.assertArrayEquals(TEST_ARRAY, list.toIntArray());
	}
	
	@Test
	public void testReplace() {
		if(getValidListTests().contains(ListTest.REPLACE)) return;
		IntList list = create(TEST_ARRAY);
		for(int i = 0;i<list.size();i++) Assert.assertEquals(TEST_ARRAY[i], list.getInt(i));
		list.replaceInts(T -> 99 - T);
		for(int i = 0;i<list.size();i++) Assert.assertEquals(TEST_ARRAY[TEST_ARRAY.length - i - 1], list.getInt(i));
	}
	
	@Test
	public void testListRemove() {
		if(getValidListTests().contains(ListTest.REMOVE)) return;
		IntList list = create(TEST_ARRAY);
		list.remInt(50);
		Assert.assertEquals(TEST_ARRAY.length - 1, list.size());
		Assert.assertFalse(list.contains(50));
	}
	
	@Test
	public void testRemoveElements() {
		if(getValidListTests().contains(ListTest.REMOVE_ELEMENTS)) return;
		IntList list = create(TEST_ARRAY);
		list.removeElements(25, 75);
		Assert.assertEquals(TEST_ARRAY.length - 50, list.size());
		for(int i = 0;i<50;i++) Assert.assertEquals(TEST_ARRAY[i + (i < 25 ? 0 : 50)], list.getInt(i));
	}
	
	@Test
	public void testExtractElements() {
		if(getValidListTests().contains(ListTest.EXTRACT_ELEMENTS)) return;
		IntList list = create(TEST_ARRAY);
		int[] elements = list.extractElements(25, 75);
		for(int i = 0;i<elements.length;i++) {
			Assert.assertEquals(TEST_ARRAY[i + 25], elements[i]);
			Assert.assertEquals(TEST_ARRAY[i + (i < 25 ? 0 : 50)], list.getInt(i));
		}
	}
	
	@Test
	public void testListIterator() {
		if(getValidListTests().contains(ListTest.LIST_ITERATOR)) return;
		IntList list = create(TEST_ARRAY);
		IntListIterator iter = list.listIterator(50);
		iter.nextInt();
		iter.set(-250);
		iter.nextInt();
		iter.add(50);
		Assert.assertEquals(-250, list.getInt(50));
		Assert.assertEquals(50, list.getInt(51));
	}
	
	@Test
	public void testSubList() {
		if(getValidListTests().contains(ListTest.SUB_LIST)) return;
		IntList list = create(TEST_ARRAY);
		IntList subList = list.subList(25, 75);
		Assert.assertEquals(50, subList.size());
		for(int i = 0;i<subList.size();i++) Assert.assertEquals(TEST_ARRAY[i + 25], subList.getInt(i));
	}
}
