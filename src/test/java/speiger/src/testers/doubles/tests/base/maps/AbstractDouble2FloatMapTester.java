package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2FloatMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractDouble2FloatMapTester extends AbstractObjectContainerTester<Double2FloatMap.Entry, Double2FloatMap> {

	protected TestDouble2FloatMapGenerator primitiveMapGenerator;

	protected Double2FloatMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, Float>, ? extends Map.Entry<Double, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2FloatMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2FloatMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2FloatMap.Entry> getOrderedElements() {
		ObjectList<Double2FloatMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2FloatMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2FloatMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2FloatMap.Entry> actualContents() {
		return getMap().double2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2FloatMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2FloatMap resetContainer(Double2FloatMap newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(float... elements) {
		for (float element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2FloatMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2FloatMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2FloatMap.Entry... entries) {
		for (Double2FloatMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2FloatMap.Entry> expected) {
		super.expectContents(expected);
		for (Double2FloatMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getFloatValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2FloatMap.Entry newEntry) {
		ObjectList<Double2FloatMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2FloatMap.Entry> expected, Double2FloatMap.Entry newEntry) {
		for (ObjectListIterator<Double2FloatMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Double2FloatMap.Entry entry(double key, float value) {
		return new AbstractDouble2FloatMap.BasicEntry(key, value);
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
	
	protected float[] emptyValueArray() {
		return new float[0];
	}
	
	protected float[] createDisjointedValueArray() {
		float[] array = new float[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Float[] emptyValueObjectArray() {
		return new Float[0];
	}
	
	protected Float[] createDisjointedValueObjectArray() {
		Float[] array = new Float[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected float get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}