package uni.fis.email.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailResponse {
    
    private boolean success;
    private String message;
    private Integer logId;
    private LocalDateTime timestamp;
}