package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestChar2IntMapGenerator extends TestMapGenerator<Character, Integer> {
	public ObjectSamples<Char2IntMap.Entry> getSamples();
	public ObjectIterable<Char2IntMap.Entry> order(ObjectList<Char2IntMap.Entry> insertionOrder);
	public Char2IntMap create(Char2IntMap.Entry... elements);
	@Override
	default Char2IntMap create(Object... elements) {
		Char2IntMap.Entry[] result = new Char2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Char2IntMap.Entry[] createArray(int length) { return new Char2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Integer>> samples() {
		ObjectSamples<Char2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getIntValue())
		);
	}
}