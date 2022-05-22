package speiger.src.testers.ints.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.testers.ints.tests.base.AbstractIntSetTester;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
public class IntOrderedSetNavigationTester extends AbstractIntSetTester {
	private IntOrderedSet orderedSet;
	private IntList values;
	private int a;
	private int c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (IntOrderedSet) getSet();
		values = IntHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getInt(0);
			if (values.size() >= 3) {
				c = values.getInt(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		try {
			orderedSet.pollFirstInt();
			fail("OrderedSet.pollFirstInt should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		try {
			orderedSet.pollLastInt();
			fail("OrderedSet.pollLastInt should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollFirst() {
		try {
			orderedSet.pollFirstInt();
			fail("OrderedSet.pollFirstInt should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollLast() {
		try {
			orderedSet.pollLastInt();
			fail("OrderedSet.pollLastInt should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, orderedSet.pollFirstInt());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, orderedSet.pollLastInt());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, orderedSet.pollFirstInt());
		assertEquals(values.subList(1, values.size()), IntHelpers.copyToList(orderedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, orderedSet.pollLastInt());
		assertEquals(values.subList(0, values.size()-1), IntHelpers.copyToList(orderedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			orderedSet.pollFirstInt();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			orderedSet.pollLastInt();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			orderedSet.firstInt();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			orderedSet.lastInt();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, orderedSet.firstInt());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, orderedSet.lastInt());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, orderedSet.firstInt());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, orderedSet.lastInt());
	}
}