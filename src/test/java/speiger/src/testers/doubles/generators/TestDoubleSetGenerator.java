package speiger.src.testers.doubles.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.doubles.sets.DoubleSet;

@SuppressWarnings("javadoc")
public interface TestDoubleSetGenerator extends TestDoubleCollectionGenerator, TestSetGenerator<Double> {
	@Override
	DoubleSet create(double...elements);
	@Override
	DoubleSet create(Object...elements);
}