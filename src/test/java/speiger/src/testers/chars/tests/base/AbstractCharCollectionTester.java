package speiger.src.testers.chars.tests.base;

import org.junit.Ignore;

import speiger.src.collections.chars.collections.CharCollection;

@Ignore
public class AbstractCharCollectionTester extends AbstractCharContainerTester<CharCollection>
{
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