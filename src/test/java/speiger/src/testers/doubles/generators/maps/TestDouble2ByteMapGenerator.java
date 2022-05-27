package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2ByteMapGenerator extends TestMapGenerator<Double, Byte> {
	public ObjectSamples<Double2ByteMap.Entry> getSamples();
	public ObjectIterable<Double2ByteMap.Entry> order(ObjectList<Double2ByteMap.Entry> insertionOrder);
	public Double2ByteMap create(Double2ByteMap.Entry... elements);
	@Override
	default Double2ByteMap create(Object... elements) {
		Double2ByteMap.Entry[] result = new Double2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Double2ByteMap.Entry[] createArray(int length) { return new Double2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Byte>> samples() {
		ObjectSamples<Double2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getByteValue())
		);
	}
}