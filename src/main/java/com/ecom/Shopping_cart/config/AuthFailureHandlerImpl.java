package com.ecom.Shopping_cart.config;

import java.io.IOException;

import com.ecom.Shopping_cart.model.UserDtls;
import com.ecom.Shopping_cart.repository.UserRepository;
import com.ecom.Shopping_cart.service.UserService;
import com.ecom.Shopping_cart.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");

        // ✅ If username/email is empty
        if (email == null || email.trim().isEmpty()) {
            super.setDefaultFailureUrl("/signin?error");
            super.onAuthenticationFailure(request, response, exception);
            return;
        }

        UserDtls userDtls = userRepository.findByEmail(email);

        // ✅ IMPORTANT: if user not found, don't do getIsEnable()
        if (userDtls == null) {
            super.setDefaultFailureUrl("/signin?error");
            super.onAuthenticationFailure(request, response, exception);
            return;
        }

        // If user is inactive
        if (!userDtls.getIsEnable()) {
            exception = new LockedException("your account is inactive");
        }
        // If user is active
        else {

            // If account is not locked
            if (userDtls.getAccountNonLocked()) {

                if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                    userService.increaseFailedAttempt(userDtls);
                    exception = new LockedException("failed attempt " + userDtls.getFailedAttempt());
                } else {
                    userService.userAccountLock(userDtls);
                    exception = new LockedException("Your account is locked !! failed attempt 3");
                }

            }
            // If account is locked
            else {

                if (userService.unlockAccountTimeExpired(userDtls)) {
                    exception = new LockedException("Your account is unlocked !! Please try to login");
                } else {
                    exception = new LockedException("your account is Locked !! Please try after sometimes");
                }
            }
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
