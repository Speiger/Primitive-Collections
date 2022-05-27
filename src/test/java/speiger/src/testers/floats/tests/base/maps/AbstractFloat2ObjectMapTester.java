package speiger.src.testers.floats.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectMapGenerator;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractFloat2ObjectMapTester<V> extends AbstractObjectContainerTester<Float2ObjectMap.Entry<V>, Float2ObjectMap<V>>
{
	protected TestFloat2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Float2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Float, V>, ? extends Map.Entry<Float, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestFloat2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestFloat2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Float2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Float2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Float2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Float2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Float2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Float2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Float2ObjectMap.Entry<V>> actualContents() {
		return getMap().float2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Float2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Float2ObjectMap<V> resetContainer(Float2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(float... elements) {
		for (float element : elements) {
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
		
	protected ObjectCollection<Float2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Float2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Float2ObjectMap.Entry<V>... entries) {
		for (Float2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getFloatKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getFloatKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Float2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Float2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getFloatKey(), entry.getValue(), getMap().get(entry.getFloatKey()));
		}
	}

	protected final void expectReplacement(Float2ObjectMap.Entry<V> newEntry) {
		ObjectList<Float2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Float2ObjectMap.Entry<V>> expected, Float2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Float2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (FloatHelpers.equals(i.next().getFloatKey(), newEntry.getFloatKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getFloatKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Float2ObjectMap.Entry<V> entry(float key, V value) {
		return new AbstractFloat2ObjectMap.BasicEntry<>(key, value);
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
	
	protected V get(float key) {
		return getMap().get(key);
	}

	protected final float k0() {
		return e0().getFloatKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final float k1() {
		return e1().getFloatKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final float k2() {
		return e2().getFloatKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final float k3() {
		return e3().getFloatKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final float k4() {
		return e4().getFloatKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}