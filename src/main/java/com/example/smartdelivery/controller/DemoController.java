package com.example.smartdelivery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String holaMundo() {
        return "Hola mundo, esto es Smart Dalivery";
    }
    
    @GetMapping("/api/saludo")
    public String saludar() {
        return "¡Hola! La aplicación SmartDelivery está funcionando correctamente.";
    }
}
