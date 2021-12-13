package speiger.src.collections.objects.set;

import java.util.SortedSet;

import com.google.common.collect.testing.TestIntegerSortedSetGenerator;
import com.google.common.collect.testing.TestSortedSetGenerator;

@SuppressWarnings("javadoc")
public abstract class TestIntegerNavigableSetGenerator extends TestIntegerSortedSetGenerator implements TestSortedSetGenerator<Integer>
{
	@Override
	protected abstract SortedSet<Integer> create(Integer[] elements);

	@Override
	public SortedSet<Integer> create(Object... elements) {
	    Integer[] array = new Integer[elements.length];
	    int i = 0;
	    for (Object e : elements) {
	      array[i++] = (Integer) e;
	    }
	    return create(array);
	}

	@Override
	public Integer belowSamplesLesser() { return -2; }
	@Override
	public Integer belowSamplesGreater() { return -1; }
	@Override
	public Integer aboveSamplesLesser() { return 5; }
	@Override
	public Integer aboveSamplesGreater() { return 6; }		
}