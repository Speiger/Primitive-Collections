package speiger.src.testers.booleans.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class BooleanCollectionForEachTester extends AbstractBooleanCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		BooleanList elements = new BooleanArrayList();
		collection.forEach(elements::add);
		BooleanHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		BooleanList elements = new BooleanArrayList();
		collection.forEach(elements, BooleanList::add);
		BooleanHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		BooleanList elements = new BooleanArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", BooleanHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		BooleanList elements = new BooleanArrayList();
		collection.forEach(elements, BooleanList::add);
		assertEquals("Different ordered iteration", BooleanHelpers.copyToList(getOrderedElements()), elements);
	}
}