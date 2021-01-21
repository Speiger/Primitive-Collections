package speiger.src.collections.ints.queues;

import speiger.src.collections.ints.base.BaseIntPriorityQueueTest;

public class IntArrayPriorityQueueTests extends BaseIntPriorityQueueTest
{
	@Override
	protected IntPriorityQueue create(int[] data) {
		return new IntArrayPriorityQueue(data);
	}
}
