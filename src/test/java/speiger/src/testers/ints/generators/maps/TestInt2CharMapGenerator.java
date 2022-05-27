package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestInt2CharMapGenerator extends TestMapGenerator<Integer, Character> {
	public ObjectSamples<Int2CharMap.Entry> getSamples();
	public ObjectIterable<Int2CharMap.Entry> order(ObjectList<Int2CharMap.Entry> insertionOrder);
	public Int2CharMap create(Int2CharMap.Entry... elements);
	@Override
	default Int2CharMap create(Object... elements) {
		Int2CharMap.Entry[] result = new Int2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Int2CharMap.Entry[] createArray(int length) { return new Int2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Character>> samples() {
		ObjectSamples<Int2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getCharValue())
		);
	}
}