package speiger.src.testers.bytes.tests.collection;

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

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.bytes.utils.ByteIteratorTester;

@Ignore
public class ByteCollectionIteratorTester extends AbstractByteCollectionTester {
	public void testIterator() {
		ByteList elements = new ByteArrayList();
		for (ByteIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextByte());
		}
		ByteHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testIterationOrdering() {
		ByteList elements = new ByteArrayList();
		for (ByteIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextByte());
		}
		ByteList expected = ByteHelpers.copyToList(getOrderedElements());
		assertEquals("Different ordered iteration", expected, elements);
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testIterator_removeAffectsBackingCollection() {
		int originalSize = collection.size();
		ByteIterator iterator = collection.iterator();
		byte element = iterator.nextByte();
		assertTrue(collection.contains(element)); // sanity check
		iterator.remove();
		assertFalse(collection.contains(element));
		assertEquals(originalSize - 1, collection.size());
	}

	@CollectionFeature.Require({ KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_knownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, ByteIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(value = KNOWN_ORDER, absent = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_knownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, ByteIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(absent = KNOWN_ORDER, value = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_unknownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, ByteIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	@CollectionFeature.Require(absent = { KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_unknownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, ByteIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	private void runIteratorTest(Set<IteratorFeature> features, ByteIteratorTester.KnownOrder knownOrder, ByteIterable elements) {
		new ByteIteratorTester(5, features, elements, knownOrder) {
			@Override
			protected ByteIterator newTargetIterator() {
				resetCollection();
				return collection.iterator();
			}

			@Override
			protected void verify(ByteList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testIteratorNoSuchElementException() {
		ByteIterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.nextByte();
		}

		try {
			iterator.nextByte();
			fail("iterator.next() should throw NoSuchElementException");
		} catch (NoSuchElementException expected) {
		}
	}
}