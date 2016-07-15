package wad.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
public class Team extends AbstractPersistable<Long> {
    
    @Column(unique = true)
    private String name;
    
    private String password;
    private String salt;
    
    private String year;
    private String description;
    private String facebook;
    private String twitter;
    
    //@ElementCollection(targetClass=String.class)
    private String tags;
    
    private String lastUpdated;
    
    @Lob
    @Column(length = 3*1024*1024)
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    
    public Team() {
        this.facebook = "";
        this.twitter = "";
        this.lastUpdated = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        this.image = null;
    }
    
    /*
    public Team() {
        this.name = "default";
        this.year = "0000";
        this.description = "A sample project.";
        this.facebook = null;
        this.twitter = null;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public List<String> getTags() {
        return Arrays.asList(this.tags.split(" "));
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, salt);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public byte[] getImage() {
        return image;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }
}