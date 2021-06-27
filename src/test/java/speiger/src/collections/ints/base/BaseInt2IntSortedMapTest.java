package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;
import speiger.src.collections.tests.SortedMapTests;

@SuppressWarnings("javadoc")
public abstract class BaseInt2IntSortedMapTest extends BaseInt2IntMapTest
{
	@Override
	public abstract Int2IntSortedMap createMap(int[] keys, int[] values);
	@Override
	public abstract Int2IntSortedMap createEmptyMap();
	
	public EnumSet<SortedMapTests> getValidSortedMapTests() { return EnumSet.allOf(SortedMapTests.class); }
	
	@Test
	public void testPutMove()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.PUT_MOVE)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(0, map.putAndMoveToFirst(120, -1));
		Assert.assertEquals(120, map.firstIntKey());
		Assert.assertEquals(-1, map.firstIntValue());
		Assert.assertEquals(0, map.putAndMoveToLast(121, -2));
		Assert.assertEquals(121, map.lastIntKey());
		Assert.assertEquals(-2, map.lastIntValue());
	}
	
	@Test
	public void testMove()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.MOVE)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertTrue(map.moveToFirst(99));
		Assert.assertFalse(map.moveToFirst(99));
		Assert.assertEquals(99, map.firstIntKey());
		Assert.assertTrue(map.moveToLast(0));
		Assert.assertFalse(map.moveToLast(0));
		Assert.assertEquals(0, map.lastIntKey());
	}
	
	@Test
	public void testGetMove()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.GET_MOVE)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertNotEquals(99, map.firstIntValue());
		Assert.assertEquals(99, map.getAndMoveToFirst(99));
		Assert.assertEquals(99, map.firstIntValue());
		Assert.assertNotEquals(0, map.lastIntValue());
		Assert.assertEquals(0, map.getAndMoveToLast(0));
		Assert.assertEquals(0, map.lastIntValue());
	}
	
	@Test
	public void testFirst()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.FIRST)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(map.pollFirstIntKey(), 0);
	}
	
	@Test
	public void testLast()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.LAST)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(map.pollLastIntKey(), 99);
	}
	
	@Test
	public void testSubMap()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.SUB_MAP)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Int2IntSortedMap subMap = map.subMap(25, 75);
		Assert.assertEquals(50, subMap.remove(50));
		Assert.assertNotEquals(50, subMap.remove(50));
		Assert.assertFalse(subMap.containsKey(20));
		Assert.assertFalse(subMap.containsKey(80));
	}
	
	@Test
	public void testHeadMap()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.HEAD_MAP)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Int2IntSortedMap subMap = map.headMap(75);
		Assert.assertEquals(50, subMap.remove(50));
		Assert.assertNotEquals(50, subMap.remove(50));
		Assert.assertFalse(subMap.containsKey(80));
	}
	
	@Test
	public void testTailMap()
	{
		if(!getValidSortedMapTests().contains(SortedMapTests.TAIL_MAP)) return;
		Int2IntSortedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Int2IntSortedMap subMap = map.tailMap(25);
		Assert.assertEquals(50, subMap.remove(50));
		Assert.assertNotEquals(50, subMap.remove(50));
		Assert.assertFalse(subMap.containsKey(20));
	}
}