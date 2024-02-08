package ru.clevertec.ecl.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import ru.clevertec.ecl.exeption.DefaultExceptionHandler;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void shouldReturnDefaultExceptionHandlerBeanIfEnabledTrue() {
        contextRunner.withPropertyValues("exception.handler.enabled=true")
                .withUserConfiguration(ExceptionHandlerAutoConfiguration.class)
                .run(context ->
                        assertThat(context).hasSingleBean(DefaultExceptionHandler.class)
                );
    }

    @Test
    public void shouldCheckNotCreateDefaultExceptionHandlerBeanIfEnabledFalse() {
        contextRunner.withPropertyValues("exception.handler.enabled=false")
                .withUserConfiguration(ExceptionHandlerAutoConfiguration.class)
                .run(context ->
                        assertThat(context).doesNotHaveBean(DefaultExceptionHandler.class)
                );
    }
}

