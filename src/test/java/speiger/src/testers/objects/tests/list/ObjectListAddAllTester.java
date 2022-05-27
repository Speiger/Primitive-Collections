package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectListAddAllTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(MinimalObjectCollection.of(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			getList().addAll(MinimalObjectCollection.of(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_withDuplicates() {
		MinimalObjectCollection<T> elementsToAdd = MinimalObjectCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll((ObjectCollection<T>)ObjectArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllListCollection_unsupportedAllPresent() {
		try {
			getList().addAll((ObjectCollection<T>)ObjectArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllListCollection_withDuplicates() {
		ObjectCollection<T> elementsToAdd = ObjectArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_supportedAllPresent() {
		assertTrue("addAll(allPresent) should return true", getList().addAll(ObjectArrayList.wrap(e0())));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllList_unsupportedAllPresent() {
		try {
			getList().addAll(ObjectArrayList.wrap(e0()));
			fail("addAll(allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllList_withDuplicates() {
		ObjectList<T> elementsToAdd = ObjectArrayList.wrap(e0(), e1(), e0(), e1());
		assertTrue("addAll(hasDuplicates) should return true", getList().addAll(elementsToAdd));
		expectAdded(e0(), e1(), e0(), e1());
	}
}