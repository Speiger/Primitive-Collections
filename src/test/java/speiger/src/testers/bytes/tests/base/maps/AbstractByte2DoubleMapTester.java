package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractByte2DoubleMapTester extends AbstractObjectContainerTester<Byte2DoubleMap.Entry, Byte2DoubleMap> {

	protected TestByte2DoubleMapGenerator primitiveMapGenerator;

	protected Byte2DoubleMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Double>, ? extends Map.Entry<Byte, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2DoubleMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2DoubleMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2DoubleMap.Entry> getOrderedElements() {
		ObjectList<Byte2DoubleMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2DoubleMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2DoubleMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2DoubleMap.Entry> actualContents() {
		return getMap().byte2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2DoubleMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2DoubleMap resetContainer(Byte2DoubleMap newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2DoubleMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2DoubleMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2DoubleMap.Entry... entries) {
		for (Byte2DoubleMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2DoubleMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2DoubleMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getDoubleValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2DoubleMap.Entry newEntry) {
		ObjectList<Byte2DoubleMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2DoubleMap.Entry> expected, Byte2DoubleMap.Entry newEntry) {
		for (ObjectListIterator<Byte2DoubleMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Byte2DoubleMap.Entry entry(byte key, double value) {
		return new AbstractByte2DoubleMap.BasicEntry(key, value);
	}
	
	protected byte[] emptyKeyArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedKeyArray() {
		byte[] array = new byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Byte[] emptyKeyObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedKeyObjectArray() {
		Byte[] array = new Byte[2];
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
	
	protected double get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}