package speiger.src.collections.floats.utils;


import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.queues.FloatPriorityDequeue;
import speiger.src.collections.floats.queues.FloatPriorityQueue;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class FloatPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static FloatPriorityQueue synchronize(FloatPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static FloatPriorityQueue synchronize(FloatPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static FloatPriorityDequeue synchronize(FloatPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static FloatPriorityDequeue synchronize(FloatPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements FloatPriorityQueue
	{
		FloatPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(FloatPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(FloatPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public FloatIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(float e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(float[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(FloatCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public float dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public float peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(float e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(float e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public FloatComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public float[] toFloatArray(float[] input) { synchronized(mutex) { return queue.toFloatArray(input); } }
		@Override
		public FloatPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(FloatConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(FloatPredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(FloatPredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(FloatPredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public float findFirst(FloatPredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(FloatPredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements FloatPriorityDequeue
	{
		FloatPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(FloatPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(FloatPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(float e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(float[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(FloatCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public float dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public FloatPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}