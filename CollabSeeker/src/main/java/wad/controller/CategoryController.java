
package wad.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Team;
import wad.repository.TeamRepository;
import wad.service.TeamService;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private TeamService teamService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String results(@RequestParam("q") String searchQuery, Model model) throws IOException, ParseException {
        List<Team> teams = new ArrayList<Team>();
        teams.addAll(teamRepository.findByTagsContainingIgnoreCase(searchQuery));
        
        model.addAttribute("myteamname", teamService.getAuthenticatedTeamName());
        model.addAttribute("teams", teams);
        return "search";
    }
    
}
