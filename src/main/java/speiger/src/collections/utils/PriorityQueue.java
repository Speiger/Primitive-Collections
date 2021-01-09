package speiger.src.collections.utils;

import java.util.Comparator;

public interface PriorityQueue<T> extends Iterable<T>
{
	public void enqueue(T e);
	public T dequeue();
	
	public int size();
	public default boolean isEmpty() { return size() <= 0; }
	public void clear();
	
	public T first();
	public T last();
	public T peek(int index);
	
	public boolean remove(T e);
	public boolean removeLast(T e);
	public void onChanged();
	public Comparator<? super T> comparator();
	
	public default T[] toArray() { return toArray((T[])new Object[size()]); }
	public T[] toArray(T[] input);
}