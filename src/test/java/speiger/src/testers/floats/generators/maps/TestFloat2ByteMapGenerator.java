package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestFloat2ByteMapGenerator extends TestMapGenerator<Float, Byte> {
	public ObjectSamples<Float2ByteMap.Entry> getSamples();
	public ObjectIterable<Float2ByteMap.Entry> order(ObjectList<Float2ByteMap.Entry> insertionOrder);
	public Float2ByteMap create(Float2ByteMap.Entry... elements);
	@Override
	default Float2ByteMap create(Object... elements) {
		Float2ByteMap.Entry[] result = new Float2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Float2ByteMap.Entry[] createArray(int length) { return new Float2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Byte>> samples() {
		ObjectSamples<Float2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getByteValue())
		);
	}
}