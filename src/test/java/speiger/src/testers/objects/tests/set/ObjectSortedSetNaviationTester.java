package speiger.src.testers.objects.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.testers.objects.tests.base.AbstractObjectSetTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectSortedSetNaviationTester<T> extends AbstractObjectSetTester<T>
{
	private ObjectSortedSet<T> sortedSet;
	private ObjectList<T> values;
	private T a;
	private T c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedSet = (ObjectSortedSet<T>) getSet();
		values = ObjectHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(sortedSet.comparator());

		// some tests assume SEVERAL == 3
		if (values.size() >= 1) {
			a = values.get(0);
			if (values.size() >= 3) {
				c = values.get(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(null, sortedSet.pollFirst());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(null, sortedSet.pollLast());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, sortedSet.pollFirst());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, sortedSet.pollLast());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, sortedSet.pollFirst());
		assertEquals(values.subList(1, values.size()), ObjectHelpers.copyToList(sortedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, sortedSet.pollLast());
		assertEquals(values.subList(0, values.size()-1), ObjectHelpers.copyToList(sortedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			sortedSet.pollFirst();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			sortedSet.pollLast();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			sortedSet.first();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			sortedSet.last();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, sortedSet.first());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, sortedSet.last());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, sortedSet.first());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, sortedSet.last());
	}
}