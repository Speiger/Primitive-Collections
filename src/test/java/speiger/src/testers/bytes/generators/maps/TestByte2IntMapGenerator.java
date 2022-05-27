package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestByte2IntMapGenerator extends TestMapGenerator<Byte, Integer> {
	public ObjectSamples<Byte2IntMap.Entry> getSamples();
	public ObjectIterable<Byte2IntMap.Entry> order(ObjectList<Byte2IntMap.Entry> insertionOrder);
	public Byte2IntMap create(Byte2IntMap.Entry... elements);
	@Override
	default Byte2IntMap create(Object... elements) {
		Byte2IntMap.Entry[] result = new Byte2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Byte2IntMap.Entry[] createArray(int length) { return new Byte2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Integer>> samples() {
		ObjectSamples<Byte2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getIntValue())
		);
	}
}