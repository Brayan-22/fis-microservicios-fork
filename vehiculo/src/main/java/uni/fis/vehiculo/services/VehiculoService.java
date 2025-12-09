package uni.fis.vehiculo.services;


import uni.fis.vehiculo.entities.VehiculoEntity;

import java.util.List;

public interface VehiculoService {

    VehiculoEntity registrarVehiculo(VehiculoEntity entity);

    List<VehiculoEntity> findVehiculosByUserId(Integer userId);
}
