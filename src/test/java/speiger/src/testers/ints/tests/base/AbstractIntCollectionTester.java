package speiger.src.testers.ints.tests.base;

import speiger.src.collections.ints.collections.IntCollection;

public class AbstractIntCollectionTester extends AbstractIntContainerTester {

	protected IntCollection collection;

	@Override
	protected IntCollection actualContents() {
		return collection;
	}

	@Override
	protected IntCollection resetContainer(IntCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}