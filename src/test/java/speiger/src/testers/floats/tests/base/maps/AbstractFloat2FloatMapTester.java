package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2FloatMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractFloat2FloatMapTester extends AbstractObjectContainerTester<Float2FloatMap.Entry, Float2FloatMap>
{
	protected TestFloat2FloatMapGenerator primitiveMapGenerator;

	protected Float2FloatMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Float>, ? extends Map.Entry<Float, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2FloatMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2FloatMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2FloatMap.Entry> getOrderedElements() {
		ObjectList<Float2FloatMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2FloatMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2FloatMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2FloatMap.Entry> actualContents() {
		return getMap().float2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2FloatMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2FloatMap resetContainer(Float2FloatMap newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2FloatMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2FloatMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2FloatMap.Entry... entries) {
		for (Float2FloatMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2FloatMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2FloatMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getFloatValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2FloatMap.Entry newEntry) {
		ObjectList<Float2FloatMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2FloatMap.Entry> expected, Float2FloatMap.Entry newEntry) {
		for (ObjectListIterator<Float2FloatMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Float2FloatMap.Entry entry(float key, float value) {
		return new AbstractFloat2FloatMap.BasicEntry(key, value);
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
	
	protected float get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}