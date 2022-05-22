package speiger.src.testers.floats.tests.collection;

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

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.floats.utils.FloatIteratorTester;

@Ignore
public class FloatCollectionIteratorTester extends AbstractFloatCollectionTester {
	public void testIterator() {
		FloatList elements = new FloatArrayList();
		for (FloatIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextFloat());
		}
		FloatHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testIterationOrdering() {
		FloatList elements = new FloatArrayList();
		for (FloatIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextFloat());
		}
		FloatList expected = FloatHelpers.copyToList(getOrderedElements());
		assertEquals("Different ordered iteration", expected, elements);
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testIterator_removeAffectsBackingCollection() {
		int originalSize = collection.size();
		FloatIterator iterator = collection.iterator();
		float element = iterator.nextFloat();
		assertTrue(collection.contains(element)); // sanity check
		iterator.remove();
		assertFalse(collection.contains(element));
		assertEquals(originalSize - 1, collection.size());
	}

	@CollectionFeature.Require({ KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_knownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, FloatIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(value = KNOWN_ORDER, absent = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_knownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, FloatIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(absent = KNOWN_ORDER, value = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_unknownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, FloatIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	@CollectionFeature.Require(absent = { KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_unknownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, FloatIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	private void runIteratorTest(Set<IteratorFeature> features, FloatIteratorTester.KnownOrder knownOrder, FloatIterable elements) {
		new FloatIteratorTester(5, features, elements, knownOrder) {
			@Override
			protected FloatIterator newTargetIterator() {
				resetCollection();
				return collection.iterator();
			}

			@Override
			protected void verify(FloatList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testIteratorNoSuchElementException() {
		FloatIterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.nextFloat();
		}

		try {
			iterator.nextFloat();
			fail("iterator.next() should throw NoSuchElementException");
		} catch (NoSuchElementException expected) {
		}
	}
}