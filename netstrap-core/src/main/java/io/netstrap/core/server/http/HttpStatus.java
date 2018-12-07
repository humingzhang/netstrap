package io.netstrap.core.server.http;


/**
 * Http状态码
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface HttpStatus {

    // --- 1xx Informational ---

    /**
     * 100 Continue (HTTP/1.1 - RFC 2616)
     */
    int CONTINUE = 100;
    /**
     * 101 Switching Protocols (HTTP/1.1 - RFC 2616)
     */
    int SWITCHING_PROTOCOLS = 101;
    /**
     * 102 Processing (WebDAV - RFC 2518)
     */
    int PROCESSING = 102;

    // --- 2xx Success ---

    /**
     * 200 OK (HTTP/1.0 - RFC 1945)
     */
    int OK = 200;
    /**
     * 201 Created (HTTP/1.0 - RFC 1945)
     */
    int CREATED = 201;
    /**
     * 202 Accepted (HTTP/1.0 - RFC 1945)
     */
    int ACCEPTED = 202;
    /**
     * 203 Non Authoritative Information (HTTP/1.1 - RFC 2616)
     */
    int NON_AUTHORITATIVE_INFORMATION = 203;
    /**
     * 204 No Content (HTTP/1.0 - RFC 1945)
     */
    int NO_CONTENT = 204;
    /**
     * 205 Reset Content (HTTP/1.1 - RFC 2616)
     */
    int RESET_CONTENT = 205;
    /**
     * 206 Partial Content (HTTP/1.1 - RFC 2616)
     */
    int PARTIAL_CONTENT = 206;
    /**
     * 207 Multi-Status (WebDAV - RFC 2518) or 207 Partial Update
     * OK (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)
     */
    int MULTI_STATUS = 207;

    // --- 3xx Redirection ---

    /**
     * 300 Mutliple Choices (HTTP/1.1 - RFC 2616)
     */
    int MULTIPLE_CHOICES = 300;
    /**
     * 301 Moved Permanently (HTTP/1.0 - RFC 1945)
     */
    int MOVED_PERMANENTLY = 301;
    /**
     * 302 Moved Temporarily (Sometimes Found) (HTTP/1.0 - RFC 1945)
     */
    int MOVED_TEMPORARILY = 302;
    /**
     * 303 See Other (HTTP/1.1 - RFC 2616)
     */
    int SEE_OTHER = 303;
    /**
     * 304 Not Modified (HTTP/1.0 - RFC 1945)
     */
    int NOT_MODIFIED = 304;
    /**
     * 305 Use Proxy (HTTP/1.1 - RFC 2616)
     */
    int USE_PROXY = 305;
    /**
     * 307 Temporary Redirect (HTTP/1.1 - RFC 2616)
     */
    int TEMPORARY_REDIRECT = 307;

    // --- 4xx Client Error ---

    /**
     * 400 Bad Request (HTTP/1.1 - RFC 2616)
     */
    int BAD_REQUEST = 400;
    /**
     * 401 Unauthorized (HTTP/1.0 - RFC 1945)
     */
    int UNAUTHORIZED = 401;
    /**
     * 402 Payment Required (HTTP/1.1 - RFC 2616)
     */
    int PAYMENT_REQUIRED = 402;
    /**
     * 403 Forbidden (HTTP/1.0 - RFC 1945)
     */
    int FORBIDDEN = 403;
    /**
     * 404 Not Found (HTTP/1.0 - RFC 1945)
     */
    int NOT_FOUND = 404;
    /**
     * 405 Method Not Allowed (HTTP/1.1 - RFC 2616)
     */
    int METHOD_NOT_ALLOWED = 405;
    /**
     * 406 Not Acceptable (HTTP/1.1 - RFC 2616)
     */
    int NOT_ACCEPTABLE = 406;
    /**
     * 407 Proxy Authentication Required (HTTP/1.1 - RFC 2616)
     */
    int PROXY_AUTHENTICATION_REQUIRED = 407;
    /**
     * 408 Request Timeout (HTTP/1.1 - RFC 2616)
     */
    int REQUEST_TIMEOUT = 408;
    /**
     * 409 Conflict (HTTP/1.1 - RFC 2616)
     */
    int CONFLICT = 409;
    /**
     * 410 Gone (HTTP/1.1 - RFC 2616)
     */
    int GONE = 410;
    /**
     * 411 Length Required (HTTP/1.1 - RFC 2616)
     */
    int LENGTH_REQUIRED = 411;
    /**
     * 412 Precondition Failed (HTTP/1.1 - RFC 2616)
     */
    int PRECONDITION_FAILED = 412;
    /**
     * 413 Request Entity Too Large (HTTP/1.1 - RFC 2616)
     */
    int REQUEST_TOO_LONG = 413;
    /**
     * 414 Request-URI Too Long (HTTP/1.1 - RFC 2616)
     */
    int REQUEST_URI_TOO_LONG = 414;
    /**
     * 415 Unsupported Media Type (HTTP/1.1 - RFC 2616)
     */
    int UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * 416 Requested Range Not Satisfiable (HTTP/1.1 - RFC 2616)
     */
    int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    /**
     * 417 Expectation Failed (HTTP/1.1 - RFC 2616)
     */
    int EXPECTATION_FAILED = 417;

    /**
     * Static constant for a 419 error.
     * 419 Insufficient Space on Resource
     * (WebDAV - draft-ietf-webdav-protocol-05?)
     * or 419 Proxy Reauthentication Required
     * (HTTP/1.1 drafts?)
     */
    int INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    /**
     * Static constant for a 420 error.
     * 420 Method Failure
     * (WebDAV - draft-ietf-webdav-protocol-05?)
     */
    int METHOD_FAILURE = 420;
    /**
     * 422 Unprocessable Entity (WebDAV - RFC 2518)
     */
    int UNPROCESSABLE_ENTITY = 422;
    /**
     * 423 Locked (WebDAV - RFC 2518)
     */
    int LOCKED = 423;
    /**
     * 424 Failed Dependency (WebDAV - RFC 2518)
     */
    int FAILED_DEPENDENCY = 424;

    // --- 5xx Server Error ---

    /**
     * 500 Server Error (HTTP/1.0 - RFC 1945)
     */
    int INTERNAL_SERVER_ERROR = 500;
    /**
     * 501 Not Implemented (HTTP/1.0 - RFC 1945)
     */
    int NOT_IMPLEMENTED = 501;
    /**
     * 502 Bad Gateway (HTTP/1.0 - RFC 1945)
     */
    int BAD_GATEWAY = 502;
    /**
     * 503 Service Unavailable (HTTP/1.0 - RFC 1945)
     */
    int SERVICE_UNAVAILABLE = 503;
    /**
     * 504 Gateway Timeout (HTTP/1.1 - RFC 2616)
     */
    int GATEWAY_TIMEOUT = 504;
    /**
     * 505 HTTP Version Not Supported (HTTP/1.1 - RFC 2616)
     */
    int HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * 507 Insufficient Storage (WebDAV - RFC 2518)
     */
    int INSUFFICIENT_STORAGE = 507;

}
