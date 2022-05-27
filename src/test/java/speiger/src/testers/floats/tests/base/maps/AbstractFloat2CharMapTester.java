package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2CharMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractFloat2CharMapTester extends AbstractObjectContainerTester<Float2CharMap.Entry, Float2CharMap> {

	protected TestFloat2CharMapGenerator primitiveMapGenerator;

	protected Float2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Character>, ? extends Map.Entry<Float, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2CharMap.Entry> getOrderedElements() {
		ObjectList<Float2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2CharMap.Entry> actualContents() {
		return getMap().float2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2CharMap resetContainer(Float2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2CharMap.Entry... entries) {
		for (Float2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getCharValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2CharMap.Entry newEntry) {
		ObjectList<Float2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2CharMap.Entry> expected, Float2CharMap.Entry newEntry) {
		for (ObjectListIterator<Float2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Float2CharMap.Entry entry(float key, char value) {
		return new AbstractFloat2CharMap.BasicEntry(key, value);
	}
	
	protected float[] emptyKeyArray() {
		return new float[0];
	}
	
	protected float[] createDisjointedKeyArray() {
		float[] array = new float[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Float[] emptyKeyObjectArray() {
		return new Float[0];
	}
	
	protected Float[] createDisjointedKeyObjectArray() {
		Float[] array = new Float[2];
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
	
	protected char get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}