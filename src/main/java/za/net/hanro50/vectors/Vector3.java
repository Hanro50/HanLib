package za.net.hanro50.vectors;

public class Vector3<type> extends Vector2<type> implements Vectors<type> {
	public Vector3(type x, type y, type z) {
		super(x, y);
		A.add(z);
	}

	public type z() {
		return A.get(2);
	}

	public void z(type z) {
		change(2, z);
	}
}
