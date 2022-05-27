package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestByte2LongMapGenerator extends TestMapGenerator<Byte, Long> {
	public ObjectSamples<Byte2LongMap.Entry> getSamples();
	public ObjectIterable<Byte2LongMap.Entry> order(ObjectList<Byte2LongMap.Entry> insertionOrder);
	public Byte2LongMap create(Byte2LongMap.Entry... elements);
	@Override
	default Byte2LongMap create(Object... elements) {
		Byte2LongMap.Entry[] result = new Byte2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Byte2LongMap.Entry[] createArray(int length) { return new Byte2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Long>> samples() {
		ObjectSamples<Byte2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getLongValue())
		);
	}
}