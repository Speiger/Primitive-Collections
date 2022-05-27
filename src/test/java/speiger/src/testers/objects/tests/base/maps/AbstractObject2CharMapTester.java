package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2CharMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractObject2CharMapTester<T> extends AbstractObjectContainerTester<Object2CharMap.Entry<T>, Object2CharMap<T>> {

	protected TestObject2CharMapGenerator<T> primitiveMapGenerator;

	protected Object2CharMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Character>, ? extends Map.Entry<T, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2CharMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2CharMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2CharMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2CharMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2CharMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2CharMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2CharMap.Entry<T>> actualContents() {
		return getMap().object2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2CharMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2CharMap<T> resetContainer(Object2CharMap<T> newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2CharMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2CharMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2CharMap.Entry<T>... entries) {
		for (Object2CharMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2CharMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2CharMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getCharValue(), getMap().getChar(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2CharMap.Entry<T> newEntry) {
		ObjectList<Object2CharMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2CharMap.Entry<T>> expected, Object2CharMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2CharMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Object2CharMap.Entry<T> entry(T key, char value) {
		return new AbstractObject2CharMap.BasicEntry<>(key, value);
	}
	
	protected T[] emptyKeyArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyArray() {
		T[] array = (T[])new Object[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected T[] emptyKeyObjectArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyObjectArray() {
		T[] array = (T[])new Object[2];
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
	
	protected char get(T key) {
		return getMap().getChar(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}