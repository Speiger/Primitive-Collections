package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

@Ignore
@SuppressWarnings("javadoc")
public class ByteListAddAllTester extends AbstractByteListTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(MinimalByteCollection.of(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			getList().addAll(MinimalByteCollection.of(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalByteCollection elementsToAdd = MinimalByteCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll((ByteCollection)ByteArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_unsupportedAllPresent() {
		try {
			getList().addAll((ByteCollection)ByteArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllListCollection_withDuplicates() {
		ByteCollection elementsToAdd = ByteArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(ByteArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_unsupportedAllPresent() {
		try {
			getList().addAll(ByteArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllList_withDuplicates() {
		ByteList elementsToAdd = ByteArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
}