package speiger.src.testers.shorts.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortSetTester;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
public class ShortSetAddAllTester extends AbstractShortSetTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedSomePresent() {
		assertTrue("add(somePresent) should return true", getSet().addAll(MinimalShortCollection.of(e3(), e0())));
		expectAdded(e3());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalShortCollection elementsToAdd = MinimalShortCollection.of(e3(), e4(), e3(), e4());
		assertTrue("add(hasDuplicates) should return true", getSet().addAll(elementsToAdd));
		expectAdded(e3(), e4());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertFalse("add(allPresent) should return false", getSet().addAll(MinimalShortCollection.of(e0())));
		expectUnchanged();
	}
}