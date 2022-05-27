package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2ShortMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractFloat2ShortMapTester extends AbstractObjectContainerTester<Float2ShortMap.Entry, Float2ShortMap> {

	protected TestFloat2ShortMapGenerator primitiveMapGenerator;

	protected Float2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Short>, ? extends Map.Entry<Float, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2ShortMap.Entry> getOrderedElements() {
		ObjectList<Float2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2ShortMap.Entry> actualContents() {
		return getMap().float2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2ShortMap resetContainer(Float2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2ShortMap.Entry... entries) {
		for (Float2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getShortValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2ShortMap.Entry newEntry) {
		ObjectList<Float2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2ShortMap.Entry> expected, Float2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Float2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Float2ShortMap.Entry entry(float key, short value) {
		return new AbstractFloat2ShortMap.BasicEntry(key, value);
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
	
	protected short get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}