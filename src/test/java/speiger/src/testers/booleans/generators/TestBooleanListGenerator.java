package speiger.src.testers.booleans.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.booleans.lists.BooleanList;

public interface TestBooleanListGenerator extends TestListGenerator<Boolean>, TestBooleanCollectionGenerator
{
	@Override
	BooleanList create(Object... elements);
	@Override
	BooleanList create(boolean... elements);
}