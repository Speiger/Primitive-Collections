package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractDouble2ObjectMapTester<V> extends AbstractObjectContainerTester<Double2ObjectMap.Entry<V>, Double2ObjectMap<V>> {

	protected TestDouble2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Double2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, V>, ? extends Map.Entry<Double, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Double2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2ObjectMap.Entry<V>> actualContents() {
		return getMap().double2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2ObjectMap<V> resetContainer(Double2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(V... elements) {
		for (V element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2ObjectMap.Entry<V>... entries) {
		for (Double2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Double2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2ObjectMap.Entry<V> newEntry) {
		ObjectList<Double2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2ObjectMap.Entry<V>> expected, Double2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Double2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Double2ObjectMap.Entry<V> entry(double key, V value) {
		return new AbstractDouble2ObjectMap.BasicEntry<>(key, value);
	}
	
	protected double[] emptyKeyArray() {
		return new double[0];
	}
	
	protected double[] createDisjointedKeyArray() {
		double[] array = new double[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Double[] emptyKeyObjectArray() {
		return new Double[0];
	}
	
	protected Double[] createDisjointedKeyObjectArray() {
		Double[] array = new Double[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected V[] emptyValueArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V[] emptyValueObjectArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueObjectArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}