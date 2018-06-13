package co.paperwork.vstream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VideoController {

    private volatile Video video;

    @GetMapping(value = "/{vname}")
    public ResponseEntity<?> getVideo(@PathVariable("vname") String vname) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        try {
            final boolean isExist = getList().parallelStream().anyMatch(v -> {
                if (v.getName().equalsIgnoreCase(vname)) {
                    video = v;
                    return true;
                } else
                    return false;
            });
            if (isExist) {
                final FileInputStream inputStream = new FileInputStream(new File(video.getLocation()));
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int n;
                byte[] data = new byte[16384];

                while ((n = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, n);
                }
                buffer.flush();
                buffer.close();
                httpHeaders.add("Content-Type", "video/mp4");
                return new ResponseEntity<>(buffer.toByteArray(), httpHeaders, HttpStatus.OK);
            } else {
                throw new VstreamException("No video found with a name: " + vname);
            }
        } catch (VstreamException e) {
            httpHeaders.add("Content-Type", "text/html");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            httpHeaders.add("Content-Type", "text/html");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private List<Video> getList() throws IOException {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("list.txt");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final List<Video> videos = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                final Video video = new Video();

                video.setName(line.split("#")[0]);
                video.setLocation(line.split("#")[1]);
                videos.add(video);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return videos;
    }
}
