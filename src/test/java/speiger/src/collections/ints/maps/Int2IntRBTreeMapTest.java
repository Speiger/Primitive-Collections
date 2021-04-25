package speiger.src.collections.ints.maps;

import java.util.EnumSet;

import speiger.src.collections.ints.base.BaseInt2IntNavigableMapTest;
import speiger.src.collections.ints.maps.impl.tree.Int2IntRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntNavigableMap;
import speiger.src.collections.tests.SortedMapTests;

@SuppressWarnings("javadoc")
public class Int2IntRBTreeMapTest extends BaseInt2IntNavigableMapTest
{
	@Override
	public EnumSet<SortedMapTests> getValidSortedMapTests() { return EnumSet.complementOf(EnumSet.of(SortedMapTests.GET_MOVE, SortedMapTests.MOVE, SortedMapTests.PUT_MOVE)); }
	
	@Override
	public Int2IntNavigableMap createMap(int[] keys, int[] values)
	{
		return new Int2IntRBTreeMap(keys, values);
	}
	
	@Override
	public Int2IntNavigableMap createEmptyMap()
	{
		return new Int2IntRBTreeMap();
	}
}