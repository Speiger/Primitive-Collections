package speiger.src.testers.chars.builder.maps;

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
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2DoubleMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2DoubleMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Char2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Double> {
	public static Char2DoubleMapTestSuiteBuilder using(TestChar2DoubleMapGenerator generator) {
		return (Char2DoubleMapTestSuiteBuilder) new Char2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2DoubleMapClearTester.class);
		testers.add(Char2DoubleMapComputeTester.class);
		testers.add(Char2DoubleMapComputeIfAbsentTester.class);
		testers.add(Char2DoubleMapComputeIfPresentTester.class);
		testers.add(Char2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Char2DoubleMapContainsKeyTester.class);
		testers.add(Char2DoubleMapContainsValueTester.class);
		testers.add(Char2DoubleMapEntrySetTester.class);
		testers.add(Char2DoubleMapEqualsTester.class);
		testers.add(Char2DoubleMapForEachTester.class);
		testers.add(Char2DoubleMapGetTester.class);
		testers.add(Char2DoubleMapGetOrDefaultTester.class);
		testers.add(Char2DoubleMapHashCodeTester.class);
		testers.add(Char2DoubleMapIsEmptyTester.class);
		testers.add(Char2DoubleMapMergeTester.class);
		testers.add(Char2DoubleMapPutTester.class);
		testers.add(Char2DoubleMapAddToTester.class);
		testers.add(Char2DoubleMapPutAllTester.class);
		testers.add(Char2DoubleMapPutAllArrayTester.class);
		testers.add(Char2DoubleMapPutIfAbsentTester.class);
		testers.add(Char2DoubleMapRemoveTester.class);
		testers.add(Char2DoubleMapRemoveEntryTester.class);
		testers.add(Char2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Char2DoubleMapReplaceTester.class);
		testers.add(Char2DoubleMapReplaceAllTester.class);
		testers.add(Char2DoubleMapReplaceEntryTester.class);
		testers.add(Char2DoubleMapSizeTester.class);
		testers.add(Char2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Double>, Map.Entry<Character, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected DoubleCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestDoubleCollectionGenerator generator) {
		return DoubleCollectionTestSuiteBuilder.using(generator);
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
		return suppressing;
	}
}