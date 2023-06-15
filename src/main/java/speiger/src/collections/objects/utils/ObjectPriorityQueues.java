package speiger.src.collections.objects.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.queues.ObjectPriorityDequeue;
import speiger.src.collections.objects.queues.ObjectPriorityQueue;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

/**
 * A Helper class for PriorityQueues
 */
public class ObjectPriorityQueues
{
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static <T> ObjectPriorityQueue<T> synchronize(ObjectPriorityQueue<T> queue) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue<T>)queue : new SynchronizedPriorityQueue<>(queue);
	}
	
	/**
	 * Returns a synchronized PriorityQueue instance based on the instance given.
	 * @param queue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized PriorityQueue wrapper.
	 */
	public static <T> ObjectPriorityQueue<T> synchronize(ObjectPriorityQueue<T> queue, Object mutex) {
		return queue instanceof SynchronizedPriorityQueue ? (SynchronizedPriorityQueue<T>)queue : new SynchronizedPriorityQueue<>(queue, mutex);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static <T> ObjectPriorityDequeue<T> synchronize(ObjectPriorityDequeue<T> dequeue) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue<T>)dequeue : new SynchronizedPriorityDequeue<>(dequeue);
	}
	
	/**
	 * Returns a synchronized PriorityDequeue instance based on the instance given.
	 * @param dequeue that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized PriorityDequeue wrapper.
	 */
	public static <T> ObjectPriorityDequeue<T> synchronize(ObjectPriorityDequeue<T> dequeue, Object mutex) {
		return dequeue instanceof SynchronizedPriorityDequeue ? (SynchronizedPriorityDequeue<T>)dequeue : new SynchronizedPriorityDequeue<>(dequeue, mutex);
	}
	
	/**
	 * Wrapper class for synchronization
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedPriorityQueue<T> implements ObjectPriorityQueue<T>
	{
		ObjectPriorityQueue<T> queue;
		protected Object mutex;
		
		protected SynchronizedPriorityQueue(ObjectPriorityQueue<T> queue) {
				this.queue = queue;
				mutex = this;
		}
		
		protected SynchronizedPriorityQueue(ObjectPriorityQueue<T> queue, Object mutex) {
			this.queue = queue;
			this.mutex = mutex;
		}
		
		@Override
		public ObjectIterator<T> iterator() { return queue.iterator(); }
		@Override
		public int size() { synchronized(mutex) { return queue.size(); } }
		@Override
		public void clear() { synchronized(mutex) { queue.clear(); } }
		@Override
		public void enqueue(T e) { synchronized(mutex) { queue.enqueue(e); } }
		@Override
		public void enqueueAll(T[] e, int offset, int length) { synchronized(mutex) { queue.enqueueAll(e, offset, length); } }
		@Override
		public void enqueueAll(ObjectCollection<T> c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public void enqueueAll(Collection<? extends T> c) { synchronized(mutex) { queue.enqueueAll(c); } }
		@Override
		public T dequeue() { synchronized(mutex) { return queue.dequeue(); } }
		@Override
		public T peek(int index) { synchronized(mutex) { return queue.peek(index); } }
		@Override
		public boolean removeFirst(T e) { synchronized(mutex) { return queue.removeFirst(e); } }
		@Override
		public boolean removeLast(T e) { synchronized(mutex) { return queue.removeLast(e); } }
		@Override
		public void onChanged() { synchronized(mutex) { queue.onChanged(); } }
		@Override
		public Comparator<? super T> comparator() { synchronized(mutex) { return queue.comparator(); } }
		@Override
		public <E> E[] toArray(E[] input) { synchronized(mutex) { return queue.toArray(input); } }
		@Override
		public ObjectPriorityQueue<T> copy() { synchronized(mutex) { return queue.copy(); } }
		@Override
		public void forEach(Consumer<? super T> action) { synchronized(mutex) { queue.forEach(action); } }
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) { synchronized(mutex) { queue.forEach(input, action); } }
		@Override
		public boolean matchesAny(Predicate<T> filter) { synchronized(mutex) { return queue.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Predicate<T> filter) { synchronized(mutex) { return queue.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Predicate<T> filter) { synchronized(mutex) { return queue.matchesAll(filter); } }
		@Override
		public T findFirst(Predicate<T> filter) { synchronized(mutex) { return queue.findFirst(filter); } }
		@Override
		public int count(Predicate<T> filter) { synchronized(mutex) { return queue.count(filter); } }
	}
	
	/**
	 * Wrapper class for synchronization
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class SynchronizedPriorityDequeue<T> extends SynchronizedPriorityQueue<T> implements ObjectPriorityDequeue<T>
	{
		ObjectPriorityDequeue<T> dequeue;
		protected SynchronizedPriorityDequeue(ObjectPriorityDequeue<T> queue) {
			super(queue);
			dequeue = queue;
		}
	
		protected SynchronizedPriorityDequeue(ObjectPriorityDequeue<T> queue, Object mutex) {
			super(queue, mutex);
			dequeue = queue;
		}
		
		@Override
		public void enqueueFirst(T e) { synchronized(mutex) { dequeue.enqueueFirst(e); } }
		@Override
		public void enqueueAllFirst(T[] e, int offset, int length) { synchronized(mutex) { dequeue.enqueueAllFirst(e, offset, length); } }
		@Override
		public void enqueueAllFirst(ObjectCollection<T> c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public void enqueueAllFirst(Collection<? extends T> c) { synchronized(mutex) { dequeue.enqueueAllFirst(c); } }
		@Override
		public T dequeueLast() { synchronized(mutex) { return dequeue.dequeueLast(); } }
		@Override
		public ObjectPriorityDequeue<T> copy() { synchronized(mutex) { return dequeue.copy(); } }
	}
}