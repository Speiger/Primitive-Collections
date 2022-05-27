package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestShort2DoubleMapGenerator extends TestMapGenerator<Short, Double> {
	public ObjectSamples<Short2DoubleMap.Entry> getSamples();
	public ObjectIterable<Short2DoubleMap.Entry> order(ObjectList<Short2DoubleMap.Entry> insertionOrder);
	public Short2DoubleMap create(Short2DoubleMap.Entry... elements);
	@Override
	default Short2DoubleMap create(Object... elements) {
		Short2DoubleMap.Entry[] result = new Short2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Short2DoubleMap.Entry[] createArray(int length) { return new Short2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Double>> samples() {
		ObjectSamples<Short2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getDoubleValue())
		);
	}
}