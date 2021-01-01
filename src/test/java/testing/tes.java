package testing;


import za.net.hanro50.files.ConfigObj;
import za.net.hanro50.files.FIleUtil;

public class tes extends ConfigObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Option
	public String test = "test";
	
	public tes() {
		super("", "tes", "", new FIleUtil());
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		tes t =new  tes();
		t.load();
		t.save();
		
	}

}
