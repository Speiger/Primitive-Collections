package speiger.src.collections.bytes.functions;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * A Type Specific Task interface that allows you to keep track of the task that is currently running.<br>
 * It extends Runnable future and supports said functions but also provides quality of life functions like:<br>
 * 
 * - isSuccesfull: which allows to detect if the task was completed properly and not interrupted or crashed.
 * - pause/resume: which allows to pause/resume the task at any moment, making it easier to create thread-safe actions.
 */
public interface ByteTask extends RunnableFuture<Byte> {
	/**
	 * Helper function to detect if the task is currently paused.
	 * @return true if paused
	 */
	public boolean isPaused();
	
	/**
	 * Pauses the task, which lets the thread finish without completing the task.
	 * Tasks are written in the way where they can pause without any issues.
	 * This won't be instant, as this function is applied asynchronous and doesn't check if the thread paused.
	 * So make sure it had the time to pause.
	 */
	public void pause();
	
	/**
	 * Pauses the task, which lets the thread finish without completing the task.
	 * Tasks are written in the way where they can pause without any issues.
	 * This won't be instant, as this function is applied asynchronous.
	 * It will await the pausing of the task.
	 */
	public void awaitPausing();
	
	/**
	 * Continues the task if it wasn't already completed.
	 * This is done by resubmitting the task to the executor provided.
	 */
	public void resume();
	
	/**
	 * Quality of life function that allows to detect if no cancellation/exception was applied to this task and it completed on its own.
	 * @return true if it was properly completed
	 */
	public boolean isSuccessful();
	
	/**
	 * A Type Specific get method that allows to reduce (un)boxing of primtives.
	 * 
	 * Waits if necessary for the computation to complete, and then
	 * retrieves its result.
	 *
	 * @return the computed result as primitive
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if the computation threw an exception
	 * @throws InterruptedException if the current thread was interrupted
	 * while waiting
	 */
	public byte getByte() throws InterruptedException, ExecutionException;

	/**
	 * Waits if necessary for at most the given time for the computation
	 * to complete, and then retrieves its result, if available.
	 *
	 * @param timeout the maximum time to wait
	 * @param unit the time unit of the timeout argument
	 * @return the computed result as primitive
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if the computation threw an exception
	 * @throws InterruptedException if the current thread was interrupted while waiting
	 * @throws TimeoutException if the wait timed out
	 */
	public byte getByte(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
	
	@Override
	@Deprecated
	public default Byte get() throws InterruptedException, ExecutionException { return Byte.valueOf(getByte()); }
	
	@Override
	@Deprecated
	public default Byte get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return Byte.valueOf(getByte(timeout, unit)); }
}