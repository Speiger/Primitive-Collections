package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.tests.SortedSetTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntSortedSetTest extends BaseIntCollectionTest
{
	@Override
	protected abstract IntSortedSet create(int[] data);
	
	protected EnumSet<SortedSetTest> getValidSortedSetTests() { return EnumSet.allOf(SortedSetTest.class); }
	
	@Test
	public void peekTest() {
		if(getValidSortedSetTests().contains(SortedSetTest.PEEK)) {
			IntSortedSet set = create(TEST_ARRAY);
			Assert.assertEquals(set.firstInt(), 0);
			Assert.assertEquals(set.lastInt(), 99);
		}
	}
	
	@Test
	public void pollTest() {
		if(getValidSortedSetTests().contains(SortedSetTest.POLL)) {
			IntSortedSet set = create(TEST_ARRAY);
			for(int i = 0;i<100;i++)
			{
				Assert.assertEquals(i, set.pollFirstInt());
			}
			set = create(TEST_ARRAY);
			for(int i = 99;i>=0;i--)
			{
				Assert.assertEquals(i, set.pollLastInt());
			}
		}
	}
	
	@Test
	public void subSetTest() {
		if(getValidSortedSetTests().contains(SortedSetTest.SUB_SET)) {
			IntSortedSet set = create(TEST_ARRAY);
			IntSortedSet subSet = set.subSet(25, 75);
			Assert.assertTrue(subSet.remove(50));
			Assert.assertFalse(subSet.remove(50));
			Assert.assertFalse(subSet.contains(20));
			Assert.assertFalse(subSet.contains(80));
		}
	}
	
	@Test
	public void headSetTest() {
		if(getValidSortedSetTests().contains(SortedSetTest.HEAD_SET)) {
			IntSortedSet set = create(TEST_ARRAY);
			IntSortedSet subSet = set.headSet(75);
			Assert.assertTrue(subSet.remove(50));
			Assert.assertFalse(subSet.remove(50));
			Assert.assertFalse(subSet.contains(80));
		}
	}
	
	@Test
	public void tailSetTest() {
		if(getValidSortedSetTests().contains(SortedSetTest.TAIL_SET)) {
			IntSortedSet set = create(TEST_ARRAY);
			IntSortedSet subSet = set.tailSet(25);
			Assert.assertTrue(subSet.remove(50));
			Assert.assertFalse(subSet.remove(50));
			Assert.assertFalse(subSet.contains(20));
		}
	}
}