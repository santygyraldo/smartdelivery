package com.example.smartdelivery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/saludo")
    public String saludar() {
        return "¡Hola! La aplicación SmartDelivery está funcionando correctamente.";
    }
}
