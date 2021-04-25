package speiger.src.collections.ints.queues;

import speiger.src.collections.ints.base.BaseIntPriorityQueueTest;

@SuppressWarnings("javadoc")
public class IntHeapPriorityQueueTests extends BaseIntPriorityQueueTest
{
	@Override
	protected IntPriorityQueue create(int[] data) {
		return new IntHeapPriorityQueue(data);
	}
}