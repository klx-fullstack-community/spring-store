package klx.tech.community.workshop.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource datasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        // dataSource.setUrl("jdbc:sqlite:database.store");
        dataSource.setUrl("jdbc:sqlite:database/store.db");
        return dataSource;
    }
}
