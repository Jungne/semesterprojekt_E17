package interfaces;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{

    
    private int id;
    private String name;
    private List<String> interests;
    private List<String> certificates;

    public User(int id, String name, List<String> interests, List<String> certificates) {
	this.id = id;
	this.name = name;
	this.interests = interests;
	this.certificates = certificates;
    }
    
    public void addInterest(String interest) {
	getInterests().add(interest);
    }
    
    public void removeInterest(String interest) {
	getInterests().remove(interest); //Jeg ved ikke om det her virker.
    }
    
    public void addCertificate(String certificate) {
	getCertificates().add(certificate);
    }
    
    public void removeCertificate(String certificate) {
	getCertificates().remove(certificate); //Også her.
    }
    
    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the interests
     */
    public List<String> getInterests() {
	return interests;
    }

    /**
     * @return the certificates
     */
    public List<String> getCertificates() {
	return certificates;
    }
    
}
