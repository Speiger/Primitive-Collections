package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestDouble2IntMapGenerator extends TestMapGenerator<Double, Integer> {
	public ObjectSamples<Double2IntMap.Entry> getSamples();
	public ObjectIterable<Double2IntMap.Entry> order(ObjectList<Double2IntMap.Entry> insertionOrder);
	public Double2IntMap create(Double2IntMap.Entry... elements);
	@Override
	default Double2IntMap create(Object... elements) {
		Double2IntMap.Entry[] result = new Double2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Double2IntMap.Entry[] createArray(int length) { return new Double2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Integer>> samples() {
		ObjectSamples<Double2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getIntValue())
		);
	}
}