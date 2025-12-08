package uni.fis.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    private Integer userId; 
    private String to;
    private String subject;
    private String body;
    
    @JsonProperty("isHtml")
    private Boolean html;   
    
    public boolean isHtml() {
        return Boolean.TRUE.equals(html);  
    }
}