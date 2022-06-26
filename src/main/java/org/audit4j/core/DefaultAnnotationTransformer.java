/*
 * Copyright (c) 2014-2015 Janith Bandara, This source is a part of
 * Audit4j - An open source auditing framework.
 * http://audit4j.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.audit4j.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.audit4j.core.annotation.Audit;
import org.audit4j.core.annotation.AuditField;
import org.audit4j.core.annotation.DeIdentify;
import org.audit4j.core.annotation.IgnoreAudit;
import org.audit4j.core.dto.AnnotationAuditEvent;
import org.audit4j.core.dto.AuditEvent;
import org.audit4j.core.dto.Field;

/**
 * The Class DefaultAnnotationTransformer use to transform annotation
 * information in to simple audit event.
 *
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 *
 * @since 2.0.0
 */
public class DefaultAnnotationTransformer implements AnnotationTransformer<AuditEvent> {

    private static final String FIELD_THROWN = "thrown";
    private static final String FIELD_RESULT = "result";
    private static final String FIELD_ELAPSED_TIME = "time";
    private static final String FIELD_ARG_PREFIX = "arg";
    private final static String ACTION = "action";

    // Default Fields serializer
    private ObjectSerializer serializer;

    public DefaultAnnotationTransformer() {
        this.serializer = new ObjectToFieldsSerializer();
    }

    public DefaultAnnotationTransformer(ObjectSerializer objectSerializer) {
        this.serializer = objectSerializer;
    }

    /**
     * Transform annotation informations to Audit Event object.
     *
     * @param annotationEvent the annotation event
     * @return the audit event
     * @since 2.0.0
     */
    @Override
    public AuditEvent transformToEvent(AnnotationAuditEvent annotationEvent) {
        AuditEvent event = null;
        Audit audit = null;

        if (annotationEvent.getClazz().isAnnotationPresent(Audit.class)
            && !annotationEvent.getMethod().isAnnotationPresent(IgnoreAudit.class)) {
            audit = annotationEvent.getClazz().getAnnotation(Audit.class);
        } else if (!annotationEvent.getClazz().isAnnotationPresent(Audit.class)
            && annotationEvent.getMethod().isAnnotationPresent(Audit.class)) {
            audit = annotationEvent.getMethod().getAnnotation(Audit.class);
        }

        if (audit != null) {
            event = new AuditEvent();

            // Extract fields and results
            List<Field> fields = new ArrayList<>();
            fields.addAll(getFieldsFromArgs(annotationEvent.getMethod(), annotationEvent.getArgs()));
            fields.addAll(getFieldFromResult(annotationEvent.getMethod(), annotationEvent.getMethodCallResult()));
            fields.addAll(getFieldFromThrowable(annotationEvent.getMethodCallThrowable()));
            if (annotationEvent.getElapsedTime() >= 0) {
                fields.add(new Field(FIELD_ELAPSED_TIME, String.valueOf(annotationEvent.getElapsedTime())));
            }
            event.setFields(fields);

            // Extract Actor
            String annotationAction = audit.action();
            if (ACTION.equals(annotationAction)) {
                event.setAction(annotationEvent.getClazz().getName() + " " + annotationEvent.getMethod().getName());
            } else {
                event.setAction(annotationAction);
            }

            // Extract repository
            event.setRepository(audit.repository());

            event.setActor(annotationEvent.getActor());
            event.setOrigin(annotationEvent.getOrigin());
        }
        return event;
    }

    /**
     * Extract fields based on annotations.
     *
     * @param method : Class method with annotations.
     * @param params : Method parameter values.
     *
     * @return list of fields extracted from method.
     *
     * @since 2.4.1
     */
    private List<Field> getFieldsFromArgs(final Method method, final Object[] params) {
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        final List<Field> fields = new ArrayList<Field>();

        int i = 0;
        for (final Annotation[] annotations : parameterAnnotations) {
            String paramName = null;
            final Object object = params[i++];
            boolean ignoreFlag = false;
            DeIdentify deidentify = null;
            for (final Annotation annotation : annotations) {
                if (annotation instanceof IgnoreAudit) {
                    ignoreFlag = true;
                    break;
                }
                if (annotation instanceof AuditField) {
                    final AuditField field = (AuditField) annotation;
                    paramName = field.field();
                }
                if (annotation instanceof DeIdentify) {
                    deidentify = (DeIdentify) annotation;
                }
            }

            if (!ignoreFlag) {
                if (null == paramName) {
                    paramName = FIELD_ARG_PREFIX + i;
                }
                serializer.serialize(fields, object, paramName, deidentify);
            }
        }
        return fields;
    }

    private List<Field> getFieldFromResult(Method method, Object methodCallResult) {
        if (method.getReturnType().equals(Void.class)) {
            return Collections.emptyList();
        }

        final List<Field> fields = new ArrayList<Field>();
        serializer.serialize(fields, methodCallResult, FIELD_RESULT, null);
        return fields;
    }

    private List<Field> getFieldFromThrowable(Throwable methodCallThrowable) {
        if (methodCallThrowable == null) {
            return Collections.emptyList();
        }

        final List<Field> fields = new ArrayList<Field>();
        serializer.serialize(fields, methodCallThrowable, FIELD_THROWN, null);
        return fields;
    }

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }
}
