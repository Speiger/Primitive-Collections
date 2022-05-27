package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2LongMapGenerator extends TestMapGenerator<Character, Long> {
	public ObjectSamples<Char2LongMap.Entry> getSamples();
	public ObjectIterable<Char2LongMap.Entry> order(ObjectList<Char2LongMap.Entry> insertionOrder);
	public Char2LongMap create(Char2LongMap.Entry... elements);
	@Override
	default Char2LongMap create(Object... elements) {
		Char2LongMap.Entry[] result = new Char2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Char2LongMap.Entry[] createArray(int length) { return new Char2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Long>> samples() {
		ObjectSamples<Char2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getLongValue())
		);
	}
}