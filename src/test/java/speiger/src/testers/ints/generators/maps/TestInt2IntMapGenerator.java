package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2IntMapGenerator extends TestMapGenerator<Integer, Integer> {
	public ObjectSamples<Int2IntMap.Entry> getSamples();
	public ObjectIterable<Int2IntMap.Entry> order(ObjectList<Int2IntMap.Entry> insertionOrder);
	public Int2IntMap create(Int2IntMap.Entry... elements);
	@Override
	default Int2IntMap create(Object... elements) {
		Int2IntMap.Entry[] result = new Int2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Int2IntMap.Entry[] createArray(int length) { return new Int2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Integer>> samples() {
		ObjectSamples<Int2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getIntValue())
		);
	}
}