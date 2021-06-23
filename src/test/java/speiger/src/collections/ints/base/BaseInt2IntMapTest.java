package speiger.src.collections.ints.base;

import java.util.EnumSet;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.collections.tests.MapTests;

@SuppressWarnings("javadoc")
public abstract class BaseInt2IntMapTest
{
	protected static final IntStrategy STRATEGY = new Strategy();
	protected static final int[] TEST_ARRAY = IntStream.range(0, 100).toArray();
	protected static final int[] PUT_ARRAY = IntStream.range(512, 1024).toArray();
	protected static final int[] PUT_VALUE_ARRAY = IntStream.range(0, 512).toArray();
	
	public abstract Int2IntMap createMap(int[] keys, int[] values);
	public abstract Int2IntMap createEmptyMap();
	
	public EnumSet<MapTests> getValidMapTests() { return EnumSet.allOf(MapTests.class); }
	
	@Test
	public void testPut()
	{
		if(!getValidMapTests().contains(MapTests.PUT)) return;
		Int2IntMap putMap = createMap(PUT_ARRAY, PUT_VALUE_ARRAY);
		Assert.assertEquals(PUT_ARRAY.length, putMap.size());
		Assert.assertEquals(0, putMap.put(0, 512));
		Assert.assertEquals(1, putMap.put(513, 2));
		Assert.assertEquals(PUT_ARRAY.length + 1, putMap.size());
		Assert.assertEquals(512, putMap.addTo(0, 1));
		Assert.assertEquals(513, putMap.get(0));
	}
	
	@Test
	public void testPutAll()
	{
		if(!getValidMapTests().contains(MapTests.PUT_ALL)) return;
		Int2IntMap putMap = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(TEST_ARRAY.length, putMap.size());
		putMap.putAll(createMap(PUT_ARRAY, PUT_VALUE_ARRAY));
		Assert.assertEquals(TEST_ARRAY.length + PUT_ARRAY.length, putMap.size());
		putMap = createMap(TEST_ARRAY, TEST_ARRAY);
		putMap.putAll(createMap(PUT_VALUE_ARRAY, PUT_ARRAY));		
		Assert.assertEquals(PUT_ARRAY.length, putMap.size());
	}
	
	@Test
	public void testContains()
	{
		if(!getValidMapTests().contains(MapTests.CONTAINS)) return;
		Int2IntMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertTrue(map.containsKey(0));
		Assert.assertFalse(map.containsKey(-1));
		Assert.assertTrue(map.containsKey(Integer.valueOf(10)));
		Assert.assertFalse(map.containsKey(Short.valueOf((short)10)));
		Assert.assertTrue(map.containsValue(50));
		Assert.assertFalse(map.containsValue(150));
		Assert.assertTrue(map.containsValue(Integer.valueOf(10)));
		Assert.assertFalse(map.containsValue(Short.valueOf((short)10)));
	}
	
	@Test
	public void testReplace()
	{
		if(!getValidMapTests().contains(MapTests.REPLACE)) return;
		Int2IntMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(0, map.replace(0, 512));
		Assert.assertEquals(512, map.get(0));
		Assert.assertTrue(map.replace(0, 512, 0));
		Assert.assertFalse(map.replace(0, 512, 0));
		map = createMap(TEST_ARRAY, TEST_ARRAY);
		map.replaceInts((K, V) -> 99 - V);
		Assert.assertEquals(99, map.get(0));
		Assert.assertEquals(0, map.get(99));
	}
	
	@Test
	public void testCompute()
	{
		if(!getValidMapTests().contains(MapTests.COMPUTE)) return;
		Int2IntMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(512, map.computeInt(0, (K, V) -> 512));
		Assert.assertEquals(512, map.get(0));
		Assert.assertEquals(512, map.computeIntIfAbsent(0, T -> 0));
		Assert.assertEquals(0, map.computeIntIfPresent(0, (T, V) -> 0));
		Assert.assertEquals(0, map.computeIntIfAbsent(-10, T -> 0));
	}
	
	@Test
	public void testMerge()
	{
		if(!getValidMapTests().contains(MapTests.MERGE)) return;
		Int2IntMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		Assert.assertEquals(50, map.mergeInt(1, 50, Integer::max));
		Assert.assertEquals(2, map.mergeInt(2, 50, Integer::min));
	}
	
	@Test
	public void testGet()
	{
		if(!getValidMapTests().contains(MapTests.GET)) return;
		Int2IntMap map = createMap(TEST_ARRAY, TEST_ARRAY);
		for(int i = 0;i<TEST_ARRAY.length;i++)
		{
			Assert.assertEquals(i, map.get(i));
		}
	}
	
	@Test
	public void testIterators()
	{
		if(!getValidMapTests().contains(MapTests.ITERATORS)) return;
		Int2IntMap map = createMap(PUT_VALUE_ARRAY, PUT_ARRAY);
		map.forEach((K, V) -> Assert.assertEquals(PUT_ARRAY[K], V));
		map.int2IntEntrySet().forEach(T -> Assert.assertEquals(PUT_ARRAY[T.getIntKey()], T.getIntValue()));
		map.keySet().forEach(T -> Assert.assertEquals(PUT_VALUE_ARRAY[T], T));
		map.values().forEach(T -> Assert.assertTrue(T >= 512 && T <= 1024));
		Assert.assertTrue(map.keySet().contains(50));
		Assert.assertFalse(map.keySet().contains(-50));
		Assert.assertTrue(map.values().contains(1000));
		Assert.assertFalse(map.values().contains(-1000));
	}
	
	@Test
	public void testRemove()
	{
		if(!getValidMapTests().contains(MapTests.REMOVE)) return;
		Int2IntMap map = createMap(PUT_VALUE_ARRAY, PUT_ARRAY);
		Assert.assertEquals(PUT_ARRAY[50], map.remInt(PUT_VALUE_ARRAY[50]));
		Assert.assertTrue(map.remove(PUT_VALUE_ARRAY[51], PUT_ARRAY[51]));
	}
	
	public static class Strategy implements IntStrategy
	{
		@Override
		public int hashCode(int o)
		{
			return o;
		}

		@Override
		public boolean equals(int key, int value)
		{
			return key == value;
		}
		
	}
}