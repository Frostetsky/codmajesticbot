package cod.majestic.discord.bot.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["cod.majestic.discord.bot.repository"])
class JpaConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties(): DataSourceProperties = DataSourceProperties()

    @Bean
    fun dataSource(dataSourceProperties: DataSourceProperties): DataSource = DriverManagerDataSource().apply {
        url = dataSourceProperties.url
        username = dataSourceProperties.username
        password = dataSourceProperties.password
        setDriverClassName(dataSourceProperties.driverClassName)
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("cod.majestic.discord.bot.model")

        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setShowSql(true)
        vendorAdapter.setGenerateDdl(false)
        em.jpaVendorAdapter = vendorAdapter

        return em
    }
}