package br.com.jazztech.processors.jsonaraaynumberstobigdecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

public class MoneySerializer extends JsonSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setParseBigDecimal(true);
            String valor = value.setScale(2, RoundingMode.HALF_UP).toString();
            try {
                BigDecimal big = (BigDecimal) df.parse(valor);
                jgen.writeNumber(big);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

}
