package wad.profiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wad.domain.Person;
import wad.domain.Post;
import wad.domain.Team;
import wad.repository.PersonRepository;
import wad.repository.PostRepository;
import wad.repository.TeamRepository;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PostRepository postRepository;

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
        
        Person jackB = new Person();
        jackB.setName("Jack Bauer");
        jackB.setSlogan("I'm federal agent Jack Bauer. This is the longest day of my life.");
        jackB.setUsername("jackb");
        jackB.setPassword("jackb");

        jackB = personRepository.save(jackB);

        Person jackR = new Person();
        jackR.setName("Jack Reacher");
        jackR.setSlogan("I know I'm smarter than an armadillo.");
        jackR.setUsername("jackr");
        jackR.setPassword("jackr");
        jackR = personRepository.save(jackR);
        
        
        Post post = new Post();
        post.setContent("Now they broke my toothbrush, I don't own anything.");
        post.setAuthor(jackR);
        
        postRepository.save(post);
        
        Post post2 = new Post();
        post2.setContent(":(");
        post2.setAuthor(jackB);
        
        postRepository.save(post2);
        
        Post post3 = new Post();
        post3.setContent("I'm not a vagrant. I'm a hobo. Big difference.");
        post3.setAuthor(jackR);
        
        postRepository.save(post3);
    }
}
