package wad.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.FriendshipRequest;
import wad.domain.FriendshipRequest.Status;
import wad.domain.Person;
import wad.domain.Post;
import wad.repository.FriendshipRequestRepository;
import wad.repository.PersonRepository;
import wad.repository.PostRepository;
import wad.service.LikeService;
import wad.service.PersonService;
import wad.service.TeamService;

@Controller
@RequestMapping("*")
public class DefaultController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FriendshipRequestRepository friendshipRequestRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private TeamService teamService;
    
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLogin(Model model) {
        return "login";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String viewSignup(Model model) {
        return "signup";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        /*PageRequest pr = new PageRequest(0, 10, Sort.Direction.DESC, "date");

        Person self = personService.getAuthenticatedPerson();
        model.addAttribute("self", self);

        List<FriendshipRequest> friends = friendshipRequestRepository.findBySourceOrTargetAndStatus(self, self, Status.ACCEPTED);
        Set<Person> persons = new HashSet<>();
        for (FriendshipRequest friend : friends) {
            persons.add(friend.getSource());
            persons.add(friend.getTarget());
        }

        if (!persons.isEmpty()) {
            List<Post> posts = postRepository.findByAuthorIn(persons, pr);
            model.addAttribute("posts", posts);
        }

        pr = new PageRequest(0, 10, Sort.Direction.DESC, "lastUpdated");
        List<Person> userList = new ArrayList<>(personRepository.findAll(pr).getContent());
        userList.remove(self);
        model.addAttribute("users", userList);

        List<FriendshipRequest> requests = friendshipRequestRepository.findByTargetAndStatus(self, Status.REQUESTED);
        if (requests != null && requests.size() > 0) {
            model.addAttribute("friendshipRequests", requests);
        }*/
        
        model.addAttribute("myteamname", teamService.getAuthenticatedTeamName());

        return "indexCollab";
    }
    
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
