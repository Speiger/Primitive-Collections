package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2IntMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractFloat2IntMapTester extends AbstractObjectContainerTester<Float2IntMap.Entry, Float2IntMap>
{
	protected TestFloat2IntMapGenerator primitiveMapGenerator;

	protected Float2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Integer>, ? extends Map.Entry<Float, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2IntMap.Entry> getOrderedElements() {
		ObjectList<Float2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2IntMap.Entry> actualContents() {
		return getMap().float2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2IntMap resetContainer(Float2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(int... elements) {
		for (int element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Float2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2IntMap.Entry... entries) {
		for (Float2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getIntValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2IntMap.Entry newEntry) {
		ObjectList<Float2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2IntMap.Entry> expected, Float2IntMap.Entry newEntry) {
		for (ObjectListIterator<Float2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Float2IntMap.Entry entry(float key, int value) {
		return new AbstractFloat2IntMap.BasicEntry(key, value);
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
	
	protected int[] emptyValueArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedValueArray() {
		int[] array = new int[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Integer[] emptyValueObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedValueObjectArray() {
		Integer[] array = new Integer[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected int get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}