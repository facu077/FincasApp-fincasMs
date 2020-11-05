package ar.edu.um.fincasapp.fincasms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;

public class UbicacionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ubicacion.class);
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setId(1L);
        Ubicacion ubicacion2 = new Ubicacion();
        ubicacion2.setId(ubicacion1.getId());
        assertThat(ubicacion1).isEqualTo(ubicacion2);
        ubicacion2.setId(2L);
        assertThat(ubicacion1).isNotEqualTo(ubicacion2);
        ubicacion1.setId(null);
        assertThat(ubicacion1).isNotEqualTo(ubicacion2);
    }
}
