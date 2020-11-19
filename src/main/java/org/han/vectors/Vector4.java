package org.han.vectors;

public class Vector4<type> extends Vector3<type> implements Vectors<type> {
	public Vector4(type x, type y, type z, type w) {
		super(x, y, z);
		A.add(w);
	}

	public type w() {
		return A.get(3);
	}

	public void w(type w) {
		change(3, w);
	}
}
