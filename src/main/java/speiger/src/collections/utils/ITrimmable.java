package speiger.src.collections.utils;

public interface ITrimmable
{
	public default void trim() {
		trim(0);
	}
	
	public void trim(int size);
}
