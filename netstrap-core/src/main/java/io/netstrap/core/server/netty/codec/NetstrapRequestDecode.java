package io.netstrap.core.server.netty.codec;

import io.netty.handler.codec.http.HttpRequestDecoder;

/**
 * @author minghu.zhang
 * @date 2018/11/10
 */
public class NetstrapRequestDecode extends HttpRequestDecoder {

    public NetstrapRequestDecode() {
        super();
    }

    public NetstrapRequestDecode(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize);
    }

    public NetstrapRequestDecode(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
    }

    public NetstrapRequestDecode(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize);
    }
}
