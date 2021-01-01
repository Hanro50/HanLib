package za.net.hanro50.vectors;

public final class vec<type> {
	public static <type> Vector2<type> D2(type x, type y) {
		return new Vector2<type>(x, y);
	}

	public static <type> Vector3<type> D3(type x, type y, type z) {
		return new Vector3<type>(x, y, z);
	}

	public static <type> Vector4<type> D4(type x, type y, type z, type w) {
		return new Vector4<type>(x, y, z, w);
	}
}
