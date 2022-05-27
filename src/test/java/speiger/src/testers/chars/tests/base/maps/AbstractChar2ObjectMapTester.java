package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractChar2ObjectMapTester<V> extends AbstractObjectContainerTester<Char2ObjectMap.Entry<V>, Char2ObjectMap<V>> {

	protected TestChar2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Char2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, V>, ? extends Map.Entry<Character, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Char2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2ObjectMap.Entry<V>> actualContents() {
		return getMap().char2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2ObjectMap<V> resetContainer(Char2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(V... elements) {
		for (V element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2ObjectMap.Entry<V>... entries) {
		for (Char2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Char2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2ObjectMap.Entry<V> newEntry) {
		ObjectList<Char2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2ObjectMap.Entry<V>> expected, Char2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Char2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Char2ObjectMap.Entry<V> entry(char key, V value) {
		return new AbstractChar2ObjectMap.BasicEntry<>(key, value);
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
	
	protected V[] emptyValueArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V[] emptyValueObjectArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueObjectArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}