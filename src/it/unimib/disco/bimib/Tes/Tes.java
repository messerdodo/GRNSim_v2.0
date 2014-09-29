/**
 * 
 * This class is the representation of a TES structure.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */
package it.unimib.disco.bimib.Tes;

//System imports
import java.util.ArrayList;


public class Tes{

	private ArrayList<Object> tes;

	/**
	 * Default constructor
	 */
	public Tes() {
		this.tes = new ArrayList<Object>();

	}

	/**
	 * Generic constructor
	 * @param attractor: The initial element of the Tes
	 */
	public Tes(Object attractor){
		this.tes = new ArrayList<Object>();
		//Inserts the attractor in the Tes only if the parameter isn't null
		if(attractor != null)
			this.tes.add(attractor);
	}

	/**
	 * This method adds an attractor in the Tes structure.
	 * @param attractor: The attractor. It mustn't be null
	 */
	public void addAttractor(Object attractor){
		if(attractor != null)
			this.tes.add(attractor);
	}

	/**
	 * This method removes an attractor from the Tes
	 * @param attractor: The attractor to be removed
	 */
	public void removeAttractor(Object attractor){
		if(attractor != null)
			this.tes.remove(attractor);
	}

	/**
	 * This method merges a given Tes in this Tes instance
	 * @param otherTes: a Tes to be merged
	 */
	public void merge(Tes otherTes){
		if(otherTes != null){
			//Adds each element in otherTess to this tes
			for(Object attractor : otherTes.tes){
				//Checks if the attractor is already present in this Tes
				if(!this.tes.contains(attractor))
					this.tes.add(attractor);
			}
		}
	}

	/**
	 * This method finds an attractor in the TES
	 * @param attractor: The attractor to be found
	 * @return: The position of the attractor in the TES
	 */
	public int find(Object attractor){

		int i = 0;
		while(i < this.tes.size() && !(tes.get(i).equals(attractor)))
			i++;
		return (i == this.tes.size() ? -1 : i);
	}

	@Override
	/**
	 * This method compares two TES(es)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tes))
			return false;

		Tes other = (Tes) obj;
		if(this.tes.size() != other.tes.size())
			return false;
		for(int i = 0; i < this.tes.size(); i++)
			if(! this.tes.get(i).equals(other.tes.get(i)))
				return false;
		return true;
	}

	/**
	 * This method returns the TES size
	 * @return: The TES size
	 */
	public int sizeTes(){
		return tes.size();
	}

	/**
	 * This method returns the index-th element of the TES
	 * @param index: The element position 
	 * @return: The required element
	 */
	public Object getElements(int index){
		return tes.get(index);
	}



}
