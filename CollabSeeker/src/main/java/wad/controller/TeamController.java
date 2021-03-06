package wad.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.Team;
import wad.repository.TeamRepository;
import wad.service.TeamService;

@Controller
@RequestMapping("/team")
public class TeamController {
   
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private @Resource(name="cats") Set<String> allcategories;

    public Set<String> getAllcategories() {
        return allcategories;
    }
    
    @RequestMapping(value = "/{teamname}")
    public String show(@PathVariable String teamname, Model model, HttpSession session) {   
        Team thisTeam = teamRepository.findByName(teamname);
        if (teamService.getAuthenticatedTeamName() != null && teamService.getAuthenticatedTeamName().equals(teamname)) {
            model.addAttribute("editable", true);
        }
        model.addAttribute("myteamname", teamService.getAuthenticatedTeamName());
        model.addAttribute("team", thisTeam);
        session.setAttribute("categories", allcategories);
        return "team";
    }
    
    @RequestMapping(value = "/edit/{teamname}", method = RequestMethod.GET)
    public String showEditing(@PathVariable String teamname, Model model) {
        Team thisTeam = teamRepository.findByName(teamname);
        if (teamService.getAuthenticatedTeamName() == null || !teamService.getAuthenticatedTeamName().equals(teamname)) {
            return "redirect:/team/"+teamname;
        }
        model.addAttribute("myteamname", teamService.getAuthenticatedTeamName());
        model.addAttribute("team", thisTeam);
        //String tags = thisTeam.getTags().stream().collect(Collectors.joining(" "));
        List<String> tags = thisTeam.getTags();
        model.addAttribute("tags", tags);
        return "editTeam";
    }
    
    @RequestMapping(value = "/edit/{teamname}", method = RequestMethod.POST)
    public String edit(@PathVariable String teamname, @RequestParam String description, @RequestParam String collabdetails, @RequestParam String email,
            @RequestParam String facebook, @RequestParam String twitter, @RequestParam String newPassword,@RequestParam(value="categories[]") List<String> categories,
            @RequestParam(value="file", required=false) MultipartFile file) throws IOException {
        Team thisTeam = teamRepository.findByName(teamname);
        if (teamService.getAuthenticatedTeamName() == null || !teamService.getAuthenticatedTeamName().equals(teamname)) {
            return "redirect:/team/"+teamname;
        }
        
        allcategories.addAll(categories);
        thisTeam.setDescription(description);
        thisTeam.setCollabdetails(collabdetails);
        thisTeam.setEmail(email);
        thisTeam.setFacebook(facebook);
        thisTeam.setTwitter(twitter);
        thisTeam.setTags(categories);
        thisTeam.setLastUpdated(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
        if (!newPassword.isEmpty()) {
            thisTeam.setPassword(newPassword);
        }
        
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
    
    @RequestMapping(value = "/allcategories", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllCategories() {
        List<String> allcat = new ArrayList<String>();
        allcat.addAll(allcategories);
        return allcat;
    }
}
