package speiger.src.testers.shorts.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.testers.shorts.tests.base.AbstractShortSetTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ShortOrderedSetMoveTester extends AbstractShortSetTester
{
	private ShortOrderedSet orderedSet;
	private ShortList values;
	private short a;
	private short c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (ShortOrderedSet) getSet();
		values = ShortHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getShort(0);
			if (values.size() >= 3) {
				c = values.getShort(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstShort());
		assertEquals(true, orderedSet.addAndMoveToFirst(e4()));
		assertNotEquals(a, orderedSet.firstShort());
		assertEquals(e4(), orderedSet.firstShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstShort());
		assertEquals(false, orderedSet.addAndMoveToFirst(c));
		assertNotEquals(a, orderedSet.firstShort());
		assertEquals(c, orderedSet.firstShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastShort());
		assertEquals(true, orderedSet.addAndMoveToLast(e4()));
		assertNotEquals(c, orderedSet.lastShort());
		assertEquals(e4(), orderedSet.lastShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastShort());
		assertEquals(false, orderedSet.addAndMoveToLast(a));
		assertNotEquals(c, orderedSet.lastShort());
		assertEquals(a, orderedSet.lastShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstShort());
		assertEquals(false, orderedSet.moveToFirst(e4()));
		assertEquals(a, orderedSet.firstShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstShort());
		assertEquals(true, orderedSet.moveToFirst(c));
		assertNotEquals(a, orderedSet.firstShort());
		assertEquals(c, orderedSet.firstShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastShort());
		assertEquals(false, orderedSet.moveToLast(e4()));
		assertEquals(c, orderedSet.lastShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastShort());
		assertEquals(true, orderedSet.moveToLast(a));
		assertNotEquals(c, orderedSet.lastShort());
		assertEquals(a, orderedSet.lastShort());
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