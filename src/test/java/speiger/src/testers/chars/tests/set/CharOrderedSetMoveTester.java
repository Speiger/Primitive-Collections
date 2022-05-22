package speiger.src.testers.chars.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.testers.chars.tests.base.AbstractCharSetTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
public class CharOrderedSetMoveTester extends AbstractCharSetTester {
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
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstChar());
		assertEquals(true, orderedSet.addAndMoveToFirst(e4()));
		assertNotEquals(a, orderedSet.firstChar());
		assertEquals(e4(), orderedSet.firstChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstChar());
		assertEquals(false, orderedSet.addAndMoveToFirst(c));
		assertNotEquals(a, orderedSet.firstChar());
		assertEquals(c, orderedSet.firstChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastChar());
		assertEquals(true, orderedSet.addAndMoveToLast(e4()));
		assertNotEquals(c, orderedSet.lastChar());
		assertEquals(e4(), orderedSet.lastChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastChar());
		assertEquals(false, orderedSet.addAndMoveToLast(a));
		assertNotEquals(c, orderedSet.lastChar());
		assertEquals(a, orderedSet.lastChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstChar());
		assertEquals(false, orderedSet.moveToFirst(e4()));
		assertEquals(a, orderedSet.firstChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstChar());
		assertEquals(true, orderedSet.moveToFirst(c));
		assertNotEquals(a, orderedSet.firstChar());
		assertEquals(c, orderedSet.firstChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastChar());
		assertEquals(false, orderedSet.moveToLast(e4()));
		assertEquals(c, orderedSet.lastChar());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastChar());
		assertEquals(true, orderedSet.moveToLast(a));
		assertNotEquals(c, orderedSet.lastChar());
		assertEquals(a, orderedSet.lastChar());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToFirstUnsupported()
	{
		try {
			orderedSet.addAndMoveToFirst(e4());
			fail("addAndMoveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToLastUnsupported()
	{
		try {
			orderedSet.addAndMoveToLast(e4());
			fail("addAndMoveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToFirstUnsupported()
	{
		try {
			orderedSet.moveToFirst(c);
			fail("moveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToLastUnsupported()
	{
		try {
			orderedSet.moveToLast(a);
			fail("moveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
}