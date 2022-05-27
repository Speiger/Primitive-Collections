package speiger.src.testers.doubles.generators;

import speiger.src.collections.doubles.sets.DoubleSortedSet;

@SuppressWarnings("javadoc")
public interface TestDoubleSortedSetGenerator extends TestDoubleSetGenerator {
	@Override
	DoubleSortedSet create(double... elements);
	@Override
	DoubleSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  double belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  double belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  double aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  double aboveSamplesGreater();
}