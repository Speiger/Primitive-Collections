package speiger.src.collections.ints.queues;

import speiger.src.collections.ints.base.BaseIntPriorityQueueTest;

@SuppressWarnings("javadoc")
public class IntArrayFIFOQueueTests extends BaseIntPriorityQueueTest
{
	@Override
	protected IntPriorityQueue create(int[] data){return new IntArrayFIFOQueue(data);}
	
	@Override
	protected boolean isUnsortedRead() { return false; }
}
