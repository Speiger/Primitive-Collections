package speiger.src.collections.floats.sets;

import java.util.Set;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractFloatSet extends AbstractFloatCollection implements FloatSet
{
	@Override
	public abstract FloatIterator iterator();
	@Override
	public AbstractFloatSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		FloatIterator i = iterator();
		while(i.hasNext())
			hashCode += Float.hashCode(i.nextFloat());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Set))
			return false;
		Set<?> l = (Set<?>)o;
		if(l.size() != size()) return false;
		try {
			return containsAll(l);
		} catch (ClassCastException | NullPointerException unused) {
			return false;
		}
	}
}