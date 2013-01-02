package com.example.vncreatures.common;

public class Common {
	public static String CREATURE_EXTRA = "creatureId";
	public static String ACTION_EXTRA = "view detail";
	public static String ACTION_CHOOSE_FAMILY = "choose family";
	public static String ACTION_CHOOSE_CLASS = "choose class";
	public static String ACTION_CHOOSE_ORDER = "choose order";
	
	public static String FAMILY_EXTRA = "familyId";
	public static String ORDER_EXTRA = "orderId";
	public static String CLASS_EXTRA = "classId";
	
	public static String KINGDOM = "1"; 
			
	public static int CREATURE_ACTIVITY_REQUEST_CODE = 0;
	
	

	public enum CREATURE {
		Animal("1"), Plant("2"), Insect("3");

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
