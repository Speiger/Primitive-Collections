package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2ShortSortedMap;

@SuppressWarnings("javadoc")
public interface TestChar2ShortSortedMapGenerator extends TestChar2ShortMapGenerator, TestSortedMapGenerator<Character, Short>
{
	@Override
	default Char2ShortSortedMap create(Object... elements) { return (Char2ShortSortedMap) TestChar2ShortMapGenerator.super.create(elements); }
	@Override
	Char2ShortSortedMap create(Char2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}