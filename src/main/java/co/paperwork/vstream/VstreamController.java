package co.paperwork.vstream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Controller
public class VstreamController {

    @GetMapping("1/player")
    public String videoPlayer(Model model) {
        String address;
        try {
            address = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            address = "localhost";
        }
        model.addAttribute("address", address);
        return "player";
    }
}
