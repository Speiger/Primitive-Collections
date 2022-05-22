package speiger.src.testers.booleans.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.IteratorFeature.MODIFIABLE;
import static com.google.common.collect.testing.IteratorFeature.UNMODIFIABLE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Set;

import com.google.common.collect.testing.IteratorFeature;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.booleans.utils.BooleanLists;
import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;
import speiger.src.testers.booleans.utils.BooleanListIteratorTester;

@Ignore
public class BooleanListListIteratorTester extends AbstractBooleanListTester {
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@ListFeature.Require(absent = { SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_unmodifiable() {
		runListIteratorTest(UNMODIFIABLE);
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@ListFeature.Require({ SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_fullyModifiable() {
		runListIteratorTest(MODIFIABLE);
	}

	private void runListIteratorTest(Set<IteratorFeature> features) {
		new BooleanListIteratorTester(4, BooleanLists.singleton(e4()), features, BooleanHelpers.copyToList(getOrderedElements()), 0) {
			@Override
			protected BooleanListIterator newTargetIterator() {
				resetCollection();
				return getList().listIterator();
			}

			@Override
			protected void verify(BooleanList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testListIterator_tooLow() {
		try {
			getList().listIterator(-1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_tooHigh() {
		try {
			getList().listIterator(getNumElements() + 1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_atSize() {
		getList().listIterator(getNumElements());
	}
}