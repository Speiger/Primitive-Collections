package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2LongMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractFloat2LongMapTester extends AbstractObjectContainerTester<Float2LongMap.Entry, Float2LongMap> {

	protected TestFloat2LongMapGenerator primitiveMapGenerator;

	protected Float2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, Long>, ? extends Map.Entry<Float, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2LongMap.Entry> getOrderedElements() {
		ObjectList<Float2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2LongMap.Entry> actualContents() {
		return getMap().float2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2LongMap resetContainer(Float2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2LongMap.Entry... entries) {
		for (Float2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Float2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getLongValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2LongMap.Entry newEntry) {
		ObjectList<Float2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2LongMap.Entry> expected, Float2LongMap.Entry newEntry) {
		for (ObjectListIterator<Float2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Float2LongMap.Entry entry(float key, long value) {
		return new AbstractFloat2LongMap.BasicEntry(key, value);
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
	
	protected long get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}