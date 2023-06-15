package speiger.src.collections.shorts.utils;


import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.queues.ShortPriorityDequeue;
import speiger.src.collections.shorts.queues.ShortPriorityQueue;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class ShortPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static ShortPriorityQueue synchronize(ShortPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static ShortPriorityQueue synchronize(ShortPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static ShortPriorityDequeue synchronize(ShortPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static ShortPriorityDequeue synchronize(ShortPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements ShortPriorityQueue
	{
		ShortPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(ShortPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(ShortPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public ShortIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(short e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(short[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(ShortCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public short dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public short peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(short e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(short e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public ShortComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public short[] toShortArray(short[] input) { synchronized(mutex) { return queue.toShortArray(input); } }
		@Override
		public ShortPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(ShortConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(ShortPredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(ShortPredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(ShortPredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public short findFirst(ShortPredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(ShortPredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements ShortPriorityDequeue
	{
		ShortPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(ShortPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(ShortPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(short e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(short[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(ShortCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public short dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public ShortPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}