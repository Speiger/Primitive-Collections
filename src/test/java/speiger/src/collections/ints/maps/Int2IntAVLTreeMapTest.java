package speiger.src.collections.ints.maps;

import speiger.src.collections.ints.base.BaseInt2IntNavigableMapTest;
import speiger.src.collections.ints.maps.impl.tree.Int2IntAVLTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntNavigableMap;

@SuppressWarnings("javadoc")
public class Int2IntAVLTreeMapTest extends BaseInt2IntNavigableMapTest
{	
	@Override
	public Int2IntNavigableMap createMap(int[] keys, int[] values)
	{
		return new Int2IntAVLTreeMap(keys, values);
	}
	
	@Override
	public Int2IntNavigableMap createEmptyMap()
	{
		return new Int2IntAVLTreeMap();
	}
}