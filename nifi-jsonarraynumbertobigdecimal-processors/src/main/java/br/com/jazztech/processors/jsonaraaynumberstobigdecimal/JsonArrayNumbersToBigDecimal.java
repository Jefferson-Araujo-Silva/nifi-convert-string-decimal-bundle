/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.jazztech.processors.jsonaraaynumberstobigdecimal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.io.StreamCallback;

@Tags({"jolt", "array", "string", "decimal", "convert", "custom"})
@CapabilityDescription("Inside a JSON Array transforms Strings in numeric formats into numbers. Created to work together with Jolt")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute = "", description = "")})
public class JsonArrayNumbersToBigDecimal extends AbstractProcessor {

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("Success")
            .description("Success relationship")
            .build();

    public static final Relationship FAILURE = new Relationship.Builder()
            .name("Failure")
            .description("Unable to convert")
            .build();
    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        descriptors = new ArrayList<>();
        descriptors = Collections.unmodifiableList(descriptors);

        relationships = new HashSet<>();
        relationships.add(SUCCESS);
        relationships.add(FAILURE);
        relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {

    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) {
        FlowFile flowFile = session.get();

        if (flowFile == null) {
            return;
        }

        final StringBuilder contentBuilder = new StringBuilder();
        session.read(flowFile, inputStream -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    contentBuilder.append(line);
                }
            } catch (IOException e) {
                getLogger().error("Error reading FlowFile content: {}", e.getMessage(), e);
            }
        });

        final String content = contentBuilder.toString();
        final String response = ArrayJsonSplit.ArrayJsonToGestaoLimite(content);

        if (response == null) {
            session.transfer(flowFile, FAILURE);
            return;
        }

        flowFile = session.write(flowFile, new StreamCallback() {
            @Override
            public void process(InputStream in, OutputStream out) throws IOException {
                byte[] dataBytes = response.getBytes(StandardCharsets.UTF_8);
                out.write(dataBytes);
            }
        });

        session.transfer(flowFile, SUCCESS);

        session.commit();
    }

}
