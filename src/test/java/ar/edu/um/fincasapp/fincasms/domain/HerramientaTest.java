package ar.edu.um.fincasapp.fincasms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;

public class HerramientaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Herramienta.class);
        Herramienta herramienta1 = new Herramienta();
        herramienta1.setId(1L);
        Herramienta herramienta2 = new Herramienta();
        herramienta2.setId(herramienta1.getId());
        assertThat(herramienta1).isEqualTo(herramienta2);
        herramienta2.setId(2L);
        assertThat(herramienta1).isNotEqualTo(herramienta2);
        herramienta1.setId(null);
        assertThat(herramienta1).isNotEqualTo(herramienta2);
    }
}
