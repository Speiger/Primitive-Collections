package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.maps.interfaces.Int2IntOrderedMap;
import speiger.src.collections.tests.OrderedMapTests;

@SuppressWarnings("javadoc")
public abstract class BaseInt2IntOrderedMapTest extends BaseInt2IntMapTest
{
	@Override
	public abstract Int2IntOrderedMap createMap(int[] keys, int[] values);
	@Override
	public abstract Int2IntOrderedMap createEmptyMap();
	
	public EnumSet<OrderedMapTests> getValidOrderedMapTests() { return EnumSet.allOf(OrderedMapTests.class); }

	@Test
	public void testPutMove()
	{
		if(!getValidOrderedMapTests().contains(OrderedMapTests.PUT_MOVE)) return;
		Int2IntOrderedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
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
		if(!getValidOrderedMapTests().contains(OrderedMapTests.MOVE)) return;
		Int2IntOrderedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
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
		if(!getValidOrderedMapTests().contains(OrderedMapTests.GET_MOVE)) return;
		Int2IntOrderedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
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
		if(!getValidOrderedMapTests().contains(OrderedMapTests.FIRST)) return;
		Int2IntOrderedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(map.pollFirstIntKey(), 0);
	}
	
	@Test
	public void testLast()
	{
		if(!getValidOrderedMapTests().contains(OrderedMapTests.LAST)) return;
		Int2IntOrderedMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(map.pollLastIntKey(), 99);
	}
}
