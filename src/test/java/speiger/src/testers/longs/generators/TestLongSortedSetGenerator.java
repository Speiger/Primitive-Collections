package speiger.src.testers.longs.generators;

import speiger.src.collections.longs.sets.LongSortedSet;

public interface TestLongSortedSetGenerator extends TestLongSetGenerator {
	@Override
	LongSortedSet create(long... elements);
	@Override
	LongSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  long belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  long belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  long aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  long aboveSamplesGreater();
}