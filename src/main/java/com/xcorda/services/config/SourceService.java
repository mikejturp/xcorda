package com.xcorda.services.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SourceService {
    private final ObjectMapper mapper;
    private final String location;
    private final AtomicReference<Map<String, Source>> sources = new AtomicReference<>();

    @Autowired
    public SourceService(ObjectMapper mapper, @Value("${xcorda.sources.location:config/sources.json}") String location) {
        this.mapper = mapper;
        this.location = location;
    }

    public Map<String, Source> getSources() {
        return sources.updateAndGet(v -> {
            if (v == null) {
                try (InputStream inputStream = Files.newInputStream(Paths.get(location))) {
                    return new HashMap<>(mapper.readValue(inputStream, new TypeReference<Map<String, Source>>() {
                    }));
                } catch (NoSuchFileException e) {
                    return new HashMap<>();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return v;
        });
    }

    public Source getSource (String id) {
        return getSources().get(id);
    }
    public void addSource(String id, Source source) {
        getSources().put(id, source);
        saveSources();
    }

    private void saveSources() {
        final File file = new File(location);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(location))) {
            mapper.writeValue(outputStream, getSources());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeSource(String id) {
        getSources().remove(id);
        saveSources();
    }
}
