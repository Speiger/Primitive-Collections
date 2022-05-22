package speiger.src.testers.floats.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.utils.FloatLists;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.testers.floats.utils.MinimalFloatCollection;

@Ignore
public abstract class AbstractFloatContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Float>, Float>> {
	protected FloatSamples samples;
	protected FloatCollection container;
	protected TestFloatCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Float>, Float> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloatCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestFloatCollectionGenerator");
		primitiveGenerator = (TestFloatCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract FloatCollection actualContents();

	protected FloatCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected FloatCollection resetContainer(FloatCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(float... elements) {
		expectContents(FloatArrayList.wrap(elements));
	}

	protected void expectContents(FloatCollection expected) {
		FloatHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(float... elements) {
		FloatList expected = FloatHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, float... elements) {
		expectAdded(index, FloatArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, FloatCollection elements) {
		FloatList expected = FloatHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(float... elements) {
		for (float element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected float[] createSamplesArray() {
		return getSampleElements().toFloatArray();
	}

	protected float[] createOrderedArray() {
		return getOrderedElements().toFloatArray();
	}

	public static class ArrayWithDuplicate {
		public final float[] elements;
		public final float duplicate;

		private ArrayWithDuplicate(float[] elements, float duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		float[] elements = createSamplesArray();
		float duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected FloatCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected FloatList getOrderedElements() {
		FloatList list = new FloatArrayList();
		for (FloatIterator iter = primitiveGenerator.order(new FloatArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextFloat());
		}
		return FloatLists.unmodifiable(list);
	}
	
	protected float[] createDisjointArray() {
		return new float[]{e3(), e4()};
	}
	
	protected float[] emptyArray() {
		return new float[0];
	}
	
	protected MinimalFloatCollection createDisjointCollection() {
		return MinimalFloatCollection.of(e3(), e4());
	}
	
	protected MinimalFloatCollection emptyCollection() {
		return MinimalFloatCollection.of();
	}

	protected final float e0() {
		return samples.e0();
	}

	protected final float e1() {
		return samples.e1();
	}

	protected final float e2() {
		return samples.e2();
	}

	protected final float e3() {
		return samples.e3();
	}

	protected final float e4() {
		return samples.e4();
	}

	protected FloatCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toFloatArray());
	}

	protected FloatCollection getSampleElements(int howMany) {
		return new FloatArrayList(samples.asList().subList(0, howMany));
	}
}