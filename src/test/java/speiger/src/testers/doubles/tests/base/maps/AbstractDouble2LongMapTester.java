package speiger.src.testers.doubles.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.doubles.generators.maps.TestDouble2LongMapGenerator;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractDouble2LongMapTester extends AbstractObjectContainerTester<Double2LongMap.Entry, Double2LongMap>
{
	protected TestDouble2LongMapGenerator primitiveMapGenerator;

	protected Double2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Double, Long>, ? extends Map.Entry<Double, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestDouble2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestDouble2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Double2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Double2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Double2LongMap.Entry> getOrderedElements() {
		ObjectList<Double2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Double2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Double2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Double2LongMap.Entry> actualContents() {
		return getMap().double2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Double2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Double2LongMap resetContainer(Double2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(long... elements) {
		for (long element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Double2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Double2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Double2LongMap.Entry... entries) {
		for (Double2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getDoubleKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getDoubleKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Double2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Double2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getDoubleKey(), entry.getLongValue(), getMap().get(entry.getDoubleKey()));
		}
	}

	protected final void expectReplacement(Double2LongMap.Entry newEntry) {
		ObjectList<Double2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Double2LongMap.Entry> expected, Double2LongMap.Entry newEntry) {
		for (ObjectListIterator<Double2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (DoubleHelpers.equals(i.next().getDoubleKey(), newEntry.getDoubleKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getDoubleKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Double2LongMap.Entry entry(double key, long value) {
		return new AbstractDouble2LongMap.BasicEntry(key, value);
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
	
	protected long[] emptyValueArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedValueArray() {
		long[] array = new long[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Long[] emptyValueObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedValueObjectArray() {
		Long[] array = new Long[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected long get(double key) {
		return getMap().get(key);
	}

	protected final double k0() {
		return e0().getDoubleKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final double k1() {
		return e1().getDoubleKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final double k2() {
		return e2().getDoubleKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final double k3() {
		return e3().getDoubleKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final double k4() {
		return e4().getDoubleKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}