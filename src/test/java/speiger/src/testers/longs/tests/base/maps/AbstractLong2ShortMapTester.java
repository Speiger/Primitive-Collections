package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2ShortMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2ShortMapTester extends AbstractObjectContainerTester<Long2ShortMap.Entry, Long2ShortMap>
{
	protected TestLong2ShortMapGenerator primitiveMapGenerator;

	protected Long2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Short>, ? extends Map.Entry<Long, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2ShortMap.Entry> getOrderedElements() {
		ObjectList<Long2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2ShortMap.Entry> actualContents() {
		return getMap().long2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2ShortMap resetContainer(Long2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(short... elements) {
		for (short element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Long2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2ShortMap.Entry... entries) {
		for (Long2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getShortValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2ShortMap.Entry newEntry) {
		ObjectList<Long2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2ShortMap.Entry> expected, Long2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Long2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Long2ShortMap.Entry entry(long key, short value) {
		return new AbstractLong2ShortMap.BasicEntry(key, value);
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
	
	protected short[] emptyValueArray() {
		return new short[0];
	}
	
	protected short[] createDisjointedValueArray() {
		short[] array = new short[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Short[] emptyValueObjectArray() {
		return new Short[0];
	}
	
	protected Short[] createDisjointedValueObjectArray() {
		Short[] array = new Short[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected short get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}