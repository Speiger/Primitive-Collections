package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestInt2BooleanMapGenerator extends TestMapGenerator<Integer, Boolean> {
	public ObjectSamples<Int2BooleanMap.Entry> getSamples();
	public ObjectIterable<Int2BooleanMap.Entry> order(ObjectList<Int2BooleanMap.Entry> insertionOrder);
	public Int2BooleanMap create(Int2BooleanMap.Entry... elements);
	@Override
	default Int2BooleanMap create(Object... elements) {
		Int2BooleanMap.Entry[] result = new Int2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Int2BooleanMap.Entry[] createArray(int length) { return new Int2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Boolean>> samples() {
		ObjectSamples<Int2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getBooleanValue())
		);
	}
}