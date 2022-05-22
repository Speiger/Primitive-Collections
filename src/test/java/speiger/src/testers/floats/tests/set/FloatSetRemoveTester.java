package speiger.src.testers.floats.tests.set;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.AbstractFloatSetTester;

public class FloatSetRemoveTester extends AbstractFloatSetTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_present() {
		getSet().remove(e0());
		assertFalse("After remove(present) a set should not contain the removed element.", getSet().contains(e0()));
	}
}