package speiger.src.testers.shorts.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.utils.ShortLists;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.shorts.utils.ShortSamples;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
public abstract class AbstractShortContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Short>, Short>> {
	protected ShortSamples samples;
	protected ShortCollection container;
	protected TestShortCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Short>, Short> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShortCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestShortCollectionGenerator");
		primitiveGenerator = (TestShortCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract ShortCollection actualContents();

	protected ShortCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected ShortCollection resetContainer(ShortCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(short... elements) {
		expectContents(ShortArrayList.wrap(elements));
	}

	protected void expectContents(ShortCollection expected) {
		ShortHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(short... elements) {
		ShortList expected = ShortHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, short... elements) {
		expectAdded(index, ShortArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, ShortCollection elements) {
		ShortList expected = ShortHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(short... elements) {
		for (short element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected short[] createSamplesArray() {
		return getSampleElements().toShortArray();
	}

	protected short[] createOrderedArray() {
		return getOrderedElements().toShortArray();
	}

	public static class ArrayWithDuplicate {
		public final short[] elements;
		public final short duplicate;

		private ArrayWithDuplicate(short[] elements, short duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		short[] elements = createSamplesArray();
		short duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected ShortCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected ShortList getOrderedElements() {
		ShortList list = new ShortArrayList();
		for (ShortIterator iter = primitiveGenerator.order(new ShortArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextShort());
		}
		return ShortLists.unmodifiable(list);
	}
	
	protected short[] createDisjointArray() {
		return new short[]{e3(), e4()};
	}
	
	protected short[] emptyArray() {
		return new short[0];
	}
	
	protected MinimalShortCollection createDisjointCollection() {
		return MinimalShortCollection.of(e3(), e4());
	}
	
	protected MinimalShortCollection emptyCollection() {
		return MinimalShortCollection.of();
	}

	protected final short e0() {
		return samples.e0();
	}

	protected final short e1() {
		return samples.e1();
	}

	protected final short e2() {
		return samples.e2();
	}

	protected final short e3() {
		return samples.e3();
	}

	protected final short e4() {
		return samples.e4();
	}

	protected ShortCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toShortArray());
	}

	protected ShortCollection getSampleElements(int howMany) {
		return new ShortArrayList(samples.asList().subList(0, howMany));
	}
}