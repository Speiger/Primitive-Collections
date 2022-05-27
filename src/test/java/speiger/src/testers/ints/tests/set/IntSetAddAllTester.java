package speiger.src.testers.ints.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.AbstractIntSetTester;
import speiger.src.testers.ints.utils.MinimalIntCollection;

@Ignore
@SuppressWarnings("javadoc")
public class IntSetAddAllTester extends AbstractIntSetTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedSomePresent() {
		assertTrue("add(somePresent) should return true", getSet().addAll(MinimalIntCollection.of(e3(), e0())));
		expectAdded(e3());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalIntCollection elementsToAdd = MinimalIntCollection.of(e3(), e4(), e3(), e4());
		assertTrue("add(hasDuplicates) should return true", getSet().addAll(elementsToAdd));
		expectAdded(e3(), e4());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertFalse("add(allPresent) should return false", getSet().addAll(MinimalIntCollection.of(e0())));
		expectUnchanged();
	}
}