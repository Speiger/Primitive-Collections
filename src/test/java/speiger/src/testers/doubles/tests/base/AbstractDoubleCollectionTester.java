package speiger.src.testers.doubles.tests.base;

import org.junit.Ignore;

import speiger.src.collections.doubles.collections.DoubleCollection;

@Ignore
public class AbstractDoubleCollectionTester extends AbstractDoubleContainerTester {

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