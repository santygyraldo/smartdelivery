package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.exception.DeliveryRouteException;
import co.edu.umanizales.smartdelivery.model.DeliveryRoute;
import co.edu.umanizales.smartdelivery.service.DeliveryRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las rutas de entrega.
 * Proporciona endpoints para realizar operaciones CRUD en la entidad DeliveryRoute.
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/routes") // Define la ruta base para todos los endpoints de este controlador
public class DeliveryRouteController {

    // Inyección de dependencia del servicio
    private final DeliveryRouteService routeService;

    /**
     * Constructor que recibe el servicio por inyección de dependencias
     * @param routeService Servicio de rutas de entrega
     */
    @Autowired
    public DeliveryRouteController(DeliveryRouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Crea una nueva ruta de entrega.
     * @param route Datos de la ruta a crear
     * @return ResponseEntity con la ruta creada y código de estado 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<DeliveryRoute> createRoute(@RequestBody DeliveryRoute route) {
        // Crea la ruta utilizando el servicio
        DeliveryRoute createdRoute = routeService.createRoute(route);
        // Retorna la ruta creada con código de estado 201 (CREATED)
        return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las rutas de entrega.
     * @return ResponseEntity con la lista de rutas y código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<DeliveryRoute>> getAllRoutes() {
        // Obtiene todas las rutas del servicio
        List<DeliveryRoute> routes = routeService.getAllRoutes();
        // Retorna la lista de rutas con código de estado 200 (OK)
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    /**
     * Obtiene una ruta por su ID.
     * @param id ID de la ruta a buscar
     * @return ResponseEntity con la ruta encontrada y código de estado 200 (OK)
     * @throws DeliveryRouteException Si la ruta no se encuentra (código 404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryRoute> getRouteById(@PathVariable Long id) throws DeliveryRouteException {
        // Busca la ruta por su ID
        DeliveryRoute route = routeService.getRouteById(id);
        // Retorna la ruta encontrada con código de estado 200 (OK)
        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    /**
     * Actualiza una ruta existente.
     * @param id ID de la ruta a actualizar
     * @param updatedRoute Datos actualizados de la ruta
     * @return ResponseEntity con la ruta actualizada y código de estado 200 (OK)
     * @throws DeliveryRouteException Si la ruta no se encuentra (código 404)
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryRoute> updateRoute(
            @PathVariable Long id, 
            @RequestBody DeliveryRoute updatedRoute) throws DeliveryRouteException {
        // Actualiza la ruta con los nuevos datos
        DeliveryRoute route = routeService.updateRoute(id, updatedRoute);
        // Retorna la ruta actualizada con código de estado 200 (OK)
        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    /**
     * Elimina una ruta por su ID.
     * @param id ID de la ruta a eliminar
     * @return ResponseEntity sin contenido y código de estado 204 (NO_CONTENT)
     * @throws DeliveryRouteException Si la ruta no se encuentra (código 404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) throws DeliveryRouteException {
        // Elimina la ruta por su ID
        routeService.deleteRoute(id);
        // Retorna respuesta exitosa sin contenido (código 204)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Maneja las excepciones de tipo DeliveryRouteException.
     * @param ex Excepción capturada
     * @return ResponseEntity con el mensaje de error y código de estado 404 (NOT_FOUND)
     */
    @ExceptionHandler(DeliveryRouteException.class)
    public ResponseEntity<String> handleDeliveryRouteException(DeliveryRouteException ex) {
        // Retorna el mensaje de error con código de estado 404 (NOT_FOUND)
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
