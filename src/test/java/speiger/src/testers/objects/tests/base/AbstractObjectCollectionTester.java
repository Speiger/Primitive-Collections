package speiger.src.testers.objects.tests.base;

import org.junit.Ignore;

import speiger.src.collections.objects.collections.ObjectCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObjectCollectionTester<T> extends AbstractObjectContainerTester<T, ObjectCollection<T>>
{
	protected ObjectCollection<T> collection;

	@Override
	protected ObjectCollection<T> actualContents() {
		return collection;
	}

	@Override
	protected ObjectCollection<T> resetContainer(ObjectCollection<T> newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}