package speiger.src.collections.chars.utils;


import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.queues.CharPriorityDequeue;
import speiger.src.collections.chars.queues.CharPriorityQueue;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class CharPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static CharPriorityQueue synchronize(CharPriorityQueue queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static CharPriorityQueue synchronize(CharPriorityQueue queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue)queue : new SynchronizedPriorityQueue(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static CharPriorityDequeue synchronize(CharPriorityDequeue dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static CharPriorityDequeue synchronize(CharPriorityDequeue dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue)dequeue : new SynchronizedPriorityDequeue(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityQueue implements CharPriorityQueue
	{
		CharPriorityQueue queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(CharPriorityQueue queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(CharPriorityQueue queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public CharIterator iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(char e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(char[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(CharCollection c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public char dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public char peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(char e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(char e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public CharComparator comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public char[] toCharArray(char[] input) { synchronized(mutex) { return queue.toCharArray(input); } }
		@Override
		public CharPriorityQueue copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(CharConsumer action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(CharPredicate filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(CharPredicate filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(CharPredicate filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public char findFirst(CharPredicate filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(CharPredicate filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 */
	public static class SynchronizedPriorityDequeue extends SynchronizedPriorityQueue implements CharPriorityDequeue
	{
		CharPriorityDequeue dequeue;
		protected SynchronizedPriorityDequeue(CharPriorityDequeue queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(CharPriorityDequeue queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(char e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(char[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(CharCollection c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public char dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public CharPriorityDequeue copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}