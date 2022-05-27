package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2LongMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2LongMapTester extends AbstractObjectContainerTester<Long2LongMap.Entry, Long2LongMap>
{
	protected TestLong2LongMapGenerator primitiveMapGenerator;

	protected Long2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Long>, ? extends Map.Entry<Long, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2LongMap.Entry> getOrderedElements() {
		ObjectList<Long2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2LongMap.Entry> actualContents() {
		return getMap().long2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2LongMap resetContainer(Long2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2LongMap.Entry... entries) {
		for (Long2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getLongValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2LongMap.Entry newEntry) {
		ObjectList<Long2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2LongMap.Entry> expected, Long2LongMap.Entry newEntry) {
		for (ObjectListIterator<Long2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Long2LongMap.Entry entry(long key, long value) {
		return new AbstractLong2LongMap.BasicEntry(key, value);
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
	
	protected long get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}