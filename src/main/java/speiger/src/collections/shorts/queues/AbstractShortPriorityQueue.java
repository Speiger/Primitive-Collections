package speiger.src.collections.shorts.queues;

import java.util.StringJoiner;

/**
 * Helper class that implements all the essential methods for the PriorityQueues
 */
public abstract class AbstractShortPriorityQueue implements ShortPriorityQueue
{
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortPriorityQueue) {
			ShortPriorityQueue queue = (ShortPriorityQueue)obj;
			if(queue.size() != size()) return false;
			for(int i = 0,m=size();i<m;i++) {
				if(queue.peek(i) != peek(i)) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
        int result = 1;
        for (int i = 0,m=size();i<m;i++) {
            result = 31 * result + Short.hashCode(peek(i));
        }
		return result;
	}
	
	@Override
	public String toString()
	{
		if(isEmpty()) return "[]";
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (int i = 0,m=size();i<m;i++) {
        	joiner.add(Short.toString(peek(i)));
        }
        return joiner.toString();
	}
}