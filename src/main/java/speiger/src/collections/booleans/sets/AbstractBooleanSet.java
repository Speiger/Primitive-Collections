package speiger.src.collections.booleans.sets;

import java.util.Set;

import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractBooleanSet extends AbstractBooleanCollection implements BooleanSet
{
	@Override
	public abstract BooleanIterator iterator();
	@Override
	public AbstractBooleanSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		BooleanIterator i = iterator();
		while(i.hasNext())
			hashCode += Boolean.hashCode(i.nextBoolean());
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