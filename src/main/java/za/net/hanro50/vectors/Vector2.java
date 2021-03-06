package za.net.hanro50.vectors;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Vector2<type> implements Vectors<type> {
	@Expose public List<type> A;

	public Vector2(type x, type y) {
		A = new ArrayList<type>();
		A.add(x);
		A.add(y);
	}

	protected void change(int Index, type val) {
		A.remove(Index);
		A.add(Index, val);
	}

	public type x() {
		return A.get(0);
	}

	public void x(type x) {
		change(0, x);
	}

	public type y() {
		return A.get(1);
	}

	public void y(type y) {
		change(1, y);
	}

		

	public String tostring() {
		String STR = "(";
		for (int i = 0; i < A.size(); i++) {
			STR = STR + String.format("{%f}", A.get(i));
		}
		STR = STR + ")";

		return STR;
	}

	
	public List<type> get() {
		// TODO Auto-generated method stub
		return A;
	}
}
