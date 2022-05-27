package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestByte2DoubleMapGenerator extends TestMapGenerator<Byte, Double> {
	public ObjectSamples<Byte2DoubleMap.Entry> getSamples();
	public ObjectIterable<Byte2DoubleMap.Entry> order(ObjectList<Byte2DoubleMap.Entry> insertionOrder);
	public Byte2DoubleMap create(Byte2DoubleMap.Entry... elements);
	@Override
	default Byte2DoubleMap create(Object... elements) {
		Byte2DoubleMap.Entry[] result = new Byte2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Byte2DoubleMap.Entry[] createArray(int length) { return new Byte2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Double>> samples() {
		ObjectSamples<Byte2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getDoubleValue())
		);
	}
}