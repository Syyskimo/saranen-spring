package org.syyskimo.saranen.spring.entities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tämä luokka kuvaa henkilöä. Luokka on abstrakti (abstract), joka tarkoittaa ettei tästä luokasta voi tehdä ilmentymiä
 * (eli olioita) vaan tämä luokka tulee periä (extends) jollain ei-abstraktilla luokalla, jotta voidaan tehdä ilmentymiä.
 *
 * Henkilöllö on tiettyjä ominaisuuksia (properties, eli luokan muuttujat):
 * weight, paino kiloina
 * female, boolean eli tosi/epätosi (true/false) -tyyppinen tieto joka kertoo onko henkilö nainen vai ei (eli mies)
 * uuid, yksilöllinen tunniste
 *
 * Lisäksi luokilla on metodeja. Nämä metodit ovat käytännössä luokan (tai oikeastaan olion, jos eivät ole staattisia)
 * funktioita.
 *
 */
public class Person {
    private double weight;
    private boolean female;

    private String uuid;

    private Map<String, Drink> drinks;

    /**
     * Tämä on luokan vakio. Vakio tarkoittaa arvoa jota ei voi muuttaa. Ja avainsana "static" taas määrittelee, että
     * kyseessä luokan - ei olion - tieto. Tämä tarkoittaa, että sitä käyttääkseen ei tarvitse olla oliota luotuna. Ja
     * jos kyseessä olisi muuttuja (eli ei olisi final -vakio), niin muuttujia olisi vain yksi käytössä, sen sijaan että
     * jokaisella luodulla oliolla olisi omansa. Vrt. propertyt (eli perusmuuttajat oliossa): propertyt ovat vain käytössä
     * kun olio on luotu ja ne on kiinnitetty juuri siihen olioon.
     *
     * Tämä kyseinen luokkavakio kertoo laskuissa suhdeluvun veden määrässä naisilla (eli naislla 55% massasta on vettä)
     */
    public static final double WATER_RATIO_FEMALE = 0.55;
    /**
     * Tämä kyseinen luokkavakio kertoo laskuissa suhdeluvun veden määrässä miehillä (eli miehillä 68% massasta on vettä)
     */
    public static final double WATER_RATIO_MALE = 0.68;
    /**
     * Tämä vakio kertoo alkoholin "poltto" nopeuden. Eli monta promillea tunnissa poistuu alkoholia kehosta.
     * Esimerkiksi: 1 promillen alkoholin kokonaan poistuminen kehosta vie vähän alta kuusi tuntia
     */
    public static final double ALC_DECAY_PER_HOUR = 0.17;

    /**
     * Luokan konstruktori. Tämä tarkoittaa erikoisfunktiota (eli metodia) jota kutsutaan silloin kun luodaan uusi
     * uusi (new) instanssi (olio) luokasta.
     *
     * @param female onko kyseinen henkilö nainen. Jos ei, niin oletetaan että mies
     * @param weight paino (kilogrammoina)
     */
    public Person(boolean female, double weight) {
        this.female = female;
        this.weight = weight;
        this.drinks = new HashMap<>();
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * @return palauttaa henkilön yksilöllisen tunnisteen merkkijonona (String)
     */
    public String getUUID() {
        return this.uuid.toString();
    }

    /**
     * @return kertoo kuinka monta Drinkkiä on juotu
     */
    public int howManyIDrank() {
        return this.drinks.size();
    }
    /**
     * @return Palauttaa juotujen ravintola-annosten määrän (jossa 1 ravintola-annos on 1 pieni keskiolut)
     */
    public double getShotAmount() {
        double shotAmount = 0.0;

        for(Drink drink: this.drinks.values()) {
            shotAmount += drink.getShotAmount();
        }
        return shotAmount;

    }

    /**
     *
     * @return Laskee yhteen ja palauttaa kaikkien juotujen juomien sisältämän raa'an alkoholin määrän
     */
    public double getConsumedAlc() {
        double alcAmount = 0.0;

        for(Drink drink: this.drinks.values()) {
            alcAmount += drink.getAlc();
        }
        return alcAmount;
    }

    /**
     *
     * @param hours montako tuntia sitten juominen on aloitettu
     * @return promillemäärä
     */
    public double getPromile(double hours) {
        return (this.getConsumedAlc() / this.getWaterAmount() * 1000.0) - (ALC_DECAY_PER_HOUR * hours);

    }

    /**
     * Leikkisä metodi joka palauttaa promillen hieman mukavammin tulkittuna tekstuaalisesti
     *
     * @param hours montako tuntia sitten juominen on aloitettu
     * @return Promillemäärä tekstuaalisena (pyöristetty kahteen desimaaliin ja laitettu "%." perään)
     */
    public String getPoliceValue(double hours) {
        return  String.format("%.2f", this.getPromile(hours)) + "%.";
    }

    /**
     * "Juo" juoman, eli käytännössä lisää olion sisäiseen listaan
     *
     * @param drink Juotava juoma
     */
    public void drink(Drink drink) {
        this.drinks.put(drink.getId(), drink);
    }

    /**
     * Lisää "lennosta" lisätyn juoman
     * @param volume kuinka paljon litroissa
     * @param alcVol mikä alkoholiprosentti
     */
    public void drink(double volume, double alcVol) {
        Drink drink = new Drink(volume, alcVol);
        this.drinks.put(drink.getId(), drink);
    }

    /**
     * @return Palauttaa veden määrän kehossä (kiloina/litroina, sama asia)
     */
    public double getWaterAmount()
    {
        return this.weight * this.getWaterRatio();
    }

    /**
     * @return Palauttaa veden suhdeluvun (eli montako prosenttia henkilön massasta on vettä)
     */
    public double getWaterRatio()
    {
        return (this.female ? WATER_RATIO_FEMALE : WATER_RATIO_MALE);

    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public String getGender()
    {
        return (this.female ? "female" : "male");
    }

    public double getWeight()
    {
        return this.weight;
    }

    /**
     * Palauttaa listan juomista järjestettynä juomisajankohdan mukaan
     *
     * Tässä hyväksi käytetään Javan streamejä (joskin vain pienesti) kts:
     * https://stackify.com/streams-guide-java-8/
     *
     * @return listan juomista järjestettynä juomisajankohdan mukaan
     */
    public List<Drink> getDrinks()
    {
        return this.drinks.values().stream().sorted().collect(Collectors.toList());
    }

    public long getDrinkingSeconds() {
        List<Drink> drinkList = this.getDrinks();
        return (drinkList.size() > 0 ? drinkList.get(0).getSecondsSince() : 0);
    }

    public double getPromile() {
        return this.getPromile(getDrinkingSeconds() / (3600.0));
    }

    public void removeDrink(String drinkUuid) {
        this.drinks.remove(drinkUuid);
    }


}
