package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2FloatMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2FloatMapTester<T> extends AbstractObjectContainerTester<Object2FloatMap.Entry<T>, Object2FloatMap<T>>
{
	protected TestObject2FloatMapGenerator<T> primitiveMapGenerator;

	protected Object2FloatMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Float>, ? extends Map.Entry<T, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2FloatMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2FloatMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2FloatMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2FloatMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2FloatMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2FloatMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2FloatMap.Entry<T>> actualContents() {
		return getMap().object2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2FloatMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2FloatMap<T> resetContainer(Object2FloatMap<T> newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2FloatMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2FloatMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2FloatMap.Entry<T>... entries) {
		for (Object2FloatMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2FloatMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2FloatMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getFloatValue(), getMap().getFloat(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2FloatMap.Entry<T> newEntry) {
		ObjectList<Object2FloatMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2FloatMap.Entry<T>> expected, Object2FloatMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2FloatMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Object2FloatMap.Entry<T> entry(T key, float value) {
		return new AbstractObject2FloatMap.BasicEntry<>(key, value);
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
	
	protected float get(T key) {
		return getMap().getFloat(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}