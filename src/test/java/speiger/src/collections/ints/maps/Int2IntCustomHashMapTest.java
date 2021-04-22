package speiger.src.collections.ints.maps;

import speiger.src.collections.ints.base.BaseInt2IntMapTest;
import speiger.src.collections.ints.maps.impl.customHash.Int2IntOpenCustomHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;

public class Int2IntCustomHashMapTest extends BaseInt2IntMapTest
{
	@Override
	public Int2IntMap createMap(int[] keys, int[] values)
	{
		return new Int2IntOpenCustomHashMap(keys, values, STRATEGY);
	}
	
	@Override
	public Int2IntMap createEmptyMap()
	{
		return new Int2IntOpenCustomHashMap(STRATEGY);
	}
}