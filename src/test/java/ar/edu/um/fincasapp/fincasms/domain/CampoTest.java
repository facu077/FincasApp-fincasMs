package ar.edu.um.fincasapp.fincasms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;

public class CampoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campo.class);
        Campo campo1 = new Campo();
        campo1.setId(1L);
        Campo campo2 = new Campo();
        campo2.setId(campo1.getId());
        assertThat(campo1).isEqualTo(campo2);
        campo2.setId(2L);
        assertThat(campo1).isNotEqualTo(campo2);
        campo1.setId(null);
        assertThat(campo1).isNotEqualTo(campo2);
    }
}
