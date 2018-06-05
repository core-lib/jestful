package org.qfox.jestful.server.formatting;

import org.qfox.jestful.server.JestfulServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangchangpei on 17/8/22.
 */
public class MultipartServletRequest extends URLEncodedServletRequest implements MultipartHttpServletRequest {
    private final List<Multipart> multiparts;

    public MultipartServletRequest(JestfulServletRequest request, Map<String, String[]> parameters, List<Multipart> multiparts) {
        super(request, parameters);
        this.multiparts = multiparts;
    }

    public Iterator<String> getFileNames() {
        Set<String> names = new LinkedHashSet<String>();
        for (Multipart multipart : multiparts) names.add(multipart.getName());
        return names.iterator();
    }

    public MultipartFile getFile(String name) {
        List<MultipartFile> files = getFiles(name);
        return files.isEmpty() ? null : files.get(0);
    }

    public List<MultipartFile> getFiles(String name) {
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        for (Multipart part : multiparts) if (name == null ? part.getName() == null : name.equals(part.getName())) files.add(part);
        return files;
    }

    public Map<String, MultipartFile> getFileMap() {
        Map<String, MultipartFile> map = new LinkedHashMap<String, MultipartFile>();
        Iterator<String> iterator = getFileNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            MultipartFile value = getFile(name);
            map.put(name, value);
        }
        return map;
    }

    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        MultiValueMap<String, MultipartFile> map = new LinkedMultiValueMap<String, MultipartFile>();
        Iterator<String> iterator = getFileNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            List<MultipartFile> value = getFiles(name);
            map.put(name, value);
        }
        return map;
    }

    public String getMultipartContentType(String name) {
        HttpHeaders headers = getMultipartHeaders(name);
        MediaType mediaType = headers != null ? headers.getContentType() : null;
        return mediaType != null ? mediaType.toString() : null;
    }

    public HttpMethod getRequestMethod() {
        String method = getMethod().toUpperCase();
        return HttpMethod.valueOf(method);
    }

    public HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String[] keys = getHeaderKeys();
        for (String key : keys) {
            String[] values = getRequestHeaders(key);
            headers.put(key, Arrays.asList(values));
        }
        return headers;
    }

    public HttpHeaders getMultipartHeaders(String name) {
        Multipart multipart = (Multipart) getFile(name);
        if (multipart == null) return null;
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> header = multipart.getMultihead().getHeader();
        for (Map.Entry<String, String> entry : header.entrySet()) headers.add(entry.getKey(), entry.getValue());
        return headers;
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return new ArrayList<Part>(multiparts);
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        for (Part part : multiparts) if (name == null ? part.getName() == null : name.equals(part.getName())) return part;
        return null;
    }
}
