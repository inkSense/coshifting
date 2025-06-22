package org.coshift.c_adapters.json;

import org.coshift.c_adapters.PersonDto;

import java.util.List;

public interface PersonJsonFileAccessor {

    List<PersonDto> readAll();

    boolean writeAll(List<PersonDto> persons);
}