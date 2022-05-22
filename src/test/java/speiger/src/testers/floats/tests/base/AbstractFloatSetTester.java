package speiger.src.testers.floats.tests.base;

import speiger.src.collections.floats.sets.FloatSet;

public class AbstractFloatSetTester extends AbstractFloatCollectionTester {
	protected final FloatSet getSet() {
		return (FloatSet)collection;
	}
}