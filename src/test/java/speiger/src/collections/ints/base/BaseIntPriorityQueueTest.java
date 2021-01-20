package speiger.src.collections.ints.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.queues.IntPriorityQueue;
import speiger.src.collections.tests.IterableTest;
import speiger.src.collections.tests.PriorityQueueTest;

public abstract class BaseIntPriorityQueueTest extends BaseIntIterableTest
{
	@Override
	protected abstract IntPriorityQueue create(int[] data);
	@Override
	protected EnumSet<IterableTest> getValidIterableTests() { return EnumSet.of(IterableTest.FOR_EACH, IterableTest.ITERATOR_FOR_EACH, IterableTest.ITERATOR_LOOP, IterableTest.ITERATOR_SKIP); }
	protected EnumSet<PriorityQueueTest> getValidPriorityQueueTests() { return EnumSet.allOf(PriorityQueueTest.class); }
	
	@Test
	public void testEnqueue() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.IN_OUT)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueueInt(i);
				Assert.assertEquals(i, queue.lastInt());
			}
			for(int i = 0;i<100;i++) {
				Assert.assertEquals(i, queue.firstInt());
				Assert.assertEquals(99, queue.lastInt());
				Assert.assertEquals(i, queue.dequeueInt());
			}
		}
	}
	
	@Test
	public void testPeek() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.PEEK)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueueInt(i);
				Assert.assertEquals(i, queue.lastInt());
			}
			for(int i = 0;i<100;i++) {
				assertEquals(i, queue.peekInt(i));
			}
		}
	}
	
	@Test
	public void testRemove() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.REMOVE)) {
			IntPriorityQueue queue = create(EMPTY_ARRAY);
			for(int i = 0;i<100;i++) {
				queue.enqueueInt(i);
				Assert.assertEquals(i, queue.lastInt());
			}
			queue.removeInt(40);
			for(int i = 0;i<99;i++) {
				if(i >= 40) assertEquals(i + 1, queue.dequeueInt());
				else assertEquals(i, queue.dequeueInt());
			}
			for(int i = 0;i<100;i++) {
				queue.enqueueInt(i);
				Assert.assertEquals(i, queue.lastInt());
			}
			queue.removeLastInt(40);
			for(int i = 0;i<99;i++) {
				if(i >= 40) assertEquals(i + 1, queue.dequeueInt());
				else assertEquals(i, queue.dequeueInt());
			}
		}
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testToArray() {
		if(getValidPriorityQueueTests().contains(PriorityQueueTest.TO_ARRAY)) {
			IntPriorityQueue q = create(EMPTY_ARRAY);
			Integer[] ref = new Integer[100];
			Integer[] shiftArray = new Integer[100];
			int[] primRef = new int[100];
			int[] shiftPrimArray = new int[100];
			for(int i = 0; i < 100; i++) {
				q.enqueue(i);
				assertEquals(i, q.lastInt());
				ref[i] = Integer.valueOf(i);
				primRef[i] = i;
				shiftPrimArray[(i+80) % 100] = i;
				shiftArray[(i+80) % 100] = Integer.valueOf(i);
			}
			assertArrayEquals(q.toArray(), ref);
			assertArrayEquals(q.toArray(new Integer[100]), ref);
			assertArrayEquals(q.toArray(null), ref);
			assertArrayEquals(q.toIntArray(), primRef);
			assertArrayEquals(q.toIntArray(new int[100]), primRef);
			assertArrayEquals(q.toIntArray(null), primRef);
			IntPriorityQueue other = create(q.toIntArray());
			for(int i = 0;i<100;i++) {
				assertEquals(other.peekInt(i), primRef[i]);
			}
			for(int i = 0;i<20;i++) {
				other.dequeueInt();
				other.enqueue(i);
			}
			assertArrayEquals(other.toIntArray(), shiftPrimArray);
			assertArrayEquals(other.toIntArray(new int[100]), shiftPrimArray);
			assertArrayEquals(other.toArray(), shiftArray);
		}
	}
}
