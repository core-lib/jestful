package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Multibody;
import org.qfox.jestful.core.Multihead;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月22日 上午10:55:49
 * @since 1.0.0
 */
public class Multipart implements MultipartFile, Part, Cloneable {
    private final Multihead multihead;
    private final Multibody multibody;
    private InputStream inputStream;

    public Multipart(Multihead multihead, Multibody multibody) {
        super();
        this.multihead = multihead;
        this.multibody = multibody;
    }

    public Multihead getMultihead() {
        return multihead;
    }

    public Multibody getMultibody() {
        return multibody;
    }

    public String getName() {
        return multihead.getDisposition() != null ? multihead.getDisposition().getName() : null;
    }

    public String getOriginalFilename() {
        return multihead.getDisposition() != null ? multihead.getDisposition().getFilename() : null;
    }

    public String getContentType() {
        return multihead.getType() != null ? multihead.getType().getName() : null;
    }

    public boolean isEmpty() {
        return getSize() == 0L;
    }

    public long getSize() {
        return multibody.getSize();
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOKit.transfer(getInputStream(), baos);
        return baos.toByteArray();
    }

    public synchronized InputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        } else {
            return inputStream = new FileInputStream(multibody.getFile());
        }
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        IOKit.transfer(getInputStream(), dest);
    }

    @Override
    public Multipart clone() {
        return new Multipart(multihead.clone(), multibody.clone());
    }

    public String getSubmittedFileName() {
        return getOriginalFilename();
    }

    public void write(String fileName) throws IOException {
        transferTo(new File(fileName));
    }

    public void delete() throws IOException {
        if (!multibody.getFile().delete()) throw new IOException("file delete fail");
    }

    public String getHeader(String name) {
        return multihead.getHeader().get(name);
    }

    public Collection<String> getHeaders(String name) {
        return Collections.singletonList(getHeader(name));
    }

    public Collection<String> getHeaderNames() {
        return multihead.getHeader().keySet();
    }

}
