package speiger.src.testers.floats.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.floats.sets.FloatSet;

public interface TestFloatSetGenerator extends TestFloatCollectionGenerator, TestSetGenerator<Float> {
	@Override
	FloatSet create(float...elements);
	@Override
	FloatSet create(Object...elements);
}