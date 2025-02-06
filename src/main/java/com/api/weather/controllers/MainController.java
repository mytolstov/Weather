package com.api.weather.controllers;

import com.api.weather.DB_tabels.Cities;
import com.api.weather.infoW.Weather;
import com.api.weather.repository.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            Weather city = new Weather(c.getTitle(), c.getId());
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
    public String addCity(@RequestParam String title, RedirectAttributes redirectAttributes){
        //получаем новый город
        Weather probeCity = new Weather(title);
        if(probeCity.getTemperature() == null){
            System.out.println("This city does not exist!");
            redirectAttributes.addFlashAttribute("errorMessage", "This city does not exist!");
            return "redirect:/";
        }else {
            Cities newCity = new Cities(probeCity.getCity());
            citiesRepository.save(newCity);
            System.out.println("Successfully Added!");
            redirectAttributes.addFlashAttribute("successMessage", "Successfully Added!");
            return "redirect:/";
        }
    }

    @GetMapping("/remove/{city}")
    public String removeTaskDB(@PathVariable(value = "city") long city, RedirectAttributes redirectAttributes){
        Cities removeCity = citiesRepository.findById(city).orElseThrow();
        System.out.println(removeCity.toString());

        citiesRepository.delete(removeCity);
        System.out.println("City Nr." + removeCity.getId() + " was removed!");
        redirectAttributes.addFlashAttribute("successMessage","Successfully Removed!");
        return "redirect:/";
    }

}


