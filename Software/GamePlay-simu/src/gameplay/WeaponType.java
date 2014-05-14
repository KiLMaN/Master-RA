package gameplay;

public enum WeaponType {
	Fire {
		@Override
		public String toString() {
			return "Fire";

		}
	},
	Water {
		@Override
		public String toString() {
			return "Water";

		}
	},
	Ice {
		@Override
		public String toString() {
			return "Ice";

		}
	},
	Electric {
		@Override
		public String toString() {
			return "Electric";

		}
	},
	Wood {
		@Override
		public String toString() {
			return "Wood";

		}
	},
	Metal {
		@Override
		public String toString() {
			return "Metal";

		}
	}
}
