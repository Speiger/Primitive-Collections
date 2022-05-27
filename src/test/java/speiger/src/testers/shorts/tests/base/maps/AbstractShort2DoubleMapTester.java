package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2DoubleMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractShort2DoubleMapTester extends AbstractObjectContainerTester<Short2DoubleMap.Entry, Short2DoubleMap> {

	protected TestShort2DoubleMapGenerator primitiveMapGenerator;

	protected Short2DoubleMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Double>, ? extends Map.Entry<Short, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2DoubleMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2DoubleMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2DoubleMap.Entry> getOrderedElements() {
		ObjectList<Short2DoubleMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2DoubleMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2DoubleMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2DoubleMap.Entry> actualContents() {
		return getMap().short2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2DoubleMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2DoubleMap resetContainer(Short2DoubleMap newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2DoubleMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2DoubleMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2DoubleMap.Entry... entries) {
		for (Short2DoubleMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2DoubleMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2DoubleMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getDoubleValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2DoubleMap.Entry newEntry) {
		ObjectList<Short2DoubleMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2DoubleMap.Entry> expected, Short2DoubleMap.Entry newEntry) {
		for (ObjectListIterator<Short2DoubleMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Short2DoubleMap.Entry entry(short key, double value) {
		return new AbstractShort2DoubleMap.BasicEntry(key, value);
	}
	
	protected short[] emptyKeyArray() {
		return new short[0];
	}
	
	protected short[] createDisjointedKeyArray() {
		short[] array = new short[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Short[] emptyKeyObjectArray() {
		return new Short[0];
	}
	
	protected Short[] createDisjointedKeyObjectArray() {
		Short[] array = new Short[2];
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
	
	protected double get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}