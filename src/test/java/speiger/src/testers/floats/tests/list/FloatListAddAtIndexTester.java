package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
public class FloatListAddAtIndexTester extends AbstractFloatListTester
{
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAtIndex_supportedPresent() {
		getList().add(0, e0());
		expectAddedIndex(0, e0());
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAtIndex_unsupportedPresent() {
		try {
			getList().add(0, e0());
			fail("add(n, present) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAtIndex_supportedNotPresent() {
		getList().add(0, e3());
		expectAddedIndex(0, e3());
	}

	@CollectionFeature.Require(FAILS_FAST_ON_CONCURRENT_MODIFICATION)
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAtIndexConcurrentWithIteration() {
		try {
			FloatIterator iterator = collection.iterator();
			getList().add(0, e3());
			iterator.nextFloat();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	public void testAddAtIndex_unsupportedNotPresent() {
		try {
			getList().add(0, e3());
			fail("add(n, notPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testAddAtIndex_middle() {
		getList().add(getNumElements() / 2, e3());
		expectAddedIndex(getNumElements() / 2, e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAtIndex_end() {
		getList().add(getNumElements(), e3());
		expectAddedIndex(getNumElements(), e3());
	}
	
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAtIndex_negative() {
		try {
			getList().add(-1, e3());
			fail("add(-1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAtIndex_tooLarge() {
		try {
			getList().add(getNumElements() + 1, e3());
			fail("add(size + 1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}
}