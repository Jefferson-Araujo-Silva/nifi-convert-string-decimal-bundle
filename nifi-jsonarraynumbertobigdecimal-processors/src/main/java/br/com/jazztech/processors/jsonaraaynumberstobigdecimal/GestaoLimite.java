package br.com.jazztech.processors.jsonaraaynumberstobigdecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GestaoLimite {

    @JsonProperty("unidade_organizacional_id")
    private Integer unidadeOrganizacionalId;

    @JsonProperty("gestao_limites_id")
    private UUID gestaoLimitesId;

    @JsonProperty("limite_total")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal limiteTotal;

    @JsonProperty("limite_disponivel")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal limiteDisponivel;

    @JsonProperty("limite_utilizado")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal limiteUtilizado;

    @JsonProperty("created_at")
    private String dataCriacao;

    @JsonProperty("updated_at")
    private String dataUltimaAlteracao;

    @Override
    public String toString() {
        return "{" +
                "unidadeOrganizacionalId=" + unidadeOrganizacionalId +
                ", gestaoLimitesId=" + gestaoLimitesId +
                ", limiteTotal=" + limiteTotal +
                ", limiteDisponivel=" + limiteDisponivel +
                ", limiteUtilizado=" + limiteUtilizado +
                ", dataCriacao='" + dataCriacao + '\'' +
                ", dataUltimaAlteracao='" + dataUltimaAlteracao + '\'' +
                '}';
    }
}
