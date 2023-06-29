package speiger.src.collections.doubles.utils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleTask;
import speiger.src.collections.doubles.functions.function.DoublePredicate;
import speiger.src.collections.doubles.functions.function.DoubleFunction;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenHashSet;
import speiger.src.collections.booleans.utils.BooleanAsyncBuilder;import speiger.src.collections.booleans.utils.BooleanAsyncBuilder.BaseBooleanTask;
import speiger.src.collections.objects.utils.ObjectAsyncBuilder;
import speiger.src.collections.objects.utils.ObjectAsyncBuilder.BaseObjectTask;
import speiger.src.collections.ints.utils.IntAsyncBuilder;import speiger.src.collections.ints.utils.IntAsyncBuilder.BaseIntTask;
import speiger.src.collections.utils.ISizeProvider;
import speiger.src.collections.utils.SanityChecks;

/**
 * 
 * 
 * The Async API allows you to process collections on a different thread without having to deal with the Multithreading complexity. <br> 
 * It uses the Lightweight Stream Replace API to do its work, which can make it faster then sequential streams. <br>
 * This feature isn't designed to do Multithreading work, but to allow moving work off the main thread into a worker thread. <br>
 * So anything executed on this is still Singlethreaded. <br>
 * <br>
 * How it works is you create the AsyncBuilder using a Iterable of the Type. <br>
 * Then select things you want to use on the Iterable. <br>
 * - For example: map/filter/peek/limit/etc <br>
 * <br>
 * After that you select your action the Iterable should be applied on. <br>
 * - For Example: forEach/findFirst/matchAny/etc <br>
 * <br>
 * Then optionally a custom Executor or callback can be applied <br>
 * At the end either execute or join can be called to start the task. <br>
 * - execute will return the Task reference so that the task can be traced back. <br>
 * - join will await the completion of the task and return the return value <br>
 * <br>
 * During Construction a couple Disposable Builder Objects will be created. <br>
 * These will be only used during construction. <br>
 * <br>
 * The Task Object is also pause-able/Interruptable at any moment during the Processing. <br>
 * 
 * A small example
 * <pre><code>
 * public void processFiles(ObjectCollection&#60;String&#62; potentialFiles) {
 * 	potentialFiles.asAsync()
 * 		.map(Paths::get).filter(Files::exists) //Modifies the collection (Optional)
 * 		.forEach(Files::delete) //Creates the action (Required)
 * 		.onCompletion(T -&#62; {}} //Callback on completion (Optional)
 * 		.execute() //Starts the task. (Required)
 * }
 * </code></pre>
 * @author Speiger
 * 
 */
public class DoubleAsyncBuilder
{
	DoubleIterable iterable;
	BaseDoubleTask task;
	
	/**
	 * Main Constructor that uses a Iterable to build a Offthread Task.
	 * @param iterable that should be processed
	 */
	public DoubleAsyncBuilder(DoubleIterable iterable) {
		this.iterable = iterable;
	}
	
	/**
	 * Helper constructor.
	 * @param task that had been build
	 */
	public DoubleAsyncBuilder(BaseDoubleTask task) {
		this.task = task;
	}
	
	/**
	 * Helper function that automatically wraps a Iterable into a AsyncBuilder since it forces this collections Iterable.
	 * @param iterable that should be wrapped
	 * @return a AsyncBuilder with the iterable wrapped
	 */
	public static DoubleAsyncBuilder of(Iterable<Double> iterable) {
		return new DoubleAsyncBuilder(DoubleIterables.wrap(iterable));
	}
	
	/**
	 * Helper function that automatically wraps a array into a AsyncBuilder since it forces this collections Iterable.
	 * @param values that should be wrapped
	 * @return a AsyncBuilder with the values wrapped
	 */
	public static DoubleAsyncBuilder of(double...values) {
		return new DoubleAsyncBuilder(DoubleArrayList.wrap(values));
	}
	
	/**
	 * Maps the elements to something else
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Builder Object with the mapped Iterable
	 */
	public <E> ObjectAsyncBuilder<E> map(DoubleFunction<E> mapper) {
		return new ObjectAsyncBuilder<>(DoubleIterables.map(iterable, mapper));
	}
	
	/**
	 * Maps the elements to something else
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Builder Object with the mapped Iterable
	 */
	public <E, V extends Iterable<E>> ObjectAsyncBuilder<E> flatMap(DoubleFunction<V> mapper) {
		return new ObjectAsyncBuilder<>(DoubleIterables.flatMap(iterable, mapper));
	}
	
	/**
	 * Maps the elements to something else
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Builder Object with the mapped Iterable
	 */
	public <E> ObjectAsyncBuilder<E> arrayflatMap(DoubleFunction<E[]> mapper) {
		return new ObjectAsyncBuilder<>(DoubleIterables.arrayFlatMap(iterable, mapper));
	}
	
	/**
	 * Filters out the unwanted elements out of the Iterable
	 * @param filter the elements that should be kept
	 * @return Self with a filter applied
	 */
	public DoubleAsyncBuilder filter(DoublePredicate filter) {
		iterable = DoubleIterables.filter(iterable, filter);
		return this;
	}
	
	/**
	 * Removes duplicated elements out of the Iterable
	 * @return Self with a deduplicator applied
	 */
	public DoubleAsyncBuilder distinct() {
		iterable = DoubleIterables.distinct(iterable);
		return this;
	}
	
	/**
	 * Repeats the elements inside of the Iterable
	 * @param repeats the amount of times the elements should be repeated
	 * @return self with a repeater applied
	 */
	public DoubleAsyncBuilder repeat(int repeats) {
		iterable = DoubleIterables.repeat(iterable, repeats);
		return this;
	}
	
	/**
	 * Limits how many elements are inside of the Iterable
	 * @param limit how many elements should max be iterated through
	 * @return self with a limiter applied
	 */
	public DoubleAsyncBuilder limit(long limit) {
		iterable = DoubleIterables.limit(iterable, limit);
		return this;
	}
	
	/**
	 * Sorts the elements inside of the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it, and this will affect the pausing feature.
	 * @param sorter that sorts the elements.
	 * @return self with a sorter applied
	 */
	public DoubleAsyncBuilder sorted(DoubleComparator sorter) {
		iterable = DoubleIterables.sorted(iterable, sorter);
		return this;
	}
	
	/**
	 * Allows to preview elements before they are processed
	 * @param action the action that should be applied
	 * @return self with a preview applied
	 */
	public DoubleAsyncBuilder peek(DoubleConsumer action) {
		iterable = DoubleIterables.peek(iterable, action);
		return this;
	}
	
	/**
	 * Iterates over the Iterable with a desired action
	 * @param action that should be applied
	 * @return a new Builder with the forEach action applied.
	 */
	public ObjectAsyncBuilder<Void> forEach(DoubleConsumer action) {
		return new ObjectAsyncBuilder<>(new ForEachTask<>(iterable.iterator(), action));
	}
	
	/**
	 * Reduces the elements inside of the Iterable down to one element
	 * @param operator that reduces the elements.
	 * @return self with the reduce action applied
	 */
	public DoubleAsyncBuilder reduce(DoubleDoubleUnaryOperator operator) {
		task = new SimpleReduceTask(iterable.iterator(), operator);
		return this;
	}
	
	/**
	 * Reduces the elements inside of the Iterable down to one element using a identity element.
	 * @param identity the element the reduce function should start with
	 * @param operator that reduces the elements.
	 * @return a new Builder with the reduce function applied
	 */
	public DoubleAsyncBuilder reduce(double identity, DoubleDoubleUnaryOperator operator) {
		return new DoubleAsyncBuilder(new ReduceTask(iterable.iterator(), operator, identity));
	}
	

	/**
	 * Pours all elements of the Iterable down into a Array.
	 * @return a new Builder with the ToArray function applied
	 */
	public ObjectAsyncBuilder<double[]> toDoubleArray() {
		return new ObjectAsyncBuilder<>(new ArrayTask(iterable));
	}
	
	/**
	 * Pours all elements into a List that can be later
	 * @return a new Builder with the pour function applied
	 */
	public ObjectAsyncBuilder<DoubleList> pourAsList() {
		return pour(new DoubleArrayList());
	}
	
	/**
	 * Pours all elements into a Set that can be later
	 * @return a new Builder with the pour function applied
	 */
	public ObjectAsyncBuilder<DoubleSet> pourAsSet() {
		return pour(new DoubleLinkedOpenHashSet());
	}
		/**
	 * Pours all elements into a collection that can be later
	 * @param <E> the return type
	 * @param collection the collection the elements
	 * @return a new Builder with the pour function applied
	 */
	public <E extends DoubleCollection> ObjectAsyncBuilder<E> pour(E collection) {
		return new ObjectAsyncBuilder<>(new CollectTask<>(iterable.iterator(), collection));
	}
	
	/**
	 * Searches through the elements of the Iterable to find if the desired element is present.
	 * @param filter that decides the desired elements
	 * @return a new Builder with the matchAny function applied
	 */
	public BooleanAsyncBuilder matchAny(DoublePredicate filter) {
		return new BooleanAsyncBuilder(new MatchTask(iterable.iterator(), filter, 0));
	}
	
	/**
	 * Searches through the elements of the Iterable to find if unwanted elements are present.
	 * @param filter that decides the unwanted elements
	 * @return a new Builder with the matchNone function applied
	 */
	public BooleanAsyncBuilder matchNone(DoublePredicate filter) {
		return new BooleanAsyncBuilder(new MatchTask(iterable.iterator(), filter, 1));
	}
	
	/**
	 * Searches through the elements of the Iterable to find if all the desired elements are present.
	 * @param filter that decides the desired elements
	 * @return a new Builder with the matchAll function applied
	 */
	public BooleanAsyncBuilder matchAll(DoublePredicate filter) {
		return new BooleanAsyncBuilder(new MatchTask(iterable.iterator(), filter, 2));
	}
	
	/**
	 * Searches through the elements of the Iterable to find if the desired element.
	 * If not present it will return the default value of the type
	 * @param filter that decides the desired elements
	 * @return self with the findFirst function applied
	 */
	public DoubleAsyncBuilder findFirst(DoublePredicate filter) {
		task = new FindFirstTask(iterable.iterator(), filter);
		return this;
	}
	
	/**
	 * Counts all desired elements inside the Iterable
	 * @param filter that decides the desired elements
	 * @return a new Builder with the count function applied
	 */
	public IntAsyncBuilder count(DoublePredicate filter) {
		return new IntAsyncBuilder(new CountTask(iterable.iterator(), filter));
	}
	
	/**
	 * Optional way to add a custom executor that runs this offthread task.
	 * Can only be set after the action was decided on.
	 * @param executor that executes the task, defaults to {@link SanityChecks#invokeAsyncTask(Runnable) }
	 * @return self with the executor set
	 * @note has to be NonNull
	 */
	public DoubleAsyncBuilder executor(Executor executor) {
		if(task == null) throw new IllegalStateException("Action is missing");
		task.withExecutor(executor);
		return this;
	}
	
	/**
	 * Optional way to set a callback that allows to compute actions after the task was completed.
	 * The state of the task has to be validated by the callback.
	 * @param callback that should be notified after completion of the task
	 * @return self with the callback set
	 * @note can be null
	 */
	public DoubleAsyncBuilder onCompletion(Consumer<DoubleTask> callback) {
		if(task == null) throw new IllegalStateException("Action is missing");
		task.withCallback(callback);
		return this;
	}
	
	/**
	 * Starts the Execution of the task without awaiting its result
	 * @return the task object that allow to trace it.
	 */
	public DoubleTask execute() {
		BaseDoubleTask toRun = task;
		toRun.begin();
		task = null;
		return toRun;
	}
	
	/**
	 * Starts the Execution of the task and will await its completion, returning the result.
	 * @return the result of the task provided.
	 * @throws ExecutionException if the task threw a exception
	 * @throws InterruptedException if the caller thread was interrupted
	 */
	public double join() throws ExecutionException, InterruptedException {
		BaseDoubleTask toRun = task;
		task = null;
		toRun.begin();
		return toRun.getDouble();
	}
	
	/**
	 * Starts the Execution of the task and will await its completion with a timeout, returning the result.
	 * @param timeout of how long the thread should wait the task to be completed
	 * @param unit of the desired waiting time.
	 * @return the result of the provided task
	 * @throws InterruptedException if the caller thread was interrupted
	 * @throws ExecutionException if the task threw a exception
	 * @throws TimeoutException if the timeout was reached before the task was finished
	 */
	public double join(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		BaseDoubleTask toRun = task;
		task = null;
		toRun.begin();
		return toRun.getDouble(timeout, unit);
	}
	
	private static class ReduceTask extends BaseDoubleTask
	{
		DoubleIterator iter;
		DoubleDoubleUnaryOperator operator;
		double value;
		
		public ReduceTask(DoubleIterator iter, DoubleDoubleUnaryOperator operator, double value) {
			this.iter = iter;
			this.operator = operator;
			this.value = value;
		}
		
		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				value = operator.apply(value, iter.nextDouble());
			}
			if(!iter.hasNext()) {
				setResult(value);
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			operator = null;
		}
	}
	
	private static class SimpleReduceTask extends BaseDoubleTask
	{
		DoubleIterator iter;
		DoubleDoubleUnaryOperator operator;
		boolean first = true;
		double value;
		
		public SimpleReduceTask(DoubleIterator iter, DoubleDoubleUnaryOperator operator) {
			this.iter = iter;
			this.operator = operator;
		}
		
		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				if(first) {
					first = false;
					value = iter.nextDouble();
				}
				else {
					value = operator.applyAsDouble(value, iter.nextDouble());
				}
			}
			if(!iter.hasNext()) {
				setResult(value);
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			operator = null;
		}
	}
	
	private static class MatchTask extends BaseBooleanTask
	{
		DoubleIterator iter;
		DoublePredicate filter;
		int type;
		
		public MatchTask(DoubleIterator iter, DoublePredicate filter, int type) {
			this.iter = iter;
			this.filter = filter;
			this.type = type;
			if(type < 0 || type > 2) throw new IllegalArgumentException("Type is not allowed has to be between 0-2");
		}
		
		@Override
		protected boolean execute() throws Exception {
			switch(type) {
				case 0:
					while(shouldRun() && iter.hasNext()) {
						if(filter.test(iter.nextDouble())) {
							setResult(true);
							return true;
						}
					}
					break;
				case 1:
					while(shouldRun() && iter.hasNext()) {
						if(filter.test(iter.nextDouble())) {
							setResult(false);
							return true;
						}
					}
					break;
				case 2:
					while(shouldRun() && iter.hasNext()) {
						if(!filter.test(iter.nextDouble())) {
							setResult(false);
							return true;
						}
					}
					break;
			}
			if(!iter.hasNext()) {
				setResult(type >= 1);
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			filter = null;
		}
	}
	
	private static class FindFirstTask extends BaseDoubleTask
	{
		DoubleIterator iter;
		DoublePredicate filter;
		
		public FindFirstTask(DoubleIterator iter, DoublePredicate filter) {
			this.iter = iter;
			this.filter = filter;
		}

		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				double entry = iter.nextDouble();
				if(filter.test(iter.nextDouble())) {
					setResult(entry);
					return true;
				}
			}
			return !iter.hasNext();
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			filter = null;
		}
	}
	
	private static class CountTask extends BaseIntTask
	{
		DoubleIterator iter;
		DoublePredicate filter;
		int counted = 0;
		
		public CountTask(DoubleIterator iter, DoublePredicate filter) {
			this.iter = iter;
			this.filter = filter;
		}
		
		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				if(filter.test(iter.nextDouble())) {
					counted++;
				}
			}
			if(!iter.hasNext())
			{
				setResult(counted);
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			filter = null;
		}
	}
	
	private static class CollectTask<T extends DoubleCollection> extends BaseObjectTask<T>
	{
		DoubleIterator iter;
		T collection;
		
		public CollectTask(DoubleIterator iter, T collection) {
			this.iter = iter;
			this.collection = collection;
		}
		
		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				collection.add(iter.nextDouble());
			}
			if(!iter.hasNext()) {
				setResult(collection);
				collection = null;
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
		}
	}
	
	private static class ArrayTask extends BaseObjectTask<double[]>
	{
		DoubleIterator iter;
		DoubleCollections.CollectionWrapper wrapper;
	
		public ArrayTask(DoubleIterable iterable) {
			iter = iterable.iterator();
			ISizeProvider prov = ISizeProvider.of(iterable);
			int size = prov == null ? -1 : prov.size();
			wrapper = size < 0 ? DoubleCollections.wrapper() : DoubleCollections.wrapper(size);
		}
		
		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				wrapper.add(iter.nextDouble());
			}
			if(!iter.hasNext()) {
				setResult(wrapper.toDoubleArray());
				wrapper = null;
				return true;
			}
			return false;
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
		}
	}
	
	private static class ForEachTask<T> extends BaseObjectTask<Void>
	{
		DoubleIterator iter;
		DoubleConsumer listener;
		
		public ForEachTask(DoubleIterator iter, DoubleConsumer listener) {
			this.iter = iter;
			this.listener = listener;
		}

		@Override
		protected boolean execute() throws Exception {
			while(shouldRun() && iter.hasNext()) {
				listener.accept(iter.nextDouble());
			}
			return !iter.hasNext();
		}
		
		@Override
		protected void onCompletion() {
			super.onCompletion();
			iter = null;
			listener = null;
		}
	}
	
	/**
	 * Base Task of the Actions that can be performed.
	 * Allows to simplify the actions that get executed.
	 */
	public abstract static class BaseDoubleTask implements DoubleTask
	{
		private static final int CREATED = 0;
		private static final int RUNNING = 1;
		private static final int PAUSING = 2;
		private static final int PAUSED = 3;
		private static final int FINISHING = 4;
		private static final int FINISHED = 5;
		private static final int EXCEPTIONALLY = 6;
		private static final int CANCELLED = 7;
		private volatile WaitNode waiter;
		private volatile int state = CREATED;
		Consumer<DoubleTask> callback;
		Executor executor = SanityChecks::invokeAsyncTask;
		double result;
		Throwable excpetion;
		
		void withCallback(Consumer<DoubleTask> callback) {
			this.callback = callback;
		}
		
		void withExecutor(Executor executor) {
			this.executor = executor;
		}
		
		void begin() {
			executor.execute(this);
		}
		
		protected abstract boolean execute() throws Exception;
		
		protected void onCompletion() {
			if(callback != null) {
				callback.accept(this);
				callback = null;
			}
			executor = null;
		}
		
		protected void setResult(double result) {
			this.result = result;
		}
		
		@Override
		public void run() {
			state = RUNNING;
			try {
				if(execute()) {
					state = FINISHING;
					finishCompletion(FINISHED);
				}
				else if(state == PAUSING) {
					state = PAUSED;
				}
			}
			catch(Exception e) {
				state = EXCEPTIONALLY;
				this.excpetion = e;
				finishCompletion(EXCEPTIONALLY);
			}
		}
		
	    private void finishCompletion(int nextState) {
	    	WaitNode current = waiter;
	    	waiter = null;
	    	while(current != null) {
                Thread t = current.thread;
                if (t != null) {
                    current.thread = null;
                    LockSupport.unpark(t);
                }
                WaitNode next = current.next;
                if (next == null)
                    break;
                current.next = null;
                current = next;
	    	}
	    	state = nextState;
	    	onCompletion();
	    }
		
		@Override
		public boolean cancel(boolean cancelIfRunnning) {
			if(state == RUNNING && !cancelIfRunnning) return false;
			state = CANCELLED;
			finishCompletion(CANCELLED);
			return true;
		}
		
		@Override
		public double getDouble() throws InterruptedException, ExecutionException {
	        int s = state;
	        return report(s <= FINISHING ? awaitDone(false, 0L) : s);
		}
		
		@Override
		public double getDouble(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			if (unit == null) throw new NullPointerException();
			int s = state;
			if (s <= FINISHING && (s = awaitDone(true, unit.toNanos(timeout))) <= FINISHING) throw new TimeoutException();
	        return report(s);
		}
		
		private double report(int s) throws ExecutionException {
			if (s == FINISHED) return result;
			if (s >= CANCELLED) throw new CancellationException();
			throw new ExecutionException(excpetion);
	    }
		
		private int awaitDone(boolean timed, long nanos) throws InterruptedException {
			final long deadline = timed ? System.nanoTime() + nanos : 0L;
			WaitNode q = null;
			boolean queued = false;
			while(true) {
				if(Thread.interrupted()) {
					removeWaiter(q);
					throw new InterruptedException();
				}
				int s = state;
				if(s > FINISHING) {
					if(q != null) q.thread = null;
					return s;
				}
				else if(s == FINISHING) Thread.yield();
				else if(q == null) q = new WaitNode();
				else if(!queued) {
					q.next = waiter;
					waiter = q;
					queued = true;
				}
				else if(timed) {
					nanos = deadline - System.nanoTime();
					if(nanos <= 0L) {
						removeWaiter(q);
						return state;
					}
					LockSupport.parkNanos(this, nanos);
				}
				else LockSupport.park(this);
			}
		}
		
		private void removeWaiter(WaitNode node) {
			if(node == null) return;
			node.thread = null;
			retry:
			while(true) {
				for(WaitNode prev = null, current = waiter, next = null; current != null; current = next) {
					next = current.next;
					if(current.thread != null) prev = current;
					else if(prev != null) {
						prev.next = next;
						if(prev.thread == null) continue retry; //Previous element got removed which means another thread was editing this while we were editing.
					}
					else if(waiter == current) {
						waiter = next;
						continue retry;
					}
				}
				break;
			}
		}
		
		@Override
		public boolean isCancelled() { return state >= CANCELLED; }
		@Override
		public boolean isDone() { return state >= FINISHING; }
		@Override
		public boolean isPaused() { return state == PAUSED; }
		@Override
		public boolean isSuccessful() { return state == FINISHED; }
		protected boolean shouldRun() { return state == RUNNING; }
		
		@Override
		public void pause() {
			if(state == PAUSED || state == PAUSING || state >= FINISHING) return;
			state = PAUSING;
		}
		
		@Override
		public void awaitPausing() {
			if(state == PAUSED) return;
			pause();
			if(state == PAUSING) {
				while(state == PAUSING) {
					Thread.yield();
				}
			}
		}
		
		@Override
		public void resume() {
			if(state != PAUSED && state != PAUSING) return;
			if(state == PAUSING) {
				while(state == PAUSING) {
					Thread.yield();
				}
			}
			state = RUNNING;
			executor.execute(this);
		}
		
		static final class WaitNode {
			volatile Thread thread;
			volatile WaitNode next;
			
			WaitNode() {
				thread = Thread.currentThread();
			}
		}
	}
}