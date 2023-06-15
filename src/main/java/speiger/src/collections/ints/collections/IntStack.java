package speiger.src.collections.ints.collections;

import java.util.NoSuchElementException;

import speiger.src.collections.utils.Stack;

/**
 * A Type-Specific {@link Stack} that reduces (un)boxing
 */
public interface IntStack
{
	/**
	 * Inserts a given Object on top of the stack
	 * @param e the Object to insert
	 * @see Stack#push(Object)
	 */
	public void push(int e);
	
	/**
	 * Helper function that pushes the top element on top of the stack again.
	 * @throws NoSuchElementException if the stack is empty
	 */
	public default void pushTop() {
		push(top());
	}
	
	/**
	 * Removes the Object on top of the stack.
	 * @return the element that is on top of the stack
	 * @throws ArrayIndexOutOfBoundsException if the stack is empty
	 * @see Stack#pop()
	 */
	public int pop();
	
	/**
	 * Provides the Object on top of the stack
	 * @return the element that is on top of the stack
	 * @throws ArrayIndexOutOfBoundsException if the stack is empty
 	 * @see Stack#top()
	 */
	public default int top() {
		return peek(0);
	}
	
	/**
	 * Provides the Selected Object from the stack.
	 * Top to bottom
	 * @param index of the element that should be provided
	 * @return the element that was requested
 	 * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
 	 * @see Stack#peek(int)
	 */
	public int peek(int index);
	
	/**
	 * Clears the stack
	 */
	public void clear();
	
	/**
	 * Provides the amount of elements currently in the stack
	 * @return amount of elements in the list
	 */
	public int size();
	
	/**
	 * @return if the stack is empty
	 */
	public default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * A method to drop the contents of the Stack without clearing the stack
	 * @return the contents of the stack into a seperate array.
	 */
	public default int[] toIntArray() { return toIntArray(new int[size()]); }
	/**
	 * A method to drop the contents of the Stack without clearing the stack
	 * @param input where the elements should be inserted to. If it does not fit then it creates a new appropiatly created array
	 * @return the contents of the stack into a seperate array.
	 * @note if the Type is generic then a Object Array is created instead of a Type Array
	 */
	public int[] toIntArray(int[] input);
}