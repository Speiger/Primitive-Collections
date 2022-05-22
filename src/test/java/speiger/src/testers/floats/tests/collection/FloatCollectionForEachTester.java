package speiger.src.testers.floats.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.floats.utils.FloatHelpers;

@Ignore
public class FloatCollectionForEachTester extends AbstractFloatCollectionTester {
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		FloatList elements = new FloatArrayList();
		collection.forEach(elements::add);
		FloatHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		FloatList elements = new FloatArrayList();
		collection.forEach(elements, FloatList::add);
		FloatHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		FloatList elements = new FloatArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", FloatHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		FloatList elements = new FloatArrayList();
		collection.forEach(elements, FloatList::add);
		assertEquals("Different ordered iteration", FloatHelpers.copyToList(getOrderedElements()), elements);
	}
}