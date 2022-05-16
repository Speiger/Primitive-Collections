package speiger.src.collections.chars.sets;

import java.util.Set;

import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractCharSet extends AbstractCharCollection implements CharSet
{
	@Override
	public abstract CharIterator iterator();
	@Override
	public AbstractCharSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		CharIterator i = iterator();
		while(i.hasNext())
			hashCode += Character.hashCode(i.nextChar());
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