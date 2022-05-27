package speiger.src.testers.objects.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.TreeSet;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.tests.base.AbstractObjectSetTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class ObjectNavigableSetNavigationTester<T> extends AbstractObjectSetTester<T>
{
	private ObjectNavigableSet<T> navigableSet;
	private ObjectList<T> values;
	private T a;
	private T b;
	private T c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableSet = (ObjectNavigableSet<T>) getSet();
		values = ObjectHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(navigableSet.comparator());
		if (values.size() >= 1) {
			a = values.get(0);
			if (values.size() >= 3) {
				b = values.get(1);
				c = values.get(2);
			}
		}
	}
	
	protected void resetWithHole() {
		super.resetContainer(primitiveGenerator.create(createArray(a, c)));
		navigableSet = (ObjectNavigableSet<T>) getSet();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(null, navigableSet.pollFirst());
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetNearby() {
		assertEquals(null, navigableSet.lower(e0()));
		assertEquals(null, navigableSet.floor(e0()));
		assertEquals(null, navigableSet.ceiling(e0()));
		assertEquals(null, navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(null, navigableSet.pollLast());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, navigableSet.pollFirst());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetNearby() {
		assertEquals(null, navigableSet.lower(e0()));
		assertEquals(a, navigableSet.floor(e0()));
		assertEquals(a, navigableSet.ceiling(e0()));
		assertEquals(null, navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, navigableSet.pollLast());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, navigableSet.pollFirst());
		assertEquals(values.subList(1, values.size()), ObjectHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			navigableSet.pollFirst();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testLowerHole() {
		resetWithHole();
		assertEquals(null, navigableSet.lower(a));
		assertEquals(a, navigableSet.lower(b));
		assertEquals(a, navigableSet.lower(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloorHole() {
		resetWithHole();
		assertEquals(a, navigableSet.floor(a));
		assertEquals(a, navigableSet.floor(b));
		assertEquals(c, navigableSet.floor(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeilingHole() {
		resetWithHole();
		assertEquals(a, navigableSet.ceiling(a));
		assertEquals(c, navigableSet.ceiling(b));
		assertEquals(c, navigableSet.ceiling(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigherHole() {
		resetWithHole();
		assertEquals(c, navigableSet.higher(a));
		assertEquals(c, navigableSet.higher(b));
		assertEquals(null, navigableSet.higher(c));
	}

	/*
	 * TODO(cpovirk): make "too small" and "too large" elements available for
	 * better navigation testing. At that point, we may be able to eliminate the
	 * "hole" tests, which would mean that ContiguousSet's tests would no longer
	 * need to suppress them.
	 */
	@CollectionSize.Require(SEVERAL)
	public void testLower() {
		assertEquals(null, navigableSet.lower(a));
		assertEquals(a, navigableSet.lower(b));
		assertEquals(b, navigableSet.lower(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		assertEquals(a, navigableSet.floor(a));
		assertEquals(b, navigableSet.floor(b));
		assertEquals(c, navigableSet.floor(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		assertEquals(a, navigableSet.ceiling(a));
		assertEquals(b, navigableSet.ceiling(b));
		assertEquals(c, navigableSet.ceiling(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		assertEquals(b, navigableSet.higher(a));
		assertEquals(c, navigableSet.higher(b));
		assertEquals(null, navigableSet.higher(c));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableSet.pollLast());
		assertEquals(values.subList(0, values.size() - 1), ObjectHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			navigableSet.pollLast();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testDescendingNavigation() {
		ObjectList<T> descending = new ObjectArrayList<>();
		for (ObjectIterator<T> i = navigableSet.descendingIterator(); i.hasNext();) {
			descending.add(i.next());
		}
		ObjectLists.reverse(descending);
		assertEquals(values, descending);
	}

	public void testEmptySubSet() {
		ObjectNavigableSet<T> empty = navigableSet.subSet(e0(), false, e0(), false);
		assertEquals(new TreeSet<>(), empty);
	}
}