package speiger.src.testers.chars.tests.collection;

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

import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.chars.utils.CharIteratorTester;

@Ignore
public class CharCollectionIteratorTester extends AbstractCharCollectionTester {
	public void testIterator() {
		CharList elements = new CharArrayList();
		for (CharIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextChar());
		}
		CharHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testIterationOrdering() {
		CharList elements = new CharArrayList();
		for (CharIterator iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.nextChar());
		}
		CharList expected = CharHelpers.copyToList(getOrderedElements());
		assertEquals("Different ordered iteration", expected, elements);
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testIterator_removeAffectsBackingCollection() {
		int originalSize = collection.size();
		CharIterator iterator = collection.iterator();
		char element = iterator.nextChar();
		assertTrue(collection.contains(element)); // sanity check
		iterator.remove();
		assertFalse(collection.contains(element));
		assertEquals(originalSize - 1, collection.size());
	}

	@CollectionFeature.Require({ KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_knownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, CharIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(value = KNOWN_ORDER, absent = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_knownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, CharIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(absent = KNOWN_ORDER, value = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_unknownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, CharIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	@CollectionFeature.Require(absent = { KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_unknownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, CharIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	private void runIteratorTest(Set<IteratorFeature> features, CharIteratorTester.KnownOrder knownOrder, CharIterable elements) {
		new CharIteratorTester(5, features, elements, knownOrder) {
			@Override
			protected CharIterator newTargetIterator() {
				resetCollection();
				return collection.iterator();
			}

			@Override
			protected void verify(CharList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testIteratorNoSuchElementException() {
		CharIterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.nextChar();
		}

		try {
			iterator.nextChar();
			fail("iterator.next() should throw NoSuchElementException");
		} catch (NoSuchElementException expected) {
		}
	}
}