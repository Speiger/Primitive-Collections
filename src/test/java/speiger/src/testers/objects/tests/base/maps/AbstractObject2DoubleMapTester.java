package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2DoubleMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractObject2DoubleMapTester<T> extends AbstractObjectContainerTester<Object2DoubleMap.Entry<T>, Object2DoubleMap<T>> {

	protected TestObject2DoubleMapGenerator<T> primitiveMapGenerator;

	protected Object2DoubleMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Double>, ? extends Map.Entry<T, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2DoubleMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2DoubleMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2DoubleMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2DoubleMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2DoubleMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2DoubleMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2DoubleMap.Entry<T>> actualContents() {
		return getMap().object2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2DoubleMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2DoubleMap<T> resetContainer(Object2DoubleMap<T> newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Object2DoubleMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2DoubleMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2DoubleMap.Entry<T>... entries) {
		for (Object2DoubleMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2DoubleMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2DoubleMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getDoubleValue(), getMap().getDouble(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2DoubleMap.Entry<T> newEntry) {
		ObjectList<Object2DoubleMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2DoubleMap.Entry<T>> expected, Object2DoubleMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2DoubleMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Object2DoubleMap.Entry<T> entry(T key, double value) {
		return new AbstractObject2DoubleMap.BasicEntry<>(key, value);
	}
	
	protected T[] emptyKeyArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyArray() {
		T[] array = (T[])new Object[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected T[] emptyKeyObjectArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyObjectArray() {
		T[] array = (T[])new Object[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected double[] emptyValueArray() {
		return new double[0];
	}
	
	protected double[] createDisjointedValueArray() {
		double[] array = new double[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Double[] emptyValueObjectArray() {
		return new Double[0];
	}
	
	protected Double[] createDisjointedValueObjectArray() {
		Double[] array = new Double[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected double get(T key) {
		return getMap().getDouble(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}