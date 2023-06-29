package speiger.src.collections.ints.base;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.ints.utils.IntIterators;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.tests.IterableTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntIterableTest
{
	protected static final int[] EMPTY_ARRAY = new int[0];
	protected static final int[] TEST_ARRAY = IntStream.range(0, 100).toArray();
	protected static final int[] DISTINCT_ARRAY = IntStream.range(0, 100).flatMap(T -> Arrays.stream(new int[]{T, T})).toArray();
	
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
	
	@Test
	public void testStreamCount() {
		if(getValidIterableTests().contains(IterableTest.STREAM_COUNT)) {
			long expected = IntStream.of(TEST_ARRAY).filter(T -> T % 2 == 0).count();
			int size = create(TEST_ARRAY).count(T -> T % 2 == 0);
			Assert.assertEquals(expected, size);
		}
	}
	
	@Test
	public void testStreamFilter() {
		if(getValidIterableTests().contains(IterableTest.STREAM_FILTER)) {
			int[] expected = IntStream.of(TEST_ARRAY).filter(T -> T % 2 == 0).toArray();
			int[] actual = IntIterators.pour(create(TEST_ARRAY).filter(T -> T % 2 == 0).iterator()).toIntArray();
			IntArrays.stableSort(actual);
			Assert.assertArrayEquals(expected, actual);
		}
	}
	
	@Test
	public void testStreamFindFirst() {
		if(getValidIterableTests().contains(IterableTest.STREAM_FIND_FIRST)) {
			int expected = IntStream.of(TEST_ARRAY).filter(T -> T / 50 > 0).findFirst().getAsInt();
			int actual = create(TEST_ARRAY).findFirst(T -> T / 50 > 0);
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void teastStreamDistinct() {
		if(getValidIterableTests().contains(IterableTest.STREAM_DISTINCT)) {
			int[] expected = IntStream.of(DISTINCT_ARRAY).distinct().toArray();
			int[] actual = IntIterators.pour(create(DISTINCT_ARRAY).distinct().iterator()).toIntArray();
			IntArrays.stableSort(actual);
			Assert.assertArrayEquals(expected, actual);
		}
	}
	
	@Test
	public void testStreamLimit() {
		if(getValidIterableTests().contains(IterableTest.STREAM_LIMIT)) {
			int[] expected = IntStream.of(TEST_ARRAY).limit(25).toArray();
			int[] actual = IntIterators.pour(create(TEST_ARRAY).limit(25).iterator()).toIntArray();
			Assert.assertEquals(expected.length, actual.length);
		}
	}
	
	@Test
	public void testStreamMap() {
		if(getValidIterableTests().contains(IterableTest.STREAM_MAP)) {
			Integer[] expected = IntStream.of(TEST_ARRAY).mapToObj(Integer::valueOf).toArray(Integer[]::new);
			Integer[] actual = ObjectIterators.pour(create(TEST_ARRAY).map(Integer::valueOf).iterator()).toArray(Integer[]::new);
			ObjectArrays.stableSort(actual);
			Assert.assertArrayEquals(expected, actual);
		}
	}
	
	@Test
	public void testStreamMatches() {
		if(getValidIterableTests().contains(IterableTest.STREAM_MATCHES)) {
			IntIterable iterable = create(TEST_ARRAY);
			Assert.assertTrue(iterable.matchesAll(T -> T >= 0));
			Assert.assertFalse(iterable.matchesAll(T -> T < 0));
			Assert.assertTrue(iterable.matchesAny(T -> T % 2 != 0));
			Assert.assertFalse(iterable.matchesAny(T -> T % 2 >= 2));
			Assert.assertTrue(iterable.matchesNone(T -> T % 2 >= 2));
			Assert.assertFalse(iterable.matchesNone(T -> T % 2 != 0));
		}
	}
	
	@Test
	public void testStreamPeek() {
		if(getValidIterableTests().contains(IterableTest.STREAM_PEEK)) {
			int[] peekCount = new int[2];
			create(TEST_ARRAY).peek(T -> peekCount[0]++).forEach(T -> peekCount[1]++);
			Assert.assertEquals(TEST_ARRAY.length, peekCount[0]);
			Assert.assertEquals(TEST_ARRAY.length, peekCount[1]);
		}
	}
	
	@Test
	public void testStreamPour() {
		if(getValidIterableTests().contains(IterableTest.STREAM_POUR)) {
			int[] expected = TEST_ARRAY;
			int[] actual = create(TEST_ARRAY).pour(new IntArrayList()).toIntArray();
			IntArrays.stableSort(actual);
			Assert.assertArrayEquals(expected, actual);
		}
	}
	
	@Test
	public void testStreamReduce() {
		if(getValidIterableTests().contains(IterableTest.STREAM_REDUCE)) {
			int expected = IntStream.of(TEST_ARRAY).reduce(0, Integer::sum);
			int actual = create(TEST_ARRAY).reduce(0, Integer::sum);
			Assert.assertEquals(expected, actual);
		}
	}
}