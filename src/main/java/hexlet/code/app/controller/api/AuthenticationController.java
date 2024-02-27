package hexlet.code.app.controller.api;

import hexlet.code.app.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.app.dto.AuthDTO;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String create(@RequestBody AuthDTO authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());

        authenticationManager.authenticate(authentication);
        return jwtUtils.generateToken(authRequest.getUsername());
    }
}
