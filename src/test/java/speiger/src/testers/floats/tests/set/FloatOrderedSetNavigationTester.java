package speiger.src.testers.floats.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.testers.floats.tests.base.AbstractFloatSetTester;
import speiger.src.testers.floats.utils.FloatHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class FloatOrderedSetNavigationTester extends AbstractFloatSetTester
{
	private FloatOrderedSet orderedSet;
	private FloatList values;
	private float a;
	private float c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (FloatOrderedSet) getSet();
		values = FloatHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getFloat(0);
			if (values.size() >= 3) {
				c = values.getFloat(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		try {
			orderedSet.pollFirstFloat();
			fail("OrderedSet.pollFirstFloat should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		try {
			orderedSet.pollLastFloat();
			fail("OrderedSet.pollLastFloat should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollFirst() {
		try {
			orderedSet.pollFirstFloat();
			fail("OrderedSet.pollFirstFloat should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollLast() {
		try {
			orderedSet.pollLastFloat();
			fail("OrderedSet.pollLastFloat should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, orderedSet.pollFirstFloat());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, orderedSet.pollLastFloat());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, orderedSet.pollFirstFloat());
		assertEquals(values.subList(1, values.size()), FloatHelpers.copyToList(orderedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, orderedSet.pollLastFloat());
		assertEquals(values.subList(0, values.size()-1), FloatHelpers.copyToList(orderedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			orderedSet.pollFirstFloat();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			orderedSet.pollLastFloat();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			orderedSet.firstFloat();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			orderedSet.lastFloat();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, orderedSet.firstFloat());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, orderedSet.lastFloat());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, orderedSet.firstFloat());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, orderedSet.lastFloat());
	}
}