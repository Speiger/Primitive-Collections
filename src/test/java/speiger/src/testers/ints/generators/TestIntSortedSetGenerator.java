package speiger.src.testers.ints.generators;

import speiger.src.collections.ints.sets.IntSortedSet;

@SuppressWarnings("javadoc")
public interface TestIntSortedSetGenerator extends TestIntSetGenerator {
	@Override
	IntSortedSet create(int... elements);
	@Override
	IntSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  int belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  int belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  int aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  int aboveSamplesGreater();
}