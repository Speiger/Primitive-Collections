package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2LongMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShort2LongMapTester extends AbstractObjectContainerTester<Short2LongMap.Entry, Short2LongMap>
{
	protected TestShort2LongMapGenerator primitiveMapGenerator;

	protected Short2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Long>, ? extends Map.Entry<Short, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2LongMap.Entry> getOrderedElements() {
		ObjectList<Short2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2LongMap.Entry> actualContents() {
		return getMap().short2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2LongMap resetContainer(Short2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2LongMap.Entry... entries) {
		for (Short2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getLongValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2LongMap.Entry newEntry) {
		ObjectList<Short2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2LongMap.Entry> expected, Short2LongMap.Entry newEntry) {
		for (ObjectListIterator<Short2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Short2LongMap.Entry entry(short key, long value) {
		return new AbstractShort2LongMap.BasicEntry(key, value);
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
	
	protected long get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}