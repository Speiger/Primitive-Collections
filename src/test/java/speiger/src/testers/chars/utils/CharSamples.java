package speiger.src.testers.chars.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;

public class CharSamples implements CharIterable
{
	private final char e0;
	private final char e1;
	private final char e2;
	private final char e3;
	private final char e4;
	
	public CharSamples(SampleElements<Character> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public CharSamples(char e0, char e1, char e2, char e3, char e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Character> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public CharIterator iterator()
	{
		return asList().iterator();
	}
	
	public CharList asList()
	{
		return CharArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public char e0()
	{
		return e0;
	}
	
	public char e1()
	{
		return e1;
	}
	
	public char e2()
	{
		return e2;
	}
	
	public char e3()
	{
		return e3;
	}
	
	public char e4()
	{
		return e4;
	}
}