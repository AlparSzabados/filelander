package org.filelander.multicast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.filelander.config.AppId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MulticastReceiver {
    private final AppId appId;
    public String ipAddress;
    @Getter
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    @Scheduled(fixedRateString = "60000")
    public void onApplicationEvent() {
        if (!enabled.get()) return;
        log.info("Listening...");
        try {
            String received = readDatagramPacket();
            MetaObject metaObject = new ObjectMapper().readValue(received, MetaObject.class);
            log.info("Received Multicast message from Manager: " + metaObject.getId());
            postAgentMeta(constructUrlFromObject(metaObject), getAgentMetaData());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private String readDatagramPacket() throws IOException {
        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ipAddress = "" + packet.getAddress();
        socket.leaveGroup(group);
        socket.close();
        return new String(packet.getData(), 0, packet.getLength());
    }

    private void postAgentMeta(String url, String json) throws IOException {
        log.info("Sending Handshake to Manager...");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        client.execute(httpPost);
        client.close();
    }

    private String getAgentMetaData() throws JsonProcessingException {
        MetaObject metaObject = new MetaObject();
        metaObject.setIpAddress("" + ipAddress);
        metaObject.setPortNumber("8090");
        metaObject.setApi("/api/handshake");
        metaObject.setId(appId.getAppID());
        return new ObjectMapper().writeValueAsString(metaObject);
    }

    private String constructUrlFromObject(MetaObject metaObject) {
        return "http:/" + ipAddress + ":" + metaObject.getPortNumber() + metaObject.getApi();
    }
}
