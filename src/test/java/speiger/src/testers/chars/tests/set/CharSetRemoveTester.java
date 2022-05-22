package speiger.src.testers.chars.tests.set;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.chars.tests.base.AbstractCharSetTester;

public class CharSetRemoveTester extends AbstractCharSetTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_present() {
		getSet().remove(e0());
		assertFalse("After remove(present) a set should not contain the removed element.", getSet().contains(e0()));
	}
}