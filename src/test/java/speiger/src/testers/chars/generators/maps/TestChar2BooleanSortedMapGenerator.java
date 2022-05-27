package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanSortedMap;

@SuppressWarnings("javadoc")
public interface TestChar2BooleanSortedMapGenerator extends TestChar2BooleanMapGenerator, TestSortedMapGenerator<Character, Boolean>
{
	@Override
	default Char2BooleanSortedMap create(Object... elements) { return (Char2BooleanSortedMap) TestChar2BooleanMapGenerator.super.create(elements); }
	@Override
	Char2BooleanSortedMap create(Char2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}