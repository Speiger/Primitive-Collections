package speiger.src.collections.ints.base;


import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.queues.IntPriorityQueue;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.tests.IterableTest;
import speiger.src.collections.tests.PriorityQueueTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntPriorityQueueTest extends BaseIntIterableTest
{
	@Override
	protected abstract IntPriorityQueue create(int[] data);
	@Override
	protected EnumSet<IterableTest> getValidIterableTests() { return EnumSet.of(IterableTest.FOR_EACH, IterableTest.ITERATOR_FOR_EACH, IterableTest.ITERATOR_LOOP, IterableTest.ITERATOR_SKIP); }
	protected EnumSet<PriorityQueueTest> getValidPriorityQueueTests() { return EnumSet.allOf(PriorityQueueTest.class); }
	protected boolean isUnsortedRead() { return true; }
	
	@Test
	public void testEnqueue() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.IN_OUT)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			for(int i = 0;i<100;i++) {
				Assert.assertEquals(i, queue.dequeue());
			}
			queue = create(TEST_ARRAY);
			for(int i = 0;i<100;i++) {
				Assert.assertEquals(i, queue.dequeue());
			}
		}
	}
	
	@Test
	public void testPeek() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.PEEK)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			if(isUnsortedRead()) {
				for(int i = 0;i<100;i++) {
					int value = queue.peek(i);
					Assert.assertTrue(value >= 0 && value < 100);
				}
			}
			else {
				for(int i = 0;i<100;i++) {
					Assert.assertEquals(i, queue.peek(i));
				}
			}
		}
	}
	
	@Test
	public void testContains() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.CONTAINS)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			Assert.assertEquals(true, queue.contains(40));
			Assert.assertEquals(false, queue.contains(140));
		}
	}
	
	@Test
	public void testRemove() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.REMOVE)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			queue.removeFirst(40);
			for(int i = 0;i<99;i++) {
				if(i >= 40) Assert.assertEquals(i + 1, queue.dequeue());
				else Assert.assertEquals(i, queue.dequeue());
			}
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			queue.removeLast(40);
			for(int i = 0;i<99;i++) {
				if(i >= 40) Assert.assertEquals(i + 1, queue.dequeue());
				else Assert.assertEquals(i, queue.dequeue());
			}
		}
	}
	
	@Test
	public void testUnorderedPeek() {
		EnumSet<PriorityQueueTest> valid = getValidPriorityQueueTests();
		if(valid.contains(PriorityQueueTest.IN_OUT) && valid.contains(PriorityQueueTest.PEEK) && !isUnsortedRead()) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueue(i);
			}
			for(int i = 0;i<25;i++) {
				queue.dequeue();
				queue.enqueue(i);
			}
			for(int i = 0;i<100;i++) {
				Assert.assertEquals((i+25) % 100, queue.peek(i));
			}
		}
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testToArray() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.TO_ARRAY)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			if(isUnsortedRead()) {
				for(int i = 0;i<100;i++) {
					queue.enqueue(i);
				}
				int[] array = queue.toIntArray();
				IntArrays.stableSort(array);
				Assert.assertArrayEquals(TEST_ARRAY, array);
				int[] data = queue.toIntArray(new int[100]);
				int[] nonData = queue.toIntArray(new int[0]);
				IntArrays.stableSort(data);
				IntArrays.stableSort(nonData);				
				Assert.assertArrayEquals(array, data);
				Assert.assertArrayEquals(array, nonData);
			}
			else {
				int[] shiftPrimArray = new int[100];
				for(int i = 0; i < 100; i++) {
					queue.enqueue(i);
					shiftPrimArray[(i+80) % 100] = i;
				}
				Assert.assertArrayEquals(TEST_ARRAY, queue.toIntArray());
				Assert.assertArrayEquals(TEST_ARRAY, queue.toIntArray(new int[100]));
				Assert.assertArrayEquals(TEST_ARRAY, queue.toIntArray(null));
				IntPriorityQueue other = create(queue.toIntArray());
				for(int i = 0;i<20;i++) {
					other.dequeue();
					other.enqueue(i);
				}
				Assert.assertArrayEquals(shiftPrimArray, other.toIntArray());
				Assert.assertArrayEquals(shiftPrimArray, other.toIntArray(new int[100]));
			}
		}
	}
	
	@Test
	public void testCopy() {
		if(!getValidPriorityQueueTests().contains(PriorityQueueTest.COPY)) return;
		IntPriorityQueue queue = create(TEST_ARRAY);
		IntPriorityQueue copy = queue.copy();
		Assert.assertFalse(queue == copy);
		Assert.assertEquals(queue, copy);
	}
}
