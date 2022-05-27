package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2ShortMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractShort2ShortMapTester extends AbstractObjectContainerTester<Short2ShortMap.Entry, Short2ShortMap> {

	protected TestShort2ShortMapGenerator primitiveMapGenerator;

	protected Short2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Short>, ? extends Map.Entry<Short, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2ShortMap.Entry> getOrderedElements() {
		ObjectList<Short2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2ShortMap.Entry> actualContents() {
		return getMap().short2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2ShortMap resetContainer(Short2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2ShortMap.Entry... entries) {
		for (Short2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getShortValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2ShortMap.Entry newEntry) {
		ObjectList<Short2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2ShortMap.Entry> expected, Short2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Short2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Short2ShortMap.Entry entry(short key, short value) {
		return new AbstractShort2ShortMap.BasicEntry(key, value);
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
	
	protected short get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}