package speiger.src.collections.ints.maps;

import speiger.src.collections.ints.base.BaseInt2IntSortedMapTest;
import speiger.src.collections.ints.maps.impl.misc.Int2IntArrayMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;

@SuppressWarnings("javadoc")
public class Int2IntArrayMapTest extends BaseInt2IntSortedMapTest
{
	@Override
	public Int2IntSortedMap createMap(int[] keys, int[] values)
	{
		return new Int2IntArrayMap(keys, values);
	}
	
	@Override
	public Int2IntSortedMap createEmptyMap()
	{
		return new Int2IntArrayMap();
	}
}