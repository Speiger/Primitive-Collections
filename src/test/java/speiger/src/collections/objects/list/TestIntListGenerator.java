package speiger.src.collections.objects.list;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.SampleElements.Ints;
import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.ints.lists.IntList;

@SuppressWarnings("javadoc")
public class TestIntListGenerator implements TestListGenerator<Integer>
{
	Function<int[], IntList> function;
	
	public TestIntListGenerator(Function<int[], IntList> function)
	{
		this.function = function;
	}

	@Override
	public SampleElements<Integer> samples()
	{
		return new Ints();
	}
	
	@Override
	public List<Integer> create(Object... elements)
	{
		int[] array = new int[elements.length];
		int i = 0;
		for(Object e : elements)
		{
			array[i++] = ((Integer)e).intValue();
		}
		return function.apply(array);
	}		
	@Override
	public Integer[] createArray(int length)
	{
		return new Integer[length];
	}
	
	@Override
	public List<Integer> order(List<Integer> insertionOrder)
	{
		return insertionOrder;
	}
}