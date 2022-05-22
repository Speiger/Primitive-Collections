package speiger.src.testers.chars.tests.base;

import speiger.src.collections.chars.collections.CharCollection;

public class AbstractCharCollectionTester extends AbstractCharContainerTester {

	protected CharCollection collection;

	@Override
	protected CharCollection actualContents() {
		return collection;
	}

	@Override
	protected CharCollection resetContainer(CharCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}