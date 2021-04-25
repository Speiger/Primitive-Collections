package speiger.src.collections.ints.maps;

import speiger.src.collections.ints.base.BaseInt2IntMapTest;
import speiger.src.collections.ints.maps.impl.hash.Int2IntOpenHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;

@SuppressWarnings("javadoc")
public class Int2IntHashMapTest extends BaseInt2IntMapTest
{
	
	@Override
	public Int2IntMap createMap(int[] keys, int[] values)
	{
		return new Int2IntOpenHashMap(keys, values);
	}
	
	@Override
	public Int2IntMap createEmptyMap()
	{
		return new Int2IntOpenHashMap();
	}
	
}
