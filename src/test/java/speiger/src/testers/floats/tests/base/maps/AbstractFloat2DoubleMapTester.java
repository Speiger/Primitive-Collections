package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractFloat2DoubleMapTester extends AbstractObjectContainerTester<Float2DoubleMap.Entry, Float2DoubleMap>
{
	protected TestFloat2DoubleMapGenerator primitiveMapGenerator;

	protected Float2DoubleMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Double>, ? extends Map.Entry<Float, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2DoubleMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2DoubleMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2DoubleMap.Entry> getOrderedElements() {
		ObjectList<Float2DoubleMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2DoubleMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2DoubleMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2DoubleMap.Entry> actualContents() {
		return getMap().float2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2DoubleMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2DoubleMap resetContainer(Float2DoubleMap newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2DoubleMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2DoubleMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2DoubleMap.Entry... entries) {
		for (Float2DoubleMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2DoubleMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2DoubleMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getDoubleValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2DoubleMap.Entry newEntry) {
		ObjectList<Float2DoubleMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2DoubleMap.Entry> expected, Float2DoubleMap.Entry newEntry) {
		for (ObjectListIterator<Float2DoubleMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Float2DoubleMap.Entry entry(float key, double value) {
		return new AbstractFloat2DoubleMap.BasicEntry(key, value);
	}
	
	protected float[] emptyKeyArray() {
		return new float[0];
	}
	
	protected float[] createDisjointedKeyArray() {
		float[] array = new float[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Float[] emptyKeyObjectArray() {
		return new Float[0];
	}
	
	protected Float[] createDisjointedKeyObjectArray() {
		Float[] array = new Float[2];
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
	
	protected double get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}