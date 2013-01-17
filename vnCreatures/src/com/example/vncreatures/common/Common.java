package com.example.vncreatures.common;

import com.example.vncreatures.R;

public class Common {
	public static int THEME = R.style.Theme_Styled_NoTitleBar_Fullscreen;
	
	public static String CREATURE_EXTRA = "creatureId";
	public static String ACTION_EXTRA = "view detail";
	public static String ACTION_CHOOSE_FAMILY = "choose family";
	public static String ACTION_CHOOSE_CLASS = "choose class";
	public static String ACTION_CHOOSE_ORDER = "choose order";

	public static String FAMILY_EXTRA = "familyId";
	public static String ORDER_EXTRA = "orderId";
	public static String CLASS_EXTRA = "classId";

	public static String KINGDOM = "kingdom";

	public static int CREATURE_ACTIVITY_REQUEST_CODE = 0;
	public static String CREATURE_URL_IMAGES_EXTRA = "urlImages";
	public static String CREATURE_URL_IMAGES_POSITION_EXTRA = "urlImagesPosition";
	
	public static final String KINGDOM_PREF = "KingdomPrefs";

	public enum CREATURE {
		animal("1"), plant("2"), insect("3");

		private final String identifier;

		private CREATURE(String identifier) {
			this.identifier = identifier;
		}

		public String toString() {
			return identifier;
		}

		public static String getEnumNameForValue(Object value) {
			CREATURE[] values = CREATURE.values();
			String enumValue = null;
			for (CREATURE eachValue : values) {
				enumValue = eachValue.toString();
				if (enumValue.equals(value)) {
					return eachValue.name();
				}
			}
			return enumValue;
		}
	}
}