package org.qfox.jestful.core.http;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Protocol;
import org.qfox.jestful.core.Status;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * <p>
 * Description: 回应状态
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 下午8:23:39
 * @since 1.0.0
 */
public class HttpStatus implements Status {

    public static final Map<Integer, String> SPECIFICATIONS = new TreeMap<Integer, String>();

    // 1xx Continue

    /**
     * {@code 100 Continue}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.1.1">HTTP/1.1</a>
     */
    public static final HttpStatus CONTINUE = new HttpStatus(100, "Continue").addToSpecifications();
    /**
     * {@code 101 Switching Protocols}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.1.2">HTTP/1.1</a>
     */
    public static final HttpStatus SWITCHING_PROTOCOLS = new HttpStatus(101, "Switching Protocols").addToSpecifications();
    /**
     * {@code 102 Processing}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2518#section-10.1">WebDAV</a>
     */
    public static final HttpStatus PROCESSING = new HttpStatus(102, "Processing").addToSpecifications();
    /**
     * {@code 103 Checkpoint}.
     *
     * @see <a href="http://code.google.com/p/gears/wiki/ResumableHttpRequestsProposal">A proposal for supporting
     * resumable POST/PUT HTTP requests in HTTP/1.0</a>
     */
    public static final HttpStatus CHECKPOINT = new HttpStatus(103, "Checkpoint").addToSpecifications();

    // 2xx Success

    /**
     * {@code 200 OK}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.1">HTTP/1.1</a>
     */
    public static final HttpStatus OK = new HttpStatus(200, "OK").addToSpecifications();
    /**
     * {@code 201 Created}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.2">HTTP/1.1</a>
     */
    public static final HttpStatus CREATED = new HttpStatus(201, "Created").addToSpecifications();
    /**
     * {@code 202 Accepted}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.3">HTTP/1.1</a>
     */
    public static final HttpStatus ACCEPTED = new HttpStatus(202, "Accepted").addToSpecifications();
    /**
     * {@code 203 Non-Authoritative Information}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.4">HTTP/1.1</a>
     */
    public static final HttpStatus NON_AUTHORITATIVE_INFORMATION = new HttpStatus(203, "Non-Authoritative Information").addToSpecifications();
    /**
     * {@code 204 No Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.5">HTTP/1.1</a>
     */
    public static final HttpStatus NO_CONTENT = new HttpStatus(204, "No Content").addToSpecifications();
    /**
     * {@code 205 Reset Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.6">HTTP/1.1</a>
     */
    public static final HttpStatus RESET_CONTENT = new HttpStatus(205, "Reset Content").addToSpecifications();
    /**
     * {@code 206 Partial Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.7">HTTP/1.1</a>
     */
    public static final HttpStatus PARTIAL_CONTENT = new HttpStatus(206, "Partial Content").addToSpecifications();
    /**
     * {@code 207 Multi-Status}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-13">WebDAV</a>
     */
    public static final HttpStatus MULTI_STATUS = new HttpStatus(207, "Multi-Status").addToSpecifications();
    /**
     * {@code 208 Already Reported}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc5842#section-7.1">WebDAV Binding Extensions</a>
     */
    public static final HttpStatus ALREADY_REPORTED = new HttpStatus(208, "Already Reported").addToSpecifications();
    /**
     * {@code 226 IM Used}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc3229#section-10.4.1">Delta encoding in HTTP</a>
     */
    public static final HttpStatus IM_USED = new HttpStatus(226, "IM Used").addToSpecifications();

    // 3xx Redirection

    /**
     * {@code 300 Multiple Choices}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.1">HTTP/1.1</a>
     */
    public static final HttpStatus MULTIPLE_CHOICES = new HttpStatus(300, "Multiple Choices").addToSpecifications();
    /**
     * {@code 301 Moved Permanently}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.2">HTTP/1.1</a>
     */
    public static final HttpStatus MOVED_PERMANENTLY = new HttpStatus(301, "Moved Permanently").addToSpecifications();

    /**
     * {@code 302 Found}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.3">HTTP/1.1</a>
     */
    public static final HttpStatus FOUND = new HttpStatus(302, "Found").addToSpecifications();
    /**
     * {@code 303 See Other}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.4">HTTP/1.1</a>
     */
    public static final HttpStatus SEE_OTHER = new HttpStatus(303, "See Other").addToSpecifications();
    /**
     * {@code 304 Not Modified}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.5">HTTP/1.1</a>
     */
    public static final HttpStatus NOT_MODIFIED = new HttpStatus(304, "Not Modified").addToSpecifications();
    /**
     * {@code 305 Use Proxy}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.6">HTTP/1.1</a>
     */
    public static final HttpStatus USE_PROXY = new HttpStatus(305, "Use Proxy").addToSpecifications();
    /**
     * {@code 307 Temporary Redirect}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.8">HTTP/1.1</a>
     */
    public static final HttpStatus TEMPORARY_REDIRECT = new HttpStatus(307, "Temporary Redirect").addToSpecifications();
    /**
     * {@code 308 Resume Incomplete}.
     *
     * @see <a href="http://code.google.com/p/gears/wiki/ResumableHttpRequestsProposal">A proposal for supporting
     * resumable POST/PUT HTTP requests in HTTP/1.0</a>
     */
    public static final HttpStatus RESUME_INCOMPLETE = new HttpStatus(308, "Resume Incomplete").addToSpecifications();

    // --- 4xx Client Error ---

    /**
     * {@code 400 Bad Request}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.1">HTTP/1.1</a>
     */
    public static final HttpStatus BAD_REQUEST = new HttpStatus(400, "Bad Request").addToSpecifications();
    /**
     * {@code 401 Unauthorized}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.2">HTTP/1.1</a>
     */
    public static final HttpStatus UNAUTHORIZED = new HttpStatus(401, "Unauthorized").addToSpecifications();
    /**
     * {@code 402 Payment Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.3">HTTP/1.1</a>
     */
    public static final HttpStatus PAYMENT_REQUIRED = new HttpStatus(402, "Payment Required").addToSpecifications();

    /**
     * {@code 403 Forbidden}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.4">HTTP/1.1</a>
     */
    public static final HttpStatus FORBIDDEN = new HttpStatus(403, "Forbidden").addToSpecifications();
    /**
     * {@code 404 Not Found}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.5">HTTP/1.1</a>
     */
    public static final HttpStatus NOT_FOUND = new HttpStatus(404, "Not Found").addToSpecifications();
    /**
     * {@code 405 Method Not Allowed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.6">HTTP/1.1</a>
     */
    public static final HttpStatus METHOD_NOT_ALLOWED = new HttpStatus(405, "Method Not Allowed").addToSpecifications();
    /**
     * {@code 406 Not Acceptable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.7">HTTP/1.1</a>
     */
    public static final HttpStatus NOT_ACCEPTABLE = new HttpStatus(406, "Not Acceptable").addToSpecifications();
    /**
     * {@code 407 Proxy Authentication Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.8">HTTP/1.1</a>
     */
    public static final HttpStatus PROXY_AUTHENTICATION_REQUIRED = new HttpStatus(407, "Proxy Authentication Required").addToSpecifications();
    /**
     * {@code 408 Request Timeout}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.9">HTTP/1.1</a>
     */
    public static final HttpStatus REQUEST_TIMEOUT = new HttpStatus(408, "Request Timeout").addToSpecifications();
    /**
     * {@code 409 Conflict}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.10">HTTP/1.1</a>
     */
    public static final HttpStatus CONFLICT = new HttpStatus(409, "Conflict").addToSpecifications();
    /**
     * {@code 410 Gone}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.11">HTTP/1.1</a>
     */
    public static final HttpStatus GONE = new HttpStatus(410, "Gone").addToSpecifications();
    /**
     * {@code 411 Length Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.12">HTTP/1.1</a>
     */
    public static final HttpStatus LENGTH_REQUIRED = new HttpStatus(411, "Length Required").addToSpecifications();
    /**
     * {@code 412 Precondition failed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.13">HTTP/1.1</a>
     */
    public static final HttpStatus PRECONDITION_FAILED = new HttpStatus(412, "Precondition Failed").addToSpecifications();
    /**
     * {@code 413 Request Entity Too Large}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.14">HTTP/1.1</a>
     */
    public static final HttpStatus REQUEST_ENTITY_TOO_LARGE = new HttpStatus(413, "Request Entity Too Large").addToSpecifications();
    /**
     * {@code 414 Request-URI Too Long}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.15">HTTP/1.1</a>
     */
    public static final HttpStatus REQUEST_URI_TOO_LONG = new HttpStatus(414, "Request-URI Too Long").addToSpecifications();
    /**
     * {@code 415 Unsupported Media Type}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.16">HTTP/1.1</a>
     */
    public static final HttpStatus UNSUPPORTED_MEDIA_TYPE = new HttpStatus(415, "Unsupported Media Type").addToSpecifications();
    /**
     * {@code 416 Requested Range Not Satisfiable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.17">HTTP/1.1</a>
     */
    public static final HttpStatus REQUESTED_RANGE_NOT_SATISFIABLE = new HttpStatus(416, "Requested range not satisfiable").addToSpecifications();
    /**
     * {@code 417 Expectation Failed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.18">HTTP/1.1</a>
     */
    public static final HttpStatus EXPECTATION_FAILED = new HttpStatus(417, "Expectation Failed").addToSpecifications();
    /**
     * {@code 418 I'm a teapot}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2324#section-2.3.2">HTCPCP/1.0</a>
     */
    public static final HttpStatus I_AM_A_TEAPOT = new HttpStatus(418, "I'm a teapot").addToSpecifications();
    /**
     * {@code 422 Unprocessable Entity}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
     */
    public static final HttpStatus UNPROCESSABLE_ENTITY = new HttpStatus(422, "Unprocessable Entity").addToSpecifications();
    /**
     * {@code 423 Locked}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.3">WebDAV</a>
     */
    public static final HttpStatus LOCKED = new HttpStatus(423, "Locked").addToSpecifications();
    /**
     * {@code 424 Failed Dependency}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.4">WebDAV</a>
     */
    public static final HttpStatus FAILED_DEPENDENCY = new HttpStatus(424, "Failed Dependency").addToSpecifications();
    /**
     * {@code 426 Upgrade Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2817#section-6">Upgrading to TLS Within HTTP/1.1</a>
     */
    public static final HttpStatus UPGRADE_REQUIRED = new HttpStatus(426, "Upgrade Required").addToSpecifications();
    /**
     * {@code 428 Precondition Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-3">Additional HTTP Status Codes</a>
     */
    public static final HttpStatus PRECONDITION_REQUIRED = new HttpStatus(428, "Precondition Required").addToSpecifications();
    /**
     * {@code 429 Too Many Requests}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-4">Additional HTTP Status Codes</a>
     */
    public static final HttpStatus TOO_MANY_REQUESTS = new HttpStatus(429, "Too Many Requests").addToSpecifications();
    /**
     * {@code 431 Request Header Fields Too Large}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-5">Additional HTTP Status Codes</a>
     */
    public static final HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = new HttpStatus(431, "Request Header Fields Too Large").addToSpecifications();

    // --- 5xx Server Error ---

    /**
     * {@code 500 Internal Server Error}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.1">HTTP/1.1</a>
     */
    public static final HttpStatus INTERNAL_SERVER_ERROR = new HttpStatus(500, "Internal Server Error").addToSpecifications();
    /**
     * {@code 501 Not Implemented}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.2">HTTP/1.1</a>
     */
    public static final HttpStatus NOT_IMPLEMENTED = new HttpStatus(501, "Not Implemented").addToSpecifications();
    /**
     * {@code 502 Bad Gateway}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.3">HTTP/1.1</a>
     */
    public static final HttpStatus BAD_GATEWAY = new HttpStatus(502, "Bad Gateway").addToSpecifications();
    /**
     * {@code 503 Service Unavailable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.4">HTTP/1.1</a>
     */
    public static final HttpStatus SERVICE_UNAVAILABLE = new HttpStatus(503, "Service Unavailable").addToSpecifications();
    /**
     * {@code 504 Gateway Timeout}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.5">HTTP/1.1</a>
     */
    public static final HttpStatus GATEWAY_TIMEOUT = new HttpStatus(504, "Gateway Timeout").addToSpecifications();
    /**
     * {@code 505 HTTP Version Not Supported}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.6">HTTP/1.1</a>
     */
    public static final HttpStatus HTTP_VERSION_NOT_SUPPORTED = new HttpStatus(505, "HTTP Version not supported").addToSpecifications();
    /**
     * {@code 506 Variant Also Negotiates}
     *
     * @see <a href="http://tools.ietf.org/html/rfc2295#section-8.1">Transparent Content Negotiation</a>
     */
    public static final HttpStatus VARIANT_ALSO_NEGOTIATES = new HttpStatus(506, "Variant Also Negotiates").addToSpecifications();
    /**
     * {@code 507 Insufficient Storage}
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.5">WebDAV</a>
     */
    public static final HttpStatus INSUFFICIENT_STORAGE = new HttpStatus(507, "Insufficient Storage").addToSpecifications();
    /**
     * {@code 508 Loop Detected}
     *
     * @see <a href="http://tools.ietf.org/html/rfc5842#section-7.2">WebDAV Binding Extensions</a>
     */
    public static final HttpStatus LOOP_DETECTED = new HttpStatus(508, "Loop Detected").addToSpecifications();
    /**
     * {@code 509 Bandwidth Limit Exceeded}
     */
    public static final HttpStatus BANDWIDTH_LIMIT_EXCEEDED = new HttpStatus(509, "Bandwidth Limit Exceeded").addToSpecifications();
    /**
     * {@code 510 Not Extended}
     *
     * @see <a href="http://tools.ietf.org/html/rfc2774#section-7">HTTP Extension Framework</a>
     */
    public static final HttpStatus NOT_EXTENDED = new HttpStatus(510, "Not Extended").addToSpecifications();
    /**
     * {@code 511 Network Authentication Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-6">Additional HTTP Status Codes</a>
     */
    public static final HttpStatus NETWORK_AUTHENTICATION_REQUIRED = new HttpStatus(511, "Network Authentication Required").addToSpecifications();

    private final HttpProtocol protocol;
    private final int code;
    private final String reason;

    public HttpStatus(int code, String reason) {
        this(HttpProtocol.valueOf("HTTP/1.1"), code, reason);
    }

    public HttpStatus(HttpProtocol protocol, int code, String reason) {
        if (protocol == null) throw new IllegalArgumentException("http protocol must not be null");
        if (code < 0) throw new IllegalArgumentException("http status code " + code + " < 0");
        this.protocol = protocol;
        this.code = code;
        this.reason = reason != null ? reason : SPECIFICATIONS.get(code);
    }

    public static HttpStatus valueOf(String status) {
        if (StringKit.isEmpty(status)) throw new IllegalArgumentException("http status line must not be null or empty");
        try {
            StringTokenizer tokenizer = new StringTokenizer(status, " ");
            String protocol = tokenizer.nextToken();
            String code = tokenizer.nextToken();
            StringBuilder reason = new StringBuilder();
            while (tokenizer.hasMoreTokens()) reason.append(tokenizer.nextToken()).append(tokenizer.hasMoreTokens() ? " " : "");
            return new HttpStatus(HttpProtocol.valueOf(protocol), Integer.valueOf(code), reason.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("malformed http status line:" + status, e);
        }
    }

    private HttpStatus addToSpecifications() {
        SPECIFICATIONS.put(code, reason);
        return this;
    }

    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpStatus that = (HttpStatus) o;

        return protocol.equals(that.protocol) && code == that.code;
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + code;
        return result;
    }

    @Override
    public String toString() {
        return protocol + " " + code + (StringKit.isBlank(reason) ? "" : " " + reason);
    }
}
