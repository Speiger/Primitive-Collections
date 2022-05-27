package speiger.src.testers.ints.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.TreeSet;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.tests.base.AbstractIntSetTester;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class IntNavigableSetNavigationTester extends AbstractIntSetTester
{
	private IntNavigableSet navigableSet;
	private IntList values;
	private int a;
	private int b;
	private int c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableSet = (IntNavigableSet) getSet();
		values = IntHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(navigableSet.comparator());
		if (values.size() >= 1) {
			a = values.getInt(0);
			if (values.size() >= 3) {
				b = values.getInt(1);
				c = values.getInt(2);
			}
		}
	}
	
	protected void resetWithHole() {
		super.resetContainer(primitiveGenerator.create(createArray(a, c)));
		navigableSet = (IntNavigableSet) getSet();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.pollFirstInt());
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetNearby() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(e0()));
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.floor(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.ceiling(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.pollLastInt());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, navigableSet.pollFirstInt());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetNearby() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(e0()));
		assertEquals(a, navigableSet.floor(e0()));
		assertEquals(a, navigableSet.ceiling(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, navigableSet.pollLastInt());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, navigableSet.pollFirstInt());
		assertEquals(values.subList(1, values.size()), IntHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			navigableSet.pollFirstInt();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testLowerHole() {
		resetWithHole();
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(a));
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
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(c));
	}

	/*
	 * TODO(cpovirk): make "too small" and "too large" elements available for
	 * better navigation testing. At that point, we may be able to eliminate the
	 * "hole" tests, which would mean that ContiguousSet's tests would no longer
	 * need to suppress them.
	 */
	@CollectionSize.Require(SEVERAL)
	public void testLower() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(a));
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
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(c));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableSet.pollLastInt());
		assertEquals(values.subList(0, values.size() - 1), IntHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			navigableSet.pollLastInt();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testDescendingNavigation() {
		IntList descending = new IntArrayList();
		for (IntIterator i = navigableSet.descendingIterator(); i.hasNext();) {
			descending.add(i.nextInt());
		}
		IntLists.reverse(descending);
		assertEquals(values, descending);
	}

	public void testEmptySubSet() {
		IntNavigableSet empty = navigableSet.subSet(e0(), false, e0(), false);
		assertEquals(new TreeSet<>(), empty);
	}
}