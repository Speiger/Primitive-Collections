package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractDouble2ByteMapTester extends AbstractObjectContainerTester<Double2ByteMap.Entry, Double2ByteMap>
{
	protected TestDouble2ByteMapGenerator primitiveMapGenerator;

	protected Double2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, Byte>, ? extends Map.Entry<Double, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2ByteMap.Entry> getOrderedElements() {
		ObjectList<Double2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2ByteMap.Entry> actualContents() {
		return getMap().double2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2ByteMap resetContainer(Double2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2ByteMap.Entry... entries) {
		for (Double2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Double2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getByteValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2ByteMap.Entry newEntry) {
		ObjectList<Double2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2ByteMap.Entry> expected, Double2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Double2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Double2ByteMap.Entry entry(double key, byte value) {
		return new AbstractDouble2ByteMap.BasicEntry(key, value);
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
	
	protected byte[] emptyValueArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedValueArray() {
		byte[] array = new byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Byte[] emptyValueObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedValueObjectArray() {
		Byte[] array = new Byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected byte get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}