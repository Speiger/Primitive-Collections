package speiger.src.testers.chars.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.utils.CharSamples;

public interface TestCharCollectionGenerator extends TestCollectionGenerator<Character>
{
	public CharSamples getSamples();
	public CharCollection create(char...elements);
	public CharIterable order(CharList insertionOrder);
	
	@Override
	public default SampleElements<Character> samples() {return getSamples().toSamples();}
	@Override
	public CharCollection create(Object... elements);
	@Override
	public default Character[] createArray(int length) { return new Character[length]; }
	@Override
	public Iterable<Character> order(List<Character> insertionOrder);
}