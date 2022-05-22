package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;
import speiger.src.testers.doubles.utils.MinimalDoubleCollection;

@Ignore
public class DoubleListAddAllTester extends AbstractDoubleListTester {
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(MinimalDoubleCollection.of(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			getList().addAll(MinimalDoubleCollection.of(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalDoubleCollection elementsToAdd = MinimalDoubleCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll((DoubleCollection)DoubleArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_unsupportedAllPresent() {
		try {
			getList().addAll((DoubleCollection)DoubleArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllListCollection_withDuplicates() {
		DoubleCollection elementsToAdd = DoubleArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(DoubleArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_unsupportedAllPresent() {
		try {
			getList().addAll(DoubleArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllList_withDuplicates() {
		DoubleList elementsToAdd = DoubleArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
}