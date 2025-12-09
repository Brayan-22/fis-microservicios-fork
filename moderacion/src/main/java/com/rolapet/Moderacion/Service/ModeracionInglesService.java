package com.rolapet.Moderacion.Service;

import com.rolapet.Moderacion.Factory.PalabrasInglesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("moderacionIngles")
public class ModeracionInglesService extends ModeracionServiceBase {

    @Autowired
    public ModeracionInglesService(PalabrasInglesFactory factory) {
        super(factory);
    }

    @Override
    public String getIdioma() {
        return "ENGLISH";
    }
}
