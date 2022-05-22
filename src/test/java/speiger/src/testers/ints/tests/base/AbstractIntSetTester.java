package speiger.src.testers.ints.tests.base;

import speiger.src.collections.ints.sets.IntSet;

public class AbstractIntSetTester extends AbstractIntCollectionTester {
	protected final IntSet getSet() {
		return (IntSet)collection;
	}
}