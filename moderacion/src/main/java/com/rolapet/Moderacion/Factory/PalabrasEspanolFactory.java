package com.rolapet.Moderacion.Factory;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory concreto para palabras malsonantes en español
 */
@Component
public class PalabrasEspanolFactory extends PalabrasMalSonantesFactory {

    @Override
    protected List<PalabraProhibidaData> crearPalabrasProhibidas() {
        List<PalabraProhibidaData> palabras = new ArrayList<>();

        // ========== SPAM Y CONTENIDO NO DESEADO ==========
        palabras.add(new PalabraProhibidaData("spam", "Contenido repetitivo no deseado"));
        palabras.add(new PalabraProhibidaData("publicidad", "Promoción no autorizada"));
        palabras.add(new PalabraProhibidaData("clickbait", "Contenido engañoso para clicks"));
        palabras.add(new PalabraProhibidaData("bot", "Actividad automatizada sospechosa"));

        // ========== FRAUDE Y ESTAFAS ==========
        palabras.add(new PalabraProhibidaData("phishing", "Intento de robo de información"));
        palabras.add(new PalabraProhibidaData("piramidal", "Esquema piramidal ilegal"));


        // ========== SEGURIDAD Y HACKEO ==========
        palabras.add(new PalabraProhibidaData("hack", "Actividad de hackeo"));
        palabras.add(new PalabraProhibidaData("hackear", "Intento de vulnerar seguridad"));
        palabras.add(new PalabraProhibidaData("crackear", "Romper protecciones de software"));
        palabras.add(new PalabraProhibidaData("keylogger", "Software malicioso"));
        palabras.add(new PalabraProhibidaData("malware", "Software dañino"));
        palabras.add(new PalabraProhibidaData("virus", "Software malicioso"));
        palabras.add(new PalabraProhibidaData("ransomware", "Software de extorsión"));

        // ========== DROGAS Y SUSTANCIAS ILEGALES ==========
        palabras.add(new PalabraProhibidaData("drogas", "Sustancias ilegales"));
        palabras.add(new PalabraProhibidaData("narcóticos", "Sustancias controladas"));
        palabras.add(new PalabraProhibidaData("marihuana", "Venta ilegal de sustancias"));
        palabras.add(new PalabraProhibidaData("cocaína", "Droga ilegal"));

        // ========== ARMAS Y VIOLENCIA ==========
        palabras.add(new PalabraProhibidaData("armas", "Comercio ilegal de armas"));
        palabras.add(new PalabraProhibidaData("bomba", "Amenaza de violencia"));
        palabras.add(new PalabraProhibidaData("explosivo", "Material peligroso"));
        palabras.add(new PalabraProhibidaData("asesinar", "Incitación a la violencia"));
        palabras.add(new PalabraProhibidaData("matar", "Amenaza grave"));

        // ========== CONTENIDO PARA ADULTOS ==========
        palabras.add(new PalabraProhibidaData("xxx", "Contenido explícito"));
        palabras.add(new PalabraProhibidaData("porno", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("sexo", "Contenido inapropiado"));
        palabras.add(new PalabraProhibidaData("desnudos", "Contenido no permitido"));

        // ========== DISCRIMINACIÓN Y ODIO ==========
        palabras.add(new PalabraProhibidaData("racista", "Discurso de odio"));
        palabras.add(new PalabraProhibidaData("discriminación", "Contenido discriminatorio"));
        palabras.add(new PalabraProhibidaData("xenofobia", "Odio por nacionalidad"));
        palabras.add(new PalabraProhibidaData("homofobia", "Discriminación LGBTQ+"));
        palabras.add(new PalabraProhibidaData("nazi", "Ideología de odio"));

        // ========== ACOSO Y BULLYING ==========
        palabras.add(new PalabraProhibidaData("acoso", "Hostigamiento"));
        palabras.add(new PalabraProhibidaData("bullying", "Intimidación"));
        palabras.add(new PalabraProhibidaData("amenaza", "Contenido amenazante"));
        palabras.add(new PalabraProhibidaData("stalkear", "Acoso persistente"));
        palabras.add(new PalabraProhibidaData("doxxing", "Publicación de información privada"));

        // ========== INFORMACIÓN PERSONAL ==========
        palabras.add(new PalabraProhibidaData("contraseña", "Información de seguridad"));
        palabras.add(new PalabraProhibidaData("cvv", "Datos de tarjeta bancaria"));

        // ========== CONTENIDO ENGAÑOSO ==========
        palabras.add(new PalabraProhibidaData("conspiración", "Teoría no verificada"));
        palabras.add(new PalabraProhibidaData("cura milagrosa", "Afirmación médica falsa"));

        // ========== ACTIVIDADES ILEGALES ==========
        palabras.add(new PalabraProhibidaData("evasión fiscal", "Delito financiero"));
        palabras.add(new PalabraProhibidaData("falsificación", "Producción de documentos falsos"));
        palabras.add(new PalabraProhibidaData("contrabando", "Comercio ilegal"));
        palabras.add(new PalabraProhibidaData("tráfico", "Actividad ilegal de transporte"));

        // ========== MANIPULACIÓN DE PLATAFORMA ==========
        palabras.add(new PalabraProhibidaData("comprar seguidores", "Manipulación de métricas"));
        palabras.add(new PalabraProhibidaData("comprar likes", "Fraude de engagement"));
        palabras.add(new PalabraProhibidaData("comprar reviews", "Opiniones falsas"));
        palabras.add(new PalabraProhibidaData("likes automáticos", "Bot de interacciones"));

        return palabras;
    }

    @Override
    public String getIdioma() {
        return "ESPAÑOL";
    }
}