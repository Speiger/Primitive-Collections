package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
public class ShortListAddAllTester extends AbstractShortListTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(MinimalShortCollection.of(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			getList().addAll(MinimalShortCollection.of(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalShortCollection elementsToAdd = MinimalShortCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll((ShortCollection)ShortArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_unsupportedAllPresent() {
		try {
			getList().addAll((ShortCollection)ShortArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllListCollection_withDuplicates() {
		ShortCollection elementsToAdd = ShortArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(ShortArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_unsupportedAllPresent() {
		try {
			getList().addAll(ShortArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllList_withDuplicates() {
		ShortList elementsToAdd = ShortArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
}