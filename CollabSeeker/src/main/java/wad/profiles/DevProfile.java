package wad.profiles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
        // All teams -> Add facebook -> Add description
        /*
        File teamData = new File("./src/main/webapp/static/csv/teams.csv");
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(teamData, Charset.defaultCharset(), CSVFormat.RFC4180);
        } catch (IOException e) {}
        if (parser != null) {
            for (CSVRecord teamRecord : parser) {
                Team nextTeam = new Team();
                nextTeam.setName(teamRecord.get(0));
                nextTeam.setYear("2016");
                nextTeam.setDescription("");
                nextTeam.setPassword("helppo");
                teamRepository.save(nextTeam);
            }
        }
        
        File facebookData = new File("./src/main/webapp/static/csv/team_and_facebook.csv");
        parser = null;
        try {
            parser = CSVParser.parse(facebookData, Charset.defaultCharset(), CSVFormat.RFC4180);
        } catch (IOException e) {}
        if (parser != null) {
            for (CSVRecord facebookRecord : parser) {
                Team team = teamRepository.findByName(facebookRecord.get(0));
                if (team != null) {
                    team.setFacebook(facebookRecord.get(3));
                    teamRepository.save(team);
                }
            }
        }
        
        File projectData = new File("./src/main/webapp/static/data/");
        DirectoryStream<Path> filepaths = null;
        try {
            filepaths = Files.newDirectoryStream(projectData.toPath());
            for (Path filepath : filepaths) {
                String description = String.join(" ", Files.readAllLines(filepath));
                Team team = teamRepository.findByName(filepath.getFileName().toString());
                if (team != null) {
                    team.setDescription(description);
                    teamRepository.save(team);
                }
            }
        } catch (IOException e) {
            System.out.println("Project data files not found!");
        }*/
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
        
        File CSVData = new File("C:/Magus/OtherProjects/Websites/iGEM/NewCollabSeeker/CollabSeeker/src/main/webapp/static/media/team_and_facebook.csv");
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(CSVData, Charset.defaultCharset(), CSVFormat.RFC4180);
        } catch (IOException e) {}
        if (parser != null) {
            for (CSVRecord record : parser) {
                Team nextTeam = new Team();
                nextTeam.setName(record.get(0));
                nextTeam.setYear("2016");
                nextTeam.setDescription("Description pending.");
                nextTeam.setFacebook(record.get(3));
                nextTeam.setPassword("helppo");
                teamRepository.save(nextTeam);
            }
        }
        
    }
}
