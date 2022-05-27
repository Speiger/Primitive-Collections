package speiger.src.testers.booleans.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;

@SuppressWarnings("javadoc")
public class BooleanSamples implements BooleanIterable
{
	private final boolean e0;
	private final boolean e1;
	private final boolean e2;
	private final boolean e3;
	private final boolean e4;
	
	public BooleanSamples(SampleElements<Boolean> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public BooleanSamples(boolean e0, boolean e1, boolean e2, boolean e3, boolean e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Boolean> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public BooleanIterator iterator()
	{
		return asList().iterator();
	}
	
	public BooleanList asList()
	{
		return BooleanArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public boolean e0()
	{
		return e0;
	}
	
	public boolean e1()
	{
		return e1;
	}
	
	public boolean e2()
	{
		return e2;
	}
	
	public boolean e3()
	{
		return e3;
	}
	
	public boolean e4()
	{
		return e4;
	}
}