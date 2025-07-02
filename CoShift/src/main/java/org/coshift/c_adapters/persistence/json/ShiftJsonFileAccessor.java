package org.coshift.c_adapters.persistence.json;

import java.util.List;

import org.coshift.c_adapters.dto.ShiftDto;

/**
 * Minimaler Datei-Port für das Lesen und Schreiben des kompletten
 * Schicht-Bestands als JSON-DTOs.
 *
 *  – Keine Such- oder Filterlogik<br>
 *  – Keine Abhängigkeit zu konkreten JSON-Bibliotheken
 */
public interface ShiftJsonFileAccessor {

    /**
     * Liest den gesamten Speicherinhalt und liefert ihn als Liste
     * von {@link ShiftDto}.
     */
    List<ShiftDto> readAll();

    /**
     * Überschreibt den kompletten Speicherinhalt mit der übergebenen Liste.
     *
     * @param shifts vollständiger neuer Datenbestand
     * @return {@code true}, wenn das Schreiben erfolgreich war,
     *         sonst {@code false}
     */
    boolean writeAll(List<ShiftDto> shifts);
}