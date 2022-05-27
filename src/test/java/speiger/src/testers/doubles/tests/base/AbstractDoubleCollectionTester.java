package speiger.src.testers.doubles.tests.base;

import org.junit.Ignore;

import speiger.src.collections.doubles.collections.DoubleCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractDoubleCollectionTester extends AbstractDoubleContainerTester<DoubleCollection>
{
	protected DoubleCollection collection;

	@Override
	protected DoubleCollection actualContents() {
		return collection;
	}

	@Override
	protected DoubleCollection resetContainer(DoubleCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}