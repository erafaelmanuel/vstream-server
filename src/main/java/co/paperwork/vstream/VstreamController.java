package co.paperwork.vstream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Controller
public class VstreamController {

    @GetMapping("get/player")
    public String videoPlayer(@RequestParam("video") String video, Model model) {
        String host;
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            host = "localhost";
        }
        model.addAttribute("host", host);
        model.addAttribute("video", video);
        return "player";
    }
}
