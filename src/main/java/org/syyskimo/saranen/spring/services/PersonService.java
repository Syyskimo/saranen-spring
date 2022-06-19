package org.syyskimo.saranen.spring.services;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.syyskimo.saranen.spring.entities.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@ApplicationScope
public class PersonService {
    private final Map<String, Person> repository;

    public PersonService() {
        this.repository = new HashMap<>();
    }

    public Person addPerson(boolean female, double weight) {
        Person person = new Person(female, weight);
        repository.put(person.getUUID(), person);
        return person;
    }

    public Optional<Person> getPerson(String uuid)
    {
        return Optional.ofNullable(this.repository.get(uuid));
    }

}
