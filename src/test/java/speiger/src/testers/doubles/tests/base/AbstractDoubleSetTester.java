package speiger.src.testers.doubles.tests.base;

import org.junit.Ignore;

import speiger.src.collections.doubles.sets.DoubleSet;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractDoubleSetTester extends AbstractDoubleCollectionTester
{
	protected final DoubleSet getSet() {
		return (DoubleSet)collection;
	}
}