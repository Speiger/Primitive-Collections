package speiger.src.collections.shorts.sets;

import java.util.Set;

import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractShortSet extends AbstractShortCollection implements ShortSet
{
	@Override
	public abstract ShortIterator iterator();
	@Override
	public AbstractShortSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		ShortIterator i = iterator();
		while(i.hasNext())
			hashCode += Short.hashCode(i.nextShort());
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