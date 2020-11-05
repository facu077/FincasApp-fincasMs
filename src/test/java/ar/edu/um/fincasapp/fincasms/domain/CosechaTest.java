package ar.edu.um.fincasapp.fincasms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;

public class CosechaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cosecha.class);
        Cosecha cosecha1 = new Cosecha();
        cosecha1.setId(1L);
        Cosecha cosecha2 = new Cosecha();
        cosecha2.setId(cosecha1.getId());
        assertThat(cosecha1).isEqualTo(cosecha2);
        cosecha2.setId(2L);
        assertThat(cosecha1).isNotEqualTo(cosecha2);
        cosecha1.setId(null);
        assertThat(cosecha1).isNotEqualTo(cosecha2);
    }
}
