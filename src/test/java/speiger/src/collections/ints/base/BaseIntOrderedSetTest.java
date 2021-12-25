package speiger.src.collections.ints.base;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.tests.OrderedSetTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntOrderedSetTest extends BaseIntCollectionTest
{
	@Override
	protected abstract IntOrderedSet create(int[] data);
	
	protected EnumSet<OrderedSetTest> getValidOrderedSetTests() { return EnumSet.allOf(OrderedSetTest.class); }
	
	@Test
	public void addMoveTest() {
		if(getValidOrderedSetTests().contains(OrderedSetTest.ADD_MOVE)) {
			IntOrderedSet set = create(TEST_ARRAY);
			Assert.assertTrue(set.addAndMoveToFirst(1050));
			Assert.assertFalse(set.addAndMoveToLast(5));
		}
	}
	
	@Test
	public void moveTest() {
		if(getValidOrderedSetTests().contains(OrderedSetTest.MOVE)) {
			IntOrderedSet set = create(TEST_ARRAY);
			Assert.assertTrue(set.moveToFirst(5));
			Assert.assertFalse(set.moveToFirst(5));
			Assert.assertTrue(set.moveToLast(5));
			Assert.assertFalse(set.moveToLast(5));
		}
	}
	
	@Test
	public void peekTest() {
		if(getValidOrderedSetTests().contains(OrderedSetTest.PEEK)) {
			IntOrderedSet set = create(TEST_ARRAY);
			Assert.assertEquals(set.firstInt(), 0);
			Assert.assertEquals(set.lastInt(), 99);
		}
	}
	
	@Test
	public void pollTest() {
		if(getValidOrderedSetTests().contains(OrderedSetTest.POLL)) {
			IntOrderedSet set = create(TEST_ARRAY);
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
}