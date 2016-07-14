package wad.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Team;
import wad.repository.TeamRepository;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private TeamRepository teamRepository;
    
    @RequestMapping(method = RequestMethod.GET)
    public String results(@RequestParam("q") String queryString, Model model) {
        List<Team> teams = new ArrayList<Team>();
        teams.addAll(teamRepository.findByNameContainingIgnoreCase(queryString));
        teams.addAll(teamRepository.findByTagsContainingIgnoreCase(queryString));
        teams.addAll(teamRepository.findByDescriptionContainingIgnoreCase(queryString));
        model.addAttribute("teams", teams);
        return "search";
    }
    
}
