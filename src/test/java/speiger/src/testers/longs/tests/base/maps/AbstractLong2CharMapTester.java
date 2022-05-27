package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2CharMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2CharMapTester extends AbstractObjectContainerTester<Long2CharMap.Entry, Long2CharMap>
{
	protected TestLong2CharMapGenerator primitiveMapGenerator;

	protected Long2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Character>, ? extends Map.Entry<Long, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2CharMap.Entry> getOrderedElements() {
		ObjectList<Long2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2CharMap.Entry> actualContents() {
		return getMap().long2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2CharMap resetContainer(Long2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Long2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2CharMap.Entry... entries) {
		for (Long2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getCharValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2CharMap.Entry newEntry) {
		ObjectList<Long2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2CharMap.Entry> expected, Long2CharMap.Entry newEntry) {
		for (ObjectListIterator<Long2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Long2CharMap.Entry entry(long key, char value) {
		return new AbstractLong2CharMap.BasicEntry(key, value);
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
	
	protected char[] emptyValueArray() {
		return new char[0];
	}
	
	protected char[] createDisjointedValueArray() {
		char[] array = new char[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Character[] emptyValueObjectArray() {
		return new Character[0];
	}
	
	protected Character[] createDisjointedValueObjectArray() {
		Character[] array = new Character[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected char get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}