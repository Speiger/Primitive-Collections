package speiger.src.collections.doubles.queues;

import java.util.StringJoiner;

/**
 * Helper class that implements all the essential methods for the PriorityQueues
 */
public abstract class AbstractDoublePriorityQueue implements DoublePriorityQueue
{
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoublePriorityQueue) {
			DoublePriorityQueue queue = (DoublePriorityQueue)obj;
			if(queue.size() != size()) return false;
			for(int i = 0,m=size();i<m;i++) {
				if(Double.doubleToLongBits(queue.peek(i)) != Double.doubleToLongBits(peek(i))) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
        int result = 1;
        for (int i = 0,m=size();i<m;i++) {
            result = 31 * result + Double.hashCode(peek(i));
        }
		return result;
	}
	
	@Override
	public String toString()
	{
		if(isEmpty()) return "[]";
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (int i = 0,m=size();i<m;i++) {
        	joiner.add(Double.toString(peek(i)));
        }
        return joiner.toString();
	}
}