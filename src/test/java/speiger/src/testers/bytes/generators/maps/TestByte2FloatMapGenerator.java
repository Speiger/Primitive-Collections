package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestByte2FloatMapGenerator extends TestMapGenerator<Byte, Float> {
	public ObjectSamples<Byte2FloatMap.Entry> getSamples();
	public ObjectIterable<Byte2FloatMap.Entry> order(ObjectList<Byte2FloatMap.Entry> insertionOrder);
	public Byte2FloatMap create(Byte2FloatMap.Entry... elements);
	@Override
	default Byte2FloatMap create(Object... elements) {
		Byte2FloatMap.Entry[] result = new Byte2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Byte2FloatMap.Entry[] createArray(int length) { return new Byte2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Float>> samples() {
		ObjectSamples<Byte2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getFloatValue())
		);
	}
}