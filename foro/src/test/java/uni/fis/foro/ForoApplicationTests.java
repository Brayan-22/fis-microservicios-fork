package uni.fis.foro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uni.fis.foro.dto.CrearForoDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ForoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearForo() throws Exception {

        CrearForoDTO dto = new CrearForoDTO();
        dto.setNombre("Foro de Prueba");

        mockMvc.perform(
                post("/api/foros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk())                      // Verifica que el endpoint responda 200
        .andExpect(jsonPath("$.id").exists())            // El foro creado debe tener un ID
        .andExpect(jsonPath("$.nombre").value("Foro de Prueba"));  // Confirma el nombre
    }
}
