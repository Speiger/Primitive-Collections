package speiger.src.collections.ints.maps;

import java.util.EnumSet;

import speiger.src.collections.ints.base.BaseInt2IntSortedMapTest;
import speiger.src.collections.ints.maps.impl.customHash.Int2IntLinkedOpenCustomHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;
import speiger.src.collections.tests.SortedMapTests;

public class Int2IntLinkedOpenCustomHashMapTest extends BaseInt2IntSortedMapTest
{
	@Override
	public EnumSet<SortedMapTests> getValidSortedMapTests() { return EnumSet.complementOf(EnumSet.of(SortedMapTests.SUB_MAP, SortedMapTests.TAIL_MAP, SortedMapTests.HEAD_MAP)); }
	
	@Override
	public Int2IntSortedMap createMap(int[] keys, int[] values)
	{
		return new Int2IntLinkedOpenCustomHashMap(keys, values, STRATEGY);
	}
	
	@Override
	public Int2IntSortedMap createEmptyMap()
	{
		return new Int2IntLinkedOpenCustomHashMap(STRATEGY);
	}
}