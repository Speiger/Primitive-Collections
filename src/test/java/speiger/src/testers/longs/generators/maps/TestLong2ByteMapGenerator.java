package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestLong2ByteMapGenerator extends TestMapGenerator<Long, Byte> {
	public ObjectSamples<Long2ByteMap.Entry> getSamples();
	public ObjectIterable<Long2ByteMap.Entry> order(ObjectList<Long2ByteMap.Entry> insertionOrder);
	public Long2ByteMap create(Long2ByteMap.Entry... elements);
	@Override
	default Long2ByteMap create(Object... elements) {
		Long2ByteMap.Entry[] result = new Long2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Long2ByteMap.Entry[] createArray(int length) { return new Long2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Byte>> samples() {
		ObjectSamples<Long2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getByteValue())
		);
	}
}