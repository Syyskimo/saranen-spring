package org.syyskimo.saranen.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.syyskimo.saranen.spring.entities.Person;
import org.syyskimo.saranen.spring.errors.PersonNotFoundException;
import org.syyskimo.saranen.spring.services.PersonService;

import java.util.Optional;

@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Tällä luodaan uusi henkilö. Käytännössä annetaan sukupuoli ja paino.
     *
     * Palautusarvona saadaan luotu olio. Oliolla on oma (generoitu) id, jota käyttämällä
     * voidaan käsitellä ko. oliota
     *
     * @param female onko neito
     * @param weight paino
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(path = "/person/create", produces= MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestParam(name = "female") boolean female,
                        @RequestParam(name = "weight") double weight) {
        Person newPerson = this.personService.addPerson(female, weight);
        return newPerson;
    }

    /**
     * Palauttaa henkilön id:llä.
     *
     * Eli selaimella (tms tavalla) voitaisiin hakea:
     * http://localhost:8080/person/c419567b-8acc-450a-b9d5-9b411ab759d2 tai mikä se id nyt sitten sattuukaan olemaan
     *
     * @param uuid henkilön id
     * @return palauttaa henkilön (tai heittää 404, jos ei löydy moista)
     */
    @CrossOrigin("*")
    @GetMapping(path ="/person/{uuid}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson(@PathVariable String uuid) {
        return this.personService.getPerson(uuid).orElseThrow(() -> new PersonNotFoundException("Unknown person"));
    }

    /**
     * Päivittää henkilön tietoja. Tässä yhdistyy url -fragmentti ja request parametrit (oikeasti käyttäisimme jotain
     * tarkempaa metodia, mutta request toimii mukavasti, koska silloin get-parametrit ovat käytössä, jolloin
     * voimme päivittää asioita pelkällä selaimella, laittamalla parametrit ? (kysymysmerkin) jälkeen & -eroteltuna.
     *
     * @param uuid henkilön id
     * @param female onko neito
     * @param weight paino
     * @return päivitetyn persoonan tahi 404, jos ei moista löydy
     */
    @CrossOrigin("*")
    @GetMapping(path ="/person/{uuid}/update", produces= MediaType.APPLICATION_JSON_VALUE)
    public Person updatePerson(@PathVariable String uuid,
                               @RequestParam(name = "female") boolean female,
                               @RequestParam(name = "weight") double weight) {
        Optional<Person> search = this.personService.getPerson(uuid);
        if (search.isPresent()) {
            Person person = search.get();
            person.setWeight(weight);
            person.setFemale(female);
            return person;
        } else {
            throw new PersonNotFoundException("Unknown person");
        }
    }

    /**
     * Juottaa henkilölle juoman.
     *
     * @param uuid persoonan id
     * @param alcVol alkoholiprosentti
     * @param volume koko (litroissa)
     * @return Palauttaa persoonen juomineen päivineen
     */
    @CrossOrigin("*")
    @GetMapping(path ="/person/{uuid}/drink", produces= MediaType.APPLICATION_JSON_VALUE)
    public Person drink(@PathVariable String uuid,
                               @RequestParam(name = "alcvol") double alcVol,
                               @RequestParam(name = "volume") double volume) {
        Optional<Person> search = this.personService.getPerson(uuid);
        if (search.isPresent()) {
            Person person = search.get();
            person.drink(volume, alcVol);
            return person;
        } else {
            throw new PersonNotFoundException("Unknown person");
        }
    }

    /**
     * Poistaa jo juodun juoman listalta. Mikä sitten syy onkaan: virhepainallus vai itsepetos?
     * API -kehittäjinä emme ota kantaa moiseen
     *
     * @param uuid henkilön id
     * @param drinkId juoman id
     * @return henkilö juomineen päivineen
     */
    @CrossOrigin("*")
    @GetMapping(path ="/person/{uuid}/undrink/{drinkId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Person removeDrink(@PathVariable String uuid,
                              @PathVariable String drinkId) {
        Optional<Person> search = this.personService.getPerson(uuid);
        if (search.isPresent()) {
            Person person = search.get();
            person.removeDrink(drinkId);
            return person;
        } else {
            throw new PersonNotFoundException("Unknown person");
        }
    }



}
