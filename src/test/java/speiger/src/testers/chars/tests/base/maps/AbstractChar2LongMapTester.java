package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2LongMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractChar2LongMapTester extends AbstractObjectContainerTester<Char2LongMap.Entry, Char2LongMap>
{
	protected TestChar2LongMapGenerator primitiveMapGenerator;

	protected Char2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Long>, ? extends Map.Entry<Character, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2LongMap.Entry> getOrderedElements() {
		ObjectList<Char2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2LongMap.Entry> actualContents() {
		return getMap().char2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2LongMap resetContainer(Char2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
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
		
	protected ObjectCollection<Char2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2LongMap.Entry... entries) {
		for (Char2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getLongValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2LongMap.Entry newEntry) {
		ObjectList<Char2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2LongMap.Entry> expected, Char2LongMap.Entry newEntry) {
		for (ObjectListIterator<Char2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Char2LongMap.Entry entry(char key, long value) {
		return new AbstractChar2LongMap.BasicEntry(key, value);
	}
	
	protected char[] emptyKeyArray() {
		return new char[0];
	}
	
	protected char[] createDisjointedKeyArray() {
		char[] array = new char[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Character[] emptyKeyObjectArray() {
		return new Character[0];
	}
	
	protected Character[] createDisjointedKeyObjectArray() {
		Character[] array = new Character[2];
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
	
	protected long get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}