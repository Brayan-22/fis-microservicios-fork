package com.rolapet.Moderacion.Factory;

import java.util.List;

/**
 * Factory abstracto para inicializar palabras malsonantes según el idioma.
 * Implementa el patrón Factory Method.
 */
public abstract class PalabrasMalSonantesFactory {

    /**
     * Template Method que define el proceso de inicialización
     */
    public final List<PalabraProhibidaData> inicializarPalabras() {
        List<PalabraProhibidaData> palabras = crearPalabrasProhibidas();
        validarPalabras(palabras);
        return palabras;
    }

    /**
     * Factory Method - Debe ser implementado por cada idioma específico
     */
    protected abstract List<PalabraProhibidaData> crearPalabrasProhibidas();

    /**
     * Hook Method - Permite validaciones adicionales (opcional)
     */
    protected void validarPalabras(List<PalabraProhibidaData> palabras) {
        if (palabras == null || palabras.isEmpty()) {
            throw new IllegalStateException("La lista de palabras no puede estar vacía");
        }
    }

    /**
     * Retorna el idioma que maneja este factory
     */
    public abstract String getIdioma();

    /**
     * Clase interna para transportar datos de palabras prohibidas
     */
    public static class PalabraProhibidaData {
        private final String palabra;
        private final String descripcion;

        public PalabraProhibidaData(String palabra, String descripcion) {
            this.palabra = palabra;
            this.descripcion = descripcion;
        }

        public String getPalabra() {
            return palabra;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
