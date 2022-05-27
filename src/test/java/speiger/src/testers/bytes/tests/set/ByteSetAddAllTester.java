package speiger.src.testers.bytes.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.bytes.tests.base.AbstractByteSetTester;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

@Ignore
public class ByteSetAddAllTester extends AbstractByteSetTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedSomePresent() {
		assertTrue("add(somePresent) should return true", getSet().addAll(MinimalByteCollection.of(e3(), e0())));
		expectAdded(e3());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalByteCollection elementsToAdd = MinimalByteCollection.of(e3(), e4(), e3(), e4());
		assertTrue("add(hasDuplicates) should return true", getSet().addAll(elementsToAdd));
		expectAdded(e3(), e4());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertFalse("add(allPresent) should return false", getSet().addAll(MinimalByteCollection.of(e0())));
		expectUnchanged();
	}
}