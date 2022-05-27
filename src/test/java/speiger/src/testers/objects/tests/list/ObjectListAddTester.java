package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectListAddTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_supportedPresent() {
		assertTrue("add(present) should return true", getList().add(e0()));
		expectAdded(e0());
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_unsupportedPresent() {
		try {
			getList().add(e0());
			fail("add(present) should throw");
		} catch (UnsupportedOperationException expected) {
		}
	}
}