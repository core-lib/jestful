package org.qfox.jestful.client;

import org.qfox.jestful.core.annotation.Protocol;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Set;

/**
 * 自动装配处理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-28 9:58
 **/
public class SpringClientProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, InitializingBean {
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private String packages;
    private Client client;
    private boolean singleton = true;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            String[] packages = StringUtils.tokenizeToStringArray(this.packages, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) register(pkg, registry);
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("error occur during classpath scanning", e);
        }
    }

    private void register(String pkg, BeanDefinitionRegistry registry) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = resourcePatternResolver.getClassLoader();
        String pattern = "classpath*:" + pkg.replace('.', '/') + "/*.class";
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        for (Resource resource : resources) {
            MetadataReader reader = this.metadataReaderFactory.getMetadataReader(resource);
            if (!reader.getClassMetadata().isInterface()) continue;
            AnnotationMetadata metadata = reader.getAnnotationMetadata();
            Set<String> annotations = metadata.getAnnotationTypes();
            boolean protocol = false;
            for (String annotation : annotations) protocol = protocol || classLoader.loadClass(annotation).isAnnotationPresent(Protocol.class);
            if (!protocol) continue;
            ScannedGenericBeanDefinition definition = new ScannedGenericBeanDefinition(reader);
            definition.setResource(resource);
            definition.setSource(resource);
            String name = beanNameGenerator.generateBeanName(definition, registry);
            ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(definition);
            definition.setScope(scopeMetadata.getScopeName());
            definition.setBeanClass(SpringClientBean.class);
            String className = reader.getClassMetadata().getClassName();
            Class<?> type = classLoader.loadClass(className);
            definition.getPropertyValues().add("type", type);
            definition.getPropertyValues().add("client", client);
            definition.getPropertyValues().add("singleton", singleton);
            registry.registerBeanDefinition(name, definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (packages == null) throw new IllegalArgumentException("packages can not be null");
        if (client == null) throw new IllegalArgumentException("client can not be null");
    }

    public ResourcePatternResolver getResourcePatternResolver() {
        return resourcePatternResolver;
    }

    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return beanNameGenerator;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public ScopeMetadataResolver getScopeMetadataResolver() {
        return scopeMetadataResolver;
    }

    public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
        this.scopeMetadataResolver = scopeMetadataResolver;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
