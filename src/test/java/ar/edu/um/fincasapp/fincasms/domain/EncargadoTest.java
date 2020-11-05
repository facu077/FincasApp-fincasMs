package ar.edu.um.fincasapp.fincasms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;

public class EncargadoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Encargado.class);
        Encargado encargado1 = new Encargado();
        encargado1.setId(1L);
        Encargado encargado2 = new Encargado();
        encargado2.setId(encargado1.getId());
        assertThat(encargado1).isEqualTo(encargado2);
        encargado2.setId(2L);
        assertThat(encargado1).isNotEqualTo(encargado2);
        encargado1.setId(null);
        assertThat(encargado1).isNotEqualTo(encargado2);
    }
}
