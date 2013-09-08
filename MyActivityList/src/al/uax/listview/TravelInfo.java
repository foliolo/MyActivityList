package al.uax.listview;

import java.io.Serializable;

import android.app.ListActivity;

public class TravelInfo extends ListActivity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String city;
	private String country;
	private int year;
	private String anotacion;
	
	//Constructor
	public TravelInfo(String city, String country, int year, String anotacion){
		this.city = city;
		this.country = country;
		this.year = year;
		this.anotacion = anotacion;
	}
	
	public String getCity() {
		return city;
	}
	public String getCountry() {
		return country;
	}
	public int getYear() {
		return year;
	}
	public String getAnotacion(){
		return anotacion;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}
}
