package org.qfox.jestful.velocity;

/**
 * Created by yangchangpei on 17/5/18_
 */
public class VelocityConfiguration {
    // 模板编码：
    private String inputEncoding = "ISO-8859-1";
    private String outputEncoding = "ISO-8859-1";
    // foreach配置
    private String directiveForeachCounterName = "velocityCount";
    private int directiveForeachCounterInitialValue = 1;
    private String directiveForeachMaxloops = "-1";
    // set配置
    private boolean directiveSetNullAllowed = false;
    // include配置
    private String directiveIncludeOutputErrormsgStart = "<!--include error:";
    private String directiveIncludeOutputErrormsgEnd = "see error log-->";
    // parse配置
    private int directiveParseMaxDepth = 10;
    // 模板加载器配置
    private String resourceLoader = "file";
    private String fileResourceLoaderDescription = "VelocityFileResourceLoader";
    private String fileResourceLoaderClass = "Velocity.Runtime.Resource.Loader.FileResourceLoader";
    private String fileResourceLoaderPath = ".";
    private boolean fileResourceLoaderCache = false;
    private int fileResourceLoaderModificationCheckInterval = 2;
    // 宏配置
    private boolean velocimacroPermissionsAllowInline = true;
    private boolean velocimacroPermissionsAllowInlineToReplaceGlobal = false;
    private boolean velocimacroPermissionsAllowInlineLocalScope = false;
    private boolean velocimacroContextLocalscope = false;
    private int velocimacroMaxDepth = 20;
    private boolean velocimacroArgumentsStrict = false;
    // 资源管理器配置
    private String resourceManagerClass = "Velocity.Runtime.Resource.ResourceManagerImpl";
    private String resourceManagerCacheClass = "Velocity.Runtime.Resource.ResourceCacheImpl";
    // 解析器池配置
    private String parserPoolClass = "Velocity.Runtime.ParserPoolImpl";
    private int parserPoolSize = 40;
    // evaluate配置
    private String directiveEvaluateContextClass = "Velocity.VelocityContext";
    // 可插入introspector配置
    private String runtimeIntrospectorUberspect = "Velocity.Util.Introspection.UberspectImpl";


    public String getInputEncoding() {
        return inputEncoding;
    }

    public void setInputEncoding(String inputEncoding) {
        this.inputEncoding = inputEncoding;
    }

    public String getOutputEncoding() {
        return outputEncoding;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public String getDirectiveForeachCounterName() {
        return directiveForeachCounterName;
    }

    public void setDirectiveForeachCounterName(String directiveForeachCounterName) {
        this.directiveForeachCounterName = directiveForeachCounterName;
    }

    public int getDirectiveForeachCounterInitialValue() {
        return directiveForeachCounterInitialValue;
    }

    public void setDirectiveForeachCounterInitialValue(int directiveForeachCounterInitialValue) {
        this.directiveForeachCounterInitialValue = directiveForeachCounterInitialValue;
    }

    public String getDirectiveForeachMaxloops() {
        return directiveForeachMaxloops;
    }

    public void setDirectiveForeachMaxloops(String directiveForeachMaxloops) {
        this.directiveForeachMaxloops = directiveForeachMaxloops;
    }

    public boolean isDirectiveSetNullAllowed() {
        return directiveSetNullAllowed;
    }

    public void setDirectiveSetNullAllowed(boolean directiveSetNullAllowed) {
        this.directiveSetNullAllowed = directiveSetNullAllowed;
    }

    public String getDirectiveIncludeOutputErrormsgStart() {
        return directiveIncludeOutputErrormsgStart;
    }

    public void setDirectiveIncludeOutputErrormsgStart(String directiveIncludeOutputErrormsgStart) {
        this.directiveIncludeOutputErrormsgStart = directiveIncludeOutputErrormsgStart;
    }

    public String getDirectiveIncludeOutputErrormsgEnd() {
        return directiveIncludeOutputErrormsgEnd;
    }

    public void setDirectiveIncludeOutputErrormsgEnd(String directiveIncludeOutputErrormsgEnd) {
        this.directiveIncludeOutputErrormsgEnd = directiveIncludeOutputErrormsgEnd;
    }

    public int getDirectiveParseMaxDepth() {
        return directiveParseMaxDepth;
    }

    public void setDirectiveParseMaxDepth(int directiveParseMaxDepth) {
        this.directiveParseMaxDepth = directiveParseMaxDepth;
    }

    public String getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(String resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getFileResourceLoaderDescription() {
        return fileResourceLoaderDescription;
    }

    public void setFileResourceLoaderDescription(String fileResourceLoaderDescription) {
        this.fileResourceLoaderDescription = fileResourceLoaderDescription;
    }

    public String getFileResourceLoaderClass() {
        return fileResourceLoaderClass;
    }

    public void setFileResourceLoaderClass(String fileResourceLoaderClass) {
        this.fileResourceLoaderClass = fileResourceLoaderClass;
    }

    public String getFileResourceLoaderPath() {
        return fileResourceLoaderPath;
    }

    public void setFileResourceLoaderPath(String fileResourceLoaderPath) {
        this.fileResourceLoaderPath = fileResourceLoaderPath;
    }

    public boolean isFileResourceLoaderCache() {
        return fileResourceLoaderCache;
    }

    public void setFileResourceLoaderCache(boolean fileResourceLoaderCache) {
        this.fileResourceLoaderCache = fileResourceLoaderCache;
    }

    public int getFileResourceLoaderModificationCheckInterval() {
        return fileResourceLoaderModificationCheckInterval;
    }

    public void setFileResourceLoaderModificationCheckInterval(int fileResourceLoaderModificationCheckInterval) {
        this.fileResourceLoaderModificationCheckInterval = fileResourceLoaderModificationCheckInterval;
    }

    public boolean isVelocimacroPermissionsAllowInline() {
        return velocimacroPermissionsAllowInline;
    }

    public void setVelocimacroPermissionsAllowInline(boolean velocimacroPermissionsAllowInline) {
        this.velocimacroPermissionsAllowInline = velocimacroPermissionsAllowInline;
    }

    public boolean isVelocimacroPermissionsAllowInlineToReplaceGlobal() {
        return velocimacroPermissionsAllowInlineToReplaceGlobal;
    }

    public void setVelocimacroPermissionsAllowInlineToReplaceGlobal(boolean velocimacroPermissionsAllowInlineToReplaceGlobal) {
        this.velocimacroPermissionsAllowInlineToReplaceGlobal = velocimacroPermissionsAllowInlineToReplaceGlobal;
    }

    public boolean isVelocimacroPermissionsAllowInlineLocalScope() {
        return velocimacroPermissionsAllowInlineLocalScope;
    }

    public void setVelocimacroPermissionsAllowInlineLocalScope(boolean velocimacroPermissionsAllowInlineLocalScope) {
        this.velocimacroPermissionsAllowInlineLocalScope = velocimacroPermissionsAllowInlineLocalScope;
    }

    public boolean isVelocimacroContextLocalscope() {
        return velocimacroContextLocalscope;
    }

    public void setVelocimacroContextLocalscope(boolean velocimacroContextLocalscope) {
        this.velocimacroContextLocalscope = velocimacroContextLocalscope;
    }

    public int getVelocimacroMaxDepth() {
        return velocimacroMaxDepth;
    }

    public void setVelocimacroMaxDepth(int velocimacroMaxDepth) {
        this.velocimacroMaxDepth = velocimacroMaxDepth;
    }

    public boolean isVelocimacroArgumentsStrict() {
        return velocimacroArgumentsStrict;
    }

    public void setVelocimacroArgumentsStrict(boolean velocimacroArgumentsStrict) {
        this.velocimacroArgumentsStrict = velocimacroArgumentsStrict;
    }

    public String getResourceManagerClass() {
        return resourceManagerClass;
    }

    public void setResourceManagerClass(String resourceManagerClass) {
        this.resourceManagerClass = resourceManagerClass;
    }

    public String getResourceManagerCacheClass() {
        return resourceManagerCacheClass;
    }

    public void setResourceManagerCacheClass(String resourceManagerCacheClass) {
        this.resourceManagerCacheClass = resourceManagerCacheClass;
    }

    public String getParserPoolClass() {
        return parserPoolClass;
    }

    public void setParserPoolClass(String parserPoolClass) {
        this.parserPoolClass = parserPoolClass;
    }

    public int getParserPoolSize() {
        return parserPoolSize;
    }

    public void setParserPoolSize(int parserPoolSize) {
        this.parserPoolSize = parserPoolSize;
    }

    public String getDirectiveEvaluateContextClass() {
        return directiveEvaluateContextClass;
    }

    public void setDirectiveEvaluateContextClass(String directiveEvaluateContextClass) {
        this.directiveEvaluateContextClass = directiveEvaluateContextClass;
    }

    public String getRuntimeIntrospectorUberspect() {
        return runtimeIntrospectorUberspect;
    }

    public void setRuntimeIntrospectorUberspect(String runtimeIntrospectorUberspect) {
        this.runtimeIntrospectorUberspect = runtimeIntrospectorUberspect;
    }
}
