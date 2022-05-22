package speiger.src.testers.doubles.generators;

import speiger.src.collections.doubles.sets.DoubleOrderedSet;

public interface TestDoubleOrderedSetGenerator extends TestDoubleSetGenerator {
	@Override
	DoubleOrderedSet create(double... elements);
	@Override
	DoubleOrderedSet create(Object... elements);
}