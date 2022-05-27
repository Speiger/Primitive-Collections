package speiger.src.testers.doubles.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2CharMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2CharMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2CharMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Character> {
	public static Double2CharMapTestSuiteBuilder using(TestDouble2CharMapGenerator generator) {
		return (Double2CharMapTestSuiteBuilder) new Double2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2CharMapClearTester.class);
		testers.add(Double2CharMapComputeTester.class);
		testers.add(Double2CharMapComputeIfAbsentTester.class);
		testers.add(Double2CharMapComputeIfPresentTester.class);
		testers.add(Double2CharMapSupplyIfAbsentTester.class);
		testers.add(Double2CharMapContainsKeyTester.class);
		testers.add(Double2CharMapContainsValueTester.class);
		testers.add(Double2CharMapEntrySetTester.class);
		testers.add(Double2CharMapEqualsTester.class);
		testers.add(Double2CharMapForEachTester.class);
		testers.add(Double2CharMapGetTester.class);
		testers.add(Double2CharMapGetOrDefaultTester.class);
		testers.add(Double2CharMapHashCodeTester.class);
		testers.add(Double2CharMapIsEmptyTester.class);
		testers.add(Double2CharMapMergeTester.class);
		testers.add(Double2CharMapPutTester.class);
		testers.add(Double2CharMapAddToTester.class);
		testers.add(Double2CharMapPutAllTester.class);
		testers.add(Double2CharMapPutAllArrayTester.class);
		testers.add(Double2CharMapPutIfAbsentTester.class);
		testers.add(Double2CharMapRemoveTester.class);
		testers.add(Double2CharMapRemoveEntryTester.class);
		testers.add(Double2CharMapRemoveOrDefaultTester.class);
		testers.add(Double2CharMapReplaceTester.class);
		testers.add(Double2CharMapReplaceAllTester.class);
		testers.add(Double2CharMapReplaceEntryTester.class);
		testers.add(Double2CharMapSizeTester.class);
		testers.add(Double2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Character>, Map.Entry<Double, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
	}
	
	protected CharCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestCharCollectionGenerator generator) {
		return CharCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}