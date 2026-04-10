package com.example.guilda.domain.aventura;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ParticipacaoMissaoID implements Serializable {

    private Long missaoId;
    private Long aventureiroId;
}