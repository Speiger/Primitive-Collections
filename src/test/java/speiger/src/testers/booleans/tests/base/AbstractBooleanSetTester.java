package speiger.src.testers.booleans.tests.base;

import speiger.src.collections.booleans.sets.BooleanSet;

public class AbstractBooleanSetTester extends AbstractBooleanCollectionTester {
	protected final BooleanSet getSet() {
		return (BooleanSet)collection;
	}
}