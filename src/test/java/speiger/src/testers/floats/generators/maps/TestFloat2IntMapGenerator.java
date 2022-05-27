package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestFloat2IntMapGenerator extends TestMapGenerator<Float, Integer> {
	public ObjectSamples<Float2IntMap.Entry> getSamples();
	public ObjectIterable<Float2IntMap.Entry> order(ObjectList<Float2IntMap.Entry> insertionOrder);
	public Float2IntMap create(Float2IntMap.Entry... elements);
	@Override
	default Float2IntMap create(Object... elements) {
		Float2IntMap.Entry[] result = new Float2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Float2IntMap.Entry[] createArray(int length) { return new Float2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Integer>> samples() {
		ObjectSamples<Float2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getIntValue())
		);
	}
}