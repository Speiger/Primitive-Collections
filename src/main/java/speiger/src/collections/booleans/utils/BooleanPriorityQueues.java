package speiger.src.collections.booleans.utils;


import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.queues.BooleanPriorityDequeue;
import speiger.src.collections.booleans.queues.BooleanPriorityQueue;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class BooleanPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static BooleanPriorityQueue synchronize(BooleanPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static BooleanPriorityQueue synchronize(BooleanPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static BooleanPriorityDequeue synchronize(BooleanPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static BooleanPriorityDequeue synchronize(BooleanPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements BooleanPriorityQueue
	{
		BooleanPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(BooleanPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(BooleanPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public BooleanIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(boolean e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(boolean[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(BooleanCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public boolean dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public boolean peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(boolean e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(boolean e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public BooleanComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public boolean[] toBooleanArray(boolean[] input) { synchronized(mutex) { return queue.toBooleanArray(input); } }
		@Override
		public BooleanPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(BooleanConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(Boolean2BooleanFunction filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Boolean2BooleanFunction filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Boolean2BooleanFunction filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public boolean findFirst(Boolean2BooleanFunction filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(Boolean2BooleanFunction filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements BooleanPriorityDequeue
	{
		BooleanPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(BooleanPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(BooleanPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(boolean e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(boolean[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(BooleanCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public boolean dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public BooleanPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}