package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;
import speiger.src.testers.longs.utils.MinimalLongCollection;

@Ignore
@SuppressWarnings("javadoc")
public class LongListAddAllTester extends AbstractLongListTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(MinimalLongCollection.of(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			getList().addAll(MinimalLongCollection.of(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalLongCollection elementsToAdd = MinimalLongCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll((LongCollection)LongArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_unsupportedAllPresent() {
		try {
			getList().addAll((LongCollection)LongArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllListCollection_withDuplicates() {
		LongCollection elementsToAdd = LongArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(LongArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_unsupportedAllPresent() {
		try {
			getList().addAll(LongArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllList_withDuplicates() {
		LongList elementsToAdd = LongArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
}