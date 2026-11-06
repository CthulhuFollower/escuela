package com.julian.escuela;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class EscuelaApplicationTests {
	@Autowired
	DataSource dataSource;

	@Test
	void contexto_perfilTest_usaH2() throws SQLException {
		// Arrange
		String prefijoEsperado = "jdbc:h2:";

		// Act
		String url;
		try (Connection conexion = dataSource.getConnection()) {
			url = conexion.getMetaData().getURL();
		}

		// Assert
		assertThat(url).startsWith(prefijoEsperado);
	}

}
