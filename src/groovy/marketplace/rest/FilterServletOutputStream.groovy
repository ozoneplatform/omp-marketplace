package marketplace.rest

import javax.servlet.ServletOutputStream

class FilterServletOutputStream extends ServletOutputStream {


    private DataOutputStream stream

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
