package com.matjazz.car;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;

@Controller
public class CarController {
    long freeId=4;
    long delId;
    long modId;

    Car carById = new Car();
    Car zeroCar = new Car( 0, "Mark", "Model", "Color");
    private List<Car> cars;
    private List<Long> waitingId = new ArrayList<>();

    public CarController() {
        Car car1 = new Car(1L, "Honda", "Civic", "black");
        Car car2 = new Car(2L, "Mazda", "3", "white");
        Car car3 = new Car(3L, "Hyundai", "IONIQ", "gray");
        cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

    }

    @GetMapping("/car")
    public String getCars(Model model) {
        if(!(cars.isEmpty())) {
            model.addAttribute("cars", cars);
            model.addAttribute("newCar", new Car());
            model.addAttribute("modCar", new Car());
            model.addAttribute("carById", carById);
            model.addAttribute("modId", modId);
            model.addAttribute("delId", delId);
        }
        else {
            cars.add(zeroCar);
            model.addAttribute("cars", cars);
            model.addAttribute("newCar", new Car());
            model.addAttribute("modCar", new Car());
            model.addAttribute("carById", carById);
            model.addAttribute("modId", modId);
            model.addAttribute("delId", delId);
            model.addAttribute("message", "Brak samochodów do wyświetlenia.");
            waitingId.clear();
            freeId=1;
        }
        return "car";
    }

    @PostMapping("/show-car-id")
    public String getCarById(@ModelAttribute Car car) {
        delId = car.getId();
        for(Car tempCar: cars) {
            if (delId == tempCar.getId()) {
                carById.setId(tempCar.getId());
                carById.setMark(tempCar.getMark());
                carById.setModel(tempCar.getModel());
                carById.setColor(tempCar.getColor());
                break;
            }
        }
        return "redirect:/car";
    }

    @PostMapping("/add-car")
    public String addCar(@ModelAttribute Car car) {
        if(waitingId.isEmpty()) {
            car.setId(freeId);
            cars.add(car);
            freeId++;
        }
        else {
            car.setId(waitingId.get(0));
            cars.add(car);
            cars.sort(Comparator.comparing(Car::getId));
            waitingId.remove(0);
        }
        return "redirect:/car";
    }

    @PostMapping("/modify-car")
    public String modifyCar(@ModelAttribute Car car) {
        int a = 0;
        modId = car.getId();
        for(Car tempCar: cars) {
            if(modId == tempCar.getId()) {
                a = cars.indexOf(tempCar);
                break;
            }
        }
        car.setId(modId);
        cars.set(a, car);

        return "redirect:/car";
    }

    @PostMapping("/modify-car-elem")
    public String modifyCarElement(@ModelAttribute Car car) {
        int a = 0;
        Car modCar = new Car();
        modId = car.getId();
        for(Car tempCar: cars) {
            if(modId == tempCar.getId()) {
                a = cars.indexOf(tempCar);
                modCar.setMark(tempCar.getMark());
                modCar.setModel(tempCar.getModel());
                modCar.setColor(tempCar.getColor());
                break;
            }
        }
        if(car.getMark() == "")
            car.setMark(modCar.getMark());
        if(car.getModel() == "")
            car.setModel(modCar.getModel());
        if(car.getColor() == "")
            car.setColor(modCar.getColor());
        car.setId(modId);
        cars.set(a, car);

        return "redirect:/car";
    }

    @PostMapping("/delete-car")
    public String removeCar(@ModelAttribute Car car) {
        delId = car.getId();
        cars.removeIf(tempCar -> (delId == tempCar.getId()));
        if(delId != 0)
            waitingId.add(delId);

        return "redirect:/car";
    }
}
