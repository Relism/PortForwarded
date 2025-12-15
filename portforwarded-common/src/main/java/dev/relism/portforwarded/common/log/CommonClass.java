package dev.relism.portforwarded.common.log;

import dev.relism.portforwarded.common.AbstractLogger;
import dev.relism.portforwarded.gwts.client.PortForwardedClient;
import dev.relism.portforwarded.gwts.client.PortForwardedClientConfig;
import dev.relism.portforwarded.gwts.client.Tunnel;
import dev.relism.portforwarded.gwts.client.events.TunnelState;
import dev.relism.portforwarded.gwts.client.events.TunnelStateChangeEvent;
import dev.relism.portforwarded.gwts.client.logging.ClientLogger;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CommonClass {

    private final String instanceReference;
    private final PortForwardedClient client;
    private final ClientLogger logger;

    private final String localHost;
    private final int localPort;

    private final Executor executor;
    private final AtomicBoolean started = new AtomicBoolean(false);

    private final int maxAttempts;
    private final long retryDelayMillis;

    public CommonClass(String instanceReference, AbstractLogger logger, String serverHost, int serverPort) {
        this(instanceReference, logger, serverHost, serverPort, 10, 2000);
    }

    public CommonClass(
            String instanceReference,
            AbstractLogger logger,
            String serverHost,
            int serverPort,
            int maxAttempts,
            long retryDelayMillis
    ) {
        this.instanceReference = Objects.requireNonNull(instanceReference, "instanceReference");
        this.logger = ClientLoggerFactory.fromAbstractLogger(Objects.requireNonNull(logger, "logger"));

        this.localHost = Objects.requireNonNull(serverHost, "serverHost");
        this.localPort = serverPort;

        this.maxAttempts = Math.max(1, maxAttempts);
        this.retryDelayMillis = Math.max(250, retryDelayMillis);

        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "portforwarded-" + this.instanceReference);
            t.setDaemon(true);
            return t;
        });

        this.client = PortForwardedClient.create(
                PortForwardedClientConfig.builder()
                        .serverHost("portforwarded.relism.dev")
                        .serverPort(7000)
                        .localHost(this.localHost)
                        .localPort(this.localPort)
                        .logger(this.logger)
                        .build()
        );

        listenToEvents();
    }

    // tunnels in a non-blocking way
    public void initialize() {
        if (!started.compareAndSet(false, true)) {
            logger.warn("PortForwarded already initialized (instance: {}). Ignoring duplicate init.", instanceReference);
            return;
        }

        logger.info(
                "PortForwarded starting (env: {}, local: {}:{})",
                instanceReference, localHost, localPort
        );

        startTunnelingAsync();
    }

    private void startTunnelingAsync() {
        CompletableFuture
                .runAsync(this::connectAndOpenWithRetry, executor)
                .exceptionally(ex -> {
                    logger.error("PortForwarded crashed unexpectedly (env: {}).", instanceReference, ex);
                    return null;
                });
    }

    private void connectAndOpenWithRetry() {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                logger.info("Connecting to tunnel server (attempt {}/{})...", attempt, maxAttempts);

                client.connect().join();

                logger.info("Connected. Opening tunnel for {}:{} ...", localHost, localPort);

                Tunnel tunnel = client.openTunnel().join();

                logger.info(
                        "Tunnel ACTIVE. Public address: {}",
                        tunnel.hostname()
                );
                return;

            } catch (Throwable t) {
                logger.warn(
                        "Tunnel setup failed (attempt {}/{}). Will retry in {} ms. Reason: {}",
                        attempt, maxAttempts, retryDelayMillis, rootMessage(t)
                );

                sleepSilently(retryDelayMillis);
            }
        }

        logger.error(
                "Unable to establish tunnel after {} attempts. Giving up (env: {}, local: {}:{}).",
                maxAttempts, instanceReference, localHost, localPort
        );
    }

    private void listenToEvents() {
        client.addTunnelListener(this::tunnelStateChangeEventHandler);
    }

    private void tunnelStateChangeEventHandler(TunnelStateChangeEvent event) {
        Tunnel tunnel = event.getTunnel();
        String host = safeHostname(tunnel);

        TunnelState state = event.getState();
        switch (state) {
            case CREATING -> logger.info("Tunnel state: CREATING (public: {}).", host);
            case CLOSING -> logger.warn("Tunnel state: CLOSING (public: {}).", host);
            case CLOSED  -> logger.warn("Tunnel state: CLOSED (public: {}).", host);
            case FAILED  -> logger.error("Tunnel state: FAILED (public: {}).", host);
            default      -> logger.debug("Tunnel state: {} (public: {}).", state, host);
        }
    }

    private static String safeHostname(Tunnel tunnel) {
        try {
            if (tunnel == null) return "<unknown>";
            String h = tunnel.hostname();
            return (h == null || h.isBlank()) ? "<unknown>" : h;
        } catch (Throwable ignored) {
            return "<unknown>";
        }
    }

    private static void sleepSilently(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static String rootMessage(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null) cur = cur.getCause();
        String msg = cur.getMessage();
        return (msg == null || msg.isBlank()) ? cur.getClass().getSimpleName() : msg;
    }

    public String getInstanceReference() {
        return instanceReference;
    }
}