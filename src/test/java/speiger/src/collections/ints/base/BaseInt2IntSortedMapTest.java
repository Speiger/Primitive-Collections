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