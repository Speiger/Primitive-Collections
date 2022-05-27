package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2LongMapGenerator extends TestMapGenerator<Integer, Long> {
	public ObjectSamples<Int2LongMap.Entry> getSamples();
	public ObjectIterable<Int2LongMap.Entry> order(ObjectList<Int2LongMap.Entry> insertionOrder);
	public Int2LongMap create(Int2LongMap.Entry... elements);
	@Override
	default Int2LongMap create(Object... elements) {
		Int2LongMap.Entry[] result = new Int2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Int2LongMap.Entry[] createArray(int length) { return new Int2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Long>> samples() {
		ObjectSamples<Int2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getLongValue())
		);
	}
}