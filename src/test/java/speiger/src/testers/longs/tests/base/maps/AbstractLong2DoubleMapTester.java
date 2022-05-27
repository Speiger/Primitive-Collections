package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2DoubleMapTester extends AbstractObjectContainerTester<Long2DoubleMap.Entry, Long2DoubleMap>
{
	protected TestLong2DoubleMapGenerator primitiveMapGenerator;

	protected Long2DoubleMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Double>, ? extends Map.Entry<Long, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2DoubleMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2DoubleMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2DoubleMap.Entry> getOrderedElements() {
		ObjectList<Long2DoubleMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2DoubleMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2DoubleMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2DoubleMap.Entry> actualContents() {
		return getMap().long2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2DoubleMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2DoubleMap resetContainer(Long2DoubleMap newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2DoubleMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2DoubleMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2DoubleMap.Entry... entries) {
		for (Long2DoubleMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2DoubleMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2DoubleMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getDoubleValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2DoubleMap.Entry newEntry) {
		ObjectList<Long2DoubleMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2DoubleMap.Entry> expected, Long2DoubleMap.Entry newEntry) {
		for (ObjectListIterator<Long2DoubleMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Long2DoubleMap.Entry entry(long key, double value) {
		return new AbstractLong2DoubleMap.BasicEntry(key, value);
	}
	
	protected long[] emptyKeyArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedKeyArray() {
		long[] array = new long[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Long[] emptyKeyObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedKeyObjectArray() {
		Long[] array = new Long[2];
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
	
	protected double get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}