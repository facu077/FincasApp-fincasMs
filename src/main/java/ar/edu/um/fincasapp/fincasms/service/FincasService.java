package ar.edu.um.fincasapp.fincasms.service;

import ar.edu.um.fincasapp.fincasms.domain.Finca;
import ar.edu.um.fincasapp.fincasms.repository.FincaRepository;
import ar.edu.um.fincasapp.fincasms.security.SecurityUtils;
import ar.edu.um.fincasapp.fincasms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FincasService {

    private static final String ENTITY_NAME = "fincasMsFinca";

    private final Logger log = LoggerFactory.getLogger(FincasService.class);
    private final FincaRepository fincaRepository;

    public FincasService(FincaRepository fincaRepository) {
        this.fincaRepository = fincaRepository;
    }

    public Finca addFinca(Finca finca)
    {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse("");
        if (currentLogin.equals(""))
        {
            throw new AuthorizationServiceException("Unauthorized");
        }
        if (finca.getId() != null)
        {
            throw new BadRequestAlertException("A new finca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        finca.setUserLogin(currentLogin);
        Finca result = fincaRepository.save(finca);
        return result;
    }

    public List<Finca> getCurrentUserFincas()
    {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse("");
        if (currentLogin.equals(""))
        {
            throw new AuthorizationServiceException("Unauthorized");
        }
        List<Finca> page = fincaRepository.findByUserLogin(currentLogin);
        return page;
    }

}
