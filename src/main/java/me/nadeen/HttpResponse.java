package me.nadeen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    
    List<String> lines = new ArrayList<>();

    public HttpResponse(BufferedReader reader) throws Exception {
        String line;

        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
    }

    public void send(DataOutputStream stream) throws Exception {
        for (String line : lines) {
            stream.writeBytes(line + "\r\n");
            stream.flush();
        }
        stream.writeBytes("\r\n");
        stream.flush();
    }
}
