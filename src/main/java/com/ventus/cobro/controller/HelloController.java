package com.ventus.cobro.controller;

import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    Logger logger = LogManager.getLogger(HelloController.class);
    
    @RequestMapping("/")
    String hellow() {
        StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        String text = interpolator.replace(
                "Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}\n" +
                        "Base64 Encoder:        ${base64Encoder:HelloWorld!}\n");

        logger.info(text);
        logger.info("Dentro de la app maintest");
        return "Hello World!";
    }

    @RequestMapping("/procesa")
    String procesa() {
        return "Procesado Terminado test version 2!";
    }

    @RequestMapping("/getStatus")
    String getStatus() {
        return "success";
    }
}
