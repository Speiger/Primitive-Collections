package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.maps.interfaces.Int2IntNavigableMap;
import speiger.src.collections.tests.NavigableSetTest;

@SuppressWarnings("javadoc")
public abstract class BaseInt2IntNavigableMapTest extends BaseInt2IntSortedMapTest
{
	@Override
	public abstract Int2IntNavigableMap createMap(int[] keys, int[] values);
	@Override
	public abstract Int2IntNavigableMap createEmptyMap();
	
	public EnumSet<NavigableSetTest> getValidNavigableMapTests() { return EnumSet.allOf(NavigableSetTest.class); }
	
	@Test
	public void desendingTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.DESENDING)) {
			Int2IntNavigableMap set = createMap(TEST_ARRAY, TEST_ARRAY).descendingMap();
			Assert.assertEquals(TEST_ARRAY[TEST_ARRAY.length - 1], set.firstIntKey());
			Assert.assertEquals(TEST_ARRAY[0], set.lastIntKey());
		}
	}
	
	@Test
	public void lowerTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.LOWER)) {
			Assert.assertTrue(createMap(TEST_ARRAY, TEST_ARRAY).lowerKey(50) < 50);
		}
	}
	
	@Test
	public void higherTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.HIGHER)) {
			Assert.assertTrue(createMap(TEST_ARRAY, TEST_ARRAY).higherKey(50) > 50);
		}
	}
	
	@Test
	public void ceilTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.CEILING)) {
			Assert.assertTrue(createMap(TEST_ARRAY, TEST_ARRAY).ceilingKey(50) >= 50);
		}
	}
	
	@Test
	public void floorTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.FLOOR)) {
			Assert.assertTrue(createMap(TEST_ARRAY, TEST_ARRAY).floorKey(50) <= 50);
		}
	}
	
	@Test
	public void naviSubSetTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.SUB_SET)) {
			Int2IntNavigableMap set = createMap(TEST_ARRAY, TEST_ARRAY);
			Int2IntNavigableMap subSet = set.subMap(25, 75);
			Assert.assertTrue(subSet.lowerKey(50) < 50);
			Assert.assertTrue(subSet.higherKey(50) > 50);
			Assert.assertTrue(subSet.ceilingKey(50) >= 50);
			Assert.assertTrue(subSet.floorKey(50) <= 50);
		}
	}
	
	@Test
	public void naviHeadSetTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.HEAD_SET)) {
			Int2IntNavigableMap set = createMap(TEST_ARRAY, TEST_ARRAY);
			Int2IntNavigableMap subSet = set.headMap(75);
			Assert.assertTrue(subSet.lowerKey(50) < 50);
			Assert.assertTrue(subSet.higherKey(50) > 50);
			Assert.assertTrue(subSet.ceilingKey(50) >= 50);
			Assert.assertTrue(subSet.floorKey(50) <= 50);			
		}
	}
	
	@Test
	public void naviTailSetTest() {
		if(getValidNavigableMapTests().contains(NavigableSetTest.TAIL_SET)) {
			Int2IntNavigableMap set = createMap(TEST_ARRAY, TEST_ARRAY);
			Int2IntNavigableMap subSet = set.tailMap(25);
			Assert.assertTrue(subSet.lowerKey(50) < 50);
			Assert.assertTrue(subSet.higherKey(50) > 50);
			Assert.assertTrue(subSet.ceilingKey(50) >= 50);
			Assert.assertTrue(subSet.floorKey(50) <= 50);
		}
	}
}
