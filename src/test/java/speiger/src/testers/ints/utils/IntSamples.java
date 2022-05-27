package speiger.src.testers.ints.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;

@SuppressWarnings("javadoc")
public class IntSamples implements IntIterable
{
	private final int e0;
	private final int e1;
	private final int e2;
	private final int e3;
	private final int e4;
	
	public IntSamples(SampleElements<Integer> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public IntSamples(int e0, int e1, int e2, int e3, int e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Integer> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public IntIterator iterator()
	{
		return asList().iterator();
	}
	
	public IntList asList()
	{
		return IntArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public int e0()
	{
		return e0;
	}
	
	public int e1()
	{
		return e1;
	}
	
	public int e2()
	{
		return e2;
	}
	
	public int e3()
	{
		return e3;
	}
	
	public int e4()
	{
		return e4;
	}
}