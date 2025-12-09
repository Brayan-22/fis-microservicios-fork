package com.rolapet.Moderacion.Service;

import com.rolapet.Moderacion.Factory.PalabrasEspanolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("moderacionEspanol")
public class ModeracionEspanolService extends ModeracionServiceBase {

    @Autowired
    public ModeracionEspanolService(PalabrasEspanolFactory factory) {
        super(factory);
    }

    @Override
    public String getIdioma() {
        return "ESPAÑOL";
    }
}