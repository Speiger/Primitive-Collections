package speiger.src.collections.utils;

public interface Stack<T>
{
	public void push(T e);
	
	public T pop();
	
	public int size();
	
	public void clear();
	
	public default T top() {
		return peek(0);
	}
	
	public T peek(int index);
}
