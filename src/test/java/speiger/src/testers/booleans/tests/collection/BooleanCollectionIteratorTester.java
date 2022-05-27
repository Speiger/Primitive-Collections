package speiger.src.testers.booleans.tests.collection;

import static com.google.common.collect.testing.IteratorFeature.MODIFIABLE;
import static com.google.common.collect.testing.IteratorFeature.UNMODIFIABLE;
import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.collect.testing.IteratorFeature;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;
import speiger.src.testers.booleans.utils.BooleanIteratorTester;

@Ignore
public class BooleanCollectionIteratorTester extends AbstractBooleanCollectionTester
{
	public void testIterator() {
		BooleanList elements = new BooleanArrayList();
		for (BooleanIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextBoolean());
		}
		BooleanHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testIterationOrdering() {
		BooleanList elements = new BooleanArrayList();
		for (BooleanIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextBoolean());
		}
		BooleanList expected = BooleanHelpers.copyToList(getOrderedElements());
		assertEquals("Different ordered iteration", expected, elements);
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testIterator_removeAffectsBackingCollection() {
		int originalSize = collection.size();
		BooleanIterator iterator = collection.iterator();
		boolean element = iterator.nextBoolean();
		assertTrue(collection.contains(element)); // sanity check
		iterator.remove();
		assertFalse(collection.contains(element));
		assertEquals(originalSize - 1, collection.size());
	}

	@CollectionFeature.Require({ KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_knownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, BooleanIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(value = KNOWN_ORDER, absent = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_knownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, BooleanIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(absent = KNOWN_ORDER, value = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_unknownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, BooleanIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	@CollectionFeature.Require(absent = { KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_unknownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, BooleanIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	private void runIteratorTest(Set<IteratorFeature> features, BooleanIteratorTester.KnownOrder knownOrder, BooleanIterable elements) {
		new BooleanIteratorTester(5, features, elements, knownOrder) {
			@Override
			protected BooleanIterator newTargetIterator() {
				resetCollection();
				return collection.iterator();
			}

			@Override
			protected void verify(BooleanList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testIteratorNoSuchElementException() {
		BooleanIterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.nextBoolean();
		}

		try {
			iterator.nextBoolean();
			fail("iterator.next() should throw NoSuchElementException");
		} catch (NoSuchElementException expected) {
		}
	}
}