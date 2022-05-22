package speiger.src.testers.booleans.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.booleans.sets.BooleanSet;

public interface TestBooleanSetGenerator extends TestBooleanCollectionGenerator, TestSetGenerator<Boolean> {
	@Override
	BooleanSet create(boolean...elements);
	@Override
	BooleanSet create(Object...elements);
}