package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;

public interface TestChar2IntSortedMapGenerator extends TestChar2IntMapGenerator, TestSortedMapGenerator<Character, Integer>
{
	@Override
	default Char2IntSortedMap create(Object... elements) { return (Char2IntSortedMap) TestChar2IntMapGenerator.super.create(elements); }
	@Override
	Char2IntSortedMap create(Char2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}