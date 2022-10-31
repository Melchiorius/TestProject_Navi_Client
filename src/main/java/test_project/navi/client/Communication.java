package test_project.navi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test_project.navi.client.entity.Route;
import test_project.navi.client.entity.Vehicle;
import test_project.navi.client.entity.VehiclePOJO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Communication {

    private final String URL = "http://localhost:8080/";

    @Autowired
    private RestTemplate restTemplate;


    public List<Route> getAllRoutes(){
        ResponseEntity<List<Route>> responseEntity = restTemplate
                .exchange(URL+"routes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Route>>() {});
        List<Route> routes = responseEntity.getBody();
        return routes;
    }

    public Route getRoute(int id){
        Route route = restTemplate.getForObject(URL+"routes"+"/"+id,Route.class);
        return route;
    }

    public void saveRoute(Route route){
        int id = route.getId();
        if(id==0) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL+"routes", route, String.class);
            System.out.println("New route was added to database");
            System.out.println(responseEntity.getBody());
            return;
        }
        restTemplate.put(URL+"routes", route);
        System.out.println("Route with ID = "+id+" was updated");
    }

    public void deleteRoute(int id){
        restTemplate.delete(URL+"routes"+"/"+id);
        System.out.println("Route with ID = "+id+" was deleted");
    }



    public List<Vehicle> getAllVehicle(){
        ResponseEntity<List<VehiclePOJO>> responseEntity = restTemplate
                .exchange(URL+"vehicles", HttpMethod.GET, null, new ParameterizedTypeReference<List<VehiclePOJO>>() {});
        List<VehiclePOJO> list = responseEntity.getBody();
        List<Vehicle> vehicles = list.stream().map(pojo -> new Vehicle(pojo)).collect(Collectors.toList());
        return vehicles;
    }

    public Vehicle getVehicle(int id){
        VehiclePOJO v = restTemplate.getForObject(URL+"vehicles"+"/"+id, VehiclePOJO.class);
        Vehicle vehicle = new Vehicle(v);
        return vehicle;
    }

    public void saveVehicle(Vehicle vehicle){
        int id = vehicle.getPOJO().getId();
        if(id==0) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL+"vehicles", vehicle.getPOJO(), String.class);
            System.out.println("New vehicle was added to database");
            System.out.println(responseEntity.getBody());
            return;
        }
        restTemplate.put(URL+"vehicles", vehicle.getPOJO());
        System.out.println("Vehicle with ID = "+id+" was updated");
    }

    public void deleteVehicle(int id){
        restTemplate.delete(URL+"vehicles"+"/"+id);
        System.out.println("Vehicle with ID = "+id+" was deleted");
    }

}
