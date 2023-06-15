package speiger.src.collections.doubles.sets;

import java.util.Set;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractDoubleSet extends AbstractDoubleCollection implements DoubleSet
{
	@Override
	public abstract DoubleIterator iterator();
	@Override
	public AbstractDoubleSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		DoubleIterator i = iterator();
		while(i.hasNext())
			hashCode += Double.hashCode(i.nextDouble());
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