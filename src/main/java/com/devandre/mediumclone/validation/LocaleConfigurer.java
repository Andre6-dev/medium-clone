package com.devandre.mediumclone.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Andre on 15/02/2024
 * @project medium-clone
 */
@Component
@Slf4j
public class LocaleConfigurer implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * Este metodo se usa para configurar el locale de la aplicacion
     * @param event: Evento que se dispara cuando la aplicacion se inicia
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("LocaleConfigurer.onApplicationEvent");
        Locale.setDefault(Locale.ENGLISH);
    }
}
