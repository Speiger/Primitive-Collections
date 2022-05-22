package speiger.src.testers.chars.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.testers.chars.tests.base.AbstractCharSetTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
public class CharOrderedSetNavigationTester extends AbstractCharSetTester {
	private CharOrderedSet orderedSet;
	private CharList values;
	private char a;
	private char c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (CharOrderedSet) getSet();
		values = CharHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getChar(0);
			if (values.size() >= 3) {
				c = values.getChar(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		try {
			orderedSet.pollFirstChar();
			fail("OrderedSet.pollFirstChar should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		try {
			orderedSet.pollLastChar();
			fail("OrderedSet.pollLastChar should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollFirst() {
		try {
			orderedSet.pollFirstChar();
			fail("OrderedSet.pollFirstChar should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollLast() {
		try {
			orderedSet.pollLastChar();
			fail("OrderedSet.pollLastChar should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, orderedSet.pollFirstChar());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, orderedSet.pollLastChar());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, orderedSet.pollFirstChar());
		assertEquals(values.subList(1, values.size()), CharHelpers.copyToList(orderedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, orderedSet.pollLastChar());
		assertEquals(values.subList(0, values.size()-1), CharHelpers.copyToList(orderedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			orderedSet.pollFirstChar();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			orderedSet.pollLastChar();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			orderedSet.firstChar();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			orderedSet.lastChar();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, orderedSet.firstChar());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, orderedSet.lastChar());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, orderedSet.firstChar());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, orderedSet.lastChar());
	}
}