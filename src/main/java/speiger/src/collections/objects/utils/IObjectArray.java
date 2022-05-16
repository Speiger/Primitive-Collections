package speiger.src.collections.objects.utils;

import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.utils.IArray;

/**
 * Type-Specific Helper class to get the underlying array of array implementations.
 * @see speiger.src.collections.objects.lists.ObjectArrayList
 * @param <T> the type of elements maintained by this Collection
 */
public interface IObjectArray<T> extends IArray
{
	/**
	 * Provides the Underlying Array in the Implementation
	 * @return underlying Array
	 * @throws ClassCastException if the return type does not match the underlying array. (Only for Object Implementations)
	 */
	public T[] elements();
	
	/**
	 * Method to indicate if the function is save to use
	 * @return true if the object is castable to the type that is requested
	 */
	public boolean isCastable();
	/**
	 * Provides the Underlying Array in the Implementation. This function exists purely because of Synchronization wrappers to help run Synchronized Code
	 * @param action the action that handles the array
	 * @throws NullPointerException if action is null
	 */
	public default void elements(Consumer<T[]> action) {
		if(!isCastable()) return;
		Objects.requireNonNull(action);
		action.accept(elements());
	}
}