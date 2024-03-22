package com.airosa.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Avisa o spring que é pra iniciar essa classe junto com a Aplicação.
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    /*
     * Cors - Sistema de Segurança
     * 
     * Configuração do springboot ou outros tipos de servidor
     * http, ou express do node, tem esse tipo de configuração
     * do CORS. No caso do spring ele ja vem configurado, e a 
     * gente tem que fazer algumas leves modificações, para a
     * gente conectar o nosso FRONT-END com a nossa BACK_END(API).
     * 
     * Vamos fazer uma configuração bem basica para liberar 
     * totalmente qualquer lado que vier a requisição, vai ser uma
     * segurança minima, no caso do POSTMAN ele consegue burla essa 
     * segurança, mas se a gente tentar conectar nosso FRONT-END em
     * JAVASCRIPT com a nossa API, vai da um erro de CORS.
     */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
         * Diz que qualquer requisição que vier de fora,
         * vai libera apartir dessa rota "/", tudo a frente
         * do / depois do 8080, estara liberado para ser
         * acessado.
         * 
         * Se a gente não colocar essa configuração aqui, nossa
         * API vai bloquear qualquer tipo de requisição vindo de
         * qualquer controller nosso.
         * 
         * addMapping("").allowedMethods() ->  Diz os metodos 
         * permitidos, se é o PUT POST GET...
         * 
         * .allowedOrigins() -> Diz EXP: somente a origin locahost
         * consiga acessar, outro site ou sistema que seja ip diferente
         * nao consegue acessar. Utiliza isso muito em cluster de uma
         * aplicação, que so os front-end dele consegue comunicar com
         * o bakcend dele e etc...
         */
        registry.addMapping("/**");
    }

}
