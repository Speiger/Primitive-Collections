package speiger.src.collections.chars.utils;

import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.utils.IArray;

/**
 * Type-Specific Helper class to get the underlying array of array implementations.
 * @see speiger.src.collections.chars.lists.CharArrayList
 */
public interface ICharArray extends IArray
{
	/**
	 * Provides the Underlying Array in the Implementation
	 * @return underlying Array
	 * @throws ClassCastException if the return type does not match the underlying array. (Only for Object Implementations)
	 */
	public char[] elements();
	
	/**
	 * Provides the Underlying Array in the Implementation. This function exists purely because of Synchronization wrappers to help run Synchronized Code
	 * @param action the action that handles the array
	 * @throws NullPointerException if action is null
	 */
	public default void elements(Consumer<char[]> action) {
		Objects.requireNonNull(action);
		action.accept(elements());
	}
}