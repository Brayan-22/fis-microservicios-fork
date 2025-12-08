package uni.fis.email.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.email.entity.EmailLog;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    
    List<EmailLog> findByUserId(Long userId);
    
    List<EmailLog> findByRecipiente(String recipient);
    
    List<EmailLog> findByStatus(String status);
    
    List<EmailLog> findByUserIdAndStatus(Long userId, String status);
    
    List<EmailLog> findBySentAtBetween(LocalDateTime start, LocalDateTime end);
}