package speiger.src.collections.bytes.utils;


import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.queues.BytePriorityDequeue;
import speiger.src.collections.bytes.queues.BytePriorityQueue;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class BytePriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static BytePriorityQueue synchronize(BytePriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static BytePriorityQueue synchronize(BytePriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static BytePriorityDequeue synchronize(BytePriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static BytePriorityDequeue synchronize(BytePriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements BytePriorityQueue
	{
		BytePriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(BytePriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(BytePriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public ByteIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(byte e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(byte[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(ByteCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public byte dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public byte peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean contains(byte e) { synchronized(mutex) { return queue.contains(e); } }
		@Override
		public boolean removeFirst(byte e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(byte e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public byte[] toByteArray(byte[] input) { synchronized(mutex) { return queue.toByteArray(input); } }
		@Override
		public BytePriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(ByteConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(BytePredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(BytePredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(BytePredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public byte findFirst(BytePredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(BytePredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements BytePriorityDequeue
	{
		BytePriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(BytePriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(BytePriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(byte e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(byte[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(ByteCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public byte dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public BytePriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}