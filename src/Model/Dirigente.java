package Model;

public class Dirigente extends Manager{

    String area;
	
	
	public void setArea(String area) {
		
		this.area=area;
		
	}
	
	public String getArea() {
		return area;
	}
	
	@Override
	public String toString() {
	    return  super.toString() +
	    		", Area: " + (area != null ? area : "N/A");
	}
	
}
