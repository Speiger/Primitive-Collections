package speiger.src.testers.floats.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.floats.lists.FloatList;

@SuppressWarnings("javadoc")
public interface TestFloatListGenerator extends TestListGenerator<Float>, TestFloatCollectionGenerator
{
	@Override
	FloatList create(Object... elements);
	@Override
	FloatList create(float... elements);
}