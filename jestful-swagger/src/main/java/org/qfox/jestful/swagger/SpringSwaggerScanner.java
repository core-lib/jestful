package org.qfox.jestful.swagger;

import io.swagger.annotations.Scope;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.*;
import io.swagger.models.auth.*;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * Created by yangchangpei on 17/6/1.
 */
public class SpringSwaggerScanner {

    public Swagger scan(ApplicationContext applicationContext) {
        return doReadSwaggerDefinition(applicationContext);
    }

    private Swagger doReadSwaggerDefinition(ApplicationContext applicationContext) {
        Map<String, Object> definitions = applicationContext.getBeansWithAnnotation(SwaggerDefinition.class);
        if (definitions == null || definitions.isEmpty()) throw new SwaggerDefinitionNotFoundException();
        if (definitions.size() > 1) throw new SwaggerDefinitionNotUniqueException(definitions);
        SwaggerDefinition definition = definitions.values().iterator().next().getClass().getAnnotation(SwaggerDefinition.class);
        Swagger swagger = new Swagger();
        swagger.setInfo(doReadSwaggerDefinitionInfo(definition));
        swagger.setHost(definition.host());
        swagger.setBasePath(definition.basePath());
        swagger.setExternalDocs(doReadSwaggerDefinitionExternalDocs(definition));
        swagger.setTags(doReadSwaggerDefinitionTags(definition));
        swagger.setSchemes(doReadSwaggerDefinitionSchemes(definition));
        swagger.setConsumes(doReadSwaggerDefinitionConsumes(definition));
        swagger.setProduces(doReadSwaggerDefinitionProduces(definition));
        swagger.setSecurity(doReadSwaggerDefinitionSecurity(definition));
        swagger.setSecurityDefinitions(doReadSwaggerDefinitionSecurityDefinitions(definition));
        return swagger;
    }

    private Map<String, SecuritySchemeDefinition> doReadSwaggerDefinitionSecurityDefinitions(SwaggerDefinition definition) {
        Map<String, SecuritySchemeDefinition> map = new LinkedHashMap<>();
        for (io.swagger.annotations.ApiKeyAuthDefinition apiKeyAuthDefinition : definition.securityDefinition().apiKeyAuthDefinitions()) {
            ApiKeyAuthDefinition define = new ApiKeyAuthDefinition();
            define.setName(apiKeyAuthDefinition.name());
            define.setIn(In.forValue(apiKeyAuthDefinition.in().toValue()));
            define.setDescription(apiKeyAuthDefinition.description());
            map.put(apiKeyAuthDefinition.key(), define);
        }
        for (io.swagger.annotations.OAuth2Definition oAuth2Definition : definition.securityDefinition().oAuth2Definitions()) {
            OAuth2Definition define = new OAuth2Definition();
            define.setAuthorizationUrl(oAuth2Definition.authorizationUrl());
            define.setTokenUrl(oAuth2Definition.tokenUrl());
            define.setFlow(oAuth2Definition.flow().name());
            define.setDescription(oAuth2Definition.description());
            Map<String, String> scopes = define.getScopes();
            for (Scope scope : oAuth2Definition.scopes()) scopes.put(scope.name(), scope.description());
            define.setScopes(scopes);
            map.put(oAuth2Definition.key(), define);
        }
        for (io.swagger.annotations.BasicAuthDefinition basicAuthDefinition : definition.securityDefinition().basicAuthDefinitions()) {
            BasicAuthDefinition define = new BasicAuthDefinition();
            define.setDescription(basicAuthDefinition.description());
            map.put(basicAuthDefinition.key(), define);
        }
        return map;
    }

    private List<SecurityRequirement> doReadSwaggerDefinitionSecurity(SwaggerDefinition definition) {
        return new ArrayList<>();
    }

    private List<String> doReadSwaggerDefinitionProduces(SwaggerDefinition definition) {
        if (Arrays.equals(definition.produces(), new String[]{""})) return new ArrayList<>();
        return Arrays.asList(definition.produces());
    }

    private List<String> doReadSwaggerDefinitionConsumes(SwaggerDefinition definition) {
        if (Arrays.equals(definition.consumes(), new String[]{""})) return new ArrayList<>();
        return Arrays.asList(definition.consumes());
    }

    private List<Scheme> doReadSwaggerDefinitionSchemes(SwaggerDefinition definition) {
        List<Scheme> schemes = new ArrayList<>();
        for (SwaggerDefinition.Scheme scheme : definition.schemes()) {
            Scheme s = Scheme.forValue(scheme.name());
            if (s != null) schemes.add(s);
        }
        return schemes;
    }

    private List<Tag> doReadSwaggerDefinitionTags(SwaggerDefinition definition) {
        List<Tag> tags = new ArrayList<>();
        for (io.swagger.annotations.Tag tag : definition.tags()) {
            Tag t = new Tag();
            t.setName(tag.name());
            t.setDescription(tag.description());
            ExternalDocs docs = new ExternalDocs();
            docs.setUrl(tag.externalDocs().url());
            docs.setDescription(tag.externalDocs().value());
            t.setExternalDocs(docs);
            tags.add(t);
        }
        return tags;
    }

    private Info doReadSwaggerDefinitionInfo(SwaggerDefinition definition) {
        Info info = new Info();
        info.setDescription(definition.info().description());
        info.setVersion(definition.info().version());
        info.setTitle(definition.info().title());
        info.setTermsOfService(definition.info().termsOfService());
        info.setContact(doReadSwaggerDefinitionInfoContact(definition));
        info.setLicense(doReadSwaggerDefinitionInfoLicense(definition));
        return info;
    }

    private Contact doReadSwaggerDefinitionInfoContact(SwaggerDefinition definition) {
        Contact contact = new Contact();
        contact.setName(definition.info().contact().name());
        contact.setEmail(definition.info().contact().email());
        contact.setUrl(definition.info().contact().url());
        return contact;
    }

    private License doReadSwaggerDefinitionInfoLicense(SwaggerDefinition definition) {
        License license = new License();
        license.setName(definition.info().license().name());
        license.setUrl(definition.info().license().url());
        return license;
    }

    private ExternalDocs doReadSwaggerDefinitionExternalDocs(SwaggerDefinition definition) {
        ExternalDocs docs = new ExternalDocs();
        docs.setUrl(definition.externalDocs().url());
        docs.setDescription(definition.externalDocs().value());
        return docs;
    }

}
