package speiger.src.testers.doubles.generators;

import speiger.src.collections.doubles.sets.DoubleNavigableSet;

@SuppressWarnings("javadoc")
public interface TestDoubleNavigableSetGenerator extends TestDoubleSortedSetGenerator {
	@Override
	DoubleNavigableSet create(double... elements);
	@Override
	DoubleNavigableSet create(Object... elements);
}