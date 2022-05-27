package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2FloatMapTester extends AbstractObjectContainerTester<Byte2FloatMap.Entry, Byte2FloatMap>
{
	protected TestByte2FloatMapGenerator primitiveMapGenerator;

	protected Byte2FloatMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Float>, ? extends Map.Entry<Byte, Float>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2FloatMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2FloatMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2FloatMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2FloatMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2FloatMap.Entry> getOrderedElements() {
		ObjectList<Byte2FloatMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2FloatMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2FloatMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2FloatMap.Entry> actualContents() {
		return getMap().byte2FloatEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2FloatMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2FloatMap resetContainer(Byte2FloatMap newValue) {
		newValue.setDefaultReturnValue(-1F);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2FloatMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2FloatMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2FloatMap.Entry... entries) {
		for (Byte2FloatMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getFloatValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getFloatValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2FloatMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2FloatMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getFloatValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2FloatMap.Entry newEntry) {
		ObjectList<Byte2FloatMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2FloatMap.Entry> expected, Byte2FloatMap.Entry newEntry) {
		for (ObjectListIterator<Byte2FloatMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(float a, float b) {
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}
	
	protected Byte2FloatMap.Entry entry(byte key, float value) {
		return new AbstractByte2FloatMap.BasicEntry(key, value);
	}
	
	protected byte[] emptyKeyArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedKeyArray() {
		byte[] array = new byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Byte[] emptyKeyObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedKeyObjectArray() {
		Byte[] array = new Byte[2];
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
	
	protected float get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final float v0() {
		return e0().getFloatValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final float v1() {
		return e1().getFloatValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final float v2() {
		return e2().getFloatValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final float v3() {
		return e3().getFloatValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final float v4() {
		return e4().getFloatValue();
	}
}