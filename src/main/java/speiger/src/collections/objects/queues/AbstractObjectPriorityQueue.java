package speiger.src.collections.objects.queues;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Helper class that implements all the essential methods for the PriorityQueues
 * @param <T> the type of elements maintained by this Collection
 */
public abstract class AbstractObjectPriorityQueue<T> implements ObjectPriorityQueue<T>
{
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectPriorityQueue) {
			ObjectPriorityQueue<T> queue = (ObjectPriorityQueue<T>)obj;
			if(queue.size() != size()) return false;
			for(int i = 0,m=size();i<m;i++) {
				if(!Objects.equals(queue.peek(i), peek(i))) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
        int result = 1;
        for (int i = 0,m=size();i<m;i++) {
            result = 31 * result + Objects.hashCode(peek(i));
        }
		return result;
	}
	
	@Override
	public String toString()
	{
		if(isEmpty()) return "[]";
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (int i = 0,m=size();i<m;i++) {
        	joiner.add(Objects.toString(peek(i)));
        }
        return joiner.toString();
	}
}