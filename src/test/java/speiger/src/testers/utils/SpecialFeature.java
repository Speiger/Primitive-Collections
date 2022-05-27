package speiger.src.testers.utils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Set;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.TesterAnnotation;

@SuppressWarnings("rawtypes")
public enum SpecialFeature implements Feature<Collection> {
	COPYING;

	private final Set<Feature<? super Collection>> implied;

	SpecialFeature(Feature<? super Collection>... implied) {
		this.implied = Helpers.copyToSet(implied);
	}

	@Override
	public Set<Feature<? super Collection>> getImpliedFeatures() {
		return implied;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@TesterAnnotation
	public @interface Require {
		SpecialFeature[] value() default {};

		SpecialFeature[] absent() default {};
	}
}
