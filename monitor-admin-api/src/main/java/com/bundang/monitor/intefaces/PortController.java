package com.bundang.monitor.intefaces;

import com.bundang.monitor.application.PortService;
import com.bundang.monitor.domain.Port;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
public class PortController {
    @Autowired
    private PortService portService;


    @GetMapping("/ports")
    public List<Port> list() {
        List<Port> ports = portService.getPorts();

        return ports;
    }

//    @GetMapping("/ports")
//    public List<Port> list(@RequestParam("number") Integer number ) {
//        List<Port> ports =
//                portService.getPorts(number);
//        return ports;
//    }


    @GetMapping("/ports/{id}")
    public Port detail(@PathVariable("id") Long id) {
        Port port = portService.getPort(id);
        return port;
    }

    @PostMapping("/ports")
    public ResponseEntity<?> create(@Valid @RequestBody Port resource)
            throws URISyntaxException {
        Port port = portService.addPort(
                Port.builder()
                        .number(resource.getNumber())
                        .state(resource.getState())
                        .build()
        );

        URI location = new URI("/posts/" + port.getId());
        return ResponseEntity.created(location).body("{}");
    }

    @PatchMapping("/posts")
    public String bulkCreate(@RequestBody List<Port> Ports) {
        portService.bulkCreate(Ports);

        return "";
    }

    @PatchMapping("/posts/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @RequestBody Port resource) {

        Integer number = resource.getNumber();
        String state = resource.getState();

        portService.updatePort(id, number, state);

        return "{}";
    }

    @DeleteMapping
    public String delete(@PathVariable("id") Long id) {
        portService.deactivePort(id);

        return "{}";
    }

}
