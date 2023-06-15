package speiger.src.collections.ints.utils;

import java.util.function.IntPredicate;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.queues.IntPriorityDequeue;
import speiger.src.collections.ints.queues.IntPriorityQueue;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class IntPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static IntPriorityQueue synchronize(IntPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static IntPriorityQueue synchronize(IntPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static IntPriorityDequeue synchronize(IntPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static IntPriorityDequeue synchronize(IntPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements IntPriorityQueue
	{
		IntPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(IntPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(IntPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public IntIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(int e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(int[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(IntCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public int dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public int peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(int e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(int e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public IntComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public int[] toIntArray(int[] input) { synchronized(mutex) { return queue.toIntArray(input); } }
		@Override
		public IntPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(IntConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(IntPredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(IntPredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(IntPredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public int findFirst(IntPredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(IntPredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements IntPriorityDequeue
	{
		IntPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(IntPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(IntPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(int e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(int[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(IntCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public int dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public IntPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}