package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2DoubleMapGenerator extends TestMapGenerator<Integer, Double> {
	public ObjectSamples<Int2DoubleMap.Entry> getSamples();
	public ObjectIterable<Int2DoubleMap.Entry> order(ObjectList<Int2DoubleMap.Entry> insertionOrder);
	public Int2DoubleMap create(Int2DoubleMap.Entry... elements);
	@Override
	default Int2DoubleMap create(Object... elements) {
		Int2DoubleMap.Entry[] result = new Int2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Int2DoubleMap.Entry[] createArray(int length) { return new Int2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Double>> samples() {
		ObjectSamples<Int2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getDoubleValue())
		);
	}
}