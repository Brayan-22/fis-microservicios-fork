package uni.fis.vehiculo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.fis.vehiculo.entities.VehiculoEntity;
import uni.fis.vehiculo.repository.VehiculoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private VehiculoRepository repository;

    @Autowired
    public VehiculoServiceImpl(VehiculoRepository repository) {
        this.repository = repository;
    }


    @Override
    public VehiculoEntity registrarVehiculo(VehiculoEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<VehiculoEntity> findVehiculosByUserId(Integer userId) {
        return repository.findByIdUsuario(userId);
    }
}
