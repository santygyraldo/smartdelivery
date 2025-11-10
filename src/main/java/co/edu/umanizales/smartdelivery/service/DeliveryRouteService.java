package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.exception.DeliveryRouteException;
import co.edu.umanizales.smartdelivery.model.DeliveryRoute;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para la gestión de rutas de entrega.
 * Maneja la lógica de negocio relacionada con las rutas de entrega.
 */
@Service
public class DeliveryRouteService {
    // Lista en memoria para almacenar las rutas de entrega
    private final List<DeliveryRoute> routes = new ArrayList<>();
    // Contador atómico para generar IDs únicos
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Crea una nueva ruta de entrega.
     * @param route La ruta a crear
     * @return La ruta creada con su ID asignado
     */
    public DeliveryRoute createRoute(DeliveryRoute route) {
        route.setId(idCounter.getAndIncrement());
        routes.add(route);
        return route;
    }

    /**
     * Obtiene todas las rutas de entrega.
     * @return Lista de todas las rutas
     */
    public List<DeliveryRoute> getAllRoutes() {
        return new ArrayList<>(routes);
    }

    /**
     * Obtiene una ruta por su ID.
     * @param id ID de la ruta a buscar
     * @return La ruta encontrada
     * @throws DeliveryRouteException Si la ruta no existe
     */
    public DeliveryRoute getRouteById(Long id) throws DeliveryRouteException {
        return routes.stream()
                .filter(route -> route.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DeliveryRouteException("Ruta no encontrada con ID: " + id));
    }

    /**
     * Actualiza una ruta existente.
     * @param id ID de la ruta a actualizar
     * @param updatedRoute Datos actualizados de la ruta
     * @return La ruta actualizada
     * @throws DeliveryRouteException Si la ruta no existe
     */
    public DeliveryRoute updateRoute(Long id, DeliveryRoute updatedRoute) throws DeliveryRouteException {
        DeliveryRoute existingRoute = getRouteById(id);
        existingRoute.setOrigin(updatedRoute.getOrigin());
        existingRoute.setDestination(updatedRoute.getDestination());
        return existingRoute;
    }

    /**
     * Elimina una ruta por su ID.
     * @param id ID de la ruta a eliminar
     * @throws DeliveryRouteException Si la ruta no existe
     */
    public void deleteRoute(Long id) throws DeliveryRouteException {
        DeliveryRoute routeToRemove = getRouteById(id);
        routes.remove(routeToRemove);
    }
}
