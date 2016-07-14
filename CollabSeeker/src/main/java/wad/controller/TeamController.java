package wad.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.FileObject;
import wad.domain.Team;
import wad.repository.FileObjectRepository;
import wad.repository.TeamRepository;
import wad.service.TeamService;

@Controller
@RequestMapping("/team")
public class TeamController {
   
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private FileObjectRepository fileRepository;
    
    @Autowired
    private TeamService teamService;
    
    @RequestMapping(value = "/{teamname}")
    public String show(@PathVariable String teamname, Model model) {   
        Team thisTeam = teamRepository.findByName(teamname);
        if (teamService.getAuthenticatedTeamName() != null && teamService.getAuthenticatedTeamName().equals(teamname)) {
            model.addAttribute("editable", true);
        }
        model.addAttribute("team", thisTeam);
        return "team";
    }
    
    @RequestMapping(value = "/edit/{teamname}", method = RequestMethod.GET)
    public String showEditing(@PathVariable String teamname, Model model) {
        Team thisTeam = teamRepository.findByName(teamname);
        model.addAttribute("team", thisTeam);
        String tags = thisTeam.getTags().stream().collect(Collectors.joining(" "));
        model.addAttribute("tags", tags);
        return "editTeam";
    }
    
    @RequestMapping(value = "/edit/{teamname}", method = RequestMethod.POST)
    public String edit(@PathVariable String teamname, @RequestParam String description,
            @RequestParam String facebook, @RequestParam String twitter, @RequestParam String tags,
            @RequestParam(value="file", required=false) MultipartFile file) throws IOException {
        Team thisTeam = teamRepository.findByName(teamname);
        thisTeam.setDescription(description);
        thisTeam.setFacebook(facebook);
        thisTeam.setTwitter(twitter);
        thisTeam.setTags(tags);
        thisTeam.setLastUpdated(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
        
        /*FileObject imageFile = new FileObject();
        imageFile.setContent(file.getBytes());
        imageFile.setName(file.getName());
        imageFile.setMediaType(file.getContentType());
        imageFile = fileRepository.save(imageFile);*/
        
        if (file != null && !file.isEmpty()) {
            thisTeam.setImage(file.getBytes());
        }
        
        teamRepository.save(thisTeam);
        return "redirect:/team/"+teamname;
    }
    
    @RequestMapping(value = "/images/{teamname}", method = RequestMethod.GET, produces="image/png")
    @ResponseBody
    public byte[] getImage(@PathVariable String teamname) {
        return teamRepository.findByName(teamname).getImage();
    }
    
}
