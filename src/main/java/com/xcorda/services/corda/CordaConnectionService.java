package com.xcorda.services.corda;

import com.xcorda.services.config.Source;
import com.xcorda.services.config.SourceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CordaConnectionService {
    private final SourceService sourceService;
    private final Map<String, CordaConnection> connections = new HashMap<>();

    public CordaConnectionService(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    public CordaConnection get (String sourceId) {
        if (!connections.containsKey(sourceId)) {
            connections.put(sourceId, create(sourceService.getSource(sourceId)));
        }
        return connections.get(sourceId);
    }

    private CordaConnection create(Source source) {
        return new CordaConnection(source);
    }

    public boolean close (String sourceId) {
        return Optional.ofNullable(connections.remove(sourceId)).map(CordaConnection::close).orElse(true);
    }
}
