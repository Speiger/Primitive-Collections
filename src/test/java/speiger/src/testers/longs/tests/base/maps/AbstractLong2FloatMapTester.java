package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2FloatMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractLong2FloatMapTester extends AbstractObjectContainerTester<Long2FloatMap.Entry, Long2FloatMap> {

	protected TestLong2FloatMapGenerator primitiveMapGenerator;

	protected Long2FloatMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Float>, ? extends Map.Entry<Long, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2FloatMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2FloatMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2FloatMap.Entry> getOrderedElements() {
		ObjectList<Long2FloatMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2FloatMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2FloatMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2FloatMap.Entry> actualContents() {
		return getMap().long2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2FloatMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2FloatMap resetContainer(Long2FloatMap newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2FloatMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2FloatMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2FloatMap.Entry... entries) {
		for (Long2FloatMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2FloatMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2FloatMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getFloatValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2FloatMap.Entry newEntry) {
		ObjectList<Long2FloatMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2FloatMap.Entry> expected, Long2FloatMap.Entry newEntry) {
		for (ObjectListIterator<Long2FloatMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Long2FloatMap.Entry entry(long key, float value) {
		return new AbstractLong2FloatMap.BasicEntry(key, value);
	}
	
	protected long[] emptyKeyArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedKeyArray() {
		long[] array = new long[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Long[] emptyKeyObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedKeyObjectArray() {
		Long[] array = new Long[2];
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
	
	protected float get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}