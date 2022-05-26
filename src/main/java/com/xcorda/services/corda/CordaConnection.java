package com.xcorda.services.corda;

import com.xcorda.services.config.Source;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CordaConnection {
    private final Source source;
    private final AtomicReference<CordaRPCConnection> client = new AtomicReference<>();

    public CordaConnection(Source source) {
        this.source = source;
    }

    private CordaRPCConnection create() {
        final CordaRPCClient client = new CordaRPCClient(new NetworkHostAndPort(source.getHostname(), Integer.parseInt(source.getPort())));
        return client.start(source.getUsername(), source.getPassword());
    }

    private CordaRPCOps ops () {
        return client.updateAndGet(x -> {
            if (x == null) {
                return create();
            }
            return x;
        }).getProxy();
    }


    public List<StateAndRef<ContractState>> list () {
        return ops().vaultQuery(ContractState.class).getStates();
    }

    public boolean close() {
        client.updateAndGet(x -> {
            if (x != null) {
                x.close();
            }
            return null;
        });

        return true;
    }
}
