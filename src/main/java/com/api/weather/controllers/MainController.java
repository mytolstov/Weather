package com.api.weather.controllers;

import ch.qos.logback.core.util.StringUtil;
import com.api.weather.DB_tabels.Cities;
import com.api.weather.infoW.Weather;
import com.api.weather.repository.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CitiesRepository citiesRepository;

    @GetMapping("/")
    public String mainPage(Model model){
        System.out.println("Bearbeitung main Page!");
        Iterable<Cities> cities = citiesRepository.findAll();
        List<Weather> citiesEnd = new ArrayList<Weather>();
        for(Cities c:cities){
            System.out.println(c.getTitle());
            Weather city = new Weather(c.getTitle());
            citiesEnd.add(city);
        }
        model.addAttribute("cities",citiesEnd);
        return "main";
    }


    @GetMapping("/add_city")
    private String addingPage(Model model){
        System.out.println("Beardeitund adding Page!");

        return "add_city";
    }

    @PostMapping("/add_city")
    public String addCity(@RequestParam String title, Model model){
        //получаем новый город
        Weather probeCity = new Weather(title);
        if(probeCity.getTemperature() == null){
            System.out.println("This city does not exist!");
            return "redirect:/";
        }else {
            Cities newCity = new Cities(probeCity.getCity());
            citiesRepository.save(newCity);
            System.out.println("Successfully Added!");
            return "redirect:/";
        }
    }

    @GetMapping("/remove/{city}")
    public String removeTaskDB(@PathVariable(value = "city") String city, Model model){
//        Task removeTask = taskRepository.findById(id).orElseThrow();
//        taskRepository.delete(removeTask);
        Cities findSity = new Cities(city);
        //обрывается
        Cities removeCity = citiesRepository.findByTitle(city);

        citiesRepository.delete(removeCity);
        System.out.println("City Nr." + removeCity.getId() + " was removed!");
        return "redirect:/";
    }

}

//продолжить
//можно добавлять и удалять города
//подключить к БД
//сделать контейнер
