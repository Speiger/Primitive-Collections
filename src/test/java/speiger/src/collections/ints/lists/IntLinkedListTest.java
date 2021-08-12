package speiger.src.collections.ints.lists;

import org.junit.Test;

import speiger.src.collections.ints.base.BaseIntListTest;
import speiger.src.collections.ints.base.BaseIntPriorityQueueTest;
import speiger.src.collections.ints.base.IIntStackTests;
import speiger.src.collections.ints.queues.IntPriorityQueue;

/**
 * @author Speiger
 *
 */
public class IntLinkedListTest extends BaseIntListTest implements IIntStackTests
{
	@Override
	public IntLinkedList create(int[] data) {
		return new IntLinkedList(data);
	}
	
	@Test
	@Override
	public void testPush() {
		IIntStackTests.super.testPush();
	}
	
	@Test
	@Override
	public void testPop() {
		IIntStackTests.super.testPop();
	}
	
	/**
	 * @author Speiger
	 */
	public static class IntLinkedListQueueTest extends BaseIntPriorityQueueTest {

		@Override
		protected IntPriorityQueue create(int[] data) {
			return new IntLinkedList(data);
		}
		
	}
}
