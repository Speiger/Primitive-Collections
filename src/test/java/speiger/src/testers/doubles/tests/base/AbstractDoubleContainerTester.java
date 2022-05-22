package speiger.src.testers.doubles.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.utils.DoubleLists;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.testers.doubles.utils.MinimalDoubleCollection;

public abstract class AbstractDoubleContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Double>, Double>> {
	protected DoubleSamples samples;
	protected DoubleCollection container;
	protected TestDoubleCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Double>, Double> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDoubleCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestDoubleCollectionGenerator");
		primitiveGenerator = (TestDoubleCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract DoubleCollection actualContents();

	protected DoubleCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected DoubleCollection resetContainer(DoubleCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(double... elements) {
		expectContents(DoubleArrayList.wrap(elements));
	}

	protected void expectContents(DoubleCollection expected) {
		DoubleHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(double... elements) {
		DoubleList expected = DoubleHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, double... elements) {
		expectAdded(index, DoubleArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, DoubleCollection elements) {
		DoubleList expected = DoubleHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected double[] createSamplesArray() {
		return getSampleElements().toDoubleArray();
	}

	protected double[] createOrderedArray() {
		return getOrderedElements().toDoubleArray();
	}

	public static class ArrayWithDuplicate {
		public final double[] elements;
		public final double duplicate;

		private ArrayWithDuplicate(double[] elements, double duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		double[] elements = createSamplesArray();
		double duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected DoubleCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected DoubleList getOrderedElements() {
		DoubleList list = new DoubleArrayList();
		for (DoubleIterator iter = primitiveGenerator.order(new DoubleArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextDouble());
		}
		return DoubleLists.unmodifiable(list);
	}
	
	protected double[] createDisjointArray() {
		return new double[]{e3(), e4()};
	}
	
	protected double[] emptyArray() {
		return new double[0];
	}
	
	protected MinimalDoubleCollection createDisjointCollection() {
		return MinimalDoubleCollection.of(e3(), e4());
	}
	
	protected MinimalDoubleCollection emptyCollection() {
		return MinimalDoubleCollection.of();
	}

	protected final double e0() {
		return samples.e0();
	}

	protected final double e1() {
		return samples.e1();
	}

	protected final double e2() {
		return samples.e2();
	}

	protected final double e3() {
		return samples.e3();
	}

	protected final double e4() {
		return samples.e4();
	}

	protected DoubleCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toDoubleArray());
	}

	protected DoubleCollection getSampleElements(int howMany) {
		return new DoubleArrayList(samples.asList().subList(0, howMany));
	}
}