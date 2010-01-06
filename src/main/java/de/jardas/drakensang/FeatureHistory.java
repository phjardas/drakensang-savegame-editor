package de.jardas.drakensang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unchecked")
public final class FeatureHistory {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(FeatureHistory.class);
	private static final List<Feature> FEATURES = new ArrayList<Feature>();

	static {
		try {
			LOG.info("Loading feature history.");
			final Document doc = new SAXReader().read(FeatureHistory.class
					.getResourceAsStream("feature-history.xml"));
			final List<Element> features = doc.getRootElement().elements(
					"feature");
			for (Element featureEl : features) {
				final Feature feature = new Feature();
				final List<Element> descriptions = featureEl
						.elements("description");

				for (Element descrEl : descriptions) {
					final String lang = descrEl.attributeValue("lang");
					final String descr = descrEl.getTextTrim();
					feature.addDescription(lang, descr);
				}

				LOG.debug("Feature #" + FEATURES.size() + ": " + feature);
				FEATURES.add(feature);
			}
		} catch (Exception e) {
			LOG.error("Error reading feature history: " + e, e);
		}
	}

	private FeatureHistory() {
		// utility class
	}

	public static Feature[] getUnknownFeatures(Settings settings) {
		final int latest = settings.getLatestKnownFeature();

		if (latest > getLatestFeatureId()) {
			return new Feature[0];
		}

		settings.setLatestKnownFeature(getLatestFeatureId());
		settings.save();

		final List<Feature> feats = FEATURES.subList(latest + 1, FEATURES
				.size());
		return feats.toArray(new Feature[feats.size()]);
	}

	public static int getLatestFeatureId() {
		return FEATURES.size() - 1;
	}

	public static class Feature {
		private final Map<String, String> descriptions = new HashMap<String, String>();

		private void addDescription(String language, String description) {
			descriptions.put(language, description);
		}

		public String getDescription(String language) {
			return descriptions.get(language);
		}

		@Override
		public String toString() {
			return "Feature[" + descriptions + "]";
		}
	}
}
