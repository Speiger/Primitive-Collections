package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2LongMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractInt2LongMapTester extends AbstractObjectContainerTester<Int2LongMap.Entry, Int2LongMap>
{
	protected TestInt2LongMapGenerator primitiveMapGenerator;

	protected Int2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Long>, ? extends Map.Entry<Integer, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2LongMap.Entry> getOrderedElements() {
		ObjectList<Int2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2LongMap.Entry> actualContents() {
		return getMap().int2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2LongMap resetContainer(Int2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2LongMap.Entry... entries) {
		for (Int2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getLongValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2LongMap.Entry newEntry) {
		ObjectList<Int2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2LongMap.Entry> expected, Int2LongMap.Entry newEntry) {
		for (ObjectListIterator<Int2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Int2LongMap.Entry entry(int key, long value) {
		return new AbstractInt2LongMap.BasicEntry(key, value);
	}
	
	protected int[] emptyKeyArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedKeyArray() {
		int[] array = new int[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Integer[] emptyKeyObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedKeyObjectArray() {
		Integer[] array = new Integer[2];
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
	
	protected long get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}