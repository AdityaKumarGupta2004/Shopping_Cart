package com.ecom.Shopping_cart.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.Shopping_cart.model.ProductOrder;
import com.ecom.Shopping_cart.model.UserDtls;
import com.ecom.Shopping_cart.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    // ------------------ PASSWORD RESET MAIL ------------------
    public Boolean sendMail(String url, String recipientEmail)
            throws UnsupportedEncodingException, MessagingException {

        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            System.out.println("Recipient email is empty, cannot send password reset mail.");
            return false;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("daspabitra55@gmail.com", "Shopping Cart");
        helper.setTo(recipientEmail);

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + url + "\">Change my password</a></p>";

        helper.setSubject("Password Reset");
        helper.setText(content, true);

        mailSender.send(message);
        return true;
    }

    // ------------------ BASE URL GENERATOR ------------------
    public static String generateUrl(HttpServletRequest request) {

        // Example:
        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        // returns:
        // http://localhost:8080
        return siteUrl.replace(request.getServletPath(), "");
    }

    // ------------------ ORDER STATUS MAIL ------------------
    public Boolean sendMailForProductOrder(ProductOrder order, String status) {

        try {

            if (order == null) {
                System.out.println("Order is null, cannot send mail.");
                return false;
            }

            if (order.getOrderAddress() == null) {
                System.out.println("OrderAddress is null, cannot send mail.");
                return false;
            }

            String toEmail = order.getOrderAddress().getEmail();

            if (toEmail == null || toEmail.trim().isEmpty()) {
                System.out.println("Order email is empty, cannot send mail.");
                return false;
            }

            if (order.getProduct() == null) {
                System.out.println("Product is null, cannot send mail.");
                return false;
            }

            // Message Template (LOCAL VARIABLE - SAFE)
            String msg = "<p>Hello [[name]],</p>"
                    + "<p>Your order is <b>[[orderStatus]]</b>.</p>"
                    + "<p><b>Product Details:</b></p>"
                    + "<p>Name : [[productName]]</p>"
                    + "<p>Category : [[category]]</p>"
                    + "<p>Quantity : [[quantity]]</p>"
                    + "<p>Price : [[price]]</p>"
                    + "<p>Payment Type : [[paymentType]]</p>";

            // Replace placeholders
            msg = msg.replace("[[name]]", safe(order.getOrderAddress().getFirstName()));
            msg = msg.replace("[[orderStatus]]", safe(status));
            msg = msg.replace("[[productName]]", safe(order.getProduct().getTitle()));
            msg = msg.replace("[[category]]", safe(order.getProduct().getCategory()));
            msg = msg.replace("[[quantity]]", safe(order.getQuantity()));
            msg = msg.replace("[[price]]", safe(order.getPrice()));
            msg = msg.replace("[[paymentType]]", safe(order.getPaymentType()));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("daspabitra55@gmail.com", "Shopping Cart");
            helper.setTo(toEmail);

            helper.setSubject("Product Order Status");
            helper.setText(msg, true);

            System.out.println("Sending order status mail to: " + toEmail);

            mailSender.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}

    // ------------------ SAFE CONVERTERS ------------------
    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }
}
