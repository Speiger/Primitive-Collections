package speiger.src.testers.booleans.tests.base;

import org.junit.Ignore;

import speiger.src.collections.booleans.sets.BooleanSet;

@Ignore
public class AbstractBooleanSetTester extends AbstractBooleanCollectionTester
{
	protected final BooleanSet getSet() {
		return (BooleanSet)collection;
	}
}