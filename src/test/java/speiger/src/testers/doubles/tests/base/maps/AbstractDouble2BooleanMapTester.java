package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2BooleanMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractDouble2BooleanMapTester extends AbstractObjectContainerTester<Double2BooleanMap.Entry, Double2BooleanMap> {

	protected TestDouble2BooleanMapGenerator primitiveMapGenerator;

	protected Double2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, Boolean>, ? extends Map.Entry<Double, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Double2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2BooleanMap.Entry> actualContents() {
		return getMap().double2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2BooleanMap resetContainer(Double2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(boolean... elements) {
		for (boolean element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2BooleanMap.Entry... entries) {
		for (Double2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Double2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getBooleanValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2BooleanMap.Entry newEntry) {
		ObjectList<Double2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2BooleanMap.Entry> expected, Double2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Double2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Double2BooleanMap.Entry entry(double key, boolean value) {
		return new AbstractDouble2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean[] emptyValueArray() {
		return new boolean[0];
	}
	
	protected boolean[] createDisjointedValueArray() {
		boolean[] array = new boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Boolean[] emptyValueObjectArray() {
		return new Boolean[0];
	}
	
	protected Boolean[] createDisjointedValueObjectArray() {
		Boolean[] array = new Boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected boolean get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}