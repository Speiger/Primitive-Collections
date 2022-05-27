package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestByte2ShortMapGenerator extends TestMapGenerator<Byte, Short> {
	public ObjectSamples<Byte2ShortMap.Entry> getSamples();
	public ObjectIterable<Byte2ShortMap.Entry> order(ObjectList<Byte2ShortMap.Entry> insertionOrder);
	public Byte2ShortMap create(Byte2ShortMap.Entry... elements);
	@Override
	default Byte2ShortMap create(Object... elements) {
		Byte2ShortMap.Entry[] result = new Byte2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Byte2ShortMap.Entry[] createArray(int length) { return new Byte2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Short>> samples() {
		ObjectSamples<Byte2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getShortValue())
		);
	}
}