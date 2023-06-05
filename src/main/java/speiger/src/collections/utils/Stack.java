package speiger.src.collections.utils;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;

/**
 * The Stack Interface represents the Last-In-First-Out layout (LIFO).
 * It provides a simple {@link #push(Object)}, {@link #pop()} function,
 * with a fast {@link #clear()} function.
 * The {@link #peek(int)} function allows to view the contents of the stack (top to bottom).
 * @param <T> the type of elements maintained by this Collection
 */
public interface Stack<T>
{
	/**
	 * Inserts a given Object on top of the stack
	 * @param e the Object to insert
	 */
	public void push(T e);
	
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
	 */
	public T pop();
	
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
	 * Clears the stack
	 */
	public void clear();
	
	/**
	 * Provides the Object on top of the stack
	 * @return the element that is on top of the stack
	 * @throws ArrayIndexOutOfBoundsException if the stack is empty
	 */
	public default T top() {
		return peek(0);
	}
	
	/**
	 * Provides the Selected Object from the stack.
	 * Top to bottom
	 * @param index of the element that should be provided
	 * @return the element that was requested
 	 * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
	 */
	public T peek(int index);
	
	/**
	 * A method to drop the contents of the Queue without clearing the queue
	 * @param <E> the keyType of elements maintained by this Collection
	 * @return the contents of the queue into a seperate array.
	 */
	public default <E> E[] toArray() {
		return toArray((E[])new Object[size()]); 
	}
	
	/**
	 * A method to drop the contents of the Stack without clearing the stack
	 * @param array where the elements should be inserted to. If it does not fit then it creates a new appropriately created array
	 * @param <E> the keyType of elements maintained by this Collection
	 * @return the contents of the stack into a separate array.
	 * @note if the Type is generic then a Object Array is created instead of a Type Array
	 */
	public <E> E[] toArray(E[] array);
	
	/**
	 * A Helper function that simplifies the process of creating a new Array.
	 * @param action the array creation function
	 * @param <E> the returning arrayType
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray(Object[])
	 */
	public default <E> E[] toArray(IntFunction<E[]> action) {
		return toArray(action.apply(size()));
	}
}
