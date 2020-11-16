package speiger.src.collections.utils;

import java.util.Iterator;

public interface BidirectionalIterator<E> extends Iterator<E>
{
	public E previous();
	
	public boolean hasPrevious();
	
	public default int back(int amount) {
		if(amount < 0) throw new IllegalStateException("Can't go forward");
		int i = 0;
		for(;i<amount && hasPrevious();previous(),i++);
		return i;
	}
}
