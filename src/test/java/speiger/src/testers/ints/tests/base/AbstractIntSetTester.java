package speiger.src.testers.ints.tests.base;

import org.junit.Ignore;

import speiger.src.collections.ints.sets.IntSet;

@Ignore
public class AbstractIntSetTester extends AbstractIntCollectionTester
{
	protected final IntSet getSet() {
		return (IntSet)collection;
	}
}