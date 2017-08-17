package org.qfox.jestful.swagger;

import io.swagger.annotations.*;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.*;
import io.swagger.models.Tag;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.*;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.Property;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangchangpei on 17/6/1.
 */
public class SpringSwaggerScanner {
    private final Pattern pattern = Pattern.compile("\\(.*?\\)");

    public Swagger scan(ApplicationContext applicationContext, MappingRegistry mappingRegistry) {
        Swagger swagger = doReadSwaggerDefinition(applicationContext);
        Map<String, Path> paths = new LinkedHashMap<>();
        Enumeration<Mapping> enumeration = mappingRegistry.enumeration();

        while (enumeration.hasMoreElements()) {
            Mapping mapping = enumeration.nextElement();
            if (!mapping.isAnnotationPresent(ApiOperation.class)) continue;

            String expression = getPathExpression(mapping);
            Path path = paths.get(expression);
            if (path == null) paths.put(expression, path = new Path());
            String method = mapping.getRestful().getMethod();
            Operation operation = new Operation();
            ApiOperation api = mapping.getAnnotation(ApiOperation.class);
            operation.summary(api.value())
                    .tags(Arrays.equals(new String[]{""}, api.tags()) ? new ArrayList<String>() : Arrays.asList(api.tags()))
                    .description(api.notes())
                    .operationId(api.nickname())
                    .schemes(toSchemes(api.protocols()))
                    .consumes(api.consumes().trim().equals("") ? new ArrayList<String>() : Arrays.asList(api.consumes().split("\\s*,\\s*")))
                    .produces(api.produces().trim().equals("") ? new ArrayList<String>() : Arrays.asList(api.produces().split("\\s*,\\s*")))
                    .deprecated(mapping.isAnnotationPresent(Deprecated.class));

            operation.setResponses(toResponse(mapping.getAnnotation(ApiResponses.class)));
            operation.setParameters(toParameters(mapping.getParameters()));

            path.set(method.toLowerCase(), operation);

        }
        swagger.setPaths(paths);
        return swagger;
    }

    private Map<String, Response> toResponse(ApiResponses apiResponses) {
        Map<String, Response> map = new LinkedHashMap<>();

        for (int i = 0; apiResponses != null && i < apiResponses.value().length; i++) {
            ApiResponse apiResponse = apiResponses.value()[i];

            Response response = new Response();
            response.setDescription(apiResponse.message());

            if ("List".equalsIgnoreCase(apiResponse.responseContainer())) {
            }

            map.put(String.valueOf(apiResponse.code()), response);
        }

        return map;
    }

    private Model toModel(Class<?> clazz) {
        try {
            Model model = new ModelImpl();
            ApiModel apiModel = clazz.getAnnotation(ApiModel.class);
            model.setTitle(apiModel.value());
            model.setDescription(apiModel.description());
            model.setReference(apiModel.reference());
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor.getName().equals("class")) continue;
                ApiModelProperty apiModelProperty = descriptor.getReadMethod().getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null && apiModelProperty.hidden()) continue;

            }
            return model;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private Property toProperty(PropertyDescriptor descriptor) {
        return null;
    }

    private List<io.swagger.models.parameters.Parameter> toParameters(Parameters parameters) {
        List<io.swagger.models.parameters.Parameter> list = new ArrayList<>();
        for (Parameter parameter : parameters) {
            io.swagger.models.parameters.Parameter param = null;
            switch (parameter.getPosition()) {
                case Position.UNKNOWN:
                    break;
                case Position.HEADER:
                    param = new HeaderParameter();
                    break;
                case Position.PATH:
                    param = new PathParameter();
                    break;
                case Position.QUERY:
                    param = new QueryParameter();
                    break;
                case Position.BODY:
                    param = new BodyParameter();
                    break;
                case Position.COOKIE:
                    param = new CookieParameter();
                    break;
                case Position.SESSION:
                    break;
            }
            ApiParam api = parameter.getAnnotation(ApiParam.class);
            param.setName(api == null || api.name().trim().equals("") ? parameter.getName() : api.name().trim());
            param.setDescription(api == null ? null : api.value());
            param.setRequired(api != null && api.required());
            param.setAccess(api == null ? null : api.access());
            param.setAllowEmptyValue(api == null ? null : api.allowEmptyValue());
            param.setReadOnly(api == null ? null : api.readOnly());
            if (param instanceof AbstractSerializableParameter<?>) {
                ((AbstractSerializableParameter<?>) param).setType(api == null ? null : api.type());
                ((AbstractSerializableParameter<?>) param).setFormat(api == null ? null : api.format());
                ((AbstractSerializableParameter<?>) param).setCollectionFormat(api == null ? null : api.collectionFormat());
                ((AbstractSerializableParameter<?>) param).setExample(api == null ? null : api.example());

            }
            list.add(param);
        }
        return list;
    }

    private List<Scheme> toSchemes(String text) {
        if (text == null || text.trim().equals("")) return new ArrayList<>();
        String[] protocols = text.split("\\s*,\\s*");
        List<Scheme> schemes = new ArrayList<>();
        for (String protocol : protocols) {
            Scheme scheme = Scheme.forValue(protocol);
            if (scheme != null) schemes.add(scheme);
        }
        return schemes;
    }

    private String getPathExpression(Mapping mapping) {
        String regex = mapping.getRegex();
        Matcher matcher = pattern.matcher(regex);
        int group = 0;
        while (matcher.find()) {
            group++;
            for (Parameter parameter : mapping.getParameters()) {
                if (parameter.getGroup() == group) {
                    regex = regex.replace(matcher.group(), "{" + parameter.getName() + "}");
                    break;
                }
            }
        }
        return regex;
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
