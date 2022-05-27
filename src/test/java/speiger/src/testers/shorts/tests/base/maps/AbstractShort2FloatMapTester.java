package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2FloatMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractShort2FloatMapTester extends AbstractObjectContainerTester<Short2FloatMap.Entry, Short2FloatMap> {

	protected TestShort2FloatMapGenerator primitiveMapGenerator;

	protected Short2FloatMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Float>, ? extends Map.Entry<Short, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2FloatMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2FloatMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2FloatMap.Entry> getOrderedElements() {
		ObjectList<Short2FloatMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2FloatMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2FloatMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2FloatMap.Entry> actualContents() {
		return getMap().short2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2FloatMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2FloatMap resetContainer(Short2FloatMap newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(float... elements) {
		for (float element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Short2FloatMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2FloatMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2FloatMap.Entry... entries) {
		for (Short2FloatMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2FloatMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2FloatMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getFloatValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2FloatMap.Entry newEntry) {
		ObjectList<Short2FloatMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2FloatMap.Entry> expected, Short2FloatMap.Entry newEntry) {
		for (ObjectListIterator<Short2FloatMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Short2FloatMap.Entry entry(short key, float value) {
		return new AbstractShort2FloatMap.BasicEntry(key, value);
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
	
	protected float[] emptyValueArray() {
		return new float[0];
	}
	
	protected float[] createDisjointedValueArray() {
		float[] array = new float[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Float[] emptyValueObjectArray() {
		return new Float[0];
	}
	
	protected Float[] createDisjointedValueObjectArray() {
		Float[] array = new Float[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected float get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}