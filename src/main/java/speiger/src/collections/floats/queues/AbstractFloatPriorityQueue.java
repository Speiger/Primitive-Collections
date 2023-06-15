package speiger.src.collections.floats.queues;

import java.util.StringJoiner;

/**
 * Helper class that implements all the essential methods for the PriorityQueues
 */
public abstract class AbstractFloatPriorityQueue implements FloatPriorityQueue
{
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatPriorityQueue) {
			FloatPriorityQueue queue = (FloatPriorityQueue)obj;
			if(queue.size() != size()) return false;
			for(int i = 0,m=size();i<m;i++) {
				if(Float.floatToIntBits(queue.peek(i)) != Float.floatToIntBits(peek(i))) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
        int result = 1;
        for (int i = 0,m=size();i<m;i++) {
            result = 31 * result + Float.hashCode(peek(i));
        }
		return result;
	}
	
	@Override
	public String toString()
	{
		if(isEmpty()) return "[]";
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (int i = 0,m=size();i<m;i++) {
        	joiner.add(Float.toString(peek(i)));
        }
        return joiner.toString();
	}
}