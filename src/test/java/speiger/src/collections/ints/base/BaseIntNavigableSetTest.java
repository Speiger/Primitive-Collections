package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.tests.NavigableSetTest;

public abstract class BaseIntNavigableSetTest extends BaseIntSortedSetTest
{
	@Override
	protected abstract IntNavigableSet create(int[] data);
	
	protected EnumSet<NavigableSetTest> getValidNavigableSetTests() { return EnumSet.allOf(NavigableSetTest.class); }
	
	@Test
	public void desendingTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.DESENDING)) {
			IntNavigableSet set = create(TEST_ARRAY).descendingSet();
			Assert.assertEquals(TEST_ARRAY[TEST_ARRAY.length - 1], set.firstInt());
			Assert.assertEquals(TEST_ARRAY[0], set.lastInt());
		}
	}
	
	@Test
	public void lowerTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.LOWER)) {
			Assert.assertTrue(create(TEST_ARRAY).lower(50) < 50);
		}
	}
	
	@Test
	public void higherTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.HIGHER)) {
			Assert.assertTrue(create(TEST_ARRAY).higher(50) > 50);
		}
	}
	
	@Test
	public void ceilTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.CEILING)) {
			Assert.assertTrue(create(TEST_ARRAY).ceiling(50) >= 50);
		}
	}
	
	@Test
	public void floorTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.FLOOR)) {
			Assert.assertTrue(create(TEST_ARRAY).floor(50) <= 50);
		}
	}
	
	@Test
	public void naviSubSetTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.SUB_SET)) {
			IntNavigableSet set = create(TEST_ARRAY);
			IntNavigableSet subSet = set.subSet(25, 75);
			Assert.assertTrue(subSet.lower(50) < 50);
			Assert.assertTrue(subSet.higher(50) > 50);
			Assert.assertTrue(subSet.ceiling(50) >= 50);
			Assert.assertTrue(subSet.floor(50) <= 50);
		}
	}
	
	@Test
	public void naviHeadSetTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.HEAD_SET)) {
			IntNavigableSet set = create(TEST_ARRAY);
			IntNavigableSet subSet = set.headSet(75);
			Assert.assertTrue(subSet.lower(50) < 50);
			Assert.assertTrue(subSet.higher(50) > 50);
			Assert.assertTrue(subSet.ceiling(50) >= 50);
			Assert.assertTrue(subSet.floor(50) <= 50);			
		}
	}
	
	@Test
	public void naviTailSetTest() {
		if(getValidNavigableSetTests().contains(NavigableSetTest.TAIL_SET)) {
			IntNavigableSet set = create(TEST_ARRAY);
			IntNavigableSet subSet = set.tailSet(25);
			Assert.assertTrue(subSet.lower(50) < 50);
			Assert.assertTrue(subSet.higher(50) > 50);
			Assert.assertTrue(subSet.ceiling(50) >= 50);
			Assert.assertTrue(subSet.floor(50) <= 50);
		}
	}
}