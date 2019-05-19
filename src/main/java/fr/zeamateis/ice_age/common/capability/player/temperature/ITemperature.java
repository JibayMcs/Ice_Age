package fr.zeamateis.ice_age.common.capability.player.temperature;

/**
 * Temperature storage capability
 */
public interface ITemperature {
    void consume(float points);

    void warm(float points);

    void set(float points);

    float getTemperature();

    float getMaxTemperature();

    void synchronize();

}