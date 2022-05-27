package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestFloat2LongMapGenerator extends TestMapGenerator<Float, Long> {
	public ObjectSamples<Float2LongMap.Entry> getSamples();
	public ObjectIterable<Float2LongMap.Entry> order(ObjectList<Float2LongMap.Entry> insertionOrder);
	public Float2LongMap create(Float2LongMap.Entry... elements);
	@Override
	default Float2LongMap create(Object... elements) {
		Float2LongMap.Entry[] result = new Float2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Float2LongMap.Entry[] createArray(int length) { return new Float2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Long>> samples() {
		ObjectSamples<Float2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getLongValue())
		);
	}
}