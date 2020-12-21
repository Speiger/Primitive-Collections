package speiger.src.collections.ints.base;

import java.util.EnumSet;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.tests.IterableTest;

public abstract class BaseIntIterableTest
{
	protected static final int[] TEST_EMPTY = new int[0];
	protected static final int[] TEST_ARRAY = IntStream.range(0, 100).toArray();
	
	protected abstract IntIterable create(int[] data);
	
	protected EnumSet<IterableTest> getValidIterableTests() { return EnumSet.allOf(IterableTest.class); }
	
	@Test
	public void testForEach() {
		if(getValidIterableTests().contains(IterableTest.FOR_EACH)) {
			create(TEST_ARRAY).forEach(T -> Assert.assertTrue(T >= 0 && T < 100));
		}
	}
	
	@Test
	public void testIteratorForEach() {
		if(getValidIterableTests().contains(IterableTest.ITERATOR_FOR_EACH)) {
			create(TEST_ARRAY).iterator().forEachRemaining(T -> Assert.assertTrue(T >= 0 && T < 100));
		}
	}
	
	@Test
	public void testSkip() {
		if(getValidIterableTests().contains(IterableTest.ITERATOR_SKIP)) {
			IntIterator iter = create(TEST_ARRAY).iterator();
			Assert.assertEquals(50, iter.skip(50));
			Assert.assertNotEquals(100, iter.skip(100));
		}
	}
	
	@Test
	public void testIteratorLoop() {
		if(getValidIterableTests().contains(IterableTest.ITERATOR_LOOP)) {
			for(int entry : create(TEST_ARRAY)) Assert.assertTrue(entry >= 0 && entry < 100);
			
			for(IntIterator iter = create(TEST_ARRAY).iterator();iter.hasNext();) {
				int entry = iter.nextInt();
				Assert.assertTrue(entry >= 0 && entry < 100);
			}
		}
	}
	
	@Test
	public void testIteratorRemovalLoop() {
		if(getValidIterableTests().contains(IterableTest.ITERATOR_REMOVAL)) {
			for(IntIterator iter = create(TEST_ARRAY).iterator();iter.hasNext();) {
				int entry = iter.nextInt();
				Assert.assertTrue(entry >= 0 && entry < 100);
				iter.remove();
			}
		}
	}
}