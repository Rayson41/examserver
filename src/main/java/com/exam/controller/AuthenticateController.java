package com.exam.controller;

import com.exam.config.JwtUtils;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.model.User;
import com.exam.service.impl.UserdetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserdetailsServiceImpl userdetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){
        boolean f = false;
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

            f=true;


        }catch (UsernameNotFoundException e){
            f=false;
            e.printStackTrace();
            System.out.println("User Not Found");
        } catch (Exception e) {
            f=false;
            e.printStackTrace();
        }

        if(f){
            UserDetails userDetails = this.userdetailsService.loadUserByUsername(jwtRequest.getUsername());
            String token = this.jwtUtils.generateToken(userDetails);
//            System.out.println("User Details "+userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        }
        else {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }

    }

    private void authenticate(String username, String password) throws Exception {

    try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

    }catch (DisabledException e){
        throw new Exception("USER DISABLED "+e.getMessage());
    }catch (BadCredentialsException e){
        throw new Exception("Invalid Credentials "+e.getMessage());
    }
    }

    @GetMapping("/current-user")
    public User getCurrentUser(Principal principal){
        return (User) this.userdetailsService.loadUserByUsername(principal.getName());
    }


}
