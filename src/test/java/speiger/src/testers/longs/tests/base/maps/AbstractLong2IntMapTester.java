package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2IntMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractLong2IntMapTester extends AbstractObjectContainerTester<Long2IntMap.Entry, Long2IntMap> {

	protected TestLong2IntMapGenerator primitiveMapGenerator;

	protected Long2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Integer>, ? extends Map.Entry<Long, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2IntMap.Entry> getOrderedElements() {
		ObjectList<Long2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2IntMap.Entry> actualContents() {
		return getMap().long2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2IntMap resetContainer(Long2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2IntMap.Entry... entries) {
		for (Long2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getIntValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2IntMap.Entry newEntry) {
		ObjectList<Long2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2IntMap.Entry> expected, Long2IntMap.Entry newEntry) {
		for (ObjectListIterator<Long2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Long2IntMap.Entry entry(long key, int value) {
		return new AbstractLong2IntMap.BasicEntry(key, value);
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
	
	protected int get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}