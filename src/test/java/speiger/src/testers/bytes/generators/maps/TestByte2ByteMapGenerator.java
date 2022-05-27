package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestByte2ByteMapGenerator extends TestMapGenerator<Byte, Byte> {
	public ObjectSamples<Byte2ByteMap.Entry> getSamples();
	public ObjectIterable<Byte2ByteMap.Entry> order(ObjectList<Byte2ByteMap.Entry> insertionOrder);
	public Byte2ByteMap create(Byte2ByteMap.Entry... elements);
	@Override
	default Byte2ByteMap create(Object... elements) {
		Byte2ByteMap.Entry[] result = new Byte2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Byte2ByteMap.Entry[] createArray(int length) { return new Byte2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Byte>> samples() {
		ObjectSamples<Byte2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getByteValue())
		);
	}
}