package speiger.src.testers.longs.tests.base;

import speiger.src.collections.longs.collections.LongCollection;

public class AbstractLongCollectionTester extends AbstractLongContainerTester {

	protected LongCollection collection;

	@Override
	protected LongCollection actualContents() {
		return collection;
	}

	@Override
	protected LongCollection resetContainer(LongCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}