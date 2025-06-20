package org.coshift.c_adapter.persistence;

import org.coshift.a_domain.Shift;
import org.coshift.d_frameworks.db.ShiftJpaEntity;

public class ShiftMapper {

    public static ShiftJpaEntity toEntity(Shift d) {
        return new ShiftJpaEntity(
                d.getDate(),
                d.getStartTime(),
                d.getEndTime());
    }

    public static Shift toDomain(ShiftJpaEntity e) {
        return new Shift(
                e.getId(),
                e.getDate(),
                e.getStartTime(),
                e.getEndTime());
    }
}
