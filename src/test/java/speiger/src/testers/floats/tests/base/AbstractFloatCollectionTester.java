package speiger.src.testers.floats.tests.base;

import speiger.src.collections.floats.collections.FloatCollection;

public class AbstractFloatCollectionTester extends AbstractFloatContainerTester {

	protected FloatCollection collection;

	@Override
	protected FloatCollection actualContents() {
		return collection;
	}

	@Override
	protected FloatCollection resetContainer(FloatCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}