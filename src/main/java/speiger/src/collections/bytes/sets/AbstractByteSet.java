package speiger.src.collections.bytes.sets;

import java.util.Set;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractByteSet extends AbstractByteCollection implements ByteSet
{
	@Override
	public abstract ByteIterator iterator();
	@Override
	public AbstractByteSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		ByteIterator i = iterator();
		while(i.hasNext())
			hashCode += Byte.hashCode(i.nextByte());
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