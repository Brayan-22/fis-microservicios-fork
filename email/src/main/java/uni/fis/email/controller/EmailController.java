package uni.fis.email.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.fis.email.dto.EmailRequest;
import uni.fis.email.dto.EmailResponse;
import uni.fis.email.entity.EmailLog;
import uni.fis.email.service.EmailService;


@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            System.out.println("Usuario autenticado: " + username);
            System.out.println("Roles: " + authentication.getAuthorities());

            EmailResponse response = emailRequest.isHtml()
                    ? emailService.sendHtmlEmail(emailRequest)
                    : emailService.sendEmail(emailRequest);

            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Datos inválidos",
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al procesar la solicitud",
                            "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<EmailLog>> getAllLogs() {
        return ResponseEntity.ok(emailService.getAllEmailLogs());
    }

    @GetMapping("/logs/recipient/{recipient}")
    public ResponseEntity<List<EmailLog>> getLogsByRecipient(@PathVariable String recipient) {
        return ResponseEntity.ok(emailService.getEmailLogsByRecipient(recipient));
    }

    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<EmailLog>> getLogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(emailService.getEmailLogsByUserId(userId));
    }

    @GetMapping("/logs/status/{status}")
    public ResponseEntity<List<EmailLog>> getLogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(emailService.getEmailLogsByStatus(status));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Email service is running");
    }
}
