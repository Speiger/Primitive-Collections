package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2ByteMapGenerator extends TestMapGenerator<Integer, Byte> {
	public ObjectSamples<Int2ByteMap.Entry> getSamples();
	public ObjectIterable<Int2ByteMap.Entry> order(ObjectList<Int2ByteMap.Entry> insertionOrder);
	public Int2ByteMap create(Int2ByteMap.Entry... elements);
	@Override
	default Int2ByteMap create(Object... elements) {
		Int2ByteMap.Entry[] result = new Int2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Int2ByteMap.Entry[] createArray(int length) { return new Int2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Byte>> samples() {
		ObjectSamples<Int2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getByteValue())
		);
	}
}