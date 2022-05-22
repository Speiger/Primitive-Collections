package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;

public class CharCollectionForEachTester extends AbstractCharCollectionTester {
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		CharList elements = new CharArrayList();
		collection.forEach(elements::add);
		CharHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		CharList elements = new CharArrayList();
		collection.forEach(elements, CharList::add);
		CharHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		CharList elements = new CharArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", CharHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		CharList elements = new CharArrayList();
		collection.forEach(elements, CharList::add);
		assertEquals("Different ordered iteration", CharHelpers.copyToList(getOrderedElements()), elements);
	}
}