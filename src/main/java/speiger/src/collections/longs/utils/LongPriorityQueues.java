package speiger.src.collections.longs.utils;

import java.util.function.LongPredicate;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.queues.LongPriorityDequeue;
import speiger.src.collections.longs.queues.LongPriorityQueue;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class LongPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static LongPriorityQueue synchronize(LongPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static LongPriorityQueue synchronize(LongPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static LongPriorityDequeue synchronize(LongPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static LongPriorityDequeue synchronize(LongPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements LongPriorityQueue
	{
		LongPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(LongPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(LongPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public LongIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(long e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(long[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(LongCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public long dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public long peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean contains(long e) { synchronized(mutex) { return queue.contains(e); } }
		@Override
		public boolean removeFirst(long e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(long e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public LongComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public long[] toLongArray(long[] input) { synchronized(mutex) { return queue.toLongArray(input); } }
		@Override
		public LongPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(LongConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(LongPredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(LongPredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(LongPredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public long findFirst(LongPredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(LongPredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements LongPriorityDequeue
	{
		LongPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(LongPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(LongPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(long e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(long[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(LongCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public long dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public LongPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}