package com.example.registration.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import com.example.registration.dao.UserDao;
import com.example.registration.model.AuthenticationRequest;
import com.example.registration.model.AuthenticationResponse;
import com.example.registration.model.Author;
import com.example.registration.model.Blog;
import com.example.registration.model.Role;
import com.example.registration.model.User;
import com.example.registration.service.AuthorService;
import com.example.registration.service.BlogSerivce;
import com.example.registration.service.UserDetailsServiceImpl;
import com.example.registration.service.UserService;
import com.example.registration.util.JwtUtil;

@Controller
public class BlogController {
 
	@Autowired
	private BlogSerivce blogService;
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	UserService userService;
	
	private User user;
	
	/*@RequestMapping(value={"home", "/", ""})
	public String home(Model model) {
		model.addAttribute("blogs", blogService.getAllBlogs());
		return "index.html";
	}*/
	
	@RequestMapping(value={"home", "/", ""})
	public String getAllPages(Model model) {
		return getonepage(model, 1);
	}
	
	@RequestMapping("/page/{pagenumber}")
	public String getonepage(Model model, @PathVariable("pagenumber") int currpage) {
		Page<Blog> page = blogService.getPage(currpage);
		int totalPage = page.getTotalPages();
		Long totalItem = page.getTotalElements();
		List<Blog> blog = page.getContent();
		
		String username = null;
		Set<Role> role = null;
		String authrole = null;
		if(user != null) {
			username = user.getUsername();
			 role = user.getRoles();
			 for(Role rol: role) {
				 authrole = rol.getName();
			 }
			System.out.println("Username: " + username);
		}
		
		
		
		//String authName = jwtTokenUtil.extractUsername(WebUtils.getCookie(request,"token").getValue()); 
		//Set<Role> authRole = userDao.findUserByUsername(authName).getRoles();
		
		System.out.println(username);
		//System.out.println(totalItem);
		
		model.addAttribute("currentPage", currpage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalItem", totalItem);
		model.addAttribute("blogs", blog);
		model.addAttribute("authName", username);
		//model.addAttribute("authName", authName);
		model.addAttribute("authRole", authrole);
		
		return "index.html";
	}
	
	@RequestMapping("/new")
	public String register(Model model) {
		Blog blog = new Blog();
		model.addAttribute("blog", blog);
		return "post.html";
	} 
	

	@RequestMapping("/registeruser")
	public String registeruser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration.html";
	}
	
	
	@PostMapping("/saveBlog")
	public String saveBlog(@ModelAttribute("blog") Blog blog, @RequestParam(value = "imageFile") MultipartFile file) throws IOException{
		String base64EncodedImage = Base64.encodeBase64String(file.getBytes());
		blog.setMyimage(base64EncodedImage); 
	
		String authorEmail = user.getUsername();
		String authorName = authorEmail;
		
		//System.out.println(file); 
		//System.out.println(authorEmail); 
		//System.out.println(authorName);
		 
		
		Author author1 = authorService.findAuthor(authorEmail);
		if(author1 == null) {
			Author author = new Author();
			author.setEmail(authorEmail);
			author.setName(authorName);
			authorService.saveAuthor(author);
			blog.setAuthor(author);
		}
		else {
			blog.setAuthor(author1);
		}
		blogService.saveBlog(blog);
		return "redirect:/home";
	}
	
	@PostMapping("/saveuser")
	public String saveUser(@ModelAttribute("user") User user){
			User user1 = userService.findbyname(user.getUsername());
			if(user1 == null) {
				Role role = new Role();
				role.setName("USER");
				user.getRoles().add(role);
				user.setEnabled(true);
				userService.saveuser(user);
			}
			return "redirect:/home";
	}
	
	
	@RequestMapping("/searchblog")
	public String search(Model model, @RequestParam(value = "search") String name) throws Exception {
		System.out.println("Author name : " + name);
		
		List<Blog> l = blogService.findBlog(name);
		model.addAttribute("blogs",l);
		
		return "index.html";
	}

	@RequestMapping(path = "/blog/{id}")
	public String displayFullBlog(Model model, @PathVariable(value = "id") Long id) throws Exception {
		//System.out.println(id);
		Blog blog = blogService.findBlogById(id);
		model.addAttribute("blogs", blog);
		return "blog.html";
	}

	@RequestMapping(path = "/author/{id}")
	public String displayAuthor(Model model, @PathVariable(value = "id") Long id) throws Exception {
		//System.out.println(id);
		Author auth = authorService.findAuthorByAuthorId(id);
		model.addAttribute("name", auth.getName());
		
		return "author.html";
	}
	
	@RequestMapping("/editblog/{id}")
	public String editblog(@PathVariable(value="id") Long id, Model model) {
		Blog blog  = blogService.findBlogById(id);
		model.addAttribute("blog", blog);
		return "update.html";
	}
	
	@PostMapping("/update/{id}")
	public String updateblog(@PathVariable(value = "id")Long id, Model model, Blog blog, BindingResult result, @RequestParam(value="imageFile") MultipartFile file) throws IOException {
		/*if(result.hasErrors()) {
			return "update.html";
		}
		blogService.saveBlog(blog);
		model.addAttribute("blogs", blogService.getAllBlogs());*/
		
		Blog old = blogService.findBlogById(id);
		
		String base64EncodedImage = null;
		
		base64EncodedImage = Base64.encodeBase64String(file.getBytes());
		
		blog.setMyimage(base64EncodedImage); 
		
		if(blog.getMyimage().length() == 0) {
			blog.setMyimage(old.getMyimage());
		}
	
		System.out.println(" ---last image---" + blog.getMyimage().length());
	
		
		
		String authorEmail = old.getAuthor().getEmail();
		String authorName = old.getAuthor().getName();
		
		Author author = authorService.findAuthor(authorEmail);
		
		author.setEmail(authorEmail);
		author.setName(authorName);
		
		authorService.saveAuthor(author);
		
		blog.setAuthor(author);
		
		blogService.saveBlog(blog);
		return "redirect:/home";
	}
	
	@RequestMapping("/deleteblog/{id}")
	public String deleteblog(@PathVariable(value = "id")Long id) {
		blogService.deleteblog(id);
		return "redirect:/home";
	}
	
	
	@RequestMapping("/token")
	public String generateTokenForUser(HttpServletResponse resp, HttpServletRequest req)
			throws Exception {
		System.out.println("helooo  ");
		 user = userService.findbyname(req.getUserPrincipal().getName());
		 System.out.println("UserName: "+ user.getUsername());
		 
/*		try {
			this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			

		} catch (BadCredentialsException e) {
			return "invalidInput";
		}*/
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
		System.out.println("USerdetails: "+ userDetails);
		String token = this.jwtTokenUtil.generateToken(userDetails);
		System.out.println("Token: " + token);
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(-1);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/user/");
		resp.addCookie(cookie);
		return "redirect:/home";
	}
	
	@RequestMapping("/aboutus")
	public String abouts() {
		return "about.html";
	}
	
	@RequestMapping("/contact")
	public String contactus() {
		return "contact.html";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "redirect:/login";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
	
	@RequestMapping("/403")
	public String accessdenied() {
		return "403.html";
	}
	
}	