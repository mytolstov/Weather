package com.api.weather.controllers;

import com.api.weather.infoW.Weather;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model){
        System.out.println("Bearbeitung main Page!");
        Weather berlin = new Weather("Berlin");
        Weather kyiv = new Weather("Kyiv");
        Weather moscow = new Weather("Moscow");
        Weather barcelona = new Weather("Barcelona");
        Weather ls = new Weather("Los Angeles");
        Weather london = new Weather("London");
        Weather tbilisi = new Weather("Tbilisi");
        Weather tokyo = new Weather("Tokyo");
        Weather sofia = new Weather("Sofia");
        Weather stockholm = new Weather("Stockholm");
        Weather karlsruhe = new Weather("Karlsruhe");

        List<Weather> cities = new ArrayList<Weather>();

        cities.add(berlin);
        cities.add(ls);
        cities.add(kyiv);
        cities.add(moscow);
        cities.add(barcelona);
        cities.add(london);
        cities.add(tbilisi);
        cities.add(tokyo);
        cities.add(sofia);
        cities.add(stockholm);
        cities.add(karlsruhe);

        model.addAttribute("cities",cities);
        return "main";
    }
}

//продолжить
//можно добавлять и удалять города
//подключить к БД
//сделать контейнер
