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

        // ========== VULGARIDADES Y GROSERÍAS ==========
        palabras.add(new PalabraProhibidaData("puta", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("puto", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("hijo de puta", "Insulto vulgar"));
        palabras.add(new PalabraProhibidaData("hijueputa", "Insulto vulgar"));
        palabras.add(new PalabraProhibidaData("cabrón", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("coño", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("joder", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("mierda", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("verga", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("carajo", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("chingada", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("pendejo", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("marica", "Vulgaridad ofensiva"));
        palabras.add(new PalabraProhibidaData("maricón", "Vulgaridad ofensiva"));
        palabras.add(new PalabraProhibidaData("culero", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("güey", "Vulgaridad leve"));
        palabras.add(new PalabraProhibidaData("pinche", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("mamada", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("chupada", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("cojones", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("huevón", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("boludo", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("pelotudo", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("concha", "Vulgaridad"));
        palabras.add(new PalabraProhibidaData("conchesumadre", "Insulto vulgar"));
        palabras.add(new PalabraProhibidaData("ctm", "Acrónimo vulgar"));
        palabras.add(new PalabraProhibidaData("hdp", "Acrónimo vulgar"));
        palabras.add(new PalabraProhibidaData("hp", "Acrónimo vulgar"));

        // ========== CONTENIDO SEXUAL EXPLÍCITO ==========
        palabras.add(new PalabraProhibidaData("porno", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("pornografía", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("xxx", "Contenido explícito"));
        palabras.add(new PalabraProhibidaData("sexo ", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("desnudo", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("desnuda", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("tetas", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("polla", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("pene", "Contenido sexual"));
        palabras.add(new PalabraProhibidaData("vagina", "Contenido sexual"));
        palabras.add(new PalabraProhibidaData("coger", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("follar", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("culear", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("chingar", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("masturbar", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("masturbación", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("paja", "Vulgaridad sexual"));
        palabras.add(new PalabraProhibidaData("corrida", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("orgasmo", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("eyacular", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("penetrar", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("penetración", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("puta desnuda", "Contenido explícito"));
        palabras.add(new PalabraProhibidaData("escort", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("prostituta", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("webcam xxX", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("onlyfans xxx", "Contenido adulto"));

        // ========== TÉRMINOS SEXUALES MUY EXPLÍCITOS ==========
        palabras.add(new PalabraProhibidaData("gangbang", "Contenido sexual extremo"));
        palabras.add(new PalabraProhibidaData("bukkake", "Contenido sexual extremo"));
        palabras.add(new PalabraProhibidaData("creampie", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("deepthroat", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("facial", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("squirt", "Contenido sexual explícito"));
        palabras.add(new PalabraProhibidaData("milf", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("dildo", "Contenido adulto"));
        palabras.add(new PalabraProhibidaData("vibrador", "Contenido adulto"));

        return palabras;
    }

    @Override
    public String getIdioma() {
        return "ESPAÑOL";
    }
}