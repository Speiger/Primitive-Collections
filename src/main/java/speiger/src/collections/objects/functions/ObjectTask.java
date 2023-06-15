package speiger.src.collections.objects.functions;

import java.util.concurrent.RunnableFuture;

/**
 * 
 * A Type Specific Task interface that allows you to keep track of the task that is currently running.<br>
 * It extends Runnable future and supports said functions but also provides quality of life functions like:<br>
 * 
 * - isSuccesfull: which allows to detect if the task was completed properly and not interrupted or crashed.
 * - pause/resume: which allows to pause/resume the task at any moment, making it easier to create thread-safe actions.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectTask<T> extends RunnableFuture<T> {
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
	
}