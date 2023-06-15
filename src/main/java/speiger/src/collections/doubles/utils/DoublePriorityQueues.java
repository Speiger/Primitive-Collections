package speiger.src.collections.doubles.utils;

import java.util.function.DoublePredicate;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.queues.DoublePriorityDequeue;
import speiger.src.collections.doubles.queues.DoublePriorityQueue;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class DoublePriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static DoublePriorityQueue synchronize(DoublePriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static DoublePriorityQueue synchronize(DoublePriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static DoublePriorityDequeue synchronize(DoublePriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static DoublePriorityDequeue synchronize(DoublePriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements DoublePriorityQueue
	{
		DoublePriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(DoublePriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(DoublePriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public DoubleIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(double e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(double[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(DoubleCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public double dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public double peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(double e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(double e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public DoubleComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public double[] toDoubleArray(double[] input) { synchronized(mutex) { return queue.toDoubleArray(input); } }
		@Override
		public DoublePriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(DoubleConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(DoublePredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(DoublePredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(DoublePredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public double findFirst(DoublePredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(DoublePredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements DoublePriorityDequeue
	{
		DoublePriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(DoublePriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(DoublePriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(double e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(double[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(DoubleCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public double dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public DoublePriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}