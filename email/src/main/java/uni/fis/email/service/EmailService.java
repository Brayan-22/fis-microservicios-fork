package uni.fis.email.service;

import java.util.List;

import uni.fis.email.dto.EmailRequest;
import uni.fis.email.dto.EmailResponse;
import uni.fis.email.entity.EmailLog;

public interface EmailService {
    
    EmailResponse sendEmail(EmailRequest emailRequest);
    
    EmailResponse sendHtmlEmail(EmailRequest emailRequest);
    
    List<EmailLog> getAllEmailLogs();
    
    List<EmailLog> getEmailLogsByUserId(Long userId);
    
    List<EmailLog> getEmailLogsByRecipient(String recipient);
    
    List<EmailLog> getEmailLogsByStatus(String status);
}