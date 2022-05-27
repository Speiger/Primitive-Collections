package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2LongMapGenerator extends TestMapGenerator<Double, Long> {
	public ObjectSamples<Double2LongMap.Entry> getSamples();
	public ObjectIterable<Double2LongMap.Entry> order(ObjectList<Double2LongMap.Entry> insertionOrder);
	public Double2LongMap create(Double2LongMap.Entry... elements);
	@Override
	default Double2LongMap create(Object... elements) {
		Double2LongMap.Entry[] result = new Double2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Double2LongMap.Entry[] createArray(int length) { return new Double2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Long>> samples() {
		ObjectSamples<Double2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getLongValue())
		);
	}
}