package speiger.src.testers.doubles.tests.list;

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

import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.utils.DoubleLists;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.doubles.utils.DoubleListIteratorTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleListListIteratorTester extends AbstractDoubleListTester
{
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
		new DoubleListIteratorTester(4, DoubleLists.singleton(e4()), features, DoubleHelpers.copyToList(getOrderedElements()), 0) {
			@Override
			protected DoubleListIterator newTargetIterator() {
				resetCollection();
				return getList().listIterator();
			}

			@Override
			protected void verify(DoubleList elements) {
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