package br.com.jazztech.processors.jsonaraaynumberstobigdecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayJsonSplit {
    public static String ArrayJsonToGestaoLimite(String arrayJson) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
            objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

            final GestaoLimite[] gestaoLimites = objectMapper.readValue(arrayJson, GestaoLimite[].class);

            String gestaoLimiteNaoFormatada = objectMapper.writeValueAsString(gestaoLimites);

            gestaoLimiteNaoFormatada = gestaoLimiteNaoFormatada.replaceAll("created_at", "dataCriacao");
            gestaoLimiteNaoFormatada = gestaoLimiteNaoFormatada.replaceAll("updated_at", "dataUltimaAlteracao");
            Pattern pattern = Pattern.compile("_(.)");
            Matcher matcher = pattern.matcher(gestaoLimiteNaoFormatada);
            StringBuilder gestaoLimiteFormatada = new StringBuilder();

            int lastIndex = 0;
            while (matcher.find()) {
                gestaoLimiteFormatada.append(gestaoLimiteNaoFormatada.substring(lastIndex, matcher.start()));
                gestaoLimiteFormatada.append(matcher.group(1).toUpperCase());
                lastIndex = matcher.end();
            }
            gestaoLimiteFormatada.append(gestaoLimiteNaoFormatada.substring(lastIndex));
            return gestaoLimiteFormatada.toString();

        } catch (JsonProcessingException jpe) {
            return null;
        }
    }

}
