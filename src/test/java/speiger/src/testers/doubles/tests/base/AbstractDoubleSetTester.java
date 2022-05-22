package speiger.src.testers.doubles.tests.base;

import speiger.src.collections.doubles.sets.DoubleSet;

public class AbstractDoubleSetTester extends AbstractDoubleCollectionTester {
	protected final DoubleSet getSet() {
		return (DoubleSet)collection;
	}
}