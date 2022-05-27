package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2LongSortedMap;

public interface TestChar2LongSortedMapGenerator extends TestChar2LongMapGenerator, TestSortedMapGenerator<Character, Long>
{
	@Override
	default Char2LongSortedMap create(Object... elements) { return (Char2LongSortedMap) TestChar2LongMapGenerator.super.create(elements); }
	@Override
	Char2LongSortedMap create(Char2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}