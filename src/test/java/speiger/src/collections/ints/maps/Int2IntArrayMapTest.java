package speiger.src.collections.ints.maps;

import speiger.src.collections.ints.base.BaseInt2IntOrderedMapTest;
import speiger.src.collections.ints.maps.impl.misc.Int2IntArrayMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntOrderedMap;

@SuppressWarnings("javadoc")
public class Int2IntArrayMapTest extends BaseInt2IntOrderedMapTest
{	
	@Override
	public Int2IntOrderedMap createMap(int[] keys, int[] values)
	{
		return new Int2IntArrayMap(keys, values);
	}
	
	@Override
	public Int2IntOrderedMap createEmptyMap()
	{
		return new Int2IntArrayMap();
	}
}