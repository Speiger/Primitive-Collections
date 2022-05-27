package speiger.src.testers.objects.tests.collection;

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

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.ObjectHelpers;
import speiger.src.testers.objects.utils.ObjectIteratorTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectCollectionIteratorTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterator() {
		ObjectList<T> elements = new ObjectArrayList<>();
		for (ObjectIterator<T> iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.next());
		}
		ObjectHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testIterationOrdering() {
		ObjectList<T> elements = new ObjectArrayList<>();
		for (ObjectIterator<T> iter = collection.iterator();iter.hasNext();) { // uses iterator()
			elements.add(iter.next());
		}
		ObjectList<T> expected = ObjectHelpers.copyToList(getOrderedElements());
		assertEquals("Different ordered iteration", expected, elements);
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testIterator_removeAffectsBackingCollection() {
		int originalSize = collection.size();
		ObjectIterator<T> iterator = collection.iterator();
		T element = iterator.next();
		assertTrue(collection.contains(element)); // sanity check
		iterator.remove();
		assertFalse(collection.contains(element));
		assertEquals(originalSize - 1, collection.size());
	}

	@CollectionFeature.Require({ KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_knownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, ObjectIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(value = KNOWN_ORDER, absent = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_knownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, ObjectIteratorTester.KnownOrder.KNOWN_ORDER, getOrderedElements());
	}

	@CollectionFeature.Require(absent = KNOWN_ORDER, value = SUPPORTS_ITERATOR_REMOVE)
	public void testIterator_unknownOrderRemoveSupported() {
		runIteratorTest(MODIFIABLE, ObjectIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	@CollectionFeature.Require(absent = { KNOWN_ORDER, SUPPORTS_ITERATOR_REMOVE })
	public void testIterator_unknownOrderRemoveUnsupported() {
		runIteratorTest(UNMODIFIABLE, ObjectIteratorTester.KnownOrder.UNKNOWN_ORDER, getSampleElements());
	}

	private void runIteratorTest(Set<IteratorFeature> features, ObjectIteratorTester.KnownOrder knownOrder, ObjectIterable<T> elements) {
		new ObjectIteratorTester<T>(5, features, elements, knownOrder) {
			@Override
			protected ObjectIterator<T> newTargetIterator() {
				resetCollection();
				return collection.iterator();
			}

			@Override
			protected void verify(ObjectList<T> elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testIteratorNoSuchElementException() {
		ObjectIterator<T> iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.next();
		}

		try {
			iterator.next();
			fail("iterator.next() should throw NoSuchElementException");
		} catch (NoSuchElementException expected) {
		}
	}
}