package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestByte2BooleanMapGenerator extends TestMapGenerator<Byte, Boolean> {
	public ObjectSamples<Byte2BooleanMap.Entry> getSamples();
	public ObjectIterable<Byte2BooleanMap.Entry> order(ObjectList<Byte2BooleanMap.Entry> insertionOrder);
	public Byte2BooleanMap create(Byte2BooleanMap.Entry... elements);
	@Override
	default Byte2BooleanMap create(Object... elements) {
		Byte2BooleanMap.Entry[] result = new Byte2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Byte2BooleanMap.Entry[] createArray(int length) { return new Byte2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Boolean>> samples() {
		ObjectSamples<Byte2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getBooleanValue())
		);
	}
}