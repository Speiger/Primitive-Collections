package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractDouble2IntMapTester extends AbstractObjectContainerTester<Double2IntMap.Entry, Double2IntMap> {

	protected TestDouble2IntMapGenerator primitiveMapGenerator;

	protected Double2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, Integer>, ? extends Map.Entry<Double, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2IntMap.Entry> getOrderedElements() {
		ObjectList<Double2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2IntMap.Entry> actualContents() {
		return getMap().double2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2IntMap resetContainer(Double2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(int... elements) {
		for (int element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2IntMap.Entry... entries) {
		for (Double2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Double2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getIntValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2IntMap.Entry newEntry) {
		ObjectList<Double2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2IntMap.Entry> expected, Double2IntMap.Entry newEntry) {
		for (ObjectListIterator<Double2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Double2IntMap.Entry entry(double key, int value) {
		return new AbstractDouble2IntMap.BasicEntry(key, value);
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
	
	protected int[] emptyValueArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedValueArray() {
		int[] array = new int[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Integer[] emptyValueObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedValueObjectArray() {
		Integer[] array = new Integer[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected int get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}