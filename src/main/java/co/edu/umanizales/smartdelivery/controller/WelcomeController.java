package co.edu.umanizales.smartdelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {

    @GetMapping("/")
    @ResponseBody
    public String holaMundo() {
        return "¡Hola Mundo! Bienvenido a Smart Delivery";
    }
    
    @GetMapping("/saludo")
    @ResponseBody
    public String saludoPersonalizado() {
        return "¡Bienvenido a Smart Delivery! Estamos listos para atenderte.";
    }
}
