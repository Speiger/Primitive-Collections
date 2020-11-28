package speiger.src.collections.utils;

import java.util.RandomAccess;

public interface IArray extends RandomAccess, ITrimmable
{
	public void ensureCapacity(int size);
}
