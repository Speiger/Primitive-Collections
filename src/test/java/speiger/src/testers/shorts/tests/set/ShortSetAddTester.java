package speiger.src.testers.shorts.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortSetTester;

@Ignore
public class ShortSetAddTester extends AbstractShortSetTester {
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_supportedPresent() {
		assertFalse("add(present) should return false", getSet().add(e0()));
		expectUnchanged();
	}
}