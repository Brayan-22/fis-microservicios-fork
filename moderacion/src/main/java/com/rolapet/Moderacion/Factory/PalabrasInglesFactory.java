package com.rolapet.Moderacion.Factory;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory concreto para palabras malsonantes en inglés
 */
@Component
public class PalabrasInglesFactory extends PalabrasMalSonantesFactory {

    @Override
    protected List<PalabraProhibidaData> crearPalabrasProhibidas() {
        List<PalabraProhibidaData> palabras = new ArrayList<>();

        // ========== VULGARIDADES Y GROSERÍAS ==========
        palabras.add(new PalabraProhibidaData("fuck", "Profanity"));
        palabras.add(new PalabraProhibidaData("fucking", "Profanity"));
        palabras.add(new PalabraProhibidaData("fucker", "Profanity"));
        palabras.add(new PalabraProhibidaData("motherfucker", "Severe profanity"));
        palabras.add(new PalabraProhibidaData("shit", "Profanity"));
        palabras.add(new PalabraProhibidaData("bitch", "Profanity"));
        palabras.add(new PalabraProhibidaData("bastard", "Profanity"));
        palabras.add(new PalabraProhibidaData("asshole", "Profanity"));
        palabras.add(new PalabraProhibidaData("ass", "Profanity"));
        palabras.add(new PalabraProhibidaData("damn", "Mild profanity"));
        palabras.add(new PalabraProhibidaData("hell", "Mild profanity"));
        palabras.add(new PalabraProhibidaData("crap", "Profanity"));
        palabras.add(new PalabraProhibidaData("piss", "Profanity"));
        palabras.add(new PalabraProhibidaData("dick", "Profanity"));
        palabras.add(new PalabraProhibidaData("cock", "Profanity"));
        palabras.add(new PalabraProhibidaData("pussy", "Profanity"));
        palabras.add(new PalabraProhibidaData("cunt", "Severe profanity"));
        palabras.add(new PalabraProhibidaData("whore", "Profanity"));
        palabras.add(new PalabraProhibidaData("slut", "Profanity"));
        palabras.add(new PalabraProhibidaData("twat", "Profanity"));
        palabras.add(new PalabraProhibidaData("prick", "Profanity"));
        palabras.add(new PalabraProhibidaData("bollocks", "British profanity"));
        palabras.add(new PalabraProhibidaData("wanker", "British profanity"));
        palabras.add(new PalabraProhibidaData("bugger", "Profanity"));
        palabras.add(new PalabraProhibidaData("shithead", "Profanity"));
        palabras.add(new PalabraProhibidaData("douchebag", "Profanity"));
        palabras.add(new PalabraProhibidaData("son of a bitch", "Profanity"));
        palabras.add(new PalabraProhibidaData("goddamn", "Profanity"));

        // ========== CONTENIDO SEXUAL EXPLÍCITO ==========
        palabras.add(new PalabraProhibidaData("porn", "Adult content"));
        palabras.add(new PalabraProhibidaData("pornography", "Adult content"));
        palabras.add(new PalabraProhibidaData("xxx", "Explicit content"));
        palabras.add(new PalabraProhibidaData("sex tape", "Adult content"));
        palabras.add(new PalabraProhibidaData("anal sex", "Explicit content"));
        palabras.add(new PalabraProhibidaData("oral sex", "Explicit content"));
        palabras.add(new PalabraProhibidaData("blowjob", "Explicit content"));
        palabras.add(new PalabraProhibidaData("handjob", "Explicit content"));
        palabras.add(new PalabraProhibidaData("naked", "Adult content"));
        palabras.add(new PalabraProhibidaData("nude", "Adult content"));
        palabras.add(new PalabraProhibidaData("tits", "Profanity"));
        palabras.add(new PalabraProhibidaData("boobs", "Profanity"));
        palabras.add(new PalabraProhibidaData("nipples", "Sexual content"));
        palabras.add(new PalabraProhibidaData("penis", "Sexual content"));
        palabras.add(new PalabraProhibidaData("vagina", "Sexual content"));
        palabras.add(new PalabraProhibidaData("masturbate", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("masturbation", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("jerk off", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("cum", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("orgasm", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("ejaculate", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("penetration", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("intercourse", "Sexual content"));
        palabras.add(new PalabraProhibidaData("horny", "Sexual content"));
        palabras.add(new PalabraProhibidaData("escort", "Adult services"));
        palabras.add(new PalabraProhibidaData("prostitute", "Adult content"));
        palabras.add(new PalabraProhibidaData("hooker", "Adult content"));
        palabras.add(new PalabraProhibidaData("webcam sex", "Adult content"));
        palabras.add(new PalabraProhibidaData("onlyfans xxx", "Adult content"));

        // ========== TÉRMINOS SEXUALES MUY EXPLÍCITOS ==========
        palabras.add(new PalabraProhibidaData("gangbang", "Extreme sexual content"));
        palabras.add(new PalabraProhibidaData("bukkake", "Extreme sexual content"));
        palabras.add(new PalabraProhibidaData("creampie", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("deepthroat", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("facial", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("squirt", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("milf", "Adult content"));
        palabras.add(new PalabraProhibidaData("dildo", "Adult content"));
        palabras.add(new PalabraProhibidaData("vibrator", "Adult content"));
        palabras.add(new PalabraProhibidaData("69", "Sexual position reference"));
        palabras.add(new PalabraProhibidaData("threesome", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("foursome", "Explicit sexual content"));
        palabras.add(new PalabraProhibidaData("orgy", "Explicit sexual content"));

        // ========== INSULTOS DISCRIMINATORIOS GRAVES ==========
        palabras.add(new PalabraProhibidaData("nigger", "Extreme racial slur"));
        palabras.add(new PalabraProhibidaData("nigga", "Racial slur"));
        palabras.add(new PalabraProhibidaData("faggot", "Extreme homophobic slur"));
        palabras.add(new PalabraProhibidaData("fag", "Homophobic slur"));
        palabras.add(new PalabraProhibidaData("dyke", "Homophobic slur"));
        palabras.add(new PalabraProhibidaData("tranny", "Transphobic slur"));
        palabras.add(new PalabraProhibidaData("retard", "Ableist slur"));
        palabras.add(new PalabraProhibidaData("retarded", "Ableist slur"));
        palabras.add(new PalabraProhibidaData("spic", "Racial slur"));
        palabras.add(new PalabraProhibidaData("chink", "Racial slur"));
        palabras.add(new PalabraProhibidaData("gook", "Racial slur"));
        palabras.add(new PalabraProhibidaData("wetback", "Racial slur"));
        palabras.add(new PalabraProhibidaData("kike", "Antisemitic slur"));

        return palabras;
    }

    @Override
    public String getIdioma() {
        return "ENGLISH";
    }
}