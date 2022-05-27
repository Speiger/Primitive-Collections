package speiger.src.testers.objects.tests.base;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.objects.utils.ObjectHelpers;
import speiger.src.testers.objects.utils.ObjectSamples;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public abstract class AbstractObjectContainerTester<T, E> extends AbstractTester<OneSizeTestContainerGenerator<E, T>>
{
	protected ObjectSamples<T> samples;
	protected E container;
	protected TestObjectCollectionGenerator<T> primitiveGenerator;
	protected CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		setupGenerator();
	}
	
	protected void setupGenerator() {
		TestContainerGenerator<E, T> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObjectCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestObjectCollectionGenerator");
		primitiveGenerator = (TestObjectCollectionGenerator<T>) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();		
	}

	protected abstract ObjectCollection<T> actualContents();

	protected E resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected E resetContainer(E newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(T... elements) {
		expectContents(ObjectArrayList.wrap(elements));
	}

	protected void expectContents(ObjectCollection<T> expected) {
		ObjectHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(T... elements) {
		ObjectList<T> expected = ObjectHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, T... elements) {
		expectAdded(index, ObjectArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, ObjectCollection<T> elements) {
		ObjectList<T> expected = ObjectHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(T... elements) {
		for (T element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected T[] createSamplesArray() {
		return getSampleElements().toArray((T[])new Object[getNumElements()]);
	}

	protected T[] createOrderedArray() {
		return getOrderedElements().toArray((T[])new Object[getNumElements()]);
	}

	public static class ArrayWithDuplicate<T> {
		public final T[] elements;
		public final T duplicate;

		private ArrayWithDuplicate(T[] elements, T duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate<T> createArrayWithDuplicateElement() {
		T[] elements = createSamplesArray();
		T duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate<>(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected ObjectCollection<T> getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected ObjectList<T> getOrderedElements() {
		ObjectList<T> list = new ObjectArrayList<>();
		for (ObjectIterator<T> iter = primitiveGenerator.order(new ObjectArrayList<>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	protected T[] createDisjointArray() {
		T[] array = (T[])new Object[2];
		array[0] = e3();
		array[1] = e4();
		return array;
	}
	protected T[] emptyArray() {
		return (T[])new Object[0];
	}
	
	protected MinimalObjectCollection<T> createDisjointCollection() {
		return MinimalObjectCollection.of(e3(), e4());
	}
	
	protected MinimalObjectCollection<T> emptyCollection() {
		return MinimalObjectCollection.of();
	}
	
	public T[] createArray(T...array) {
		return array;
	}
	
	protected final T e0() {
		return samples.e0();
	}

	protected final T e1() {
		return samples.e1();
	}

	protected final T e2() {
		return samples.e2();
	}

	protected final T e3() {
		return samples.e3();
	}

	protected final T e4() {
		return samples.e4();
	}

	protected E createTestSubject() {
		return (E)primitiveGenerator.create(getSampleElements(size.getNumElements()).toArray((T[])new Object[getNumElements()]));
	}

	protected ObjectCollection<T> getSampleElements(int howMany) {
		return new ObjectArrayList<>(samples.asList().subList(0, howMany));
	}
}