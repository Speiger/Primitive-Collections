package speiger.src.testers.chars.builder;

import static com.google.common.collect.testing.features.CollectionFeature.DESCENDING_VIEW;
import static com.google.common.collect.testing.features.CollectionFeature.SUBSET_VIEW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.NavigableSetNavigationTester;

import junit.framework.TestSuite;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.utils.CharLists;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.impl.CharSortedSetSubsetTestSetGenerator.CharNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.chars.tests.set.CharNavigableSetNavigationTester;
import speiger.src.testers.chars.utils.CharSamples;

@SuppressWarnings("javadoc")
public class CharNavigableSetTestSuiteBuilder extends CharSortedSetTestSuiteBuilder {
	public static CharNavigableSetTestSuiteBuilder using(TestCharNavigableSetGenerator generator) {
		return (CharNavigableSetTestSuiteBuilder) new CharNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(CharNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Character>, Character>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));

		if (!parentBuilder.getFeatures().contains(SUBSET_VIEW)) {
			// Other combinations are inherited from SortedSetTestSuiteBuilder.
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
		}
		if (!parentBuilder.getFeatures().contains(DESCENDING_VIEW)) {
			derivedSuites.add(createDescendingSuite(parentBuilder));
		}
		return derivedSuites;
	}

	@Override
	CharSortedSetTestSuiteBuilder newBuilderUsing(TestCharSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new CharNavigableSetSubsetTestSetGenerator (delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Character>, Character>> parentBuilder) {
		TestCharNavigableSetGenerator delegate = (TestCharNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return CharNavigableSetTestSuiteBuilder.using(new TestCharSetGenerator() {

			@Override
			public SampleElements<Character> samples() {
				return delegate.samples();
			}
			
			@Override
			public CharSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public CharIterable order(CharList insertionOrder) {
				CharList list = new CharArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				CharLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Character> order(List<Character> insertionOrder) {
				CharList list = new CharArrayList();
				for(Character entry : delegate.order(insertionOrder))
				{
					list.add(entry.charValue());
				}
				CharLists.reverse(list);
				return list;
			}
			
			@Override
			public CharNavigableSet create(char... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public CharNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}