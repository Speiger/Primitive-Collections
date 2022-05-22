package speiger.src.testers.booleans.tests.list;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;

public class BooleanListAddTester extends AbstractBooleanListTester {
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