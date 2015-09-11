package marketplace.rest

import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class FilterServletOutputStream extends ServletOutputStream {


    private DataOutputStream stream

    public boolean isReady() {

    }

    public void setWriteListener(WriteListener writeListener) {

    }

    public FilterServletOutputStream(OutputStream output) {
        stream = new DataOutputStream(output)
    }

    @Override
    public void write(int b) {
        stream.write(b)
    }

    @Override
    public void write(byte[] b) {
        stream.write(b)
    }

    @Override
    public void write(byte[] b, int off, int len) {
        stream.write(b , off, len)
    }
}
