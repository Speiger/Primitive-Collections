package speiger.src.testers.floats.builder.maps;

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
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2CharMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2CharMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2CharMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Float2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Character> {
	public static Float2CharMapTestSuiteBuilder using(TestFloat2CharMapGenerator generator) {
		return (Float2CharMapTestSuiteBuilder) new Float2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2CharMapClearTester.class);
		testers.add(Float2CharMapComputeTester.class);
		testers.add(Float2CharMapComputeIfAbsentTester.class);
		testers.add(Float2CharMapComputeIfPresentTester.class);
		testers.add(Float2CharMapSupplyIfAbsentTester.class);
		testers.add(Float2CharMapContainsKeyTester.class);
		testers.add(Float2CharMapContainsValueTester.class);
		testers.add(Float2CharMapEntrySetTester.class);
		testers.add(Float2CharMapEqualsTester.class);
		testers.add(Float2CharMapForEachTester.class);
		testers.add(Float2CharMapGetTester.class);
		testers.add(Float2CharMapGetOrDefaultTester.class);
		testers.add(Float2CharMapHashCodeTester.class);
		testers.add(Float2CharMapIsEmptyTester.class);
		testers.add(Float2CharMapMergeTester.class);
		testers.add(Float2CharMapPutTester.class);
		testers.add(Float2CharMapAddToTester.class);
		testers.add(Float2CharMapPutAllTester.class);
		testers.add(Float2CharMapPutAllArrayTester.class);
		testers.add(Float2CharMapPutIfAbsentTester.class);
		testers.add(Float2CharMapRemoveTester.class);
		testers.add(Float2CharMapRemoveEntryTester.class);
		testers.add(Float2CharMapRemoveOrDefaultTester.class);
		testers.add(Float2CharMapReplaceTester.class);
		testers.add(Float2CharMapReplaceAllTester.class);
		testers.add(Float2CharMapReplaceEntryTester.class);
		testers.add(Float2CharMapSizeTester.class);
		testers.add(Float2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Character>, Map.Entry<Float, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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