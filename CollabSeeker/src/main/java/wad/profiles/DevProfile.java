package wad.profiles;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wad.domain.Team;
import wad.repository.TeamRepository;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {

    @Autowired
    private TeamRepository teamRepository;
    
    @PostConstruct
    public void init() {
        Team aaltohelsinki = new Team();
        aaltohelsinki.setName("Aalto-Helsinki");
        aaltohelsinki.setYear("2016");
        aaltohelsinki.setDescription("We are working to detect and degrade cyanotoxins.");
        aaltohelsinki.setFacebook("AaltoHelsinki");
        aaltohelsinki.setTwitter("AaltoHelsinki");
        aaltohelsinki.setTags(String.join(" ", "aalto", "helsinki", "aalto-helsinki",
        "cyanobacteria", "modelling", "propane", "energy", "lab", "finland"));
        aaltohelsinki.setPassword("helppo");
        teamRepository.save(aaltohelsinki);
        
        Team uppsala = new Team();
        uppsala.setName("Uppsala");
        uppsala.setYear("2016");
        uppsala.setDescription("Polycyclic aromatic hydrocarbons (PAHs) are potent carcinogens. Our project aims to degrade PAHs in industrial waste");
        uppsala.setFacebook("UppsalaIgem");
        uppsala.setTags(String.join(" ", "uppsala", "bah", "bap", "naphthalene", "environment", "bioremediation", "survey",
        "polycyclic", "aromatic", "hydrocarbons", "laccase", "rhamnolipids", "sweden"));
        uppsala.setPassword("helppo");
        teamRepository.save(uppsala);
        
    }
}
