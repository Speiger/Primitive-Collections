package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2BooleanMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShort2BooleanMapTester extends AbstractObjectContainerTester<Short2BooleanMap.Entry, Short2BooleanMap>
{
	protected TestShort2BooleanMapGenerator primitiveMapGenerator;

	protected Short2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Boolean>, ? extends Map.Entry<Short, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Short2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2BooleanMap.Entry> actualContents() {
		return getMap().short2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2BooleanMap resetContainer(Short2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(boolean... elements) {
		for (boolean element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Short2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2BooleanMap.Entry... entries) {
		for (Short2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getBooleanValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2BooleanMap.Entry newEntry) {
		ObjectList<Short2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2BooleanMap.Entry> expected, Short2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Short2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Short2BooleanMap.Entry entry(short key, boolean value) {
		return new AbstractShort2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean[] emptyValueArray() {
		return new boolean[0];
	}
	
	protected boolean[] createDisjointedValueArray() {
		boolean[] array = new boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Boolean[] emptyValueObjectArray() {
		return new Boolean[0];
	}
	
	protected Boolean[] createDisjointedValueObjectArray() {
		Boolean[] array = new Boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected boolean get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}