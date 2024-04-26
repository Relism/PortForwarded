package dev.relism.portforwarded.ngrok;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.installer.NgrokVersion;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import dev.relism.portforwarded.PortForwarded;

public class NgrokHelper {
    public static Tunnel createTunnel(Proto proto, int port) {
        NgrokClient ngrokClient = PortForwarded.getNgrokClient();
        final CreateTunnel minecraftCreateTunnel = new CreateTunnel.Builder()
                .withNgrokVersion(NgrokVersion.V3)
                .withProto(proto)
                .withAddr(port)
                .build();
        return ngrokClient.connect(minecraftCreateTunnel);
    }
}
