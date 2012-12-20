package com.example.vncreatures.common;

public class Common {
	public static String CREATURE_EXTRA = "creatureId";
	public static String ACTION_EXTRA = "action";
	public static int CREATURE_ACTIVITY_REQUEST_CODE = 0;

	public enum CREATURE {
		Anime("1"), Plant("2"), Insect("3");

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
