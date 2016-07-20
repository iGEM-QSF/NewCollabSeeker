package wad.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Searcher;
import wad.domain.Team;
import wad.repository.TeamRepository;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private TeamRepository teamRepository;
    
    private final String indexDir = "./src/main/webapp/static/index/";
    //private final String dataDir = "./src/main/webapp/static/data/";
    
    private Searcher searcher;
    
    @RequestMapping(method = RequestMethod.GET)
    public String results(@RequestParam("q") String searchQuery, Model model) throws IOException, ParseException {
        //List<Team> teams = new ArrayList<Team>();
        //teams.addAll(teamRepository.findByNameContainingIgnoreCase(queryString));
        //teams.addAll(teamRepository.findByTagsContainingIgnoreCase(queryString));
        //teams.addAll(teamRepository.findByDescriptionContainingIgnoreCase(queryString));
        this.searcher = new Searcher(this.indexDir);
        TopDocs hits = this.searcher.search(searchQuery);
        List<Team> teams = new ArrayList<Team>();
        
        for (ScoreDoc hit : hits.scoreDocs) {
            Document doc = this.searcher.getDocument(hit);
            String filename = doc.get("filename");
            Team team = teamRepository.findByName(filename);
            if (team != null) {
                teams.add(team);
            }
            //Team newTeam = new Team();
            //newTeam.setName(doc.get("filename"));
            //newTeam.setDescription(doc.get("contents"));
            //teams.add(newTeam);
        }
        
        model.addAttribute("teams", teams);
        return "search";
    }
    
}
